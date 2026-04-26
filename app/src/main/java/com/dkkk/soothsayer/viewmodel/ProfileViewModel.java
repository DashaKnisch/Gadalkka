package com.dkkk.soothsayer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.repository.UserRepository;

/**
 * ViewModel для ProfileActivity.
 */
public class ProfileViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        username.setValue(userRepository.getUsername());
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    /**
     * Выход из аккаунта.
     */
    public void logout() {
        // Очистка данных через репозиторий (добавим метод в репозиторий)
        userRepository.logout();
        navigateToLogin.setValue(true);
    }
}
