package Logic;

import Database.Database;
import Entities.Car;
import Entities.Repair;
import Entities.Run;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatusChange {

    int cargo,mil, status;   //масса груза, пробег, статус машины
    ArrayList<Repair> repairs= new ArrayList<>();   //список ремонтов
    ArrayList<Run> runs= new ArrayList<>();  //списко рейсов
    private String carNum;   //номер машины
    Connection connection;   //объект для подключения к БД

    //метод получения рейсов машины
    public ArrayList<Run> getRuns(String runNum)
    {
        runs.clear();   //очищаем список
        try {
            connection = Database.getConnection();   //подлючаемся к БД
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Car.C_NUMBER, Run.RN_DISTANCE,CarRun.CR_CARGO" +
                    " FROM [CarRun]" +
                    " INNER JOIN Car ON CarRun.CR_CAR = Car.C_ID" +
                    " INNER JOIN Run ON CarRun.CR_RUN = Run.RN_ID" +
                    " WHERE C_NUMBER = ?");    //собираем нужные поля с нужных таблиц
            preparedStatement.setString(1, runNum);    //устанавливаем номер машины
            ResultSet rs = preparedStatement.executeQuery();   //выполняем запрос
            while (rs.next()){     //пока в результате есть данные
                String r_number=rs.getString("C_NUMBER");
                int r_distance = rs.getInt("RN_DISTANCE");
                int r_cargo = rs.getInt("CR_CARGO");
                runs.add(new Run(r_number,r_distance,r_cargo));   //добавляем в список
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return runs;
    }

    //метод получения ремонтов машины
    public ArrayList<Repair> getRepairs(String runNum)
    {
        repairs.clear();
        try {
            connection = Database.getConnection();     //подключаемся к БД
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Car.C_NUMBER, RepairType.RT_NAME" +
                    " FROM [Repair]" +
                    " INNER JOIN RepairType ON Repair.R_TYPE = RepairType.RT_ID" +
                    " INNER JOIN Car ON Repair.R_CAR = Car.C_ID" +
                    " WHERE C_NUMBER = ?");      //собираем нужные поля с нужных таблиц
            preparedStatement.setString(1, runNum);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String r_number=rs.getString("C_NUMBER");
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return repairs;
    }

    //метод расчета пробега
    public int countMileage(ArrayList<Run> list){
        mil=0;
        for(Run run: list)    //проходим по списку рейсов
        {
            mil+=run.getDistance();    //получаем значение расстояния каждого рейса
        }
        return mil;
    }

    //метод расчета массы груза
    public int countCargo(ArrayList<Run> list){
        cargo=0;
        for(Run run: list) //проходим по списку рейсов
        {
            cargo+=run.getCargo();  //получаем значение массы груза каждого рейса
        }
        return cargo;
    }

    //метод проверки условий
    public int statusChanger(int mil, int cargo,ArrayList<Repair> repairs){
        int stat=1;    //стандартный статус на ходу
        if ((mil>=20000) || (cargo>=5000))    //если значение пробега больше 20000 или масса груза больше 5000
        {
            stat=3;    //ставим статус планового ремонта
        }
        if (repairs.size()>=3){    //если количество ремонтов больше трех
            stat=4;    //ставим статус списано
        }
        else if ((mil<20000) && (cargo<5000)){    //если условия не превышены ставим стандартный статус
            stat=1;
        }
        return stat;
    }

    //метод смены статуса
    public void changeStatus(ArrayList<Car> list){
        for(Car car: list)    //проходим по всему списку машин
        {
            carNum =car.getNum();    //получаем номер машины
            status=statusChanger(countMileage(getRuns(carNum)),countCargo(getRuns(carNum)),getRepairs(carNum));    //получаем номер статуса
            updateData(countMileage(getRuns(carNum)),countCargo(getRuns(carNum)), carNum,status);    //вызываем метод обновления данных и передаем туда расчитанные данные
        }
    }

    //метод обновления данных
    public void updateData(int mil, int cargo, String runNum,int stat){
        try {
            connection = Database.getConnection();    //подключаемся к БД
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Car SET C_MILEAGE = ?, C_CARGO= ?, C_STATUS= ? WHERE C_NUMBER = ?");
            preparedStatement.setInt(1,mil);
            preparedStatement.setInt(2,cargo);
            preparedStatement.setInt(3,stat);
            preparedStatement.setString(4,runNum);
            preparedStatement.executeUpdate();    //выполняем запрос
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
