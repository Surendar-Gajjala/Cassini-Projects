package com.cassinisys.pdm.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_LOCKABLE")
@PrimaryKeyJoinColumn(name = "LOCKABLE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public abstract class PDMLockable extends CassiniObject{

    @ApiObjectField(required = true)
    @Column(name = "LOCKED")
    private boolean locked = Boolean.FALSE;

    @ApiObjectField(required = true)
    @Column(name = "LOCKED_BY")
    private Integer lockedBy;

    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "LOCKED_DATE")
    private Date lockedDate = new Date();

    public PDMLockable(Enum objectType){super(objectType);}

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Integer getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Integer lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Date getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }
}
