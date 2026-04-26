package com.dkkk.soothsayer.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.dkkk.soothsayer.data.UserDBHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Репозиторий для работы с пользователями.
 *
 * Отвечает за:
 * - авторизацию (login)
 * - регистрацию (register)
 * - хэширование паролей (SHA-256)
 * - хранение текущей сессии пользователя (SharedPreferences)
 *
 * Является связующим слоем между UI и базой данных пользователей.
 */
public class UserRepository {

    /** Хелпер для работы с таблицей пользователей */
    private final UserDBHelper userDbHelper;

    /** Локальное хранилище для сохранения авторизованного пользователя */
    private final SharedPreferences sharedPreferences;

    /**
     * Конструктор репозитория пользователей.
     *
     * @param context контекст приложения для доступа к БД и SharedPreferences
     */
    public UserRepository(Context context) {
        userDbHelper = new UserDBHelper(context);
        sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
    }

    /**
     * Выполняет вход пользователя в систему.
     *
     * Логика:
     * - ищет пользователя по логину
     * - хэширует введённый пароль
     * - сравнивает с сохранённым хэшем
     * - при успехе сохраняет пользователя в SharedPreferences
     *
     * @param login логин пользователя
     * @param password пароль пользователя (в открытом виде)
     * @return true если авторизация успешна, иначе false
     */
    public boolean login(String login, String password) {

        Cursor cursor = userDbHelper.getUserByLogin(login);

        if (cursor.moveToFirst()) {

            String storedPassword = cursor.getString(
                    cursor.getColumnIndexOrThrow("password")
            );

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

    /**
     * Регистрирует нового пользователя.
     *
     * Перед сохранением пароль хэшируется (SHA-256).
     *
     * @param username имя пользователя
     * @param email email
     * @param password пароль (в открытом виде)
     * @param birthdate дата рождения
     * @param zodiac знак зодиака
     * @return true если регистрация успешна
     */
    public boolean register(String username, String email, String password,
                            String birthdate, String zodiac) {

        String hashedPassword = hashPassword(password);

        return userDbHelper.insertUser(
                username,
                email,
                hashedPassword,
                birthdate,
                zodiac
        );
    }

    /**
     * Хэширует пароль с использованием SHA-256.
     *
     * Используется для безопасного хранения паролей в базе данных.
     *
     * @param password исходный пароль
     * @return SHA-256 хэш пароля в виде hex-строки
     */
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

    /**
     * Сохраняет логин текущего пользователя в локальное хранилище.
     *
     * Используется для "запоминания" авторизованного пользователя.
     *
     * @param login логин пользователя
     */
    private void saveUserLogin(String login) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", login);
        editor.apply();
    }

    /**
     * Возвращает имя текущего авторизованного пользователя.
     *
     * @return логин пользователя или "Неизвестно", если пользователь не авторизован
     */
    public String getUsername() {
        return sharedPreferences.getString("username", "Неизвестно");
    }

    /**
     * Выполняет выход пользователя из системы.
     *
     * Очищает локальные данные авторизации.
     */
    public void logout() {
        sharedPreferences.edit().clear().apply();
    }
}