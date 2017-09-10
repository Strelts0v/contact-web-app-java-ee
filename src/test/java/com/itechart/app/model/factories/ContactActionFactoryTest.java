package com.itechart.app.model.factories;

import com.itechart.app.model.actions.*;
import org.junit.Assert;
import org.junit.Test;

public class ContactActionFactoryTest {

    private ContactActionFactory factory = new ContactActionFactory();

    @Test
    public void defineCreateContactActionTest() throws Exception {
        final String restUrl = "create_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == CreateContactAction.class);
    }

    @Test
    public void defineAddAttachmentToContactActionTest() throws Exception {
        final String restUrl = "add_attachment_to_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == AddAttachmentToContactAction.class);
    }

    @Test
    public void defineAddPhoneToContactActionTest() throws Exception {
        final String restUrl = "add_phone_to_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == AddPhoneToContactAction.class);
    }

    @Test
    public void defineDeleteContactActionTest() throws Exception {
        final String restUrl = "delete_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == DeleteContactAction.class);
    }

    @Test
    public void defineDeleteAttachmentFromContactActionTest() throws Exception {
        final String restUrl = "delete_attachment_from_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == DeleteAttachmentFromContactAction.class);
    }

    @Test
    public void defineDeletePhoneFromContactActionTest() throws Exception {
        final String restUrl = "delete_phone_from_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == DeletePhoneFromContactAction.class);
    }

    @Test
    public void defineGetContactActionTest() throws Exception {
        final String restUrl = "get_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == GetContactAction.class);
    }

    @Test
    public void defineGetContactsActionTest() throws Exception {
        final String restUrl = "get_contacts";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == GetContactsAction.class);
    }

    @Test
    public void defineSearchContactActionTest() throws Exception {
        final String restUrl = "search_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == SearchContactAction.class);
    }

    @Test
    public void defineSendEmailToContactsActionTest() throws Exception {
        final String restUrl = "send_email_to_contacts";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == SendEmailToContactsAction.class);
    }

    @Test
    public void defineUpdateAttachmentFromContactActionTest() throws Exception {
        final String restUrl = "update_attachment_from_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == UpdateAttachmentFromContactAction.class);
    }

    @Test
    public void defineUpdateContactActionTest() throws Exception {
        final String restUrl = "update_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == UpdateContactAction.class);
    }

    @Test
    public void defineUpdatePhoneFromContactActionTest() throws Exception {
        final String restUrl = "update_phone_from_contact";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == UpdatePhoneFromContactAction.class);
    }

    @Test
    public void defineIllegalContactActionTest() throws Exception {
        final String restUrl = "illegal";

        final String errorMessage
                = "Expected and actual type of returned ContactAction instance are different";

        final ContactAction action = factory.defineContactAction(restUrl);
        Assert.assertTrue(errorMessage, action.getClass() == ErrorContactAction.class);
    }
}