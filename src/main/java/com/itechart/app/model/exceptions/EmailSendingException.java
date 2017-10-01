package com.itechart.app.model.exceptions;

public class EmailSendingException extends Exception {

    public EmailSendingException(){}

    public EmailSendingException(String message, Throwable exception){
        super(message, exception);
    }

    public EmailSendingException(String message){
        super(message);
    }

    public EmailSendingException(Throwable exception){
        super(exception);
    }
}
