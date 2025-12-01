package com.cassinisys.plm.model.pqm;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_PPAP_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMPPAPType extends PQMQualityType {

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;
    @Transient
    private List<PQMPPAPType> childrens = new ArrayList<>();

    public PQMPPAPType() {
        super.setQualityType(QualityType.NCRTYPE);
    }

}
