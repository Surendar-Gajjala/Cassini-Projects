package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 24-07-2020.
 */
@Data
@AllArgsConstructor
public class ASAssignedToChecklist {
    private String fullName;
    private String checklistName;
}
