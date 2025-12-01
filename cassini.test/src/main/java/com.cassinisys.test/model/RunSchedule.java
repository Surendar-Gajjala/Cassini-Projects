package com.cassinisys.test.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

/**
 * Created by GSR on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNSCHEDULE")
@Data
public class RunSchedule implements Serializable {

    @Id
    @SequenceGenerator(name = "RUNSCHEDULE_ID_GEN", sequenceName = "RUNSCHEDULE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RUNSCHEDULE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RUNCONFIG")
    private TestRunConfiguration runConfig;
    @Column(name = "SUNDAY")
    private Time sunday;
    @Column(name = "MONDAY")
    private Time monday;
    @Column(name = "TUESDAY")
    private Time tuesday;
    @Column(name = "WEDNESDAY")
    private Time wednesday;
    @Column(name = "THURSDAY")
    private Time thursday;
    @Column(name = "FRIDAY")
    private Time friday;
    @Column(name = "SATURDAY")
    private Time saturday;
}
