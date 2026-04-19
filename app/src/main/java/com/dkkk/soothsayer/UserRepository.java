package com.dkkk.soothsayer;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Репозиторий для работы с данными пользователей.
 * Обеспечивает абстракцию над базой данных и настройками.
 */
public class UserRepository {
    private final DBHelper dbHelper;
    private final SharedPreferences sharedPreferences;

    public UserRepository(Context context) {
        dbHelper = new DBHelper(context);
        sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
    }

    /**
     * Проверяет учетные данные пользователя.
     */
    public boolean login(String login, String password) {
        if (dbHelper.isValidUser(login, password)) {
            saveUserLogin(login);
            return true;
        }
        return false;
    }

    /**
     * Регистрирует нового пользователя.
     */
    public boolean register(String login, String password) {
        return dbHelper.insertUser(login, password);
    }

    /**
     * Сохраняет логин текущего пользователя.
     */
    private void saveUserLogin(String login) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", login);
        editor.apply();
    }

    /**
     * Возвращает имя текущего пользователя.
     */
    public String getUsername() {
        return sharedPreferences.getString("username", "Неизвестно");
    }

    /**
     * Очищает данные пользователя (выход из аккаунта).
     */
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
