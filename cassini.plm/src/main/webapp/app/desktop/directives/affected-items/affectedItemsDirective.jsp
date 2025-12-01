<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;"
                ng-if="hasPermission">
                <i class="la la-plus dropdown-toggle" ng-click="addAffectedItems()"
                   title="{{addAffectedItemTitle}}"
                   style="cursor: pointer"></i>
            </th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
            <th class="col-width-150" translate>ITEM_TYPE</th>
            <th class="col-width-200" translate>ITEM_NAME</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th ng-if="objectType == 'QCR' || objectType == 'ECR'" translate>PROBLEM_REPORT</th>
            <th style="width: 150px" translate>LIFECYCLE</th>
            <th style="text-align: center" translate>REVISION</th>
            <th class="col-width-250" translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="saveAll()"
                   ng-if="selectedItems.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-click="removeAll()" title="Remove"
                   ng-if="selectedItems.length > 1"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="loading == true">
            <td colspan="15">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                        <span translate>LOADING_AFFECTIVE_ITEM</span>
                    </span>
            </td>
        </tr>
        <tr ng-if="loading == false && affectedItems.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/affectedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_ITEMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-if="loading == false" ng-repeat="affectedItem in affectedItems track by $index">
            <td ng-if="hasPermission"></td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a ui-sref="app.items.details({itemId: affectedItem.item})"
                   title="{{clickToShowDetails}}">{{affectedItem.itemNumber}}
                </a>
            </td>
            <td class="col-width-150">
                <span>{{affectedItem.itemType}}</span>
            </td>
            <td class="col-width-200" title="{{affectedItem.itemName}}">
                {{affectedItem.itemName}}
            </td>
            <td class="col-width-250" title="{{affectedItem.description}}">
                {{affectedItem.description}}
            </td>
            <td ng-if="objectType == 'QCR'">
                <a href=""
                   ui-sref="app.pqm.pr.details({problemReportId:affectedItem.problemReport})">{{affectedItem.prNumber}}</a>
            </td>
            <td ng-if="objectType == 'ECR'">
                <span ng-repeat="problemReport in affectedItem.problemReportList">
                    <a href="" ui-sref="app.pqm.pr.details({problemReportId:problemReport.id})"
                       title="{{clickToShowDetails}}">{{problemReport.prNumber}}<span
                            ng-if="($index + 1) < affectedItem.problemReportList.length"> , </span></a>
                </span>
            </td>
            <td>
                <item-status item="affectedItem"></item-status>
            </td>
            <td style="text-align: center">{{affectedItem.revision}}</td>
            <td class="col-width-250">
                <span ng-if="affectedItem.editMode == true">
                   <form>
                       <input type="text" class="form-control" style="width: 150px;" ng-model="affectedItem.notes"/>
                   </form>
                </span>
                <span ng-if="affectedItem.editMode == false">
                    {{affectedItem.notes}}
                </span>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                 <span class="btn-group" ng-if="affectedItem.editMode == true" style="margin: 0">
                    <i ng-show="affectedItem.isNew == true"
                       title="{{ 'SAVE' | translate }}"
                       ng-click="save(affectedItem)" class="la la-check">
                    </i>
                    <i ng-show="affectedItem.isNew == true"
                       title="{{ 'CANCEL' | translate }}"
                       ng-click="onCancel(affectedItem)"
                       class="la la-times">
                    </i>
                      <i ng-show="affectedItem.isNew == false"
                         title="{{ 'SAVE' | translate }}"
                         ng-click="updateItem(affectedItem)"
                         class="la la-check">
                      </i>
                     <i ng-show="affectedItem.isNew == false"
                        title="{{ 'CANCEL' | translate }}"
                        ng-click="cancelChanges(affectedItem)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="affectedItem.editMode == false && hasPermission"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="editItem(affectedItem)">
                            <a translate>EDIT_ITEM</a>
                        </li>
                        <li id="removeAffectedItem" ng-click="deleteAffectedItem(affectedItem)"
                            ng-class="{'disabled':affectedItem.disableDelete}">
                            <a translate>REMOVE_ITEM</a>
                        </li>
                        <plugin-table-actions context="object.affected" object-value="affectedItem"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>