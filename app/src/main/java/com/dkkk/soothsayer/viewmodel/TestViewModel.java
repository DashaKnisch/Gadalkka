package com.dkkk.soothsayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.model.test.Answer;
import com.dkkk.soothsayer.model.test.Question;
import com.dkkk.soothsayer.model.test.TestResult;
import com.dkkk.soothsayer.repository.TestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel для прохождения тестов.
 *
 * Управляет логикой тестирования: загрузкой вопросов, сохранением ответов,
 * навигацией между вопросами и подсчётом результата.
 *
 * Используется в:
 * - активности теста "Какая ты ведьма" (TestWitchActivity)
 * - активности теста "Какой камень тебе подходит" (TestStoneActivity)
 *
 * Архитектура: MVVM (Model-View-ViewModel)
 * Жизненный цикл: привязан к Activity, переживает повороты экрана
 *
 * @author Soothsayer Team
 * @version 1.0
 */
public class TestViewModel extends AndroidViewModel {

    /**
     * Текущий отображаемый вопрос
     */
    public MutableLiveData<Question> currentQuestion = new MutableLiveData<>();

    /**
     * Список вариантов ответов для текущего вопроса
     */
    public MutableLiveData<List<Answer>> answers = new MutableLiveData<>();

    /**
     * Флаг отображения экрана с результатом (true = показывать результат)
     */
    public MutableLiveData<Boolean> showResult = new MutableLiveData<>(false);

    /**
     * Результат теста (заголовок, описание, изображение)
     */
    public MutableLiveData<TestResult> result = new MutableLiveData<>();

    /**
     * Флаг возможности перехода к следующему вопросу
     * (true = ответ выбран, можно идти дальше)
     */
    public MutableLiveData<Boolean> canGoNext = new MutableLiveData<>(false);

    /**
     * Репозиторий для работы с базой данных
     */
    private TestRepository repo;

    /**
     * Название текущего теста ("witch" или "stone")
     */
    private String testName;

    /**
     * Список всех вопросов теста
     */
    private List<Question> questionList;

    /**
     * Карта выбранных ответов.
     * Ключ = ID вопроса, значение = выбранный ответ.
     * (важно: используется questionId, а не порядковый индекс)
     */
    private final Map<Integer, Answer> selectedAnswers = new HashMap<>();

    /**
     * Текущий индекс (позиция) в списке вопросов
     */
    private int index = 0;

    /**
     * Конструктор ViewModel.
     *
     * @param app экземпляр приложения
     */
    public TestViewModel(@NonNull Application app) {
        super(app);
    }

    /**
     * Инициализация теста.
     * Вызывается после создания ViewModel.
     *
     * @param testName название теста ("witch" или "stone")
     */
    public void init(String testName) {
        this.testName = testName;
        this.repo = new TestRepository(getApplication(), testName);

        resetState();
        loadQuestions();
    }

    /**
     * Сброс состояния теста.
     * Очищает выбранные ответы и сбрасывает индекс.
     */
    private void resetState() {
        index = 0;
        selectedAnswers.clear();
        showResult.setValue(false);
    }

    /**
     * Загрузка всех вопросов теста из репозитория.
     */
    private void loadQuestions() {
        questionList = repo.getQuestions();

        if (questionList == null || questionList.isEmpty()) return;

        loadCurrent();
    }

    /**
     * Загрузка текущего вопроса и его ответов.
     * Обновляет LiveData для отображения в UI.
     */
    private void loadCurrent() {

        Question q = questionList.get(index);

        currentQuestion.setValue(q);
        answers.setValue(repo.getAnswers(q.id));

        // проверка по questionId (НЕ index)
        canGoNext.setValue(selectedAnswers.containsKey(q.id));
    }

    /**
     * Обработка выбора ответа пользователем.
     *
     * @param a выбранный вариант ответа
     */
    public void selectAnswer(Answer a) {

        if (questionList == null || questionList.isEmpty()) return;

        Question q = questionList.get(index);

        selectedAnswers.put(q.id, a);

        canGoNext.setValue(true);
    }

    /**
     * Получение выбранного ответа для текущего вопроса.
     *
     * @return выбранный ответ или null, если ответ не выбран
     */
    public Answer getSelectedAnswer() {

        if (questionList == null || questionList.isEmpty()) return null;

        Question q = questionList.get(index);

        return selectedAnswers.get(q.id);
    }

    /**
     * Переход к следующему вопросу.
     * Если вопрос последний — запускается подсчёт результата.
     */
    public void next() {

        if (questionList == null || questionList.isEmpty()) return;

        Question q = questionList.get(index);

        if (!selectedAnswers.containsKey(q.id)) return;

        if (index < questionList.size() - 1) {
            index++;
            loadCurrent();
        } else {
            calculateResult();
        }
    }

    /**
     * Возврат к предыдущему вопросу.
     */
    public void prev() {

        if (index > 0) {
            index--;
            loadCurrent();
        }
    }

    /**
     * Подсчёт результата теста.
     *
     * Алгоритм:
     * 1. Считает, сколько раз встречался каждый type (score)
     * 2. Находит type с максимальным количеством повторений
     * 3. Загружает из БД результат с этим type
     *
     * При равном количестве победителей берётся первый найденный максимум.
     */
    private void calculateResult() {

        Map<Integer, Integer> typeCount = new HashMap<>();

        for (Answer a : selectedAnswers.values()) {

            int type = a.score;

            Integer count = typeCount.get(type);

            if (count == null) {
                count = 0;
            }

            typeCount.put(type, count + 1);
        }

        int bestType = -1;
        int max = 0;

        for (Map.Entry<Integer, Integer> e : typeCount.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                bestType = e.getKey();
            }
        }

        result.setValue(repo.getResult(testName, bestType));
        showResult.setValue(true);
    }

    /**
     * Перезапуск теста.
     * Сбрасывает состояние и начинает сначала.
     */
    public void restart() {
        resetState();
        loadCurrent();
    }

    /**
     * Проверка, является ли текущий вопрос первым.
     *
     * @return true если первый вопрос, false иначе
     */
    public boolean isFirst() {
        return index == 0;
    }
}