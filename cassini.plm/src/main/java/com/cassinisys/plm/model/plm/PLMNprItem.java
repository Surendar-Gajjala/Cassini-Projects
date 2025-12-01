package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 18-12-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_NPRITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMNprItem extends PLMItem {

    @Transient
    public List<ObjectAttribute> objectAttributes = new ArrayList<>();
    @Transient
    public List<ObjectAttribute> revisionObjectAttributes = new ArrayList<>();
    @Transient
    public List<PLMItemAttribute> itemTypeAttributes = new ArrayList<>();
    @Column(name = "NPR")
    private Integer npr;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "ASSIGNED_NUMBER")
    private Boolean assignedNumber = Boolean.FALSE;

    @Column(name = "TEMPORARY_NUMBER")
    private String temporaryNumber;

    public PLMNprItem() {
        super();
    }


}
