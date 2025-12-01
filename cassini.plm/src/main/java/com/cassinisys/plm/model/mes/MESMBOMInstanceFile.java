package com.cassinisys.plm.model.mes;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by smukka on 04-10-2022.
 */
@Entity
@Table(name = "MES_MBOMINSTANCE_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESMBOMInstanceFile extends PLMFile {

    @Column(name = "MBOM_INSTANCE")
    private Integer mbomInstance;
}
