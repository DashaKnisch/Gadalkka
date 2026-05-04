package com.dkkk.soothsayer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Хелпер для работы с базой данных тестов.
 *
 * Отвечает за:
 * - копирование готовой базы данных из папки assets в файловую систему приложения
 * - предоставление доступа к базе данных через SQLiteOpenHelper
 *
 * База данных (tests.db) содержит таблицы:
 * - questions (вопросы для тестов witch и stone)
 * - answers (варианты ответов с баллами)
 * - results (результаты тестов с заголовками, описаниями и изображениями)
 *
 * @author Soothsayer Team
 * @version 1.0
 */
public class TestDatabaseHelper extends SQLiteOpenHelper {

    /** Имя файла базы данных в папке assets и в файловой системе */
    private static final String DB_NAME = "tests.db";

    /** Контекст приложения для доступа к assets и файловой системе */
    private final Context context;

    /**
     * Конструктор хелпера.
     *
     * @param context контекст приложения
     */
    public TestDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    /**
     * Вызывается при первом создании базы данных.
     * Не используется, так как база данных копируется готовой из assets.
     *
     * @param db объект базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {}

    /**
     * Вызывается при обновлении версии базы данных.
     * Не используется в текущей реализации.
     *
     * @param db объект базы данных
     * @param oldVersion старая версия
     * @param newVersion новая версия
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /**
     * Копирует готовую базу данных из папки assets в файловую систему приложения.
     *
     * База данных копируется только если она ещё не существует в директории приложения.
     * После копирования база становится доступной для чтения и записи.
     *
     * Алгоритм работы:
     * 1. Определяет путь, где должна находиться база данных в приложении
     * 2. Если файл базы данных отсутствует:
     *    - создаёт необходимые директории
     *    - открывает поток чтения из assets
     *    - открывает поток записи в файловую систему
     *    - копирует данные по 1 КБ
     *    - закрывает потоки
     *
     * @param context контекст приложения (используется для доступа к assets)
     */
    public static void copyDatabase(Context context) {
        try {
            File dbFile = context.getDatabasePath(DB_NAME);

            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();

                InputStream is = context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                os.flush();
                os.close();
                is.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}