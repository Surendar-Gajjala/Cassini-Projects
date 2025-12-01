package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by subramanyamreddy on 023 23-May -17.
 */
@Entity
@Data
@Table(name = "PLM_ITEMREVISIONSTATUSHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItemRevisionStatusHistory {

    @Id
    @SequenceGenerator(name = "ITEMREVISIONSTATUSHISTORY_ID_GEN", sequenceName = "ITEMREVISIONSTATUSHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMREVISIONSTATUSHISTORY_ID_GEN")
    @Column(name = "ROWID")
    private Integer id;

    @Column(name = "ITEM")
    private Integer item;

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
