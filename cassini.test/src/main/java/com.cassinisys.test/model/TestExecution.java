package com.cassinisys.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 31-07-2018.
 */
@Entity
@Table(name = "TEST_EXECUTION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@ApiObject(group = "TEST")
@Data
public class TestExecution implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "TEST_EXECUTION_ID_GEN", sequenceName = "TEST_EXECUTION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_EXECUTION_ID_GEN")
    @Column(name = "ID")
    private Integer id;
    @ApiObjectField(required = true)
    @Column(name = "TYPE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.test.model.ExecutionType")})
    private ExecutionType type;
}
