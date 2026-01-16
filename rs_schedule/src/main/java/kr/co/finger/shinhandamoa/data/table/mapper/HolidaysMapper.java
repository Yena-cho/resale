package kr.co.finger.shinhandamoa.data.table.mapper;

import kr.co.finger.shinhandamoa.data.table.model.Holidays;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HolidaysMapper {
    // 특정 연도의 공휴일 목록 조회
    List<Holidays> getHolidaysByYear(int year);

    // 공휴일 삽입
    void insertHoliday(Holidays holiday);

    // 공휴일 삭제
    void deleteHolidayById(int id);
}
