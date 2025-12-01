package com.cassinisys.test.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNOUTPUTLOG")
@Data
public class RunOutputLog implements Serializable {

    @Id
    @SequenceGenerator(name = "RUNOUTPUTLOG_ID_GEN", sequenceName = "RUNOUTPUTLOG_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RUNOUTPUTLOG_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer rowId;

    @Column(name = "RUN")
    private Integer testRun;

    @Column(name = "LOG")
    private String log;
}
