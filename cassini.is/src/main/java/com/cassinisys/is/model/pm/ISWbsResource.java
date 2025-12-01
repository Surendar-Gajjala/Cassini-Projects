package com.cassinisys.is.model.pm;
/**
 * Model for ISWbsResource
 */

import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_WBSRESOURCE")
@PrimaryKeyJoinColumn(name = "RESOURCE_ID")
@ApiObject(name = "PM")
public class ISWbsResource extends ISProjectResource {
}
