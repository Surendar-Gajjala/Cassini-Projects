package com.cassinisys.plm.model.pqm;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by smukka on 16-03-2022.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_PPAP_CHECKLIST")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMPPAPChecklist extends PLMFile {

    @Column(name = "PPAP")
    private Integer ppap;

    @Column(name = "REVISION")
    private String revision;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    public PQMPPAPChecklist() {
        super.setObjectType(PLMObjectType.PPAPCHECKLIST);
    }
}
