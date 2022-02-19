package TablesControllers;

import Database.Database;
import Entities.Car;
import Logic.StatusChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableView<Car> carTbl;
    @FXML
    private TableColumn<Car, Integer> col_carNumber;
    @FXML
    private TableColumn<Car, String>  col_carBrand;
    @FXML
    private TableColumn<Car, String> col_carType;
    @FXML
    private TableColumn<Car, Integer> col_carMileage;
    @FXML
    private TableColumn<Car, Integer> col_carCargo;
    @FXML
    private TableColumn<Car, String> col_carStatus;
    @FXML
    private Button repairList;
    @FXML
    private Button runList;

    Connection connection;
   static  Timer timer = new Timer();
   static StatusChange stCh=new StatusChange();

    ObservableList<Car> cars = FXCollections.observableArrayList();
    ArrayList<Car> carList=new ArrayList<>();
    private static String selected;
    @FXML
    void initialize() {
        initCols();
        loadData();
        timerLoader();
    }

    private void initCols() {

        col_carNumber.setCellValueFactory(new PropertyValueFactory<>("num"));
        col_carBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        col_carType.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_carMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        col_carCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        col_carStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void loadData(){

        try {
            cars.clear();
            connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs=statement.executeQuery("SELECT " +
                    "CAR.C_ID," +
                    "CAR.C_NUMBER," +
                    "CAR.C_BRAND," +
                    "CARTYPE.CT_NAME," +
                    "CAR.C_MILEAGE," +
                    "CAR.C_CARGO," +
                    "CARSTATUS.CS_NAME" +
                    " FROM [Car]" +
                    "INNER JOIN CarType ON Car.C_TYPE = CarType.CT_ID" +
                    " INNER JOIN CarStatus ON Car.C_STATUS = CarStatus.CS_ID");
            while (rs.next()){
                String c_number=rs.getString("C_NUMBER");
                String c_brand = rs.getString("C_BRAND");
                String ct_name = rs.getString("CT_NAME");
                int c_mileage = rs.getInt("C_MILEAGE");
                int c_cargo = rs.getInt("C_CARGO");
                String c_status = rs.getString("CS_NAME");
                cars.add(new Car(c_number,c_brand,ct_name,c_mileage,c_cargo,c_status));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        carTbl.setItems(cars);
        carList.addAll(cars);
    }

    public void onRepairClick(ActionEvent actionEvent) throws IOException {
        makeNum();
       FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("repairList.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.carTbl.getScene().getWindow());
        stage.showAndWait();
    }

    public void onRunClick(ActionEvent actionEvent) throws IOException {
        makeNum();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("runList.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.carTbl.getScene().getWindow());
        stage.showAndWait();
    }

    public void makeNum(){
        Car selectCar=carTbl.getSelectionModel().getSelectedItem();
        setSelected(selectCar.getNum());
        System.out.println(selectCar.getNum());

    }

    public static String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void timerLoader(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadData();
                stCh.changeStatus(carList);
                System.out.println("Обновление по расписанию");
            }
        }, 20*1000, 20*1000);
    }
    public static void timerStop(){
        timer.cancel();
    }

    public void onSearchClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("search.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Поиск");
        stage.initOwner(this.carTbl.getScene().getWindow());
        stage.showAndWait();
        SearchController controller = loader.getController();
        if (controller.getModalResult()) {
            ObservableList<Car>  searched = controller.addCar();
            carTbl.setItems(searched);
        }
    }
}
