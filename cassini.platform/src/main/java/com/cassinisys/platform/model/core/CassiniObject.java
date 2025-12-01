package com.cassinisys.platform.model.core;

import com.cassinisys.platform.config.ObjectEntityListener;
import com.cassinisys.platform.model.common.Tag;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author reddy
 */
@Entity
@Data
@Table(name = "CASSINI_OBJECT")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(ObjectEntityListener.class)
public abstract class CassiniObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "OBJECT_ID_GEN", sequenceName = "OBJECT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBJECT_ID_GEN")
    @Column(name = "OBJECT_ID", nullable = false)
    private Integer id;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate = new Date();

    @Column(name = "CREATED_BY", nullable = false)
    private Integer createdBy = 1;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE", nullable = false)
    private Date modifiedDate = new Date();

    @Column(name = "MODIFIED_BY")
    private Integer modifiedBy = 1;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    @Column(name = "OBJECT_TYPE", nullable = false)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    private Enum objectType;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "OBJECT")
    @Fetch(FetchMode.SELECT)
    private List<Tag> tags = new ArrayList<>();

    @Transient
    private Map<String, Object> attributesMap = new HashMap<>();

    @SuppressWarnings("unused")
    public CassiniObject() {
        this.objectType = ObjectType.UNKNOWN;
    }

    protected CassiniObject(Enum objectType) {
        this.objectType = objectType;
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
        CassiniObject other = (CassiniObject) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}