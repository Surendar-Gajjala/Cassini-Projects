package com.cassinisys.erp.model.production;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by reddy on 27/02/16.
 */
@Entity
@Table(name = "ERP_PRODUCTIONORDERITEMMATERIALPROPERTY")
@ApiObject(group = "PRODUCTION")
public class ERPProductionItemBomMaterialProperty {

    @ApiObjectField (required = true)
    private Integer id;

    @ApiObjectField (required = true)
    private Integer item;

    @ApiObjectField (required = true)
    private Integer bomItem;

    @ApiObjectField (required = true)
    private Integer property;

    @ApiObjectField (required = true)
    private String value;

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ITEM")
    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    @Column(name = "BOMITEM")
    public Integer getBomItem() {
        return bomItem;
    }

    public void setBomItem(Integer bomItem) {
        this.bomItem = bomItem;
    }

    @Column(name = "PROPERTY")
    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }

    @Column(name = "VALUE")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
