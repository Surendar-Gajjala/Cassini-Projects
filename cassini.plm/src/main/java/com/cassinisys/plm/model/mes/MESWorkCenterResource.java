package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 26-10-2020.
 */
@Entity
@Table(name = "MES_WORKCENTER_RESOURCE")
@Data
public class MESWorkCenterResource implements Serializable {

    @Id
    @SequenceGenerator(name = "WORKCENTERRESOURCE_ID_GEN", sequenceName = "WORKCENTERRESOURCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKCENTERRESOURCE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORK_CENTER")
    private Integer workCenter;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "MES_OBJECT_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    private ObjectType objectType;

    @Column(name = "SHIFT")
    private Integer shift;

}
