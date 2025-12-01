package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReqIFHeader {
    private String identifier;
    private String title;
    private String comment;
    private Date creationDate;
    private String repositoryId;
    private String reqIFToolId;
    private String reqIFVersion;
    private String sourceToolId;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getReqIFToolId() {
        return reqIFToolId;
    }

    public void setReqIFToolId(String reqIFToolId) {
        this.reqIFToolId = reqIFToolId;
    }

    public String getReqIFVersion() {
        return reqIFVersion;
    }

    public void setReqIFVersion(String reqIFVersion) {
        this.reqIFVersion = reqIFVersion;
    }

    public String getSourceToolId() {
        return sourceToolId;
    }

    public void setSourceToolId(String sourceToolId) {
        this.sourceToolId = sourceToolId;
    }

    public Element toXml() {
        Element elem = new Element("THE-HEADER");
        Element elemReIFHeader = new Element("REQ-IF-HEADER");
        elemReIFHeader.setAttribute("IDENTIFIER", UUID.randomUUID().toString());
        Element e = new Element("TITLE");
        e.setText(title);
        elemReIFHeader.addContent(e);
        e = new Element("COMMENT");
        e.setText(comment);
        elemReIFHeader.addContent(e);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        e = new Element("CREATION-TIME");
        e.setText(df.format(creationDate));
        elemReIFHeader.addContent(e);
        e = new Element("REQ-IF-TOOL-ID");
        e.setText(reqIFToolId);
        elemReIFHeader.addContent(e);
        e = new Element("REQ-IF-VERSION");
        e.setText(reqIFVersion);
        elemReIFHeader.addContent(e);
        e = new Element("SOURCE-TOOL-ID");
        e.setText(sourceToolId);
        elemReIFHeader.addContent(e);
        elem.addContent(elemReIFHeader);
        return elem;
    }
}
