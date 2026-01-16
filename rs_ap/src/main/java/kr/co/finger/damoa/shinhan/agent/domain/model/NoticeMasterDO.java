package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xnotimas;
import kr.co.finger.shinhandamoa.data.table.model.Xrcpmas;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 청구원장 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class NoticeMasterDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeMasterDO.class);
    
    private final Xnotimas xnotimas;
    private List<NoticeDetailsDO> noticeDetailsList;

    public NoticeMasterDO(Xnotimas xnotimas) {
        this.xnotimas = xnotimas;
        this.noticeDetailsList = new ArrayList<>();
    }

    public void addDetails(NoticeDetailsDO noticeDetails) {
        noticeDetailsList.add(noticeDetails);
    }
    
    public long getNoticeAmount() {
        long sum = 0;
        for (NoticeDetailsDO each : noticeDetailsList) {
            sum += each.getNoticeAmount();
        }

        return sum;
    }
    
    public long getRemainAmount() {
        long sum = 0;
        for (NoticeDetailsDO each : noticeDetailsList) {
            sum += each.getRemainAmount();
        }
        
        return sum;
    }
    
    public String getId() {
        return xnotimas.getNotimascd();
    }

    public List<NoticeDetailsDO> getDetailsList() {
        return Collections.unmodifiableList(noticeDetailsList);
    }

    public String getCustomerName() {
        return xnotimas.getCusname();
    }

    public Date getStartDate() {
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(StringUtils.defaultString(xnotimas.getStartdate()));
        } catch (ParseException e) {
            // never occur
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }
            return new Date();
        }
    }

    public Date getEndDate() {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(StringUtils.defaultString(xnotimas.getEnddate() + "235959"));
        } catch (ParseException e) {
            // never occur
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }
            return new Date();
        }
    }
    
    public boolean matchesByAmount(ChaDO cha, long amount) {
        final List<Pair<Long, List<NoticeDetailsDO>>> remainList = this.generateRemainList(cha);

        // 입금 가능 여부 확인
        LOGGER.debug("[청구원장#{}] 납부가능금액: {}", this.getId(), remainList);
        // 금액 조건 맞는 청구
        for (Pair<Long, List<NoticeDetailsDO>> eachRemain : remainList) {
            if (amount != eachRemain.getLeft()) {
                LOGGER.debug("납부금액 상이");
                continue;
            }

            LOGGER.info("납부금액 일치: {}", eachRemain);
            return true;
        }

        return false;
    }

    /**
     * 입금 가능 금액 목록 생성
     *
     * @param cha 기관
     * @return
     */
    public List<Pair<Long, List<NoticeDetailsDO>>> generateRemainList(ChaDO cha) {
        List<Pair<Long, List<NoticeDetailsDO>>> remainList;
        if (cha.isEnablePartialPayment()) {
            final Predicate<NoticeDetailsDO> predicate = notiDet -> {
                final PayItemDO payItem = notiDet.getPayItem();

                boolean required = payItem.isRequired();
                boolean isSelected = notiDet.isSelected();

                return required || isSelected;
            };
            
            final List<NoticeDetailsDO> detailsList = this.getDetailsList();
            final List<NoticeDetailsDO> requiredList = ListUtils.select(detailsList, predicate);
            final List<NoticeDetailsDO> optionalList = ListUtils.selectRejected(detailsList, predicate);

            // 필수 총액
            long requiredSum = 0;
            for (NoticeDetailsDO eachDetails : requiredList) {
                requiredSum += eachDetails.getRemainAmount();
            }

            remainList = new ArrayList<>();
            
            // 옵션 목록
            final List<Pair<Long, List<NoticeDetailsDO>>> optionalSumList = generateOptionalSumList(optionalList);
            for (Pair<Long, List<NoticeDetailsDO>> eachOptionalSum : optionalSumList) {
                remainList.add(new ImmutablePair<>(Long.valueOf(requiredSum + eachOptionalSum.getLeft().longValue()), ListUtils.sum(requiredList, eachOptionalSum.getRight())));
            }
            
            if(remainList.isEmpty()) {
                remainList.add(new ImmutablePair<>(this.getRemainAmount(), this.getDetailsList()));
            }
        } else {
            remainList = Arrays.asList(new ImmutablePair<>(this.getRemainAmount(), this.getDetailsList()));
        }
        
        return remainList;
    }

    private List<Pair<Long, List<NoticeDetailsDO>>> generateOptionalSumList(List<NoticeDetailsDO> optionalList) {
        if(optionalList == null || optionalList.isEmpty()) {
            return new ArrayList<>();
        }
        
        final RealMatrix calculator = createMatrix(optionalList.size());
        final RealMatrix source = MatrixUtils.createRealMatrix(optionalList.size(), 1);
        for (int i = 0; i < optionalList.size(); i++) {
            source.setEntry(i, 0, optionalList.get(i).getRemainAmount());
        }
        final RealMatrix resultMatrix = calculator.multiply(source);
        final List<Pair<Long, List<NoticeDetailsDO>>> resultList = new ArrayList<>();
        for (int i = 0; i < calculator.getRowDimension(); i++) {
            ArrayList<NoticeDetailsDO> noticeDetailsList = new ArrayList<>();
            double[] row = calculator.getRow(i);
            for(int eachRow=0; eachRow<row.length; eachRow++) {
                int selected = (int) row[eachRow];
                if(selected != 1) {
                    continue;
                }

                noticeDetailsList.add(optionalList.get(eachRow));
            }

            resultList.add(new ImmutablePair<>(Long.valueOf((long) resultMatrix.getEntry(i, 0)), noticeDetailsList));
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
    
    public boolean matchesByDate(ChaDO cha, Date date) {
        if(!cha.isEnableValidatePeriod()) {
            return true;
        }

        if(date.before(this.getStartDate())) {
            return false;
        }

        if(date.after(this.getEndDate())) {
            return false;
        }

        return true;
    }

    public ReceiptMasterDO createReceiptMaster(long transactionAmount) {
        if(transactionAmount == this.getRemainAmount()) {
            this.setStatus("PA03");
        } else if(transactionAmount < this.getRemainAmount()) {
            this.setStatus("PA04");
        } else if(transactionAmount > this.getRemainAmount()) {
            this.setStatus("PA05");
        }
        
        // 수납원장
        final Xrcpmas xrcpmas = new Xrcpmas();
        xrcpmas.setNotimascd(xnotimas.getNotimascd());
        xrcpmas.setChacd(xnotimas.getChacd());
        xrcpmas.setVano(xnotimas.getVano());
        xrcpmas.setMaskey(xnotimas.getMaskey());
        xrcpmas.setMasmonth(xnotimas.getMasmonth());
        xrcpmas.setMasday(xnotimas.getMasday());
        xrcpmas.setCuskey(xnotimas.getCuskey());
        xrcpmas.setCusgubn1(xnotimas.getCusgubn1());
        xrcpmas.setCusgubn2(xnotimas.getCusgubn2());
        xrcpmas.setCusgubn3(xnotimas.getCusgubn3());
        xrcpmas.setCusgubn4(xnotimas.getCusgubn4());
        xrcpmas.setCusname(xnotimas.getCusname());
        xrcpmas.setCushp(xnotimas.getCushp());
        xrcpmas.setCusmail(xnotimas.getCusmail());
        xrcpmas.setSmsyn(xnotimas.getSmsyn());
        xrcpmas.setMailyn(xnotimas.getMailyn());
        xrcpmas.setCusoffno(xnotimas.getCusoffno());
        xrcpmas.setRcpamt(transactionAmount);
        ReceiptMasterDO receiptMasterDO = new ReceiptMasterDO(xrcpmas);
        
        // 수납원장 상세
        long remainTransactionAmount = transactionAmount;
        for(int i=0; i<noticeDetailsList.size();) {
            NoticeDetailsDO each = noticeDetailsList.get(i);
            i++;

            if(each.getRemainAmount() < 1) {
                continue;
            }
            
            long detailsAmount = Math.min(remainTransactionAmount, each.getRemainAmount());
            if (remainTransactionAmount > 0){
                if(noticeDetailsList.size() == i) {
                    // 마지막 건이면
                    detailsAmount = remainTransactionAmount;
                }

                remainTransactionAmount -= detailsAmount;

                ReceiptDetailsDO receiptDetails = each.createReceiptDetails(detailsAmount);
                receiptMasterDO.addDetails(receiptDetails);
            }
        }
        
        return receiptMasterDO;
    }

    private void setStatus(String status) {
        this.xnotimas.setNotimasst(status);
    }

    public NoticeMasterDO getFilterForPartialPayment(ChaDO cha, long amount) {
        final List<Pair<Long, List<NoticeDetailsDO>>> remainList = this.generateRemainList(cha);

        // 입금 가능 여부 확인
        LOGGER.debug("[청구원장#{}] 납부가능금액: {}", this.getId(), remainList);
        // 금액 조건 맞는 청구
        int i=0;
        for (; i<remainList.size(); i++) {
            Pair<Long, List<NoticeDetailsDO>> eachRemain = remainList.get(i);
            if (amount != eachRemain.getLeft()) {
                LOGGER.debug("납부금액 상이");
                continue;
            }

            NoticeMasterDO noticeMaster = new NoticeMasterDO(this.xnotimas);
            noticeMaster.noticeDetailsList = eachRemain.getRight(); 
            return noticeMaster;
        }
        
        // never occur
        throw new RuntimeException();
    }

    public Xnotimas getXnotimas() {
        return xnotimas;
    }
    
    public long getPaymentAmount() {
        long sum = 0;
        for (NoticeDetailsDO each : noticeDetailsList) {
            sum += each.getPaymentAmount();
        }

        return sum;
    }

    public void refreshStatus() {
        if (this.isCanceled()) {
            this.setStatus("PA06");
            return;
        }
        
        if(this.getRemainAmount() == this.getNoticeAmount()) {
            this.setStatus("PA02");
            return;
        }
        
        if(this.getRemainAmount() == 0L) {
            this.setStatus("PA03");
            return;
        }
        
        if(this.getPaymentAmount() > this.getNoticeAmount()) {
            this.setStatus("PA05");
            return;
        }
        
        if(this.getPaymentAmount() < this.getNoticeAmount()) {
            this.setStatus("PA04");
            return;
        }
    }

    private boolean isCanceled() {
        return StringUtils.equals(xnotimas.getNotiCanYn(), "Y");
    }
}
