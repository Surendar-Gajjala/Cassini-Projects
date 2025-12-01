package com.cassinisys.platform.model.col;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "MEETINGATTENDEE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingAttendee implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MeetingAttendeeId id;

    public MeetingAttendee() {
    }

    public MeetingAttendee(MeetingAttendeeId id) {
        this.id = id;
    }

    public MeetingAttendee(Integer meetingId, Integer personId) {
        this.id = new MeetingAttendeeId(meetingId, personId);
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
        MeetingAttendee other = (MeetingAttendee) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
