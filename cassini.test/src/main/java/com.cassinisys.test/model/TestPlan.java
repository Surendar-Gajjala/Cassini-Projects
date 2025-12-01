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
@Table(name = "TEST_PLAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "TEST")
@Data
@EqualsAndHashCode
public class TestPlan extends CassiniObject {
    @ApiObjectField(required = true)
    @Column(name = "SCENARIO")
    private Integer scenario;
    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;

    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private List<TestSuite> children = new ArrayList<>();

    public TestPlan() {
        super(TestObjectType.TESTPLAN);
    }


}
