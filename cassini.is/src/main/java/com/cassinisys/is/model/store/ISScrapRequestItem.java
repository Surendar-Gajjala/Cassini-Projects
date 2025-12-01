package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.ObjectAtrributeDTO;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Varsha Malgireddy on 8/28/2018.
 */
@Entity
@Table(name = "IS_SCRAPREQUESTITEM")
@PrimaryKeyJoinColumn(name = "ROWID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "SCRAPITEM")
public class ISScrapRequestItem extends CassiniObject {

    @Column(name = "QUANTITY")
    private Double quantity = 0.0;

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "IS_SCRAPREQUEST")
    private Integer scrapRequest;

    @Transient
    private ItemDTO itemDTO = new ItemDTO();
    @Transient
    private List<ObjectAtrributeDTO> itemAttributesList;

    public ISScrapRequestItem() {
        super(ISObjectType.SCRAPREQUESTITEM);
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public ItemDTO getItemDTO() {
        return itemDTO;
    }

    public void setItemDTO(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
    }

    public Integer getScrapRequest() {
        return scrapRequest;
    }

    public void setScrapRequest(Integer scrapRequest) {
        this.scrapRequest = scrapRequest;
    }

    public List<ObjectAtrributeDTO> getItemAttributesList() {
        return itemAttributesList;
    }

    public void setItemAttributesList(List<ObjectAtrributeDTO> itemAttributesList) {
        this.itemAttributesList = itemAttributesList;
    }
}



