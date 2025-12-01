<div>
    <style scoped>
        .wf-props-panel {
            border-left: 1px dotted #D3D3D3;
            padding: 10px;
            /*background-color: #F9F9F9;*/
            background-color: #FFF;
        }

        .djs-group {
            cursor: pointer !important;
        }

        .bjs-powered-by {
            display: none !important;
        }

        .djs-palette {
            display: none;
        }

        .djs-palette-entries {
            display: none;
        }

        .djs-palette-toggle {
            display: none;
        }

        .djs-context-pad .entry {
            display: none !important;
        }

        .djs-element.inprocess-status .djs-visual rect {
            fill: #f6ff16 !important;
        }

        .djs-element.inprocess-status .djs-visual circle {
            fill: #f6ff16 !important;
        }

        .djs-element.completed-status .djs-visual rect {
            fill: #25F19B !important;
        }

        .djs-element.completed-status .djs-visual circle {
            fill: #25F19B !important;
        }

        .button-container {
            padding: 10px;
            border-bottom: 1px solid #eee;
        }

        .open > .dropdown-toggle.btn {
            color: beige !important;
        }

    </style>
    <div class="row row-eq-height" style="margin: 0" ng-show="taskWorkflowVm.workflow == null">
        <div ng-hide="taskWorkflowVm.workflow != null">
            <h5>Workflow not been assigned to Task yet</h5>
        </div>
    </div>
    <div class="row row-eq-height" style="margin: 0" ng-show="taskWorkflowVm.workflow != null">
        <div class="col-sm-8" style="padding: 0">
            <div class="button-container"
                 ng-if="!taskWorkflowVm.workflow.finished && hasPermission('permission.changes.edit')">
                <button class="btn btn-xs btn-primary mr5" ng-click="taskWorkflowVm.startWorkflow()"
                        ng-if="taskWorkflowVm.workflow.currentStatus == taskWorkflowVm.workflow.start.id">
                    Start Workflow
                </button>
                <div style="display: inline-block" uib-dropdown
                     ng-if="taskWorkflowVm.workflow.currentStatusObject.transitions.length > 1">
                    <button uib-dropdown-toggle type="button" class="btn btn-xs btn-success min-width dropdown-toggle"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">Promote<span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-repeat="transition in taskWorkflowVm.workflow.currentStatusObject.transitions">
                            <a href="" ng-click="taskWorkflowVm.promote(transition.toStatus)">{{transition.toStatusObject.name}}</a>
                        </li>
                    </ul>
                </div>
                <button class="btn btn-xs btn-success mr5"
                        ng-if="taskWorkflowVm.workflow.currentStatusObject.transitions.length == 1"
                        ng-click="taskWorkflowVm.promote(taskWorkflowVm.workflow.currentStatusObject.transitions[0].toStatus)"
                        ng-disabled="taskWorkflowVm.workflow.currentStatus == taskWorkflowVm.workflow.start.id ||
                            taskWorkflowVm.workflow.currentStatus == taskWorkflowVm.workflow.finish.id">Promote
                </button>
                <button class="btn btn-xs btn-danger mr5" ng-click="taskWorkflowVm.demote()"
                        ng-disabled="taskWorkflowVm.workflow.currentStatus == taskWorkflowVm.workflow.start.id ||
                            taskWorkflowVm.workflow.currentStatus == taskWorkflowVm.workflow.finish.id">Demote
                </button>
            </div>
            <div id="canvas" class="workflow-viewer" style="width: 100%; padding: 50px;"></div>
        </div>
        <div class="col-sm-4 wf-props-panel">
            <%@include file="workflowAssignments.jsp" %>
        </div>
    </div>
</div>