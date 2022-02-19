package TablesControllers;

import Database.Database;
import Entities.Car;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField numTxt;  //поле для ввода номера
    @FXML
    private Button searchBtn;  //кнопка поиска
    @FXML
    private Label numLbl;   //вспомогательный текст
    Connection connection;
    ObservableList<Car> car =FXCollections.observableArrayList();   //список для вывода результата поиска
    private Boolean modalResult = false;   //переменная модального результата
    String sNum;  //номер машины
    @FXML
    void onSearchBtnClick(ActionEvent actionEvent) {
       sNum = numTxt.getText();  //получаем номер машины с формы
        try {
            car.clear();  //очиащаем список
            connection = Database.getConnection();
            PreparedStatement ps=connection.prepareStatement("SELECT Car.C_ID,Car.C_NUMBER,Car.C_BRAND,CarType.CT_NAME,Car.C_MILEAGE,Car.C_CARGO,CarStatus.CS_NAME" +
                    " FROM [Car]" +
                    " INNER JOIN CarType ON Car.C_TYPE = CarType.CT_ID" +
                    " INNER JOIN CarStatus ON Car.C_STATUS = CarStatus.CS_ID" +
                    " WHERE C_NUMBER = ?");  //собираем данные с БД с условием
            ps.setString(1, sNum);  //устанавливаем условие поиска
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String c_number=rs.getString("C_NUMBER");
                String c_brand = rs.getString("C_BRAND");
                String ct_name = rs.getString("CT_NAME");
                int c_mileage = rs.getInt("C_MILEAGE");
                int c_cargo = rs.getInt("C_CARGO");
                String c_status = rs.getString("CS_NAME");
                car.add(new Car(c_number,c_brand,ct_name,c_mileage,c_cargo,c_status));  //формируем список из полученных данных
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (car.size()==0)   //если ничего не найдено
            showAlertWithoutHeaderText();   //метод вызова диалогового окна
        else{
        this.modalResult = true;  // ставим результат модального окна на false
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();}  // закрываем окно к которому привязана кнопка
    }

    //метод вызова диалогового окна
    private void showAlertWithoutHeaderText() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Поиск по номеру");
        alert.setHeaderText(null);
        alert.setContentText("Машины с таким номером не найдено");
        alert.showAndWait();
    }

    @FXML
    void initialize() {
    }

    public Boolean getModalResult() {
        return modalResult;
    } //метод получения модального результата

    public ObservableList<Car> addCar() throws IOException { //метод для заполенения списка результатами поиска
       return car;
    }
}


