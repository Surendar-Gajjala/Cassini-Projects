package com.cassinisys.plm.model.pm;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by swapna on 12/31/17.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_DELIVERABLE")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class PLMDeliverable implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column
    private Integer id;

    @Column(name = "ITEM")
    private Integer itemRevision;

    @Column(name = "DELIVARY_STATUS")
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pm.DeliverableStatus")})
    private DeliverableStatus deliverableStatus;

    @Column(name = "CRITERIA")
    private String criteria;

    @Column(name = "SCRIPT")
    private String script;

    @Transient
    private PLMItemRevision revision;

    @Transient
    private PLMItem item;

    @Transient
    private String objectType;

    @Transient
    private Integer objectId;

    @Transient
    private String owner;

    @Transient
    private Integer ownerId;

    @Transient
    private String contextName;
    @Transient
    private String status;

    public PLMDeliverable() {
    }

}
