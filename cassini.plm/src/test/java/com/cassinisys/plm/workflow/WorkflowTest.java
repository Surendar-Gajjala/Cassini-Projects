/*
package com.cassinisys.plm.workflow;

import com.cassinisys.platform.model.wfm.WorkFlowDefinition;
import com.cassinisys.platform.service.wfm.WorkFlowDefinitionService;
import com.cassinisys.plm.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.Rollback;

*/
/**
 * Created by reddy on 09/02/16.
 *//*

public class WorkflowTest extends BaseTest {

    @Value("classpath:workflow.json")
    private Resource workflowResource;

    @Autowired
    private WorkFlowDefinitionService workFlowDefinitionService;

    @Test
    @Rollback(false)
    public void testWorkflow() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        WorkFlowDefinition workflow = mapper.readValue(workflowResource.getInputStream(), WorkFlowDefinition.class);
        if(workflow != null) {
            workflow = workFlowDefinitionService.create(workflow);
            System.out.println(workflow.getId());
        }
    }

}
*/
