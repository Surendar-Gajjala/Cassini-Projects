package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.EmailMessage;
import com.cassinisys.platform.model.col.MailServer;
import com.cassinisys.platform.model.col.ObjectMailSettings;
import com.cassinisys.platform.repo.col.MailServerRepository;
import com.cassinisys.platform.repo.col.ObjectMailSettingsRepository;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.util.BASE64DecoderStream;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class EmailMessageService {
    @Value("classpath:mime.types.txt")
    private Resource reportsResource;
    @Autowired
    private MailServerRepository mailServerRepository;
    @Autowired
    private ObjectMailSettingsRepository objectMailSettingsRepository;

    private MimetypesFileTypeMap mimeTypes = null;


    public List<EmailMessage> getEmailMessages(Integer objectId, Pageable pageable) {
        List<EmailMessage> emailMessages = new ArrayList<>();
        int start = 0;
        int end = 0;

        try {
            Store store = getStore(objectId);
            if(store == null) return emailMessages;

            IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
            if(!folder.isOpen()) {
                folder.open(Folder.READ_ONLY);
            }
            if(pageable.getPageSize() == 1) {
                start = 1;
            }
            else  {
                start = ((pageable.getPageNumber() - 1) * pageable.getPageSize()) + 1;
            }
            end = (pageable.getPageNumber() * pageable.getPageSize());
            if(end > folder.getMessageCount()) {
                end = folder.getMessageCount();
            }
            if(start <= folder.getMessageCount()) {
                Message[] messages = folder.getMessages(start, end);
                Arrays.sort(messages, (Object o1, Object o2) -> {
                    Message m1 = (Message)o1;
                    Message m2 = (Message)o2;

                    try {
                        return m2.getSentDate().compareTo(m1.getSentDate());
                    } catch (MessagingException e) {
                    }
                    return 0;
                });

                for (Message msg : messages) {
                    EmailMessage emailMessage = new EmailMessage();
                    emailMessage.setId(msg.getMessageNumber());
                    emailMessage.setSubject(msg.getSubject());

                    InternetAddress internetAddress = (InternetAddress) msg.getFrom()[0];
                    String person = internetAddress.getPersonal();
                    if(person == null || person.trim().isEmpty()) {
                        person = internetAddress.getAddress();
                    }
                    emailMessage.setFrom(person);
                    emailMessage.setTimestamp(msg.getReceivedDate());

                    Address[] addresses = msg.getAllRecipients();
                    for(Address address : addresses) {
                        internetAddress = (InternetAddress) address;
                        person = internetAddress.getPersonal();

                        if(person == null || person.trim().isEmpty()) {
                            person = internetAddress.getAddress();
                        }
                        emailMessage.getRecipients().add(person);
                    }

                    String contentType = msg.getContentType();
                    String msgText = "";
                    if (contentType.contains("multipart")) {
                        Multipart multiPart = (Multipart) msg.getContent();

                        for (int i = 0; i < multiPart.getCount(); i++) {
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                emailMessage.getAttachments().add(part.getFileName());
                            }
                            else if (part.getContent() instanceof BASE64DecoderStream){
                            /*
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            part.getDataHandler().writeTo(bos);
                            String decodedContent = bos.toString();
                            msgText += "<img src=\"data:image/jpeg;base64," + decodedContent + "\">";
                            */
                            }
                            else {
                                msgText += part.getContent().toString();
                            }
                        }
                        emailMessage.setMessage(msgText);
                        try {
                            emailMessage.setMessageText(Jsoup.parse(msgText).text());
                        } catch (Exception e) {
                        }
                    }
                    else {
                        emailMessage.setMessage(msg.getContent().toString());
                    }
                    if(emailMessages.size() > 0) {
                        emailMessages.get(0).setTotalMessages(folder.getMessageCount());
                    }
                    emailMessages.add(emailMessage);
                }
            }

            folder.close(true);
            store.close();
        } catch (MessagingException | IOException e) {
            throw new CassiniException("Error getting email messages. REASON: " + e.getMessage());
        }
        return emailMessages;
    }

    public void downloadAttachment(Integer objectId, Integer messageNumber, String fileName, HttpServletResponse response) {
        try {
            OutputStream out = response.getOutputStream();
            response.setContentType(getMimeType(fileName));

            if(fileName.indexOf(" ") != -1) {
                response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
            }
            else {
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            }

            Store store = getStore(objectId);
            if(store == null) return;
            IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
            if(!folder.isOpen()) {
                folder.open(Folder.READ_ONLY);
            }
            Message message = folder.getMessage(messageNumber);
            if(message == null) return;

            String contentType = message.getContentType();
            if (contentType.contains("multipart")) {
                Multipart multiPart = (Multipart) message.getContent();

                for (int i = 0; i < multiPart.getCount(); i++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) &&
                            part.getFileName().equalsIgnoreCase(fileName)) {
                        InputStream is = part.getInputStream();
                        IOUtils.copy(is, out);
                        break;
                    }
                }
            }
            folder.close(true);
            store.close();
            out.flush();
        } catch (MessagingException | IOException e) {
            throw new CassiniException((e.getMessage()));
        }
    }

    private Store getStore(Integer objectId) throws MessagingException {
        Store store = null;
        ObjectMailSettings objectMailSettings = objectMailSettingsRepository.findByObjectId(objectId);
        if (objectMailSettings != null) {
            MailServer mailServer = mailServerRepository.findOne(objectMailSettings.getMailServer());
            if (mailServer != null) {
                String host = mailServer.getImapServer();
                Integer port = mailServer.getImapPort();
                String username = objectMailSettings.getReceiverEmail();
                String password = objectMailSettings.getReceiverPassword();

                Properties props = System.getProperties();
                props.setProperty("mail.store.protocol", "imaps");
                Session session = Session.getDefaultInstance(props, null);

                store = session.getStore("imaps");
                store.connect(host, port, username, password);
            }
        }

        return store;
    }


    private String getMimeType(String fileName) {
        try {
            if(mimeTypes == null) {
                mimeTypes = new MimetypesFileTypeMap(reportsResource.getInputStream());
            }

            return mimeTypes.getContentType(fileName);
        } catch (IOException e) {
            return null;
        }
    }

}
