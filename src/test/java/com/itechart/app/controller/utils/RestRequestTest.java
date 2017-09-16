package com.itechart.app.controller.utils;

import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletException;

public class RestRequestTest {

    @Test
    public void getCreateActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/create_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "create_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getAddAttachmentToContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/add_attachment_to_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "add_attachment_to_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getAddPhoneToContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/add_phone_to_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "add_phone_to_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getDeleteAttachmentFromContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/delete_attachment_from_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "delete_attachment_from_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getDeleteContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/delete_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "delete_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getDeletePhoneFromContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/delete_phone_from_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "delete_phone_from_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getGetContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/get_contact/1";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "get_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getResourceIdFromValidRestUriGetContactActionTest() throws Exception {
        final String pathInfo = "/get_contact/1";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final int expectedResource = 1;
        final String errorMessage = "Expected id from rest request and actual are different";

        final int resultAction = restRequest.getResourceId();
        Assert.assertEquals(errorMessage, expectedResource, resultAction);
    }

    @Test
    public void hasResourceIdFromValidRestUriGetContactActionTest() throws Exception {
        final String pathInfo = "/get_contact/1";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String errorMessage = "Expected that RestRequest object contains resource id";

        Assert.assertTrue(errorMessage, restRequest.hasResourceId());
    }

    @Test
    public void getGetContactsActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/get_contacts?offset=0&count=4";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "get_contacts";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getSearchContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/search_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "search_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getSendEmailToContactsActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/send_email_to_contacts";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "send_email_to_contacts";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getUpdateAttachmentFromContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/update_attachment_from_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "update_attachment_from_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getUpdateContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/update_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "update_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test
    public void getUpdatePhoneFromContactActionFromValidRestUriTest() throws Exception {
        final String pathInfo = "/update_phone_from_contact";
        final RestRequest restRequest = new RestRequest(pathInfo);
        final String expectedAction = "update_phone_from_contact";
        final String errorMessage = "Expected action from rest request and actual are different";

        final String resultAction = restRequest.getActionFromRestUrl();
        Assert.assertEquals(errorMessage, expectedAction, resultAction);
    }

    @Test(expected = ServletException.class)
    public void getActionFromInvalidRestUriTest() throws Exception {
        final String pathInfo = "/invalid_uri";
        new RestRequest(pathInfo);
    }
}