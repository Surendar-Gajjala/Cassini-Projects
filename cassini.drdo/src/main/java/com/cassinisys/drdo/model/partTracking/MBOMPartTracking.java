package com.cassinisys.drdo.model.partTracking;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Entity
@Table(name = "MBOMPARTTRACKING")
public class MBOMPartTracking implements Serializable {

    @Id
    @SequenceGenerator(name = "PARTTRACKINGITEM_ID_GEN", sequenceName = "PARTTRACKINGITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTTRACKINGITEM_ID_GEN")
    private Integer id;

    @ApiObjectField(required = true)
    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "PARENT")
    @ApiObjectField(required = true)
    private Integer parent;

    @ApiObjectField
    @Column(name = "HASPARTTRACKING")
    private boolean hasPartTracking;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PERCENTAGE")
    private Double percentage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public boolean isHasPartTracking() {
        return hasPartTracking;
    }

    public void setHasPartTracking(boolean hasPartTracking) {
        this.hasPartTracking = hasPartTracking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
