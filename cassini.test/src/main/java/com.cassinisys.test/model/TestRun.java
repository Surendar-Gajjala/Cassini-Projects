package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Entity
@Table(name = "TEST_RUN")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode
public class TestRun extends CassiniObject {
    @ApiObjectField(required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RUN_CONFIG")
    private TestRunConfiguration testRunConfiguration;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISH_TIME", nullable = false)
    @ApiObjectField(required = true)
    private Date finishTime = new Date();
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME", nullable = false)
    @ApiObjectField(required = true)
    private Date startTime = new Date();
    @ApiObjectField
    @Column(name = "STATUS")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.test.model.RunStatus")})
    private RunStatus status;
    @Transient
    private RunScenario runScenario;
    @Column(name = "TOTAL")
    private Integer total;
    @Column(name = "PASSED")
    private Integer passed;
    @Column(name = "FAILED")
    private Integer failed;
    @Transient
    private RunOutputLog runOutputLog;
    @Transient
    private String scenarioName;

    public TestRun() {
        super(TestObjectType.TESTRUN);
    }

}
