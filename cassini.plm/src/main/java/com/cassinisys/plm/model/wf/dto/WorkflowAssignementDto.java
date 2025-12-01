package com.cassinisys.plm.model.wf.dto;


import lombok.Data;

import java.util.Date;
@Data
public class WorkflowAssignementDto  {

  private String workflowName;
  private String workflowStatus;
  private Date assignedDate;
  private Integer objectId;
  private String objectType;
  private String objectName;

}
