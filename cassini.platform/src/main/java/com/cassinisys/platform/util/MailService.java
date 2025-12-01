package com.cassinisys.platform.util;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.dto.MailSettingsDto;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lakshmi on 20-05-2017.
 */

@Service
public class MailService implements EmailService {

    @Autowired
    public JavaMailSenderImpl mailSender;
    @Autowired
    Configuration fmConfiguration;
    @Autowired
    private Environment env;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private MessageSource messageSource;

    @Transactional
    public void sendEmail(Mail mail) {
        MailSettingsDto mailSettingsDto = null;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Preference mailPreference = preferenceRepository.findByPreferenceKey("SYSTEM.EMAIL_SETTINGS");
        if (mailPreference != null && mailPreference.getJsonValue() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                mailSettingsDto = objectMapper.readValue(mailPreference.getJsonValue(), MailSettingsDto.class);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            if (mailSettingsDto != null) {
                mailSender.setUsername(mailSettingsDto.getUserName());
                mailSender.setPassword(mailSettingsDto.getPassword());

                mimeMessageHelper.setFrom(mailSettingsDto.getUserName());

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
                props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
                props.put("mail.smtp.host", mailSettingsDto.getHost());
                props.put("mail.smtp.port", mailSettingsDto.getPort());
                props.put("mail.smtp.ssl.trust", mailSettingsDto.getSslTrust());
                props.put("mail.smtp.from", mailSettingsDto.getUserName());
            } else {
                mailSender.setUsername(env.getProperty("mail.username"));
                mailSender.setPassword(env.getProperty("mail.password"));
                mimeMessageHelper.setFrom(env.getProperty("mail.username"));

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
                props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
                props.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
                props.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
                props.put("mail.smtp.ssl.trust", env.getProperty("mail.smtp.ssl.trust"));
                props.put("mail.smtp.from", env.getProperty("mail.username"));
            }

            if (mail.getMailSubject() != null)
                mimeMessageHelper.setSubject(mail.getMailSubject());

            if (mail.getMailTo() != null)
                mimeMessageHelper.setTo(mail.getMailTo());

            if (mail.getMailToList() != null && mail.getMailToList().length > 0)
                mimeMessageHelper.setTo(mail.getMailToList());

            if (mail.getMailCc() != null)
                mimeMessageHelper.setCc(mail.getMailCc());

            if (mail.getMailCcList() != null && mail.getMailCcList().length > 0)
                mimeMessageHelper.setCc(mail.getMailCcList());

            if (mail.getMailBccList() != null && mail.getMailBccList().length > 0)
                mimeMessageHelper.setBcc(mail.getMailBccList());

            if (mail.getMailBcc() != null)
                mimeMessageHelper.setBcc(mail.getMailBcc());

            if (mail.getTemplatePath() != null) {
                mail.setMailContent(geContentFromTemplate(mail.getTemplatePath(), mail.getModel()));
            }

            mimeMessageHelper.setText(mail.getMailContent(), true);

            if (mail.getPathToAttachment() != null && mail.getPathToAttachment().length() > 0) {

                FileSystemResource file = new FileSystemResource(new File(mail.getPathToAttachment()));
                mimeMessageHelper.addAttachment(file.getFilename(), file);

            }

            if (mail.getAttachmentPaths().size() > 0) {

                for (String path : mail.getAttachmentPaths()) {
                    FileSystemResource file = new FileSystemResource(new File(path));
                    mimeMessageHelper.addAttachment(file.getFilename(), file);
                }
            }

            try {
                mailSender.send(mimeMessageHelper.getMimeMessage());
            } catch (MailAuthenticationException e) {
                e.printStackTrace();
                throw new CassiniException(messageSource.getMessage("mail_authentication_failed", null, "Mail authentication failed. Please contact administrator", LocaleContextHolder.getLocale()));
            } catch (MailSendException e) {
                e.printStackTrace();
                throw new CassiniException(messageSource.getMessage("mail_connection_failed", null, "Mail server connection failed", LocaleContextHolder.getLocale()));
            }


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Transactional
    public String geContentFromTemplate(String templatePath, Map<String, Object> model) {
        StringBuffer content = new StringBuffer();

        try {

            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/templates/");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate(templatePath), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Transactional
    public void sendSimpleMessage(Mail mail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail.getMailTo());
            message.setSubject(mail.getMailSubject());
            message.setText(mail.getMailContent());

            if (mail.getMailCcList() != null && mail.getMailCcList().length > 0) {
                message.setCc(mail.getMailCcList());
            }

            if (mail.getMailBccList() != null && mail.getMailBccList().length > 0) {

                message.setBcc(mail.getMailBccList());
            }

            mailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }


}