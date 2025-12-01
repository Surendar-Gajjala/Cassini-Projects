package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam on 06-11-2021.
 */
@Entity
@Table(name = "PLM_DOCUMENT_REVIEWER")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMDocumentReviewer implements Serializable {
    @Id
    @SequenceGenerator(name = "DOCUMENT_REVIEWER_ID_GEN", sequenceName = "DOCUMENT_REVIEWER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_REVIEWER_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DOCUMENT")
    private Integer document;

    @Column(name = "REVIEWER")
    private Integer reviewer;

    @Column(name = "APPROVER")
    private Boolean approver = Boolean.FALSE;

    @Column(name = "VOTE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.plm.DocumentApprovalStatus")})
    private DocumentApprovalStatus status = DocumentApprovalStatus.NONE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VOTE_TIMESTAMP")
    private Date voteTimestamp = new Date();

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private String reviewerName;
}
