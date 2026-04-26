package com.dkkk.soothsayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.repository.UserRepository;

/**
 * ViewModel для экрана входа (Login).
 *
 * Отвечает за:
 * - выполнение авторизации пользователя
 * - проверку корректности введённых данных
 * - передачу состояния UI (успех, ошибка, ошибки формы)
 */
public class LoginViewModel extends AndroidViewModel {

    /** Репозиторий пользователей */
    private final UserRepository userRepository;

    /** Состояние успешного входа */
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();

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
    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    /**
     * Возвращает состояние успешного входа.
     *
     * @return true если вход выполнен успешно
     */
    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    /**
     * Возвращает сообщение об ошибке авторизации.
     *
     * @return текст ошибки
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Возвращает состояние ошибки формы.
     *
     * Используется для подсветки полей ввода при ошибках.
     *
     * @return true если есть ошибка в форме
     */
    public LiveData<Boolean> getFormError() {
        return formError;
    }

    /**
     * Выполняет авторизацию пользователя.
     *
     * Проверяет:
     * - заполнены ли поля
     * - корректность данных через UserRepository
     *
     * @param login логин или email пользователя
     * @param password пароль пользователя
     */
    public void login(String login, String password) {

        boolean error = false;

        if (login.isEmpty() || password.isEmpty()) {
            error = true;
            errorMessage.setValue("Заполните все поля");
        }

        if (error) {
            formError.setValue(true);
            return;
        }

        boolean success = userRepository.login(login, password);

        if (success) {
            formError.setValue(false);
            loginSuccess.setValue(true);
        } else {
            formError.setValue(true);
            errorMessage.setValue("Пользователь не найден, введите корректные данные!");
        }
    }
}