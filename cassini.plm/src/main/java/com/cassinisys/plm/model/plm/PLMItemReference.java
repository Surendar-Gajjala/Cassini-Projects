package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by Home on 4/20/2016.
 */
@Entity
@Data
@Table(name = "PLM_ITEMREFERENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItemReference {

    @Id
    @SequenceGenerator(name = "ITEMREFERENCE_ID_GEN", sequenceName = "ITEMREFERENCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMREFERENCE_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    private Integer id;

    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "ITEM", nullable = false)
    private Integer item;

    @Column(name = "REVISION", nullable = false)
    private String revision;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "NOTES", nullable = false)
    private String notes;

    public PLMItemReference() {
    }


    public PLMItemReference copy() {
        PLMItemReference copy = new PLMItemReference();
        copy.setItem(this.getItem());
        copy.setParent(this.getParent());
        copy.setRevision(this.getRevision());
        copy.setStatus(this.getStatus());
        copy.setNotes(this.getNotes());
        copy.setNotes(this.getNotes());
        return copy;
    }

}
