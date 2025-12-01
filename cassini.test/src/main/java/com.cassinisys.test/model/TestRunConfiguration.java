package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 03-07-2018.
 */

@Entity
@Table(name = "TEST_RUNCONFIGURATION")
@PrimaryKeyJoinColumn(name = "ID")
@ApiObject(group = "TEST")
@Data
@EqualsAndHashCode
public class TestRunConfiguration extends CassiniObject {
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @ApiObjectField(required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCENARIO")
    private TestScenario scenario;

    @Transient
    private List<TestScenario> children = new ArrayList<>();

    public TestRunConfiguration() {
        super(TestObjectType.TESTRUNCONFIGURATION);
    }

}
