package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "MES_BOMITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESBOMItem extends CassiniObject {

    @Column(name = "MBOM_REVISION")
    private Integer mbomRevision;

    @Column(name = "BOMITEM")
    private Integer bomItem;

    @Column(name = "PHANTOM")
    private Integer phantom;

    @Column(name = "PARENT")
    private Integer parent;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mes.MESBomItemType")})
    @Column(name = "TYPE")
    private MESBomItemType type = MESBomItemType.NORMAL;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "MANUFACTURER_PART")
    private Integer manufacturerPart;

    @Transient
    private String phantomName;
    @Transient
    private String phantomNumber;

    public MESBOMItem() {
        super.setObjectType(MESEnumObject.MBOMITEM);
    }
}
