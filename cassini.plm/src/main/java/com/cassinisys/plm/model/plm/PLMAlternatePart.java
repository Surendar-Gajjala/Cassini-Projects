package com.cassinisys.plm.model.plm;

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
 * Created by subramanyam on 13-12-2020.
 */
@Entity
@Data
@Table(name = "PLM_ALTERNATEPART")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMAlternatePart implements Serializable {

    @Id
    @SequenceGenerator(name = "ALTERNATE_PART_ID_GEN", sequenceName = "ALTERNATE_PART_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALTERNATE_PART_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PART")
    private Integer part;

    @Column(name = "REPLACEMENT_PART")
    private Integer replacementPart;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.plm.ReplacementType")})
    @Column(name = "DIRECTION")
    private ReplacementType direction = ReplacementType.ONEWAY;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate = new Date();

    }
