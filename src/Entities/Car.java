package Entities;

public class Car {


private String num;
private String brand;
private String type;
private int mileage;
private int cargo;
private String status;

    public Car(String num, String brand, String type, int mileage, int cargo, String status) {
        this.num = num;
        this.brand = brand;
        this.type = type;
        this.mileage = mileage;
        this.cargo = cargo;
        this.status = status;
    }

    public String getNum() {
        return num;
    }

}

