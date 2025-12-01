package com.cassinisys.plm.model.wf.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by CassiniSystems on 15-07-2020.
 */
@Data
public class WorkflowRevisionDto {

	private Integer id;
	private String name;
	private Integer latestRevision;
	private String number;
	private String description;
	private String type;
	private String createdBy;
	private String modifiedBy;
	private String status;
	private String objectType;
	private Integer instances = 0;

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date releasedDate;
	private String revision;
	private PLMLifeCyclePhase lifeCyclePhase;
	private Boolean released = false;


}
