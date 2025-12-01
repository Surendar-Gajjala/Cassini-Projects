<style scoped>

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
        /*background-color: #fff;*/
    }

    .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
        background-color: #d6e1e0;
    }

    #bop-plan .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
        position: absolute !important;
        display: contents !important;
    }

    .ml-12 {
        margin-left: 12px;
    }

    .ml-3 {
        margin-left: 3px;
    }

    .validate-plan-resources-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        padding-top: 20px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .validate-plan-resources-model .validate-plan-resources-content {
        margin: auto;
        display: block;
        height: 94%;
        width: 80%;
        background-color: white;
        border-radius: 7px !important;
    }

    .validate-plan-resources-header {
        padding: 5px;
        text-align: center;
        border-bottom: 1px solid lightgrey;
        height: 50px;
    }

    .validate-plan-resources-footer {
        border-bottom: 1px solid lightgrey;
        padding: 5px;
        text-align: center;
        height: 50px;
        width: 100%;
        display: flex;
    }

    .plan-header {
        font-weight: bold;
        font-size: 22px;
        /*position: absolute;*/
        display: inline-block;
        /*left: 44%;*/
        margin-top: 7px;
    }

    .plan-content {
        padding: 10px;
        overflow: auto;
        min-width: 100%;
        width: auto;
    }

    .plan-close {
        position: relative;
        width: 38px;
        height: 38px;
        opacity: 0.3;
    }

    .plan-close:hover {
        opacity: 0.6;
        border-radius: 50%;
        background-color: #ddd;
    }

    .plan-close:before, .plan-close:after {
        position: absolute;
        top: 7px;
        left: 18px;
        content: ' ';
        height: 22px;
        width: 2px;
        background-color: #333;
    }

    .plan-close:before {
        transform: rotate(45deg) !important;
    }

    .plan-close:after {
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

    .plan-close-btn {
        position: absolute;
        right: 40px;
        top: 7px;
        width: 32px;
        height: 32px;
        opacity: 0.3;
    }

    .plan-close-btn:hover {
        opacity: 0.6;
        border-radius: 50%;
        background-color: #ddd;
    }

    .plan-close-btn:before, .plan-close-btn:after {
        position: absolute;
        top: 9px;
        left: 15px;
        content: ' ';
        height: 15px;
        width: 2px;
        background-color: #333;
    }

    .plan-close-btn:before {
        transform: rotate(45deg) !important;
    }

    .plan-close-btn:after {
        transform: rotate(-45deg) !important;
    }

    .operation-bold {
        font-weight: 600;
    }

    .operation-no-resources {
        color: red;
    }
</style>
<div id="bop-plan" class="responsive-table">
    <table class='table table-striped highlight-row' style="margin: 0;">
        <thead>
        <tr>
            <th class="col-width-150" translate>SEQUENCE_NUMBER</th>
            <th class="col-width-100" translate>NUMBER</th>
            <th class="col-width-150" translate>TYPE</th>
            <th class="col-width-200" translate>NAME</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th class="col-width-100" translate>SETUP_TIME</th>
            <th class="col-width-100" translate>CYCLE_TIME</th>
            <%--<th class="actions-col sticky-col sticky-actions-col">
                <span translate>ACTIONS</span>
            </th>--%>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="mbomInstanceOperationsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_OPERATIONS</span>
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationsVm.loading == false && mbomInstanceOperationsVm.mbomInstanceOperations.length == 0">
            <td colspan="12">
                {{ 'NO_OPERATIONS' | translate}}
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationsVm.mbomInstanceOperations.length > 0"
            ng-repeat="plan in mbomInstanceOperationsVm.mbomInstanceOperations">
            <td class="col-width-150">
                <span class="level{{plan.level}}" ng-if="!plan.editMode">
                    <i ng-if="plan.count > 0 && plan.editMode == false" class="fa"
                       style="cursor: pointer;margin-right: 0;"
                       ng-class="{'fa-caret-right': (plan.expanded == false || plan.expanded == null || plan.expanded == undefined),
                                  'fa-caret-down': plan.expanded == true}"
                       ng-click="mbomInstanceOperationsVm.togglePlanNode(plan)"></i>
                    <a href="" ng-class="{'ml-12': plan.count == 0,'ml-3':plan.count > 0 && !plan.expanded}"
                       ng-click="mbomInstanceOperationsVm.showMBOMInstanceOperationDetails(plan)" title="Click to show details"
                       ng-if="plan.type == 'OPERATION'" style="margin-right: 10px;">{{plan.sequenceNumber}}</a>
                    <span ng-class="{'ml-12': plan.count == 0,'ml-3':plan.count > 0 && !plan.expanded}"
                          ng-if="plan.type == 'PHANTOM'">{{plan.sequenceNumber}}</span>
                    <span class="label label-info label-count" title="Resources"
                          ng-if="plan.type == 'OPERATION' && plan.isNew == false && plan.editMode == false"
                          style="font-size: 12px;padding: 1px 4px !important;"
                          ng-click="mbomInstanceOperationsVm.showBopPlanResourcesDetails(plan)"
                          ng-bind-html="plan.resourceCount">
                    </span>
                    <span class="label label-primary label-count" title="Parts"
                          ng-if="plan.type == 'OPERATION' && plan.isNew == false && plan.editMode == false"
                          style="font-size: 12px;padding: 1px 4px !important;"
                          ng-click="mbomInstanceOperationsVm.showBopPlanPartsDetails(plan)"
                          ng-bind-html="plan.partCount">
                    </span>
                </span>
            </td>
            <td class="col-width-100">
                <a ng-if="plan.type == 'OPERATION'" href=""
                   ng-click="mbomInstanceOperationsVm.showOperationDetails(operation)">{{plan.number}}</a>
                <span ng-if="plan.type == 'PHANTOM'">{{plan.number}}</span>
            </td>
            <td class="col-width-150">
                <span>{{plan.typeName}}</span>
            </td>
            <td class="col-width-200">
                <span ng-if="plan.editMode == false">{{plan.name}}</span>
            </td>

            <td class="col-width-250">
                <span ng-if="plan.editMode == false">{{plan.description}}</span>
            </td>
            <td class="col-width-100">
                <span ng-if="plan.editMode == false">{{plan.setupTime}}</span>
            </td>
            <td class="col-width-100">
                <span ng-if="plan.editMode == false">{{plan.cycleTime}}</span>
            </td>

            <%--<td class="text-center actions-col sticky-col sticky-actions-col">
                    <span class="btn-group" ng-if="plan.editMode == true" style="margin: -1px">
                        <i title="{{ 'SAVE' | translate }}" ng-if="plan.isNew"
                           ng-click="mbomInstanceOperationsVm.savePlan(plan)" class="la la-check">
                        </i>
                        <i title="{{ 'SAVE' | translate }}" ng-if="!plan.isNew"
                           ng-click="mbomInstanceOperationsVm.updatePlan(plan)" class="la la-check">
                        </i>
                        <i title="{{ 'REMOVE' | translate }}"
                           ng-click="mbomInstanceOperationsVm.cancelChanges(plan)" class="la la-times">
                        </i>
                    </span>
                    <span ng-if="plan.editMode == false && !bopRevision.released"
                          class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="mbomInstanceOperationsVm.editPlan(plan)">
                            <a href="" translate>EDIT</a>
                        </li>
                        <li ng-click="mbomInstanceOperationsVm.removePlan(plan)">
                            <a href="" translate>REMOVE</a>
                        </li>
                    </ul>
                </span>
            </td>--%>
        </tr>
        </tbody>
    </table>
</div>