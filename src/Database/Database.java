package Database;

import java.sql.*;

public class Database {
   public static Connection connection;
   public static Statement statement;
   public static ResultSet resultSet;

    public  static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost; databaseName = CarPark; integratedSecurity=true");
        if (connection != null) {
           System.out.println("Connected");
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
        statement.close();
        resultSet.close();
    }

}
