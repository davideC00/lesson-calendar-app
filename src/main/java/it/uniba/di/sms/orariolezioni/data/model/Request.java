package it.uniba.di.sms.orariolezioni.data.model;

public class Request {

    public int id;
    public String fromTeacher;
    public String toTeacher;
    public int lesson;

    public Request( String fromTeacher, String toTeacher, int lesson) {
        this.fromTeacher = fromTeacher;
        this.toTeacher = toTeacher;
        this.lesson = lesson;
    }

    public Request(int id, String fromTeacher, String toTeacher, int lesson) {
        this.id = id;
        this.fromTeacher = fromTeacher;
        this.toTeacher = toTeacher;
        this.lesson = lesson;
    }


    
}
