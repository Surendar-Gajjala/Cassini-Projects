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
                    <i class="la la-plus dropdown-toggle" ng-if="workOrder.status != 'FINISH'"
                       ng-click="workOrderSparePartVm.addSpareParts()"
                       title="{{'ADD_SPARE_PARTS' | translate}}" style="cursor: pointer"></i>
                </th>
                <th style="width: 150px;" translate>NUMBER</th>
                <th class="col-width-150" translate>NAME</th>
                <th class="col-width-200" translate>TYPE</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th translate>QUANTITY</th>
                <th translate>DISPOSITION_TYPE</th>
                <th translate>SERIAL_NUMBERS</th>
                <th class="col-width-250" translate>NOTES</th>
                <th style="width: 100px; text-align: center" ng-if="workOrder.status != 'FINISH'">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="workOrderSparePartVm.saveParts()"
                       ng-if="workOrderSparePartVm.selectedSpareParts.length > 1"
                       title="Save" style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="workOrderSparePartVm.removeParts()" title="Remove"
                       ng-if="workOrderSparePartVm.selectedSpareParts.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="workOrderSparePartVm.workOrderSpareParts.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/SpareParts.png" alt="" class="image">
                        <div class="message" translate>NO_SPARE_PARTS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="sparePart in workOrderSparePartVm.workOrderSpareParts track by $index">
                <td></td>
                <td>
                    <a title="{{clickToShowDetails}}" ng-click="workOrderSparePartVm.showSparePart(sparePart)">
                        {{sparePart.sparePart.number}}
                    </a>
                </td>
                <td title="{{sparePart.sparePart.name}}" class="col-width-150">
                    <span ng-bind-html="sparePart.sparePart.name"></span>
                </td>
                <td class="col-width-200">{{sparePart.sparePart.type.name}}</td>
                <td class="col-width-250">{{sparePart.sparePart.description}}</td>
                <td style="width: 100px !important;">
                    <span ng-if="!sparePart.editMode">{{sparePart.quantity}}</span>
                    <form> <input ng-if="sparePart.editMode" type="text" class="form-control" ng-model="sparePart.quantity" placeholder="{{ 'ENTER_QUANTITY ' | translate }}"
                           style="width: 100px;" valid-number></form>
                </td>
                <td>
                   <form>
                       <span ng-if="!sparePart.editMode && sparePart.disposition == 'REPLACE'" class="label label-primary">{{sparePart.disposition}}</span>
                       <span ng-if="!sparePart.editMode && sparePart.disposition == 'REPAIR'" class="label label-warning">{{sparePart.disposition}}</span>
                       <ui-select ng-model="sparePart.disposition" theme="bootstrap" style="width:100%"
                                  ng-if="sparePart.editMode">
                           <ui-select-match placeholder="Select Disposition Type">{{$select.selected.label}}
                           </ui-select-match>
                           <ui-select-choices
                                   repeat="dispositionType.value as dispositionType in workOrderSparePartVm.dispositionTypes | filter: $select.search">
                               <div ng-bind="dispositionType.label"></div>
                           </ui-select-choices>
                       </ui-select>
                   </form>
                </td>
                <td>
                    <form>
                        <span ng-if="!sparePart.editMode && sparePart.disposition == 'REPLACE'">{{sparePart.newSerialNumbers}}</span>
                        <form> <input ng-if="sparePart.editMode && sparePart.disposition == 'REPLACE'" type="text" placeholder="{{ 'SERIAL_NUMBER ' | translate }}"
                               class="form-control" ng-model="sparePart.newSerialNumbers"
                               style="width: 100px;"> </form>
                    </form>
                </td>
                <td class="col-width-250">
                    <form>
                        <span ng-if="sparePart.editMode == true">
                       <form> <input type="text" class="form-control" placeholder="{{ 'ENTER_NOTES ' | translate }}" ng-model="sparePart.notes"/></form>
                    </span>
                    </form>
                    <span ng-if="sparePart.editMode == false"
                          ng-bind-html="sparePart.notes  | highlightText: freeTextQuery"
                          title="{{sparePart.notes}}"></span>
                </td>
                <td class="text-center" ng-if="workOrder.status != 'FINISH'">
                 <span class="btn-group" ng-if="sparePart.editMode == true" style="margin: 0">
                    <button ng-show="sparePart.isNew == true" class="btn btn-xs btn-success" type="button"
                            title="{{saveItemTitle}}"
                            ng-click="workOrderSparePartVm.savePart(sparePart)">
                        <i class="fa fa-check"></i>
                    </button>
                    <button ng-show="sparePart.isNew == true" class="btn btn-xs btn-danger" type="button"
                            title="{{cancelChangesTitle}}"
                            ng-click="workOrderSparePartVm.removePart(sparePart)">
                        <i class="fa fa-times"></i>
                    </button>
                      <button ng-show="sparePart.isNew == false" class="btn btn-xs btn-success"
                              type="button" title="{{saveItemTitle}}"
                              ng-click="workOrderSparePartVm.updatePart(sparePart)">
                          <i class="fa fa-check"></i>
                      </button>
                     <button ng-show="sparePart.isNew == false" class="btn btn-xs btn-danger" type="button"
                             title="{{cancelChangesTitle}}"
                             ng-click="workOrderSparePartVm.cancelChanges(sparePart)">
                         <i class="fa fa-times"></i>
                     </button>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="sparePart.editMode == false"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="sparePart.editMode == false"
                            ng-click="workOrderSparePartVm.editPart(sparePart)">
                            <a translate>EDIT_PART</a>
                        </li>
                        <li ng-if="sparePart.editMode == false"
                            ng-click="workOrderSparePartVm.deletePart(sparePart)">
                            <a translate>REMOVE_PART</a>
                        </li>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>