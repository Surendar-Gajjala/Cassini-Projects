package com.cassinisys.plm.model.cm;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by lakshmi on 1/2/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_CHANGEFILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMChangeFile extends PLMFile {
    @Column(name = "CHANGE", nullable = false)
    private Integer change;
    @Transient
    private String createdPerson;


}
