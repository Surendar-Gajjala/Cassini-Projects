package com.cassinisys.is.model.tm;
/* Model for ISTaskObserverId  */

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ISTaskObserverId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "TASK", nullable = false)
    private Integer taskId;

    @Column(name = "PERSON", nullable = false)
    private Integer personId;

    public ISTaskObserverId() {
    }

    public ISTaskObserverId(Integer taskId, Integer personId) {
        this.taskId = taskId;
        this.personId = personId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((personId == null) ? 0 : personId.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISTaskObserverId other = (ISTaskObserverId) obj;
        if (personId == null) {
            if (other.personId != null)
                return false;
        } else if (!personId.equals(other.personId))
            return false;
        if (taskId == null) {
            if (other.taskId != null)
                return false;
        } else if (!taskId.equals(other.taskId))
            return false;
        return true;
    }

}
