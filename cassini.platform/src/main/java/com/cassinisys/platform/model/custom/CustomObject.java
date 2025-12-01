package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "CUSTOM_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObject extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private CustomObjectType type;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "SUPPLIER")
    private Integer supplier;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Boolean hasBom = Boolean.FALSE;
    @Transient
    private Boolean hasFiles = Boolean.FALSE;
    @Transient
    private String supplierName;
    @Transient
    private String supplierEmail;
    @Transient
    private String workflowStatus="None";
    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private List<CustomObjectFile> itemFiles = new ArrayList<>();

    public CustomObject() {
        super.setObjectType(ObjectType.CUSTOMOBJECT);
    }
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";
}
