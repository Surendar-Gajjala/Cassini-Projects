package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_QUALITY_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMQualityType extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT_TYPE")
    private Integer parentType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NUMBER_SOURCE", nullable = true)
    private AutoNumber numberSource;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.QualityType")})
    @Column(name = "QUALITY_TYPE", nullable = true)
    private QualityType QualityType;

    @Transient
    private List<PQMQualityType> children = new ArrayList<>();
    @Transient
    private PQMQualityType parentTypeReference;
    @Transient
    private List<PQMQualityTypeAttribute> attributes = new ArrayList<>();
    @Transient
    private Boolean usedType = Boolean.FALSE;

    public PQMQualityType() {
        super.setObjectType(PLMObjectType.QUALITY_TYPE);
    }


}
