package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 19-06-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_GLOSSARYENTRY")
@PrimaryKeyJoinColumn(name = "ID")
public class PLMGlossaryEntry extends CassiniObject {

    @Transient
    List<PLMGlossaryEntryDetails> glossaryEntryDetails = new ArrayList<>();
    @Transient
    List<PLMGlossaryEntryEdit> entryEdits = new ArrayList<>();
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "VERSION")
    private Integer version;
    @Column(name = "LATEST")
    private Boolean latest = Boolean.FALSE;
    @Column(name = "DEFAULT_NAME")
    private String defaultName;
    @Column(name = "DEFAULT_DESCRIPTION")
    private String defaultDescription;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEFAULT_DETAIL")
    private PLMGlossaryEntryDetails defaultDetail;
    @Column(name = "NOTES")
    private String notes;
    @Transient
    private PLMGlossaryEntryDetails defaultGlossaryEntryDetail;

    public PLMGlossaryEntry() {
        super(PLMObjectType.TERMINOLOGYENTRY);
    }


}
