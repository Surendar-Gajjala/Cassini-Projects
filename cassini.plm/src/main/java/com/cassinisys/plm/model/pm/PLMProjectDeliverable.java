package com.cassinisys.plm.model.pm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by swapna on 12/31/17.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_PROJECTDELIVERABLE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProjectDeliverable extends PLMDeliverable {

    @Column(name = "PROJECT")
    private Integer project;

    public PLMProjectDeliverable() {
    }

}
