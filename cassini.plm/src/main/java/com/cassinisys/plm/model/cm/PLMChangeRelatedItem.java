package com.cassinisys.plm.model.cm;

import com.cassinisys.plm.model.plm.PLMItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyamreddy on 022 22-May -17.
 */
@Entity
@Data
@Table(name = "PLM_CHANGE_RELATED_ITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMChangeRelatedItem implements Serializable  {

    @Id
    @SequenceGenerator(name = "CHANGE_RELATED_ITEM_ID_GEN", sequenceName = "CHANGE_RELATED_ITEM_ID_SEQ ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_RELATED_ITEM_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    private Integer id;

    @Column(name = "CHANGE", nullable = false)
    private Integer change;

    @Column(name = "ITEM", nullable = false)
    private Integer item;

    @Transient
    private PLMItem plmItem;


}
