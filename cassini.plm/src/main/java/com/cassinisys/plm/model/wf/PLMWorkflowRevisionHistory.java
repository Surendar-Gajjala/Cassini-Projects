package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GSR on 15-07-2020.
 */
@Entity
@Data
@Table(name = "PLM_WORKFLOW_REVISION_HISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowRevisionHistory implements Serializable {

	@Id
	@SequenceGenerator(name = "WORKFLOW_REVISION_HISTORY_ID_GEN", sequenceName = "WORKFLOW_REVISION_HISTORY_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOW_REVISION_HISTORY_ID_GEN")
	@Column(name = "ID")
	private Integer id;

	@Column(name = "WORKFLOW")
	private Integer workflow;

	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "OLD_STATUS")
	private PLMLifeCyclePhase oldStatus;

	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "NEW_STATUS")
	private PLMLifeCyclePhase newStatus;

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP")
	private Date timestamp = new Date();

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;


}
