package com.cassinisys.plm.model.pdm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "PDM_GITHUB_ITEMREVISION_RELEASE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMGitHubItemRevisionRelease implements Serializable {
    @Id
    @Column(name = "ITEM_REVISION")
    private Integer itemRevision;

    @Column(name = "RELEASE_ID")
    private Long releaseId;

    @Column(name = "RELEASE_NAME")
    private String releaseName;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "RELEASE_DATE")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;
}
