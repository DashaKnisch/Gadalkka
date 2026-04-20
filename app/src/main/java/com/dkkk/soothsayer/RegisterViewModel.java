package com.dkkk.soothsayer;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RegisterViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final MutableLiveData<Boolean> formError = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getFormError() {
        return formError;
    }

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