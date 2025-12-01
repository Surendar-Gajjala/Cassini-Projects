package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GSR on 19-06-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_GLOSSARY")
@PrimaryKeyJoinColumn(name = "ID")
public class PLMGlossary extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "GLOSSARY_NUMBER")
    private String number;

    @Column(name = "REVISION")
    private String revision;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Column(name = "IS_RELEASED")
    private Boolean isReleased;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE")
    private Date releasedDate;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEFAULT_DETAIL")
    private PLMGlossaryDetails defaultDetail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEFAULT_LANGUAGE")
    private PLMGlossaryLanguages defaultLanguage;

    @Transient
    private List<PLMGlossaryDetails> glossaryDetails = new ArrayList<>();

    @Transient
    private PLMGlossaryDetails defaultGlossaryDetail;

    @Transient
    private GlossaryEntryPermission glossaryEntryPermission;

    public PLMGlossary() {
        super(PLMObjectType.TERMINOLOGY);
    }


}
