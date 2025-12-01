package com.cassinisys.is.model.procm;
/* Model for ISRequisitionApproval */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_REQUISITIONAPPROVAL")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISRequisitionApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "REQUISITIONAPPROVAL_ID_GEN",
            sequenceName = "ROW_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "REQUISITIONAPPROVAL_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "REQUISITION", nullable = false)
    @ApiObjectField(required = true)
    private Integer requisition;

    @Column(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Integer person;

    @Column(name = "APPROVED")
    @ApiObjectField(required = true)
    private boolean approved;

    public ISRequisitionApproval() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getRequisition() {
        return requisition;
    }

    public void setRequisition(Integer requisition) {
        this.requisition = requisition;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
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
        ISRequisitionApproval other = (ISRequisitionApproval) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
