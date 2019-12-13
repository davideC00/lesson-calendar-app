package it.uniba.di.sms.orariolezioni.data.model;

import java.util.Date;

public class Event {
    public int id;
    public Date fromTime;
    public Date toTime;

    public Event(Date fromTime, Date toTime){
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public Event(int id, Date fromTime, Date toTime){
        this(fromTime, toTime);
        this.id = id;
    }
}
