package com.cassinisys.pdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyamreddy on 20-Feb-17.
 */
@Entity
@Table(name = "PDM_FILEREFERENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMFileReference implements Serializable{

    @Id
    @SequenceGenerator(
            name = "FILEREFERENCE_ID_GEN",
            sequenceName = "FILEREFERENCE_ID_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "FILEREFERENCE_ID_GEN"
    )
    @Column(name = "REF_ID")
    private Integer refId;

    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "REFERENCE")
    private Integer reference;

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }
}
