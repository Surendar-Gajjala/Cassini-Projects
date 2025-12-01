package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.ObjectAtrributeDTO;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Entity
@Table(name = "CUSTOM_ROADCHALANITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "ID")
@ApiObject(name = "STORE")
public class CustomRoadChalanItem extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROAD_CHALAN")
    @ApiObjectField(required = true)
    @JsonIgnore
    private CustomRoadChalan roadChalan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MATERIAL")
    @ApiObjectField(required = true)
    private ISMaterialItem materialItem;

    @ApiObjectField(required = true)
    @Column(name = "QUANTITY")
    private Double quantity = 1.0;

    @ApiObjectField(required = true)
    @Column(name = "NOTES")
    private String notes;
    @Transient
    private ItemDTO itemDTO = new ItemDTO();
    @Transient
    private String reference;
    @Transient
    private String receiveDate;
    @Transient
    private List<ObjectAtrributeDTO> itemAttributesList;

    public CustomRoadChalanItem() {
        super(ISObjectType.ROADCHALLANITEM);
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public CustomRoadChalan getRoadChalan() {
        return roadChalan;
    }

    public void setRoadChalan(CustomRoadChalan roadChalan) {
        this.roadChalan = roadChalan;
    }

    public ISMaterialItem getMaterialItem() {
        return materialItem;
    }

    public void setMaterialItem(ISMaterialItem materialItem) {
        this.materialItem = materialItem;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
