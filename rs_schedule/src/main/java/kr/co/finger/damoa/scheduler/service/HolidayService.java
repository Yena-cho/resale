package kr.co.finger.damoa.scheduler.service;

import kr.co.finger.shinhandamoa.data.table.mapper.HolidaysMapper;
import kr.co.finger.shinhandamoa.data.table.model.Holidays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class HolidayService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final HolidaysMapper holidaysMapper;

    public HolidayService(HolidaysMapper holidayMapper) {
        this.holidaysMapper = holidayMapper;
    }

    // 특정 연도의 공휴일 목록 조회
    public List<Holidays> getHolidaysForYear(int year) {
        return holidaysMapper.getHolidaysByYear(year);
    }

    // 특정 연도의 첫 영업일 계산
    public LocalDate getFirstBusinessDay(int year, int month) {
        List<Holidays> holidays = getHolidaysForYear(year);
        
        LocalDate date = LocalDate.of(year, month, 1);

        while (isHolidayOrWeekend(date, holidays)) {
            date = date.plusDays(1);
        }

        return date;
    }

    // 공휴일 또는 주말 여부 확인
    private boolean isHolidayOrWeekend(LocalDate date, List<Holidays> holidays) {
        return holidays.stream().anyMatch(h -> h.getHolidayDate().equals(date)) ||
                date.getDayOfWeek().getValue() >= 6;
    }
}
