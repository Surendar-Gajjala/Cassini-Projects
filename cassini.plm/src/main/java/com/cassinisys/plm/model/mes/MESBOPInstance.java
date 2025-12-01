package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by smukka on 04-10-2022.
 */
@Entity
@Table(name = "MES_BOPINSTANCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESBOPInstance extends CassiniObject {

    @Column(name = "MBOMINSTANCE")
    private Integer mbomInstance;

    @Column(name = "BOP_REVISION")
    private Integer bopRevision;

    public MESBOPInstance() {
        super.setObjectType(MESEnumObject.BOPINSTANCE);
    }

}
