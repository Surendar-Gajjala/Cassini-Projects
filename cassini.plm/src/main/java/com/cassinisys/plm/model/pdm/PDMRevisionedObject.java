package com.cassinisys.plm.model.pdm;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_REVISIONED_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMRevisionedObject extends PDMVaultObject {
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "MASTER")
    private PDMRevisionMaster master;

    @Column(name = "REVISION")
    private String revision;

    @Column(name = "LATEST_REVISION")
    private Boolean latestRevision = Boolean.TRUE;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "LATEST_VERSION")
    private Boolean latestVersion = Boolean.TRUE;

    @Column(name = "ITEM_OBJECT")  //References PLM_ITEMREVISION (ITEM_ID)
    private Integer itemObject;

    @Transient
    private PDMFileVersion fileVersion;

    @Transient
    private PLMItemRevision plmItemRevision;

    public PDMRevisionedObject(Enum type) {
        super(type);
    }
}
