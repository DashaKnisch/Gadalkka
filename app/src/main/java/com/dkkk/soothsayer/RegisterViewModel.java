package com.dkkk.soothsayer;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel для RegisterActivity.
 * Обрабатывает логику регистрации.
 */
public class RegisterViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

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

    /**
     * Выполняет попытку регистрации.
     */
    public void register(String login, String password) {
        if (login.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Введите данные");
            return;
        }

        if (userRepository.register(login, password)) {
            registerSuccess.setValue(true);
        } else {
            errorMessage.setValue("Пользователь уже существует");
        }
    }
}
