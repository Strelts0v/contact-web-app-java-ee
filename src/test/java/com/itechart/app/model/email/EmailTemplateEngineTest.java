package com.itechart.app.model.email;

import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.enums.EmailTemplateEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EmailTemplateEngineTest {

    private EmailTemplateEngine templateEngine = new EmailTemplateEngine();

    private final String expectedFirstName = "test fisrtname";
    private final String expectedSurname = "test lastname";
    private final String expectedMessage = "test message";
    private final String expectedEmail = "test email@gmail.com";
    private final String expectedBirthdayFirstName = "test birthday first name";
    private final String expectedBirthdaySurname = "test birthday surname";

    @Test
    public void generateDefaultTemplateShouldReturnNotNullStringTest() throws Exception {
        Map<String, String> emailsParamMap = new HashMap<>();
        emailsParamMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM, expectedFirstName);
        emailsParamMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM, expectedSurname);
        emailsParamMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM, expectedMessage);
        emailsParamMap.put(ContactActionProperties.EMAIL_FROM_PARAM, expectedEmail);

        EmailTemplateEnum template = EmailTemplateEnum.DEFAULT;

        final String resultString = templateEngine.generateTemplate(template, emailsParamMap);
        final String errorMessage = "Expected not null result string from generated email template";
        Assert.assertNotNull(errorMessage, resultString);
    }

    @Test
    public void generateDefaultTemplateShouldContainsValidContentTest() throws Exception {
        Map<String, String> emailsParamMap = new HashMap<>();
        emailsParamMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM, expectedFirstName);
        emailsParamMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM, expectedSurname);
        emailsParamMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM, expectedMessage);
        emailsParamMap.put(ContactActionProperties.EMAIL_FROM_PARAM, expectedEmail);

        EmailTemplateEnum template = EmailTemplateEnum.DEFAULT;

        final String resultString = templateEngine.generateTemplate(template, emailsParamMap);
        final String errorMessage = "Expected valid content from generated email template";
        Assert.assertTrue(errorMessage, resultString.contains(expectedFirstName));
        Assert.assertTrue(errorMessage, resultString.contains(expectedSurname));
        Assert.assertTrue(errorMessage, resultString.contains(expectedMessage));
        Assert.assertTrue(errorMessage, resultString.contains(expectedEmail));
    }

    @Test
    public void generateBirthdayTemplateShouldReturnNotNullStringTest() throws Exception {
        Map<String, String> emailsParamMap = new HashMap<>();
        emailsParamMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM, expectedFirstName);
        emailsParamMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM, expectedSurname);
        emailsParamMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM, expectedMessage);
        emailsParamMap.put(ContactActionProperties.EMAIL_FROM_PARAM, expectedEmail);
        emailsParamMap.put(ContactActionProperties.EMAIL_BIRTHDAY_FIRST_NAME_PARAM, expectedBirthdayFirstName);
        emailsParamMap.put(ContactActionProperties.EMAIL_BIRTHDAY_LAST_NAME_PARAM, expectedBirthdaySurname);

        EmailTemplateEnum template = EmailTemplateEnum.BIRTHDAY;

        final String resultString = templateEngine.generateTemplate(template, emailsParamMap);
        final String errorMessage = "Expected not null result string from generated email template";
        Assert.assertNotNull(errorMessage, resultString);
    }

    @Test
    public void generateBirthdayTemplateShouldContainsValidContentTest() throws Exception {
        Map<String, String> emailsParamMap = new HashMap<>();
        emailsParamMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM, expectedFirstName);
        emailsParamMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM, expectedSurname);
        emailsParamMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM, expectedMessage);
        emailsParamMap.put(ContactActionProperties.EMAIL_FROM_PARAM, expectedEmail);
        emailsParamMap.put(ContactActionProperties.EMAIL_BIRTHDAY_FIRST_NAME_PARAM, expectedBirthdayFirstName);
        emailsParamMap.put(ContactActionProperties.EMAIL_BIRTHDAY_LAST_NAME_PARAM, expectedBirthdaySurname);

        EmailTemplateEnum template = EmailTemplateEnum.BIRTHDAY;

        final String resultString = templateEngine.generateTemplate(template, emailsParamMap);
        final String errorMessage = "Expected valid content from generated email template";
        Assert.assertTrue(errorMessage, resultString.contains(expectedFirstName));
        Assert.assertTrue(errorMessage, resultString.contains(expectedSurname));
        Assert.assertTrue(errorMessage, resultString.contains(expectedMessage));
        Assert.assertTrue(errorMessage, resultString.contains(expectedEmail));
        Assert.assertTrue(errorMessage, resultString.contains(expectedBirthdayFirstName));
        Assert.assertTrue(errorMessage, resultString.contains(expectedBirthdaySurname));
    }
}