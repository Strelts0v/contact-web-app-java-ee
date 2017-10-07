package com.itechart.app.model.email;

import com.itechart.app.model.cryptography.Cryptographer;
import com.itechart.app.model.cryptography.CryptographerXor;
import com.itechart.app.model.exceptions.EmailSendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * implementation of EmailService interface using gmail via TLS
 */
public class GmailEmailService implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(GmailEmailService.class);

    /**
     * java mail session object
     */
    private static Session SESSION;

    /**
     * object for retrieving mail settings
     */
    private static Properties PROPERTIES;

    /**
     * initializes session and properties objects for getting ready sending emails
     */
    public GmailEmailService() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("mail.properties");
        try {
            PROPERTIES = new Properties();
            PROPERTIES.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cryptographer cryptographer = new CryptographerXor();

        SESSION = Session.getInstance(PROPERTIES,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                PROPERTIES.getProperty("mail.username"),
                                cryptographer.decrypt(PROPERTIES.getProperty("mail.password"))
                        );
                    }
                });
    }

    @Override
    public void sendMessage(String fromEmail, String toEmail, String subject, String messageText)
            throws EmailSendingException {
        try {
            Message message = new MimeMessage(SESSION);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(messageText, "text/html; charset=utf-8");
            Transport.send(message);
            logger.info("Email to address " + toEmail + " was successfully send.");
        } catch (MessagingException me) {
            logger.error(me.getMessage() + ". Error during sending email to address " + toEmail);
            throw new EmailSendingException(me);
        }
    }
}
