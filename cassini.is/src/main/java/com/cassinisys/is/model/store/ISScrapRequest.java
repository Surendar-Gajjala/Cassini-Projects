package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Varsha Malgireddy on 8/28/2018.
 */
@Entity
@Table(name = "IS_SCRAPREQUEST")
@PrimaryKeyJoinColumn(name = "ROWID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "SCRAP")
public class ISScrapRequest extends CassiniObject {

    @Column(name = "SCRAP_NUMBER")
    private String scrapNumber;

    @Column(name = "REQUESTED_BY")
    private Integer requestedBy;

    @Column(name = "PROJECT")
    private Integer project;

    @Column(name = "STORE")
    private Integer store;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private List<ISScrapRequestItem> items;

    public ISScrapRequest() {
        super(ISObjectType.SCRAPREQUEST);
    }

    public String getScrapNumber() {
        return scrapNumber;
    }

    public void setScrapNumber(String scrapNumber) {
        this.scrapNumber = scrapNumber;
    }

    public Integer getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Integer requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ISScrapRequestItem> getItems() {
        return items;
    }

    public void setItems(List<ISScrapRequestItem> items) {
        this.items = items;
    }
}
