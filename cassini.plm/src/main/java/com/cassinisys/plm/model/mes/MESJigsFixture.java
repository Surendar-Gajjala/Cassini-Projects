package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import java.util.LinkedList;
import java.util.List;
import com.cassinisys.plm.model.plm.dto.FileDto;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */

@Entity
@Table(name = "MES_JIGS_FIXTURE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESJigsFixture extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESJigsFixtureType type;

    @Column(name = "REQUIRES_MAINTENANCE", nullable = false)
    private Boolean requiresMaintenance = Boolean.TRUE;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mes.JigsFixtureType")})
    @Column(name = "DEVICE_TYPE", nullable = true)
    private JigsFixtureType jigType = JigsFixtureType.JIG;

    @Column(name = "IMAGE")
    private byte[] image;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;
    @Transient
    private String modifiedPerson;

    public MESJigsFixture() {
        super.setObjectType(MESEnumObject.JIGFIXTURE);
    }

}
