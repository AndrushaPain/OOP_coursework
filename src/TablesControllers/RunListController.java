package TablesControllers;

import Database.Database;
import Entities.Run;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RunListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Run> runTbl;  //таблица для вывода списка рейсов
    @FXML
    private TableColumn<Run, String> col_carNumber;
    @FXML
    private TableColumn<Run, Integer> col_run;
    @FXML
    private TableColumn<Run, Integer> col_distance;   //определение столбцов
    @FXML
    private TableColumn<Run, Integer> col_cargo;
    @FXML
    private TableColumn<Run, LocalDate> col_startdate;
    @FXML
    private TableColumn<Run, LocalDate> col_enddate;

    Connection connection;
    private String runNum;  //номер машины
    ObservableList<Run> runs = FXCollections.observableArrayList();  //список для вывода в таблицу
    @FXML
    void initialize() {
        loadData();
    }

    private void initCols() {
        col_run.setCellValueFactory(new PropertyValueFactory<>("run"));
        col_carNumber.setCellValueFactory(new PropertyValueFactory<>("carnum"));
        col_distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        col_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        col_startdate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        col_enddate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }
    public void loadData(){
        runNum=Controller.getSelected();
        try {
            connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Car.C_NUMBER, Run.RN_ID, Run.RN_DISTANCE,CarRun.CR_CARGO, CarRun.CR_START,CarRun.CR_END " +
                    " FROM [CarRun]" +
                    " INNER JOIN Car ON CarRun.CR_CAR = Car.C_ID" +
                    " INNER JOIN Run ON CarRun.CR_RUN = Run.RN_ID" +
                    " WHERE C_NUMBER = ?");
            preparedStatement.setString(1, runNum);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int r_id = rs.getInt("RN_ID");
                String r_number=rs.getString("C_NUMBER");
                int r_distance = rs.getInt("RN_DISTANCE");
                int r_cargo = rs.getInt("CR_CARGO");
                LocalDate r_sdate = rs.getObject("CR_START",LocalDate.class);
                LocalDate r_edate = rs.getObject("CR_END",LocalDate.class);
                runs.add(new Run(r_id,r_number,r_distance,r_cargo,r_sdate,r_edate));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        initCols();
        runTbl.setItems(runs);
    }
}
