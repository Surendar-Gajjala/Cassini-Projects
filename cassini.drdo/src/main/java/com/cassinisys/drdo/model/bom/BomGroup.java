package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyam reddy on 08-11-2018.
 */
@Entity
@Table(name = "BOMGROUP")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class BomGroup extends CassiniObject {

    @ApiObjectField
    @Column(name = "NAME")
    private String name;

    @ApiObjectField
    @Column(name = "CODE")
    private String code;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.bom.BomItemType")})
    @Column(name = "TYPE")
    private BomItemType type = BomItemType.SECTION;

    @Column(name = "VERSITY")
    private Boolean versity = Boolean.FALSE;

    public BomGroup() {
        super(DRDOObjectType.BOMGROUP);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BomItemType getType() {
        return type;
    }

    public void setType(BomItemType type) {
        this.type = type;
    }

    public Boolean getVersity() {
        return versity;
    }

    public void setVersity(Boolean versity) {
        this.versity = versity;
    }
}
