<div style="padding: 5px;">
    <style scoped>
        .status-panel {
            border: 1px solid #d7d7d7;
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
            font-size: 16px !important;
        }
    </style>
    <div class="status-panel">
        <div class="status-panel-heading" style="background-color: #428bca;border-color: #428bca;">
            <div class="status-panel-btns">
                <a href=""
                   ng-if="selectedStatus.flag != 'COMPLETED' && permission
                       && (personDetails.person.id == object.createdBy || checkForQualityUser())"
                   ng-hide="workflow.cancelled == true || workflow.finished == true"
                   ng-click="showPersons('approvers')"><i class="la la-plus"></i></a>
            </div>
            <!-- panel-btns -->
            <h3 class="status-panel-title" translate>APPROVERS</h3>
        </div>
        <div class="status-panel-body">
            <div ng-if="selectedStatus.approvers.length == 0" style="padding: 10px;">
                <h5 translate>NO_APPROVERS</h5>
            </div>
            <div class="status-assignment" ng-repeat="assignment in selectedStatus.approvers">
                <div class="row" style="margin: 0">
                    <div class="pull-right" <%--ng-if="assignment.vote == null"--%>
                         ng-if="selectedStatus.flag != 'COMPLETED' && (personDetails.person.id == object.createdBy || checkForQualityUser())">
                        <i class="fa fa-times-circle" title="{{removePerson}}"
                           ng-click="deleteWorkflowAssignment(assignment)"
                           ng-if="assignment.vote == null && !assignment.editMode"
                           style="font-size:18px;cursor:pointer;margin-left: 3px;margin-top: 4px;"></i>
                    </div>
                    <div class="pull-right">
                        <div ng-hide="approveState != null"
                             ng-if="workflow.currentStatusObject == selectedStatus && assignment.vote == null && personDetails.person.id == assignment.person">
                            <a href="" class="btn btn-success btn-xs"
                            <%--ng-if="permission"--%>
                               ng-click='saveStatusAssignment(assignment,"Approve")'>Approve</a>
                            <a href="" class="btn btn-danger btn-xs"
                            <%--ng-if="permission"--%>
                               ng-click='saveStatusAssignment(assignment,"Reject")'>Reject</a>
                        </div>
                        <div ng-if="assignment.vote != null">
                            <span class="text-success" ng-if="assignment.vote == 'APPROVE'" style="font-weight: bolder">
                                <i class="fa fa-check mr5"></i>APPROVE
                            </span>
                            <span class="text-danger" ng-if="assignment.vote == 'REJECT'" style="font-weight: bolder">
                                <i class="fa fa-close mr5"></i>REJECT
                                <i class="text-muted fa fa-edit ml5" style="cursor:pointer;" title="Change vote"
                                   ng-if="personDetails.person.id == assignment.person && selectedStatus.flag != 'COMPLETED'"
                                   ng-click="approveState = null;assignment.vote = null;assignment.comments = null;"></i>
                            </span>
                        </div>
                    </div>
                    <div>
                        <h4>{{assignment.personName}}</h4>
                    </div>
                </div>
                <div style="margin-top: 10px;">
                    <div class="text-muted" translate>NOTES</div>
                    <p ng-if="assignment.comments != null && !assignment.editMode">{{assignment.comments}}</p>

                    <p ng-if="assignment.comments == null && !assignment.editMode" style="font-style: italic"
                       translate>
                        NO_COMMENTS</p>

                    <div ng-if="assignment.editMode">
                        <textarea class="form-control" rows="3" ng-model="assignment.comments"
                                  style="resize: none"></textarea>

                        <div style="margin-top: 5px; text-align: right">
                            <button class='btn btn-xs btn-success' ng-if="approveState == 'Approve'"
                                    ng-click='saveAssignment(assignment,"Approve")'
                                    translate>
                                SUBMIT
                            </button>
                            <button class='btn btn-xs btn-success' ng-if="approveState == 'Reject'"
                                    ng-click='saveAssignment(assignment,"Reject")'
                                    translate>
                                SUBMIT
                            </button>
                            <button class='btn btn-xs btn-default'
                                    ng-if="approveState == 'Reject' || approveState == 'Approve'"
                                    ng-click='cancelAssignment(assignment)'
                                    translate>CANCEL
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="status-panel">
        <div class="status-panel-heading" style="background-color: #5BC0DE;border-color: #bce8f1;">
            <div class="status-panel-btns">
                <a href=""
                   ng-if="selectedStatus.flag != 'COMPLETED' && permission
                       && (personDetails.person.id == object.createdBy || checkForQualityUser())"
                   ng-hide="workflow.cancelled == true || workflow.finished == true"
                   ng-click="showPersons('acknowledgers')"><i class="la la-plus"></i></a>
            </div>
            <!-- panel-btns -->
            <h3 class="status-panel-title" translate>ACKNOWLEDGERS</h3>
        </div>
        <div class="status-panel-body">
            <div ng-if="selectedStatus.acknowledgers.length == 0" style="padding: 10px;">
                <h5 translate>NO_ACKNOWLEDGERS</h5>
            </div>
            <div class="status-assignment" ng-repeat="assignment in selectedStatus.acknowledgers">
                <div class="row" style="margin: 0">
                    <div class="pull-right"
                         ng-if="selectedStatus.flag != 'COMPLETED' && (personDetails.person.id == object.createdBy || checkForQualityUser())">
                        <i class="fa fa-times-circle" title="{{removePerson}}"
                           ng-click="deleteWorkflowAssignment(assignment)"
                           ng-if="!assignment.acknowledged  && !assignment.editMode"
                           style="font-size:18px;cursor:pointer;margin-left: 3px;margin-top: 4px;"></i>
                    </div>
                    <div class="pull-right">
                        <div ng-if="workflow.currentStatusObject == selectedStatus && assignment.acknowledged == false && personDetails.person.id == assignment.person">
                            <a href="" class="btn btn-primary btn-xs"
                            <%--ng-if="permission"--%>
                               ng-click='saveAssignment(assignment,"Acknowledge")'>Acknowledge</a>
                        </div>
                        <div ng-if="assignment.acknowledged == true">
                            <span class="text-success" style="font-weight: bolder">
                                <i class="fa fa-check mr5"></i>ACKNOWLEDGED
                            </span>
                        </div>
                    </div>
                    <div>
                        <h4>{{assignment.personName}}</h4>
                    </div>
                </div>

                <div style="margin-top: 10px;">
                    <div class="text-muted" translate>NOTES</div>
                    <p ng-if="assignment.comments != null && !assignment.editMode">{{assignment.comments}}</p>

                    <p ng-if="assignment.comments == null && !assignment.editMode" style="font-style: italic"
                       translate>
                        NO_COMMENTS</p>

                    <div ng-if="assignment.editMode">
                        <textarea class="form-control" rows="3" ng-model="assignment.comments"
                                  style="resize: none"></textarea>
                    </div>
                    <div ng-if="assignment.comments == null && !assignment.editMode && personDetails.person.id == assignment.person">
                        <a href="" ng-click="assignment.editMode = true"
                        <%--ng-if="permission"--%>
                           ng-hide="assignment.acknowledged == true"><span translate>ADD_COMMENT</span></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="status-panel">
        <div class="status-panel-heading" style="background-color: #F0AD4E;border-color: #faebcc;">
            <div class="status-panel-btns">
                <a href=""
                   ng-if="selectedStatus.flag != 'COMPLETED' && permission
                       && (personDetails.person.id == object.createdBy || checkForQualityUser())"
                   ng-hide="workflow.cancelled == true || workflow.finished == true"
                   ng-click="showPersons('observers')"><i class="la la-plus"></i></a>
            </div>
            <!-- panel-btns -->
            <h3 class="status-panel-title" translate>OBSERVERS</h3>
        </div>
        <div class="status-panel-body">
            <div ng-if="selectedStatus.observers.length == 0" style="padding: 10px;">
                <h5 translate>NO_OBSERVERS</h5>
            </div>
            <div class="status-assignment" ng-repeat="assignment in selectedStatus.observers">
                <div class="row" style="margin: 0">
                    <div class="pull-right"
                         ng-if="selectedStatus.flag != 'COMPLETED' && (personDetails.person.id == object.createdBy || checkForQualityUser())">
                        <i class="fa fa-times-circle" title="{{removePerson}}"
                           ng-click="deleteWorkflowAssignment(assignment)"
                           style="font-size:18px;cursor:pointer;margin-left: 3px;margin-top: 4px;"></i>
                    </div>
                    <div>
                        <h4>{{assignment.personName}}</h4>
                    </div>
                </div>
                <div style="margin-top: 10px;">
                    <div class="text-muted" translate>NOTES</div>
                    <p ng-if="assignment.comments != null && !assignment.editMode">{{assignment.comments}}</p>

                    <p ng-if="assignment.comments == null && !assignment.editMode" style="font-style: italic"
                       translate>
                        NO_COMMENTS
                    </p>

                    <div ng-if="assignment.editMode">
                        <textarea class="form-control" rows="3" ng-model="assignment.comments"
                                  style="resize: none"></textarea>

                        <div style="margin-top: 5px; text-align: right">
                            <button class='btn btn-xs btn-default'
                                    ng-click='assignment.editMode = false;assignment.comments = null' translate>
                                CANCEL
                            </button>
                            <button class='btn btn-xs btn-success'
                                    ng-click='saveAssignment(assignment,null)'
                                    translate>SAVE
                            </button>
                        </div>
                    </div>
                    <div ng-if="assignment.comments == null && !assignment.editMode && personDetails.person.id == assignment.person">
                        <a href="" ng-click="assignment.editMode = true"
                        <%--ng-if="permission"--%>><span
                                translate>ADD_COMMENT</span></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>