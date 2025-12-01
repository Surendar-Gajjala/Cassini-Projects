package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GSR on 19-06-2018.
 */
@Entity
@Data
@Table(name = "PLM_GLOSSARYREVISIONSTATUSHISTORY")
public class PLMGlossaryRevisionStatusHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "GLOSSARYREVISIONSTATUSHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "GLOSSARY")
    private Integer glossary;

    @Column(name = "REVISION")
    private String revision;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "FROM_STATUS")
    private PLMLifeCyclePhase fromStatus;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "TO_STATUS")
    private PLMLifeCyclePhase toStatus;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timeStamp;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "NOTES")
    private String notes;


}
