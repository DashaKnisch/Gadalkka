package com.dkkk.soothsayer.viewmodel;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.repository.UserRepository;

/**
 * ViewModel для экрана регистрации пользователя.
 *
 * Отвечает за:
 * - валидацию данных регистрации
 * - создание нового пользователя через репозиторий
 * - передачу состояния UI (успех, ошибка, ошибки формы)
 */
public class RegisterViewModel extends AndroidViewModel {

    /** Репозиторий пользователей */
    private final UserRepository userRepository;

    /** Состояние успешной регистрации */
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    /** Сообщение об ошибке */
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    /** Состояние ошибки формы (валидация) */
    private final MutableLiveData<Boolean> formError = new MutableLiveData<>();

    /**
     * Конструктор ViewModel.
     *
     * Инициализирует репозиторий пользователей.
     *
     * @param application приложение (контекст)
     */
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    /**
     * Возвращает состояние успешной регистрации.
     *
     * @return true если регистрация выполнена успешно
     */
    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    /**
     * Возвращает сообщение об ошибке регистрации.
     *
     * @return текст ошибки
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Возвращает состояние ошибки формы.
     *
     * Используется для подсветки некорректных полей ввода.
     *
     * @return true если данные введены с ошибкой
     */
    public LiveData<Boolean> getFormError() {
        return formError;
    }

    /**
     * Выполняет регистрацию пользователя.
     *
     * Проверяет:
     * - заполненность логина
     * - корректность email
     * - минимальную длину пароля
     *
     * При успешной валидации создаёт пользователя через UserRepository.
     *
     * @param login логин пользователя
     * @param email email пользователя
     * @param password пароль пользователя
     */
    public void register(String login, String email, String password) {

        boolean error = false;

        if (login.isEmpty()) {
            error = true;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = true;
        }

        if (password.isEmpty() || password.length() < 10) {
            error = true;
        }

        if (error) {
            formError.setValue(true);
            errorMessage.setValue("Введите корректные данные");
            return;
        }

        boolean success = userRepository.register(
                login,
                email,
                password,
                "01-01-2000",
                "unknown"
        );

        if (success) {
            formError.setValue(false);
            registerSuccess.setValue(true);
        } else {
            formError.setValue(true);
            errorMessage.setValue("Пользователь уже существует");
        }
    }
}