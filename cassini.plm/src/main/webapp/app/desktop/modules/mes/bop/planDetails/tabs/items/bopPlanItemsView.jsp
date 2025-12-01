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

    .ml-12 {
        margin-left: 12px;
    }

    .ml-3 {
        margin-left: 3px;
    }
</style>
<div class="responsive-table">
    <table class='table table-striped highlight-row' style="margin: 0;">
        <thead>
        <tr>
            <th style="width: 20px;" ng-if="!bopRevision.released && !bopRevision.rejected"></th>
            <th style="width:1px !important;white-space: nowrap;text-align: left;" translate>ITEM_NUMBER</th>
            <th translate>ITEM_TYPE</th>
            <th translate>ITEM_NAME</th>
            <th translate>DESCRIPTION</th>
            <th class="col-width-100" style="text-align: center" translate>REVISION</th>
            <th class="col-width-75" style="text-align: center" translate>QUANTITY</th>
            <th class="col-width-200" translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col"
                ng-if="!bopRevision.released && !bopRevision.rejected">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="bopPlanItemsVm.saveAll()"
                   ng-if="bopPlanItemsVm.addedMbomItems.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-if="bopPlanItemsVm.addedMbomItems.length > 1"
                   ng-click="bopPlanItemsVm.removeAll()" title="Remove"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="bopPlanItemsVm.loading == true">
            <td colspan="14">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                     class="mr5"><span translate>LOADING_ITEMS</span>
            </td>
        </tr>
        <tr style="font-weight: bold;width: 50px;"
            ng-if="bopPlanItemsVm.loading == false">
            <td style="width: 20px;" ng-if="!bopRevision.released && !bopRevision.rejected" title="Add Consume Parts">
                <i class="la la-plus" style="cursor: pointer" ng-click="bopPlanItemsVm.addItems('CONSUMED')"></i>
            </td>
            <td colspan="12">
                    <span>
                        <i class="fa fa-caret-right" ng-if="!bopPlanItemsVm.expandedConsumedParts"
                           ng-click="bopPlanItemsVm.toggleConsumedParts()"></i>
                        <i class="fa fa-caret-down" ng-if="bopPlanItemsVm.expandedConsumedParts"
                           ng-click="bopPlanItemsVm.toggleConsumedParts()"></i>
                    </span>
                <span>Consumed Parts</span>
            </td>
        </tr>
        <tr ng-if="bopPlanItemsVm.loading == false && bopPlanItemsVm.consumedParts.length == 0 && bopPlanItemsVm.expandedConsumedParts">
            <td colspan="12">
                {{ 'NO_CONSUMED_PARTS' | translate}}
            </td>
        </tr>
        <tr ng-if="bopPlanItemsVm.loading == false && bopPlanItemsVm.expandedConsumedParts"
            ng-repeat="bopPlanItem in bopPlanItemsVm.consumedParts">
            <td ng-if="!bopRevision.released && !bopRevision.rejected"></td>
            <td style="width:1px !important;white-space: nowrap;text-align: left;">
                {{bopPlanItem.itemNumber}}
            </td>
            <td class="col-width-150">
                {{bopPlanItem.itemTypeName}}
            </td>
            <td class="col-width-200">
                {{bopPlanItem.itemName}}
            </td>
            <td class="col-width-250" style="text-align: center">{{bopPlanItem.description}}</td>
            <td class="col-width-75" style="text-align: center">{{bopPlanItem.revision}}</td>
            <td class="col-width-75" style="text-align: center">
                <span ng-if="!bopPlanItem.editMode">{{bopPlanItem.quantity}}</span>
                <input type="text" class="form-control" ng-if="bopPlanItem.editMode" ng-model="bopPlanItem.quantity"/>
            </td>
            <td class="col-width-200">
                <span ng-if="!bopPlanItem.editMode">{{bopPlanItem.notes}}</span>
                <input type="text" class="form-control" ng-if="bopPlanItem.editMode" ng-model="bopPlanItem.notes"
                       ng-enter="bopPlanItemsVm.onOk(bopPlanItem)"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col"
                ng-if="!bopRevision.released && !bopRevision.rejected">
                <span class="btn-group" ng-if="bopPlanItem.editMode == true" style="margin: -1px">
                    <i title="{{ 'SAVE' | translate }}" ng-if="bopPlanItem.isNew"
                       ng-click="bopPlanItemsVm.saveItem(bopPlanItem)" class="la la-check">
                    </i>
                    <i title="{{ 'SAVE' | translate }}" ng-if="!bopPlanItem.isNew"
                       ng-click="bopPlanItemsVm.updateItem(bopPlanItem)" class="la la-check">
                    </i>
                    <i title="{{ 'REMOVE' | translate }}"
                       ng-click="bopPlanItemsVm.cancelChanges(bopPlanItem)" class="la la-times">
                    </i>
                </span>
                <span ng-if="bopPlanItem.editMode == false || bopPlanItem.editMode == undefined" class="row-menu"
                      uib-dropdown dropdown-append-to-body style="min-width: 35px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="bopPlanItemsVm.editItem(bopPlanItem)">
                            <a href="" translate>EDIT</a>
                        </li>
                        <li ng-click="bopPlanItemsVm.removeItem(bopPlanItem)">
                            <a href="" translate>REMOVE</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        <tr style="font-weight: bold;width: 50px;"
            ng-if="bopPlanItemsVm.loading == false">
            <td style="width: 20px;" ng-if="!bopRevision.released && !bopRevision.rejected" title="Add Produce Parts">
                <i class="la la-plus" style="cursor: pointer" ng-click="bopPlanItemsVm.addItems('PRODUCED')"></i>
            </td>
            <td colspan="12">
                    <span>
                        <i class="fa fa-caret-right" ng-if="!bopPlanItemsVm.expandedProducedParts"
                           ng-click="bopPlanItemsVm.toggleProducedParts()"></i>
                        <i class="fa fa-caret-down" ng-if="bopPlanItemsVm.expandedProducedParts"
                           ng-click="bopPlanItemsVm.toggleProducedParts()"></i>
                    </span>
                <span>Produced Parts</span>
            </td>
        </tr>
        <tr ng-if="bopPlanItemsVm.loading == false && bopPlanItemsVm.producedParts.length == 0 && bopPlanItemsVm.expandedProducedParts">
            <td colspan="12">
                {{ 'NO_PRODUCED_PARTS' | translate}}
            </td>
        </tr>
        <tr ng-if="bopPlanItemsVm.loading == false && bopPlanItemsVm.expandedProducedParts"
            ng-repeat="bopPlanItem in bopPlanItemsVm.producedParts">
            <td ng-if="!bopRevision.released && !bopRevision.rejected"></td>
            <td style="width:1px !important;white-space: nowrap;text-align: left;">
                {{bopPlanItem.itemNumber}}
            </td>
            <td class="col-width-150">
                {{bopPlanItem.itemTypeName}}
            </td>
            <td class="col-width-200">
                {{bopPlanItem.itemName}}
            </td>
            <td class="col-width-250" style="text-align: center">{{bopPlanItem.description}}</td>
            <td class="col-width-75" style="text-align: center">{{bopPlanItem.revision}}</td>
            <td class="col-width-75" style="text-align: center">
                <span ng-if="!bopPlanItem.editMode">{{bopPlanItem.quantity}}</span>
                <input type="text" class="form-control" ng-if="bopPlanItem.editMode" ng-model="bopPlanItem.quantity"/>
            </td>
            <td class="col-width-200">
                <span ng-if="!bopPlanItem.editMode">{{bopPlanItem.notes}}</span>
                <input type="text" class="form-control" ng-if="bopPlanItem.editMode" ng-model="bopPlanItem.notes"
                       ng-enter="bopPlanItemsVm.onOk(bopPlanItem)"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col"
                ng-if="!bopRevision.released && !bopRevision.rejected">
                <span class="btn-group" ng-if="bopPlanItem.editMode == true" style="margin: -1px">
                    <i title="{{ 'SAVE' | translate }}" ng-if="bopPlanItem.isNew"
                       ng-click="bopPlanItemsVm.saveItem(bopPlanItem)" class="la la-check">
                    </i>
                    <i title="{{ 'SAVE' | translate }}" ng-if="!bopPlanItem.isNew"
                       ng-click="bopPlanItemsVm.updateItem(bopPlanItem)" class="la la-check">
                    </i>
                    <i title="{{ 'REMOVE' | translate }}"
                       ng-click="bopPlanItemsVm.cancelChanges(bopPlanItem)" class="la la-times">
                    </i>
                </span>
                <span ng-if="bopPlanItem.editMode == false || bopPlanItem.editMode == undefined" class="row-menu"
                      uib-dropdown dropdown-append-to-body style="min-width: 35px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="bopPlanItemsVm.editItem(bopPlanItem)">
                            <a href="" translate>EDIT</a>
                        </li>
                        <li ng-click="bopPlanItemsVm.removeItem(bopPlanItem)">
                            <a href="" translate>REMOVE</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>