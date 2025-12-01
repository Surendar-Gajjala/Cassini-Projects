package com.cassinisys.platform.model.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "CUSTOM_REVISIONED_OBJECT_LIFECYCLE_HISTORY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomRevisionedObjectLifecycleHistory extends CustomObjectLifecycleHistory {

    @Column(name = "OBJECT")
    private Integer customRevisionedObject;
}
