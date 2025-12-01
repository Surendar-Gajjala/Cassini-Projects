package com.cassinisys.plm.model.pm;

import lombok.Data;

/**
 * Created by subramanyam on 23-12-2020.
 */
@Data
public class ProjectMemberDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String fullName;
    private Boolean hasImage = Boolean.FALSE;
}
