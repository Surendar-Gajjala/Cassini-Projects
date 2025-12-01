package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.Specification;
import com.cassinisys.plm.model.wf.PLMWorkFlowStatusAcknowledger;
import com.cassinisys.plm.model.wf.PLMWorkFlowStatusApprover;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CassiniSystems on 19-05-2020.
 */
@Data
public class WorkflowDto {

	List<PLMWorkFlowStatusApprover> workFlowStatusApprovers = new ArrayList<>();
	List<PLMWorkFlowStatusAcknowledger> workFlowStatusAcknowledgers = new ArrayList<>();
	private Integer id;
	private PLMWorkflow workflow;
	private String type;
	private String objectType;
	private String number;
	private String currentStatus;
	private String revision;
	private String previousStatus;
	private String holdBy;
	private String wfRevision;
	private String name;
	private String description;
	private String workflowNumber;
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startedOn;
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date finishedOn;
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date cancelledOn;
	@Type(type = "com.cassinisys.platform.util.EnumUserType",
			parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
	@Column(name = "TYPE", nullable = false)
	private WorkflowStatusType statusType = WorkflowStatusType.NORMAL;
	@Type(type = "com.cassinisys.platform.util.EnumUserType",
			parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
	@Column(name = "TYPE", nullable = false)
	private WorkflowStatusType previousStatuType = WorkflowStatusType.NORMAL;
	private PLMItem item;
	private PLMECO eco;
	private PLMDCR dcr;
	private PLMDCO dco;
	private PLMProject project;
	private PLMActivity activity;
	private PLMTask task;
	private Specification specification;
	private Requirement requirement;
	private PLMManufacturer manufacturer;
	private PLMManufacturerPart manufacturerPart;
	private PQMInspectionPlan inspectionPlan;
	private PQMInspection inspection;
	private PQMProblemReport problemReport;
	private PQMNCR ncr;
	private PQMQCR qcr;
	private PLMMCO mco;
	private PLMECR ecr;
	private PLMVariance variance;


}
