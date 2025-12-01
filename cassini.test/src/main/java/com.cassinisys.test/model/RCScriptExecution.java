package com.cassinisys.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyam on 31-07-2018.
 */
@Entity
@Table(name = "TEST_RCSCRIPTEXECUTION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "TEST")
@Data
@EqualsAndHashCode
public class RCScriptExecution extends RCExecution {
    @ApiObjectField(required = true)
    @Column(name = "LANGUAGE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.test.model.ScriptLanguage")})
    private ScriptLanguage scriptLanguage;
    @ApiObjectField(required = true)
    @Column(name = "SCRIPT", nullable = false)
    private String script;
}
