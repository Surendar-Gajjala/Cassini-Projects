package com.cassinisys.is.service.mail;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by reddy on 11/28/15.
 */
public class MailClient {
    public static void main(String args[]) throws Exception {
        // Get system properties
        Properties props = System.getProperties();

        // Setup mail server
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "25000");

        // Get session
        Session session = Session.getDefaultInstance(props, null);

        // Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("raghuram.tera@cassinisys.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("1234@cassinimail.com"));
        message.setSubject("The Subject");
        message.setText("The Message");

        // Send message
        Transport.send(message);
    }
}
