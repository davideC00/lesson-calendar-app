package it.uniba.di.sms.orariolezioni.data.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Lesson {
    
    public int id;
    public String teacher;
    public String subject;
    public Date fromTime;
    public Date toTime;
    
    
    public Lesson(String teacher, String subject, Date fromTime, Date toTime){
        this.teacher = teacher;
        this.subject = subject;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    // TODO check if this can be made deleted
    public Lesson(int id, String teacher, String subject, Date fromTime, Date toTime){
        this.id = id;
        this.teacher = teacher;
        this.subject = subject;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }
}
