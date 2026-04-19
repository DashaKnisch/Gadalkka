package com.dkkk.soothsayer;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel для LoginActivity.
 * Обрабатывает логику входа и управляет состоянием UI.
 */
public class LoginViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Выполняет попытку входа.
     */
    public void login(String login, String password) {
        if (login.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Поля не могут быть пустыми");
            return;
        }

        if (userRepository.login(login, password)) {
            loginSuccess.setValue(true);
        } else {
            errorMessage.setValue("Неверный логин или пароль");
        }
    }
}
