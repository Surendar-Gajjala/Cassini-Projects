package com.cassinisys.platform.model.wfm;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rajabrahmachary on 16-05-2016.
 */
@Entity
@Data
@Table(name = "ActivityAssignment")
public class ActivityAssignment implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    private Integer id;

    @Column(name = "ACTIVITY")
    private Integer activity;

    @Column(name = "ASSIGNEDTO")
    private Integer assignedTo;

}
