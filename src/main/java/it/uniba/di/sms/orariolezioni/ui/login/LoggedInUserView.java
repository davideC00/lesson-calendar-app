package it.uniba.di.sms.orariolezioni.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String username;
    private String type;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String username, String type) {
        this.username = username;
        this.type = type;
    }

    String getUsername() {
        return username;
    }

    String getType() {
        return type;
    }
}
