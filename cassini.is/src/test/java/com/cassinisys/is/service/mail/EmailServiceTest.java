package com.cassinisys.is.service.mail;

import com.cassinisys.is.BaseTest;
import com.cassinisys.platform.model.col.EmailMessage;
import com.cassinisys.platform.service.col.EmailMessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmailServiceTest extends BaseTest {
    @Autowired
    private EmailMessageService emailMessageService;

    @Test
    public void testEmailMessages() throws Exception {
        /*List<EmailMessage> messages = emailMessageService.getEmailMessages(35);
        for (EmailMessage message : messages) {
            System.out.println(message.getSubject());
        }*/
    }
}
