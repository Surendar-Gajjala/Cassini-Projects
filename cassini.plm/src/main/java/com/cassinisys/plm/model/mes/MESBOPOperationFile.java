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
 * Created by smukka on 28-07-2022.
 */
@Entity
@Data
@Table(name = "MES_BOP_OPERATION_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class MESBOPOperationFile extends PLMFile {

    @Column(name = "BOP_OPERATION")
    private Integer bopOperation;


}
