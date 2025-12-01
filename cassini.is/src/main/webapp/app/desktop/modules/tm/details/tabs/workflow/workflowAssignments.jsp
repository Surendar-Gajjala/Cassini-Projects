<style scoped>
    .status-panel {
        border: 1px solid #d7d7d7;
        background-color: #fff;
        border-radius: 5px;
        margin-bottom: 20px;
    }

    .status-panel-heading {
        padding: 10px;
        border-bottom: 1px solid #ddd;
        color: #fff !important;
    }

    .status-panel-heading a {
        color: #fff;
    }

    .status-panel-title {
        font-size: 18px;
        margin: 0;
    }

    .status-panel-body {
        padding: 0;
    }

    .status-panel-btns {
        float: right;
    }

    .status-assignment {
        border-bottom: 1px solid #ddd;
        padding: 10px;
    }

    .status-assignment:nth-child(odd) {

    }

    .status-assignment h4 {
        margin: 0;
    }
</style>
<div ng-if="taskWorkflowVm.selectedStatus != null">
    <div class="status-panel">
        <div class="status-panel-heading" style="background-color: #428bca;border-color: #428bca;">
            <div class="status-panel-btns">
                <a href=""
                   ng-if="taskWorkflowVm.selectedStatus.flag == 'UNASSIGNED' && hasPermission('permission.changes.edit')"
                   ng-click="taskWorkflowVm.showPersons('approvers')"><i class="fa fa-plus"></i></a>
            </div>
            <!-- panel-btns -->
            <h3 class="status-panel-title">Approvers</h3>
        </div>
        <div class="status-panel-body">
            <div ng-if="taskWorkflowVm.selectedStatus.approvers.length == 0" style="padding: 10px;">
                <h5>No Approvers</h5>
            </div>
            <div class="status-assignment" ng-repeat="assignment in taskWorkflowVm.selectedStatus.approvers">
                <div class="row" style="margin: 0">
                    <div class="pull-right">
                        <i class="fa fa-times-circle" title="{{taskWorkflowVm.removePerson}}"
                           ng-click="taskWorkflowVm.deletePerson(assignment)"
                           style="font-size:18px;cursor:pointer;"></i>
                    </div>
                    <div class="pull-right" ng-if="taskWorkflowVm.workflow.started">
                        <div ng-if="assignment.vote == null">
                            <a href="" class="btn btn-success btn-xs" ng-if="hasPermission('permission.changes.edit')"
                               ng-click="assignment.vote = 'APPROVE'">Approve</a>
                            <a href="" class="btn btn-danger btn-xs" ng-if="hasPermission('permission.changes.edit')"
                               ng-click="assignment.vote = 'REJECT'">Reject</a>
                        </div>
                        <div ng-if="assignment.vote != null">
                            <span class="text-success" ng-if="assignment.vote == 'APPROVE'" style="font-weight: bolder">
                                <i class="fa fa-check mr5"></i>Approved
                            </span>
                            <span class="text-danger" ng-if="assignment.vote == 'REJECT'" style="font-weight: bolder">
                                <i class="fa fa-close mr5"></i>Rejected</span>
                        </div>
                    </div>
                    <div>
                        <h4>{{assignment.personObject.firstName}} {{assignment.personObject.lastName}}</h4>
                    </div>
                </div>
                <div style="margin-top: 10px;">
                    <div class="text-muted">Notes</div>
                    <p ng-if="assignment.comments != null && !assignment.editMode">{{assignment.comments}}</p>

                    <p ng-if="assignment.comments == null && !assignment.editMode" style="font-style: italic">
                        No Comments</p>

                    <div ng-if="assignment.editMode">
                        <textarea class="form-control" rows="5" ng-model="assignment.comments"
                                  style="resize: none"></textarea>

                        <div style="margin-top: 5px; text-align: right">
                            <button class='btn btn-xs btn-default'
                                    ng-click='assignment.editMode = false;assignment.comments = null'>Cancel
                            </button>
                            <button class='btn btn-xs btn-success' ng-click='taskWorkflowVm.saveAssignment(assignment)'>
                                Save
                            </button>
                        </div>
                    </div>
                    <div ng-if="assignment.comments == null && !assignment.editMode">
                        <a href="" ng-click="assignment.editMode = true"
                           ng-if="hasPermission('permission.changes.edit')"><span>Add Comment</span></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%--    <div class="status-panel">
            <div class="status-panel-heading" style="background-color: #5BC0DE;border-color: #bce8f1;">
                <div class="status-panel-btns">
                    <a href=""
                       ng-if="taskWorkflowVm.selectedStatus.flag == 'UNASSIGNED' && hasPermission('permission.changes.edit')"
                       ng-click="taskWorkflowVm.showPersons('acknowledgers')"><i class="fa fa-plus"></i></a>
                </div>
                <!-- panel-btns -->
                <h3 class="status-panel-title" translate>ACKNOWLEDGERS</h3>
            </div>
            <div class="status-panel-body">
                <div ng-if="taskWorkflowVm.selectedStatus.acknowledgers.length == 0" style="padding: 10px;">
                    <h5 translate>NO_ACKNOWLEDGERS</h5>
                </div>
                <div class="status-assignment" ng-repeat="assignment in taskWorkflowVm.selectedStatus.acknowledgers">
                    <div class="row" style="margin: 0">
                        <div class="pull-right">
                            <i class="fa fa-times-circle" title="{{taskWorkflowVm.removePerson}}"
                               ng-click="taskWorkflowVm.deletePerson(assignment)"
                               style="font-size:18px;cursor:pointer;"></i>
                        </div>
                        <div class="pull-right" ng-if="taskWorkflowVm.workflow.started">
                            <div ng-if="assignment.acknowledged == false">
                                <a href="" class="btn btn-primary btn-xs" ng-if="hasPermission('permission.changes.edit')"
                                   ng-click="assignment.acknowledged = true">Acknowledge</a>
                            </div>
                            <div ng-if="assignment.acknowledged == true">
                                <span class="text-success" style="font-weight: bolder">
                                    <i class="fa fa-check mr5"></i>ACKNOWLEDGED
                                </span>
                            </div>
                        </div>
                        <div>
                            <h4>{{assignment.personObject.firstName}}, {{assignment.personObject.lastName}}</h4>
                        </div>
                    </div>

                    <div style="margin-top: 10px;">
                        <div class="text-muted" translate>NOTES</div>
                        <p ng-if="assignment.comments != null && !assignment.editMode">{{assignment.comments}}</p>

                        <p ng-if="assignment.comments == null && !assignment.editMode" style="font-style: italic" translate>
                            NO_COMMENTS</p>

                        <div ng-if="assignment.editMode">
                            <textarea class="form-control" rows="5" ng-model="assignment.comments"
                                      style="resize: none"></textarea>

                            <div style="margin-top: 5px; text-align: right">
                                <button class='btn btn-xs btn-default'
                                        ng-click='assignment.editMode = false;assignment.comments = null' translate>CANCEL
                                </button>
                                <button class='btn btn-xs btn-success' ng-click='taskWorkflowVm.saveAssignment(assignment)'
                                        translate>SAVE
                                </button>
                            </div>
                        </div>
                        <div ng-if="assignment.comments == null && !assignment.editMode">
                            <a href="" ng-click="assignment.editMode = true"
                               ng-if="hasPermission('permission.changes.edit')"><span translate>ADD_COMMENT</span></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="status-panel">
            <div class="status-panel-heading" style="background-color: #F0AD4E;border-color: #faebcc;">
                <div class="status-panel-btns">
                    <a href=""
                       ng-if="taskWorkflowVm.selectedStatus.flag == 'UNASSIGNED' && hasPermission('permission.changes.edit')"
                       ng-click="taskWorkflowVm.showPersons('observers')"><i class="fa fa-plus"></i></a>
                </div>
                <!-- panel-btns -->
                <h3 class="status-panel-title" translate>OBSERVERS</h3>
            </div>
            <div class="status-panel-body">
                <div ng-if="taskWorkflowVm.selectedStatus.observers.length == 0" style="padding: 10px;">
                    <h5 translate>NO_OBSERVERS</h5>
                </div>
                <div class="status-assignment" ng-repeat="assignment in taskWorkflowVm.selectedStatus.observers">
                    <div class="row" style="margin: 0">
                        <div class="pull-right">
                            <i class="fa fa-times-circle" title="{{taskWorkflowVm.removePerson}}"
                               ng-click="taskWorkflowVm.deletePerson(assignment)"
                               style="font-size:18px;cursor:pointer;"></i>
                        </div>
                        <div>
                            <h4>{{assignment.personObject.firstName}}, {{assignment.personObject.lastName}}</h4>
                        </div>
                    </div>
                    <div style="margin-top: 10px;">
                        <div class="text-muted" translate>NOTES</div>
                        <p ng-if="assignment.comments != null && !assignment.editMode">{{assignment.comments}}</p>

                        <p ng-if="assignment.comments == null && !assignment.editMode" style="font-style: italic" translate>
                            NO_COMMENTS
                        </p>

                        <div ng-if="assignment.editMode">
                            <textarea class="form-control" rows="5" ng-model="assignment.comments"
                                      style="resize: none"></textarea>

                            <div style="margin-top: 5px; text-align: right">
                                <button class='btn btn-xs btn-default'
                                        ng-click='assignment.editMode = false;assignment.comments = null' translate>CANCEL
                                </button>
                                <button class='btn btn-xs btn-success' ng-click='taskWorkflowVm.saveAssignment(assignment)'
                                        translate>SAVE
                                </button>
                            </div>
                        </div>
                        <div ng-if="assignment.comments == null && !assignment.editMode">
                            <a href="" ng-click="assignment.editMode = true"
                               ng-if="hasPermission('permission.changes.edit')"><span translate>ADD_COMMENT</span></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>--%>
</div>


<div ng-if="taskWorkflowVm.selectedStatus == null"
     style="background-color: #fff;/*border: 1px solid #eee;*/">
    <div>
        <style scoped>
            .timeline {
                width: 250px;
            }

            .timeline::before {
                left: -80px;
            }

            .timeline .flag {
                cursor: pointer;
            }

            .timeline .time-wrapper {
                margin-top: 1px;
            }

            .timeline .time {
                font-size: 12px;
                padding: 6px;
            }
        </style>
        <div class="row">
            <div class="item-details-tabs">
                <uib-tabset active="taskWorkflowVm.selectedTabIndex">
                    <uib-tab heading="{{taskWorkflowVm.tabs.history.heading}}"
                             select="taskWorkflowVm.tabActivated(taskWorkflowVm.tabs.history.id)">
                        <div ng-include="taskWorkflowVm.tabs.history.template"
                             ng-controller="WorkflowHistoryController as workflowHistoryVm"></div>
                    </uib-tab>
                    <%--  <uib-tab heading="{{taskWorkflowVm.tabs.attributes.heading}}"
                               select="taskWorkflowVm.tabActivated(taskWorkflowVm.tabs.attributes.id)">
                          <div ng-include="taskWorkflowVm.tabs.attributes.template"
                               ng-controller="WorkflowAttributesController as workflowAttributesVm"></div>
                      </uib-tab>--%>
                </uib-tabset>
            </div>
        </div>
    </div>
    <%--<h4 style="margin: 0">Workflow History</h4>

    <div>
        <style scoped>
            .timeline {
                width: 250px;
            }
            .timeline::before {
                left: -80px;
            }
            .timeline .flag {
                cursor: pointer;
            }
            .timeline .time-wrapper {
                margin-top: 1px;
            }
            .timeline .time {
                font-size: 12px;
                padding: 6px;
            }
        </style>

        <div ng-if="taskWorkflowVm.workflowHistory.length == 0" style="padding: 20px;font-style: italic">
            No history
        </div>

        <ul class="timeline" style="margin-left: 100px;" ng-if="taskWorkflowVm.workflowHistory.length > 0">
            <li ng-repeat="history in taskWorkflowVm.workflowHistory">
                <div class="direction-r">
                    <div class="flag-wrapper">
                        <span class="flag" style="">{{history.statusObject.name}}</span>
                        <span class="time-wrapper">
                    <span class="time">{{history.timestamp}}</span>
                </span>
                    </div>
                    <div class="desc"></div>
                </div>
            </li>
        </ul>
    </div>--%>
</div>