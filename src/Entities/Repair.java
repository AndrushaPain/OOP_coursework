package Entities;

import java.time.LocalDate;

public class Repair {
    private String type;
    private String carNum;
    private LocalDate startDate;
    private LocalDate endDate;

    public Repair( String carNum,String type, LocalDate startDate, LocalDate endDate) {
        this.carNum = carNum;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
