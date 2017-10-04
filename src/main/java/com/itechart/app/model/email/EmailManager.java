package com.itechart.app.model.email;

import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.enums.EmailTemplateEnum;
import com.itechart.app.model.exceptions.EmailSendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class EmailManager {

    private final Logger logger = LoggerFactory.getLogger(EmailManager.class);

    private EmailService service;

    public EmailManager(){
        this.service = new GmailEmailService();
    }

    public void sendEmail(List<String> destinationEmailList, EmailTemplateEnum emailTemplate,
                          Map<String, String> emailParamsMap) throws EmailSendingException{

        EmailTemplateEngine templateEngine = new EmailTemplateEngine();
        String body = templateEngine.generateTemplate(emailTemplate, emailParamsMap);

        Thread emailSender = new Thread(new EmailSender(
                service,
                emailParamsMap.get(ContactActionProperties.EMAIL_FROM_PARAM),
                destinationEmailList,
                emailParamsMap.get(ContactActionProperties.EMAIL_SUBJECT_PARAM),
                body
        ));

        emailSender.start();

        logger.info("Email sender was successfully start.");
    }
}
