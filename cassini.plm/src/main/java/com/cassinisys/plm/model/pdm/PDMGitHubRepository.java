package com.cassinisys.plm.model.pdm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PDM_GITHUB_REPOSITORY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMGitHubRepository extends PDMObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT")
    private PDMGitHubAccount account;

    @Transient
    private Boolean selected = Boolean.FALSE;
    @Transient
    private Integer releases = 0;
    @Transient
    private Integer forks = 0;
    @Transient
    private Integer openIssues = 0;
    @Transient
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date updatedAt;


    public PDMGitHubRepository() {
        super(PDMObjectType.PDM_GITHUBREPOSITORY);
    }
}
