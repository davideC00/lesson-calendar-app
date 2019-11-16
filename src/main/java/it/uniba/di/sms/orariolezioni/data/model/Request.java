package it.uniba.di.sms.orariolezioni.data.model;

public class Request {

    public int id;
    public String fromTeacher;
    public String toTeacher;
    public String fromTime;
    public String toTime;

    public Request( String fromTeacher, String toTeacher, String fromTime, String toTime) {
        this.fromTeacher = fromTeacher;
        this.toTeacher = toTeacher;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public Request(int id, String fromTeacher, String toTeacher, String fromTime, String toTime) {
        this.id = id;
        this.fromTeacher = fromTeacher;
        this.toTeacher = toTeacher;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }


    
}
