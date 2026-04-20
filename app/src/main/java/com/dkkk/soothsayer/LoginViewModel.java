package com.dkkk.soothsayer;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final MutableLiveData<Boolean> formError = new MutableLiveData<>();

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

    public LiveData<Boolean> getFormError() {
        return formError;
    }

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