package com.dkkk.soothsayer.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dkkk.soothsayer.data.TarotDatabaseHelper;
import com.dkkk.soothsayer.model.BallPrediction;

/**
 * Репозиторий для работы с предсказаниями магического шара.
 *
 * Инкапсулирует все операции чтения из базы данных предсказаний:
 * - получение случайного предсказания
 *
 * База данных (gadalka.db) содержит таблицу ball_predictions
 * с полями id и text.
 *
 * Применяется в:
 * - BallViewModel (получение предсказания для отображения)
 * - BallActivity (через ViewModel)
 *
 */
public class BallRepository {

    /** Объект базы данных SQLite (для чтения и записи) */
    private final SQLiteDatabase db;

    /**
     * Конструктор репозитория.
     *
     * При создании объекта выполняется копирование базы данных из assets
     * (если она ещё не скопирована) и открывается соединение с базой.
     *
     * @param context контекст приложения (для доступа к assets и файловой системе)
     */
    public BallRepository(Context context) {
        // Копирование базы данных из папки assets во внутреннее хранилище
        // (если файл ещё не существует)
        TarotDatabaseHelper.copyDatabase(context);

        // Открытие базы данных "gadalka.db" в режиме приватного доступа
        // Если базы не существует, она будет создана автоматически
        db = context.openOrCreateDatabase("gadalka.db", Context.MODE_PRIVATE, null);
    }

    /**
     * Получение случайного предсказания из базы данных.
     *
     * Используется SQLite-функция RANDOM() для случайной сортировки записей,
     * затем берётся первая запись (LIMIT 1).
     *
     * Алгоритм работы:
     * 1. Выполняется SQL-запрос с сортировкой по случайному числу
     * 2. Берётся первый результат
     * 3. Создаётся объект BallPrediction из полученных данных
     * 4. Курсор закрывается и возвращается результат
     *
     * @return объект BallPrediction со случайным предсказанием,
     *         или null, если в таблице нет записей или произошла ошибка
     */
    public BallPrediction getRandomPrediction() {

        // Запрос на получение одной случайной записи из таблицы ball_predictions
        Cursor c = db.rawQuery(
                "SELECT * FROM ball_predictions ORDER BY RANDOM() LIMIT 1",
                null
        );

        // Если запись найдена, создаём объект модели
        if (c.moveToFirst()) {
            BallPrediction p = new BallPrediction(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("text"))
            );

            c.close();
            return p;
        }

        // Если записей нет, закрываем курсор и возвращаем null
        c.close();
        return null;
    }
}