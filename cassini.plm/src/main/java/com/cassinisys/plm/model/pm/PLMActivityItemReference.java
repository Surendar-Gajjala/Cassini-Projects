package com.cassinisys.plm.model.pm;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 20-06-2018.
 */
@Entity
@Data
@Table(name = "PLM_ACTIVITYITEMREFERENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMActivityItemReference implements Serializable {

    @Id
    @SequenceGenerator(name = "ACTIVITYITEMREFERENCE_ID_GEN", sequenceName = "ACTIVITYITEMREFERENCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITYITEMREFERENCE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ACTIVITY")
    private Integer activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private PLMItemRevision item;

    @Transient
    private PLMItem plmItem;

}
