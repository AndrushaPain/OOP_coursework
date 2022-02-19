package Entities;

import java.time.LocalDate;

public class Run {

    private String carnum;
    private int run;
    private int distance;
    private int cargo;
    private LocalDate startDate;
    private LocalDate endDate;

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
    }
    public int getCargo() {
        return cargo;
    }
   }
