package com.dkkk.soothsayer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Вспомогательный класс для работы с SQLite базой данных матрицы.
 *
 * Отвечает за:
 * - подключение к локальной базе данных
 * - копирование готовой базы данных из assets при первом запуске
 * - предоставление доступа к существующему файлу БД
 *
 * ВАЖНО:
 * В проекте используется уже готовая база данных (prebuilt),
 * поэтому метод onCreate() не используется.
 */
public class MatrixDBHelper extends SQLiteOpenHelper {

    /** Имя файла базы данных */
    private static final String DB_NAME = "matrix.db";

    /** Контекст приложения */
    private final Context context;

    /**
     * Конструктор класса.
     *
     * @param context контекст приложения
     *
     * При создании сразу проверяет наличие базы данных
     * и при необходимости копирует её из assets.
     */
    public MatrixDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;

        copyDatabaseIfNeeded();
    }

    /**
     * Вызывается при первом создании базы данных.
     *
     * В данном проекте НЕ используется,
     * так как база уже заранее создана и хранится в assets.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // не используется
    }

    /**
     * Вызывается при обновлении версии базы данных.
     *
     * В данном проекте не реализовано,
     * так как структура БД фиксированная.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // не используется
    }

    /**
     * Копирует готовую базу данных из assets в внутреннюю память приложения.
     *
     * Логика:
     * - проверяет, существует ли уже база
     * - если нет → копирует файл из assets
     * - записывает его в каталог приложения
     *
     * Это позволяет использовать заранее подготовленную SQLite базу.
     */
    private void copyDatabaseIfNeeded() {
        try {

            String outFileName = context.getDatabasePath(DB_NAME).getPath();

            // если база уже есть — ничего не делаем
            if (context.getDatabasePath(DB_NAME).exists()) return;

            InputStream input = context.getAssets().open(DB_NAME);
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;

            // копирование файла по частям
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}