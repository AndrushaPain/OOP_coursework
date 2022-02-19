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
    private TableView<Car> carTbl;   //таблица с машинами
    @FXML
    private TableColumn<Car, Integer> col_carNumber;
    @FXML
    private TableColumn<Car, String>  col_carBrand;
    @FXML
    private TableColumn<Car, String> col_carType;    //определение столбцов
    @FXML
    private TableColumn<Car, Integer> col_carMileage;
    @FXML
    private TableColumn<Car, Integer> col_carCargo;
    @FXML
    private TableColumn<Car, String> col_carStatus;
    @FXML
    private Button repairList;    //кнопка вывода ремонтов
    @FXML
    private Button runList;   //кнопка вывода рейсов

    Connection connection;
   static  Timer timer = new Timer();   //объект класса таймера
   static StatusChange stCh=new StatusChange();   //объект класса смены статуса

    ObservableList<Car> cars = FXCollections.observableArrayList();   //список для вывода в таблицу
    ArrayList<Car> carList=new ArrayList<>();   //список для смены статуса
    private static String selected;   //выбранная машина
    @FXML
    void initialize() {   //инициализация формы
        initCols();
        loadData();
        timerLoader();
    }

    //инициализация столбцов таблицы
    private void initCols() {
        col_carNumber.setCellValueFactory(new PropertyValueFactory<>("num"));
        col_carBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        col_carType.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_carMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        col_carCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        col_carStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    //метод загрузки данных из БД
    public void loadData(){
        try {
            cars.clear();
            connection = Database.getConnection();   //подключаемся к БД
            Statement statement = connection.createStatement();   //создаем запрос
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
                    " INNER JOIN CarStatus ON Car.C_STATUS = CarStatus.CS_ID");   //получаем необходимые поля
            while (rs.next()){   //пока в результате выполенения есть данные -  считываем их
                String c_number=rs.getString("C_NUMBER");
                String c_brand = rs.getString("C_BRAND");
                String ct_name = rs.getString("CT_NAME");
                int c_mileage = rs.getInt("C_MILEAGE");
                int c_cargo = rs.getInt("C_CARGO");
                String c_status = rs.getString("CS_NAME");
                cars.add(new Car(c_number,c_brand,ct_name,c_mileage,c_cargo,c_status));   //заполняем список машин
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        carTbl.setItems(cars);   //устанавливаем список в таблицу
        carList.addAll(cars);   //заполняем список для смены статуса
    }

    //метод ослеживания нажатия на кнопку вывода списка ремонтов
    public void onRepairClick(ActionEvent actionEvent) throws IOException {
        makeNum();   //получаем номер машины
       FXMLLoader loader = new FXMLLoader();       //создание формы из fxml файла
        loader.setLocation(getClass().getResource("repairList.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();   //создание нового окна
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);   //указываем что оно модальное
        stage.initOwner(this.carTbl.getScene().getWindow());   //блокировка окна на котором нажата кнопка
        stage.showAndWait();   //открываем и ждем закрытия
    }

    //метод ослеживания нажатия на кнопку вывода списка рейсов
    public void onRunClick(ActionEvent actionEvent) throws IOException {
        makeNum();  //получаем номер машины
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("runList.fxml"));  //создание формы из fxml файла
        Parent root = loader.load();
        Stage stage = new Stage(); //создание нового окна
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL); //указываем что оно модальное
        stage.initOwner(this.carTbl.getScene().getWindow()); //блокировка окна на котором нажата кнопка
        stage.showAndWait(); //открываем и ждем закрытия
    }

    // метод получения выбранной машины
    public void makeNum(){
        Car selectCar=carTbl.getSelectionModel().getSelectedItem(); //получаем выбранный элемент таблицы
        setSelected(selectCar.getNum()); //вызываем метод установки значения переменной номера
    }

    //получаем выбранное значение
    public static String getSelected() {
        return selected;
    }

    //метод установки значения переменной номера
    public void setSelected(String selected) {
        this.selected = selected;
    }

    //метод запуска таймера
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

    //метод остановки таймера
    public static void timerStop(){
        timer.cancel();
    }

    //метод ослеживания нажатия на кнопку поиска
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
        SearchController controller = loader.getController(); //получаем доступ к запущенному контроллеру
        if (controller.getModalResult()) { //проверяем что нажали кнопку поиска на форме
            ObservableList<Car>  searched = controller.addCar(); //получаем найденную машину
            carTbl.setItems(searched);  //заполняем таблицу результатами поиска
        }
    }
}
