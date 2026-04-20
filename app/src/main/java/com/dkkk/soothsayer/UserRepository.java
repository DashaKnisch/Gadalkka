package com.dkkk.soothsayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserRepository {

    private final DBHelper dbHelper;
    private final SharedPreferences sharedPreferences;

    public UserRepository(Context context) {
        dbHelper = new DBHelper(context);
        sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
    }

    public boolean login(String login, String password) {

        Cursor cursor = dbHelper.getUserByLoginOrEmail(login);

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));

            String hashedInput = hashPassword(password);

            if (storedPassword.equals(hashedInput)) {
                saveUserLogin(login);
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }

    public boolean register(String username, String email, String password,
                            String birthdate, String zodiac) {

        String hashedPassword = hashPassword(password);

        return dbHelper.insertUser(username, email, hashedPassword, birthdate, zodiac);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveUserLogin(String login) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", login);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", "Неизвестно");
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
    }
}