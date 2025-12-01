package com.cassinisys.plm.model.pm;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by smukka on 24-07-2022.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PROJECT_TEMPLATETASK_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectTemplateTaskFile extends PLMFile {

    @Column
    private Integer task;

    public ProjectTemplateTaskFile() {
    }


}
