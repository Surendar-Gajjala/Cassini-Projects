<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 30px;" ng-if="hasPermission && hasCreate">
                <i class="la la-plus" style="cursor: pointer" title="{{addRelatedItemTitle}}"
                   ng-click="showRelatedItems()"></i>
            </th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
            <th style="width: 200px;" translate>ITEM_TYPE</th>
            <th class="col-width-250" style="width: 300px;" translate>ITEM_NAME</th>
            <th class="col-width-250" style="width: 300px;" translate>DESCRIPTION</th>
            <th style="width: 150px;" translate>LIFE_CYCLE_PHASE</th>
            <th style="width: 100px;text-align: center;" translate>REVISION</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                ACTIONS
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="loading == false && relatedItems.length == 0">

            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/relatedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_RELATED_ITEMS'| translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="relatedItem in relatedItems track by $index">
            <td ng-if="hasPermission && hasCreate"></td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a href="" ng-click="showItem(relatedItem)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="relatedItem.itemNumber"></span>
                </a>
            </td>

            <td>
                {{relatedItem.itemType}}
            </td>
            <td>
                {{relatedItem.itemName}}
            </td>
            <td class="col-width-250">
                {{relatedItem.description}}
            </td>
            <td style="width: 150px">
                <item-status item="relatedItem"></item-status>
            </td>
            <td style="width: 100px;text-align: center">
                {{relatedItem.revision}}
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span ng-if="hasPermission" class="row-menu" uib-dropdown dropdown-append-to-body>
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-class="{'cursor-override': !hasDelete}"
                            title="{{hasDelete ? '' : noPermission}}">
                            <a ng-click="deleteItem(relatedItem)"
                               ng-class="{'disabled': !hasDelete}"translate>REMOVE_ITEM</a>
                        </li>
                        <plugin-table-actions context="object.related" object-value="relatedItem"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>