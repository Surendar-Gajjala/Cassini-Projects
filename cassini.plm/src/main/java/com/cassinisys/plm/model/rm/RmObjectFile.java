package com.cassinisys.plm.model.rm;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by CassiniSystems on 20-10-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_OBJECTFILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RmObjectFile extends PLMFile {

    @Column(name = "OBJECT")
    private Integer object;


}
