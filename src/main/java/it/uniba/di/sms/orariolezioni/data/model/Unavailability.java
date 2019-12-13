package it.uniba.di.sms.orariolezioni.data.model;

import java.util.Date;

public class Unavailability extends Event {

    public String teacher;

    public Unavailability(String teacher, Date fromTime, Date toTime){
        super(fromTime, toTime);
        this.teacher = teacher;
    }

    public Unavailability(int id, String teacher, Date fromTime, Date toTime){
        super(id, fromTime, toTime);
        this.teacher = teacher;
    }
}
