package it.uniba.di.sms.orariolezioni.data.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Lesson extends Event{

    public String teacher;
    public String subject;

    public Lesson(String teacher, String subject, Date fromTime, Date toTime){
        super(fromTime, toTime);
        this.teacher = teacher;
        this.subject = subject;
    }

    // TODO check if this can be made deleted
    public Lesson(int id, String teacher, String subject, Date fromTime, Date toTime){
        super(id, fromTime, toTime);
        this.teacher = teacher;
        this.subject = subject;
    }
}
