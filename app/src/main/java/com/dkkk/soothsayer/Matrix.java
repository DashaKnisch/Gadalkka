package com.dkkk.soothsayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Активность для вычисления и отображения "матрицы" на основе введённой даты в формате дд.мм.гггг.
 * Вычисления выполняются по нумерологическим формулам, результат выводится в различные зоны интерфейса.
 */
public class Matrix extends AppCompatActivity {

    private EditText vvod;
    private Button knopka;
    private ImageButton reset;
    private TextView result;
    private ImageView matrixImage;
    private TextView zonaTalent;
    private TextView zonaPortret;
    private TextView zonaMat;
    private TextView zonaKarma;
    private TextView centralArcan;
    private TextView zonaM1;
    private TextView zonaM2;
    private TextView zonaG1;
    private TextView zonaG2;
    private TextView Talant1;
    private TextView Talant2;
    private TextView Portret1;
    private TextView Portret2;
    private TextView Mat1;
    private TextView Mat2;
    private TextView Karma1;
    private TextView Karma2;

    /**
     * Массив текстов, случайный из которых выводится при успешном вводе даты.
     */
    private final String[] textArray = {
            "Состояние человека «в плюсе»:\n" +
                    "\n" +
                    "Движение. Подобно Колеснице этот человек уверенно движется вперёд, умело преодолевая все преграды на пути. Он любит скорость и динамичное развитие событий, любое промедление, а тем более остановка для него неприемлемы. \n" +
                    "Состояние человека «в минусе»:\n" +
                    "\n" +
                    "Застой. Если жизнь заставляет такого человека остановиться, ему очень сложно вновь начать движение вперёд. Поэтому любые достаточно серьёзные препятствия на пути Колесницы — могут стать фатальными. В некоторых случаях неудачи бывают случайными, а обстоятельства приведшие к остановке мелкими и несущественными.\n" +
                    "Состояние человека «в плюсе»:\n" +
                    "\n" +
                    "Проницательность. Такой человек очень проницателен, быстро понимает истинные причины происходящего, хорошо чувствует мотивы и предвидит события. Хочет разобраться в мельчайших подробностях, всегда и всё любит «раскладывать по полочкам», и это обычно ему удаётся. \n" +
                    "Состояние человека «в минусе»:\n" +
                    "\n" +
                    "Отстранённость и эгоизм. В минусе это может быть закрытый, замкнутый или погружённый в себя человек. Больше всего он думает о себе, а значит, ему сложно строить построить здоровые отношения. Втайне может гордиться своей принципиальностью и считать окружающих недостойными себя.\n" +
                    "Состояние человека «в плюсе»:\n" +
                    "\n" +
                    "Амбиции. Это очень решительный и амбициозный человек, он не привык размениваться на мелочи, ставит перед собой глобальные цели и верит в их достижение. Крайне требователен к себе и подчинённым, быстро двигается по карьерной лестнице. Может завести бизнес и успешно развить своё дело до высокого уровня.\n" +
                    "Состояние человека «в минусе»:\n" +
                    "\n" +
                    "Высокомерие. Добившись некоторых результатов, такой человек становится высокомерным гордецом. При любой возможности превозносит собственные заслуги, кичится общественным положением или материальным достатком. Может незаслуженно оскорблять подчинённых и друзей, ставя себя выше остальных."
    };

    /**
     * Метод жизненного цикла onCreate - инициализация интерфейса и обработчиков событий.
     * @param savedInstanceState сохранённое состояние (если есть)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matrix);

        // Инициализация всех View по ID
        vvod = findViewById(R.id.vvod);
        knopka = findViewById(R.id.knopka);
        reset = findViewById(R.id.reset);
        result = findViewById(R.id.result);
        matrixImage = findViewById(R.id.matrixImage);
        zonaTalent = findViewById(R.id.zonaTalant);
        zonaPortret = findViewById(R.id.zonaPortret);
        zonaMat = findViewById(R.id.zonaMat);
        zonaKarma = findViewById(R.id.zonaKarma);
        centralArcan = findViewById(R.id.centralArcan);
        zonaM1 = findViewById(R.id.zonaM1);
        zonaM2 = findViewById(R.id.zonaM2);
        zonaG1 = findViewById(R.id.zonaG1);
        zonaG2 = findViewById(R.id.zonaG2);
        Talant1 = findViewById(R.id.Talant1);
        Talant2 = findViewById(R.id.Talant2);
        Portret1 = findViewById(R.id.Portret1);
        Portret2 = findViewById(R.id.Portret2);
        Mat1 = findViewById(R.id.Mat1);
        Mat2 = findViewById(R.id.Mat2);
        Karma1 = findViewById(R.id.Karma1);
        Karma2 = findViewById(R.id.Karma2);

        // Обработчик нажатия кнопки сброса: скрывает результаты и показывает поля для ввода
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvod.setVisibility(View.VISIBLE);
                knopka.setVisibility(View.VISIBLE);
                matrixImage.setVisibility(View.GONE);
                result.setVisibility(View.GONE);
                zonaTalent.setVisibility(View.GONE);
                zonaPortret.setVisibility(View.GONE);
                zonaMat.setVisibility(View.GONE);
                zonaKarma.setVisibility(View.GONE);
                centralArcan.setVisibility(View.GONE);
                zonaM1.setVisibility(View.GONE);
                zonaM2.setVisibility(View.GONE);
                zonaG1.setVisibility(View.GONE);
                zonaG2.setVisibility(View.GONE);
                Talant1.setVisibility(View.GONE);
                Talant2.setVisibility(View.GONE);
                Mat1.setVisibility(View.GONE);
                Mat2.setVisibility(View.GONE);
                Karma1.setVisibility(View.GONE);
                Karma2.setVisibility(View.GONE);
                Portret1.setVisibility(View.GONE);
                Portret2.setVisibility(View.GONE);
            }
        });

        // Обработчик нажатия кнопки "рассчитать"
        knopka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = vvod.getText().toString();
                if (validateInput(input)) {
                    // Скрываем ввод, показываем результаты
                    vvod.setVisibility(View.GONE);
                    knopka.setVisibility(View.GONE);
                    matrixImage.setVisibility(View.VISIBLE);
                    result.setVisibility(View.VISIBLE);
                    zonaTalent.setVisibility(View.VISIBLE);
                    zonaPortret.setVisibility(View.VISIBLE);
                    zonaMat.setVisibility(View.VISIBLE);
                    zonaKarma.setVisibility(View.VISIBLE);
                    centralArcan.setVisibility(View.VISIBLE);
                    zonaM1.setVisibility(View.VISIBLE);
                    zonaM2.setVisibility(View.VISIBLE);
                    zonaG1.setVisibility(View.VISIBLE);
                    zonaG2.setVisibility(View.VISIBLE);
                    Talant1.setVisibility(View.VISIBLE);
                    Talant2.setVisibility(View.VISIBLE);
                    Portret1.setVisibility(View.VISIBLE);
                    Portret2.setVisibility(View.VISIBLE);
                    Mat1.setVisibility(View.VISIBLE);
                    Mat2.setVisibility(View.VISIBLE);
                    Karma1.setVisibility(View.VISIBLE);
                    Karma2.setVisibility(View.VISIBLE);

                    // Вывод случайного текста из массива
                    Random random = new Random();
                    int randomIndex = random.nextInt(textArray.length);
                    String randomText = textArray[randomIndex];
                    result.setText(randomText);

                    // Выполнить вычисления и заполнить поля
                    extractFromInput();
                } else {
                    Toast.makeText(Matrix.this, "Введена неправильная дата", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Проверка корректности ввода даты в формате дд.мм.гггг.
     * @param input строка с датой
     * @return true, если дата соответствует формату и содержит валидные числа
     */
    private boolean validateInput(String input) {
        // Простейшая проверка формата через регулярное выражение
        if (!input.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            return false;
        }
        String[] parts = input.split("\\.");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (day < 1 || day > 31) return false;
        if (month < 1 || month > 12) return false;
        if (year < 1900 || year > 2100) return false; // Можно уточнить диапазон
        return true;
    }

    /**
     * Метод для обработки введённой даты и вычисления значений, которые будут отображены в интерфейсе.
     * Использует числовые преобразования и нумерологические формулы.
     */
    private void extractFromInput() {
        String[] parts = vvod.getText().toString().split("\\.");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        // Пример вычисления центрального аркана (сумма всех цифр даты)
        int sum = sumDigits(day) + sumDigits(month) + sumDigits(year);

        // Упрощение суммы (цифровая редукция)
        while (sum > 22) { // Можно менять в зависимости от правил
            sum = sumDigits(sum);
        }
        centralArcan.setText(String.valueOf(sum));

        // Пример заполнения других зон (можно расширить по нужной логике)
        zonaM1.setText(String.valueOf(day));
        zonaM2.setText(String.valueOf(month));
        zonaG1.setText(String.valueOf(year / 100));  // Первая часть года
        zonaG2.setText(String.valueOf(year % 100));  // Вторая часть года

        // Другие зоны могут заполняться по собственной логике

        // В этом методе следует реализовать всю нужную логику вычислений матрицы
    }

    /**
     * Метод для подсчёта суммы цифр числа.
     * @param num число
     * @return сумма цифр числа
     */
    private int sumDigits(int num) {
        int s = 0;
        while (num > 0) {
            s += num % 10;
            num /= 10;
        }
        return s;
    }
}