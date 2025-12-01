<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;"
                ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'">
                <i class="la la-plus dropdown-toggle" ng-click="mcoRelatedItemsVm.addRelatedItems()"
                   title="{{addRelatedItemTitle}}" style="cursor: pointer"></i>
            </th>
            <th class="" translate>PART_NUMBER</th>
            <th style="width: 150px" translate>PART_NAME</th>
            <th translate>MANUFACTURER_PART_TYPE</th>
            <th translate>NOTES</th>
            <th style="width: 100px; text-align: center">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="mcoRelatedItemsVm.saveAll()"
                   ng-if="mcoRelatedItemsVm.selectedParts.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-click="mcoRelatedItemsVm.removeAll()" title="Remove"
                   ng-if="mcoRelatedItemsVm.selectedParts.length > 1"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="mcoRelatedItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="mcoRelatedItemsVm.loading == false && mcoRelatedItemsVm.relatedItems.length == 0">
            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/relatedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_RELATED_ITEMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="relatedItem in mcoRelatedItemsVm.relatedItems">
            <td ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'"></td>
            <td>
                <a title="{{clickToShowDetails}}" ng-click="showPart()"
                   ui-sref="app.mfr.mfrparts.details({mfrId:relatedItem.part.manufacturer,manufacturePartId:relatedItem.part.id})">
                    {{relatedItem.part.partNumber}}
                </a>
            </td>
            <td>
                {{relatedItem.part.partName}}
            </td>
            <td>
                {{relatedItem.part.mfrPartType.name}}
            </td>
            <td class="description-column">
                <span ng-if="!relatedItem.editMode">{{relatedItem.notes}}</span>

                <form>
                    <input ng-if="relatedItem.editMode" class="form-control" ng-model="relatedItem.notes"/>
                </form>
            </td>
            <td class="text-center">
                  <span class="btn-group"
                        ng-if="relatedItem.editMode == true"
                        style="margin: 0">
                    <i ng-show="relatedItem.isNew == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="mcoRelatedItemsVm.save(relatedItem)"
                       class="la la-check">
                    </i>
                    <i ng-show="relatedItem.isNew == true"
                       title="{{cancelChangesTitle}}"
                       ng-click="mcoRelatedItemsVm.onCancel(relatedItem)"
                       class="la la-times">
                    </i>
                      <i ng-show="relatedItem.isNew == false"
                         title="{{'SAVE' | translate}}"
                         ng-click="mcoRelatedItemsVm.updateItem(relatedItem)">
                          <i class="la la-check"></i>
                      </i>
                     <i ng-show="relatedItem.isNew == false"
                        title="{{cancelChangesTitle}}"
                        ng-click="mcoRelatedItemsVm.cancelChanges(relatedItem)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="relatedItem.editMode == false && (hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="relatedItem.editMode == false"
                            ng-click="mcoRelatedItemsVm.editItem(relatedItem)">
                            <a translate>EDIT_ITEM</a>
                        </li>
                        <li ng-if="relatedItem.editMode == false"
                            ng-click="mcoRelatedItemsVm.deleteItem(relatedItem)">
                            <a translate>REMOVE_ITEM</a>
                        </li>
                        <plugin-table-actions context="mco.related" object-value="relatedItem"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>