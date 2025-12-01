package com.cassinisys.platform.model.activitystream;

import com.cassinisys.platform.model.core.Session;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 29-07-2020.
 */
@Data
public class SessionsDto {
    private Session session;

    private List<ActivityStream> activityStreams = new LinkedList<>();

}
