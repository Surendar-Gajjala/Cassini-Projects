<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 30px" ng-hide="external.external == true">
                <div class="ckbox ckbox-default"
                     style="display: inline-block;">
                    <input id="items{{$index}}" name="itemSelected" type="checkbox"
                           ng-model="mfrPartsWhereusedVm.flag" ng-click="mfrPartsWhereusedVm.selectAll()">
                    <label ng-if="mfrPartsWhereusedVm.itemMfrs.length != 0"
                           for="items{{$index}}" class="item-selection-checkbox"></label>
                </div>
            </th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
            <th translate>ITEM_TYPE</th>
            <th class="col-width-250" translate>ITEM_NAME</th>
            <th translate style="text-align: center;width: 150px">REVISION</th>
            <th translate>LIFE_CYCLE_PHASE</th>
            <th translate>UNITS</th>
            <th translate>CREATED_BY</th>
            <th translate>CREATED_DATE</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="mfrPartsWhereusedVm.loading == true">
            <td colspan="9">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                    <span translate>LOADING_ITEMS</span>
                </span>
            </td>
        </tr>

        <tr ng-if="mfrPartsWhereusedVm.loading == false && mfrPartsWhereusedVm.itemMfrs.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/whereUsed.png" alt="" class="image">

                    <div class="message">{{ 'NO_ITEMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="item in mfrPartsWhereusedVm.items">
            <td style="width: 30px;" ng-hide="external.external == true">
                <div class="ckbox ckbox-default" style="display: inline-block;">
                    <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                           ng-model="item.selected" ng-click="mfrPartsWhereusedVm.toggleSelection(item)">
                    <label for="item{{$index}}" class="item-selection-checkbox"></label>
                </div>
            </td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a ng-hide="external.external == true" href="" ng-click="mfrPartsWhereusedVm.showItem(item)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    {{item.itemMasterObject.itemNumber}}</a>
                <span ng-hide="external.external == false">{{item.itemMasterObject.itemNumber}}</span>
            </td>
            <td>{{item.itemMasterObject.itemType.name}}</td>
            <td class="col-width-250">{{item.itemMasterObject.itemName}}</td>
            <td style="text-align: center;width: 150px">{{item.revision}}</td>
            <td>{{item.lifeCyclePhase.phase}}</td>
            <td>{{item.itemMasterObject.units}}</td>
            <td>{{item.createdByObject.fullName}}</td>
            <td>{{item.createdDate}}</td>
        </tr>
        </tbody>
    </table>
</div>