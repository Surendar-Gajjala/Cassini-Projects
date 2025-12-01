package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_WORKORDER_PART")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MROWorkOrderPart implements Serializable {

    @Id
    @SequenceGenerator(name = "MROWORKORDERPART_ID_GEN", sequenceName = "MROWORKORDERPART_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MROWORKORDERPART_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORK_ORDER")
    private Integer workOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PART")
    private MROSparePart sparePart;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "SERIAL_NUMBERS")
    @Type(
            type = "com.cassinisys.platform.util.converter.StringArrayType"
    )
    private String[] serialNumbers = new String[0];

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.PartDispositionType")})
    @Column(name = "DISPOSITION", nullable = true)
    private PartDispositionType disposition = PartDispositionType.REPLACE;

    @Column(name = "NOTES")
    private String notes;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate = new Date();
}
