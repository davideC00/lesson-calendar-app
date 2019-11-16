package it.uniba.di.sms.orariolezioni.data.dao;

import java.util.List;

import it.uniba.di.sms.orariolezioni.data.model.Request;

public interface RequestDao {

    // TODO still usefull?

    List<Request> getAllRequests();

    List<Request> findByTeacher(String usernameTeacher);

    void insertAll(Request... requests);

    void delete(Request request);
}
