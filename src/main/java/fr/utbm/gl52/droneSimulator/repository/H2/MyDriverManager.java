package fr.utbm.gl52.droneSimulator.repository.H2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDriverManager {
    private static String url = "jdbc:H2://localhost:3306/lo54";
    private static String user = "lo54";
    private static String password = "lo54";
    private static Connection connection;

    public static Connection getConnection() {
        if(connection == null){
            connect();
        }
        return connection;
    }

    public static void connect() {
        // TODO utiliser un gestionnaire de connexion

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
