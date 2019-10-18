package it.uniba.di.sms.orariolezioni.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private Type type;

    public enum Type{
        SCHEDULER, TEACHER
    }

    public LoggedInUser(String userId, String displayName, Type type) {
        this.userId = userId;
        this.displayName = displayName;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
