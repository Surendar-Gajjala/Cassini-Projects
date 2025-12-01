package com.cassinisys.erp.model.hrm;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.converters.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by reddy on 10/10/15.
 */
@Entity
@Table(name = "ERP_HOLIDAY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPHoliday implements Serializable{
    @ApiObjectField
    private Integer id;
    @ApiObjectField
    private String name;
    @ApiObjectField
    private String description;
    @ApiObjectField
    private Date date;

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "DATE")
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

