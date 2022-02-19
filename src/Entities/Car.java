package Entities;

public class Car {


private String num;   //номер машины
private String brand;  //марка
private String type;  //тип
private int mileage;   //пробег
private int cargo;   //масса перевезенного груза
private String status;   //статус машины

    //конструктор для создания машины
    public Car(String num, String brand, String type, int mileage, int cargo, String status) {
        this.num = num;
        this.brand = brand;
        this.type = type;
        this.mileage = mileage;
        this.cargo = cargo;
        this.status = status;
    }
    //метод получения номера машины
    public String getNum() {
        return num;
    }

}

