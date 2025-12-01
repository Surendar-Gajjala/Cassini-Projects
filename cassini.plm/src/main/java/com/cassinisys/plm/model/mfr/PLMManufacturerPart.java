package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

/**
 * Created by Home on 4/25/2016.
 */
@Entity
@Table(name = "PLM_MANUFACTURERPART")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "MFRPART_ID")
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMManufacturerPart extends CassiniObject {

    @Column(name = "MFRPART_NUMBER", nullable = false)
    private String partNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MFRPART_TYPE")
    private PLMManufacturerPartType mfrPartType;

    @Column(name = "MFRPART_NAME", nullable = false)
    private String partName;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "STATUS", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mfr.ManufacturerPartStatus")})
    private ManufacturerPartStatus status;

    @Column(name = "MANUFACTURER", nullable = false)
    private Integer manufacturer;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "THUMBNAIL")
    private byte[] thumbnail;

    @Column(name = "SERIALIZED")
    private Boolean serialized = Boolean.TRUE;

    @Transient
    private Integer workflowDefId;

    @Transient
    private Integer count;

    @Transient
    private String mfrName;

    @Transient
    private String workflowStatus="None";
    
    @Transient
    private String statusPrint;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String phaseName;

    @Transient
    private String modifiedPerson;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    public PLMManufacturerPart() {
        super(PLMObjectType.MANUFACTURERPART);
    }
    
    @Transient
    private List<FileDto> inspectionReportFiles = new LinkedList<>();

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";

}
