package TablesControllers;

import Database.Database;
import Entities.Repair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RepairListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Repair> repairTbl;
    @FXML
    private TableColumn<Repair, Integer> col_carNumber;
    @FXML
    private TableColumn<Repair, String>  col_type;
    @FXML
    private TableColumn<Repair, LocalDate> col_startdate;
    @FXML
    private TableColumn<Repair, LocalDate> col_enddate;

    Connection connection;
    private String repNum;

    ObservableList<Repair> repairs = FXCollections.observableArrayList();
    @FXML
    void initialize() {
        loadData();
    }

    private void initCols() {
        col_carNumber.setCellValueFactory(new PropertyValueFactory<>("carNum"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_startdate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        col_enddate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }

    public void loadData(){
        repNum=Controller.getSelected();
        try {
            connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Car.C_NUMBER, RepairType.RT_NAME, Repair.R_STARTDATE,Repair.R_ENDDATE " +
                    " FROM [Repair]" +
                    " INNER JOIN RepairType ON Repair.R_TYPE = RepairType.RT_ID" +
                    " INNER JOIN Car ON Repair.R_CAR = Car.C_ID" +
                    " WHERE C_NUMBER = ?");
            preparedStatement.setString(1, repNum);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String r_number=rs.getString("C_NUMBER");
                String r_type = rs.getString("RT_NAME");
                LocalDate r_sdate = rs.getObject("R_STARTDATE",LocalDate.class);
                LocalDate r_edate = rs.getObject("R_ENDDATE",LocalDate.class);
                repairs.add(new Repair(r_number,r_type,r_sdate,r_edate));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        initCols();
        repairTbl.setItems(repairs);
    }

}
