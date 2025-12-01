<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;"
                ng-if="hasPermission('ncr','edit') && !ncr.released && ncr.statusType != 'REJECTED'">
                <i class="la la-plus dropdown-toggle" ng-click="ncrRelatedItemsVm.addRelatedItems()"
                   title="{{addRelatedItemTitle}}" style="cursor: pointer"></i>
            </th>
            <th class="" translate>PART_NUMBER</th>
            <th style="width: 150px" translate>PART_NAME</th>
            <th translate>MANUFACTURER_PART_TYPE</th>
            <th translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-if="ncrRelatedItemsVm.selectedParts.length > 0"
                   ng-click="ncrRelatedItemsVm.saveAll()"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="ncrRelatedItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="ncrRelatedItemsVm.loading == false && ncrRelatedItemsVm.relatedItems.length == 0">

            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/relatedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_RELATED_ITEMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="relatedItem in ncrRelatedItemsVm.relatedItems">
            <td ng-if="hasPermission('ncr','edit') && !ncr.released && ncr.statusType != 'REJECTED'"></td>
            <td>
                <a title="{{clickToShowDetais}}" ng-click="showRelatedItem()"
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
            <td class="description-column">
                <span ng-if="!relatedItem.editMode">{{relatedItem.notes}}</span>

                <form>
                    <input ng-if="relatedItem.editMode" class="form-control" ng-model="relatedItem.notes"/>
                </form>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                 <span class="btn-group" ng-if="relatedItem.editMode == true" style="margin: 0">
                    <i ng-show="relatedItem.isNew == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="ncrRelatedItemsVm.save(relatedItem)"
                       class="la la-check">
                    </i>
                    <i ng-show="relatedItem.isNew == true"
                       title="{{'CANCEL' | translate}}"
                       ng-click="ncrRelatedItemsVm.onCancel(relatedItem)"
                       class="la la-times">
                    </i>
                     <i ng-show="relatedItem.isNew == false"
                        title="{{'SAVE' | translate}}"
                        ng-click="ncrRelatedItemsVm.updateItem(relatedItem)"
                        class="la la-check">
                     </i>
                     <i ng-show="relatedItem.isNew == false"
                        title="{{'CANCEL' | translate}}"
                        ng-click="ncrRelatedItemsVm.cancelChanges(relatedItem)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="relatedItem.editMode == false && hasPermission('ncr','edit') && !ncr.released && ncr.statusType != 'REJECTED'"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="relatedItem.editMode == false"
                            ng-click="ncrRelatedItemsVm.editItem(relatedItem)">
                            <a translate>EDIT_PART</a>
                        </li>
                        <li ng-if="relatedItem.editMode == false"
                            ng-click="ncrRelatedItemsVm.deleteItem(relatedItem)">
                            <a translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="ncr.related" object-value="relatedItem"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>