package com.dkkk.soothsayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Помощник для работы с базой данных пользователей.
 * Управляет созданием, обновлением таблицы и операциями вставки и проверки пользователей.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "UserDB";  // Имя базы данных
    private static final int DB_VERSION = 1;         // Версия базы данных

    /**
     * Конструктор класса DBHelper.
     *
     * @param context контекст приложения
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Создаёт таблицу пользователей при создании базы данных.
     *
     * @param db объект базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, password TEXT)");
    }

    /**
     * Обновляет базу данных при изменении версии.
     *
     * @param db объект базы данных
     * @param oldVersion старая версия базы данных
     * @param newVersion новая версия базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    /**
     * Вставляет нового пользователя в таблицу.
     *
     * @param name имя пользователя, должно быть уникальным
     * @param password пароль пользователя
     * @return true, если пользователь успешно добавлен, false — если возникла ошибка (например, имя уже существует)
     */
    public boolean insertUser(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("password", password);

        long result = -1;
        try {
            result = db.insertOrThrow("users", null, cv);
        } catch (SQLiteConstraintException ignored) {
            // Игнорируем ошибку уникальности имени
        }

        return result != -1;
    }

    /**
     * Проверяет наличие пользователя с заданным именем и паролем.
     *
     * @param name имя пользователя
     * @param password пароль пользователя
     * @return true, если пользователь с таким именем и паролем существует, иначе false
     */
    public boolean isValidUser(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE name=? AND password=?", new String[]{name, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}