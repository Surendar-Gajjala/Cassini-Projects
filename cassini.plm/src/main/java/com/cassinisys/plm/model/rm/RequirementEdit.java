package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "RM_REQUIREMENTEDIT")
public class RequirementEdit implements Serializable {

    @Id
    @SequenceGenerator(name = "REQUIREMENTEDIT_ID_GEN", sequenceName = "REQUIREMENTEDIT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUIREMENTEDIT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REQUIREMENT")
    private Integer requirement;

    @Column(name = "EDITED_NAME")
    private String editedName;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "EDITED_DESCRIPTION")
    private String editedDescription;

    @Column(name = "EDIT_NOTES")
    private String editNotes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EDITED_BY")
    private Person person;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.rm.RequirementEditStatus")})
    @Column(name = "STATUS", nullable = true)
    private RequirementEditStatus status = RequirementEditStatus.NEW;


    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EDITED_DATE")
    private Date editedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCEPTED_DATE")
    private Date acceptedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REJECTED_DATE")
    private Date rejectedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINAL_DATE")
    private Date finalDate;

    @Transient
    private Requirement requirements;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.FALSE;


}