package fr.utbm.gl52.droneSimulator.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDriverManager {
    private static String url = "jdbc:mysql://localhost:3306/gl52";
    private static String user = "gl52";
    private static String password = "gl52";
    private static Connection connection;

    public static Connection openConnection() {
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
