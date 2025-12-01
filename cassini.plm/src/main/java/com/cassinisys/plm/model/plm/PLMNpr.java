package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by GSR on 18-12-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_NPR")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMNpr extends CassiniObject {

    @Column(name = "NUMBER")
    private String number;
    @Column(name = "REQUESTER")
    private Integer requester;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.plm.model.plm.NPRStatus")})
    private NPRStatus status = NPRStatus.OPEN;
    @Column(name = "REASON_FOR_REQUEST")
    private String reasonForRequest;
    @Column(name = "REJECT_REASON")
    private String rejectReason;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    public PLMNpr() {
        super(PLMObjectType.PLMNPR);
    }


}
