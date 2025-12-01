package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by lakshmi on 5/22/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "DISCUSSIONGROUP")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "GROUP_ID")
public class DiscussionGroup extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;


    @Column(name = "MODERATOR", nullable = false)
    private Integer moderator;


    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive=false;


    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType") })
    @Column(name = "CTX_OBJECT_TYPE", nullable = true)
    private ObjectType ctxObjectType;

    @Column(name = "CTX_OBJECT_ID", nullable = true)
    private Integer ctxObjectId;


    @Transient
    private Long messagesCount;

    @Transient
    private String personName;


    public DiscussionGroup(){
    super(ObjectType.DISCUSSIONGROUP);
    }



}