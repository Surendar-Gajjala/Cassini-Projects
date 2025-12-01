package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "PGC_MATERIAL")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCMaterial extends PGCObject {

    public PGCMaterial() {
        super.setObjectType(PGCEnumObject.PGCMATERIAL);
    }

}
