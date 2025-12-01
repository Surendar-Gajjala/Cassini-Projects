<div  ng-if="inspection.objectType == 'MATERIALINSPECTION'" class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 30px;"
                ng-if="hasPermission('inspection','edit') && !inspection.released && inspection.statusType != 'REJECTED'">
                <i class="la la-plus" style="cursor: pointer" title="{{addRelatedItemTitle}}"
                   ng-if="inspection.objectType == 'MATERIALINSPECTION'"
                   ng-click="inspectionRelatedItemsVm.addMaterialRelatedItems()"></i>
            </th>
            <th style="width: 200px;" translate>PART_NUMBER</th>
            <th style="width: 200px;" translate>MANUFACTURER_PART_TYPE</th>
            <th class="col-width-250" style="width: 300px;" translate>PART_NAME</th>
            <th class="col-width-250" style="width: 300px;" translate>DESCRIPTION</th>
            <th class="col-width-250" style="width: 300px;" translate>MANUFACTURER</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                ACTIONS
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="inspectionRelatedItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="inspectionRelatedItemsVm.loading == false && inspectionRelatedItemsVm.relatedItems.length == 0">
            <td colspan="12"><span translate>NO_RELATED_ITEMS</span></td>
        </tr>
        <tr ng-repeat="relatedItem in inspectionRelatedItemsVm.relatedItems">
            <td ng-if="hasPermission('inspection','edit') && !inspection.released && inspection.statusType != 'REJECTED'"></td>
            <td>
                <a ui-sref="app.mfr.mfrparts.details({mfrId:relatedItem.manufacturer,manufacturePartId:relatedItem.material})"
                   ng-click="showRelatedItem()"
                   title="{{clickToShowDetails}}">{{relatedItem.partNumber}}
                </a>
            </td>
            <td>{{relatedItem.partType}}</td>
            <td>
                <span title="{{relatedItem.partName}}">
                    {{relatedItem.partName}}
                </span>
            </td>
            <td title="{{relatedItem.description}}">
                {{relatedItem.description}}
            </td>
            <td  style="width: 150px">
                {{relatedItem.manufacturerName}}
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu"
                      ng-if="hasPermission('inspection','edit') && !inspection.released && inspection.statusType != 'REJECTED'"
                      uib-dropdown dropdown-append-to-body>
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="inspectionRelatedItemsVm.deleteItem(relatedItem)">
                            <a translate>REMOVE_ITEM</a>
                        </li>
                    </ul>
                    <plugin-table-actions context="object.related" object-value="relatedItem"></plugin-table-actions>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div ng-if="inspection.objectType == 'ITEMINSPECTION'">
    <related-items-view object-id="inspectionRelatedItemsVm.inspectionId" object-type="ITEMINSPECTION"
                        has-permission="hasPermission('inspection','edit') && !inspection.released && inspection.statusType != 'REJECTED'">
    </related-items-view>
</div>