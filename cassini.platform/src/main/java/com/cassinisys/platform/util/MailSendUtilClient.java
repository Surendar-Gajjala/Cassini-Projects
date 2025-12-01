
/**
 * @author niraj kumar - P7109254
 *
 *	MailSendUtilClient - This class contains utility for sending mail through BMT server.
 *
 */

package com.cassinisys.platform.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;


/**
 * this  class is used to send emails
 * @author lakshmi
 *
 */
@Component
public class MailSendUtilClient {

    //private Logger log = LogManager.getLogger(MailSendUtilClient.class);

    private String userName;
    private String password="";
    private String sendingHost;
    private int sendingPort;
    private String from;
    private String to;
    private String subject;
    private String text;
    private String receivingHost;
    private String[] toAddress;
    private String[] ccAddress;

    Properties props = null;

    @Autowired
    private Environment env;

    public void setEmailAccountDetails(){

        sendingHost=env.getProperty("mail.smtp.host");
        sendingPort=Integer.parseInt(String.valueOf(env.getProperty("mail.smtp.port")));
        userName=env.getProperty("mail.smtp.user");
        password=env.getProperty("mail.smtp.password");

        props = new Properties();

        props.put("mail.smtp.host", sendingHost);
        props.put("mail.smtp.port", String.valueOf(sendingPort));
        props.put("mail.smtp.user", userName);
        props.put("mail.smtp.password",password);

        props.put("mail.smtp.auth", "true");


    }


    public void sendMail(EmailDetails emailDetails){

        this.from=emailDetails.getFrom();
        this.to=emailDetails.getTo();
        this.subject=emailDetails.getSubject();
        this.text=emailDetails.getText();

        setEmailAccountDetails();
        Session session1 = Session.getDefaultInstance(props);

        Message simpleMessage = new MimeMessage(session1);

        /** MIME stands for Multipurpose Internet Mail Extensions */

        InternetAddress fromAddress = null;
        InternetAddress toAddress = null;

        try {

            fromAddress = new InternetAddress(this.from);
            toAddress = new InternetAddress(this.to);

        } catch (AddressException ae) {

            ae.printStackTrace();

        }

        try {

            simpleMessage.setFrom(fromAddress);

            simpleMessage.setRecipient(RecipientType.TO, toAddress);

            simpleMessage.setSubject(this.subject);

            simpleMessage.setText(this.text);

            /** sometimes Transport.send(simpleMessage); is used, but for gmail it's different*/

            Transport transport = session1.getTransport("smtp");

            transport.connect(this.sendingHost, sendingPort, "kotidynamic@gmail.com",
                    "");

            transport.sendMessage(simpleMessage, simpleMessage.getAllRecipients());
            //  Transport.send(simpleMessage);
            //System.out.println("sent");
            //      transport.close();

            //        log.info("Mail sent successfully ..!!!");

        } catch (MessagingException me) {

            me.printStackTrace();
            /** display an error message*/
            //      log.error("ERROR :Sending email to: " + to + " failed !!!",me);
        }

    }



      public void sendEmailToMultiple(EmailDetails emailDetails,Map<String, MultipartFile> fileMap){

        this.from=emailDetails.getFrom();
        this.toAddress = emailDetails.getToAddress();
        this.ccAddress = emailDetails.getCcAddress();
        this.subject=emailDetails.getSubject();
        this.text=emailDetails.getText();

        setEmailAccountDetails();
        Session session1 = Session.getDefaultInstance(props);
        Message simpleMessage = new MimeMessage(session1);

        InternetAddress fromIntAddress = null;
        InternetAddress[] toIntAddress =  new InternetAddress[toAddress.length];
        InternetAddress[] ccIntAddress = null;

        for (int i = 0; i < toAddress.length; i++) {

            try {
                fromIntAddress = new InternetAddress(this.from);
                toIntAddress[i] = new InternetAddress(toAddress[i]);
            } catch (AddressException ae) {
                ae.printStackTrace();
            }
        }

        if (ccAddress != null && ccAddress.length !=0) {
            ccIntAddress = new InternetAddress[ccAddress.length];
            for (int i = 0; i < ccAddress.length; i++) {

                try {

                    ccIntAddress[i] = new InternetAddress(ccAddress[i]);
                } catch (AddressException ae) {
                    ae.printStackTrace();
                }
            }
        }
        try {

            Multipart multipart = new MimeMultipart();


            simpleMessage.setFrom(fromIntAddress);
            simpleMessage.setRecipients(RecipientType.TO, toIntAddress);
            simpleMessage.setRecipients(RecipientType.BCC, ccIntAddress);



            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message


             simpleMessage.setSubject(this.subject);
           // simpleMessage.setContent(this.text,"text/html; charset=utf-8");
            messageBodyPart.setContent(this.text, "text/html; charset=utf-8");

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            for (MultipartFile file : fileMap.values()) {

                messageBodyPart = new MimeBodyPart();
                javax.activation.DataSource source = new FileDataSource(file.getName());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getName());
                multipart.addBodyPart(messageBodyPart);

            }
            // Send the complete message parts
            simpleMessage.setContent(multipart);

            Transport transport = session1.getTransport("smtp");
            transport.connect (this.sendingHost,sendingPort, this.userName, this.password);
            transport.sendMessage(simpleMessage, simpleMessage.getAllRecipients());
            transport.close();


        } catch (MessagingException me) {
            me.printStackTrace();
            //  log.error("ERROR :Sending email to: " + to + " failed !!!",me);
        }

    }


    public void readMail(){

        /**this will print subject of all messages in the inbox of sender@gmail.com*/

        this.receivingHost = env.getProperty("imap.protocol");//for imap protocol

        Properties props2=System.getProperties();

        props2.setProperty("mail.store.protocol", "imaps");

        Session session2=Session.getDefaultInstance(props2, null);

        try {

            Store store=session2.getStore("imaps");

            store.connect(this.receivingHost,this.userName, this.password);

            Folder folder=store.getFolder("INBOX");//get inbox

            folder.open(Folder.READ_ONLY);//open folder only to read

            Message message[]=folder.getMessages();

            for(int i=0;i<message.length;i++){


                //  log.info(message[i].getSubject());
            }



            folder.close(true);

            store.close();

        }catch(Exception e) {


            // throws UnsupportedEncodingException
            // log.error("ERROR :Error in receiving mail:",e);

        }

    }

    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        //System.out.println("Hello World!");
        new MailSendUtilClient().sendMail(new EmailDetails());
    }

}
