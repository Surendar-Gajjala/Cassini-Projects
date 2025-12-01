package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by reddy on 22/12/15.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_BOM")
@PrimaryKeyJoinColumn(name = "BOMITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMBom extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT")
    private PLMItemRevision parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private PLMItem item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SUBSTITUTE_ITEM")
    private PLMItem substituteItem;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "REFDES")
    private String refdes;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "AS_RELEASED_REVISION")
    private Integer asReleasedRevision;

    @Column(name = "SEQUENCE")
    private Integer sequence;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFFECTIVE_FROM")
    private Date effectiveFrom;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFFECTIVE_TO")
    private Date effectiveTo;

    @Column(name = "HAS_SUBSTITUTES")
    private Boolean hasSubstitutes = Boolean.FALSE;

    @Transient
    private Integer children;

    @Transient
    private List<PLMBom> childrens = new LinkedList<>();

    @Transient
    private Integer level;

    @Transient
    private List<PLMBom> fromItems;

    @Transient
    private List<PLMBom> toItems;

    @Transient
    private List<PLMBom> fromRevisionItems;

    @Transient
    private List<PLMBom> toRevisionItems;

    @Transient
    private Integer updatedQty;

    @Transient
    private String updatedDefdes;

    @Transient
    private String color;

    @Transient
    private String rev;
    @Transient
    private String updatedRevision;

    @Transient
    private String updatedLifeCycle;
    @Transient
    private String lifeCycle;

    @Transient
    private String updatedNotes;

    @Transient
    private Integer count = 0;

    @Transient
    private PLMItemRevision bomItemRevision;

    @Transient
    private Boolean itemExist = Boolean.FALSE;
    @Transient
    private Integer mfrId;
    @Transient
    private Integer mfrPartId;
    @Transient
    private String mfrPartNumber;
    @Transient
    private String mfrPartName;
    @Transient
    private PLMItemRevision asReleasedRevisionObject;
    @Transient
    private String path;

    public PLMBom() {
        super(PLMObjectType.BOMITEM);
    }

    public PLMBom copy() {
        PLMBom copy = new PLMBom();
        copy.setItem(this.getItem());
        copy.setParent(this.getParent());
        copy.setQuantity(this.getQuantity());
        copy.setRefdes(this.getRefdes());
        copy.setNotes(this.getNotes());
        copy.setSequence(this.getSequence());
        return copy;
    }


}

