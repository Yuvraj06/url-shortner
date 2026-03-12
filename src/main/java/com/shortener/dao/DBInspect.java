package com.shortener.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBInspect {
    public static void main(String[] args) throws Exception {
        loadEnv(".env");

        try (Connection conn = DBConnections.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                "SELECT column_name, data_type FROM information_schema.columns " +
                "WHERE table_name = 'short_urls' ORDER BY ordinal_position"
            );

            System.out.println("Columns in short_urls:");
            while (rs.next()) {
                System.out.println("  " + rs.getString("column_name") + " (" + rs.getString("data_type") + ")");
            }
        }
    }

    private static void loadEnv(String path) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int eq = line.indexOf('=');
                if (eq < 0) continue;
                System.setProperty(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
            }
        }
    }
}
