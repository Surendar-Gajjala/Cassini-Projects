package com.cassinisys.drdo.model.transactions;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Entity
@Table(name = "INWARDSTATUSHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class InwardStatusHistory implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "INWARD_STATUS_HISTORY_ID_GEN", sequenceName = "INWARD_STATUS_HISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INWARD_STATUS_HISTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ApiObjectField
    @Column(name = "INWARD")
    private Integer inward;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.InwardStatus")})
    @Column(name = "OLD_STATUS")
    private InwardStatus oldStatus;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.transactions.InwardStatus")})
    @Column(name = "NEW_STATUS")
    private InwardStatus newStatus;

    @ApiObjectField
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP", nullable = false)
    private Date timestamp;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BY_USER")
    private Person user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInward() {
        return inward;
    }

    public void setInward(Integer inward) {
        this.inward = inward;
    }

    public InwardStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(InwardStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public InwardStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(InwardStatus newStatus) {
        this.newStatus = newStatus;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }
}
