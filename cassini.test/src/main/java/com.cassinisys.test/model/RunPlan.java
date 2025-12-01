package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNPLAN")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode
public class RunPlan extends CassiniObject {
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @JsonIgnore
    @ApiObjectField(required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCENARIO")
    private RunScenario runScenario;
    @Transient
    private List<RunSuite> children = new ArrayList<>();

    public RunPlan() {
        super(TestObjectType.RUNPLAN);
    }
}
