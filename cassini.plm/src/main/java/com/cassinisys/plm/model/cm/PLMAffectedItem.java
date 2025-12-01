package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 022 22-May -17.
 */
@Entity
@Data
@Table(name = "PLM_AFFECTEDITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class PLMAffectedItem implements Serializable {

    @Id
    @SequenceGenerator(name = "AFFECTEDITEM_ID_GEN", sequenceName = "AFFECTEDITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AFFECTEDITEM_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    private Integer id;

    @Column(name = "CHANGE", nullable = false)
    private Integer change;

    @Column(name = "ITEM", nullable = false)
    private Integer item;

    @Column(name = "TO_ITEM", nullable = false)
    private Integer toItem;

    @Column(name = "FROM_REVISION", nullable = false)
    private String fromRevision;

    @Column(name = "TO_REVISION", nullable = false)
    private String toRevision;

    @Column(name = "NOTES", nullable = false)
    private String notes;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;

    @Column(name = "CHANGE_REQUESTS")
    @Type(type = "com.cassinisys.platform.util.converter.IntArrayUserType")
    private Integer[] changeRequests = new Integer[0];

    @Transient
    private PLMItem itemObject;
    @Transient
    private PLMItemRevision configurableItem;
    @Transient
    private PLMItemRevision revisedItem;

    @Transient
    private List<PLMECR> ecrList = new ArrayList<>();


}
