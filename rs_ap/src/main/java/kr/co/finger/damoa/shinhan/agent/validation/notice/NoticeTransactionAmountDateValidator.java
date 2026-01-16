package kr.co.finger.damoa.shinhan.agent.validation.notice;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.domain.model.*;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;
import kr.co.finger.damoa.shinhan.agent.validation.Validator;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 금액/기간 검증
 *
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class NoticeTransactionAmountDateValidator implements Validator {
    private final static Logger LOGGER = LoggerFactory.getLogger(NoticeTransactionAmountDateValidator.class);

    @Override
    @Deprecated
    public ValidationResult validate(List<NotiMas> notiMasList, ChacdInfo chacdInfo, MsgIF msgIF) {
        throw new RuntimeException("폐기된 인터페이스");
    }

    @Override
    public ValidationResult validate(MsgIF requestInterface, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        final NoticeMessage noticeMessage = (NoticeMessage) requestInterface;

        // 기관코드
        final String corpCode = cha.getId();
        // 거래금액
        final long transactionAmount = NumberUtils.toLong(StringUtils.trim(noticeMessage.getTransactionAmount()));

        final long sumamt = NumberUtils.toLong(limitMap.get("sumamt").toString());
        final long sumcnt = NumberUtils.toLong(limitMap.get("sumcnt").toString());

        LOGGER.debug("거래금액: {}", transactionAmount);
        LOGGER.debug("기관정보[{}]: {}", corpCode, cha);

        if (cha.getAmtchkty().equals("N")) {
            // 금액, 체크하지 않을 때.
            LOGGER.info("입금방식 SET {}", cha.getAmtchkty());
            return SUCCESS;
        }

        // 한도방식일 경우
        if(cha.getAmtchkty().equals("L")) {
            // 1회 한도가 0이 아니고 거래금액보다 크거나 같은경우
            if(!cha.getLimitOnce().equals("0") && Long.parseLong(cha.getLimitOnce()) < transactionAmount){
                LOGGER.info("가상계좌 이체한도 1회 한도 초과 {} < {}", cha.getLimitOnce(), transactionAmount);
                return new ValidationResult("V404", "수취계좌입금한도초과");
            }

            // 1일 한도가 0이 아니고 거래일 총금액+거래금액보다 크거나 같은경우
            if(!cha.getLimitDay().equals("0") && Long.parseLong(cha.getLimitDay()) < transactionAmount+sumamt){
                LOGGER.info("가상계좌 이체한도 1일 한도 초과 {} < {}", cha.getLimitDay(), transactionAmount);
                return new ValidationResult("V404", "수취계좌입금한도초과");
            }

            // 1일 횟수가 0이 아니고 거래일 총횟수+1 보다 크거나 같은경우
            if(!cha.getLimitDayCnt().equals("0") && Long.parseLong(cha.getLimitDayCnt()) < sumcnt+1){
                LOGGER.info("가상계좌 이체한도 1일 횟수 초과 {} < {}", cha.getLimitDayCnt(), sumcnt+1);
                return new ValidationResult("V404", "수취계좌입금한도초과");
            }
            return SUCCESS;
        }

        // 금액체크시
        LOGGER.info("거래금액: {}", transactionAmount);
        final List<NoticeMasterDO> selectedNoticeMasterList = ListUtils.select(noticeMasterList, each -> each.matchesByAmount(cha, transactionAmount));
        
        if(selectedNoticeMasterList.isEmpty()) {
            LOGGER.info("납부금액 일치하는 청구원장 없음");
            return new ValidationResult("V407", "납부금액다름");
        }

        final List<NoticeMasterDO> validNoticeMasterList = ListUtils.select(selectedNoticeMasterList, each -> each.matchesByDate(cha, new Date()));

        if(validNoticeMasterList.isEmpty()) {
            LOGGER.info("납부기간 일치하는 청구원장 없음");
            return new ValidationResult("V859", "수납기간이 아닙니다");
        }
        
        return SUCCESS;
    }

    /**
     * 입금 가능 금액 목록 생성
     *
     * @param cha 기관
     * @param noticeMaster 청구원장
     * @return
     */
    private List<Long> generateRemainList(ChaDO cha, NoticeMasterDO noticeMaster) {
        List<Long> remainList;
        if (cha.isEnablePartialPayment()) {
            final Predicate<NoticeDetailsDO> predicate = notiDet -> {
                PayItemDO payItem = notiDet.getPayItem();

                return payItem.isRequired();
            };
            final List<NoticeDetailsDO> detailsList = noticeMaster.getDetailsList();
            final List<NoticeDetailsDO> requiredList = ListUtils.select(detailsList, predicate);
            final List<NoticeDetailsDO> optionalList = ListUtils.selectRejected(detailsList, predicate);

            // 필수 총액
            long requiredSum = 0;
            for (NoticeDetailsDO eachDetails : requiredList) {
                requiredSum += eachDetails.getRemainAmount();
            }

            // 옵션 목록
            final List<Long> optionalSumList = generateOptionalSumList(optionalList);
            remainList = new ArrayList<>();
            for (Long eachOptionalSum : optionalSumList) {
                remainList.add(requiredSum + eachOptionalSum.longValue());
            }
        } else {
            remainList = Arrays.asList(noticeMaster.getRemainAmount());
        }
        return remainList;
    }

    private List<Long> generateOptionalSumList(List<NoticeDetailsDO> optionalList) {
        if(optionalList == null || optionalList.isEmpty()) {
            return new ArrayList<>();
        }
        final RealMatrix calculator = createMatrix(optionalList.size());
        final RealMatrix source = MatrixUtils.createRealMatrix(optionalList.size(), 1);
        for (int i = 0; i < optionalList.size(); i++) {
            source.setEntry(i, 0, optionalList.get(i).getRemainAmount());
        }
        final RealMatrix resultMatrix = calculator.multiply(source);
        final List<Long> resultList = new ArrayList<>();
        for (int i = 0; i < calculator.getRowDimension(); i++) {
            resultList.add((long) resultMatrix.getEntry(i, 0));
        }
        return resultList;
    }

    /**
     *
     * @param count
     * @return
     */
    private RealMatrix createMatrix(int count) {
        final int column = count;
        final int row = 1 << column;
        final RealMatrix matrix = MatrixUtils.createRealMatrix(row, column);

        for (int i = 0; i < row; i++) {
            final String maze = StringUtils.reverse(StringUtils.leftPad(Integer.toBinaryString(i), column, '0'));
            for (int j = 0; j < maze.length(); j++) {
                int value = NumberUtils.toInt(StringUtils.substring(maze, j, j + 1), 0);
                LOGGER.trace("value: {}", value);
                matrix.setEntry(i, j, value);
            }
            LOGGER.info("{}", maze);
        }

        return matrix;
    }
}
