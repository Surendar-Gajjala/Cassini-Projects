package com.cassinisys.plm.model.req;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by reddy on 22/12/15.
 */
@Entity
@Data
@Table(name = "PLM_REQUIREMENTDOCUMENTREVISIONSTATUSHISTORY")
public class PLMRequirementDocumentRevisionStatusHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "REQUIREMENTDOCUMENTREVISIONSTATUSHISTORY_ID_GEN", sequenceName = "REQUIREMENTDOCUMENTREVISIONSTATUSHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTDOCUMENTREVISIONSTATUSHISTORY_ID_GEN")
    @Column(name = "ROWID")
    private Integer id;

    @Column(name = "DOCUMENTREVISION")
    private Integer documentRevision;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "OLD_STATUS")
    private PLMLifeCyclePhase oldStatus;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NEW_STATUS")
    private PLMLifeCyclePhase newStatus;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp = new Date();

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

}
