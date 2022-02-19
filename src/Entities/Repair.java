package Entities;

import java.time.LocalDate;

public class Repair {
    private String type;   //тип ремонта
    private String carNum;  //номер машины
    private LocalDate startDate;  //дата начала ремонта
    private LocalDate endDate;  //дата окончания

    //конструктор для создания ремонта
    public Repair( String carNum,String type, LocalDate startDate, LocalDate endDate) {
        this.carNum = carNum;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
