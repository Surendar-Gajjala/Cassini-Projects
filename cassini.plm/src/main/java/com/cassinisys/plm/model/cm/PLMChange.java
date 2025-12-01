package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by reddy on 08/01/16.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_CHANGE")
@PrimaryKeyJoinColumn(name = "CHANGE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMChange extends CassiniObject {

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.ChangeType")})
    @Column(name = "CHANGE_TYPE", nullable = true)
    private ChangeType changeType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CHANGE_CLASS")
    private PLMChangeType changeClass;

    @Column(name = "CHANGE_REASON_TYPE")
    private String changeReasonType;

    @Column(name = "WORKFLOW_STATUS")
    private Integer workflowStatus;

    @Column(name = "REVISION_CREATION_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.RevisionCreationType")})
    private RevisionCreationType revisionCreationType;

    @Column(name = "REVISIONS_CREATED")
    private Boolean revisionsCreated = Boolean.FALSE;

    @Transient
    private Integer workflowDefinition;

    public PLMChange() {
        super(PLMObjectType.CHANGE);
    }


}
