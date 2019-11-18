package it.uniba.di.sms.orariolezioni.data.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Lesson {
    public String teacher;
    public Date fromDate;
    public Date toDate;

    public Lesson(String teacher, Date fromDate, Date toDate){
        this.teacher = teacher;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
