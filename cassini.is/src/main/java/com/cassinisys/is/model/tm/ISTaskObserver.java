package com.cassinisys.is.model.tm;
/* Model for ISTaskObserver  */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "IS_TASKOBSERVER")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "TM")
public class ISTaskObserver implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @ApiObjectField(required = true)
    private ISTaskObserverId id;

    public ISTaskObserver() {
    }

    public ISTaskObserver(ISTaskObserverId id) {
        this.id = id;
    }

    public ISTaskObserver(Integer taskId, Integer personId) {
        this.id = new ISTaskObserverId(taskId, personId);
    }

    public ISTaskObserverId getId() {
        return id;
    }

    public void setId(ISTaskObserverId id) {
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
        ISTaskObserver other = (ISTaskObserver) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
