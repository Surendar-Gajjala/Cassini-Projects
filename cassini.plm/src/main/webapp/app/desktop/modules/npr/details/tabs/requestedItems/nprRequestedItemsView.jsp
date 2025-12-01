<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 30px;">
                <i class="la la-plus"
                   ng-if="!loginPersonDetails.external && (npr.status == 'OPEN' || npr.status == 'PENDING')"
                   title="" style="cursor: pointer"
                   ng-click="nprRequestedItemsVm.createRequestedItem()"></i>
            </th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
            <th class="col-width-150" translate>ITEM_TYPE</th>
            <th class="col-width-200" style="width: 200px;" translate>ITEM_NAME</th>
            <th class="col-width-250" style="width: 200px;" translate>DESCRIPTION</th>
            <th style="width: 150px;text-align: center" translate>REVISION</th>
            <th style="width: 200px;" translate>LIFE_CYCLE_PHASE</th>
            <th class="col-width-250" translate>NOTES</th>
            <th style="width: 100px;text-align: center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="nprRequestedItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_REQUESTED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="nprRequestedItemsVm.loading == false && nprRequestedItemsVm.nprItems.length == 0">
            <td colspan="12" style="background-color: #f9fbfe !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Items.png" alt="" class="image">
                    <div class="message">{{ 'NO_REQUESTED_ITEMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="item in nprRequestedItemsVm.nprItems">
            <td></td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a href="" ng-click="nprRequestedItemsVm.showItem(item.latestRevision)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{item.itemNumber}}</a>
            </td>
            <td class="col-width-150">{{item.itemType.name}}</td>
            <td class="col-width-200">{{item.itemName}}</td>
            <td class="col-width-250">{{item.description}}</td>
            <td style="width: 150px;text-align: center">{{item.latestRevisionObject.revision}}</td>
            <td>
                <item-status item="item.latestRevisionObject"></item-status>
            </td>
            <td class="col-width-250">
                <span ng-if="!item.editMode">{{item.notes}}</span>
                <form>
                    <input ng-if="item.editMode" type="text" ng-model="item.notes" class="form-control"/>
                </form>
            </td>
            <td class="text-center">
                <span class="btn-group"
                      ng-if="item.editMode == true && loginPersonDetails.external == false && (npr.status == 'OPEN' || npr.status == 'HOLD')"
                      style="margin: -1px">
                   <i title="{{ 'SAVE' | translate }}"
                      ng-click="nprRequestedItemsVm.updateItem(item)"
                      class="la la-check">
                   </i>
                   <i title="{{cancelChangesTitle}}"
                      ng-click="nprRequestedItemsVm.cancelChanges(item)"
                      class="la la-times">
                   </i>
                </span>
                <span class="row-menu"
                      ng-if="item.editMode == false && npr.status != 'APPROVED' && npr.status != 'REJECTED' && !item.assignedNumber"
                      uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="!loginPersonDetails.external && npr.status == 'PENDING' && !item.assignedNumber">
                            <a href="" ng-click="nprRequestedItemsVm.assignItemNumber(item)"
                               translate>ASSIGN_ITEM_NUMBER</a>
                        </li>
                        <li ng-if="!loginPersonDetails.external && (npr.status == 'OPEN' || npr.status == 'HOLD')">
                            <a href="" ng-click="nprRequestedItemsVm.editItem(item)"
                               translate>EDIT_ITEM</a>
                        </li>
                        <li ng-if="!loginPersonDetails.external && (npr.status == 'OPEN' || npr.status == 'HOLD')">
                            <a href=""
                               ng-click="nprRequestedItemsVm.deleteItem(item)"
                               translate>DELETE_ITEM</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>