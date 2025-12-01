package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PDM_FILE_VERSION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMFileVersion extends PDMVaultObject {
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "FILE")
    private PDMFile file;

    @Column(name = "COMMIT")
    private Integer commit;

    @Column(name = "VERSION")
    private Integer version = 1;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.TRUE;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "TIMESTAMP")
    private Long timestamp;

    @Column(name = "ATTACHED_TO")
    private Integer attachedTo;

    @Column(name = "VISUALIZATION_ID")
    private String visualizationId;

    @Column(name = "PART_REFERENCES")
    private String partReferences;

    @Transient
    private String attachedToRevision;

    public PDMFileVersion() {
        super(PDMObjectType.PDM_FILEVERSION);
    }
}
