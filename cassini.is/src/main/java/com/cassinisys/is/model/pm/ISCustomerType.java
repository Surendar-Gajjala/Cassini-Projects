package com.cassinisys.is.model.pm;
/* Model for ISCustomerType */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_CUSTOMERTYPE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PM")
public class ISCustomerType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "TYPE_CODE", nullable = false)
    @ApiObjectField(required = true)
    String typeCode;
    @Column(name = "NAME", nullable = false, unique = true)
    @ApiObjectField(required = true)
    String name;
    @Id
    @SequenceGenerator(name = "CUSTOMERTYPE_ID_GEN",
            sequenceName = "CUSTOMERTYPE_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "CUSTOMERTYPE_ID_GEN")
    @Column(name = "TYPE_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;
    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;

    public ISCustomerType() {
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        ISCustomerType other = (ISCustomerType) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
