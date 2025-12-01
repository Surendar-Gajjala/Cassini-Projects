<div ng-show="qcrFor == 'NCR'" class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;" ng-if="!qcrReleased && hasPermission('qcr','edit')">
                <i class="la la-plus dropdown-toggle" ng-if="qcrFor == 'NCR'"
                   ng-click="qcrRelatedItemsVm.addRelatedMaterials()" title="{{addRelatedItemTitle}}"
                   style="cursor: pointer"></i>
            </th>
            <th translate>PART_NUMBER</th>
            <th style="width: 150px" translate>PART_NAME</th>
            <th translate>MANUFACTURER_PART_TYPE</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                <span translate>ACTIONS</span>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="qcrRelatedItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="qcrRelatedItemsVm.loading == false && qcrRelatedItemsVm.relatedItems.length == 0">
            <td colspan="12"><span translate>NO_RELATED_ITEMS</span></td>
        </tr>
        <tr ng-repeat="relatedItem in qcrRelatedItemsVm.relatedItems">
            <td ng-if="!qcrReleased"></td>
            <td>
                <a title="{{clickToShowDetails}}" ng-click="showRelatedItem()"
                   ui-sref="app.mfr.mfrparts.details({mfrId:relatedItem.material.manufacturer,manufacturePartId:relatedItem.material.id})">
                    {{relatedItem.material.partNumber}}
                </a>

            </td>
            <td>
                {{relatedItem.material.partName}}
            </td>
            <td>
                {{relatedItem.material.mfrPartType.name}}
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu"
                      ng-if="!qcrReleased && hasPermission('qcr','edit')" uib-dropdown
                      dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="qcrRelatedItemsVm.deleteItem(relatedItem)">
                            <a translate>REMOVE_PART</a>
                        </li>
                    </ul>
                    <plugin-table-actions context="object.related" object-value="relatedItem"></plugin-table-actions>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div ng-show="qcrFor == 'PR'">
    <related-items-view object-id="qcrRelatedItemsVm.qcrId" object-type="QCR"
                        has-permission="hasPermission('qcr','edit') && !qcr.released && qcr.statusType != 'REJECTED'">
    </related-items-view>
</div>