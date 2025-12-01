package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "CUSTOM_OBJECT_LIFECYCLE_HISTORY")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectLifecycleHistory implements Serializable {

    @Id
    @SequenceGenerator(name = "CUSTOM_ID_GEN", sequenceName = "CUSTOM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOM_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "OBJECT")
    private Integer customObject;

    @Column(name = "OLD_STATUS")
    private String oldStatus;

    @Column(name = "NEW_STATUS")
    private String newStatus;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP", nullable = false)
    private Date timestamp = null;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;
}
