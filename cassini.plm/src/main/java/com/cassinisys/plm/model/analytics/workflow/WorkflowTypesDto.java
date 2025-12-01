package com.cassinisys.plm.model.analytics.workflow;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by CassiniSystems on 18-07-2020.
 */
@Data
public class WorkflowTypesDto {

	private List<Integer> workflowTypes = new LinkedList<>();

	private List<Integer> workflowObjectTypes = new LinkedList<>();

	private List<Integer> itemTypes = new LinkedList<>();

	private List<Integer> changeTypes = new LinkedList<>();

	private List<Integer> qualityTypes = new LinkedList<>();

	private List<Integer> ecoTypes = new LinkedList<>();

	private List<Integer> ecrTypes = new LinkedList<>();

	private List<Integer> dcrTypes = new LinkedList<>();

	private List<Integer> dcoTypes = new LinkedList<>();

	private List<Integer> mcoTypes = new LinkedList<>();

	private List<Integer> varianceTypes = new LinkedList<>();

	private List<Integer> inspectionRevisionTypes = new LinkedList<>();

	private List<Integer> qcrTypes = new LinkedList<>();

	private List<Integer> ncrTypes = new LinkedList<>();

	private List<Integer> prTypes = new LinkedList<>();

	private List<Integer> projectTypes = new LinkedList<>();

	private List<Integer> activityTypes = new LinkedList<>();

	private List<Integer> requirementTypes = new LinkedList<>();

	private List<Integer> specificationTypes = new LinkedList<>();

	private List<Integer> mfrTypes = new LinkedList<>();

	private List<Integer> mfrPartTypes = new LinkedList<>();

	private List<Integer> nprTypes = new LinkedList<>();

	private List<Integer> workOrderTypes = new LinkedList<>();

	private List<Integer> itemMcoTypes = new LinkedList<>();

	private List<Integer> partMcoTypes = new LinkedList<>();



}
