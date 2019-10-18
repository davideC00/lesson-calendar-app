package it.uniba.di.sms.orariolezioni.data;

import it.uniba.di.sms.orariolezioni.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        // TODO: handle loggedInUser authentication
        switch(username){
            case "scheduler":
                LoggedInUser scheduler =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Scheduler",
                            LoggedInUser.Type.SCHEDULER);
                return new Result.Success<>(scheduler);
            case "impedovo":
                LoggedInUser impedovo =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Donato Impedovo",
                            LoggedInUser.Type.TEACHER);
                return new Result.Success<>(impedovo);
            case "roselli":
                LoggedInUser roselli =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Teresa Roselli",
                            LoggedInUser.Type.TEACHER);
                return new Result.Success<>(roselli);
        }

        return new Result.Error(new Exception("Login Failed"));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
