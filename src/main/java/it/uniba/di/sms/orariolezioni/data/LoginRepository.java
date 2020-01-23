package it.uniba.di.sms.orariolezioni.data;

import android.content.SharedPreferences;

import java.util.List;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.model.User;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private DbHandler dataSource;
    private SharedPreferences sharedPref;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private LoginRepository(DbHandler dataSource, SharedPreferences sharedPref) {
        this.dataSource = dataSource;
        this.sharedPref = sharedPref;
    }

    public static LoginRepository getInstance(DbHandler dataSource, SharedPreferences sharedPref) {
        if (instance == null) {
            instance = new LoginRepository(dataSource, sharedPref);
        }
        return instance;
    }


    public boolean isLoggedIn() {
        return sharedPref.contains("username") && sharedPref.contains("password");
    }

    public User getCachedUser(){
        String username = sharedPref.getString("username", "");
        String password = sharedPref.getString("password", "");
        return new User(username, password);
    }

    public void logout() {
        user = null;
        dataSource.logout();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();
    }

    private void setLoggedInUser(User user) {
        this.user = user;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", user.username);
        editor.putString("password", "password"); //TODO password is useless now
        editor.apply();
    }

    public Result<User> login(String username, String password) {
        // handle login
        Result<User> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<User>) result).getData());
        }
        return result;
    }
}
