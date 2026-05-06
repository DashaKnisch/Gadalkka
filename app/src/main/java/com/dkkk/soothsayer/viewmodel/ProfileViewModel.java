package com.dkkk.soothsayer.viewmodel;

import android.app.Application;
import android.database.Cursor;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.repository.UserRepository;

import java.util.Locale;

/**
 * ViewModel для экрана профиля пользователя.
 *
 * Отвечает за:
 * - загрузку данных пользователя из репозитория
 * - валидацию вводимых данных (поля, email, знак зодиака)
 * - обновление профиля с проверкой уникальности имени
 * - нормализацию знаков зодиака (EN → RU)
 *
 * Использует UserRepository для доступа к данным и SharedPreferences.
 *
 */
public class ProfileViewModel extends AndroidViewModel {

    /**
     * LiveData для отображения имени пользователя.
     */
    public MutableLiveData<String> username = new MutableLiveData<>();

    /**
     * LiveData для отображения email.
     */
    public MutableLiveData<String> email = new MutableLiveData<>();

    /**
     * LiveData для отображения знака зодиака.
     */
    public MutableLiveData<String> zodiac = new MutableLiveData<>();

    /**
     * LiveData для отображения сообщений об ошибках (Toast).
     */
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    /**
     * LiveData для индикации ошибки валидации в полях ввода.
     * true - поля подсвечиваются красным, false - нормальное состояние.
     */
    public MutableLiveData<Boolean> formError = new MutableLiveData<>();

    /**
     * Репозиторий для работы с данными пользователя.
     */
    private final UserRepository repo;

    /**
     * Конструктор ViewModel.
     *
     * @param app контекст приложения
     */
    public ProfileViewModel(@NonNull Application app) {
        super(app);
        repo = new UserRepository(app);
    }

    /**
     * Загружает данные текущего авторизованного пользователя из базы данных.
     * Обновляет LiveData username, email, zodiac.
     */
    public void loadUser() {

        Cursor c = repo.getCurrentUser();

        if (c != null && c.moveToFirst()) {
            username.setValue(c.getString(c.getColumnIndexOrThrow("username")));
            email.setValue(c.getString(c.getColumnIndexOrThrow("email")));
            zodiac.setValue(c.getString(c.getColumnIndexOrThrow("zodiac")));
        }

        if (c != null) c.close();
    }

    /**
     * Выполняет валидацию полей формы редактирования профиля.
     * Проверяет:
     * - заполненность полей
     * - корректность email (формат)
     * - корректность знака зодиака (12 вариантов)
     *
     * @param username имя пользователя
     * @param email email пользователя
     * @param zodiac знак зодиака
     * @return true если все поля валидны, иначе false
     */
    public boolean validate(String username, String email, String zodiac) {

        if (username.isEmpty() || email.isEmpty() || zodiac.isEmpty()) {
            errorMessage.setValue("Заполните все поля");
            formError.setValue(true);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.setValue("Некорректный email");
            formError.setValue(true);
            return false;
        }

        String normalizedZodiac = normalizeZodiac(zodiac);

        if (!isValidZodiac(normalizedZodiac)) {
            errorMessage.setValue("Введите корректный знак зодиака");
            formError.setValue(true);
            return false;
        }

        formError.setValue(false);
        return true;
    }

    /**
     * Обновляет данные профиля пользователя.
     * Сначала выполняет валидацию, затем проверяет уникальность имени пользователя.
     * При успехе обновляет сессию и локальные данные.
     *
     * @param username новое имя пользователя
     * @param email новый email
     * @param zodiac новый знак зодиака
     * @return true если обновление успешно, false если валидация не пройдена или имя занято
     */
    public boolean updateUser(String username, String email, String zodiac) {

        if (!validate(username, email, zodiac)) return false;

        String fixedZodiac = capitalize(normalizeZodiac(zodiac));

        boolean success = repo.updateUserSafe(
                username,
                email,
                fixedZodiac
        );

        if (!success) {
            errorMessage.setValue("Имя пользователя уже занято");
            formError.setValue(true);
            return false;
        }

        repo.updateSession(username);
        formError.setValue(false);
        return true;
    }

    /**
     * Нормализует строку знака зодиака.
     * Преобразует английские названия в русские.
     * Приводит к нижнему регистру и обрезает пробелы.
     *
     * @param value исходная строка знака зодиака
     * @return нормализованный знак зодиака на русском языке
     */
    private String normalizeZodiac(String value) {

        if (value == null) return "";

        String z = value.trim().toLowerCase(Locale.ROOT);

        switch (z) {
            // EN → RU
            case "aries": return "овен";
            case "taurus": return "телец";
            case "gemini": return "близнецы";
            case "cancer": return "рак";
            case "leo": return "лев";
            case "virgo": return "дева";
            case "libra": return "весы";
            case "scorpio": return "скорпион";
            case "sagittarius": return "стрелец";
            case "capricorn": return "козерог";
            case "aquarius": return "водолей";
            case "pisces": return "рыбы";

            default: return z;
        }
    }

    /**
     * Проверяет, является ли переданная строка корректным знаком зодиака.
     * Валидны только русские названия 12 знаков.
     *
     * @param z нормализованная строка знака зодиака
     * @return true если знак существует в списке, иначе false
     */
    private boolean isValidZodiac(String z) {

        switch (z) {
            case "овен":
            case "телец":
            case "близнецы":
            case "рак":
            case "лев":
            case "дева":
            case "весы":
            case "скорпион":
            case "стрелец":
            case "козерог":
            case "водолей":
            case "рыбы":
                return true;
        }

        return false;
    }

    /**
     * Приводит первую букву строки к верхнему регистру, остальные к нижнему.
     *
     * @param value исходная строка
     * @return строка с заглавной первой буквой
     */
    private String capitalize(String value) {

        if (value == null || value.isEmpty()) return value;

        value = value.trim().toLowerCase(Locale.ROOT);

        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}