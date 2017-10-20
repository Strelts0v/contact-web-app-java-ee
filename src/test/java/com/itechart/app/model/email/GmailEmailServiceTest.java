package com.itechart.app.model.email;

import com.itechart.app.model.exceptions.EmailSendingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Transport;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

// set PowerMock test runner instead of JUnit
@RunWith(PowerMockRunner.class)
// prepare class for executing tests
@PrepareForTest({Transport.class})
public class GmailEmailServiceTest {

    private EmailService service;

    private static final int EXPECTED_INVOCATION_TIMES = 1;

    // before each test set new email service object
    @Before
    public void setUp(){
        service = new GmailEmailService();
    }

    @Test(expected = EmailSendingException.class)
    public void sendEmailMessageWithInvalidAddressTest() throws Exception {
        // mock static class Transport
        mockStatic(Transport.class);
        // do nothing when static methods of class Transport are invoked
        doNothing().when(Transport.class);
        // execute sending email
        service.sendMessage(anyString(), anyString(),
                anyString(), anyString());
        // verify that static method of class Transport was invoked
        verifyStatic(times(EXPECTED_INVOCATION_TIMES));
    }

    @Test
    public void sendEmailMessageWithValidAddressTest() throws Exception {
        final String toEmail = "to@gmail.com";
        final String fromEmail = "from@gmail.com";
        final String subject = "test subject";
        final String body = "test body";

        mockStatic(Transport.class);
        doNothing().when(Transport.class);

        service.sendMessage(toEmail, fromEmail, subject, body);

        verifyStatic(times(EXPECTED_INVOCATION_TIMES));
    }
}