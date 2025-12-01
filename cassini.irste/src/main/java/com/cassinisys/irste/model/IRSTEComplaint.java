package com.cassinisys.irste.model;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Nageshreddy on 06-11-2018.
 */

@Data
@Entity
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "IRSTE_COMPLAINT")
public class IRSTEComplaint extends CassiniObject {

    @Column(name = "COMPLAINT_NUMBER")
    private String complaintNumber;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "UTILITY")
    private String utility;


    @Column(name = "GROUP_UTILITY")
    private String groupUtility;

    @Column(name = "DETAILS")
    private String details;

    @Column(name = "PERSON")
    private Integer person;

    @Column(name = "RESPONDER")
    private Integer responder;

    @Column(name = "ASSISTOR")
    private Integer assistor;

    @Column(name = "FACILITATOR")
    private Integer facilitator;

    @Transient
    private Person personObject;

    @Transient
    private String complainant;


    @Transient
    private Person assistorPersonObject;

    @Transient
    private Person facilitatorPersonObject;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.irste.model.ComplaintStatus")})
    @Column(name = "STATUS")
    private ComplaintStatus status = ComplaintStatus.NEW;

    public IRSTEComplaint() {
        super(IRSTEObjectType.COMPLAINT);
    }

}

