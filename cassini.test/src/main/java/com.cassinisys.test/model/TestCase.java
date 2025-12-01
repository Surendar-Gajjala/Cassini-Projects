package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Entity
@Table(name = "TEST_CASE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "TEST")
@Data
@EqualsAndHashCode
public class TestCase extends CassiniObject {
    @ApiObjectField(required = true)
    @Column(name = "SUITE")
    private Integer suite;
    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;
    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;
    @ApiObjectField(required = true)
    @Column(name = "EXECUTION")
    private Integer execution;
    @Transient
    private TestProgramExecution programExecution;
    @Transient
    private TestScriptExecution scriptExecution;
    @Transient
    private String executionType;
    @Transient
    private Integer runConfiguration;
    @Transient
    private List<TestInputParam> testInputParams = new ArrayList<>();
    @Transient
    private List<TestOutputParam> testOutputParams = new ArrayList<>();

    public TestCase() {
        super(TestObjectType.TESTCASE);
    }
}
