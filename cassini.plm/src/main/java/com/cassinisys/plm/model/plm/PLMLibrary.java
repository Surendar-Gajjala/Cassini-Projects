package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_LIBRARY")
@PrimaryKeyJoinColumn(name = "LIBRARY_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMLibrary extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT")
    private Integer parent;

    protected PLMLibrary() {
        super(PLMObjectType.LIBRARY);
    }


}
