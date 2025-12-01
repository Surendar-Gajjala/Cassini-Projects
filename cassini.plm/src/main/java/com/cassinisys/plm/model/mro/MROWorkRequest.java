package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.Media;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_WORK_REQUEST")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROWorkRequest extends MROObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MROWorkRequestType type;

    @Column(name = "ASSET")
    private Integer asset;

    @Column(name = "REQUESTOR")
    private Integer requestor;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.WorkPriorityType")})
    @Column(name = "PRIORITY", nullable = true)
    private WorkPriorityType priority = WorkPriorityType.LOW;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.WorkRequestStatusType")})
    @Column(name = "STATUS", nullable = true)
    private WorkRequestStatusType status = WorkRequestStatusType.PENDING;

    @Column(name = "ATTACHMENTS")
    @Type(type = "com.cassinisys.platform.util.converter.IntArrayUserType")
    private Integer[] attachments;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private List<MROObjectAttribute> mroObjectAttributes = new ArrayList<>();

    @Transient
    private String assetName;

    @Transient
    private List<Media> images = new ArrayList<>();

    @Transient
    private List<Media> videos = new ArrayList<>();

    @Transient
    private List<Attachment> attachmentList = new ArrayList<>();

    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;
    @Transient
    private String requestorPerson;

    @Transient
    private String priorityName;

    @Transient
    private String statusName;

    @Transient
    private String modifiedPerson;

    public MROWorkRequest() {
        super.setObjectType(MROEnumObject.MROWORKREQUEST);
    }
}
