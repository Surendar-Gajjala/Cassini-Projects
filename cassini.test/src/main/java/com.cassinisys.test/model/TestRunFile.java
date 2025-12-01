package com.cassinisys.test.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by CassiniSystems on 10-09-2018.
 */
@Entity
@Table(name = "TEST_RUNFILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@Data
@EqualsAndHashCode
public class TestRunFile extends TestFile {
    @Column(name = "TEST_RUN")
    private Integer testRun;

}
