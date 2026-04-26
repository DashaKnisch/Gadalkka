package com.dkkk.soothsayer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Класс для работы с базой данных пользователей (SQLite).
 *
 * Отвечает за:
 * - создание таблицы пользователей
 * - добавление нового пользователя
 * - получение пользователя по логину или email
 *
 * Используется в UserRepository как слой доступа к данным.
 */
public class UserDBHelper extends SQLiteOpenHelper {

    /** Название базы данных */
    private static final String DB_NAME = "UserDB.db";

    /** Версия базы данных */
    private static final int DB_VERSION = 1;

    /**
     * Конструктор класса базы данных пользователей.
     *
     * @param context контекст приложения
     */
    public UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Создание таблицы пользователей при первом запуске приложения.
     *
     * Структура таблицы:
     * - id: уникальный идентификатор
     * - username: имя пользователя (unique)
     * - email: email (unique)
     * - password: хэш пароля
     * - birthdate: дата рождения
     * - zodiac: знак зодиака
     *
     * @param db объект базы данных SQLite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT UNIQUE, " +
                        "email TEXT UNIQUE, " +
                        "password TEXT, " +
                        "birthdate TEXT, " +
                        "zodiac TEXT)"
        );
    }

    /**
     * Обновление структуры базы данных.
     *
     * При изменении версии таблица пересоздаётся.
     *
     * @param db база данных
     * @param oldVersion старая версия
     * @param newVersion новая версия
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param username имя пользователя
     * @param email email пользователя
     * @param password хэшированный пароль
     * @param birthdate дата рождения
     * @param zodiac знак зодиака
     * @return true если вставка прошла успешно, иначе false
     */
    public boolean insertUser(String username, String email, String password,
                              String birthdate, String zodiac) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("birthdate", birthdate);
        cv.put("zodiac", zodiac);

        return db.insert("users", null, cv) != -1;
    }

    /**
     * Получает пользователя по логину или email.
     *
     * Используется при авторизации.
     *
     * @param login логин или email
     * @return Cursor с данными пользователя
     */
    public Cursor getUserByLogin(String login) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM users WHERE username=? OR email=?",
                new String[]{login, login}
        );
    }
}