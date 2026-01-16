package kr.co.finger.shinhandamoa.data;

import kr.co.finger.shinhandamoa.data.domain.mapper.InvoiceApplyMapper;
import kr.co.finger.shinhandamoa.data.domain.mapper.InvoiceMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XnotimasreqMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XnotimasreqdetMapper;
import kr.co.finger.shinhandamoa.data.table.model.Xnotimasreq;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 고지서 DAO
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class InvoiceDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceDAO.class);

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private XnotimasreqdetMapper xnotimasreqdetMapper;

    public List<InvoiceVO> select(InvoiceVF filter, RowBounds rowBounds) {
        return invoiceMapper.select(filter,  rowBounds);
    }
}
