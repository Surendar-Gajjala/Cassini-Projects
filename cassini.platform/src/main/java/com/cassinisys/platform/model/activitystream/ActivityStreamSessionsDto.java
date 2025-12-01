package com.cassinisys.platform.model.activitystream;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 29-07-2020.
 */
@Data
public class ActivityStreamSessionsDto {
    private List<SessionsDto> sessionDtoList = new LinkedList<>();

    private Boolean first = Boolean.FALSE;

    private Boolean last = Boolean.FALSE;
}
