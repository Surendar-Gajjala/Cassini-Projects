package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 18-05-2017.
 */
@Entity
@Data
@Table(name = "PLM_LIFECYCLEPHASE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMLifeCyclePhase implements Serializable {

    @Id
    @SequenceGenerator(name = "LIFECYCLEPHASE_ID_GEN", sequenceName = "LIFECYCLEPHASE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIFECYCLEPHASE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "LIFECYCLE")
    private Integer lifeCycle;

    @Column(name = "PHASE")
    private String phase;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.plm.LifeCyclePhaseType")})
    @Column(name = "PHASE_TYPE", nullable = true)
    private LifeCyclePhaseType phaseType;


}
