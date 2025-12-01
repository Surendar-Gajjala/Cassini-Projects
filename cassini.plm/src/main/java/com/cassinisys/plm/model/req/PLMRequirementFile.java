package com.cassinisys.plm.model.req;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTFILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementFile extends PLMFile {

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUIREMENT")
    private PLMRequirementVersion requirement;

}
