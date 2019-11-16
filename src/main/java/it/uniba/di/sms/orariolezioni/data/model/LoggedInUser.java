package it.uniba.di.sms.orariolezioni.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String username;
    private Type type;

    public enum Type{
        SCHEDULER, TEACHER
    }

    public LoggedInUser(String username, Type type) {
        this.username = username;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }
}
