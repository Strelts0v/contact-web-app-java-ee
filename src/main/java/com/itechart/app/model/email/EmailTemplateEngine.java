package com.itechart.app.model.email;

import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.enums.EmailTemplateEnum;
import org.stringtemplate.v4.ST;

import java.util.Map;

public class EmailTemplateEngine {

    public String generateTemplate(EmailTemplateEnum emailTemplate,
                                  Map<String, String> emailParamsMap){
        String templateBody = null;
        switch (emailTemplate){
            case BIRTHDAY:
                templateBody = generateBirthdayTemplate(emailParamsMap);
                break;
            case DEFAULT:
                templateBody = generateDefaultTemplate(emailParamsMap);
                break;
        }
        return templateBody;
    }

    private String generateBirthdayTemplate(Map<String, String> emailsParamsMap){
        String birthdayTemplateStr =
                "Happy birthday, dear <firstName> <lastName>!\n\n" +
                "<message>\n\n" +
                "Your <senderFirstName> <senderLastName>.";

        ST birthdayTemplate = new ST(birthdayTemplateStr);
        birthdayTemplate.add("firstName", emailsParamsMap.get(ContactActionProperties.EMAIL_BIRTHDAY_LAST_NAME_PARAM));
        birthdayTemplate.add("lastName", emailsParamsMap.get(ContactActionProperties.EMAIL_BIRTHDAY_FIRST_NAME_PARAM));
        birthdayTemplate.add("message", emailsParamsMap.get(ContactActionProperties.EMAIL_MESSAGE_PARAM));
        birthdayTemplate.add("senderFirstName", emailsParamsMap.get(ContactActionProperties.EMAIL_FIRST_NAME_PARAM));
        birthdayTemplate.add("senderLastName", emailsParamsMap.get(ContactActionProperties.EMAIL_LAST_NAME_PARAM));

        return birthdayTemplate.render();
    }

    private String generateDefaultTemplate(Map<String, String> emailsParamsMap){
        String defaultTemplateStr =
                "Good afternoon,\n\n" +
                "<message>\n\n" +
                "<senderFirstName> <senderLastName>,\n" +
                "<emailFrom>.";

        ST defaultTemplate = new ST(defaultTemplateStr);
        defaultTemplate.add("message", emailsParamsMap.get(ContactActionProperties.EMAIL_MESSAGE_PARAM));
        defaultTemplate.add("senderFirstName", emailsParamsMap.get(ContactActionProperties.EMAIL_FIRST_NAME_PARAM));
        defaultTemplate.add("senderLastName", emailsParamsMap.get(ContactActionProperties.EMAIL_LAST_NAME_PARAM));
        defaultTemplate.add("emailFrom", emailsParamsMap.get(ContactActionProperties.EMAIL_FROM_PARAM));

        return defaultTemplate.render();
    }
}
