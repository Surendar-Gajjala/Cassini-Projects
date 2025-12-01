package com.cassinisys.irste.model;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@Data
@Entity
@Table(name = "IRSTE_COMPLAINTHISTORY")
public class IRSTEComplaintHistory implements Serializable {

    private static IRSTEComplaintHistory singleInstance = null;
    @Id
    @SequenceGenerator(name = "IRSTE_COMPLAINTHISTORY_ID_GEN", sequenceName = "IRSTE_COMPLAINTHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IRSTE_COMPLAINTHISTORY_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "COMPLAINT")
    private Integer complaint;
    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;
    @Column(name = "SUBMITTED_BY")
    private Integer submittedBy;
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.irste.model.ComplaintStatus")})
    @Column(name = "NEW_STATUS")
    private ComplaintStatus newStatus;
    @JsonSerialize(
            using = CustomDateSerializer.class
    )
    @JsonDeserialize(
            using = CustomDateDeserializer.class
    )
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "MODIFIED_DATE",
            nullable = false
    )
    @ApiObjectField(
            required = true
    )
    private Date modifiedDate = new Date();

    public static IRSTEComplaintHistory getInstance() {
        singleInstance = new IRSTEComplaintHistory();
        return singleInstance;
    }

}
