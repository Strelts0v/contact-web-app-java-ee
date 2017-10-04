package com.itechart.app.model.actions;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.email.EmailManager;
import com.itechart.app.model.enums.EmailTemplateEnum;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.exceptions.EmailSendingException;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendEmailToContactsAction implements ContactAction{

    private final Logger logger = LoggerFactory.getLogger(SendEmailToContactsAction.class);

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(
                ContactActionProperties.EMAIL_SUBMIT_PARAM));
        if(!isSubmit){
            String initialEmailsIdsToSend = requestContent.getParameter(
                    ContactActionProperties.EMAIL_INITIAL_EMAILS_TO_SEND_PARAM);
            if(initialEmailsIdsToSend != null) {
                if (!initialEmailsIdsToSend.equals("")) {
                    List<String> emailsToSend = getEmailsByIds(initialEmailsIdsToSend);
                    requestContent.insertAttribute(ContactActionProperties
                            .EMAIL_INITIAL_EMAIL_TO_SEND_REQUEST_ATTRIBUTE, emailsToSend);
                }
            }
            page = PageConfigurationManager.getPageName(
                    ContactActionProperties.EMAIL_PAGE_NAME);
        } else {
            String emailsStr = requestContent.getParameter(
                    ContactActionProperties.EMAILS_TO_SEND_PARAM);
            List<String> emails = parseEmails(emailsStr);

            EmailTemplateEnum emailTemplate;
            try {
                String emailTemplateStr = requestContent.getParameter(
                        ContactActionProperties.EMAIL_TEMPLATE_PARAM);
                emailTemplate = EmailTemplateEnum.valueOf(emailTemplateStr.toUpperCase());
            } catch (IllegalArgumentException iae){
                logger.error(iae.getMessage());
                page = getEmailErrorPage();
                insertEmailSendingRequestAttributes(requestContent,
                        ContactActionProperties.SENDING_EMAIL_WAS_UNSUCCESSFUL);
                return page;
            }

            Map<String, String> emailParamsMap = emailTemplate.getEmailParamsMap(requestContent);
            EmailManager emailManager = new EmailManager();
            try {
                emailManager.sendEmail(emails, emailTemplate, emailParamsMap);
            } catch (EmailSendingException ese){
                logger.error(ese.getMessage());
                page = getEmailErrorPage();
                insertEmailSendingRequestAttributes(requestContent,
                        ContactActionProperties.SENDING_EMAIL_WAS_UNSUCCESSFUL);
                return page;
            }

            page = PageConfigurationManager.getPageName(ContactActionProperties.EMAIL_PAGE_NAME);
            requestContent.insertAttribute(
                    ContactActionProperties.WAS_SENDING_EMAIL_SUCCESSFUL_REQUEST_ATTRIBUTE,
                    ContactActionProperties.SENDING_EMAIL_WAS_SUCCESSFUL);
        }
        logger.info("Return " + page + " to client");
        return page;
    }

    private List<String> parseEmails(String emailsStr){
        final String splitterValue = ";";

        Iterable<String> emailsIterator = Splitter.on(splitterValue)
                .omitEmptyStrings()
                .split(emailsStr);

        int emailsArraySize = Lists.newArrayList(emailsIterator).size();
        List<String> emails = new ArrayList<>(emailsArraySize);
        for(String email : emailsIterator){
            emails.add(email);
        }
        return emails;
    }

    private String getEmailErrorPage(){
        return PageConfigurationManager.getPageName(
                ContactActionProperties.EMAIL_PAGE_NAME);
    }

    private void insertEmailSendingRequestAttributes(
            RequestContent requestContent, Boolean wasSendingSuccessful){

        requestContent.insertAttribute(ContactActionProperties
                .WAS_CONTACT_SUCCESSFULLY_SAVED_REQUEST_ATTRIBUTE, wasSendingSuccessful);
    }

    private List<String> getEmailsByIds(String emailsIdsStr){
        List<String> emailList = new ArrayList<>();
        int[] emailsIds = parseEmailsIds(emailsIdsStr);
        ContactDao dao = null;
        try{
            dao = JdbcContactDao.newInstance();
            emailList = dao.getEmailsByIds(emailsIds);
            dao.closeDao();
        } catch (ContactDaoException cde){
            logger.error(cde.getMessage());
            try {
                if(dao != null) {
                    dao.closeDao();
                }
            } catch (ContactDaoException cdex){
                logger.error(cde.getMessage());
            }
        }
        return emailList;
    }

    private int[] parseEmailsIds(String emailsIdsStr){
        final String splitterValue = ",";

        Iterable<String> emailsIdsIterator = Splitter.on(splitterValue)
                .omitEmptyStrings()
                .split(emailsIdsStr);

        int emailsIdsSize = Lists.newArrayList(emailsIdsIterator).size();
        int[] emailsIds = new int[emailsIdsSize];
        int i = 0;
        for(String emailIdStr : emailsIdsIterator){
            emailsIds[i++] = Integer.parseInt(emailIdStr);
        }
        return emailsIds;
    }
}
