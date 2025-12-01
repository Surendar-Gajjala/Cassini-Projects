package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by subramanyam reddy on 07-10-2018.
 */
@Entity
@Table(name = "BOMINSTANCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class BomInstance extends CassiniObject {

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM", nullable = false)
    private ItemInstance item;

    @ApiObjectField
    @Column(name = "BOM", nullable = false)
    private Integer bom;

    @Column(name = "HAS_PARTTRACKING")
    private Boolean hasPartTracking = Boolean.FALSE;

    @ApiObjectField
    @Column(name = "PERCENTAGE")
    private Double percentage;

    @ApiObjectField
    @Column(name = "STATUS")
    private String status;

    public BomInstance() {
        super(DRDOObjectType.BOMINSTANCE);
    }

    public ItemInstance getItem() {
        return item;
    }

    public void setItem(ItemInstance item) {
        this.item = item;
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }

    public Boolean getHasPartTracking() {
        return hasPartTracking;
    }

    public void setHasPartTracking(Boolean hasPartTracking) {
        this.hasPartTracking = hasPartTracking;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
