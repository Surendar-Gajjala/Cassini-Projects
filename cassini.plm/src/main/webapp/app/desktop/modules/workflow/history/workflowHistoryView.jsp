<div style="height: 100%; overflow-y: auto;">
    <style scoped>
        ul.timeline {
            width: 100%;
            margin-left: -30px !important;
            margin-top: 0;
        }

        ul.timeline:before {
            left: 68px;
            width: 2px;
            background: #ddd;
        }

        ul.timeline .direction-r .flag {
            background: rgb(231, 228, 228);
        }

        ul.timeline .direction-r .flag:before {
            width: 15px;
            height: 15px;
            margin-top: -10px;
            background: #2a6fa8;
            border-radius: 10px;
            border: 2px solid #2a6fa8;
            left: -41px;
            top: 18px;
        }

        ul.timeline .direction-r .flag:after {
            border-right-color: rgb(231, 228, 228);
        }

        .wofklow-assignments {
            font-size: 14px !important;
            font-style: normal !important;
            padding-top: 10px;
        }
    </style>
    <ul class="timeline" style="margin-left: 100px;">
        <div ng-if="wfHistoryVm.workflowHistory.length == 0" style="padding: 0px;margin-left: 100px;font-style: italic">
            <span translate>NO_HISTORY</span>
        </div>

        <li ng-repeat="history in wfHistoryVm.workflowHistory">
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
            </div>
        </li>
    </ul>
</div>