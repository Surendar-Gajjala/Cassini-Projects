package com.cassinisys.test.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by GSR on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNPROGRAMEXECUTION")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode
public class RunProgramExecution extends RunExecution {
    @ApiObjectField(required = true)
    @Column(name = "PROGRAM")
    private String program;
    @ApiObjectField(required = true)
    @Column(name = "PARAMS")
    private String params;
    @ApiObjectField(required = true)
    @Column(name = "WORKING_DIR")
    private String workingDir;
}
