package com.cassinisys.platform.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 08-12-2020.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class GroupProfileId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROFILE", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSONGROUP", nullable = false)
    private PersonGroup group;

    public GroupProfileId() {
    }

    public GroupProfileId(Profile profile, PersonGroup group) {
        this.profile = profile;
        this.group = group;
    }


}