package com.dkkk.soothsayer.viewmodel.taro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dkkk.soothsayer.R;

import java.util.Random;

/**
 * ViewModel для TaroActivity.
 * Содержит данные о картах и логику их выбора.
 */
public class TaroViewModel extends ViewModel {

    private final int[] situationImages = {
            R.drawable.rasklad1, R.drawable.rasklad2, R.drawable.rasklad3, R.drawable.rasklad4, R.drawable.rasklad5
    };

    private final String[] situationTexts = {
            "В жизни бывают времена когда кажется, что ничего не происходит. Вы должны научиться непредвзято смотреть на мир и на себя, видеть то, что есть, а не то, что хотели или надеялись увидеть. Вы обязаны пойти на личную жертву. Если Вы выдержите, жертва окупится.",
            "Вы достигли главного, конечного успеха в Ваших устремлениях. Вы способны управлять жизнью с пользой для других, если удастся заставить их слушать.Возможно, у Вас не хватит сил управлять ими, но Вы можете избежать их власти над Вами.",
            "Вы имеете власть управлять окружающим миром.Сейчас пришло время, когда успех и понимание Вам гарантированы. Вы способны бороться и готовы умереть за людей и имущество, но нужно понимать, стоит ли умирать за то, что защищаешь.",
            "Эта карта олицетворяет человека, имеющего власть продвинуть или сокрушить Вас. Все, чего Вы достигли, рухнуло. Разовьете ли Вы потенциал управлять окружающим миром или разменяете на демонстрацию суетной власти?",
            "Перед Вами испытание на выносливость. Не поддавайтесь отчаянию. Перед Вами выбор величайшей важности для Вас и Вашего будущего. Вы уже нашли путь, используйте Вашу власть мудро, оставайтесь в спокойной уверенности."
    };

    private final int[] dayImages = {
            R.drawable.card_lovers, R.drawable.card_death, R.drawable.card_wheel, R.drawable.card_star, R.drawable.card_sun, R.drawable.card_hermit
    };

    private final String[] dayTexts = {
            "Влюбленные призывают Вас исследовать и примирить противоположности в Вас самих, а не пытаться удалить или изменить их. Понимая и примиряя обе стороны своей натуры, Вы станете единым целым, не конфликтующим с собой.",
            "Большая перемена вот-вот случится в Вашей жизни. Хорошая или плохая, но ее не избежать. Заметьте, что эта карта, хотя и символизирует позитивную трансформацию в духовном усовершенствовании, при гадании очень неблагоприятна.",
            "Есть в жизни вещи, на которые никто не может повлиять. Вы должны ознавать, что Колесо постоянно вращается и Ваша жизнь зависит от него.Если Вы хотите добиться успеха, придется бороться, чтобы овладеть неподвластным.",
            "В этой точке Вашего путешествия Вы становитесь учителем, равно как и искателем. Звезды располагаются в Вашем направлении, давая Вам власть: быть в центре Вселенной, чтобы изменять небеса по собственной воле.",
            "Сейчас Вы можете ясно видеть свой путь. То, над чем Вы работали так напряженно, готово принести плоды. Не анализируйте их, не подвергайте сомнению, пытаясь понять их действительный смысл.",
            "Если эта карта олицетворяет гадающего, то Вам предстоит период самопознания. Это время, когда следует заново оценить свою жизнь, связи и цели. Возможные изменения, которые произойдут в жизни."
    };

    private final MutableLiveData<CardResult> selectedCard = new MutableLiveData<>();
    private final Random random = new Random();

    public LiveData<CardResult> getSelectedCard() {
        return selectedCard;
    }

    public void generateSituation() {
        int index = random.nextInt(situationImages.length);
        selectedCard.setValue(new CardResult(situationImages[index], situationTexts[index], 1));
    }

    public void generateDay() {
        int index = random.nextInt(dayImages.length);
        selectedCard.setValue(new CardResult(dayImages[index], dayTexts[index], 2));
    }

    public static class CardResult {
        public final int imageRes;
        public final String text;
        public final int type; // 1 - situation, 2 - day

        public CardResult(int imageRes, String text, int type) {
            this.imageRes = imageRes;
            this.text = text;
            this.type = type;
        }
    }
}
