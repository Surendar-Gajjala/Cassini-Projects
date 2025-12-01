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
@Table(name = "TEST_RUNSUITE")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode
public class RunSuite extends CassiniObject {
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @JsonIgnore
    @ApiObjectField(required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLAN")
    private RunPlan runPlan;
    @Transient
    private List<RunCase> children = new ArrayList<>();

    public RunSuite() {
        super(TestObjectType.RUNSUITE);
    }

}
