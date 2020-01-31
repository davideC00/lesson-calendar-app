package it.uniba.di.sms.orariolezioni;

import android.app.Application;

/**
 * Class for storing global variables
 */
public class OrarioLezioniApplication extends Application {

    private String teacher;

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

}
