<div>
    <style scoped>
        .itemName-column {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            width: 230px;
            vertical-align: bottom;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;">
                    <span uib-dropdown dropdown-append-to-body
                          ng-if="itemConfiguredVm.itemRevision.hasBom && !itemRevision.rejected && lockedObjectPermission">
                            <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                               title="{{itemConfiguredVm.addInstance}}"
                               style="cursor: pointer">
                            </i>
                            <ul uib-dropdown-menu class=" dropdown-menu" role="menu"
                                style="z-index: 9999 !important;max-height: 365px;overflow-y: auto;min-width: 200px;">
                                <li style="font-weight: bold;padding: 7px 10px;" translate>
                                    SELECT_BOM_CONFIGURATION
                                </li>
                                <li style="padding: 7px 10px;" ng-if="itemConfiguredVm.bomConfigurations.length == 0"
                                    translate>
                                    No BOM configurations
                                </li>
                                <li ng-repeat="bomConfiguration in itemConfiguredVm.bomConfigurations">
                                    <a href ng-click="itemConfiguredVm.onSelectBomConfiguration(bomConfiguration)">{{bomConfiguration.name}}</a>
                                </li>
                            </ul>
                    </span>
                    <i class="la la-plus"
                       ng-if="!itemConfiguredVm.itemRevision.hasBom && !itemRevision.released && !itemRevision.rejected && lockedObjectPermission"
                       title={{itemConfiguredVm.addInstance}}
                       style="cursor: pointer"
                       ng-click="itemConfiguredVm.createInstanceItem()">
                    </i>
                </th>
                <th ng-if="itemConfiguredVm.itemRevision.hasBom" translate>BOM_CONFIGURATION</th>
                <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                <th translate>ITEM_NAME</th>
                <th translate>DESCRIPTION</th>
                <th class='added-column'
                    ng-repeat="selectedAttribute in itemConfiguredVm.attributes">
                    <span class="itemName-column" title="{{selectedAttribute.attributeDef.name}}">{{selectedAttribute.attributeDef.name}}</span>
                </th>
                <th class="text-center" style="width: 70px;">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" style="font-size: 16px;cursor: pointer;padding: 5px;"
                       ng-if="item.configurable && combinations.length > 0"
                       ng-click="createAllInstance()" title="{{saveCombination}}"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-if="combinations.length > 0"
                       ng-click="itemConfiguredVm.removeAllCombinations()"
                       title="{{itemConfiguredVm.removeCombinations}}"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemConfiguredVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_ITEMS</span>
                    </span>
                </td>
            </tr>

            <tr ng-if="itemConfiguredVm.loading == false && (itemConfiguredVm.items.length == 0 && combinations.length == 0)">
                <td colspan="11"><span translate>NO_ITEMS</span></td>
            </tr>
            <tr colspan="12" ng-repeat="itemInstance in combinations">
                <td style="width: 20px;"></td>
                <td ng-if="itemConfiguredVm.itemRevision.hasBom">
                    <span class="itemName-column" title="{{itemInstance.configurationName}}">{{itemInstance.configurationName}}</span>
                </td>
                <td style="width: 1% !important;white-space: nowrap;">
                    <div class="row">
                        <span ng-if="itemInstance.editMode == false">{{itemInstance.itemNumber}}</span>
                        <input ng-if="itemInstance.editMode" placeholder="Enter Number" class="form-control input-sm"
                               type="text" style="width: 150px; display: inline-block"
                               ng-model="itemInstance.itemNumber">
                    </div>
                </td>
                <td class="col-width-200">
                    <div class="row" style="padding-right: 8px;">
                        <span ng-if="itemInstance.editMode == false">{{itemInstance.itemName}}</span>
                        <input ng-if="itemInstance.editMode" placeholder="Enter Name" class="form-control input-sm"
                               type="text" style="width: 100%; display: inline-block"
                               ng-model="itemInstance.itemName">
                    </div>
                </td>
                <td class="col-width-250">
                    <div class="row" style="padding-right: 8px;">
                        <span ng-if="itemInstance.editMode == false">{{itemInstance.description}}</span>
                        <input ng-if="itemInstance.editMode" placeholder="{{'DESCRIPTION' | translate}}"
                               class="form-control input-sm"
                               type="text" style="width: 100%; display: inline-block"
                               ng-model="itemInstance.description">
                    </div>
                </td>
                <td ng-repeat="objectAttribute in itemInstance.itemAttributes">
                    <span ng-if="!itemInstance.isNew" class="itemName-column">
                          {{objectAttribute.listValue}}
                    </span>
                    <select ng-if="itemInstance.isNew" class="form-control" ng-model="objectAttribute.listValue"
                            placeholder="select" style="width: 100%;"
                            ng-options="value for value in objectAttribute.lovValues">
                    </select>
                </td>
                <td class="text-center" style="width: 70px;">
                    <span ng-if="itemInstance.editMode">
                        <button style="margin-top: 5px;" title="{{itemConfiguredVm.save}}"
                                class="btn btn-xs btn-success"
                                type="button" ng-click="itemConfiguredVm.create(itemInstance)"><i
                                class="fa fa-check"></i>
                        </button>
                        <button style="margin-top: 5px;" title="{{itemConfiguredVm.cancel}}"
                                ng-click="itemConfiguredVm.cancelRow(itemInstance);"
                                class="btn btn-xs btn-danger">
                            <i class="fa fa-times"></i>
                        </button>
                    </span>
                </td>
            </tr>
            <tr ng-repeat="item in itemConfiguredVm.items">
                <td style="width: 20px;"></td>
                <td ng-if="itemConfiguredVm.itemRevision.hasBom">
                    <span class="itemName-column" title="{{item.configurationName}}">{{item.configurationName}}</span>
                </td>
                <td style="width: 1% !important;white-space: nowrap;">
                    <a href="" ng-click="itemConfiguredVm.showItem(item)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                    </a>
                </td>
                <td class="col-width-200">
                    <span title="{{item.itemName}}">{{item.itemName}}</span>
                </td>
                <td class="col-width-250"><span title="{{item.description}}">{{item.description}}</span>
                </td>
                <td ng-repeat="objectAttribute in itemConfiguredVm.attributes">
                    <span ng-if="objectAttribute.attributeDef.dataType == 'LIST'" class="itemName-column"
                          title="{{item[attrName]}}"
                          ng-init="attrName = objectAttribute.attributeDef.id">
                        {{item[attrName]}}</span>
                </td>
                <td class="text-center" style="width: 70px;">
                    <button class="btn btn-xs btn-danger" ng-click="itemConfiguredVm.deleteItem(item)"
                            ng-hide="itemRevision.released || itemRevision.rejected || !lockedObjectPermission"
                            title="{{itemConfiguredVm.removeItem}}">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>