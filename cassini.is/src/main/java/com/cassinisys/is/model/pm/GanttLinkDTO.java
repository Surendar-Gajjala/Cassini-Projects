package com.cassinisys.is.model.pm;

import java.io.Serializable;

/**
 * Created by swapna on 09/05/19.
 */
public class GanttLinkDTO implements Serializable {

    private Integer cassiniId;

    private Long ganttId;

    public Integer getCassiniId() {
        return cassiniId;
    }

    public void setCassiniId(Integer cassiniId) {
        this.cassiniId = cassiniId;
    }

    public Long getGanttId() {
        return ganttId;
    }

    public void setGanttId(Long ganttId) {
        this.ganttId = ganttId;
    }
}
