package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by subramanyam on 10-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_PARAM_ACTUAL_VALUE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMParamActualValue implements Serializable {

    @Id
    @SequenceGenerator(name = "PQM_OBJECT_ID_GEN", sequenceName = "PQM_OBJECT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PQM_OBJECT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CHECKLIST")
    private Integer checklist;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARAM")
    private PQMInspectionPlanChecklistParameter param;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.ChecklistResult")})
    @Column(name = "RESULT", nullable = true)
    private ChecklistResult result = ChecklistResult.NONE;

    @Column(name = "TEXT_VALUE")
    private String textValue;

    @Column(name = "INTEGER_VALUE")
    private Integer integerValue;

    @Column(name = "DOUBLE_VALUE")
    private Double doubleValue;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_VALUE")
    private Date dateValue;

    @Column(name = "TIME_VALUE")
    private Time timeValue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP_VALUE")
    private Date timestampValue;

    @Column(name = "REF_VALUE")
    private Integer refValue;

    @Column(name = "BOOLEAN_VALUE")
    private Boolean booleanValue;

    @Column(name = "LIST_VALUE")
    private String listValue;

    @Column(name = "MLIST_VALUE")
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] mListValue;

    @Column(name = "IMAGE_VALUE")
    private byte[] imageValue;

    @Column(name = "CURRENCY_VALUE")
    private Double currencyValue;


}
