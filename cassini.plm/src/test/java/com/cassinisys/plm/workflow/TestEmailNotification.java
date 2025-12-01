/*
package com.cassinisys.plm.workflow;

import com.cassinisys.platform.service.security.SecurityService;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

*/
/**
 * Created by reddy on 6/3/17.
 *//*

public class TestEmailNotification extends BaseTest {
    @Autowired
    private MailService mailService;

    @Autowired
    private SecurityService securityService;

    @Test
    public void testEmailNotification() throws Exception {

        Mail mail = new Mail();
        mail.setMailTo("raghuram.tera@cassinisys.com");
        mail.setMailSubject("Cassini.PLM Workflow Notification");
        mail.setTemplatePath("email/workflow/workflowApprover.html");
        mail.setModel(new HashMap<>());

        mailService.sendEmail(mail);
    }

    @Test
    public void testPasswordReset() throws Exception {
        securityService.resetPassword(securityService.getLoginByLoginName("admin"));
    }
}
*/
