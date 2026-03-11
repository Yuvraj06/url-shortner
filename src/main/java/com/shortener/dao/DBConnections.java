package com.shortener.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnections {
    // The format is jdbc:postgresql://HOST/DATABASE
    private static final String URL = "jdbc:postgresql://ep-royal-bird-a1qem3d9-pooler.ap-southeast-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_YeTqd2LCSvz4"; // In a real job, we'd hide this!

    public static Connection getConnection() throws SQLException {
        try {
            // This loads the PostgreSQL driver into memory
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found!", e);
        }
    }
}