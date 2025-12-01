package com.cassinisys.platform.service.activitystream;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;


@Data
public class BaseActivityStream {

    @Autowired
    protected ActivityStreamService activityStreamService;

    @Autowired
    protected ActivityStreamResourceBundle activityStreamResourceBundle;

    protected String highlightValue(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("<span style='color:#2a6fa8'>");
        if (value == null || value.equals("")) {
            sb.append("- empty -");
        } else {
            sb.append(value);
        }
        sb.append("</span>");
        return sb.toString();
    }

    protected String addMarginToMessage(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='margin-left: 20px;'>");
        sb.append(message);
        sb.append("</div>");
        return sb.toString();
    }
}
