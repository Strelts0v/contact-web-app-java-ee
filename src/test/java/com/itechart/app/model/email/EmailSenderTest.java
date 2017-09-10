package com.itechart.app.model.email;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EmailSenderTest {

    private EmailService service = new GmailEmailService();

    @Test
    public void sendMessageTest() throws Exception {
        final String from = "ka1oken4by@gmail.com";
        final String to = "gleb.streltsov.4by@gmail.com";
        final String subject = "test";
        final String messageText = "test message";

        List<String> destinationEmailList = new ArrayList<>();
        destinationEmailList.add(to);

        Thread emailSender = new Thread(new EmailSender(
                service,
                from,
                destinationEmailList,
                subject,
                messageText
        ));

        emailSender.start();
        emailSender.join();
    }
}