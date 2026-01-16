package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.data.InvoiceApplyDAO;
import kr.co.finger.shinhandamoa.data.InvoiceApplyVF;
import kr.co.finger.shinhandamoa.data.InvoiceApplyVO;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDF;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDO;
import org.apache.commons.collections4.ListUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 고지서 신청서 DO 저장소
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class InvoiceApplyRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceApplyRepository.class);

    @Autowired
    private InvoiceApplyDAO invoiceApplyDAO;

    /**
     * 고지서 신청서 조회
     *
     * @param notiMasReqCd 고지서 신청서 번호
     * @return
     */
    public InvoiceApplyDO get(String notiMasReqCd) {
        final InvoiceApplyVO item = invoiceApplyDAO.get(notiMasReqCd);

        if(item == null) {
            return null;
        }

        return new InvoiceApplyDO(item);
    }

    /**
     * 고지서 신청서 목록 중 첫번째 객체 조회
     *
     * @param filter
     * @return
     */
    public InvoiceApplyDO getFirst(InvoiceApplyDF filter) {
        final List<InvoiceApplyVO> itemList = invoiceApplyDAO.selectForUpdate(filter.toValueFilter(), new RowBounds(0, 1));
        if(ListUtils.defaultIfNull(itemList, Collections.emptyList()).size() == 0) {
            return null;
        }

        final InvoiceApplyVO item = itemList.get(0);

        return new InvoiceApplyDO(item);
    }

    /**
     * 고지서 신청서 업데이트
     *
     * @param object
     */
    public void update(InvoiceApplyDO object) {
        final InvoiceApplyVO vo = object.getVO();

        invoiceApplyDAO.update(vo);
    }

    public List<InvoiceApplyDO> find(InvoiceApplyDF filter, RowBounds rowBounds) {
        final InvoiceApplyVF invoiceApplyVF = filter.toValueFilter();

        return invoiceApplyDAO.select(invoiceApplyVF, rowBounds).stream()
                .map(invoiceApplyVO -> new InvoiceApplyDO(invoiceApplyVO))
                .sorted(Comparator.comparing(InvoiceApplyDO::getId))
                .collect(Collectors.toList());
    }

    public List<InvoiceApplyDO> findForUpdate(InvoiceApplyDF filter, RowBounds rowBounds) {
        final InvoiceApplyVF invoiceApplyVF = filter.toValueFilter();

        return invoiceApplyDAO.selectForUpdate(invoiceApplyVF, rowBounds).stream()
                .map(invoiceApplyVO -> new InvoiceApplyDO(invoiceApplyVO))
                .sorted(Comparator.comparing(InvoiceApplyDO::getId))
                .collect(Collectors.toList());
    }
}
