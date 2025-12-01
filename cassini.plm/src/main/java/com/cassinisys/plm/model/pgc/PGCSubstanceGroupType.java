package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 21-11-2020.
 */
@Entity
@Table(name = "PGC_SUBSTANCEGROUP_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCSubstanceGroupType extends PGCObjectType {

    @Transient
    private List<PGCSubstanceGroupType> childrens = new ArrayList<>();

    public PGCSubstanceGroupType() {
        super.setObjectType(PGCEnumObject.PGCSUBSTANCEGROUPTYPE);
    }
}
