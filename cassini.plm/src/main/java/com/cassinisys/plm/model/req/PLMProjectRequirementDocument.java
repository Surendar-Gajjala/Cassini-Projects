package com.cassinisys.plm.model.req;

import com.cassinisys.plm.model.pm.PLMProject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 08-07-2021.
 */
@Entity
@Table(name = "PLM_PROJECT_REQUIREMENTDOCUMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMProjectRequirementDocument implements Serializable {

    @Id
    @SequenceGenerator(name = "PROJECT_REQ_DOC_ID_GEN", sequenceName = "PROJECT_REQ_DOC_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_REQ_DOC_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT")
    private PLMProject project;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "REQ_DOCUMENT")
    private PLMRequirementDocumentRevision reqDocument;

}
