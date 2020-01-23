package it.uniba.di.sms.orariolezioni.ui.login;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private SharedPreferences sharedPref;
    private DbHandler db;

    public LoginViewModelFactory(DbHandler db, SharedPreferences sharedPref){
        this.db = db;
        this.sharedPref = sharedPref;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(db, sharedPref));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
