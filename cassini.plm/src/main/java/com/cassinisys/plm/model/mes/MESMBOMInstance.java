package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by smukka on 04-10-2022.
 */
@Entity
@Table(name = "MES_MBOMINSTANCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESMBOMInstance extends CassiniObject {

    @Column(name = "PRODUCTION_ORDER_ITEM")
    private Integer productionOrderItem;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "BATCH_NUMBER")
    private String batchNumber;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MFG_DATE", nullable = false)
    private Date mfgDate;

    @Column(name = "MBOM_REVISION")
    private Integer mbomRevision;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "RELEASED")
    private Boolean released = Boolean.FALSE;

    @Column(name = "REJECTED")
    private Boolean rejected = Boolean.FALSE;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REJECTED_DATE", nullable = false)
    private Date rejectedDate;

    @Column(name = "REJECTED_REASON")
    private String rejectedReason;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISH_DATE", nullable = false)
    private Date finishDate;

    @Transient
    private String mbomName;
    @Transient
    private String mbomNumber;
    @Transient
    private String mbomRevisionName;

    public MESMBOMInstance() {
        super.setObjectType(MESEnumObject.MBOMINSTANCE);
    }

}
