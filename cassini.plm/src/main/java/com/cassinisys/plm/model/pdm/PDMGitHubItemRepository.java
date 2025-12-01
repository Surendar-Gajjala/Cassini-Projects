package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "PDM_GITHUB_ITEM_REPOSITORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMGitHubItemRepository implements Serializable {
    @Id
    @Column(name = "ITEM")
    private Integer item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REPOSITORY")
    private PDMGitHubRepository repository;
}
