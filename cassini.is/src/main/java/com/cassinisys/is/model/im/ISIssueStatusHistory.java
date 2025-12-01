package com.cassinisys.is.model.im;
/**
 * Model for ISIssueStatusHistory
 */

import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "IS_ISSUESTATUSHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "IM")
public class ISIssueStatusHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ISSUESTATUSHISTORY_ID_GEN",
            sequenceName = "ROW_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ISSUESTATUSHISTORY_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "ISSUE", nullable = false)
    @ApiObjectField(required = true)
    private Integer issue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE", nullable = false)
    @ApiObjectField(required = true)
    private Date modifiedDate;

    @Column(name = "MODIFIED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer modifiedBy;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.im.IssueStatus")})
    @Column(name = "OLD_STATUS", nullable = true)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    @ApiObjectField
    private IssueStatus oldStatus;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.im.IssueStatus")})
    @Column(name = "NEW_STATUS", nullable = false)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    @ApiObjectField
    private IssueStatus newStatus;

    public ISIssueStatusHistory() {
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public IssueStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(IssueStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public IssueStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(IssueStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISIssueStatusHistory other = (ISIssueStatusHistory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
