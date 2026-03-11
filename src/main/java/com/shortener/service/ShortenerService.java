package com.shortener.service;

import com.shortener.dao.URLDao;
import java.util.Random;

public class ShortenerService {
    private URLDao urlDao = new URLDao();
    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

     public String shortenURL(String longUrl) {
        String shortCode = generateCode();
        urlDao.saveUrl(longUrl, shortCode);
        return shortCode;
    }

    // Method to generate a random 6-character code
    private String generateCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        while (code.length() < 6) {
            int index = (int) (rnd.nextFloat() * ALPHABET.length());
            code.append(ALPHABET.charAt(index));
        }
        return code.toString();
    }

   

    public String getOriginalURL(String code) {
    return urlDao.getLongUrl(code);
}
}