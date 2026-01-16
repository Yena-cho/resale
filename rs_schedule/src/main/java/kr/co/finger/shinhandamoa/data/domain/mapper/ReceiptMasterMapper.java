package kr.co.finger.shinhandamoa.data.domain.mapper;

import kr.co.finger.shinhandamoa.data.domain.model.Statistics;
import kr.co.finger.shinhandamoa.data.domain.model.XrcpdetVO;
import kr.co.finger.shinhandamoa.data.domain.model.XrcpmasVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReceiptMasterMapper {
    Statistics aggregate(@Param("chaCd") String chaCd, @Param("svecdList") List<String> sveCdList, @Param("rcpmasstList") List<String> rcpMasStList, @Param("fromDate") String fromDate, @Param("toDate") String toDate);

    List<XrcpdetVO> getDistinctAdjfiregkey(@Param("rcpmascdList") List<String> rcpmascdList);

    List<XrcpmasVO> getRcpmasWithoutDet(@Param("rcpmascdList") List<String> rcpmascdList);
}
