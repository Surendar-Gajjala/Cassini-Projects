package com.cassinisys.platform.model.core;

import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "OBJECTTYPEATTRIBUTE")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectTypeAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ATTRIBUTE_ID_GEN",
            sequenceName = "ATTRIBUTE_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ATTRIBUTE_ID_GEN")
    @Column(name = "ATTRIBUTE_ID", nullable = false)
    private Integer id;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.DataType")})
    @Column(name = "DATA_TYPE", nullable = false)
    private DataType dataType;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REQUIRED")
    private boolean required;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType")})
    @Column(name = "OBJECT_TYPE", nullable = false)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    private Enum objectType;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType")})
    @Column(name = "REF_TYPE", nullable = false)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    private Enum refType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "LOV", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lov lov;

    @Column(name = "LIST_MULTIPLE")
    private boolean listMultiple = Boolean.FALSE;

    @Column(name = "DEFAULT_TEXTVALUE")
    private String defaultTextValue;

    @Column(name = "DEFAULT_LISTVALUE")
    private String defaultListValue;

    @Column(name = "VISIBLE")
    private boolean visible = Boolean.TRUE;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "MEASUREMENT")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Measurement measurement;

    @Column(name = "FORMULA")
    private String formula;

    @Column(name = "VALIDATIONS")
    private String validations;

    @Column(name = "ATTRIBUTE_GROUP")
    private String attributeGroup;

    @Column(name = "REF_SUB_TYPE")
    private Integer refSubType;

    @Column(name = "PLUGIN")
    private boolean plugin = Boolean.FALSE;

    @Transient
    private String refSubTypeName;

    @Transient
    private MeasurementUnit measurementUnit;

    public ObjectTypeAttribute() {
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObjectTypeAttribute other = (ObjectTypeAttribute) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
