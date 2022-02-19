package Entities;

import java.time.LocalDate;

public class Run {

    private String carnum;   //номер машины
    private int run;         //номер рейса
    private int distance;    //расстояние
    private int cargo;        //масса груза
    private LocalDate startDate;  //дата начала рейса
    private LocalDate endDate;  //дата окончания рейса

    //конструктор для создания рейса
    public Run( int run, String carnum, int distance, int cargo, LocalDate startDate, LocalDate endDate) {
        this.run=run;
        this.carnum=carnum;
        this.distance = distance;
        this.cargo=cargo;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Run (String carnum,int distance,int cargo){
        this.carnum=carnum;
        this.distance=distance;
        this.cargo=cargo;
    }
    public int getDistance() {
        return distance;
    }   //метод получения расстояния
    public int getCargo() {
        return cargo;
    }  //метод получения массы груза
   }
