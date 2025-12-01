<div style="position: relative;" id="params-view">
    <div style="overflow-y: auto; overflow-x: hidden; padding:0 5px;height: 100%;">
        <div class="responsive-table" style="height: 100%;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="" translate>NAME</th>
                    <th style="width: 100px;text-align: center;" translate>EXPECTED_VALUE_TYPE</th>
                    <th style="width: 100px;text-align: center;" translate>EXPECTED_VALUE</th>
                    <th style="width: 100px;text-align: center;" translate>UNITS</th>
                    <th style="width: 100px;text-align: center;" translate>PASS_CRITERIA</th>
                    <th style="width: 100px;text-align: center;" translate>STATUS</th>
                    <th style="width: 100px;text-align: center;" translate>ACTUAL_VALUE</th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>
                        ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="inspectionChecklistParamsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PARAMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="inspectionChecklistParamsVm.loading == false && inspectionChecklistParamsVm.paramActualValues.length == 0">
                    <td colspan="25" translate>NO_PARAMS</td>
                </tr>

                <tr ng-repeat="param in inspectionChecklistParamsVm.paramActualValues">
                    <td style="">
                        <span>{{param.param.name}}</span>
                    </td>
                    <td style="width: 100px;text-align: center;">
                        <span>{{param.param.expectedValueType}}</span>
                    </td>
                    <td style="width: 100px;">
                        <span ng-if="param.param.expectedValueType == 'TEXT'">{{param.param.expectedValue.textValue}}</span>
                        <span ng-if="param.param.expectedValueType == 'INTEGER'">{{param.param.expectedValue.integerValue}}</span>
                        <span ng-if="param.param.expectedValueType == 'DOUBLE'">{{param.param.expectedValue.doubleValue}}</span>
                        <span ng-if="param.param.expectedValueType == 'BOOLEAN'">{{param.param.expectedValue.booleanValue}}</span>
                        <span ng-if="param.param.expectedValueType == 'DATE'">{{param.param.expectedValue.dateValue}}</span>
                    </td>
                    <td style="width: 100px;text-align: center;">
                        {{param.param.units}}
                    </td>
                    <td style="text-align: center;">
                        <span>{{param.param.passCriteria}}</span>
                    </td>
                    <td style="width: 100px;text-align: center;">
                        <checklist-status status="param.result"></checklist-status>
                    </td>
                    <td style="width: 100px;text-align: center;">
                        <span ng-if="!param.editMode && param.param.expectedValueType == 'TEXT'">{{param.textValue}}</span>
                        <span ng-if="!param.editMode && param.param.expectedValueType == 'INTEGER'">{{param.integerValue}}</span>
                        <span ng-if="!param.editMode && param.param.expectedValueType == 'DOUBLE'">{{param.doubleValue}}</span>
                        <span ng-if="!param.editMode && param.param.expectedValueType == 'BOOLEAN'">{{param.booleanValue}}</span>
                        <span ng-if="!param.editMode && param.param.expectedValueType == 'DATE'">{{param.dateValue}}</span>
                        <input ng-if="param.editMode == true && param.param.expectedValueType == 'TEXT'"
                               class="form-control"
                               type="text" ng-model="param.textValue"/>
                        <input ng-if="param.editMode == true && param.param.expectedValueType == 'INTEGER'"
                               class="form-control" type="number" ng-pattern="/^[0-9]+$/"
                               ng-model="param.integerValue"/>
                        <input ng-if="param.editMode == true && param.param.expectedValueType == 'DOUBLE'"
                               class="form-control" type="text"
                               ng-model="param.doubleValue" valid-number/>
                        <input ng-show="param.editMode == true && param.param.expectedValueType == 'DATE'"
                               type="text" class="form-control"
                               ng-model="param.dateValue" date-picker/>
                        <select ng-if="param.editMode == true && param.param.expectedValueType == 'BOOLEAN'"
                                class="form-control input-sm"
                                ng-model="param.booleanValue"
                                ng-options="dataType for dataType in inspectionChecklistParamsVm.booleanValues">
                        </select>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col"
                        style="text-align:center; width: 80px;">
                        <span class="btn-group"
                              ng-if="param.editMode == true"
                              style="margin: -1px">
                            <button class="btn btn-xs btn-success" type="button" title="Save"
                                    ng-click="inspectionChecklistParamsVm.onOk(param)">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-xs btn-danger" type="button" title="Cancel"
                                    ng-click="inspectionChecklistParamsVm.onCancel(param)">
                                <i class="fa fa-times"></i>
                            </button>
                        </span>
                        <span ng-if="param.editMode != true && (inspectionChecklistParamsVm.checklist.assignedTo == loginPersonDetails.person.id || hasPermission('admin','all'))"
                              ng-hide="inspection.released || inspection.statusType == 'REJECTED'"
                              class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="inspectionChecklistParamsVm.editParams(param)">
                                    <a href="" translate>EDIT</a>
                                </li>
                            </ul>
                        </span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
