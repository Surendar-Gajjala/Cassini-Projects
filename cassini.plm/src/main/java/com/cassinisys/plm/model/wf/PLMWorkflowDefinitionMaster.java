package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by CassiniSystems on 15-07-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWDEFINITIONMASTER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowDefinitionMaster extends CassiniObject {

	@Column(name = "NUMBER")
	private String number;


	@Column(name = "INSTANCES")
	private Integer instances = 0;

	@Column(name = "LATEST_REVISION")
	private Integer latestRevision;
	@Column(name = "LATEST_RELEASED_REVISION")
	private Integer latestReleasedRevision;

	public PLMWorkflowDefinitionMaster() {
		super(PLMObjectType.PLMWORKFLOWDEFINITION);
	}


}
