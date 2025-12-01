package com.cassinisys.is.model.col;
/* Model For ISProjectMessage */

import com.cassinisys.platform.model.col.Message;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_PROJECTMESSAGE")
@PrimaryKeyJoinColumn(name = "MESSAGE_ID")
@ApiObject(name = "COL")
public class ISProjectMessage extends Message {

    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    public ISProjectMessage() {
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

}
