package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PDM_GITHUB_ACCOUNT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMGitHubAccount extends PDMObject {

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pdm.GitHubAuthType")})
    @Column(name = "AUTH_TYPE")
    private GitHubAuthType authType = GitHubAuthType.TOKEN;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "OAUTH_TOKEN")
    private String oauthToken;

    @Column(name = "ORGANIZATION")
    private String organization;

    public PDMGitHubAccount() {
        super(PDMObjectType.PDM_GITHUBACCOUNT);
    }
}
