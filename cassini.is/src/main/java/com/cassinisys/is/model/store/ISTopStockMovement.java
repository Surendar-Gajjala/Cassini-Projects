package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.ObjectAtrributeDTO;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Entity
@Table(name = "IS_STOCKMOVEMENT")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "ROWID")
@ApiObject(name = "STORE")
public class ISTopStockMovement extends CassiniObject {

    @ApiObjectField(required = true)
    private Integer item;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ISTopStore store;

    @Column(name = "MOVEMENT", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.procm.MovementType")})
    @ApiObjectField(required = true)
    private MovementType movementType;

    @Column(name = "QUANTITY", nullable = false)
    @ApiObjectField(required = true)
    private Double quantity = 0.0;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP", nullable = false)
    @ApiObjectField(required = true)
    private Date timeStamp;

    @Column(name = "RECORDED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer recordedBy;

    @Column
    private Integer project;

    @Column(name = "OPENING_BALANCE")
    private Double openingBalance = 0.0;

    @Column(name = "CLOSING_BALANCE")
    private Double closingBalance = 0.0;

    @Transient
    private ItemDTO itemDTO = new ItemDTO();

    @Transient
    private String reference;

    @Transient
    private String receiveDate;

    @Transient
    private List<ObjectAtrributeDTO> itemAttributesList;

    public ISTopStockMovement() {
        super(ISObjectType.STOCKMOVEMENT);
    }

    public ISTopStockMovement(ISObjectType objectType) {
        super(objectType);
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public ISTopStore getStore() {
        return store;
    }

    public void setStore(ISTopStore store) {
        this.store = store;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(Integer recordedBy) {
        this.recordedBy = recordedBy;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Double getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(Double closingBalance) {
        this.closingBalance = closingBalance;
    }

    public ItemDTO getItemDTO() {
        return itemDTO;
    }

    public void setItemDTO(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public List<ObjectAtrributeDTO> getItemAttributesList() {
        return itemAttributesList;
    }

    public void setItemAttributesList(List<ObjectAtrributeDTO> itemAttributesList) {
        this.itemAttributesList = itemAttributesList;
    }

}
