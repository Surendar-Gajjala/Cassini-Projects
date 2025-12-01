package com.cassinisys.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyam on 31-07-2018.
 */
@Entity
@Table(name = "TEST_RCPROGRAMEXECUTION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "TEST")
@Data
@EqualsAndHashCode
public class RCProgramExecution extends RCExecution {
    @ApiObjectField(required = true)
    @Column(name = "PROGRAM")
    private String program;
    @ApiObjectField
    @Column(name = "PARAMS")
    private String params;
    @ApiObjectField
    @Column(name = "WORKING_DIR", nullable = false)
    private String workingDir;
}
