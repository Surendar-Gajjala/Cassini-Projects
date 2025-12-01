<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;"
                ng-if="hasPermission('ncr','edit') && !ncr.released  && ncr.statusType != 'REJECTED'">
                <i class="la la-plus dropdown-toggle" ng-click="ncrProblemItemsVm.addAffectedItems()"
                   title="{{addProblemItemTitle}}" style="cursor: pointer"></i>
            </th>
            <th style="width: 150px;" translate>PART_NUMBER</th>
            <th style="width: 150px" translate>PART_NAME</th>
            <th style="width: 150px;" translate>MANUFACTURER_PART_TYPE</th>
            <th style="width: 150px;" translate>RECEIVED_QTY</th>
            <th translate>INSPECTED_QTY</th>
            <th translate>DEFECTIVE_QTY</th>
            <th translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="ncrProblemItemsVm.saveAll()"
                   ng-if="ncrProblemItemsVm.selectedItems.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>

                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-click="ncrProblemItemsVm.removeAll()" title="Remove"
                   ng-if="ncrProblemItemsVm.selectedItems.length > 1"></i>

            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="ncrProblemItemsVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PROBLEM_ITEMS</span>
                        </span>
            </td>
        </tr>
        <tr ng-if="ncrProblemItemsVm.loading == false && ncrProblemItemsVm.problemItems.length == 0">

            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/affectedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_ITEMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="problemItem in ncrProblemItemsVm.problemItems track by $index">
            <td ng-if="!ncr.released"></td>
            <td>
                <a ng-if="!problemItem.editMode" title="{{clickToShowDetails}}" ng-click="showAffectedItem()"
                   ui-sref="app.mfr.mfrparts.details({mfrId:problemItem.material.manufacturer,manufacturePartId:problemItem.material.id})">
                    {{problemItem.material.partNumber}}
                </a>
                <span ng-if="problemItem.editMode">{{problemItem.material.partNumber}}</span>
            </td>
            <td>{{problemItem.material.partName}}</td>
            <td>{{problemItem.material.mfrPartType.name}}</td>
            <td>
                <span ng-if="!problemItem.editMode">{{problemItem.receivedQty}}</span>

                <form>
                    <input ng-if="problemItem.editMode" type="number" class="form-control"
                           ng-model="problemItem.receivedQty"
                           ng-pattern="/^[0-9]{1}[0-9]*$/"/>
                </form>
            </td>
            <td>
                <span ng-if="!problemItem.editMode">{{problemItem.inspectedQty}}</span>

                <form><input ng-if="problemItem.editMode" type="number" class="form-control"
                             ng-model="problemItem.inspectedQty"
                             ng-pattern="/^[0-9]{1}[0-9]*$/"/></form>
            </td>
            <td>
                <span ng-if="!problemItem.editMode">{{problemItem.defectiveQty}}</span>

                <form><input ng-if="problemItem.editMode" type="number" class="form-control"
                             ng-model="problemItem.defectiveQty"
                             ng-pattern="/^[0-9]{1}[0-9]*$/"/></form>
            </td>
            <td class="description-column">
                <form>
                     <span ng-if="problemItem.editMode == true">
                    <form><input type="text" class="form-control" ng-model="problemItem.notes"/></form>
                </span>
                </form>
                <span ng-if="problemItem.editMode == false">
                    {{problemItem.notes}}
                </span>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                 <span class="btn-group" ng-if="problemItem.editMode == true" style="margin: 0">
                    <i ng-show="problemItem.isNew == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="ncrProblemItemsVm.save(problemItem)"
                       class="la la-check">
                    </i>
                    <i ng-show="problemItem.isNew == true"
                       title="{{'CANCEL' | translate}}"
                       ng-click="ncrProblemItemsVm.onCancel(problemItem)"
                       class="la la-times">
                    </i>
                      <i ng-show="problemItem.isNew == false"
                         title="{{'SAVE' | translate}}"
                         ng-click="ncrProblemItemsVm.updateItem(problemItem)"
                         class="la la-check">
                      </i>
                     <i ng-show="problemItem.isNew == false"
                        title="{{'CANCEL' | translate}}"
                        ng-click="ncrProblemItemsVm.cancelChanges(problemItem)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="hasPermission('ncr','edit') && problemItem.editMode == false && !ncr.released && ncr.statusType != 'REJECTED'"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="problemItem.editMode == false"
                            ng-click="ncrProblemItemsVm.editItem(problemItem)">
                            <a translate>EDIT_PART</a>
                        </li>
                        <li ng-if="problemItem.editMode == false"
                            ng-click="ncrProblemItemsVm.deleteItem(problemItem)">
                            <a translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="ncr.affected" object-value="problemItem"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>