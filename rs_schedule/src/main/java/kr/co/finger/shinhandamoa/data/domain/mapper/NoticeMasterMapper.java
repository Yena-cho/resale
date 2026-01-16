package kr.co.finger.shinhandamoa.data.domain.mapper;

import kr.co.finger.shinhandamoa.data.domain.model.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NoticeMasterMapper {
    Statistics aggregate(@Param("chaCd") String chaCd, @Param("stateList") List<String> stateList, @Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("cancelYn") String cancelYn);
}
