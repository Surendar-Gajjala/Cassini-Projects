package com.cassinisys.drdo.model.bom;

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
 * Created by subramanyam reddy on 01-11-2018.
 */
@Entity
@Table(name = "ITEMINSTANCESTATUSHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class ItemInstanceStatusHistory implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "ITEM_STATUS_HISTORY_ID_GEN", sequenceName = "ITEM_STATUS_HISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_STATUS_HISTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEMINSTANCE")
    private ItemInstance itemInstance;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.bom.ItemInstanceStatus")})
    @Column(name = "STATUS")
    private ItemInstanceStatus status = ItemInstanceStatus.NEW;

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

    @ApiObjectField
    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "PRESENT_STATUS")
    private String presentStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemInstance getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(ItemInstance itemInstance) {
        this.itemInstance = itemInstance;
    }

    public ItemInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(ItemInstanceStatus status) {
        this.status = status;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPresentStatus() {
        return presentStatus;
    }

    public void setPresentStatus(String presentStatus) {
        this.presentStatus = presentStatus;
    }
}
