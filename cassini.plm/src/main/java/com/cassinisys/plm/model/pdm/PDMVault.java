package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_VAULT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMVault extends PDMObject {

    public PDMVault() {
        super(PDMObjectType.PDM_VAULT);
    }
}
