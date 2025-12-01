<style scoped>
    .center {
        display: block;
        margin-left: auto;
        margin-right: auto;
        margin-top: 4%;
        width: 300px;
    }

    .no-conversations .no-conversations-message {
        font-size: 20px;
        font-weight: 300 !important;
        text-align: center;
    }

    .rel-table-title {
        font-size: 16px;
        color: #2f6696;
    }

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
        /*background-color: #fff;*/
    }

    .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
        background-color: #d6e1e0;
    }

    .related-item {
        padding: 5px 10px 15px 10px;
    }

    .related-item:last-child {
        padding: 5px 10px 5px 10px;
    }

    .related-item .responsive-table {
        position: relative !important;
    }

    .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
        position: absolute !important;
        display: contents !important;
    }
</style>
<div class='responsive-table' ng-if="item.itemType.itemClass != 'PART'">
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 30px;"
                ng-if="!itemRevision.released && !itemRevision.rejected && external.external != true && relatedItemCreatePermission"
                ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission">
                <i class="la la-plus"
                   title={{relatedItemsVm.addMfrParts}} style="cursor: pointer"
                   ng-click="createRelatedItem()"></i>
            </th>
            <th style="width: 30px;"
                ng-if="!itemRevision.released && !itemRevision.rejected && sharedPermission == 'WRITE' && external.external == true && relatedItemCreatePermission"
                ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission">
                <i class="la la-plus"
                   title={{relatedItemsVm.addMfrParts}} style="cursor: pointer"
                   ng-click="createRelatedItem()"></i>
            </th>
            <th style="width: 200px;" translate>RELATIONSHIP</th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
            <th class="col-width-250" style="width: 200px;" translate>ITEM_NAME</th>
            <th style="width: 200px;" translate>LIFE_CYCLE_PHASE</th>
            <th style="width: 200px;text-align: center" translate>REVISION</th>
            <th style="width: 200px;" translate>ATTRIBUTES</th>
            <th style="width: 100px;text-align: center" translate>ACTIONS
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="relatedItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="relatedItemsVm.loading == false && relatedItemsVm.relatedItems.length == 0">
            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/relatedItems.png" alt="" class="image">

                    <div class="message">{{noRelatedItems}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-if="relatedItemsVm.relatedItems.length > 0"
            ng-repeat="relatedItem in relatedItemsVm.relatedItems">
            <td style="width: 30px;"
                ng-if="!itemRevision.released && !itemRevision.rejected && external.external != true && relatedItemCreatePermission"
                ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission"></td>
            <td style="width: 30px;"
                ng-if="!itemRevision.released && !itemRevision.rejected && sharedPermission == 'WRITE' && external.external == true && relatedItemCreatePermission"
                ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission">

            </td>
            <td>{{relatedItem.relationship.name}}</td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a href="" ng-click="relatedItemsVm.showItem(relatedItem.fromItemRevision.id)"
                   ng-if="relatedItem.fromItemRevision.id != itemRevision.id"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{relatedItem.fromItemNumber}}</a>

                <a href="" ng-click="relatedItemsVm.showItem(relatedItem.toItemRevision.id)"
                   ng-if="relatedItem.fromItemRevision.id == itemRevision.id"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{relatedItem.toItemNumber}}</a>
            </td>
            <td class="col-width-250">
                <span ng-if="relatedItem.fromItemRevision.id == itemRevision.id">{{relatedItem.toItemName}}</span>
                <span ng-if="relatedItem.fromItemRevision.id != itemRevision.id">{{relatedItem.fromItemName}}</span>
            </td>
            <td>
                <item-status ng-if="relatedItem.fromItemRevision.id == itemRevision.id"
                             item="relatedItem.toItemRevision"></item-status>
                <item-status ng-if="relatedItem.fromItemRevision.id != itemRevision.id"
                             item="relatedItem.fromItemRevision"></item-status>
            </td>
            <td style="width: 200px;text-align: center">
                <span ng-if="relatedItem.fromItemRevision.id == itemRevision.id">{{relatedItem.toItemRevision.revision}}</span>
                <span ng-if="relatedItem.fromItemRevision.id != itemRevision.id">{{relatedItem.fromItemRevision.revision}}</span>
            </td>
            <td>
                <button class="btn btn-xs btn-primary" title="{{relatedItemsVm.showAttributesTitle}}"
                        ng-click="relatedItemsVm.showRelatedItemAttributes(relatedItem)" translate>ATTRIBUTES
                </button>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                      ng-if="relatedItem.fromItemRevision.id == itemRevision.id"
                      ng-hide="external.external == true && sharedPermission == 'READ'">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="external.external == false"
                            ng-class="{'cursor-override': !relatedItemDeletePermission}"
                            title="{{relatedItemDeletePermission ? '' : noPermission}}">
                            <a ng-click="relatedItemsVm.deleteRelatedItem(relatedItem)"
                               ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' ||
                               (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id || !relatedItemDeletePermission)}"
                               href="" translate>DELETE_RELATEDITEM</a>
                        </li>
                        <li ng-if="external.external == true && sharedPermission == 'WRITE'"
                            ng-class="{'cursor-override': !relatedItemDeletePermission}"
                            title="{{relatedItemDeletePermission ? '' : noPermission}}">
                            <a ng-click="relatedItemsVm.deleteRelatedItem(relatedItem)"
                               ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' ||
                               (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id || !relatedItemDeletePermission)}"
                               href="" translate>DELETE_RELATEDITEM</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div ng-if="item.itemType.itemClass == 'PART'">
    <div class="related-item">
        <h5>
            <i class="la la-plus" title={{relatedItemsVm.addMfrParts}} style="cursor: pointer"
               ng-click="createRelatedItem()"
               ng-if="!itemRevision.released && !itemRevision.rejected && external.external != true"
               ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission"></i>
            <span class="rel-table-title" translate>RELATIONSHIPS</span>
        </h5>

        <div class="responsive-table">
            <table class='table table-striped highlight-row' style="margin: 0;">
                <thead>
                <tr>
                    <th style="width: 30px;"
                        ng-if="!itemRevision.released && !itemRevision.rejected && sharedPermission == 'WRITE' && external.external == true"
                        ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission">
                        <i class="la la-plus"
                           title={{relatedItemsVm.addMfrParts}} style="cursor: pointer"
                           ng-click="createRelatedItem()"></i>
                    </th>
                    <th class="col-width-200" translate>RELATIONSHIP</th>
                    <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                    <th class="col-width-250" translate>ITEM_NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px;text-align: center" translate>REVISION</th>
                    <th class="col-width-200" translate>LIFE_CYCLE_PHASE</th>
                    <th class="col-width-150" translate>ATTRIBUTES</th>
                    <th class="actions-col sticky-col sticky-actions-col">
                        <span translate>ACTIONS</span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="relatedItemsVm.loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
                    </td>
                </tr>
                <tr ng-if="relatedItemsVm.loading == false && relatedItemsVm.relatedItems.length == 0">
                    <td colspan="12">
                        {{noRelatedItems}}
                    </td>
                </tr>
                <tr ng-if="relatedItemsVm.relatedItems.length > 0"
                    ng-repeat="relatedItem in relatedItemsVm.relatedItems">
                    <td style="width: 30px;"
                        ng-if="!itemRevision.released && !itemRevision.rejected && sharedPermission == 'WRITE' && external.external == true"
                        ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission">

                    </td>
                    <td class="col-width-200">{{relatedItem.relationship.name}}</td>
                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="relatedItemsVm.showItem(relatedItem.fromItemRevision.id)"
                           ng-if="relatedItem.fromItemRevision.id != itemRevision.id"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{relatedItem.fromItemNumber}}</a>

                        <a href="" ng-click="relatedItemsVm.showItem(relatedItem.toItemRevision.id)"
                           ng-if="relatedItem.fromItemRevision.id == itemRevision.id"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{relatedItem.toItemNumber}}</a>
                    </td>
                    <td class="col-width-250">
                        <span ng-if="relatedItem.fromItemRevision.id == itemRevision.id">{{relatedItem.toItemName}}</span>
                        <span ng-if="relatedItem.fromItemRevision.id != itemRevision.id">{{relatedItem.fromItemName}}</span>
                    </td>
                    <td class="col-width-250">
                        <span ng-if="relatedItem.fromItemRevision.id == itemRevision.id">{{relatedItem.toItemDescription}}</span>
                        <span ng-if="relatedItem.fromItemRevision.id != itemRevision.id">{{relatedItem.fromItemDescription}}</span>
                    </td>
                    <td style="width: 150px;text-align: center">
                        <span ng-if="relatedItem.fromItemRevision.id == itemRevision.id">{{relatedItem.toItemRevision.revision}}</span>
                        <span ng-if="relatedItem.fromItemRevision.id != itemRevision.id">{{relatedItem.fromItemRevision.revision}}</span>
                    </td>
                    <td class="col-width-200">
                        <item-status ng-if="relatedItem.fromItemRevision.id == itemRevision.id"
                                     item="relatedItem.toItemRevision"></item-status>
                        <item-status ng-if="relatedItem.fromItemRevision.id != itemRevision.id"
                                     item="relatedItem.fromItemRevision"></item-status>
                    </td>
                    <td class="col-width-150">
                        <button class="btn btn-xs btn-primary" title="{{relatedItemsVm.showAttributesTitle}}"
                                ng-click="relatedItemsVm.showRelatedItemAttributes(relatedItem)" translate>ATTRIBUTES
                        </button>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                      ng-if="relatedItem.fromItemRevision.id == itemRevision.id"
                      ng-hide="external.external == true && sharedPermission == 'READ'">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="external.external == false && deleteItemPermission"
                            ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}"
                            ng-click="relatedItemsVm.deleteRelatedItem(relatedItem)">
                            <a href="" translate>DELETE_RELATEDITEM</a>
                        </li>
                        <li ng-if="external.external == true && sharedPermission == 'WRITE'"
                            ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}"
                            ng-click="relatedItemsVm.deleteRelatedItem(relatedItem)">
                            <a href="" translate>DELETE_RELATEDITEM</a>
                        </li>
                        <plugin-table-actions context="item.related" object-value="relatedItem"></plugin-table-actions>
                    </ul>
                </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="related-item">
        <h5>
            <i class="la la-plus"
               title="Add Alternate Parts" style="cursor: pointer"
               ng-click="relatedItemsVm.addAlternateParts()"
               ng-if="!itemRevision.released && !itemRevision.rejected && !external.external"
               ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission"></i>
            <span class="rel-table-title" translate>ALTERNATE_PARTS</span>
        </h5>

        <div class="responsive-table">
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th style="width: 30px;"
                        ng-if="!itemRevision.released && !itemRevision.rejected && sharedPermission == 'WRITE' && external.external == true"
                        ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission">
                        <i class="la la-plus"
                           title="Add Alternate Parts" style="cursor: pointer"
                           ng-click="relatedItemsVm.addAlternateParts()"></i>
                    </th>
                    <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                    <th class="col-width-150" translate>ITEM_TYPE</th>
                    <th class="col-width-250" translate>ITEM_NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px;text-align: center" translate>REVISION</th>
                    <th class="col-width-200" translate>LIFE_CYCLE_PHASE</th>
                    <th class="col-width-150" translate>DIRECTION</th>
                    <th class="actions-col sticky-col sticky-actions-col">
                        <span translate>ACTIONS</span>
                        <i class="fa fa-check-circle" ng-click="relatedItemsVm.saveAlternateParts()"
                           ng-if="relatedItemsVm.selectedAlternateParts.length > 1"
                           title="Save"
                           style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                        <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                           ng-click="relatedItemsVm.removeAllAlternateParts()" title="Remove"
                           ng-if="relatedItemsVm.selectedAlternateParts.length > 1"></i>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="relatedItemsVm.loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_ALTERNATE_PARTS</span>
                    </td>
                </tr>
                <tr ng-if="relatedItemsVm.loading == false && relatedItemsVm.alternateParts.length == 0">
                    <td colspan="12">
                        {{ 'NO_ALTERNATE_PARTS' | translate}}
                    </td>
                </tr>
                <tr ng-if="relatedItemsVm.alternateParts.length > 0"
                    ng-repeat="alternatePart in relatedItemsVm.alternateParts">
                    <td style="width: 30px;"
                        ng-if="!itemRevision.released && !itemRevision.rejected && sharedPermission == 'WRITE' && external.external == true"
                        ng-hide="selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission">

                    </td>
                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="relatedItemsVm.showItem(alternatePart.replacementPartRevision)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{alternatePart.replacementPartNumber}}</a>
                    </td>
                    <td class="col-width-150">{{alternatePart.replacementPartType}}</td>
                    <td class="col-width-250">{{alternatePart.replacementPartName}}</td>
                    <td class="col-width-250">{{alternatePart.replacementPartDescription}}</td>
                    <td style="width: 150px;text-align: center">
                        <span>{{alternatePart.replacementRevision}}</span>
                    </td>
                    <td class="col-width-200">
                        <item-status item="alternatePart"></item-status>
                    </td>
                    <td class="col-width-150">
                        <form>
                       <span ng-if="alternatePart.direction == 'ONEWAY' && !alternatePart.editMode"
                             class="label label-primary label-outline bg-light-primary ">{{alternatePart.direction}}</span>
                    <span ng-if="alternatePart.direction == 'TWOWAY' && !alternatePart.editMode"
                          class="label label-success label-outline bg-light-success">{{alternatePart.direction}}</span>
                            <ui-select ng-model="alternatePart.direction" theme="bootstrap" style="width:150px;"
                                       ng-if="alternatePart.editMode">
                                <ui-select-match placeholder="Select Direction">{{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="direction in relatedItemsVm.directions | filter: $select.search">
                                    <div ng-bind="direction"></div>
                                </ui-select-choices>
                            </ui-select>
                        </form>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                    <span class="btn-group" ng-if="alternatePart.editMode == true" style="margin: -1px">
                        <i ng-if="alternatePart.isNew"
                           title="{{ 'SAVE' | translate}}"
                           ng-click="relatedItemsVm.saveAlternatePart(alternatePart)"
                           class="la la-check">
                        </i>
                        <i ng-if="!alternatePart.isNew"
                           title="{{ 'SAVE' | translate}}"
                           ng-click="relatedItemsVm.updateAlternatePart(alternatePart)"
                           class="la la-check">
                        </i>
                        <i title="Remove" ng-if="alternatePart.isNew"
                           ng-click="relatedItemsVm.removeAlternatePart(alternatePart)"
                           class="la la-times">
                        </i>
                        <i ng-if="!alternatePart.isNew"
                           title="{{ 'CANCEL' | translate}}"
                           ng-click="relatedItemsVm.cancelChanges(alternatePart)"
                           class="la la-times">
                        </i>
                    </span>
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                          ng-if="!alternatePart.editMode"
                          ng-hide="external.external == true && sharedPermission == 'READ'">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="external.external == false && deleteItemPermission"
                            ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}"
                            ng-click="relatedItemsVm.editAlternatePart(alternatePart)">
                            <a href="" translate>EDIT_PART</a>
                        </li>
                        <li ng-if="external.external == false && deleteItemPermission"
                            ng-click="relatedItemsVm.deleteAlternatePart(alternatePart)">
                            <a href="" ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}" translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="item.related" object-value="alternatePart"></plugin-table-actions>
                    </ul>
                    </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="related-item">
        <h5 class="rel-table-title" translate>SUBSTITUTE_PARTS</h5>

        <div class="responsive-table">
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th style="width: 1% !important;white-space: nowrap;" translate>PARENT_ITEM_NUMBER</th>
                    <th class="col-width-200" translate>PARENT_ITEM_NAME</th>
                    <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                    <th class="col-width-150" translate>ITEM_TYPE</th>
                    <th class="col-width-200" translate>ITEM_NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px;text-align: center" translate>REVISION</th>
                    <th class="col-width-200" translate>LIFE_CYCLE_PHASE</th>
                    <th class="actions-col sticky-col sticky-actions-col" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="relatedItemsVm.loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_SUBSTITUTE_PARTS</span>
                    </td>
                </tr>
                <tr ng-if="relatedItemsVm.loading == false && relatedItemsVm.substituteParts.length == 0">
                    <td colspan="12">
                        {{ 'NO_SUBSTITUTE_PARTS' | translate}}
                    </td>
                </tr>
                <tr ng-if="relatedItemsVm.substituteParts.length > 0"
                    ng-repeat="substitutePart in relatedItemsVm.substituteParts">
                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="relatedItemsVm.showItem(substitutePart.parentRevision)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{substitutePart.parentItemNumber}}</a>
                    </td>
                    <td class="col-width-200">{{substitutePart.parentItemName}}</td>
                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="relatedItemsVm.showItem(substitutePart.replacementPartRevision)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{substitutePart.replacementPartNumber}}</a>
                    </td>
                    <td class="col-width-150">{{substitutePart.replacementPartType}}</td>
                    <td class="col-width-200">{{substitutePart.replacementPartName}}</td>
                    <td class="col-width-250">{{substitutePart.replacementPartDescription}}</td>
                    <td style="text-align: center;">
                        {{substitutePart.replacementRevision}}
                    </td>
                    <td class="col-width-200">
                        <item-status item="substitutePart"></item-status>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                      ng-hide="external.external == true && sharedPermission == 'READ'">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="external.external == false && deleteItemPermission"
                            ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}"
                            ng-click="relatedItemsVm.deleteSubstitutePart(substitutePart)">
                            <a href="" translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="item.related" object-value="substitutePart"></plugin-table-actions>
                    </ul>
                </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>