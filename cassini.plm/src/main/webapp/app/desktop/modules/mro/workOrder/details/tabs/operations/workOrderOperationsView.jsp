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
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th class="col-width-150" translate>NAME</th>
                <th class="col-width-150" translate>PARAM_NAME</th>
                <th translate>PARAM_TYPE</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th translate>VALUE</th>
                <th translate>RESULT</th>
                <th class="col-width-250" translate>NOTES</th>
                <th class="actions-col sticky-col sticky-actions-col" style="width: 100px; text-align: center"
                    ng-if="workOrder.status != 'FINISH'">
                    <span translate>ACTIONS</span>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="workOrderOperationVm.workOrderOperations.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Operations.png" alt="" class="image">

                        <div class="message" translate>NO_OPERATIONS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="operation in workOrderOperationVm.workOrderOperations track by $index">
                <td class="col-width-150" title="{{operation.name}}">
                    <span ng-bind-html="operation.name  | highlightText: freeTextQuery"></span>

                </td>
                <td class="col-width-200" title="{{operation.paramName}}">
                    <span ng-bind-html="operation.paramName  | highlightText: freeTextQuery"></span>

                </td>
                <td class="col-width-300">
                    <span>{{operation.paramType}}</span>
                    <span ng-if="operation.paramType == 'LIST'"> ( {{operation.lov.name}} )</span>
                </td>
                <td class="col-width-250" title="{{operation.description}}">
                    <span ng-bind-html="operation.description | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-200">
                    <span ng-if="!operation.editMode && operation.paramType == 'TEXT'">{{operation.textValue}}</span>
                    <span ng-if="!operation.editMode && operation.paramType == 'INTEGER'">{{operation.integerValue}}</span>
                    <span ng-if="!operation.editMode && operation.paramType == 'DECIMAL'">{{operation.decimalValue}}</span>
                    <span ng-if="!operation.editMode && operation.paramType == 'LIST'">{{operation.listValue}}</span>
                    <span ng-if="!operation.editMode && operation.paramType == 'BOOLEAN'">{{operation.booleanValue}}</span>

                    <input type="text" class="form-control" ng-if="operation.editMode && operation.paramType == 'TEXT'"
                           ng-model="operation.textValue"/>
                    <input type="text" class="form-control"
                           ng-if="operation.editMode && operation.paramType == 'INTEGER'"
                           ng-model="operation.integerValue" numbers-only/>
                    <input type="text" class="form-control"
                           ng-if="operation.editMode && operation.paramType == 'DECIMAL'"
                           ng-model="operation.decimalValue" valid-number/>
                    <ui-select ng-model="operation.listValue"
                               ng-if="operation.editMode && operation.paramType == 'LIST'" theme="bootstrap"
                               style="width:200px;">
                        <ui-select-match placeholder="Select value">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="value in operation.lov.values | filter: $select.search">
                            <div ng-bind="value"></div>
                        </ui-select-choices>
                    </ui-select>
                    <ui-select ng-model="operation.booleanValue"
                               ng-if="operation.editMode && operation.paramType == 'BOOLEAN'" theme="bootstrap"
                               style="width:200px;">
                        <ui-select-match placeholder="Select value">{{$select.selected.label}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="value.value as value in workOrderOperationVm.booleanValues | filter: $select.search">
                            <div ng-bind="value.label"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>
                <td style="width: 100px;">
                    <form>
                        <checklist-status ng-if="!operation.editMode"
                                          status="operation.result"></checklist-status>
                        <ui-select ng-model="operation.result" ng-if="operation.editMode" theme="bootstrap"
                                   style="width:100px;">
                            <ui-select-match placeholder="{{select}}">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="result in workOrderOperationVm.operationResults | filter: $select.search">
                                <div ng-bind="result"></div>
                            </ui-select-choices>
                        </ui-select>
                    </form>
                </td>
                <td class="col-width-250">
                    <span ng-if="!operation.editMode" ng-bind-html="operation.notes | highlightText: freeTextQuery"
                          title="{{operation.notes}}"></span>

                    <form>
                        <input type="text" class="form-control" ng-if="operation.editMode" ng-model="operation.notes"/>
                    </form>
                </td>
                <td class="text-center actions-col sticky-col sticky-actions-col" ng-if="workOrder.status != 'FINISH'">
                 <span class="btn-group" ng-if="operation.editMode == true" style="margin: 0">
                     <button class="btn btn-xs btn-success" type="button" title="{{saveItemTitle}}"
                             ng-click="workOrderOperationVm.updateOperation(operation)">
                         <i class="fa fa-check"></i>
                     </button>
                     <button class="btn btn-xs btn-danger" type="button" title="{{cancelChangesTitle}}"
                             ng-click="workOrderOperationVm.cancelChanges(operation)">
                         <i class="fa fa-times"></i>
                     </button>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body ng-if="operation.editMode == false"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="operation.editMode == false"
                            ng-click="workOrderOperationVm.editOperation(operation)">
                            <a translate>EDIT_OPERATION</a>
                        </li>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>