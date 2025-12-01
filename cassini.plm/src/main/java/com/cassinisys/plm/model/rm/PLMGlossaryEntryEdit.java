package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam reddy on 01-10-2018.
 */
@Entity
@Data
@Table(name = "PLM_GLOSSARYENTRYEDIT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMGlossaryEntryEdit implements Serializable {

    @Id
    @SequenceGenerator(name = "GLOSSARY_ENTRY_EDIT_ID_GEN", sequenceName = "GLOSSARY_ENTRY_EDIT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLOSSARY_ENTRY_EDIT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ENTRY")
    private Integer entry;

    @Column(name = "EDITED_DESCRIPTION")
    private String editedDescription;

    @Column(name = "EDITED_NOTES")
    private String editedNotes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EDITED_BY")
    private Person person;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.rm.GlossaryEntryEditStatus")})
    @Column(name = "STATUS", nullable = true)
    private GlossaryEntryEditStatus status = GlossaryEntryEditStatus.NONE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LANGUAGE")
    private PLMGlossaryLanguages language;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCEPTED_DATE")
    private Date acceptedDate;

    @Column(name = "EDIT_VERSION")
    private Integer editVersion;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.FALSE;


}
