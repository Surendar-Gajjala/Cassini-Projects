package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "RM_REQUIREMENTREVISIONHISTORY")
public class RequirementRevisionHistory implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "REQUIREMNTREVISIONHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REQUIREMENT")
    private Integer requirement;

    @Column(name = "FROM_REVISION")
    private String fromRevision;

    @Column(name = "TO_REVISION")
    private String toRevision;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timeStamp;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;


}