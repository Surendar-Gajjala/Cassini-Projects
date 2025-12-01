package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by CassiniSystems on 18-05-2017.
 */
@Entity
@Data
@Table(name = "PLM_LIFECYCLE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMLifeCycle implements Serializable {

    @Id
    @SequenceGenerator(name = "LIFECYCLE_ID_GEN", sequenceName = "LIFECYCLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIFECYCLE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE")
    @Fetch(FetchMode.SELECT)
    private List<PLMLifeCyclePhase> phases;

    @Transient
    private Boolean usedLifeCycle = Boolean.FALSE;


    public PLMLifeCyclePhase getPhaseByType(LifeCyclePhaseType type) {
        for (PLMLifeCyclePhase phase : phases) {
            if (phase.getPhaseType() == type) {
                return phase;
            }
        }
        return null;
    }

    public PLMLifeCyclePhase getPhaseByName(String name) {
        for (PLMLifeCyclePhase phase : phases) {
            if (phase.getPhase().equalsIgnoreCase(name)) {
                return phase;
            }
        }
        return null;
    }
}
