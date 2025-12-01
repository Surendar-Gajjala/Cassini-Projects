package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_MCO_AFFECTEDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMMCOAffectedItem extends CassiniObject {

    @Column(name = "MCO")
    private Integer mco;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.MCOChangeType")})
    @Column(name = "CHANGE_TYPE", nullable = true)
    private MCOChangeType changeType = MCOChangeType.REPLACED;

    @Column(name = "MATERIAL")
    private Integer material;

    @Column(name = "REPLACEMENT")
    private Integer replacement;

    @Column(name = "NOTES")
    private String notes;

    public PLMMCOAffectedItem() {
        super.setObjectType(PLMObjectType.MCOAFFECTEDITEM);
    }

}
