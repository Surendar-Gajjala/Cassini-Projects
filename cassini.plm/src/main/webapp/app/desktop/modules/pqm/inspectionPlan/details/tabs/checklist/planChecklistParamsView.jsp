<div style="position: relative;" id="params-view">
    <div style="overflow-y: auto; overflow-x: hidden; padding:0 5px;height: 100%;">
        <div class="responsive-table" style="height: 100%;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px" ng-if="!inspectionPlanRevision.released">
                        <i class="la la-plus"
                           title="Add Param" style="cursor: pointer"
                           ng-click="planChecklistParamsVm.addParameter()"></i>
                    </th>
                    <th style="" translate>NAME</th>
                    <th style="width: 100px;text-align: center;" translate>EXPECTED_VALUE_TYPE</th>
                    <th style="width: 100px;" translate>UNITS</th>
                    <th style="width: 100px;text-align: center;" translate>PASS_CRITERIA</th>
                    <th style="width: 100px;text-align: center;" translate>EXPECTED_VALUE</th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>
                        ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="planChecklistParamsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PARAMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="planChecklistParamsVm.loading == false && planChecklistParamsVm.checklistParameters.length == 0">
                    <td colspan="25" translate>NO_PARAMS</td>
                </tr>

                <tr ng-repeat="param in planChecklistParamsVm.checklistParameters">
                    <td ng-if="!inspectionPlanRevision.released"></td>
                    <td style="">
                        <span ng-if="!param.editMode">{{param.name}}</span>
                        <input class="form-control" ng-model="param.name" ng-if="param.editMode"/>
                    </td>
                    <td style="width: 100px;text-align: center;">
                        <span ng-if="!param.editMode">{{param.expectedValueType}}</span>
                        <select ng-if="param.editMode == true" class="form-control input-sm"
                                ng-model="param.expectedValueType"
                                ng-options="dataType for dataType in planChecklistParamsVm.dataTypes">
                        </select>
                    </td>
                    <td style="">
                        <span ng-if="!param.editMode">{{param.units}}</span>
                        <input class="form-control" ng-model="param.units" ng-if="param.editMode"/>
                    </td>
                    <td style="width: 100px;text-align: center;">
                        <span ng-if="!param.editMode">{{param.passCriteria}}</span>
                        <select ng-if="param.editMode == true && (param.expectedValueType == 'TEXT' || param.expectedValueType == 'BOOLEAN')"
                                class="form-control input-sm"
                                ng-model="param.passCriteria" style="font-size: 16px;padding: 3px !important;"
                                ng-options="criteria for criteria in planChecklistParamsVm.criteria1">
                        </select>
                        <select ng-if="param.editMode == true && (param.expectedValueType == 'INTEGER' || param.expectedValueType == 'DOUBLE' || param.expectedValueType == 'DATE')"
                                class="form-control input-sm" style="font-size: 16px;padding: 3px !important;"
                                ng-model="param.passCriteria"
                                ng-options="criteria for criteria in planChecklistParamsVm.criteria2">
                        </select>
                    </td>
                    <td style="width: 100px;text-align: center;">
                        <span ng-if="!param.editMode && param.expectedValueType == 'TEXT'">{{param.expectedValue.textValue}}</span>
                        <span ng-if="!param.editMode && param.expectedValueType == 'INTEGER'">{{param.expectedValue.integerValue}}</span>
                        <span ng-if="!param.editMode && param.expectedValueType == 'DOUBLE'">{{param.expectedValue.doubleValue}}</span>
                        <span ng-if="!param.editMode && param.expectedValueType == 'BOOLEAN'">{{param.expectedValue.booleanValue}}</span>
                        <span ng-if="!param.editMode && param.expectedValueType == 'DATE'">{{param.expectedValue.dateValue}}</span>
                        <input ng-if="param.editMode == true && param.expectedValueType == 'TEXT'" class="form-control"
                               type="text" ng-model="param.expectedValue.textValue"/>
                        <input ng-if="param.editMode == true && param.expectedValueType == 'INTEGER'"
                               class="form-control" type="text"
                               ng-model="param.expectedValue.integerValue" valid-number/>
                        <input ng-if="param.editMode == true && param.expectedValueType == 'DOUBLE'"
                               class="form-control" type="text"
                               ng-model="param.expectedValue.doubleValue" valid-number/>
                        <input ng-show="param.editMode == true && param.expectedValueType == 'DATE'"
                               type="text" class="form-control"
                               ng-model="param.expectedValue.dateValue" date-picker/>
                        <select ng-if="param.editMode == true && param.expectedValueType == 'BOOLEAN'"
                                class="form-control input-sm"
                                ng-model="param.expectedValue.booleanValue"
                                ng-options="dataType for dataType in planChecklistParamsVm.booleanValues">
                        </select>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col"
                        style="text-align:center; width: 80px;">
                        <span class="btn-group"
                              ng-if="param.isNew == true || param.editMode == true"
                              style="margin: -1px">
                            <button class="btn btn-xs btn-success" type="button" title="Save"
                                    ng-click="planChecklistParamsVm.onOk(param)">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-xs btn-danger" type="button" title="Cancel"
                                    ng-click="planChecklistParamsVm.onCancel(param)">
                                <i class="fa fa-times"></i>
                            </button>
                        </span>
                        <span ng-if="param.isNew != true && param.editMode != true && !inspectionPlanRevision.released && !inspectionPlanRevision.rejected"
                              class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="planChecklistParamsVm.editParams(param)">
                                    <a href="" translate>EDIT</a>
                                </li>

                                <li ng-click="planChecklistParamsVm.deleteParams(param)">
                                    <a href="" translate>DELETE</a>
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
