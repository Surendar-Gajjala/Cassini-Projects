package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_ASSEMBLY_BOM_OCCURRENCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMAssemblyBOMOccurrence extends PDMBOMOccurrence {
    @Column(name = "ASSEMBLY")
    private Integer assembly;
}
