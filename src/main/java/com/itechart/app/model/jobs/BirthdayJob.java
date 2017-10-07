package com.itechart.app.model.jobs;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BirthdayJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(BirthdayJob.class);

    /** object for extracting job config params from resource bundle jobs.properties */
    private static final ResourceBundle jobsConfigBundle
            = ResourceBundle.getBundle("jobs-params");

    private final static String EMAIL_FIRST_NAME_PROPERTY_NAME = "job.birthday.email.from.firstname";
    private final static String EMAIL_SURNAME_PROPERTY_NAME = "job.birthday.email.from.surname";
    private final static String EMAIL_FROM_PROPERTY_NAME = "job.birthday.email.from";
    private final static String EMAIL_SUBJECT_PROPERTY_NAME = "job.birthday.subject";
    private final static String EMAIL_MESSAGE_PROPERTY_NAME = "job.birthday.message";

    public BirthdayJob(){
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ContactDao dao = null;
        logger.info("starting of Birthday job...");
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
                    logger.error(ese.getMessage());
                    throw new JobExecutionException("Error during sending email");
                }
                logger.info("Birthday message was successfully send to email: " + contact.getEmail());
            }

            dao.closeDao();
        } catch(ContactDaoException cde){
            logger.error(cde.getMessage());
            try {
                if(dao != null) {
                    dao.closeDao();
                }
            } catch (ContactDaoException cdex){
                logger.error(cde.getMessage());
            }
        }
    }

    private Map<String, String> prepareEmailParams(Contact contact){
        Map<String, String> emailParamsMap = new HashMap<>();

        emailParamsMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM,
                jobsConfigBundle.getString(EMAIL_FIRST_NAME_PROPERTY_NAME));
        emailParamsMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM,
                jobsConfigBundle.getString(EMAIL_SURNAME_PROPERTY_NAME));
        emailParamsMap.put(ContactActionProperties.EMAIL_FROM_PARAM,
                jobsConfigBundle.getString(EMAIL_FROM_PROPERTY_NAME));
        emailParamsMap.put(ContactActionProperties.EMAIL_SUBJECT_PARAM,
                jobsConfigBundle.getString(EMAIL_SUBJECT_PROPERTY_NAME));
        emailParamsMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM,
                jobsConfigBundle.getString(EMAIL_MESSAGE_PROPERTY_NAME));

        emailParamsMap.put(ContactActionProperties.EMAIL_BIRTHDAY_FIRST_NAME_PARAM, contact.getFirstName());
        emailParamsMap.put(ContactActionProperties.EMAIL_BIRTHDAY_LAST_NAME_PARAM, contact.getSurname());

        return emailParamsMap;
    }
}
