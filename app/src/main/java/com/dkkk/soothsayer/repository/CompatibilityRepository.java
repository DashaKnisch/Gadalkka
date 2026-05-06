package com.dkkk.soothsayer.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dkkk.soothsayer.data.TarotDatabaseHelper;
import com.dkkk.soothsayer.model.CompatibilityResult;

/**
 * Репозиторий для работы с совместимостью (CompatibilityRepository).
 *
 * Отвечает за:
 * - получение данных о совместимости из базы данных
 * - выполнение запросов к таблице compatibility
 * - преобразование курсора в модель CompatibilityResult
 *
 * Используется в:
 * - CompatibilityViewModel для расчёта совместимости
 * - вычислении процента и текста по паре чисел судьбы
 *
 */
public class CompatibilityRepository {

    /** Объект SQLiteDatabase для выполнения запросов к БД */
    private final SQLiteDatabase db;

    /**
     * Конструктор репозитория.
     *
     * Копирует базу данных из assets при необходимости
     * и открывает соединение с БД gadalka.db
     *
     * @param context контекст приложения (используется для копирования и открытия БД)
     */
    public CompatibilityRepository(Context context) {
        TarotDatabaseHelper.copyDatabase(context);
        db = context.openOrCreateDatabase("gadalka.db", Context.MODE_PRIVATE, null);
    }

    /**
     * Получает результат совместимости по двум числам судьбы.
     *
     * Выполняет поиск в таблице compatibility где num1 и num2 совпадают.
     * Порядок чисел важен (1,2 не равно 2,1).
     *
     * @param num1 число судьбы первого человека (1-9)
     * @param num2 число судьбы второго человека (1-9)
     * @return объект CompatibilityResult с процентом и текстом,
     *         или null если запись не найдена
     */
    public CompatibilityResult getResult(int num1, int num2) {

        Cursor c = db.rawQuery(
                "SELECT * FROM compatibility WHERE num1=? AND num2=?",
                new String[]{String.valueOf(num1), String.valueOf(num2)}
        );

        if (c.moveToFirst()) {

            CompatibilityResult result = new CompatibilityResult(
                    c.getInt(c.getColumnIndexOrThrow("percentage")),
                    c.getString(c.getColumnIndexOrThrow("text"))
            );

            c.close();
            return result;
        }

        c.close();
        return null;
    }
}