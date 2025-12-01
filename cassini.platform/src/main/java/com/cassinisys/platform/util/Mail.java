package com.cassinisys.platform.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lakshmi on 25-05-2017.
 */
public class Mail {

    private String mailFrom;

    private String templatePath;

    private String mailTo;

    private String[] mailToList;

    private String mailCc;

    private String[] mailCcList;

    private String mailBcc;

    private String[] mailBccList;

    private String mailSubject;

    private String mailContent;

    private String contentType;

    private String pathToAttachment;

    private List<String> attachmentPaths = new ArrayList<>();

    private List<Object> attachments;

    private Map<String, Object> model;

    public Mail() {
        contentType = "text/plain";
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMailBcc() {
        return mailBcc;
    }

    public void setMailBcc(String mailBcc) {
        this.mailBcc = mailBcc;
    }

    public String getMailCc() {
        return mailCc;
    }

    public void setMailCc(String mailCc) {
        this.mailCc = mailCc;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public Date getMailSendDate() {
        return new Date();
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public String[] getMailToList() {
        return mailToList;
    }

    public void setMailToList(String[] mailToList) {
        this.mailToList = mailToList;
    }

    public String getPathToAttachment() {
        return pathToAttachment;
    }

    public void setPathToAttachment(String pathToAttachment) {
        this.pathToAttachment = pathToAttachment;
    }

    public String[] getMailCcList() {
        return mailCcList;
    }

    public void setMailCcList(String[] mailCcList) {
        this.mailCcList = mailCcList;
    }

    public String[] getMailBccList() {
        return mailBccList;
    }

    public void setMailBccList(String[] mailBccList) {
        this.mailBccList = mailBccList;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }
}