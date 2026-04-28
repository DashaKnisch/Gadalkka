package com.dkkk.soothsayer.data;

import android.content.Context;
import java.io.*;

/**
 * Класс для работы с предзагруженной базой данных Таро.
 *
 * Отвечает за:
 * - копирование базы данных из assets в внутреннюю память приложения
 * - первичную инициализацию SQLite базы
 *
 * Используется для работы с готовой (offline) базой taro.db,
 * которая заранее создана в DBeaver или другом редакторе.
 */
public class TarotDatabaseHelper {

    /** Имя файла базы данных */
    private static final String DB_NAME = "taro.db";

    /**
     * Копирует базу данных из папки assets в внутреннее хранилище приложения.
     *
     * Выполняется только один раз — при первом запуске приложения.
     *
     * Алгоритм:
     * - проверка существования базы данных
     * - создание директории databases при необходимости
     * - копирование файла taro.db из assets
     *
     * @param context контекст приложения
     */
    public static void copyDatabase(Context context) {

        File dbFile = context.getDatabasePath(DB_NAME);

        if (dbFile.exists()) return;

        dbFile.getParentFile().mkdirs();

        try (InputStream is = context.getAssets().open(DB_NAME);
             OutputStream os = new FileOutputStream(dbFile)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}