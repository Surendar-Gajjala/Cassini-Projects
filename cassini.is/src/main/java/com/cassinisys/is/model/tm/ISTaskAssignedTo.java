package com.cassinisys.is.model.tm;
/* Model for  ISTaskAssignedTo */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "IS_TASKASSIGNEDTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "TM")
public class ISTaskAssignedTo implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ISTaskAssignedToId id;

    public ISTaskAssignedTo() {
    }

    public ISTaskAssignedTo(ISTaskAssignedToId id) {
        this.id = id;
    }

    public ISTaskAssignedTo(Integer taskId, Integer personId) {
        this.id = new ISTaskAssignedToId(taskId, personId);
    }

    public ISTaskAssignedToId getId() {
        return id;
    }

    public void setId(ISTaskAssignedToId id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        ISTaskAssignedTo other = (ISTaskAssignedTo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
