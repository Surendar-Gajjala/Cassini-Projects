package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Entity
@Table(name = "TEST_RCSUITE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "TEST")
@Data
@EqualsAndHashCode
public class RCSuite extends CassiniObject {
    @ApiObjectField(required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLAN")
    private RCPlan plan;
    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;
    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;

    protected RCSuite() {
        super(TestObjectType.RCSUITE);
    }
}
