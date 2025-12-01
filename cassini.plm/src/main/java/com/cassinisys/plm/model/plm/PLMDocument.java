package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by reddy on 22/12/15.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_DOCUMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMDocument extends PLMFile {

    @Column(name = "REVISION")
    private String revision;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Transient
    private String filePath;
    @Transient
    private List<PLMDocument> children = new LinkedList<>();

    public PLMDocument() {
        super.setObjectType(PLMObjectType.DOCUMENT);
    }

}
