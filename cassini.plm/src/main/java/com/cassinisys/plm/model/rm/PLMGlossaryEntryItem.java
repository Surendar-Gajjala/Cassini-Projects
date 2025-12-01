package com.cassinisys.plm.model.rm;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 19-06-2018.
 */
@Entity
@Table(name = "PLM_GLOSSARYENTRYITEM")
public class PLMGlossaryEntryItem implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "GLOSSARYENTRYITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "GLOSSARY")
    private Integer glossary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ENTRY")
    private PLMGlossaryEntry entry;

    @Column(name = "NOTES")
    private String notes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGlossary() {
        return glossary;
    }

    public void setGlossary(Integer glossary) {
        this.glossary = glossary;
    }

    public PLMGlossaryEntry getEntry() {
        return entry;
    }

    public void setEntry(PLMGlossaryEntry entry) {
        this.entry = entry;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
