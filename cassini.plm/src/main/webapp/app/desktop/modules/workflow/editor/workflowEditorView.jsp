<div class="view-container" fitcontent>
    <style scoped>
        .wf-props-panel {
            border-left: 1px dotted #D3D3D3;
            padding: 10px;
        }

        .item-rev {
            font-size: 16px;
            font-weight: normal;
        }

        .item-number {
            display: inline-block;
        }

        .workflow-tabs .tab-content {
            padding: 0;
        }

        .workflow-form-data-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 50px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .workflow-form-data-model .workflow-form-data-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .workflow-form-data-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .workflow-form-data-footer {
            border-bottom: 1px solid lightgrey;
            padding: 5px;
            text-align: center;
            height: 50px;
            width: 100%;
            display: flex;
        }

        .form-data-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        .form-data-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
        }

        .form-data-close {
            position: absolute;
            right: 35px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .form-data-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .form-data-close:before, .form-data-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .form-data-close:before {
            transform: rotate(45deg) !important;
        }

        .form-data-close:after {
            transform: rotate(-45deg) !important;
        }

        #configuration-error {
            display: none;
            padding: 11px !important;
            margin-bottom: 0 !important;
            width: 100%;
            height: 50px;
            margin-top: -50px;
            float: left;
            position: relative;
        }

        .config-close-btn {
            position: absolute;
            right: 40px;
            top: 7px;
            width: 32px;
            height: 32px;
            opacity: 0.3;
        }

        .config-close-btn:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close-btn:before, .config-close-btn:after {
            position: absolute;
            top: 9px;
            left: 15px;
            content: ' ';
            height: 15px;
            width: 2px;
            background-color: #333;
        }

        .config-close-btn:before {
            transform: rotate(45deg) !important;
        }

        .config-close-btn:after {
            transform: rotate(-45deg) !important;
        }
    </style>
    <div class="view-toolbar">
        <div class="btn-group">

            <button class="btn btn-sm btn-default" ng-click="wfEditorVm.gotoAllWorkflows()"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

            <button class="btn btn-sm btn-default" title="Show revision history"
                    ng-click="wfEditorVm.showWorkflowRevisionHistory()" ng-disabled="wfEditorVm.workflow.id == null">
                <i class="fa fa-history" aria-hidden="true" style=""></i>
            </button>

            <button class="btn btn-sm btn-success" ng-if="hasPermission('plmworkflowdefinition','edit')"
                    ng-click="wfEditorVm.saveWorkflow()"
                    ng-disabled="wfEditorVm.workflow.released"
                    title="{{'WORKFLOW_NEW_HEAD_SAVE' | translate}}">
                <i class="fa fa-save" style="" aria-hidden="true"></i>
            </button>

            <button class="btn btn-sm btn-success"
                    ng-if="wfEditorVm.mode == 'edit' && hasPermission('plmworkflowdefinition','edit')"
                    ng-click="wfEditorVm.showSaveAsPanel()"
                    title="{{'WORKFLOW_NEW_HEAD_SAVEAS' | translate}}">
                <i class="fa fa-copy" style="" aria-hidden="true"></i>
            </button>

            <button class="btn btn-sm btn-success"
                    ng-if="wfEditorVm.mode == 'edit'"
                    ng-hide="wfEditorVm.workflow.lifeCyclePhase.phase == wfEditorVm.firstLifecyclePhase.phase || wfEditorVm.workflow.released"
                    ng-class="{'cursor-override': !hasPermission('plmworkflowdefinition','demote')}"
                    title="{{hasPermission('plmworkflowdefinition','demote') ? 'Demote' : noPermission}}"
                    ng-click="wfEditorVm.demoteDefinition()">
                <i class="fa fa-toggle-left"
                   ng-class="{'disabled': !hasPermission('plmworkflowdefinition','demote')}" style=""></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{hasPermission('plmworkflowdefinition','promote') ? 'Promote' : noPermission}}"
                    ng-if="wfEditorVm.workflow.lifeCyclePhase.phase != wfEditorVm.lastLifecyclePhase.phase && wfEditorVm.mode == 'edit'"
                    ng-class="{'cursor-override': !hasPermission('plmworkflowdefinition','promote')}"
                    title="Promote" ng-click="wfEditorVm.promoteDefinition()">
                <i class="fa fa-toggle-right" ng-class="{'disabled': !hasPermission('plmworkflowdefinition','promote')}"
                   style=""></i>
            </button>
            <button class="btn btn-sm btn-success"
                    ng-show="wfEditorVm.workflow.released
                    && wfEditorVm.workflow.master.latestRevision == wfEditorVm.workflow.id && hasPermission('plmworkflowdefinition','edit')"
                    ng-click="wfEditorVm.reviseDefinition()"
                    title="New revision">
                <i class="fa fa-random"></i>
            </button>

            <button class="btn btn-sm btn-success"
                    ng-show="wfEditorVm.workflow.released"
                    ng-click="wfEditorVm.workflowInstances()"
                    title="Show instances">
                <i class="fa flaticon-plan2 nav-icon-font"></i>
            </button>

        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-8" style="padding: 10px;">
                <div id="canvas" style="width: 100%">
                    <iframe id="workflowFrame"
                            ng-if="!wfEditorVm.workflow.released"
                            src="app/assets/js/bpmn-js/index.html"
                            iframe-onload="wfEditorVm.onEditorLoad()"
                            frameborder="0" height="100%" width="100%"></iframe>
                </div>
            </div>
            <div class="col-sm-4 wf-props-panel">
                <div ng-if="wfEditorVm.workflow != null && wfEditorVm.selectedElement.type == 'bpmn:Process'">
                    <div class="row row-eq-height" style="margin: 0;">
                        <div class="col-sm-12" style="padding: 0;">
                            <div class="workflow-tabs">
                                <uib-tabset active="active">
                                    <uib-tab heading="Details"
                                             active="tabs.details.active"
                                             select="selectWorkflowTab(tabs.details.id)">
                                        <div ng-include="tabs.details.template"></div>
                                    </uib-tab>
                                    <uib-tab heading="Events"
                                             ng-hide="wfEditorVm.workflow.id == null"
                                             active="tabs.events.active"
                                             select="selectWorkflowTab(tabs.events.id)">
                                        <div ng-include="tabs.events.template"></div>
                                    </uib-tab>
                                </uib-tabset>
                            </div>
                        </div>
                    </div>
                </div>
                <div ng-if="wfEditorVm.selectedElement != null && wfEditorVm.selectedElement.type == 'bpmn:Task' &&
                            wfEditorVm.selectedStatus != null">
                    <div style="margin-bottom: 20px;">
                        <h4 translate>WORKFLOW_NEW_STATUS_DETAILS</h4>
                    </div>
                    <div class="form-group">
                        <label class="control-label" translate>WORKFLOW_ALL_NAME</label>
                        <input type="text" class="form-control" placeholder="{{'ENTER_STATE_NAME'| translate}}"
                               ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released"
                               ng-model="wfEditorVm.selectedStatus.name"
                               ng-change="wfEditorVm.updateStatus()">
                    </div>
                    <div class="form-group">
                        <label class="control-label" translate>DESCRIPTION</label> :
                        <textarea class="form-control" rows="3" style="resize: none;"
                                  ng-model="wfEditorVm.selectedStatus.description"
                                  placeholder="{{'ENTER_DESCRIPTION_TITLE'| translate}}"
                                  ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released"></textarea>
                    </div>
                    <div class="form-group">
                        <label class="control-label" translate>WORKFLOW_NEW_STATUS_TYPE</label> :
                        <ui-select ng-model="wfEditorVm.selectedStatus.type" theme="bootstrap" style="width:100%"
                                   ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released"
                                   on-select="wfEditorVm.onSelectStatusType($item)">
                            <ui-select-match placeholder="{{wfEditorVm.select}}">{{$select.selected}}</ui-select-match>
                            <ui-select-choices
                                    repeat="type in ['NORMAL', 'RELEASED', 'REJECTED'] | filter: $select.search">
                                <div ng-bind="type"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div class="form-group" ng-hide="wfEditorVm.workflow.id == null">
                        <a href="" ng-click="wfEditorVm.showActivityFormData()"
                           style="font-weight: 600;padding-left: 2px;">
                            <span ng-if="!wfEditorVm.workflow.released">Add form data</span>
                            <span ng-if="wfEditorVm.workflow.released">View form data</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="workflow-form-data" class="workflow-form-data-model modal">
        <div class="workflow-form-data-content">
            <div class="workflow-form-data-header">
                <span class="form-data-header">( {{wfEditorVm.selectedStatus.name}} ) Activity Form Data</span>
                <a href="" ng-click="hideActivityFormData()" class="form-data-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div id="configuration-error" class="alert {{formDataNotificationBackground}} animated">
                <span style="margin-right: 10px;"><i class="fa {{formDataNotificationClass}}"></i></span>
                <a href="" class="config-close-btn" ng-click="closeFormDataErrorNotification()"></a>
                {{formDataMessage}}
            </div>
            <div class="form-data-content" style="padding: 10px;overflow: auto;">
                <classification-attribute-view
                        has-permission="!wfEditorVm.workflow.released"></classification-attribute-view>
            </div>
        </div>
    </div>
</div>