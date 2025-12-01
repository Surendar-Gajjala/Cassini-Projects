package com.cassinisys.platform.model.core;

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
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "OBJECTATTRIBUTE")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ObjectAttributeId id;

    @Column(name = "TEXT_VALUE")
    private String stringValue;

    @Column(name = "INTEGER_VALUE")
    private Integer integerValue;

    @Column(name = "DOUBLE_VALUE")
    private Double doubleValue;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_VALUE")
    private Date dateValue;

    @Column(name = "BOOLEAN_VALUE")
    private Boolean booleanValue = Boolean.FALSE;

    @Column(name = "LIST_VALUE")
    private String listValue;

    @Column(name = "MLIST_VALUE")
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] mListValue = new String[0];

    @Column(name = "TIME_VALUE")
    private Time timeValue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP_VALUE")
    private Date timestampValue;

    @Column(name = "REF_VALUE")
    private Integer refValue;

    @Column(name = "CURRENCY_VALUE")
    private Double currencyValue;

    @Column(name = "CURRENCY_TYPE")
    private Integer currencyType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEASUREMENT_UNIT")
    private MeasurementUnit measurementUnit;

    @Column(name = "IMAGE_VALUE")
    private byte[] imageValue;

    @Column(name = "FORMULA_VALUE")
    private String formulaValue;

    @Column(name = "ATTACHMENT_VALUE")
    @Type(
            type = "com.cassinisys.platform.util.converter.IntArrayUserType"
    )
    private Integer[] attachmentValues = new Integer[0];

    @Column(name = "LONGTEXT_VALUE")
    private String longTextValue;

    @Column(name = "RICHTEXT_VALUE")
    private String richTextValue;

    @Column(name = "HYPERLINK_VALUE")
    private String hyperLinkValue;

    @Transient
    private ObjectTypeAttribute objectTypeAttribute;

    public ObjectAttribute() {
    }


    public String getValueAsString() {
        String value = "";
        if (stringValue != null) {
            value = stringValue;
        } else if (integerValue != null) {
            value = integerValue.toString();
        } else if (doubleValue != null) {
            value = doubleValue.toString();
        } else if (currencyValue != null) {
            value = currencyValue.toString();
        } else if (dateValue != null) {
            value = dateValue.toString();
        } else if (timestampValue != null) {
            value = timestampValue.toString();
        } else if (booleanValue != null) {
            value = booleanValue.toString();
        } else if (longTextValue != null) {
            value = longTextValue.toString();
        } else if (richTextValue != null) {
            value = richTextValue.toString();
        } else if (refValue != null) {
            value = refValue.toString();
        } else if (formulaValue != null) {
            value = formulaValue.toString();
        }

        MeasurementUnit mu = getMeasurementUnit();
        if (mu != null) {
            value += " " + mu.getSymbol();
        }
        return value;
    }
}
