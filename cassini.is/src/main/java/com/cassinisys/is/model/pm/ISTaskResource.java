package com.cassinisys.is.model.pm;
/**
 * Model for ISTaskResource
 */

import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_TASKRESOURCE")
@PrimaryKeyJoinColumn(name = "RESOURCE_ID")
@ApiObject(name = "PM")
public class ISTaskResource extends ISProjectResource {

}
