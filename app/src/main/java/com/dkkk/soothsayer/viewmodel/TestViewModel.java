package com.dkkk.soothsayer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dkkk.soothsayer.R;

import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel для Test Activity.
 */
public class TestViewModel extends ViewModel {

    private final String[] questions = {
            "Вопрос 1: Какое время суток ты предпочитаешь?",
            "Вопрос 2: Какие магические атрибуты ты выберешь?",
            "Вопрос 3: Какое животное ты выберешь?",
            "Вопрос 4: Какую стихию ты выберешь?",
            "Вопрос 5: Какое оружие ты выберешь?",
            "Вопрос 6: Чего ты боишься?"
    };

    private final String[][] answerOptions = {
            {"Утро", "День", "Вечер", "Полдень", "Заря", "Ночь"},
            {"Метла", "Зелья", "Магический шар", "Палочка", "Книга магии", "Карты Таро"},
            {"Сова", "Собака", "Рыбка", "Феникс", "Белый кот", "Чёрный ворон"},
            {"Воздух", "Земля", "Вода", "Огонь", "Магия света", "Магия тьмы"},
            {"Лук", "Щит", "Копьё", "Меч", "Книга", "Не нуждаюсь"},
            {"Высоты", "Заточения", "Предательства", "Одиночества", "Зла", "Ничего)"}
    };

    private final Map<String, Integer> answerScores = new HashMap<>();
    private int currentQuestionIndex = 0;
    private int totalScore = 0;

    private final MutableLiveData<QuestionState> questionState = new MutableLiveData<>();
    private final MutableLiveData<ResultState> resultState = new MutableLiveData<>();

    public TestViewModel() {
        initScores();
    }

    private void initScores() {
        String[] allAnswers = {"Утро", "Метла", "Сова", "Воздух", "Лук", "Высоты"};
        for (String ans : allAnswers) answerScores.put(ans, 1);
        // ... упрощено для примера, в реальности заполнить все
        answerScores.put("Ночь", 6); answerScores.put("Карты Таро", 6);
        answerScores.put("Чёрный ворон", 6); answerScores.put("Магия тьмы", 6);
        answerScores.put("Не нуждаюсь", 6); answerScores.put("Ничего)", 6);
        // Дозаполним остальные для корректности логики
        String[] s2 = {"День", "Зелья", "Собака", "Земля", "Щит", "Заточения"};
        for (String ans : s2) answerScores.put(ans, 2);
        String[] s3 = {"Вечер", "Магический шар", "Рыбка", "Вода", "Копьё", "Предательства"};
        for (String ans : s3) answerScores.put(ans, 3);
        String[] s4 = {"Полдень", "Палочка", "Феникс", "Огонь", "Меч", "Одиночества"};
        for (String ans : s4) answerScores.put(ans, 4);
        String[] s5 = {"Заря", "Книга магии", "Белый кот", "Магия света", "Книга", "Зла"};
        for (String ans : s5) answerScores.put(ans, 5);
    }

    public LiveData<QuestionState> getQuestionState() { return questionState; }
    public LiveData<ResultState> getResultState() { return resultState; }

    public void startTest() {
        currentQuestionIndex = 0;
        totalScore = 0;
        showNextQuestion();
    }

    public void answer(String text) {
        Integer score = answerScores.get(text);
        if (score != null) totalScore += score;
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            showNextQuestion();
        } else {
            showResult();
        }
    }

    private void showNextQuestion() {
        questionState.setValue(new QuestionState(questions[currentQuestionIndex], answerOptions[currentQuestionIndex]));
    }

    private void showResult() {
        int resIdx = totalScore <= 10 ? 0 : totalScore <= 15 ? 1 : totalScore <= 20 ? 2 : totalScore <= 25 ? 3 : totalScore <= 30 ? 4 : 5;
        int img = 0; String txt = "";
        switch (resIdx) {
            case 0: img = R.drawable.vozdux; txt = "Ты - ведьма воздуха..."; break;
            case 1: img = R.drawable.zemla; txt = "Ты - ведьма земли..."; break;
            case 2: img = R.drawable.voda; txt = "Ты - ведьма воды..."; break;
            case 3: img = R.drawable.ogon; txt = "Ты - ведьма огня..."; break;
            case 4: img = R.drawable.svet; txt = "Ты - ведьма света..."; break;
            case 5: img = R.drawable.tma; txt = "Ты - ведьма тьмы..."; break;
        }
        resultState.setValue(new ResultState(img, txt));
    }

    public static class QuestionState {
        public final String question;
        public final String[] answers;
        public QuestionState(String q, String[] a) { this.question = q; this.answers = a; }
    }

    public static class ResultState {
        public final int imageRes;
        public final String description;
        public ResultState(int i, String d) { this.imageRes = i; this.description = d; }
    }
}
