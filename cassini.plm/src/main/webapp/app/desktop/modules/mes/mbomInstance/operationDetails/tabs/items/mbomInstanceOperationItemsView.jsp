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
                <i class="fa fa-check-circle" ng-click="mbomInstanceOperationItemsVm.saveAll()"
                   ng-if="mbomInstanceOperationItemsVm.addedMbomItems.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-if="mbomInstanceOperationItemsVm.addedMbomItems.length > 1"
                   ng-click="mbomInstanceOperationItemsVm.removeAll()" title="Remove"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="mbomInstanceOperationItemsVm.loading == true">
            <td colspan="14">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                     class="mr5"><span translate>LOADING_ITEMS</span>
            </td>
        </tr>
        <tr style="font-weight: bold;width: 50px;"
            ng-if="mbomInstanceOperationItemsVm.loading == false">
            <td colspan="12">
                    <span>
                        <i class="fa fa-caret-right" ng-if="!mbomInstanceOperationItemsVm.expandedConsumedParts"
                           ng-click="mbomInstanceOperationItemsVm.toggleConsumedParts()"></i>
                        <i class="fa fa-caret-down" ng-if="mbomInstanceOperationItemsVm.expandedConsumedParts"
                           ng-click="mbomInstanceOperationItemsVm.toggleConsumedParts()"></i>
                    </span>
                <span>Consumed Parts</span>
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationItemsVm.loading == false && mbomInstanceOperationItemsVm.consumedParts.length == 0 && mbomInstanceOperationItemsVm.expandedConsumedParts">
            <td colspan="12">
                {{ 'NO_CONSUMED_PARTS' | translate}}
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationItemsVm.loading == false && mbomInstanceOperationItemsVm.expandedConsumedParts"
            ng-repeat="bopPlanItem in mbomInstanceOperationItemsVm.consumedParts">
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
                       ng-enter="mbomInstanceOperationItemsVm.onOk(bopPlanItem)"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">              
            </td>
        </tr>
        <tr style="font-weight: bold;width: 50px;"
            ng-if="mbomInstanceOperationItemsVm.loading == false">
            <td colspan="12">
                    <span>
                        <i class="fa fa-caret-right" ng-if="!mbomInstanceOperationItemsVm.expandedProducedParts"
                           ng-click="mbomInstanceOperationItemsVm.toggleProducedParts()"></i>
                        <i class="fa fa-caret-down" ng-if="mbomInstanceOperationItemsVm.expandedProducedParts"
                           ng-click="mbomInstanceOperationItemsVm.toggleProducedParts()"></i>
                    </span>
                <span>Produced Parts</span>
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationItemsVm.loading == false && mbomInstanceOperationItemsVm.producedParts.length == 0 && mbomInstanceOperationItemsVm.expandedProducedParts">
            <td colspan="12">
                {{ 'NO_PRODUCED_PARTS' | translate}}
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationItemsVm.loading == false && mbomInstanceOperationItemsVm.expandedProducedParts"
            ng-repeat="bopPlanItem in mbomInstanceOperationItemsVm.producedParts">
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
                       ng-enter="mbomInstanceOperationItemsVm.onOk(bopPlanItem)"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col"> 
            </td>
        </tr>
        </tbody>
    </table>
</div>