package com.dkkk.soothsayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel для Goroscope Activity.
 * Содержит логику получения текста и изображений для знаков зодиака.
 */
public class GoroscopeViewModel extends ViewModel {

    private final MutableLiveData<GoroscopeResult> goroscopeResult = new MutableLiveData<>();

    public LiveData<GoroscopeResult> getGoroscopeResult() {
        return goroscopeResult;
    }

    public void fetchHoroscope(String zodiac) {
        String query = zodiac.toLowerCase().trim();
        String text = getHoroscopeText(query);
        int image = getImageResource(query);

        if (text.isEmpty()) {
            goroscopeResult.setValue(null);
        } else {
            goroscopeResult.setValue(new GoroscopeResult(text, image));
        }
    }

    private String getHoroscopeText(String zodiac) {
        switch (zodiac) {
            case "овен": case "aries":
                return "Прекрасное время для творчества, вы сможете воплотить в жизнь яркие идеи. Чтобы владеть ситуацией, вам необходимо проявить решительность и инициативу.";
            case "телец": case "taurus":
                return "Эту неделю нужно начать с улыбки и оптимистичного взгляда на мир. По возможности разберитесь с долгами, попытки отложить эту проблему ни к чему не приведут.";
            case "близнецы": case "gemini":
                return "Вам необходимо спуститься с небес на землю, чтобы определить свои дальнейшие планы и главную линию жизни. Госпожа фортуна не забудет улыбнуться вам в нужный момент.";
            case "рак": case "cancer":
                return "На этой неделе вас будут переполнять творческие идеи и замыслы. Вам понадобятся единомышленники, которые помогли бы их воплощению в жизнь. Прислушивайтесь к интуиции.";
            case "лев": case "leo":
                return "Если вам удалось запустить новый проект, наладить свой бизнес, то можно расслабиться. Займите выжидательную позицию, будьте готовы к компромиссам.";
            case "дева": case "virgo":
                return "Дела могут пойти не совсем так, как вы ожидали, перспективы будут довольно туманны. Если вы не уверены в своих действиях, лучше не спешить, это позволит избежать проблем.";
            case "весы": case "libra":
                return "На этой неделе терпение и спокойствие помогут вам избежать ненужных стрессов и сохранить необходимые силы для активности на личном фронте.";
            case "скорпион": case "scorpio":
                return "У вас появится возможность для максимально успешной реализации задуманного. Вероятна благоприятная ситуация на работе и во взаимоотношениях с партнерами.";
            case "стрелец": case "sagittarius":
                return "Подходящий период для приобретения новых знаний и повышения профессионального уровня. Вам стоит проявить щедрость. Ваша интуиция будет подсказывать.";
            case "козерог": case "capricorn":
                return "Важно на этой неделе закончить неотложное дело, которое уже давно не дает вам покоя. Тщательно распланируйте свои действия, и всё будет.";
            case "водолей": case "aquarius":
                return "Ваша склонность к построению воздушных замков грозит обернуться рассеянностью и опозданием.";
            case "рыбы": case "pisces":
                return "На этой неделе в узоре вашей судьбы переплетутся две нити, одна из которых представляет собой энергию завершения процессов, а другая, возобновления старых связей.";
            default:
                return "";
        }
    }

    private int getImageResource(String zodiac) {
        switch (zodiac) {
            case "овен": case "aries": return R.drawable.aries_image;
            case "телец": case "taurus": return R.drawable.taurus_image;
            case "близнецы": case "gemini": return R.drawable.gemini_image;
            case "рак": case "cancer": return R.drawable.cancer_image;
            case "лев": case "leo": return R.drawable.leo_image;
            case "дева": case "virgo": return R.drawable.virgo_image;
            case "весы": case "libra": return R.drawable.libra_image;
            case "скорпион": case "scorpio": return R.drawable.scorpio_image;
            case "стрелец": case "sagittarius": return R.drawable.sagittarius_image;
            case "козерог": case "capricorn": return R.drawable.capricorn_image;
            case "водолей": case "aquarius": return R.drawable.aquarius_image;
            case "рыбы": case "pisces": return R.drawable.pisces_image;
            default: return 0;
        }
    }

    public static class GoroscopeResult {
        public final String text;
        public final int imageRes;

        public GoroscopeResult(String text, int imageRes) {
            this.text = text;
            this.imageRes = imageRes;
        }
    }
}
