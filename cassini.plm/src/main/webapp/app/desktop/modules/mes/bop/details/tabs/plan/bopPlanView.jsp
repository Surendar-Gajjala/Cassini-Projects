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
            <th style="width: 20px;">
                <span uib-dropdown dropdown-append-to-body style="width: 100%;" ng-if="!bopRevision.released">
                    <i class="la la-plus dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                        <li>
                            <a href="" ng-click="bopPlanVm.addPhantom(null)"
                               translate>ADD_PHANTOM</a>
                        </li>
                        <li>
                            <a href="" ng-click="bopPlanVm.addOperation(null)">
                                <span translate>ADD_OPERATION</span></a>
                        </li>
                    </ul>
                </span>
            </th>
            <th class="col-width-150" translate>SEQUENCE_NUMBER</th>
            <th class="col-width-100" translate>NUMBER</th>
            <th class="col-width-150" translate>TYPE</th>
            <th class="col-width-200" translate>NAME</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th class="col-width-100" translate>SETUP_TIME</th>
            <th class="col-width-100" translate>CYCLE_TIME</th>
            <th class="actions-col sticky-col sticky-actions-col">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="bopPlanVm.saveAllPlans()"
                   ng-if="bopPlanVm.addedOperations.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-if="bopPlanVm.addedOperations.length > 1"
                   ng-click="bopPlanVm.removeAllPlans()" title="Remove"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="bopPlanVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_PLAN</span>
            </td>
        </tr>
        <tr ng-if="bopPlanVm.loading == false && bopPlanVm.bopPlans.length == 0">
            <td colspan="12">
                {{ 'NO_PLAN' | translate}}
            </td>
        </tr>
        <tr ng-if="bopPlanVm.bopPlans.length > 0" ng-repeat="plan in bopPlanVm.bopPlans">
            <td style="width:20px">
                <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                      ng-if="plan.type == 'PHANTOM' && !plan.editMode && !bopRevision.released">
                    <i class="la la-plus dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                        <li>
                            <a href="" ng-click="bopPlanVm.addPhantom(plan)"
                               translate>ADD_PHANTOM</a>
                        </li>
                        <li>
                            <a href="" ng-click="bopPlanVm.addOperation(plan)">
                                <span translate>ADD_OPERATION</span></a>
                        </li>
                    </ul>
                </span>
            </td>
            <td class="col-width-150">
                <span class="level{{plan.level}}" ng-if="!plan.editMode">
                    <i ng-if="plan.count > 0 && plan.editMode == false" class="fa"
                       style="cursor: pointer;margin-right: 0;"
                       ng-class="{'fa-caret-right': (plan.expanded == false || plan.expanded == null || plan.expanded == undefined),
                                  'fa-caret-down': plan.expanded == true}"
                       ng-click="bopPlanVm.togglePlanNode(plan)"></i>
                    <a href="" ng-class="{'ml-12': plan.count == 0,'ml-3':plan.count > 0 && !plan.expanded}"
                       ng-click="bopPlanVm.showBopPlanDetails(plan)" title="Click to show details"
                       ng-if="plan.type == 'OPERATION'" style="margin-right: 10px;">{{plan.sequenceNumber}}</a>
                    <span ng-class="{'ml-12': plan.count == 0,'ml-3':plan.count > 0 && !plan.expanded}"
                          ng-if="plan.type == 'PHANTOM'">{{plan.sequenceNumber}}</span>
                    <span class="label label-info label-count" title="Resources"
                          ng-if="plan.type == 'OPERATION' && plan.isNew == false && plan.editMode == false"
                          style="font-size: 12px;padding: 1px 4px !important;"
                          ng-click="bopPlanVm.showBopPlanResourcesDetails(plan)"
                          ng-bind-html="plan.resourceCount">
                    </span>
                    <span class="label label-primary label-count" title="Parts"
                          ng-if="plan.type == 'OPERATION' && plan.isNew == false && plan.editMode == false"
                          style="font-size: 12px;padding: 1px 4px !important;"
                          ng-click="bopPlanVm.showBopPlanPartsDetails(plan)"
                          ng-bind-html="plan.partCount">
                    </span>
                </span>
                <input type="text" class="form-control" style="width: 99%;" ng-if="plan.editMode"
                       placeholder="Enter sequence number" ng-model="plan.sequenceNumber"/>
            </td>
            <td class="col-width-100">
                <a ng-if="plan.type == 'OPERATION'" href="" ng-click="bopPlanVm.showOperationDetails(plan)">{{plan.number}}</a>
                <span ng-if="plan.type == 'PHANTOM'">{{plan.number}}</span>
            </td>
            <td class="col-width-150">
                <span>{{plan.typeName}}</span>
            </td>
            <td class="col-width-200">
                <span ng-if="plan.editMode == false && plan.type == 'PHANTOM'">{{plan.name}}</span>
                <span ng-if="plan.type == 'OPERATION'">{{plan.name}}</span>
                <input type="text" class="form-control" ng-if="plan.editMode && plan.type == 'PHANTOM'"
                       style="width: 99%;" placeholder="Enter name" ng-model="plan.name"
                       ng-enter="bopPlanVm.onOk(plan)"/>
            </td>

            <td class="col-width-250">
                <span ng-if="plan.editMode == false && plan.type == 'PHANTOM'">{{plan.description}}</span>
                <span ng-if="plan.type == 'OPERATION'">{{plan.description}}</span>
                <input type="text" class="form-control" ng-if="plan.editMode && plan.type == 'PHANTOM'"
                       style="width: 99%;" placeholder="Enter description" ng-model="plan.description"
                       ng-enter="bopPlanVm.onOk(plan)"/>
            </td>
            <td class="col-width-100">
                <span ng-if="plan.editMode == false">{{plan.setupTime}}</span>
                <input type="text" class="form-control" ng-if="plan.editMode && plan.type == 'OPERATION'"
                       style="width: 99%;" placeholder="Enter time" ng-model="plan.setupTime" numbers-only
                       ng-enter="bopPlanVm.onOk(plan)"/>
            </td>
            <td class="col-width-100">
                <span ng-if="plan.editMode == false">{{plan.cycleTime}}</span>
                <input type="text" class="form-control" ng-if="plan.editMode && plan.type == 'OPERATION'"
                       style="width: 99%;" placeholder="Enter time" ng-model="plan.cycleTime" numbers-only
                       ng-enter="bopPlanVm.onOk(plan)"/>
            </td>

            <td class="text-center actions-col sticky-col sticky-actions-col">
                    <span class="btn-group" ng-if="plan.editMode == true" style="margin: -1px">
                        <i title="{{ 'SAVE' | translate }}" ng-if="plan.isNew"
                           ng-click="bopPlanVm.savePlan(plan)" class="la la-check">
                        </i>
                        <i title="{{ 'SAVE' | translate }}" ng-if="!plan.isNew"
                           ng-click="bopPlanVm.updatePlan(plan)" class="la la-check">
                        </i>
                        <i title="{{ 'REMOVE' | translate }}"
                           ng-click="bopPlanVm.cancelChanges(plan)" class="la la-times">
                        </i>
                    </span>
                    <span ng-if="plan.editMode == false && !bopRevision.released"
                          class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="bopPlanVm.editPlan(plan)">
                            <a href="" translate>EDIT</a>
                        </li>
                        <li ng-click="bopPlanVm.removePlan(plan)">
                            <a href="" translate>REMOVE</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div id="validate-plan-resources" class="validate-plan-resources-model modal">
    <div class="validate-plan-resources-content">
        <div class="validate-plan-resources-header">
            <span class="plan-header">{{bop.number}} - Resources and Parts Validate</span>
            <a href="" ng-click="hideValidatePlanResources()" class="plan-close pull-right"
               style="display: inline-block"></a>
        </div>
        <div id="configuration-error" class="alert {{notificationBackground}} animated">
            <span style="margin-right: 10px;"><i class="fa {{notificationClass}}"></i></span>
            <a href="" class="plan-close-btn" ng-click="closeErrorNotification()"></a>
            {{message}}
        </div>
        <div class="plan-content" style="padding: 10px 0 10px 10px;overflow: auto;">
            <div style="width:200px;margin:auto;">
                <div class="switch-toggle switch-candy">
                    <input id="plan-resources" name="folderType" type="radio" checked
                           ng-click="bopPlanVm.loadResourcePartValidate('resources', $event)">
                    <label for="plan-resources" onclick="" translate>Resources</label>

                    <input id="plan-parts" name="folderType" type="radio"
                           ng-click="bopPlanVm.loadResourcePartValidate('parts', $event)">
                    <label for="plan-parts" onclick="" translate>Parts</label>
                    <a href=""></a>
                </div>
            </div>
            <div id="plan-resources-table" style="overflow:auto;">
                <table class='table table-striped highlight-row'
                       ng-if="bopPlanVm.selectedResourceValidate == 'resources'">
                    <thead>
                    <tr>
                        <th class="col-width-150" translate>SEQUENCE_NUMBER</th>
                        <th class="col-width-100" translate>NUMBER</th>
                        <th class="col-width-150" translate>TYPE</th>
                        <th class="col-width-200" translate>NAME</th>
                        <th class="col-width-250" translate>DESCRIPTION</th>
                        <th class="col-width-75" style="text-align: center" translate>TOTAL_QUANTITY</th>
                        <th class="col-width-75" style="text-align: center" style="" translate>CONSUMED</th>
                        <td style="width: 20px;"></td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="bopPlanVm.loadingValidate == true">
                        <td colspan="25"><img
                                src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5"><span translate>LOADING_RESOURCES</span>
                        </td>
                    </tr>
                    <tr ng-if="bopPlanVm.loadingValidate == false && bopPlanVm.bopPlanResources.length == 0">
                        <td colspan="25"><span translate>NO_RESOURCES</span></td>
                    </tr>
                    <tr ng-repeat="plan in bopPlanVm.bopPlanResources"
                        ng-class="{'operation-bold':plan.type == 'OPERATION' && plan.resources.length > 0}">
                        <td class="col-width-150">
                        <span class="level{{plan.level}}">
                            <i ng-if="plan.count > 0" class="fa"
                               style="cursor: pointer;margin-right: 0;"
                               ng-class="{'fa-caret-right': (plan.expanded == false || plan.expanded == null || plan.expanded == undefined),
                                  'fa-caret-down': plan.expanded == true}"
                               ng-click="bopPlanVm.togglePlanValidateNode(plan)"></i>
                            <span ng-class="{'ml-12': plan.count == 0,'ml-3':plan.count > 0 && !plan.expanded}">{{plan.sequenceNumber}}</span>
                        </span>
                        </td>
                        <td class="col-width-100">
                        <span ng-class="{'operation-no-resources':plan.type == 'OPERATION' && plan.resources.length == 0}"
                              title="{{plan.type == 'OPERATION' && plan.resources.length == 0 ? 'No resources found':''}}">{{plan.number}}</span>
                        </td>
                        <td class="col-width-150">
                            <span ng-if="plan.type == 'OPERATION' || plan.type == 'PHANTOM'">{{plan.typeName}}</span>
                            <span ng-if="plan.type == undefined">{{plan.resource}}</span>
                        </td>
                        <td class="col-width-200">
                            {{plan.name}}
                        </td>
                        <td class="col-width-250">
                            {{plan.description}}
                        </td>
                        <td class="col-width-75" style="text-align: center">
                            {{plan.quantity}}
                        </td>
                        <td class="col-width-75" style="text-align: center">
                            {{plan.consumedQty}}
                        </td>
                        <td>
                        <span ng-if="plan.type != 'OPERATION' && plan.type != 'PHANTOM'">
                        <i class="fa fa-circle" title="" style="color: red" ng-if="plan.consumedQty == 0"></i>
                        <i class="fa fa-warning" title="Partially consumed" style="color: orange"
                           ng-if="plan.consumedQty > 0 && plan.quantity > plan.consumedQty"></i>
                        <i class="fa fa-check" title="Total consumed" ng-if="plan.quantity == plan.consumedQty"
                           style="color: green"></i>
                        </span>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <table class='table table-striped' ng-if="bopPlanVm.selectedResourceValidate == 'parts'">
                    <thead>
                    <tr>
                        <th style="width:1px !important;white-space: nowrap;text-align: left;" translate>ITEM_NUMBER
                        </th>
                        <th translate>ITEM_TYPE</th>
                        <th translate>ITEM_NAME</th>
                        <th class="col-width-100" style="text-align: center" translate>REVISION</th>
                        <th class="col-width-75" style="text-align: center" translate>QUANTITY</th>
                        <th class="col-width-75" style="text-align: center" translate>CONSUMED</th>
                        <th class="col-width-75" style="text-align: center" translate>PRODUCED</th>
                        <th style="width: 20px;"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="bopPlanVm.loadingValidate == true">
                        <td colspan="25"><img
                                src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5"><span translate>LOADING_ITEMS</span>
                        </td>
                    </tr>
                    <tr ng-if="bopPlanVm.loadingValidate == false && bopPlanVm.mBomItems.length == 0">
                        <td colspan="25"><span translate>NO_ITEMS</span></td>
                    </tr>
                    <tr id="{{$index}}" ng-repeat="mBomItem in bopPlanVm.mBomItems">
                        <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <span class="level{{mBomItem.level}}">
                            <i ng-if="mBomItem.hasBom" class="fa"
                               style="cursor: pointer;margin-right: 0;"
                               ng-class="{'fa-caret-right': (mBomItem.expanded == false || mBomItem.expanded == null || mBomItem.expanded == undefined),
                                      'fa-caret-down': mBomItem.expanded == true}"
                               ng-click="bopPlanVm.toggleMBOMItemNode(mBomItem)"></i>
                            <span ng-if="mBomItem.type == 'NORMAL'" ng-class="{'ml9': mBomItem.hasBom == false}">{{mBomItem.itemNumber}}</span>
                            <span ng-if="mBomItem.type == 'PHANTOM'" ng-class="{'ml9': mBomItem.hasBom == false}">{{mBomItem.phantomNumber}}</span>
                        </span>
                        </td>
                        <td class="col-width-150">
                            <span ng-if="mBomItem.type == 'NORMAL'">{{mBomItem.itemTypeName}}</span>
                            <span ng-if="mBomItem.type == 'PHANTOM'">Phantom</span>
                        </td>
                        <td class="col-width-200">
                            <span ng-if="mBomItem.type == 'NORMAL'">{{mBomItem.itemName}}</span>
                            <span ng-if="mBomItem.type == 'PHANTOM'">{{mBomItem.phantomName}}</span>
                        </td>
                        <td class="col-width-75" style="text-align: center">{{mBomItem.revision}}</td>
                        <td class="col-width-75" style="text-align: center">
                            {{mBomItem.quantity}}
                        </td>
                        <td class="col-width-75" style="text-align: center">
                            {{mBomItem.consumedQty}}
                        </td>
                        <td class="col-width-75" style="text-align: center">
                            {{mBomItem.producedQty}}
                        </td>
                        <td>
                            <span ng-if="!mBomItem.hasBom">
                            <i class="fa fa-circle" title="" style="color: red"
                               ng-if="mBomItem.consumedQty == 0 && mBomItem.producedQty == 0"></i>
                            <i class="fa fa-warning" title="Partially consumed" style="color: orange"
                               ng-if="(mBomItem.consumedQty > 0 || mBomItem.producedQty) && (mBomItem.quantity > mBomItem.consumedQty || mBomItem.quantity > mBomItem.producedQty)"></i>
                            <i class="fa fa-check" title="Total consumed"
                               ng-if="mBomItem.quantity == mBomItem.consumedQty && mBomItem.quantity == mBomItem.producedQty"
                               style="color: green"></i>
                            </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>