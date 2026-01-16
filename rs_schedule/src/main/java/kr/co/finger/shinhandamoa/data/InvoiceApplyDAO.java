package kr.co.finger.shinhandamoa.data;

import kr.co.finger.shinhandamoa.data.domain.mapper.InvoiceApplyMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XnotimasreqMapper;
import kr.co.finger.shinhandamoa.data.table.model.Xnotimasreq;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 고지서 신청서 DAO
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class InvoiceApplyDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceApplyDAO.class);

    @Autowired
    private InvoiceApplyMapper invoiceApplyMapper;

    @Autowired
    private XnotimasreqMapper xnotimasreqMapper;

    /**
     * 고지서 신청서 목록 조회
     *
     * @param filter
     * @param rowBounds
     * @return
     */
    public List<InvoiceApplyVO> select(InvoiceApplyVF filter, RowBounds rowBounds) {
        List<InvoiceApplyVO> itemList = invoiceApplyMapper.select(filter, rowBounds);
        return itemList;
    }

    /**
     * 고지서 신청서 목록 조회
     *
     * @param filter
     * @param rowBounds
     * @return
     */
    public List<InvoiceApplyVO> selectForUpdate(InvoiceApplyVF filter, RowBounds rowBounds) {
        List<InvoiceApplyVO> itemList = invoiceApplyMapper.selectForUpdate(filter, rowBounds);
        return itemList;
    }

    /**
     * 고지서 신청서 조회
     *
     * @param notiMasReqCd
     * @return
     */
    public InvoiceApplyVO get(String notiMasReqCd) {
        return invoiceApplyMapper.get(notiMasReqCd);
    }

    public void update(InvoiceApplyVO vo) {
        final Xnotimasreq xnotimasreq = vo.getXnotimasreq();
        xnotimasreqMapper.updateByPrimaryKey(xnotimasreq);
    }

    public int count(InvoiceApplyVF filter) {
        return invoiceApplyMapper.count(filter);
    }
}
