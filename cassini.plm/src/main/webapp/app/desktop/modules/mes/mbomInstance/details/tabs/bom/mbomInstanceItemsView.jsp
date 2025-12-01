<div class="mbom-items-view" style="width: 100%;">
    <div class='responsive-table'>
        <table class='table table-striped'>
            <thead>
            <tr>
                <th style="width:1px !important;white-space: nowrap;text-align: left;" translate>ITEM_NUMBER</th>
                <th translate>ITEM_TYPE</th>
                <th translate>ITEM_NAME</th>
                <th translate>DESCRIPTION</th>
                <th class="col-width-100" style="text-align: center" translate>REVISION</th>
                <th class="col-width-75" style="text-align: center" translate>QUANTITY</th>
                <th class="col-width-75" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mbomInstanceItemsVm.loading == true">
                <td colspan="25"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_ITEMS</span>
                </td>
            </tr>
            <tr ng-if="mbomInstanceItemsVm.loading == false && mbomInstanceItemsVm.mBomItems.length == 0">
                <td colspan="25"><span translate>NO_ITEMS</span></td>
            </tr>
            <tr id="{{$index}}" ng-repeat="bomItem in mbomInstanceItemsVm.mBomItems">
                <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <span class="level{{bomItem.level}}">
                            <i ng-if="bomItem.hasBom && bomItem.editMode == false" class="fa"
                               style="cursor: pointer;margin-right: 0;"
                               ng-class="{'fa-caret-right': (bomItem.expanded == false || bomItem.expanded == null || bomItem.expanded == undefined),
                                          'fa-caret-down': bomItem.expanded == true}"
                               ng-click="mbomInstanceItemsVm.toggleItemNode(bomItem)"></i>
                            <span ng-class="{'ml9': bomItem.hasBom == false}">{{bomItem.itemNumber}}</span>
                        </span>
                </td>
                <td class="col-width-200">{{bomItem.itemTypeName}}</td>
                <td class="col-width-200">{{bomItem.itemName}}</td>
                <td class="col-width-300" style="text-align: center">{{bomItem.description}}</td>
                <td class="col-width-100" style="text-align: center">{{bomItem.revision}}</td>
                <td class="col-width-75" style="text-align: center">{{bomItem.quantity}}</td>
                <td>

                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>