package com.cassinisys.platform.model.col;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data
@EqualsAndHashCode(callSuper=false)
public class MeetingAttendeeId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "MEETING", nullable = false)
    private Integer meetingId;

    @Column(name = "PERSON", nullable = false)
    private Integer personId;

    public MeetingAttendeeId() {

    }

    public MeetingAttendeeId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public MeetingAttendeeId(Integer meetingId, Integer personId) {
        this.meetingId = meetingId;
        this.personId = personId;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((personId == null) ? 0 : personId.hashCode());
        result = prime * result + ((meetingId == null) ? 0 : meetingId.hashCode());
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
        MeetingAttendeeId other = (MeetingAttendeeId) obj;
        if (personId == null) {
            if (other.personId != null)
                return false;
        } else if (!personId.equals(other.personId))
            return false;
        if (meetingId == null) {
            if (other.meetingId != null)
                return false;
        } else if (!meetingId.equals(other.meetingId))
            return false;
        return true;
    }
}
