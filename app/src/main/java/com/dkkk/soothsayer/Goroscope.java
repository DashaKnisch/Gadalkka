package com.dkkk.soothsayer;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Активность для отображения гороскопа по знаку зодиака.
 * Позволяет пользователю ввести знак, отображает текст гороскопа с эффектом печати и изображение знака.
 */
public class Goroscope extends AppCompatActivity {

    private EditText user_field;
    private Button main_btn;
    private TextView result_info;
    private ImageView zodiacImage;

    private Handler handler = new Handler();
    private int index = 0;
    private String horoscopeText = "";

    /**
     * Инициализация активности.
     * Настраивает интерфейс и обработчик нажатия кнопки.
     *
     * @param savedInstanceState сохранённое состояние активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goroscope);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);
        zodiacImage = findViewById(R.id.zodiacImage);

        main_btn.setOnClickListener(new View.OnClickListener() {
            /**
             * Обработка нажатия на кнопку "Показать гороскоп".
             * Получает знак зодиака из поля ввода, получает текст и изображение,
             * запускает анимацию печати текста.
             *
             * @param v кнопка, на которую нажали
             */
            @Override
            public void onClick(View v) {
                String zodiac = user_field.getText().toString().toLowerCase();
                horoscopeText = getHoroscopeText(zodiac);
                int imageResource = getImageResource(zodiac);
                if (horoscopeText.isEmpty()) {
                    Toast.makeText(Goroscope.this, "Пожалуйста, введите свой знак зодиака.", Toast.LENGTH_SHORT).show();
                } else {
                    result_info.setText("");
                    index = 0;
                    handler.postDelayed(runnable, 50);
                    zodiacImage.setImageResource(imageResource);
                }
            }
        });
    }

    /**
     * Runnable для эффекта поочередного вывода текста гороскопа с задержкой.
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (index < horoscopeText.length()) {
                result_info.append(String.valueOf(horoscopeText.charAt(index)));
                index++;
                handler.postDelayed(this, 50); // Задержка между символами (мс)
            }
        }
    };

    /**
     * Возвращает текст гороскопа для заданного знака зодиака.
     *
     * @param zodiac знак зодиака (русское или английское название в нижнем регистре)
     * @return текст гороскопа или пустая строка, если знак не распознан
     */
    private String getHoroscopeText(String zodiac) {
        switch (zodiac) {
            case "овен":
            case "aries":
                return "Прекрасное время для творчества, вы сможете воплотить в жизнь яркие идеи. Чтобы владеть ситуацией, вам необходимо проявить решительность и инициативу.";
            case "телец":
            case "taurus":
                return "Эту неделю нужно начать с улыбки и оптимистичного взгляда на мир. По возможности разберитесь с долгами, попытки отложить эту проблему ни к чему не приведут.";
            case "близнецы":
            case "gemini":
                return "Вам необходимо спуститься с небес на землю, чтобы определить свои дальнейшие планы и главную линию жизни. Госпожа фортуна не забудет улыбнуться вам в нужный момент.";
            case "рак":
            case "cancer":
                return "На этой неделе вас будут переполнять творческие идеи и замыслы. Вам понадобятся единомышленники, которые помогли бы их воплощению в жизнь. Прислушивайтесь к интуиции.";
            case "лев":
            case "leo":
                return "Если вам удалось запустить новый проект, наладить свой бизнес, то можно расслабиться. Займите выжидательную позицию, будьте готовы к компромиссам.";
            case "дева":
            case "virgo":
                return "Дела могут пойти не совсем так, как вы ожидали, перспективы будут довольно туманны. Если вы не уверены в своих действиях, лучше не спешить, это позволит избежать проблем.";
            case "весы":
            case "libra":
                return "На этой неделе терпение и спокойствие помогут вам избежать ненужных стрессов и сохранить необходимые силы для активности на личном фронте.";
            case "скорпион":
            case "scorpio":
                return "У вас появится возможность для максимально успешной реализации задуманного. Вероятна благоприятная ситуация на работе и во взаимоотношениях с партнерами.";
            case "стрелец":
            case "sagittarius":
                return "Подходящий период для приобретения новых знаний и повышения профессионального уровня. Вам стоит проявить щедрость. Ваша интуиция будет подсказывать.";
            case "козерог":
            case "capricorn":
                return "Важно на этой неделе закончить неотложное дело, которое уже давно не дает вам покоя. Тщательно распланируйте свои действия, и всё будет.";
            case "водолей":
            case "aquarius":
                return "Ваша склонность к построению воздушных замков грозит обернуться рассеянностью и опозданием.";
            case "рыбы":
            case "pisces":
                return "На этой неделе в узоре вашей судьбы переплетутся две нити, одна из которых представляет собой энергию завершения процессов, а другая, возобновления старых связей.";
            default:
                return "";
        }
    }

    /**
     * Возвращает идентификатор ресурса изображения для заданного знака зодиака.
     *
     * @param zodiac знак зодиака (русское или английское название в нижнем регистре)
     * @return идентификатор ресурса изображения или 0, если знак не распознан
     */
    private int getImageResource(String zodiac) {
        switch (zodiac) {
            case "овен":
            case "aries":
                return R.drawable.aries_image;
            case "телец":
            case "taurus":
                return R.drawable.taurus_image;
            case "близнецы":
            case "gemini":
                return R.drawable.gemini_image;
            case "рак":
            case "cancer":
                return R.drawable.cancer_image;
            case "лев":
            case "leo":
                return R.drawable.leo_image;
            case "дева":
            case "virgo":
                return R.drawable.virgo_image;
            case "весы":
            case "libra":
                return R.drawable.libra_image;
            case "скорпион":
            case "scorpio":
                return R.drawable.scorpio_image;
            case "стрелец":
            case "sagittarius":
                return R.drawable.sagittarius_image;
            case "козерог":
            case "capricorn":
                return R.drawable.capricorn_image;
            case "водолей":
            case "aquarius":
                return R.drawable.aquarius_image;
            case "рыбы":
            case "pisces":
                return R.drawable.pisces_image;
            default:
                return 0;
        }
    }

    /**
     * Обработчик нажатия кнопки возврата на главный экран.
     *
     * @param v кнопка, на которую нажали
     */
    public void GoBack2(View v) {
        Intent intent = new Intent(this, Homeactivity.class);
        startActivity(intent);
    }
}