// DatabaseConnection.java
package com.tokopancing.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/toko_alat_pancing";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Koneksi database berhasil!");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error koneksi database: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Koneksi database ditutup!");
            } catch (SQLException e) {
                System.out.println("Error menutup koneksi: " + e.getMessage());
            }
        }
    }
}