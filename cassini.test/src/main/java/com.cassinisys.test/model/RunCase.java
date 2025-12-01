package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by GSR on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNCASE")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode
public class RunCase extends CassiniObject {
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @JsonIgnore
    @ApiObjectField(required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SUITE")
    private RunSuite suite;
    @ApiObjectField(required = true)
    @Column(name = "EXECUTION")
    private Integer execution;
    @Column(name = "RESULT")
    private boolean result;
    @ApiObjectField(required = true)
    @Column(name = "TESTCASEID")
    private Integer testCaseId;

    public RunCase() {
        super(TestObjectType.RUNCASE);
    }
}
