package kr.co.finger.shinhandamoa.data.domain.mapper;

import kr.co.finger.shinhandamoa.data.InvoiceVF;
import kr.co.finger.shinhandamoa.data.InvoiceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 고지서 신청서 매퍼
 *
 * @author wisehouse@finger.co.kr
 */
@Mapper
@Repository
public interface InvoiceMapper {
    List<InvoiceVO> select(@Param("filter") InvoiceVF filter, RowBounds rowBounds);

    InvoiceVO get();
}
