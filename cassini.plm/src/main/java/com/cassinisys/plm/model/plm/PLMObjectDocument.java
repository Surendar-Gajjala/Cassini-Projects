package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by lakshmi on 16-01-2017.
 */
@Entity
@Data
@Table(name = "PLM_OBJECT_DOCUMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class PLMObjectDocument extends CassiniObject {

    @Column(name = "OBJECT", nullable = false)
    private Integer object;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DOCUMENT", nullable = false)
    private PLMDocument document;

    @Column(name = "FOLDER")
    private Integer folder;

    @Column(name = "DOCUMENT_TYPE")
    private String documentType;

    public PLMObjectDocument() {
        super(PLMObjectType.OBJECTDOCUMENT);
    }
}
