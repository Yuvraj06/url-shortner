package com.shortener.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class URLDao {
    public void saveUrl(String longUrl, String shortCode) {
    String query = "INSERT INTO short_urls (full_url, short_code) VALUES (?, ?)";

    try (Connection conn = DBConnections.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, longUrl);
        pstmt.setString(2, shortCode);
        
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
            System.out.println("✅ SUCCESS: Saved " + shortCode + " to Neon!");
        } else {
            System.out.println("⚠️ WARNING: Query ran but no rows were added.");
        }
        
    } catch (SQLException e) {
        System.err.println("❌ DATABASE ERROR: " + e.getMessage());
        e.printStackTrace();
    }
}

    public String getLongUrl(String shortCode) {
    String query = "SELECT full_url FROM short_urls WHERE short_code = ?";
    
    try (Connection conn = DBConnections.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, shortCode);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getString("full_url");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Return null if the code doesn't exist
    }
}
