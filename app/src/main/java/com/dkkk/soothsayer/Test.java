package com.dkkk.soothsayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Активность для проведения теста по выбору типа "ведьмы" на основе ответов пользователя.
 * <p>
 * Представляет собой серию вопросов с вариантами ответов.
 * На основе выбранных ответов подсчитывается суммарный балл,
 * который отображает результат с изображением и описанием.
 */
public class Test extends AppCompatActivity {

    /** Кнопка для начала теста */
    private Button startButton;

    /** Кнопка для сброса результатов и повторного прохождения теста */
    private ImageButton reset;

    /** Массив кнопок с вариантами ответов */
    private Button[] answerButtons;

    /** Текстовое поле для вывода текущего вопроса */
    private TextView questionTextView;

    /** Изображение, отображающее результат теста */
    private ImageView resultImageView;

    /** Текстовое поле для описания результата теста */
    private TextView resultTextView;

    /** Индекс текущего вопроса */
    private int currentQuestionIndex = 0;

    /** Суммарный счетчик очков за выбранные ответы */
    private int totalScore = 0;

    /** Карта, связывающая текст ответа с количеством очков */
    private Map<String, Integer> answerScores;

    /** Массив вопросов теста */
    private String[] questions = {
            "Вопрос 1: Какое время суток ты предпочитаешь?",
            "Вопрос 2: Какие магические атрибуты ты выберешь?",
            "Вопрос 3: Какое животное ты выберешь?",
            "Вопрос 4: Какую стихию ты выберешь?",
            "Вопрос 5: Какое оружие ты выберешь?",
            "Вопрос 6: Чего ты боишься?"
    };

    /** Варианты ответов для каждого вопроса */
    private String[][] answerOptions = {
            {"Утро", "День", "Вечер", "Полдень", "Заря", "Ночь"},
            {"Метла", "Зелья", "Магический шар", "Палочка", "Книга магии", "Карты Таро"},
            {"Сова", "Собака", "Рыбка", "Феникс", "Белый кот", "Чёрный ворон"},
            {"Воздух", "Земля", "Вода", "Огонь", "Магия света", "Магия тьмы"},
            {"Лук", "Щит", "Копьё", "Меч", "Книга", "Не нуждаюсь"},
            {"Высоты", "Заточения", "Предательства", "Одиночества", "Зла", "Ничего)"}
    };

    /**
     * Метод жизненного цикла активности, вызывается при создании.
     * Инициализирует элементы интерфейса и логику теста.
     *
     * @param savedInstanceState сохранённое состояние активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        startButton = findViewById(R.id.startButton);
        reset = findViewById(R.id.reset);
        questionTextView = findViewById(R.id.questionTextView);
        resultImageView = findViewById(R.id.resultImageView);
        resultTextView = findViewById(R.id.resultTextView);

        // Инициализация карты очков для каждого ответа
        answerScores = new HashMap<>();

        // Время суток
        answerScores.put("Утро", 1);
        answerScores.put("День", 2);
        answerScores.put("Вечер", 3);
        answerScores.put("Полдень", 4);
        answerScores.put("Заря", 5);
        answerScores.put("Ночь", 6);

        // Магические атрибуты
        answerScores.put("Метла", 1);
        answerScores.put("Зелья", 2);
        answerScores.put("Магический шар", 3);
        answerScores.put("Палочка", 4);
        answerScores.put("Книга магии", 5);
        answerScores.put("Карты Таро", 6);

        // Животные
        answerScores.put("Сова", 1);
        answerScores.put("Собака", 2);
        answerScores.put("Рыбка", 3);
        answerScores.put("Феникс", 4);
        answerScores.put("Белый кот", 5);
        answerScores.put("Чёрный ворон", 6);

        // Стихии
        answerScores.put("Воздух", 1);
        answerScores.put("Земля", 2);
        answerScores.put("Вода", 3);
        answerScores.put("Огонь", 4);
        answerScores.put("Магия света", 5);
        answerScores.put("Магия тьмы", 6);

        // Оружие
        answerScores.put("Лук", 1);
        answerScores.put("Щит", 2);
        answerScores.put("Копьё", 3);
        answerScores.put("Меч", 4);
        answerScores.put("Книга", 5);
        answerScores.put("Не нуждаюсь", 6);

        // Страхи
        answerScores.put("Высоты", 1);
        answerScores.put("Заточения", 2);
        answerScores.put("Предательства", 3);
        answerScores.put("Одиночества", 4);
        answerScores.put("Зла", 5);
        answerScores.put("Ничего)", 6);

        // Обработчик кнопки сброса теста
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.VISIBLE);
                resultImageView.setVisibility(View.GONE);
                resultTextView.setVisibility(View.GONE);
                questionTextView.setVisibility(View.VISIBLE);
                currentQuestionIndex = 0;
                totalScore = 0;
                showQuestion();
            }
        });

        // Обработчик кнопки старта теста
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestion();
                startButton.setVisibility(View.GONE);
            }
        });

        // Инициализация массива кнопок ответов
        answerButtons = new Button[]{
                findViewById(R.id.answerButton1),
                findViewById(R.id.answerButton2),
                findViewById(R.id.answerButton3),
                findViewById(R.id.answerButton4),
                findViewById(R.id.answerButton5),
                findViewById(R.id.answerButton6)
        };

        // Назначение обработчиков нажатия для каждой кнопки ответа
        for (int i = 0; i < answerButtons.length; i++) {
            final int buttonIndex = i;
            answerButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = answerButtons[buttonIndex].getText().toString();
                    Integer score = answerScores.get(buttonText);
                    if (score != null) {
                        totalScore += score;
                    } else {
                        Log.e("TestActivity", "Score not defined for answer: " + buttonText);
                    }
                    currentQuestionIndex++;
                    if (currentQuestionIndex < questions.length) {
                        showQuestion();
                    } else {
                        showResult();
                        questionTextView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    /**
     * Отображает текущий вопрос и варианты ответов.
     * Показывает только те кнопки, для которых есть варианты ответов.
     */
    private void showQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionTextView.setText(questions[currentQuestionIndex]);
            if (currentQuestionIndex < answerOptions.length) {
                String[] currentAnswers = answerOptions[currentQuestionIndex];
                for (int i = 0; i < answerButtons.length; i++) {
                    if (i < currentAnswers.length) {
                        answerButtons[i].setText(currentAnswers[i]);
                        answerButtons[i].setVisibility(View.VISIBLE);
                    } else {
                        answerButtons[i].setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    /**
     * Показывает результат теста в виде изображения и текста,
     * соответствующих диапазону суммарных очков пользователя.
     */
    private void showResult() {
        // Скрываем кнопки ответов
        for (Button button : answerButtons) {
            button.setVisibility(View.GONE);
        }

        // Определение индекса результата по сумме очков
        int resultIndex;
        if (totalScore <= 10) {
            resultIndex = 0;
        } else if (totalScore <= 15) {
            resultIndex = 1;
        } else if (totalScore <= 20) {
            resultIndex = 2;
        } else if (totalScore <= 25) {
            resultIndex = 3;
        } else if (totalScore <= 30) {
            resultIndex = 4;
        } else {
            resultIndex = 5;
        }

        // Отображение изображения и текста результата в зависимости от результата
        switch (resultIndex) {
            case 0:
                resultImageView.setImageResource(R.drawable.vozdux);
                resultTextView.setText("Ты - ведьма воздуха. Ты - свободная духом и неукротимая сила природы, стремящаяся к высотам.");
                break;
            case 1:
                resultImageView.setImageResource(R.drawable.zemla);
                resultTextView.setText("Ты - ведьма земли. Ты крепка и стабильна, твоя магия связана с плодородием и силой природы.");
                break;
            case 2:
                resultImageView.setImageResource(R.drawable.voda);
                resultTextView.setText("Ты - ведьма воды. Ты глубока и загадочна, умеешь владеть эмоциями и течением жизни.");
                break;
            case 3:
                resultImageView.setImageResource(R.drawable.ogon);
                resultTextView.setText("Ты - ведьма огня. Ты страстна и сильна, твоя энергия способна преобразить мир вокруг.");
                break;
            case 4:
                resultImageView.setImageResource(R.drawable.svet);
                resultTextView.setText("Ты - ведьма света. Ты светишься добротой и умеешь исцелять души.");
                break;
            case 5:
                resultImageView.setImageResource(R.drawable.tma);
                resultTextView.setText("Ты - ведьма тьмы. Ты обладаешь силой тайн и мистики, и не боишься неизвестного.");
                break;
        }

        resultImageView.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.VISIBLE);
    }
}
