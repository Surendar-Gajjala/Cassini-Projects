package com.cassinisys.platform.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;

public interface ActivityStreamConverter {
    String getConverterKey();
    String convertToString(ActivityStream as);
}
