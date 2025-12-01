package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Nageshreddy on 04-09-2017.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWTYPE")
@PrimaryKeyJoinColumn(name = "TYPE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowType extends CassiniObject {

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PARENT_TYPE")
	private Integer parentType;

	@Column(name = "ASSIGNABLE")
	private String assignable;

	@Column(name = "ASSIGNED_TYPE")
	private Integer assignedType;

	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "NUMBER_SOURCE", nullable = true)
	private AutoNumber numberSource;

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "LIFECYCLE", nullable = true)
	private PLMLifeCycle lifecycle;

	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
	private Lov revisionSequence;

	@Transient
	private List<PLMWorkflowType> children = new ArrayList<>();

	@Transient
	private List<PLMWorkflowTypeAttribute> attributes = new ArrayList<>();

	@Transient
	private PLMWorkflowType parentTypeReference;

	public PLMWorkflowType() {
		super(PLMObjectType.WORKFLOWTYPE);
	}

	@Transient
	@JsonIgnore
	public PLMWorkflowType getChildTypeByPath(String path) {
		PLMWorkflowType workflowType = null;

		Map<String, PLMWorkflowType> childrenMap = children.stream()
				.collect(Collectors.toMap(PLMWorkflowType::getName, Function.identity()));

		int index = path.indexOf('/');
		String name;
		if(index != -1) {
			name = path.substring(0, index);
			PLMWorkflowType childType = childrenMap.get(name);
			if(childType != null) {
				workflowType = childType.getChildTypeByPath(path.substring(index + 1));
			}
		}
		else {
			name = path;
			workflowType = childrenMap.get(name);
		}
		return workflowType;
	}


}
