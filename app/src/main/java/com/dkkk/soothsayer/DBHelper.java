package com.dkkk.soothsayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserDB";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "birthdate TEXT, " +
                "zodiac TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser(String username, String email, String password,
                              String birthdate, String zodiac) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("birthdate", birthdate);
        cv.put("zodiac", zodiac);

        long result = db.insert("users", null, cv);
        return result != -1;
    }

    public Cursor getUserByLoginOrEmail(String login) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM users WHERE username=? OR email=?",
                new String[]{login, login}
        );
    }
}