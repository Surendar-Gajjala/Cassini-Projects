package com.cassinisys.plm.model.req;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 08-07-2021.
 */
@Entity
@Table(name = "PLM_REQUIREMENT_ITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMRequirementItem implements Serializable {

    @Id
    @SequenceGenerator(name = "REQUIREMENT_ITEM_ID_GEN", sequenceName = "REQUIREMENT_ITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENT_ITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUIREMENT_VERSION")
    private PLMRequirementVersion requirementVersion;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private PLMItemRevision item;

    @Transient
    private String personName;

    @Transient
    private Integer reqDoc;
    @Transient
    private Integer documentChildren;

}
