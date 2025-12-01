package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 20-10-2017.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_SHAREDOBJECT")
@PrimaryKeyJoinColumn(name = "SHARE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSharedObject extends CassiniObject {

    @Column(name = "SHARED_OBJECT_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    private ObjectType sharedObjectType;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "SHARE_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.plm.model.plm.ShareType")})
    private ShareType shareType;

    @Column(name = "SHARED_TO")
    private Integer sharedTo;

    @Column(name = "SHARED_BY")
    private Integer sharedBy;

    @Column(name = "PERMISSION")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.plm.model.plm.SharePermission")})
    private SharePermission permission;

    @Column(name = "PARENT_OBJECT_ID")
    private Integer parentObjectId;

    @Column(name = "PARENT_OBJECT_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    private ObjectType parentObjectType;

    @Transient
    private List<Integer> sharedToObjects = new ArrayList<>();

    @Transient
    private Integer userId;

    @Transient
    private boolean added = (Boolean.FALSE);

    @Transient
    private List<String> existingItems;

    @Transient
    private List<String> newItems;


    public PLMSharedObject() {
        super(PLMObjectType.SHAREDOBJECT);
    }


}
