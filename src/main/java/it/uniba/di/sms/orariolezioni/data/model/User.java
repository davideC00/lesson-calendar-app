package it.uniba.di.sms.orariolezioni.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {

    public String username;
    public String type;


    public User(String username, String type) {
        this.username = username;
        this.type = type;
    }

}
