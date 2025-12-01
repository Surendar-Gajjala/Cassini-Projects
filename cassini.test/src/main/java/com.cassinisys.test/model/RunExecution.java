package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by GSR on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNEXECUTION")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode
public class RunExecution extends CassiniObject {
    @ApiObjectField(required = true)
    @Column(name = "TYPE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.test.model.ExecutionType")})
    private ExecutionType executionType;

    public RunExecution() {
        super(TestObjectType.RUNEXECUTION);
    }

}
