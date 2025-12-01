package com.cassinisys.platform.util;

/**
 * Created by lakshmi on 7/10/2016.
 */
public class EmailDetails {

    private String from;

    private String to;

    private String subject;

    private String text;

    private String[] toAddress;

    private String[] ccAddress;

    private String templateUrl;

    public EmailDetails(){

    }

    public EmailDetails(String from, String to, String subject, String text, String[] toAddress, String[] ccAddress) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.toAddress = toAddress;
        this.ccAddress = ccAddress;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getToAddress() {
        return toAddress;
    }

    public void setToAddress(String[] toAddress) {
        this.toAddress = toAddress;
    }

    public String[] getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String[] ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }
}
