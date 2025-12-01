package com.cassinisys.plm.model.pgc;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GSR.
 */
@Entity
@Table(name = "PGC_DECLARATION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCDeclaration extends PGCObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE", nullable = false)
    private PGCDeclarationType type;

    @Column(name = "SUPPLIER", nullable = false)
    private Integer supplier;

    @Column(name = "CONTACT", nullable = false)
    private Integer contact;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DUE_DATE")
    private Date dueDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIVED_DATE")
    private Date receivedDate;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pgc.DeclarationStatus")})
    @Column(name = "STATUS", nullable = true)
    private DeclarationStatus status = DeclarationStatus.OPEN;

    @Transient
    private String supplierName;

    @Transient
    private String supplierContactName;
    @Transient
    private Integer supplierContact;
    @Transient
    private String createPerson;
    @Transient
    private String modifiedPerson;

    @Transient
    private List<PGCObjectAttribute> pgcObjectAttributes = new ArrayList<>();

    public PGCDeclaration() {
        super.setObjectType(PGCEnumObject.PGCDECLARATION);
    }

}

