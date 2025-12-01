package com.cassinisys.plm.model.pm;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyamreddy on 031 31-Jan -18.
 */
@Entity
@Data
@Table(name = "PLM_PROJECTITEMREFERENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProjectItemReference implements Serializable {

    @Id
    @SequenceGenerator(name = "PROJECTITEMREFERENCE_ID_GEN", sequenceName = "PROJECTITEMREFERENCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTITEMREFERENCE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT")
    private PLMProject project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private PLMItemRevision item;

    @Transient
    private PLMItem plmItem;


}

