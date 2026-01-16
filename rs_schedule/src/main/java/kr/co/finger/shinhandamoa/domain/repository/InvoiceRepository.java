package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.data.InvoiceDAO;
import kr.co.finger.shinhandamoa.data.InvoiceVF;
import kr.co.finger.shinhandamoa.data.InvoiceVO;
import kr.co.finger.shinhandamoa.domain.model.InvoiceDF;
import kr.co.finger.shinhandamoa.domain.model.InvoiceDO;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 청구서 저장소
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class InvoiceRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceRepository.class);

    @Autowired
    private InvoiceDAO invoiceDAO;

    /**
     * 목록 조회
     *
     * @return
     */
    public List<InvoiceDO> find(InvoiceDF filter) {
        final InvoiceVF vf = filter.toValueFilter();
        final List<InvoiceVO> itemList = invoiceDAO.select(vf, new RowBounds());
        final List<InvoiceDO> doList = itemList.stream()
                .map(invoiceVO -> new InvoiceDO(invoiceVO))
                .sorted(Comparator.comparing(InvoiceDO::getSeqNo)).collect(Collectors.toList());

        return doList;
    }
}
