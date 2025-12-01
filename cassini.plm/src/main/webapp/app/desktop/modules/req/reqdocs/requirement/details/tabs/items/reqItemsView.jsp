<div>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px">
                    <i class="la la-plus"  ng-hide="reqVersion.lifeCyclePhase.phase == 'Approved'" style="cursor: pointer" title="{{'ADD_ITEM' | translate}}" ng-click="reqItemsVm.addItems()"></i>
                </th>
                <th style="width: 200px;" translate>ITEM_NUMBER</th>
                <th style="width: 200px;" translate>ITEM_TYPE</th>
                <th style="width: 200px;" translate>ITEM_NAME</th>
                <th style="width: 200px;" translate>DESCRIPTION</th>
                <th style="width: 150px;text-align: center" translate>REVISION</th>
                <th style="width: 200px;" translate>LIFECYCLE</th>
                <th style="width: 75px;text-align: center" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="reqItemsVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_ITEMS</span>
                </td>
            </tr>
            <tr ng-if="reqItemsVm.loading == false && reqItemsVm.reqItems.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Items.png" alt="" class="image">

                        <div class="message" translate>NO_ITEMS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-if="reqItemsVm.loading == false"
                ng-repeat="reqItem in reqItemsVm.reqItems">
                <td></td>
                <td style="width: 150px">
                    <a href="" ng-click="reqItemsVm.showItem(reqItem)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="reqItem.item.itemMasterObject.itemNumber | highlightText: freeTextQuery"></span>
                    </a>
                </td>
                <td class="column-width-150">
                    <span ng-bind-html="reqItem.item.itemMasterObject.itemType.name  | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-200">
                    <span ng-bind-html="reqItem.item.itemMasterObject.itemName  | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-250"><span
                        ng-bind-html="reqItem.item.itemMasterObject.description | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 150px; text-align: center;">
                    {{reqItem.item.revision}}
                </td>
                <td style="width: 150px">
                    <item-status item="reqItem.item"></item-status>
                </td>
                <td class="text-center">
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                            style="z-index: 9999 !important;">
                            <li ng-click="reqItemsVm.deleteRequirementItem(reqItem)">
                                <a href="" translate>REMOVE_ITEM</a>
                            </li>
                            <plugin-table-actions context="req.item" object-value="reqItem"></plugin-table-actions>
                        </ul>
                    </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>