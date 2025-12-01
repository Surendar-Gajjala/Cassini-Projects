package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_CHANGE_REQUEST_AFFECTEDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMChangeRequestAffectedItem extends CassiniObject {

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IS_IMPLEMENTED")
    private Boolean isImplemented = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IMPLEMENTED_DATE")
    private Date implementedDate;

    @Transient
    private Integer ecr;

    @Transient
    private Integer dcr;


}
