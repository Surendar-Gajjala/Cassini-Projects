<div>
    <style scoped>
        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;">
                    <i class="la la-plus dropdown-toggle" ng-click="maintenancePlanOperationVm.addOperation()"
                       title="{{addAffectedItemTitle}}" style="cursor: pointer"></i>
                </th>
                <th style="width: 200px" translate>NAME</th>
                <th style="width: 150px" translate>PARAM_NAME</th>
                <th translate>PARAM_TYPE</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th style="width: 100px; text-align: center">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="maintenancePlanOperationVm.saveOperations()"
                       ng-if="maintenancePlanOperationVm.addedOperations.length > 1"
                       title="Save" style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="maintenancePlanOperationVm.removeAllOperations()" title="Remove"
                       ng-if="maintenancePlanOperationVm.addedOperations.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="maintenancePlanOperationVm.maintenancePlanOperations.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Operations.png" alt="" class="image">

                        <div class="message" translate>NO_OPERATIONS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="operation in maintenancePlanOperationVm.maintenancePlanOperations track by $index">
                <td style="width: 20px;"></td>
                <td style="width: 150px;">
                    <span ng-if="!operation.editMode" ng-bind-html="operation.name  | highlightText: freeTextQuery"
                          title="{{operation.name}}">  </span>
                    <span ng-if="!operation.editMode">{{operation.name.length > 25 ? '...' : ''}}</span>

                    <form>
                        <input ng-if="operation.editMode" type="text" class="form-control" ng-model="operation.name"
                               style="width:150px;"/>
                    </form>

                </td>
                <td style="width: 200px;">
                    <span ng-if="!operation.editMode" ng-bind-html="operation.paramName  | highlightText: freeTextQuery"
                          title="{{operation.paramName}}">  </span>
                    <span ng-if="!operation.editMode">{{operation.paramName.length > 25 ? '...' : ''}}</span>
                    <input ng-if="operation.editMode" type="text" class="form-control" ng-model="operation.paramName"
                           style="width:200px;"/>
                </td>
                <td style="width: 300px;">
                    <span ng-if="!operation.editMode">{{operation.paramType}}</span>
                    <span ng-if="!operation.editMode && operation.paramType == 'LIST'"> ( {{operation.lov.name}} )</span>

                    <div style="display: flex;" ng-if="operation.editMode">
                        <ui-select ng-model="operation.paramType" theme="bootstrap" style="width:150px">
                            <ui-select-match placeholder="Select Param Type">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="paramType in maintenancePlanOperationVm.paramTypes | filter: $select.search">
                                <div ng-bind="paramType"></div>
                            </ui-select-choices>
                        </ui-select>
                        <ui-select ng-model="operation.lov" ng-if="operation.paramType == 'LIST'" theme="bootstrap"
                                   style="width:200px;margin-left: 10px !important;">
                            <ui-select-match placeholder="Select Lov">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="lov in maintenancePlanOperationVm.lovs | filter: $select.search">
                                <div ng-bind="lov.name"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </td>
                <td class="col-width-250">
                    <span ng-if="!operation.editMode"
                          ng-bind-html="operation.description | highlightText: freeTextQuery"
                          title="{{operation.description}}"> </span>
                    <form>
                        <input ng-if="operation.editMode" type="text" class="form-control"
                               ng-model="operation.description"/>
                    </form>

                </td>
                <td class="text-center">
                 <span class="btn-group" ng-if="operation.editMode == true" style="margin: 0">
                    <i ng-show="operation.isNew == true"  type="button"
                            title="{{save}}"
                            ng-click="maintenancePlanOperationVm.saveOperation(operation)"
                        class="la la-check">
                    </i>
                    <i ng-show="operation.isNew == true"  type="button"
                            title="{{cancelChangesTitle}}"
                            ng-click="maintenancePlanOperationVm.removeOperation(operation)" class="la la-times">
                    </i>
                      <i ng-show="operation.isNew == false"
                              type="button" title="{{save}}"
                              ng-click="maintenancePlanOperationVm.updateOperation(operation)"
                           class="la la-check">
                      </i>
                     <i ng-show="operation.isNew == false"  type="button"
                             title="{{cancelChangesTitle}}"
                             ng-click="maintenancePlanOperationVm.cancelChanges(operation)" class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body ng-if="operation.editMode == false"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="operation.editMode == false"
                            ng-click="maintenancePlanOperationVm.editOperation(operation)">
                            <a translate>EDIT_OPERATION</a>
                        </li>
                        <li ng-if="operation.editMode == false"
                            ng-click="maintenancePlanOperationVm.deleteOperation(operation)">
                            <a translate>DELETE_OPERATION</a>
                        </li>
                        <plugin-table-actions context="maintenancePlan.operation" object-value="operation"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>