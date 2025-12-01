<div id="events-view" style="overflow: auto">
    <style scoped>
        #events-table .ui-select-choices {
            position: absolute !important;
        }

        .workflow-event-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 5%; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .workflow-event-model .workflow-event-content {
            margin: auto;
            display: block;
            height: 80%;
            width: 60%;
            background-color: white;
            border-radius: 7px !important;
        }

        .workflow-event-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .workflow-event-footer {
            border-bottom: 1px solid lightgrey;
            padding: 5px;
            text-align: center;
            height: 50px;
            width: 100%;
            display: flex;
        }

        .event-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        .event-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
        }

        .event-close {
            position: relative;
            right: 0;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .event-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .event-close:before, .event-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .event-close:before {
            transform: rotate(45deg) !important;
        }

        .event-close:after {
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

        .mt-55 {
            margin-top: 55px;
        }
    </style>
    <div class="form-group" style="padding-top: 10px;position: absolute;width: 100%;"
         ng-if="!wfEditorVm.workflow.released">
        <label class="col-sm-4" style="text-align: right;margin-top: 6px;">
            <span translate>ADD_EVENT</span>
            <span class="asterisk">*</span> : </label>

        <div class="col-sm-7">
            <ui-select ng-model="wfEditorVm.selectedEvent" theme="bootstrap"
                       style="width:100%" on-select="onSelectEvent($item)">
                <ui-select-match placeholder="Select Event">
                    {{$select.selected.label}}
                </ui-select-match>
                <ui-select-choices
                        repeat="eventType.value as eventType in eventTypes">
                    <div>{{eventType.label}}</div>
                </ui-select-choices>
            </ui-select>
        </div>
    </div>
    <table id="events-table" class="table table-striped" ng-class="{'mt-55':wfEditorVm.workflow.released == false}">
        <thead>
        <tr>
            <th></th>
            <th style="width: 1% !important;white-space: nowrap;" translate>EVENT_NAME</th>
            <th style="min-width: 180px;" translate>ACTIVITY</th>
            <th style="min-width: 180px;" translate>ACTION_TYPE</th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ACTION_DATA</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="eventsLoading == true">
            <td colspan="25">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5">
                    <span translate>LOADING_EVENTS</span>
                </span>
            </td>
        </tr>
        <tr ng-if="events.length == 0 && eventsLoading == false">
            <td colspan="15" translate>NO_EVENTS</td>
        </tr>
        <tr ng-if="events.length > 0 && eventsLoading == false"
            ng-repeat="event in events">
            <td style="vertical-align: middle !important;">
                <i class="las la-plus" style="cursor: pointer;"
                   ng-hide="wfEditorVm.workflow.released"
                   ng-click="addWorkflowEvent(event)"></i>
            </td>
            <td style="vertical-align: middle !important;min-width: 150px;">
                {{getEventTypeLabel(event)}}
            </td>
            <td>
                <div ng-repeat="workflowEvent in event.workflowDefinitionEvents" style="padding:5px 0">
                    <ui-select ng-model="workflowEvent.activity" theme="bootstrap"
                               style="width:100%" on-select="onSelectActivity($item,workflowEvent)"
                               ng-if="event.eventType == 'WorkflowActivityStart' || event.eventType == 'WorkflowActivityFinish'"
                               ng-disabled="wfEditorVm.workflow.released">
                        <ui-select-match placeholder="Select Activity">
                            {{$select.selected.name}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="status in workflowStatuses | filter: $select.search">
                            <div>{{status.name}}</div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </td>
            <td>
                <div ng-repeat="workflowEvent in event.workflowDefinitionEvents" style="padding:5px 0">
                    <ui-select ng-model="workflowEvent.actionType" theme="bootstrap"
                               style="width:100%"
                               ng-disabled="wfEditorVm.workflow.released">
                        <ui-select-match placeholder="Select Action">
                            {{$select.selected.label}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="actionType.value as actionType in workflowActionTypes | filter: $select.search">
                            <div>{{actionType.label}}</div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </td>
            <td style="width: 1% !important;white-space: nowrap;">
                <div ng-repeat="workflowEvent in event.workflowDefinitionEvents" style="padding:5px 0;height: 40px;">
                    <a href=""
                       ng-if="workflowEvent.actionType == 'SetLifecyclePhase' && !wfEditorVm.workflow.released"
                       ng-click="showSetLifecycleDialog(workflowEvent)">
                        <span ng-if="workflowEvent.actionData == null || workflowEvent.actionData == ''">Edit</span>
                        <span ng-if="workflowEvent.actionData != null && workflowEvent.actionData != ''">Update</span>
                    </a>
                    <a href=""
                       ng-if="workflowEvent.actionType == 'SetLifecyclePhase' && wfEditorVm.workflow.released">
                        <span ng-click="showSetLifecycleDialog(workflowEvent)">View</span>
                    </a>
                    <a href=""
                       ng-if="workflowEvent.actionType == 'ExecuteScript' && wfEditorVm.workflow.released">
                        <span ng-click="showScriptDialog(workflowEvent)">View</span>
                    </a>
                    <a href=""
                       ng-if="workflowEvent.actionType == 'ExecuteScript' && !wfEditorVm.workflow.released"
                       ng-click="showScriptDialog(workflowEvent)">
                        <span ng-if="workflowEvent.actionData == null || workflowEvent.actionData == ''">Edit</span>
                        <span ng-if="workflowEvent.actionData != null && workflowEvent.actionData != ''">Update</span>
                    </a>

                    <i class="la la-save" ng-if="workflowEvent.actionData != null && workflowEvent.actionData != ''"
                       ng-hide="wfEditorVm.workflow.released"
                       style="padding-top: 5px;cursor: pointer;" title="Save"
                       ng-click="saveWorkflowEvent(workflowEvent)"></i>
                    <i class="la la-trash" ng-click="deleteWorkflowEvent(event,workflowEvent)"
                       ng-hide="wfEditorVm.workflow.released"
                       style="padding-top: 5px;cursor: pointer;" title="Delete"></i>
                </div>
            </td>
        </tr>
        </tbody>
    </table>

    <div id="workflow-event" class="workflow-event-model modal">
        <div class="workflow-event-content">
            <div class="workflow-event-header">
                <span class="event-header">{{wfEditorVm.dialogHeader}}</span>
                <a href="" ng-click="hideWorkflowEvent()" class="event-close pull-right"
                   style="display: inline-block;"></a>
                <a href="" ng-click="updateEventData()" class="btn btn-xs btn-success pull-right"
                   style="display: inline-block;position: relative;right: 5px;top: 5px;">Save</a>
            </div>
            <div id="configuration-error" class="alert {{notificationBackground}} animated">
                <span style="margin-right: 10px;"><i class="fa {{notificationClass}}"></i></span>
                <a href="" class="config-close-btn" ng-click="closeErrorNotification()"></a>
                {{message}}
            </div>
            <div class="event-content" style="padding: 10px;overflow: auto;height: calc(100% - 30px);">
                <div ng-if="selectedWorkflowEvent.actionType == 'SetLifecyclePhase'" style="height: 100%;">
                    <div class="form-group" style="width: 100%;"
                         ng-if="!wfEditorVm.workflow.released">
                        <label class="col-sm-4" style="text-align: right;margin-top: 6px;">
                            <span translate>ADD_LIFECYCLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="wfEditorVm.selectedLifecycle" theme="bootstrap"
                                       style="width:100%" on-select="addLifecycle($item)">
                                <ui-select-match placeholder="Select Lifecycle">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="lifecycle in lifecycles">
                                    <div>{{lifecycle.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <table class='table table-striped highlight-row'>
                        <thead>
                        <tr>
                            <th translate>LIFECYCLE</th>
                            <th translate>LIFE_CYCLE_PHASE</th>
                            <th style="width: 40px;"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="data in selectedWorkflowEvent.actionDataList">
                            <td>
                                {{data.lifecycle.name}}
                            </td>
                            <td>
                                <ui-select ng-model="data.lifecyclePhase" theme="bootstrap"
                                           style="width:100%" ng-disabled="wfEditorVm.workflow.released"
                                           ng-if="((selectedWorkflowEvent.activity == null) || (selectedWorkflowEvent.activity.type == 'NORMAL') || (selectedWorkflowEvent.activity.type == 'RELEASED' && selectedWorkflowEvent.eventType == 'WorkflowActivityStart'))">
                                    <ui-select-match placeholder="Select Lifecycle Phase">
                                        {{$select.selected.phase}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lifecyclePhase in data.lifecycle.normalLifecyclePhases | filter: $select.search">
                                        <div>{{lifecyclePhase.phase}}</div>
                                    </ui-select-choices>
                                </ui-select>
                                <ui-select ng-model="data.lifecyclePhase" theme="bootstrap"
                                           style="width:100%" ng-disabled="wfEditorVm.workflow.released"
                                           ng-if="(selectedWorkflowEvent.activity.type == 'RELEASED' && selectedWorkflowEvent.eventType == 'WorkflowActivityFinish')">
                                    <ui-select-match placeholder="Select Lifecycle Phase">
                                        {{$select.selected.phase}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lifecyclePhase in data.lifecycle.releasedLifecyclePhases | filter: $select.search">
                                        <div>{{lifecyclePhase.phase}}</div>
                                    </ui-select-choices>
                                </ui-select>
                                <ui-select ng-model="data.lifecyclePhase" theme="bootstrap"
                                           style="width:100%" ng-if="selectedWorkflowEvent.activity.type == 'REJECTED'"
                                           ng-disabled="wfEditorVm.workflow.released">
                                    <ui-select-match placeholder="Select Lifecycle Phase">
                                        {{$select.selected.phase}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lifecyclePhase in data.lifecycle.rejectedLifecyclePhases | filter: $select.search">
                                        <div>{{lifecyclePhase.phase}}</div>
                                    </ui-select-choices>
                                </ui-select>
                            </td>
                            <td style="vertical-align: middle !important;text-align: center;">
                                <i class="la la-trash" ng-hide="wfEditorVm.workflow.released"
                                   ng-click="removeLifecycle(data)" title="Remove"
                                   style="cursor: pointer;"></i>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div ng-if="selectedWorkflowEvent.actionType == 'ExecuteScript'" style="height: calc(100% - 20px);">
                    <pre id="execute-script" style="height: 100%;" ng-model="wfEditorVm.script"></pre>
                </div>
            </div>
        </div>
    </div>
</div>