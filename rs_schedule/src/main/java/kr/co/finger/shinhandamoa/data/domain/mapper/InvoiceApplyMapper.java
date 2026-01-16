package kr.co.finger.shinhandamoa.data.domain.mapper;

import kr.co.finger.shinhandamoa.data.InvoiceApplyVF;
import kr.co.finger.shinhandamoa.data.InvoiceApplyVO;
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
public interface InvoiceApplyMapper {
    List<InvoiceApplyVO> select(@Param("filter") InvoiceApplyVF filter, RowBounds rowBounds);

    InvoiceApplyVO get(String notiMasReqCd);

    List<InvoiceApplyVO> selectForUpdate(@Param("filter") InvoiceApplyVF filter, RowBounds rowBounds);

    int count(@Param("filter") InvoiceApplyVF filter);
}
