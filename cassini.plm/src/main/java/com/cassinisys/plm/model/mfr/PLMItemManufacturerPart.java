package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Home on 4/25/2016.
 */
@Entity
@Table(name = "PLM_ITEMMANUFACTURERPART")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMItemManufacturerPart implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ITEMMANUFACTURERPART_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANUFACTURER_PART", nullable = false)
    private PLMManufacturerPart manufacturerPart;

    @Column(name = "ITEM", nullable = false)
    private Integer item;

    @Column(name = "STATUS", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mfr.ManufacturerPartStatus")})
    private ManufacturerPartStatus status;

    @Column(name = "NOTES", nullable = false)
    private String notes;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "MODIFIED_DATE",
            nullable = false
    )
    private Date modifiedDate = new Date();

    @Transient
    private String itemNumber;

    @Transient
    private PLMItem itemMasterObject;


}
