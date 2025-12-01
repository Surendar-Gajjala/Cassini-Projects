<div>
    <style scoped>
        .workflow-assignments {
            font-size: 14px !important;
            font-style: normal !important;
            padding-top: 10px;
            width: 140% !important;
        }
    </style>
    <div>
        <h5 style="margin-left: 12px; font-style: italic">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.number}})
        </h5>
    </div>
    <div ng-if="workflowHistoryVm.workflowHistory.length == 0" style="padding: 20px;font-style: italic">
        <span translate>NO_HISTORY</span>
    </div>
    <br>
    <ul class="timeline" style="margin-left: -220px;" ng-if="workflowHistoryVm.workflowHistory.length > 0">
        <li ng-repeat="history in workflowHistoryVm.workflowHistory">
            <div class="direction-r">
                <div style="position: relative;display: inline-block;text-align: left;">
                    <span class="flag" ng-if="!history.hold && !history.unhold && !history.demoted">{{history.statusObject.name}}</span>
                    <span class="flag" ng-if="history.hold">{{history.statusObject.name}}-HOLD</span>
                    <span class="flag" ng-if="history.unhold">{{history.statusObject.name}}-REMOVE HOLD</span>
                    <span class="flag" ng-if="history.demoted">{{history.statusObject.name}}-DEMOTED</span>
                    <span class="time-wrapper"></span>
                </div>
                <div class="desc">
                    <span style="font-size: 16px;font-style: normal;">
                        <span style="font-size: 14px;">{{history.timestamp}}</span>
                        <span style="font-style: italic;font-size: 14px;">
                            ( {{history.statusObject.createdByObject.firstName}} )
                        </span>
                    </span>

                    <div class="workflow-assignments" ng-if="history.assignmentsList.length > 0">
                        <table class="table table-striped table-compact table-bordered">
                            <thead>
                            <tr>
                                <th>Person</th>
                                <th>Vote</th>
                                <th>TimeStamp</th>
                                <th>Comments</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="workflowAssignment in history.assignmentsList">
                                <td>{{workflowAssignment.personObject.fullName}}</td>
                                <td>
                                    <span class="label label-success"
                                          ng-if="workflowAssignment.assignmentType === 'APPROVER' && workflowAssignment.vote === 'APPROVE'">
                                            {{workflowAssignment.vote}}
                                    </span>
                                    <span class="label label-danger"
                                          ng-if="workflowAssignment.assignmentType === 'APPROVER' && workflowAssignment.vote === 'REJECT'">
                                            {{workflowAssignment.vote}}
                                    </span>
                                     <span class="label label-success"
                                           ng-if="workflowAssignment.assignmentType === 'ACKNOWLEDGER' && workflowAssignment.acknowledged === true">
                                            ACKNOWLEDGED
                                        </span>
                                        <span class="label label-success"
                                              ng-if="workflowAssignment.assignmentType === 'OBSERVER'">
                                            OBSERVER
                                        </span>
                                </td>
                                <td>{{workflowAssignment.timeStamp}}</td>
                                <td>{{workflowAssignment.comments}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </li>
    </ul>
</div>