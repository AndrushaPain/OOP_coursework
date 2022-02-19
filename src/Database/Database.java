package Database;

import java.sql.*;

public class Database {
   public static Connection connection;  //объект для соединения с БД
   public static Statement statement;   //объект для создания запроса
   public static ResultSet resultSet;   ////объект для вывода результата запроса

    //метод соединения с БД
    public  static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost; databaseName = CarPark; integratedSecurity=true");
        if (connection != null) {   //если подключилось
           System.out.println("Connected");
        }
        return connection;
    }
    //метод закрытия соединения
    public static void closeConnection() throws SQLException {
        connection.close();
        statement.close();
        resultSet.close();
    }

}
