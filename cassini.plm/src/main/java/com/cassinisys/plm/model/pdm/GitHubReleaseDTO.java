package com.cassinisys.plm.model.pdm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GitHubReleaseDTO {
    private String name;
    private String comments;
    private Date releaseDate;
}
