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

    int cargo,mil, status;
    ArrayList<Repair> repairs= new ArrayList<>();
    ArrayList<Run> runs= new ArrayList<>();
    private String carNum;
    Connection connection;

    public ArrayList<Run> getRuns(String runNum)
    {
        runs.clear();
        try {
            connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Car.C_NUMBER, Run.RN_DISTANCE,CarRun.CR_CARGO" +
                    " FROM [CarRun]" +
                    " INNER JOIN Car ON CarRun.CR_CAR = Car.C_ID" +
                    " INNER JOIN Run ON CarRun.CR_RUN = Run.RN_ID" +
                    " WHERE C_NUMBER = ?");
            preparedStatement.setString(1, runNum);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String r_number=rs.getString("C_NUMBER");
                int r_distance = rs.getInt("RN_DISTANCE");
                int r_cargo = rs.getInt("CR_CARGO");
                runs.add(new Run(r_number,r_distance,r_cargo));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return runs;
    }

    public ArrayList<Repair> getRepairs(String runNum)
    {
        repairs.clear();
        try {
            connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Car.C_NUMBER, RepairType.RT_NAME" +
                    " FROM [Repair]" +
                    " INNER JOIN RepairType ON Repair.R_TYPE = RepairType.RT_ID" +
                    " INNER JOIN Car ON Repair.R_CAR = Car.C_ID" +
                    " WHERE C_NUMBER = ?");
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

    public int countMileage(ArrayList<Run> list){
        mil=0;
        for(Run run: list)
        {
            mil+=run.getDistance();
        }
        return mil;
    }
    public int countCargo(ArrayList<Run> list){
        cargo=0;
        for(Run run: list)
        {
            cargo+=run.getCargo();
        }
        return cargo;
    }

    public int statusChanger(int mil, int cargo,ArrayList<Repair> repairs){
        int stat=1;
        if ((mil>=20000) || (cargo>=5000))
        {
            stat=3;
        }
        if (repairs.size()>=3){
            stat=3;
        }
        else if ((mil<20000) && (cargo<5000)){
            stat=1;
        }
        return stat;
    }

    public void changeStatus(ArrayList<Car> list){
        for(Car car: list)
        {
            carNum =car.getNum();
            status=statusChanger(countMileage(getRuns(carNum)),countCargo(getRuns(carNum)),getRepairs(carNum));
            updateData(countMileage(getRuns(carNum)),countCargo(getRuns(carNum)), carNum,status);
        }

    }
    public void updateData(int mil, int cargo, String runNum,int stat){
        try {
            connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Car SET C_MILEAGE = ?, C_CARGO= ?, C_STATUS= ? WHERE C_NUMBER = ?");
            preparedStatement.setInt(1,mil);
            preparedStatement.setInt(2,cargo);
            preparedStatement.setInt(3,stat);
            preparedStatement.setString(4,runNum);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
