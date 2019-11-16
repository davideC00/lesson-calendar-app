package it.uniba.di.sms.orariolezioni.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String username;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}
