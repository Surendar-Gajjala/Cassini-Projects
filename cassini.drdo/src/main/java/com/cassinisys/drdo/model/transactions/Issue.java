package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.BomInstance;
import com.cassinisys.drdo.model.dto.IssueItemDto;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Entity
@Table(name = "ISSUE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Issue extends CassiniObject {

    @Column(name = "NUMBER")
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUEST")
    private Request request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOMINSTANCE")
    private BomInstance bomInstance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ISSUED_TO")
    private Person issuedTo;

    @Column(name = "NOTES")
    private String notes;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.IssueStatus")})
    @Column(name = "STATUS")
    private IssueStatus status = IssueStatus.BDL_QC;

    @Column(name = "VERSITY")
    private Boolean versity = Boolean.FALSE;

    @Transient
    private Boolean provisionalApprove = Boolean.FALSE;

    @Transient
    private List<IssueItemDto> issueItemDtos = new ArrayList<>();

    public Issue() {
        super(DRDOObjectType.ISSUE);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public BomInstance getBomInstance() {
        return bomInstance;
    }

    public void setBomInstance(BomInstance bomInstance) {
        this.bomInstance = bomInstance;
    }

    public Person getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(Person issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<IssueItemDto> getIssueItemDtos() {
        return issueItemDtos;
    }

    public void setIssueItemDtos(List<IssueItemDto> issueItemDtos) {
        this.issueItemDtos = issueItemDtos;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public Boolean getProvisionalApprove() {
        return provisionalApprove;
    }

    public void setProvisionalApprove(Boolean provisionalApprove) {
        this.provisionalApprove = provisionalApprove;
    }

    public Boolean getVersity() {
        return versity;
    }

    public void setVersity(Boolean versity) {
        this.versity = versity;
    }
}
