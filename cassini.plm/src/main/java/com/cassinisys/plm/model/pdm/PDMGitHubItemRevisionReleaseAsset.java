package com.cassinisys.plm.model.pdm;

import com.cassinisys.platform.model.col.Attachment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PDM_GITHUB_ITEMREVISION_RELEASE_ASSET")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMGitHubItemRevisionReleaseAsset extends Attachment {
    @Column(name = "RELEASE")
    private Integer release;
}
