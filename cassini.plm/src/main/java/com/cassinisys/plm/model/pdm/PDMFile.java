package com.cassinisys.plm.model.pdm;

import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMFile extends PDMVaultObject {
    @Column(name = "FOLDER")
    private Integer folder;

    @Column(name = "LATEST_VERSION")
    private Integer latestVersion = 1;

    @Column(name = "VERSIONS")
    private Integer versions = 1;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pdm.PDMFileType")})
    @Column(name = "FILE_TYPE", nullable = false)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    private PDMFileType fileType;

    @Column(name = "AUTHORING_APP")
    private String authoringApp;

    @Column(name = "AUTHORING_APP_VERSION")
    private String authoringAppVersion;


    public PDMFile() {
        super(PDMObjectType.PDM_FILE);
    }
}
