<div>
    <style scoped>
        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }

        a.disabled {
            cursor: not-allowed !important;
            color: lightgrey;
        }

        .planDateContainer {
            margin-bottom: 0 !important;
        }

        .planDateContainer [type=text] {
            display: block;
            filter: alpha(opacity=0);
            opacity: 0;
            position: absolute;
            margin-top: -20px;
            margin-left: -25px;
            width: 65px;
            cursor: pointer;
        }

        #audit-plan > table > tbody > tr > td {
            border-bottom: 1px solid lightgray;
        }

    </style>
    <div id="audit-plan" class='responsive-table'>
        <table class='table'>
            <thead>
            <tr>
                <th style="width: 30px;"
                    ng-if="supplierAudit.status.phaseType != 'RELEASED' && !loginPersonDetails.external">
                    <i class="la la-plus" title="{{'ADD_SUPPLIER' | translate}}" style="cursor: pointer"
                       ng-click="supplierAuditPlanVm.addSuppliers()"></i>
                </th>
                <th class="col-width-100" translate>NUMBER</th>
                <th class="col-width-150" translate>NAME</th>
                <th class="col-width-150" translate>CITY</th>
                <th colspan="2" class="col-width-100" translate>STATUS</th>
                <th ng-repeat="month in supplierAuditPlanVm.months" style="text-align: center;">{{month.label}}</th>
                <th class="col-width-100"
                    ng-if="supplierAudit.status.phaseType != 'RELEASED' && !loginPersonDetails.external"
                    style="text-align: center;" translate>ACTIONS
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="supplierAuditPlanVm.loading == true">
                <td colspan="25"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_PLAN</span>
                </td>
            </tr>
            <tr ng-if="supplierAuditPlanVm.loading == false && supplierAuditPlanVm.auditPlans.length == 0">
                <td colspan="25"><span translate>NO_PLAN</span></td>
            </tr>
            <tr ng-repeat-start="plan in supplierAuditPlanVm.auditPlans">
                <td rowspan="2"
                    ng-if="supplierAudit.status.phaseType != 'RELEASED' && !loginPersonDetails.external"></td>
                <td rowspan="2" style="vertical-align: middle !important;">
                    <a href="" ng-click="supplierAuditPlanVm.showSupplierDetails(plan)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{plan.number}}</a>
                </td>
                <td rowspan="2" style="vertical-align: middle !important;">
                    <a href="" ng-click="supplierAuditPlanVm.showSupplierPlanDetails(plan)"
                       title="{{showPlanDetailsTitle}}">{{plan.name}}</a>
                       <span class='label label-default label-count' style='margin-left: 5px;margin-top: -2px;font-size: 12px;background-color: #e4dddd;padding: 1px 4px !important;'>{{plan.reviewerCount}}</span>   
                </td>
                <td rowspan="2" style="vertical-align: middle !important;">{{plan.city}}</td>
                <td rowspan="2" class="col-width-100"
                    style="vertical-align: middle !important;border-right: 1px solid lightgrey;">
                    <audit-plan-status object="plan"></audit-plan-status>
                </td>
                <td style="color: blue;width: 20px;">P</td>
                <td ng-repeat="month in plan.months" style="text-align: center">
                    <label class="planDateContainer" ng-if="plan.editMode">
                    <span style="font-size: 16px;vertical-align: bottom;cursor: pointer !important;">
                        <span ng-if="month.plannedDate == null || month.plannedDate == ''"
                              title="Click to select dat">-</span>
                        <span ng-if="month.plannedDate != null && month.plannedDate != ''" style="font-size: 12px;"
                              title="Click to select date">{{month.plannedDate}}</span>
                        <span class="la la-times" ng-if="month.plannedDate != null && month.plannedDate != ''"
                              ng-click="month.plannedDate = null;plan.plannedStartDate = null"
                              title="Click to remove date"></span>
                    </span>
                        <input type="text" name="{{plan.id}}{{month.value}}" ng-model="month.plannedDate"
                               month="month" plan="plan" planned-year="supplierAudit.plannedYear"
                               update-planned-date="setPlannedDate" on-month-picker/>
                    </label>
                    <i class="la la-clock" title="{{month.plannedDate}}" style="color: blue;font-size: 18px;"
                       ng-if="!plan.editMode && plan.plannedStartDate != null && plan.plannedStartDate != '' && month.plannedDate != null"></i>

                </td>
                <td rowspan="2" class="text-center"
                    ng-if="supplierAudit.status.phaseType != 'RELEASED' && !loginPersonDetails.external"
                    style="width: 100px;;vertical-align: middle !important;border-left: 1px solid lightgrey;">
                    <span class="btn-group" ng-if="plan.editMode == true" style="margin: 0">
                        <i title="{{'SAVE' | translate}}" style="cursor: pointer"
                           ng-click="supplierAuditPlanVm.savePlan(plan)"
                           class="la la-check">
                        </i>
                        <i title="{{'CANCEL' | translate}}" style="cursor: pointer"
                           ng-click="supplierAuditPlanVm.cancelChanges(plan)"
                           class="la la-times">
                        </i>
                    </span>
                    <span class="row-menu" uib-dropdown
                          ng-if="plan.editMode == false && plan.status != 'APPROVED'" style="min-width: 50px">
                        <span dropdown-append-to-body></span>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                            style="z-index: 9999 !important;">
                            <li style="cursor: pointer" ng-click="supplierAuditPlanVm.editPlan(plan)"
                                ng-class="{'disabled':plan.approvedCount > 0}">
                                <a translate>EDIT</a>
                            </li>
                            <li ng-click="supplierAuditPlanVm.deletePlan(plan)" style="cursor: pointer"
                                ng-class="{'disabled':plan.status == 'COMPLETED' || plan.status == 'APPROVED'}">
                                <a translate>REMOVE</a>
                            </li>
                        </ul>
                </span>
                </td>
            </tr>
            <tr ng-repeat-end>
                <td style="color: darkgreen;width: 20px;">C</td>
                <td ng-repeat="month in plan.months" style="text-align: center">
                    <label class="planDateContainer" ng-if="plan.editMode">
                    <span style="font-size: 16px;vertical-align: bottom;cursor: pointer !important;">
                        <span ng-if="month.completedDate == null || month.completedDate == ''"
                              title="Click to select date">-</span>
                        <span ng-if="month.completedDate != null && month.completedDate != ''" style="font-size: 12px;"
                              title="Click to select date">{{month.completedDate}}</span>
                        <span class="la la-times" ng-if="month.completedDate != null && month.completedDate != ''"
                              ng-click="month.completedDate = null;plan.finishedDate = null"
                              title="Click to remove date"></span>
                    </span>
                        <input type="text" name="{{plan.id}}{{month.value}}" ng-model="month.completedDate"
                               month="month" plan="plan" planned-year="supplierAudit.plannedYear"
                               update-completed-date="setCompletedDate" on-month-picker/>
                    </label>
                    <i class="la la-check-circle" title="{{month.completedDate}}"
                       style="color: darkgreen;font-size: 18px;"
                       ng-if="!plan.editMode && plan.finishedDate != null && plan.finishedDate != '' && month.completedDate != null"></i>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>