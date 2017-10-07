package com.itechart.app.model.exceptions;

public class EmailTemplateEngineException extends Exception{

    public EmailTemplateEngineException(){}

    public EmailTemplateEngineException(String message, Throwable exception){
        super(message, exception);
    }

    public EmailTemplateEngineException(String message){
        super(message);
    }

    public EmailTemplateEngineException(Throwable exception){
        super(exception);
    }
}
