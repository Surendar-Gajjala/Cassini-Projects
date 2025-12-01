package com.cassinisys.test.model;

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
@Table(name = "TEST_RUNSCRIPTEXECUTION")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode
public class RunScripTextExecution extends RunExecution {
    @ApiObjectField(required = true)
    @Column(name = "LANGUAGE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.test.model.ScriptLanguage")})
    private ScriptLanguage scriptLanguage;
    @ApiObjectField(required = true)
    @Column(name = "SCRIPT")
    private String script;
}
