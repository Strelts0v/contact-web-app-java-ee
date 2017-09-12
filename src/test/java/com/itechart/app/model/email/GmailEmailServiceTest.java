package com.itechart.app.model.email;

import org.junit.Test;

public class GmailEmailServiceTest {

    private EmailService service = new GmailEmailService();

    @Test
    public void sendMessageTest() throws Exception {
        final String from = "ka1oken4by@gmail.com";
        final String to = "gleb.streltsov.4by@gmail.com";
        final String subject = "test";
        final String messageText = "test message";

        service.sendMessage(from, to, subject, messageText);
    }

}