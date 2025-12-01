package com.cassinisys.erp.model.production;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by reddy on 27/02/16.
 */
@Entity
@Table(name = "ERP_MATERIALPROPERTY")
@ApiObject(group = "PRODUCTION")
public class ERPMaterialProperty {

    @ApiObjectField(required = true)
    private Integer id;

    @ApiObjectField(required = true)
    private Integer material;

    @ApiObjectField(required = true)
    private String property;

    @ApiObjectField(required = true)
    private Integer lov;

    @ApiObjectField(required = true)
    private String defaultValue;

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

    @Column(name = "MATERIAL")
    public Integer getMaterial() {
        return material;
    }

    public void setMaterial(Integer material) {
        this.material = material;
    }

    @Column(name = "PROPERTY")
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Column(name = "LOV")
    public Integer getLov() {
        return lov;
    }

    public void setLov(Integer lov) {
        this.lov = lov;
    }

    @Column(name = "DEFAULT_VALUE")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
