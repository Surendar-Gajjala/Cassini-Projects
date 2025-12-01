package com.cassinisys.is.model.tm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Varsha Malgireddy on 8/3/2018.
 */

@Entity
@Table(name = "IS_TASKCOMPLETIONHISTORY")
@ApiObject(name = "TM")
public class ISTaskCompletionHistory implements Serializable {

    @Id
    @SequenceGenerator(name = "TASKCOMPLETIONHISTORY_ID_GEN", sequenceName = "TASKCOMPLETIONHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASKCOMPLETIONHISTORY_ID_GEN")
    @Column(name = "ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer Id;

    @Column
    private Integer task;

    @Column
    @JsonSerialize(
            using = CustomDateSerializer.class
    )
    @JsonDeserialize(
            using = CustomDateDeserializer.class
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    @Column
    private Double completion = 0.0;

    @Column(name = "UNITS_COMPLETED")
    private Double unitsCompleted = 0.0;

    @Column(name = "COMPLETED_BY")
    private Integer completedBy;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private Person completedPerson;

    @Transient
    private Boolean resources = Boolean.FALSE;

    public ISTaskCompletionHistory() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Double getCompletion() {
        return completion;
    }

    public void setCompletion(Double completion) {
        this.completion = completion;
    }

    public Double getUnitsCompleted() {
        return unitsCompleted;
    }

    public void setUnitsCompleted(Double unitsCompleted) {
        this.unitsCompleted = unitsCompleted;
    }

    public Integer getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(Integer completedBy) {
        this.completedBy = completedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Person getCompletedPerson() {
        return completedPerson;
    }

    public void setCompletedPerson(Person completedPerson) {
        this.completedPerson = completedPerson;
    }

    public Boolean getResources() {
        return resources;
    }

    public void setResources(Boolean resources) {
        this.resources = resources;
    }
}
