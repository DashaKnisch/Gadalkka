package com.dkkk.soothsayer.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dkkk.soothsayer.data.TestDatabaseHelper;
import com.dkkk.soothsayer.model.test.Answer;
import com.dkkk.soothsayer.model.test.Question;
import com.dkkk.soothsayer.model.test.TestResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с базой данных тестов.
 *
 * Инкапсулирует все операции чтения из базы данных:
 * - получение списка вопросов для конкретного теста
 * - получение вариантов ответов для конкретного вопроса
 * - получение результата по типу (вычисленному доминирующему score)
 *
 * Применяется в:
 * - TestViewModel (загрузка данных для отображения)
 * - TestRunActivity (через ViewModel)
 *
 * @author Soothsayer Team
 * @version 1.0
 */
public class TestRepository {

    /** Объект базы данных SQLite (только для чтения) */
    private final SQLiteDatabase db;

    /** Название текущего теста ("witch" или "stone") */
    private final String testName;

    /**
     * Конструктор репозитория.
     *
     * @param context контекст приложения (для доступа к базе данных)
     * @param testName название теста ("witch" или "stone")
     */
    public TestRepository(Context context, String testName) {
        TestDatabaseHelper helper = new TestDatabaseHelper(context);
        db = helper.getReadableDatabase();
        this.testName = testName;
    }

    /**
     * Получение списка всех вопросов для текущего теста.
     *
     * Вопросы сортируются по полю order_num, чтобы сохранить правильный порядок.
     *
     * @return список вопросов (может быть пустым, если вопросы не найдены)
     */
    public List<Question> getQuestions() {

        List<Question> list = new ArrayList<>();

        Cursor c = db.rawQuery(
                "SELECT * FROM questions WHERE test_name=? ORDER BY order_num",
                new String[]{testName}
        );

        while (c.moveToNext()) {
            list.add(new Question(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("text"))
            ));
        }

        c.close();
        return list;
    }

    /**
     * Получение списка вариантов ответов для конкретного вопроса.
     *
     * @param questionId идентификатор вопроса (внешний ключ в таблице answers)
     * @return список вариантов ответов (может быть пустым, если ответы не найдены)
     */
    public List<Answer> getAnswers(int questionId) {

        List<Answer> list = new ArrayList<>();

        Cursor c = db.rawQuery(
                "SELECT * FROM answers WHERE test_name=? AND question_id=?",
                new String[]{testName, String.valueOf(questionId)}
        );

        while (c.moveToNext()) {
            list.add(new Answer(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getInt(c.getColumnIndexOrThrow("question_id")),
                    c.getString(c.getColumnIndexOrThrow("text")),
                    c.getInt(c.getColumnIndexOrThrow("score"))
            ));
        }

        c.close();
        return list;
    }

    /**
     * Получение результата теста по типу.
     *
     * Тип результата (type) соответствует доминирующему score,
     * который был вычислен на основе выбранных пользователем ответов.
     *
     * @param testName название теста ("witch" или "stone")
     * @param type числовой тип результата (1-6)
     * @return объект TestResult с заголовком, описанием и именем изображения,
     *         или null, если результат не найден
     */
    public TestResult getResult(String testName, int type) {

        Cursor c = db.rawQuery(
                "SELECT * FROM results WHERE test_name=? AND type=?",
                new String[]{testName, String.valueOf(type)}
        );

        if (c.moveToFirst()) {

            TestResult r = new TestResult(
                    c.getString(c.getColumnIndexOrThrow("title")),
                    c.getString(c.getColumnIndexOrThrow("text")),
                    c.getString(c.getColumnIndexOrThrow("image"))
            );

            c.close();
            return r;
        }

        c.close();
        return null;
    }
}