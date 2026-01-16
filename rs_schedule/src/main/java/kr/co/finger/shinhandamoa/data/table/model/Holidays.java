package kr.co.finger.shinhandamoa.data.table.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Holidays {

    private int id;
    private LocalDate holidayDate;
    private String holidayName;
    private int year;

}
