<div>
    <style scoped>
        .adjust-height {
            height: calc(100% - 31px);
        }

    </style>
    <div class="form-group" style="margin: 0;padding-top: 10px;text-align: center;"
         ng-if="itemWhereUsedVm.bomItems.length > 0">
        <div class="ckbox ckbox-default" style="display: inline-block;">
            <input id="revisions" type="checkbox" ng-model="itemWhereUsedVm.showAllRevisions"
                   style="width: 25px;box-shadow: none;margin-top: 2px;"
                   ng-change="itemWhereUsedVm.loadAllRevisions(itemWhereUsedVm.showAllRevisions)">
            <label for="revisions" class="item-selection-checkbox" style="padding:0;margin: 0 !important;"
                   translate>SHOW_ALL_REVISIONS</label>
        </div>
    </div>
    <div class='responsive-table' id="whereUsedTable" style="top: 42px;">
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 1% !important;white-space: nowrap;">
                    <i class="mr10 fa" style="cursor: pointer; font-size: 16px;" ng-if="itemWhereUsedVm.showExpandAll"
                       title="{{itemWhereUsedVm.expandedAll ? collapseAllTitle : expandAllTitle}}"
                       ng-class="{'fa-caret-right': itemWhereUsedVm.expandedAll == false, 'fa-caret-down': itemWhereUsedVm.expandedAll == true}"
                       ng-click="itemWhereUsedVm.expandAllWhereUsed()"></i>
                    <span translate>ITEM_NUMBER</span>
                </th>
                <th class="col-width-250" translate>ITEM_NAME</th>
                <th translate>ITEM_TYPE</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th style="text-align: center" translate>REVISION</th>
                <th translate>LIFE_CYCLE_PHASE</th>
                <th style="text-align: center" translate>QUANTITY</th>
                <th style="text-align: center" translate>UNITS</th>
                <th translate>REF_DES</th>
                <th class="description-column" translate>NOTES</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemWhereUsedVm.loading == true">
                <td colspan="9">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5"><span translate>LOADING_ITEMS</span>
                </span>
                </td>
            </tr>

            <tr ng-if="itemWhereUsedVm.loading == false && itemWhereUsedVm.bomItems.length == 0">
                <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/whereUsed.png" alt="" class="image">

                        <div class="message">{{ 'NO_ITEMS' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="item in itemWhereUsedVm.bomItems">
                <td style="width: 1% !important;white-space: nowrap;">
                <span class="level{{item.level}}">
                    <i ng-if="item.children > 0" class="mr5 fa"
                       style="cursor: pointer;" title="{{itemWhereUsedVm.ExpandCollapse}}"
                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                       'fa-caret-down': item.expanded == true}"
                       ng-click="itemWhereUsedVm.toggleNode(item)"></i>
                    <a ng-if="loginPersonDetails.external == false" href=""
                       ng-click="itemWhereUsedVm.whereUsedItem(item)">
                        <span ng-class="{'ml8': item.children == 0}">{{item.parent.itemMasterObject.itemNumber}}</span>
                    </a>
                    <span ng-if="loginPersonDetails.external == true" ng-class="{'ml8': item.children == 0}">{{item.parent.itemMasterObject.itemNumber}}</span>
                </span>
                </td>
                <td class="col-width-250">{{item.parent.itemMasterObject.itemName}}</td>
                <td>{{item.parent.itemMasterObject.itemType.name}}</td>
                <td class="description-column" title="{{item.parent.itemMasterObject.description}}"><span
                        ng-bind-html="item.parent.itemMasterObject.description "></span>

                </td>
                <td style="text-align: center">{{item.parent.revision}}</td>
                <td>{{item.parent.lifeCyclePhase.phase}}</td>
                <td style="text-align: center">{{item.quantity}}</td>
                <td style="text-align: center">{{item.item.units}}</td>
                <td>{{item.refdes}}</td>
                <td class="description-column">{{item.notes}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>