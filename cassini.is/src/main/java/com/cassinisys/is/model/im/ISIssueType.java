package com.cassinisys.is.model.im;
/**
 * Model for ISIssueType
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_ISSUETYPE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "IM")
public class ISIssueType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ISSUETYPE_GEN_ID",
            sequenceName = "ISSUETYPE_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ISSUETYPE_GEN_ID")
    @Column(name = "TYPE_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "LABEL", nullable = false)
    @ApiObjectField(required = true)
    private String label;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    public ISIssueType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        ISIssueType other = (ISIssueType) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
