package com.itechart.app.model.email;

import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.exceptions.EmailSendingException;

import java.util.List;

/**
 * Execution stream that performs sending email basic messages
 * to specified list of addresses
 */
public class EmailSender implements Runnable {

    private EmailService service;

    /** property - who sends email message */
    private String from;

    /** property - who gets email message */
    private List<String> destinationEmailList;

    /** property - subject of email message */
    private String subject;

    /** property - message content */
    private String messageText;

    public EmailSender(EmailService service){
        this.service = service;
    }

    /**
     * constructor of thread object that specifies parameters for email sending
     * @param service - email service for sending email
     * @param from - who sends email message
     * @param destinationEmailList - who gets email message
     * @param subject - subject of email message
     * @param messageText - message content
     */
    public EmailSender(
            final EmailService service,
            final String from,
            final List<String> destinationEmailList,
            final String subject,
            final String messageText) {
        this.service = service;
        this.from = from;
        this.destinationEmailList = destinationEmailList;
        this.subject = subject;
        this.messageText = messageText;
    }

    @Override
    /**
     * executes email sending with specified parameters
     */
    public void run() {
        for(String to : destinationEmailList) {
            try {
                service.sendMessage(from, to, subject, messageText);
            } catch (EmailSendingException ese){
                AppLogger.error(ese.getMessage());
            }
        }
    }

    public EmailService getService() {
        return service;
    }

    public void setService(EmailService service) {
        this.service = service;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getDestinationEmailList() {
        return destinationEmailList;
    }

    public void setDestinationEmailList(List<String> destinationEmailList) {
        this.destinationEmailList = destinationEmailList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}