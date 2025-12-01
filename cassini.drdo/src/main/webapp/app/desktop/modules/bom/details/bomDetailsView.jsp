<style>
    .popover {
        min-width: 450px !important;
        max-width: 450px !important;
    }

    .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .ui-select-bootstrap > .ui-select-match > .btn {
        width: 100% !important;
    }

    table .ui-select-choices {
        position: absolute !important;
    }

    /*tr.disabledItem {
        color: #bfa2a2 !important;
    }

    tr.disabledItem td {
        color: #bfa2a2 !important;
    }

    tr.disabledItem td a {
        color: #bfa2a2 !important;
    }*/
</style>
<div class="responsive-table" style="padding: 10px;" ng-if="bomDetailsVm.showBomItems == true">
    <table class="table table-striped highlight-row" style="margin-bottom: 20px;">
        <thead>
        <tr>
            <th ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE' || !hasPermission('permission.bom.add')"
                style="width: 24px;"></th>
            <th ng-if="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'"
                style="width: 24px;"></th>
            <th class="threeHundred-column">Nomenclature</th>
            <%--<th style="width: 100px;">Item Code</th>--%>
            <th class="oneFifty-column">Type</th>
            <th class="oneTwenty-column" ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">Code</th>
            <th class="hundred-column" style="text-align: center;">Qty</th>
            <th class="hundred-column">Units</th>
            <th class="hundred-column">Work Center</th>
            <th class="hundred-column">Revision</th>
            <th class="oneFifty-column">Drawing Number</th>
            <th class="oneFifty-column" ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">Material</th>
            <%-- <th class="auto-column" ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">Description
             </th>--%>
            <%----------------------   MBOM Columns  ----------------------%>
            <th ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'"
                style="text-align: center;">UPN
            </th>
            <th ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">Status</th>
            <th ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">Percentage
            </th>

            <th style="width: 70px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="bomDetailsVm.selectedBom.objectType == 'BOM'">
            <td style="background: none !important;"
                ng-if="hasPermission('permission.bom.add')">
                <i class="fa fa-plus-circle" style="color: darkblue;"
                   ng-click="bomDetailsVm.createBomGroupType(bomDetailsVm.selectedBom,'SECTION')"
                   title="Add Section"></i>
            </td>
            <td colspan="25">{{bomDetailsVm.selectedBom.item.itemMaster.itemName}}</td>
        </tr>
        <tr ng-if="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
            <td>
                <i class="fa flaticon-stamp13" ng-click="bomDetailsVm.getInstanceRequestReport()"
                   title="MBOM Request Report"></i>
            </td>
            <td colspan="8">{{bomDetailsVm.selectedBom.item.instanceName}}</td>
            <td>{{bomDetailsVm.selectedBom.status}}</td>
            <td>{{bomDetailsVm.selectedBom.percentage | limitTo: 5}}</td>
            <td style="width: 90px;text-align: center">
                <div class="btn-group"
                     ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                    <%--<button title="Generate TARB Document" class="btn btn-xs btn-primary" style="margin-right: 3px;"
                            ng-click="bomDetailsVm.getInstanceTarbDocument(bomDetailsVm.selectedBom)">
                        <i class="fa fa-file-text"></i>
                    </button>--%>
                    <%--<button ng-show="bomDetailsVm.selectedBom.hasPartTracking == false"
                            style="background-color: #f5ae0e"
                            title="Assign Part Tracking process" class="btn btn-xs btn-info"
                            ng-click="bomDetailsVm.assignPartTracking(bomDetailsVm.selectedBom)">
                        <i class="fa fa-list"></i>
                    </button>
                    <button ng-show="bomDetailsVm.selectedBom.hasPartTracking == true"
                            title="View Part Tracking" class="btn btn-xs btn-success"
                            ng-click="bomDetailsVm.showPartTracking(bomDetailsVm.selectedBom)">
                        <i class="fa fa-eye"></i>
                    </button>--%>
                </div>
            </td>
        </tr>
        <tr ng-if="bomDetailsVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Items...</span>
                        </span>
            </td>
        </tr>
        <tr ng-if="bomDetailsVm.loading == false && bomDetailsVm.bomItems.length == 0">
            <td colspan="25">No Items</td>
        </tr>
        <tr ng-repeat="item in bomDetailsVm.bomItems" ng-class="{'selected': item.selected}">
            <td ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE' || !hasPermission('permission.bom.add')"
                style="background: none !important;">
                <span ng-if="item.bomItemType == 'SECTION' || item.bomItemType == 'COMMONPART'" class="row-menu"
                      uib-dropdown dropdown-append-to-body>
                   <i class="fa fa-plus-circle dropdown-toggle" uib-dropdown-toggle title="Add New"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href="" ng-click="bomDetailsVm.createBomGroupType(item,'SUBSYSTEM')">Add Sub
                                System</a>
                            </li>
                            <li><a href="" ng-click="bomDetailsVm.createBomGroupType(item,'UNIT')">Add Unit</a></li>
                            <li><a href="" ng-show="item.bomItemType == 'COMMONPART'"
                                   ng-click="bomDetailsVm.addNewBomItem(item)">Add Part</a></li>
                        </ul>
               </span>

                <span ng-if="item.bomItemType == 'SUBSYSTEM'" class="row-menu" uib-dropdown dropdown-append-to-body>
                   <i class="fa fa-plus-circle dropdown-toggle" uib-dropdown-toggle title="Add New"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href="" ng-click="bomDetailsVm.createBomGroupType(item,'UNIT')">Add Unit</a></li>
                            <li><a href="" ng-click="bomDetailsVm.addNewBomItem(item)">Add Part</a></li>
                        </ul>
               </span>

                <span ng-if="item.bomItemType == 'UNIT'">
                   <i class="fa fa-plus-circle" ng-click="bomDetailsVm.addNewBomItem(item)" title="Add Part"></i>
               </span>
            </td>
            <td ng-if="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'"
                style="background: none !important;">
                <i ng-if="item.bomItemType == 'SECTION' || item.bomItemType == 'COMMONPART'" class="fa flaticon-stamp13"
                   ng-click="bomDetailsVm.getSectionRequestReport(item)"
                   title="Section Request Report"></i>
            </td>
            <td class="threeHundred-column">
                <p class="level{{item.level}}" ng-if="item.bomItemType != 'PART'"
                   title="{{item.expanded ? 'Collapse':'Expand'}}"
                   ng-click="bomDetailsVm.toggleNode(item)" style="margin:0;">
                    <i ng-if="item.children.length > 0" class="mr5 fa"
                       style="cursor: pointer; color: #909090;font-size: 18px;"
                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                       'fa-caret-down': item.expanded == true}"></i>
                    {{item.typeRef.name}}<span ng-if="item.typeRef.versity"> ( VSPL )</span>
                </p>

                <p class="level{{item.level}}" ng-if="item.bomItemType == 'PART'" style="margin:0;">
                    <a ui-sref="app.items.details({itemId: item.item.id})" title="Click to show Item details">
                        <span ng-class="{'ml20': item.itemRevision.hasBom == false}">
                            <span ng-bind-html="item.item.itemMaster.itemName | highlightText: searchBomText"></span>
                            <span ng-bind-html="item.item.partSpec.specName | highlightText: searchBomText"></span>
                        </span>
                    </a>
                </p>
            </td>
            <td class="oneFifty-column">
                <bom-group-type ng-if="item.bomItemType != 'PART'" object="item.typeRef"></bom-group-type>
                <p style="margin: 0px;">{{item.item.itemMaster.parentType.name}}</p>
            </td>
            <td class="oneTwenty-column" ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                <span ng-if="item.bomItemType == 'PART'">{{item.hierarchicalCode}}</span>
            </td>
            <td class="hundred-column" style="text-align: center;">
                <span class="badge badge-primary"
                      ng-if="item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots && item.editMode == false"
                      style="font-size: 13px;">{{item.quantity}}
                </span>
                <span class="badge badge-primary"
                      ng-if="item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots && item.editMode == false"
                      style="font-size: 13px;">{{item.fractionalQuantity}}
                </span>
                <input ng-if="item.bomItemType == 'PART' && !item.item.hasBom
                              && !item.item.itemMaster.itemType.hasLots && item.editMode == true"
                       ng-model="item.newQuantity" type="number" class="form-control" style="width: 90px;">

                <input ng-if="item.bomItemType == 'PART' && !item.item.hasBom
                              && item.item.itemMaster.itemType.hasLots && item.editMode == true"
                       ng-model="item.newFractionalQuantity" type="number" class="form-control" style="width: 90px;">
            </td>
            <td class="hundred-column">
                <span ng-if="item.bomItemType == 'PART'">
                    {{item.item.itemMaster.itemType.units}}
                </span>
            </td>
            <td class="hundred-column" style="text-align: center;">
                <span ng-if="item.bomItemType == 'PART' && item.editMode == false"
                      style="font-size: 13px;">
                    <span ng-bind-html="item.workCenter | highlightText: searchBomText"></span>
                </span>
                <ui-select ng-if="item.bomItemType == 'PART' && !item.item.hasBom && item.editMode == true"
                           ng-model="item.newWorkCenter" theme="bootstrap" style="width:80%">
                    <ui-select-match placeholder="Select">
                        {{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="center in bomDetailsVm.workCenters track by $index">
                        <div ng-bind="center"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td class="hundred-column" style="text-align: center;">
                <span ng-if="item.bomItemType == 'PART'">
                    {{item.item.revision}}
                </span>
            </td>
            <td class="oneFifty-column">
                {{item.item.drawingNumber}}
            </td>
            <td ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                {{item.item.itemMaster.material}}
            </td>
            <%--<td class="auto-column" style="width: 200px;"
                ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                <span ng-bind-html="item.item.itemMaster.description | highlightText: freeTextQuery"></span>
            </td>--%>


            <%---------------------------   MBOM Columns ---------------------------------%>

            <td class="hundred-column" ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'"
                style="text-align: center;">
                <span>
                    <a ng-if="item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots && item.issuedInstances.length > 0"
                       title="Click to show Issued" style="font-size: 14px;"
                       class="badge badge-success"
                       uib-popover-template="bomDetailsVm.issuedInstancesPopOver.templateUrl"
                       popover-append-to-body="true"
                       popover-popup-delay="50"
                       popover-placement="top-right"
                       popover-title="Issued UPN"
                       popover-trigger="'outsideClick'">{{item.issuedInstances.length}}
                    </a>
                    <a ng-if="item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots && item.lotInstances.length > 0"
                       title="Click to show Issued" style="font-size: 14px;"
                       class="badge badge-success"
                       uib-popover-template="bomDetailsVm.issuedInstancesPopOver.templateUrl"
                       popover-append-to-body="true"
                       popover-popup-delay="50"
                       popover-placement="top-right"
                       popover-title="Issued UPN"
                       popover-trigger="'outsideClick'">{{item.lotInstances.length}}
                    </a>
                </span>
            </td>
            <td class="oneFifty-column" ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                <span ng-if="item.bomItemType == 'SECTION' || item.bomItemType == 'COMMONPART'">{{item.status}}</span>
            </td>
            <td class="hundred-column" ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                <span ng-if="item.bomItemType == 'SECTION' || item.bomItemType == 'COMMONPART'">{{item.percentage | limitTo: 5}}</span> <%--only it shows 5 letters--%>
            </td>

            <td style="width: 90px;text-align: center">
                <div class="btn-group" ng-hide="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                    <button ng-if="hasPermission('permission.admin.all') && (item.bomItemType == 'SECTION' || item.bomItemType == 'COMMONPART')"
                            class="btn btn-xs" ng-click="bomDetailsVm.synchronizeSelectedSection(item)"
                            title="Synchronize Section">
                        <i class="fa fa-refresh"></i>
                    </button>
                    <button ng-if="hasPermission('permission.admin.all') && (item.bomItemType == 'UNIT')"
                            class="btn btn-xs" ng-click="bomDetailsVm.synchronizeSelectedUnit(item)"
                            title="Synchronize Unit">
                        <i class="fa fa-refresh"></i>
                    </button>
                    <button ng-if="item.bomItemType == 'SECTION' || item.bomItemType == 'COMMONPART'"
                            class="btn btn-xs btn-primary" title="Click to Print"
                            ng-click="bomDetailsVm.printSelectedType(item)">
                        <i class="fa fa-print"></i>
                    </button>
                    <button ng-if="item.editMode == true" class="btn btn-xs btn-success" title="Save"
                            ng-click="bomDetailsVm.saveItem(item)">
                        <i class="fa fa-check"></i>
                    </button>
                    <button ng-if="item.editMode == true && item.isNew == true" class="btn btn-xs btn-default"
                            title="Remove Item"
                            ng-click="bomDetailsVm.removeItem(item)">
                        <i class="fa fa-times"></i>
                    </button>
                    <button ng-if="item.editMode == true && item.isNew == false" class="btn btn-xs btn-default"
                            title="Cancel Changes"
                            ng-click="bomDetailsVm.cancelChanges(item)">
                        <i class="fa fa-times"></i>
                    </button>

                    <button ng-if="item.editMode == false && item.bomItemType == 'PART'" class="btn btn-xs btn-warning"
                            title="Edit Item"
                            ng-click="bomDetailsVm.editItem(item)">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button ng-if="item.editMode == false" class="btn btn-xs btn-danger"
                            title="Remove Item"
                            ng-hide="!hasPermission('permission.bom.delete')"
                            ng-click="bomDetailsVm.deleteItem(item)">
                        <i class="fa fa-trash"></i>
                    </button>
                </div>
                <%-------------------------Part Tracking and Failure Buttons buttons---------%>

                <div class="btn-group"
                     ng-show="bomDetailsVm.selectedBom.objectType == 'BOMINSTANCE'">
                    <button class="btn btn-xs btn-primary" style="margin-right: 3px;"
                            ng-show="item.bomItemType == 'SECTION' || item.bomItemType == 'COMMONPART'"
                            title="Generate Section TARB Document"
                            ng-click="bomDetailsVm.getInstanceSectionTarbDocument(item)">
                        <i class="fa fa-file-text"></i>
                    </button>
                    <%--<button ng-show="item.bomItemType == 'SECTION'  && item.hasPartTracking == false"
                            style="background-color: #f5ae0e"
                            title="Assign Part Tracking process" class="btn btn-xs btn-info"
                            ng-click="bomDetailsVm.assignPartTracking(item)">
                        <i class="fa fa-list"></i>
                    </button>
                    <button ng-show="item.bomItemType == 'SECTION' && item.hasPartTracking == true"
                            title="View Part Tracking" class="btn btn-xs btn-success"
                            ng-click="bomDetailsVm.showPartTracking(item)">
                        <i class="fa fa-eye"></i>
                    </button>--%>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>


<%-------------------------------------------For Report------------------------------------------%>


<div style="display: none">
    <table class="table table-striped highlight-row" style="margin-bottom: 20px;" id="printSelectedType">
        <thead>
        <tr>
            <th>Nomenclature</th>
            <th>Type</th>
            <th>Quantity</th>
            <th>Units</th>
            <th>Material</th>
            <th>Drawing Number</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="item in bomDetailsVm.printItems"
            ng-class="{'parentType':item.bomItemType != 'PART'}">
            <td>
                <p class="level{{item.level}}" ng-if="item.bomItemType != 'PART'">
                    {{item.typeRef.name}}<span ng-if="item.typeRef.versity"> ( VSPL )</span>
                </p>

                <p class="level{{item.level}}" ng-if="item.bomItemType == 'PART'">{{item.item.itemMaster.itemName}}
                    {{item.item.partSpec.specName}}</p>
            </td>
            <td>
                <bom-group-type ng-if="item.bomItemType != 'PART'" object="item.typeRef"></bom-group-type>
                <span>{{item.item.itemMaster.parentType.name}}</span>
            </td>
            <td>
                <span class="badge badge-primary"
                      ng-if="item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots"
                      style="font-size: 13px;">{{item.quantity}}
                </span>
                <span class="badge badge-primary"
                      ng-if="item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots"
                      style="font-size: 13px;">{{item.fractionalQuantity}}
                </span>
            </td>
            <td>
                <span ng-if="item.bomItemType == 'PART'">
                    {{item.item.itemMaster.itemType.units}}
                </span>
            </td>
            <td>{{item.item.itemMaster.material}}</td>
            <td>{{item.item.drawingNumber}}</td>
        </tr>
        </tbody>
    </table>
</div>

<div style="display: none">
    <div ng-repeat="sectionTarb in bomDetailsVm.bomInstanceTarbDocument.sectionTarbDtoList">
        <table class="table table-striped highlight-row" style="margin-bottom: 20px;"
               id="bomInstanceTarbDocument{{$index}}">
            <thead>
            <tr>
                <th class="threeHundred-column">Nomenclature</th>
                <th class="oneFifty-column">Type</th>
                <th class="hundred-column">Units</th>
                <th class="oneFifty-column">Drawing Number</th>
                <th class="hundred-column" style="text-align: center;">Quantity</th>
                <th class="hundred-column" style="text-align: center;">Issued</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="bomItem in sectionTarb.sectionParts">
                <td>{{bomItem.item.itemMaster.itemName}}</td>
                <td>{{bomItem.item.itemMaster.parentType.name}}</td>
                <td>{{bomItem.item.itemMaster.itemType.units}}</td>
                <td>{{bomItem.item.drawingNumber}}</td>
                <td>
                    <span ng-if="!bomItem.item.itemMaster.itemType.hasLots"
                          style="font-size: 13px;">{{bomItem.quantity}}
                    </span>
                    <span ng-if="bomItem.item.itemMaster.itemType.hasLots"
                          style="font-size: 13px;">{{bomItem.fractionalQuantity}}
                    </span>
                </td>
                <td>
                    <span>{{bomItem.issuedInstances.length}}</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

</div>

<%------------------------------------   Synchronize BomItem to selected missiles --------------------------%>

<%--<div id="missilesMenu" class="sections-model modal">
    <div class="modal-content">
        <h3 style="text-align: center;background: lavender;height: 10%;margin: 0px;">Missiles</h3>

        <div style="height: 80%;">
            <div style="margin-left: auto;margin-right:auto;margin-top:2%;width: 90%;height:96%;border: 1px solid lightgrey;overflow-y: auto">
                <table class="table table-striped" style="box-shadow: none">
                    <thead>
                    <tr>
                        <th>
                            <input type="checkbox" name="licence" ng-model="bomDetailsVm.selectAllCheck"
                                   style="height:15px; width:15px; vertical-align: middle;margin: 0px;"
                                   ng-click="bomDetailsVm.selectMissiles(bomDetailsVm.selectAllMissles)"/>
                        </th>
                        <th>Missile</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="bomDetailsVm.missiles.length == 0">
                        <td colspan="15"> No Missiles</td>
                    </tr>
                    <tr ng-repeat="missile in bomDetailsVm.missiles">
                        <td style="background-color: white;border: none;width: 25px;">
                            <input type="checkbox" name="section" ng-model="bomSection.selected"
                                   style="height:15px; width:15px; vertical-align: middle;margin: 0px;"
                                   ng-change="bomDetailsVm.selectMissile(missile)"/>
                        </td>
                        <td style="text-align: left;background-color: white;border: none;">
                            {{missile.item.instanceName}}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div style="height: 10%;text-align: center;background: lightgrey;">
            <button class="btn btn-sm btn-primary" ng-click="bomDetailsVm.synchronizeBomItem()"
                    style="margin-top: 5px;">Synchronize BomItem
            </button>
            <button class="btn btn-sm btn-default" ng-click="closeSectionsMenu()" style="margin-top: 5px;">Close
            </button>
        </div>
    </div>
</div>--%>

<div id="sectionsMenu" class="sections-model modal">
    <div class="modal-content">
        <h3 style="text-align: center;background: lavender;height: 10%;margin: 0px;">Sections</h3>

        <div style="height: 80%;">
            <div style="margin-left: auto;margin-right:auto;margin-top:2%;width: 90%;height:96%;border: 1px solid lightgrey;overflow-y: auto">
                <table class="table table-striped" style="box-shadow: none">
                    <thead>
                    <tr>
                        <th>
                            <input type="checkbox" name="licence" ng-model="bomDetailsVm.selectAllCheck"
                                   style="height:15px; width:15px; vertical-align: middle;margin: 0px;"
                                   ng-click="bomDetailsVm.selectAllSection(bomDetailsVm.selectAllCheck)"/>
                        </th>
                        <th>Section</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="bomDetailsVm.bomSections.length == 0">
                        <td colspan="15"> No Sections</td>
                    </tr>
                    <tr ng-repeat="bomSection in bomDetailsVm.bomSections">
                        <td style="background-color: white;border: none;width: 25px;">
                            <input type="checkbox" name="section" ng-model="bomSection.selected"
                                   style="height:15px; width:15px; vertical-align: middle;margin: 0px;"
                                   ng-click="bomDetailsVm.selectSection(bomSection)"/>
                        </td>
                        <td style="text-align: left;background-color: white;border: none;">
                            <span>{{bomSection.typeRef.name}}<span
                                    ng-if="bomSection.typeRef.versity"> ( VSPL )</span></span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div style="height: 10%;text-align: center;background: lightgrey;">
            <button class="btn btn-sm btn-primary" ng-click="bomDetailsVm.printSelectedSections()"
                    style="margin-top: 5px;">Print
            </button>
            <button class="btn btn-sm btn-default" ng-click="closeSectionsMenu()" style="margin-top: 5px;">Close
            </button>
        </div>
    </div>
</div>

<div id="sectionReport-view" class="sections-model modal">
    <div class="report-content">
        <div class="responsive-table"
             style="width: 100%;overflow-y: auto;position: relative;background: white;">
            <table class="table table-striped" id="printSectionRequestReport">
                <thead>
                <tr>
                    <th class="threeHundred-column">Nomenclature</th>
                    <th>Type</th>
                    <th>BOM Quantity</th>
                    <th>Requested Quantity</th>
                    <th>Not Yet Requested (NYR)</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="bomItem in bomDetailsVm.requestReportItems">
                    <td class="threeHundred-column">
                        <span class="level{{bomItem.item.level}}" ng-if="bomItem.item.bomItemType != 'PART'">
                                    <i class="mr5 fa fa-caret-down"
                                       style="cursor: pointer; color: #909090;font-size: 18px;">
                                    </i>
                            <span>{{bomItem.item.typeRef.name}}
                                <span ng-if="bomItem.item.typeRef.versity"> ( VSPL )</span>
                            </span>
                        </span>
                        <span class="level{{bomItem.item.level}}" ng-if="bomItem.item.bomItemType == 'PART'">
                            {{bomItem.item.item.itemMaster.itemName}}
                        </span>
                    </td>
                    <td>
                        <bom-group-type ng-if="bomItem.item.bomItemType != 'PART'"
                                        object="bomItem.item.typeRef"></bom-group-type>
                        <span>{{bomItem.item.item.itemMaster.parentType.name}}</span>
                    </td>
                    <td>
                        <span class="badge badge-primary"
                              ng-if="bomItem.item.bomItemType == 'PART' && !bomItem.item.item.itemMaster.itemType.hasLots"
                              style="font-size: 13px;">{{bomItem.item.quantity}}
                        </span>
                        <span class="badge badge-primary"
                              ng-if="bomItem.item.bomItemType == 'PART' && bomItem.item.item.itemMaster.itemType.hasLots"
                              style="font-size: 13px;">{{bomItem.item.fractionalQuantity}}
                        </span>
                    </td>
                    <td>
                        <span class="badge badge-success"
                              ng-if="bomItem.item.bomItemType == 'PART' && !bomItem.item.item.itemMaster.itemType.hasLots"
                              style="font-size: 13px;">{{bomItem.requested}}
                        </span>
                        <span class="badge badge-success"
                              ng-if="bomItem.item.bomItemType == 'PART' && bomItem.item.item.itemMaster.itemType.hasLots"
                              style="font-size: 13px;">{{bomItem.fractionalRequested}}
                        </span>
                    </td>
                    <td>
                        <span ng-if="bomItem.item.bomItemType == 'PART' && !bomItem.item.item.itemMaster.itemType.hasLots"
                              ng-show="(bomItem.item.quantity - bomItem.requested) == bomItem.item.quantity">
                            NYR
                        </span>
                        <span ng-if="bomItem.item.bomItemType == 'PART' && bomItem.item.item.itemMaster.itemType.hasLots"
                              ng-show="(bomItem.item.fractionalQuantity - bomItem.fractionalRequested) == bomItem.item.fractionalQuantity">
                            NYR
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div style="height: 10%;text-align: center;background: lightgrey;">
            <button class="btn btn-sm btn-default" ng-click="bomDetailsVm.closeSectionRequestReport()"
                    style="margin-top: 5px;">Close
            </button>
            <button class="btn btn-sm btn-primary" ng-click="bomDetailsVm.printSectionRequestReport()"
                    style="margin-top: 5px;">Print
            </button>
        </div>
    </div>
</div>


<div id="workCentersMenu" class="sections-model modal">
    <div class="workCenter-content" style="background: white;">
        <form class="form-inline" style="border: 1px solid lightgrey;padding: 10px;text-align: center;">
            <div class="form-group" style="margin-right: 0px;width: 35%">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">Work Center <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="bomDetailsVm.selectedWorkCenter" theme="bootstrap" style="width:100%"
                               on-select="bomDetailsVm.onSelectWorkCenter($item)">
                        <ui-select-match
                                placeholder="Select Work Center">
                            {{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="workCenter in bomDetailsVm.workCenters track by workCenter">
                            <div ng-bind="workCenter"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>
        <div style="height: 80%;">
            <div ng-if="bomDetailsVm.selectedWorkCenter != null"
                 style="margin-left: auto;margin-right:auto;width: 100%;height:100%;border: 1px solid lightgrey;padding: 5px;overflow-y: auto;">
                <table class="table table-striped" style="box-shadow: none">
                    <thead>
                    <tr>
                        <th>Nomenclature</th>
                        <th>Type</th>
                        <th>Code</th>
                        <th>BOM Qty</th>
                        <th>Inward Qty</th>
                        <th>Units</th>
                        <th>Drawing Number</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="bomDetailsVm.workCenterItems.content.length == 0">
                        <td colspan="15"> No Items found</td>
                    </tr>
                    <tr ng-repeat="workCenterItem in bomDetailsVm.workCenterItems">
                        <td>
                            <p class="level{{workCenterItem.level}}" ng-if="workCenterItem.type == 'SECTION'"
                               title="{{workCenterItem.expanded ? 'Collapse':'Expand'}}" style="margin:0;"
                               ng-click="bomDetailsVm.toggleSection(workCenterItem)">
                                <i ng-if="workCenterItem.children.length > 0" class="mr5 fa"
                                   style="cursor: pointer; color: #909090;font-size: 18px;"
                                   ng-class="{'fa-caret-right': (workCenterItem.expanded == false || workCenterItem.expanded == null || workCenterItem.expanded == undefined),
                                          'fa-caret-down': workCenterItem.expanded == true}"></i>
                                <span style="font-weight: 600;color: black;">
                                    {{workCenterItem.typeRef.name}} <span ng-if="workCenterItem.typeRef.versity"> ( VSPL )</span>( {{workCenterItem.children.length}} )</span>
                            </p>

                            <p class="level{{workCenterItem.level}}" ng-if="workCenterItem.bomItemType == 'PART'"
                               style="margin:0;">
                                {{workCenterItem.item.itemMaster.itemName}}
                            </p>
                        </td>
                        <td>
                            <span ng-if="workCenterItem.bomItemType == 'PART'">{{workCenterItem.item.itemMaster.parentType.name}}</span>
                        </td>
                        <td>
                            <span ng-if="workCenterItem.bomItemType == 'PART'">{{workCenterItem.hierarchicalCode}}</span>
                        </td>
                        <td>
                            <span class="badge badge-primary"
                                  ng-if="workCenterItem.bomItemType == 'PART' && !workCenterItem.item.itemMaster.itemType.hasLots"
                                  style="font-size: 13px;">{{workCenterItem.quantity}}
                            </span>
                            <span class="badge badge-primary"
                                  ng-if="workCenterItem.bomItemType == 'PART' && workCenterItem.item.itemMaster.itemType.hasLots"
                                  style="font-size: 13px;">{{workCenterItem.fractionalQuantity}}
                            </span>
                        </td>
                        <td>
                            <span class="badge badge-success"
                                  ng-if="workCenterItem.bomItemType == 'PART' && !workCenterItem.item.itemMaster.itemType.hasLots"
                                  style="font-size: 13px;">{{workCenterItem.inwardQty}}
                            </span>
                            <span class="badge badge-success"
                                  ng-if="workCenterItem.bomItemType == 'PART' && workCenterItem.item.itemMaster.itemType.hasLots"
                                  style="font-size: 13px;">{{workCenterItem.fractionalInwardQty}}
                            </span>
                        </td>
                        <td>
                            <span ng-if="workCenterItem.bomItemType == 'PART'">{{workCenterItem.item.itemMaster.itemType.units}}</span>
                        </td>
                        <td>
                            <span ng-if="workCenterItem.bomItemType == 'PART'">{{workCenterItem.item.drawingNumber}}</span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div style="height: 10%;text-align: center;background: lightgrey;">
            <button class="btn btn-sm btn-default" ng-click="closeWorkCentersMenu()" style="margin-top: 5px;">Close
            </button>
        </div>
    </div>
</div>


<div id="newRightSidePanel" class="sidePanel modal">
    <div class="right-side" style="background: white;">
        <div style="height: 10%;background: lightgrey;">

        </div>
        <div style="height: 80%;overflow-y: auto;">
            Hai
        </div>
        <div style="height: 10%;text-align: right;background: lightgrey;">
            <button class="btn btn-sm btn-danger" ng-click="bomDetailsVm.closeNewSidePanel()"
                    style="margin-top: 5px;margin-right: 10px;">Close
            </button>
        </div>
    </div>
</div>