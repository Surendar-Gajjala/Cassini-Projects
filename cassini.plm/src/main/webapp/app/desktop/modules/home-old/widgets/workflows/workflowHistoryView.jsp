<div>
    <div ng-show="workflowHistoryVm.object == null">
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.item != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.item.itemNumber}} - Revision : {{workflowHistoryVm.workflow.item.rev.revision}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.eco != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.eco.ecoNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.project != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.project.name}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.activity != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.activity.name}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.manufacturer != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.manufacturer.name}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.manufacturerPart != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.manufacturerPart.partNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.specification != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.specification.objectNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.workflow.requirement != null">
            {{workflowHistoryVm.workflow.name}}({{workflowHistoryVm.workflow.requirement.objectNumber}})
        </h5>
    </div>
    <div ng-show="workflowHistoryVm.object != null">
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.item != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.item.itemNumber}} - Revision : {{workflowHistoryVm.object.revision}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.eco != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.eco.ecoNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.dco != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.dco.dcoNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.dcr != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.dcr.crNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.ecr != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.ecr.crNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.mco != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.mco.mcoNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.variance != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.variance.varianceNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.project != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.project.name}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.activity != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.activity.name}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.manufacturer != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.manufacturer.name}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.manufacturerPart != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.manufacturerPart.partNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.specification != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.specification.objectNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.requirement != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.requirement.objectNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.inspectionPlan != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.inspectionPlan.number}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.inspection != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.inspection.inspectionNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.problemReport != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.problemReport.prNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.ncr != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.ncr.ncrNumber}})
        </h5>
        <h5 style="margin-left: 12px; font-style: italic" ng-if="workflowHistoryVm.object.qcr != null">
            {{workflowHistoryVm.object.workflow.name}}({{workflowHistoryVm.object.qcr.qcrNumber}})
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
                </div>
                <div class="desc">
                    <div ng-show="history.statusApprovers.length >0 || history.statusAcknowledgers.length >0" style="font-size: 14px;margin-top: 10px;background: lavender;padding: 10px;border:1px solid lightgrey;
                             box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.4);">
                        <span ng-if="history.statusApprovers.length >0" class="badge badge-primary" style="font-size: 14px;">Approvers</span><br>

                        <div ng-repeat="approver in history.statusApprovers"
                             style="padding: 5px 0">
                            {{$index + 1}}. {{approver.personObject.fullName}} :
                            <span class="badge badge-success" ng-if="approver.vote == 'APPROVE'">Approved</span>
                            <span class="badge badge-danger" ng-if="approver.vote == 'REJECT'">Rejected</span>
                            <br><br>
                            <span style="font-size: 14px;" ng-if="approver.timeStamp != null">({{approver.timeStamp}})</span>
                            <br><br>
                            <span style="font-size: 12px">Comments : {{approver.comments}}</span>
                        </div>
                        <hr style="margin: 5px;">
                        <span ng-if="history.statusAcknowledgers.length >0" class="badge badge-warning" style="font-size: 14px;">Acknowledgers</span><br>

                        <div ng-repeat="acknowledger in history.statusAcknowledgers"
                             style="padding: 5px 0">
                            {{$index + 1}}. {{acknowledger.personObject.fullName}} :
                            <span class="badge badge-success"
                                  ng-if="acknowledger.acknowledged == true">Acknowledged</span>
                            <br><br>
                            <span style="font-size: 14px;" ng-if="acknowledger.timeStamp != null">({{acknowledger.timeStamp}})</span>
                            <br><br>
                            <span style="font-size: 12px">Comments : {{acknowledger.comments}}</span>
                        </div>
                    </div>
                </div>
            </div>
        </li>
    </ul>
</div>