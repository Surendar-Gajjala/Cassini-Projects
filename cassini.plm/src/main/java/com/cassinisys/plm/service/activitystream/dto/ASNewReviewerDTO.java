package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by GSR on 22-07-2021.
 */
@Data
@AllArgsConstructor
public class ASNewReviewerDTO {
    private Integer id;
    private String fullName;
    private String type;
    private String name;
}
