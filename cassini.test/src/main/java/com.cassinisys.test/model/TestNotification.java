package com.cassinisys.test.model;

import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by Suresh Cassini on 02-Jul-18.
 */
@Entity
@Table(name = "TEST_NOTIFICATION")
@ApiObject(group = "TEST")
@Data
public class TestNotification {
    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "IM_TESTNOTIFICATION_ID_GEN", sequenceName = "IM_TESTNOTIFICATION_ID_GEN_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IM_TESTNOTIFICATION_ID_GEN")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PERSON")
    private Integer person;
}
