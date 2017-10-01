package com.itechart.app.model.email;

import com.itechart.app.model.exceptions.EmailSendingException;

/**
 * specifies contract for sending email messages
 */
public interface EmailService {

    /**
     * performs sending basic email message
     * @param fromEmail   - who sends email message
     * @param toEmail     - who gets email message
     * @param subject     - subject of email message
     * @param messageText - message content
     */
    void sendMessage(String fromEmail, String toEmail, String subject, String messageText)
            throws EmailSendingException;
}