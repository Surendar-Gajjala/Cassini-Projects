<div style="position: absolute; left:0; bottom: 0; right: 0; top: 0;overflow: hidden;">
    <style scoped>
        .wf-props-panel {
            border-left: 1px dotted #D3D3D3;
            padding: 10px;
        }

        .djs-group {
            cursor: pointer !important;
        }

        .djs-element.inprocess-status .djs-visual rect {
            fill: #60b6ff !important;
        }

        .djs-element.inprocess-status .djs-visual circle {
            fill: #60b6ff !important;
        }

        .djs-element.onhold-status .djs-visual rect {
            fill: #efc86a !important;
        }

        .djs-element.onhold-status .djs-visual circle {
            fill: #efc86a !important;
        }

        .djs-element.terminated-status .djs-visual rect {
            fill: #ee6965 !important;
        }

        .djs-element.terminated-status .djs-visual circle:nth-child(2) {
            fill: #ee6965 !important;
        }

        .djs-element.completed-status .djs-visual rect {
            fill: #3AD0BA !important;
        }

        .djs-element.completed-status .djs-visual circle {
            fill: #3AD0BA !important;
        }

        .button-container {
            padding: 10px;
            border-bottom: 1px solid #eee;
            height: 48px;
        }

        .open > .dropdown-toggle.btn {
            color: beige !important;
        }

        .password-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 100px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .password-model .passwordModel-content {
            margin: auto;
            display: block;
            height: 200px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #password-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #password-content {
            height: 100px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        input.password {
            -webkit-text-security: disc;
        }

        .password-header {
            font-weight: bold;
            font-size: 22px;
        }

        #password-footer {
            border-top: 1px solid lightgrey;
            padding: 5px;
            text-align: right;
            height: 50px;
            width: 100%;
            background-color: #edeeef;
            border-bottom-left-radius: 7px;
            border-bottom-right-radius: 7px;
        }

        .hold-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 100px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .hold-model .holdModel-content {
            margin: auto;
            display: block;
            height: 235px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #hold-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #hold-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        .hold-header {
            font-weight: bold;
            font-size: 22px;
        }

        #hold-footer {
            border-top: 1px solid lightgrey;
            padding: 5px;
            text-align: right;
            height: 40px;
            width: 100%;
            background-color: #edeeef;
            border-bottom-left-radius: 7px;
            border-bottom-right-radius: 7px;
        }

        .btn.disabled, .btn[disabled] {
            opacity: 0.5;
        }

    </style>
    <div class="row row-eq-height" style="margin: 0;height: 100%;">
        <div class="col-sm-12" style="padding: 7px;margin-left: 10px;" ng-if="object.workflow == null">
            <p>{{noWorkflowMsg}}</p>

            <div class="no-data">
                <img src="app/assets/no_data_images/Workflow.png" alt="" class="image">

                <div class="message">{{ 'MO_WORKFLOW' | translate}}</div>
                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
            </div>
        </div>
        <div class="col-sm-8" style="padding: 0" ng-show="object.workflow != null">
            <div class="button-container"
                 ng-if="!workflow.finished && permission">
                <button class="btn btn-xs btn-primary mr5" ng-click="startWorkflow()"
                        ng-disabled="!canStartWorkflow"
                        ng-if="workflow.currentStatus == workflow.start.id" translate>
                    START_WORKFLOW
                </button>

                <div style="display: inline-block" uib-dropdown
                     ng-if="workflow.currentStatusObject.transitions.length > 1 && workflow.currentStatusObject.flag !== 'ONHOLD'">
                    <button uib-dropdown-toggle type="button" class="btn btn-xs btn-success min-width dropdown-toggle"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false" translate>PROMOTE<span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-repeat="transition in workflow.currentStatusObject.transitions">
                            <a href="" ng-click="promote(transition.toStatus)">{{transition.toStatusObject.name}}</a>
                        </li>
                    </ul>
                </div>
                <button class="btn btn-xs btn-success mr5"
                        ng-if="workflow.currentStatusObject.transitions.length == 1 && workflow.currentStatusObject.flag !== 'ONHOLD'"
                        ng-click="promote(workflow.currentStatusObject.transitions[0].toStatus)"
                        ng-disabled="workflow.currentStatus == workflow.start.id ||
                            workflow.currentStatus == workflow.finish.id" translate>PROMOTE
                </button>

                <button class="btn btn-xs btn-danger mr5" ng-click="demote()"
                        ng-if="workflow.started && workflow.currentStatusObject.flag !== 'ONHOLD' &&
                        workflow.currentStatusObject.flag !== 'TERMINATED' && workflow.currentStatusObject.transitionedFrom != null"
                        ng-disabled="workflow.currentStatus == workflow.start.id ||
                            workflow.currentStatus == workflow.finish.id" translate>DEMOTE
                </button>

                <button style="float: right" class="btn btn-xs btn-warning mr5" ng-click="workflowHold()"
                        ng-disabled="workflow.started == false"
                        ng-if="workflow.currentStatus.type != 'START' &&
                        workflow.currentStatusObject.type != 'FINISH' &&
                        workflow.currentStatusObject.type != 'TERMINATE' &&
                        workflow.currentStatusObject.flag != 'ONHOLD'" translate>
                    HOLD
                </button>
                <button style="float: right" class="btn btn-xs btn-info mr5" ng-click="workflowOnHold()"
                        ng-disabled="personDetails.person.id != workflow.holdBy"
                        ng-if="workflow.currentStatus.type != 'START' &&
                        workflow.currentStatusObject.type != 'FINISH' &&
                        workflow.currentStatusObject.type != 'TERMINATE' &&
                        workflow.currentStatusObject.flag === 'ONHOLD'" translate>
                    REMOVE_HOLD
                </button>
            </div>
            <div id="canvas" class="workflow-viewer" style="width: 100%; padding: 20px;"></div>
        </div>
        <div class="col-sm-4 wf-props-panel" style="height: 100%;overflow-y: auto;" ng-show="object.workflow != null">
            <%@include file="workflowAssignments.jsp" %>
        </div>
    </div>
</div>

<div id="password-modal" class="password-model modal">
    <div class="passwordModel-content">
        <div id="password-header">
            <span ng-if="workflowStatus == 'APPROVE'" class="password-header"
                  translate>CHANGE_APPROVE_PASSWORD</span>
            <span ng-if="workflowStatus == 'REJECT'" class="password-header"
                  translate>CHANGE_REJECT_PASSWORD</span>
        </div>
        <div id="password-content">
            <p ng-if="personApprovalPassword.password != null && error != ''"
               style="margin-left: 200px; color: red;width:auto;font-size: 14px;">{{error}}
            </p>

            <div class="form-group">
                <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                    <span translate>PASSWORD</span>
                    <span class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                   <form>
                       <input type="text" class="form-control input-sm password"
                              placeholder="{{ENTER_APPROVAL_PASSWORD | translate}}"
                              ng-model="personApprovalPassword.password">
                   </form>
                </div>
            </div>
        </div>
        <div style="text-align: right; padding: 8px">
            <button class="btn btn-sm btn-default"
                    ng-click="hidePasswordDialog()" translate>
                CANCEL
            </button>
            <button class="btn btn-sm btn-success"
                    ng-click="voteApproval()"
                    ng-disabled="personApprovalPassword.password == null || personApprovalPassword.password == ''">
                <span ng-if="workflowStatus == 'APPROVE'" translate>APPROVE</span>
                <span ng-if="workflowStatus == 'REJECT'" translate>REJECT</span>
            </button>
        </div>
    </div>
</div>
<div id="hold-modal" class="hold-model modal">
    <div class="holdModel-content">
        <div id="hold-header">
            <span ng-if="holdType == 'HOLD'" class="hold-header" translate>WORKFLOW_HOLD</span>
            <span ng-if="holdType == 'ONHOLD'" class="hold-header" translate>WORKFLOW_REMOVE_HOLD</span>

            <div id="hold-content">
                <p ng-if="(notes == null || notes == '')  && error != ''"
                   style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{error}}
                </p>

                <div class="form-group">
                    <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                        <span translate>NOTES</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <textarea type="hold" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_NOTES' | translate}}"
                                  ng-model="notes"></textarea>
                    </div>
                </div>
            </div>
            <div style="text-align: right">
                <button class="btn btn-xs btn-default"
                        ng-click="hideHoldDialog()" translate>
                    CANCEL
                </button>
                <button ng-if="holdType == 'HOLD'" class="btn btn-xs btn-success"
                        ng-click="putWorkflowOnHold()" translate>
                    SUBMIT
                </button>
                <button ng-if="holdType == 'ONHOLD'" class="btn btn-xs btn-success"
                        ng-click="removeWorkflowOnHold()" translate>
                    SUBMIT
                </button>
            </div>
        </div>
    </div>
</div>
