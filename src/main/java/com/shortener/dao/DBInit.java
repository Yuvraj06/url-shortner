package com.shortener.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class DBInit {
    public static void main(String[] args) throws Exception {
        loadEnv(".env");

        String sql = readFile("schema.sql");

        try (Connection conn = DBConnections.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Schema applied successfully!");
        }
    }

    private static String readFile(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
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
