package com.itechart.app.model.jobs;

import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.email.EmailManager;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.enums.EmailTemplateEnum;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.exceptions.EmailSendingException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;

public class BirthdayJob implements Job {

    private static final String EMAIL_FROM_PARAM = "ka1oken4by@gmail.com";
    private static final String EMAIL_FROM_FIRST_NAME = "Gleb";
    private static final String EMAIL_FROM_SURNAME = "Streltsov";

    public BirthdayJob(){
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ContactDao dao = null;
        try {
            dao = JdbcContactDao.newInstance();
            List<Contact> contactList = dao.getContactsByBirthday(new Date());

            EmailManager emailManager = new EmailManager();
            for(Contact contact : contactList){
                Map<String, String> emailParamsMap = prepareEmailParams(contact);
                List<String> emailList = new ArrayList<>(1);
                emailList.add(contact.getEmail());
                try {
                    emailManager.sendEmail(emailList, EmailTemplateEnum.BIRTHDAY, emailParamsMap);
                } catch (EmailSendingException ese){
                    AppLogger.error(ese.getMessage());
                    throw new JobExecutionException("Error during sending email");
                }
                AppLogger.info("Birthday message was successfully send to email: " + contact.getEmail());
            }

            dao.closeDao();
        } catch(ContactDaoException cde){
            AppLogger.error(cde.getMessage());
            try {
                if(dao != null) {
                    dao.closeDao();
                }
            } catch (ContactDaoException cdex){
                AppLogger.error(cde.getMessage());
            }
        }
    }

    private Map<String, String> prepareEmailParams(Contact contact){
        Map<String, String> emailParamsMap = new HashMap<>();

        emailParamsMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM, EMAIL_FROM_FIRST_NAME);
        emailParamsMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM, EMAIL_FROM_SURNAME);
        emailParamsMap.put(ContactActionProperties.EMAIL_FROM_PARAM, EMAIL_FROM_PARAM);
        emailParamsMap.put(ContactActionProperties.EMAIL_BIRTHDAY_FIRST_NAME_PARAM, contact.getFirstName());
        emailParamsMap.put(ContactActionProperties.EMAIL_BIRTHDAY_LAST_NAME_PARAM, contact.getSurname());
        emailParamsMap.put(ContactActionProperties.EMAIL_SUBJECT_PARAM, "Birthday");
        emailParamsMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM, "Happy birthday! I wish you good health!");

        return emailParamsMap;
    }
}
