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

        .workflow-history-panel {
            position: absolute;
            left: 10px;
            right: 0;
            bottom: 0;
            overflow-y: auto;
            top: 0;
        }

        .wofklow-assignments {
            font-size: 14px !important;
            font-style: normal !important;
            padding-top: 10px;
            width: auto !important;
        }
    </style>
    <div class="row">
        <div style="padding-bottom: 50px;">
            <div ng-if="loadingHistory == true">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5">
                    <span translate>LOADING_HISTORY</span>
                </span>
            </div>
            <div ng-if="workflowHistory.length == 0 && loadingHistory == false"
                 style="padding: 20px;font-style: italic">
                <span translate>NO_HISTORY</span>
            </div>
            <ul class="timeline" style="margin-left: 100px;"
                ng-if="workflowHistory.length > 0 && loadingHistory == false">
                <li ng-repeat="history in workflowHistory">
                    <div class="direction-r">
                        <div style="position: relative;display: inline-block;text-align: left;">
                            <span class="flag" ng-if="!history.hold && !history.unhold && !history.demoted">{{history.statusObject.name}}</span>
                            <span class="flag" ng-if="history.hold">{{history.statusObject.name}}-HOLD</span>
                                    <span class="flag"
                                          ng-if="history.unhold">{{history.statusObject.name}}-REMOVE HOLD</span>
                                    <span class="flag"
                                          ng-if="history.demoted">{{history.statusObject.name}}-DEMOTED</span>
                            <span class="time-wrapper"></span>
                        </div>
                        <div class="desc">
                                    <span style="font-size: 16px;font-style: normal;">
                                        <span style="font-size: 14px;">{{history.timestamp}}</span>
                                        <span style="font-style: italic;font-size: 14px;">
                                            ( {{history.statusObject.createdByObject.fullName}} )
                                        </span>
                                    </span>
                        </div>
                        <div class="desc" ng-if="history.notes != null && (history.hold || history.unhold)">
                                    <span style="font-size: 16px;font-style: normal;">
                                        <span style="font-size: 14px;">({{history.notes}})</span>
                                    </span>
                        </div>
                        <div class="wofklow-assignments" ng-if="history.assignmentsList.length > 0">
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
                </li>
            </ul>
        </div>
    </div>
</div>