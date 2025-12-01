package com.cassinisys.plm.model.pdm;

import com.cassinisys.plm.model.plm.PLMItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_MASTER_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMRevisionMaster extends PDMObject {
    @Column(name = "LATEST_REVISION")
    private String latestRevision;

    @Column(name = "LATEST_VERSION")
    private Integer latestVersion = 1;

    @Column(name = "ITEM_OBJECT") //References PLM_ITEM (ITEM_ID)
    private Integer itemObject;

    @Transient
    private PLMItem plmItem;

    public PDMRevisionMaster() {
        super(PDMObjectType.PDM_REVISIONMASTER);
    }
}
