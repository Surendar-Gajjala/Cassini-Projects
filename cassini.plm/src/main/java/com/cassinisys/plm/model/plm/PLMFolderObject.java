package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Entity
@Data
@Table(name = "PLM_FOLDEROBJECT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMFolderObject {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    private Integer id;

    @Column(name = "FOLDER")
    private Integer folder;

    @Column(name = "OBJECT_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    private ObjectType objectType;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_ADDED")
    private Date dateadded = new Date();

}
