<style>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        position: absolute;
        overflow: auto;
        height: 100%;
        padding: 10px;
    }

    .view-content .responsive-table table {
        table-layout: fixed;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .popover {
        min-width: 450px !important;
        max-width: 450px !important;
    }

    .planning-info-panel {
        display: none;
        z-index: 101;
        width: 300px;
        position: absolute;
        top: 0px;
        left: 0px;
        background-color: #fff;
        bottom: 0px;
        border: 1px solid #ddd;
        overflow-y: auto;
    }

    .planning-info-panel .info-panel-header {
        border-bottom: 1px solid #ddd;
        padding-left: 10px;
    }

    .planning-info-panel .info-panel-header h3 {
        line-height: 50px;
        margin: 0;
    }

    .info-panel-header .close {
        position: absolute;
        right: 10px;
        top: 10px;
        width: 32px;
        line-height: 50px;
        height: 50px;
        padding-top: 7px;
        margin-right: -5px;
        opacity: 0.3;

    }

    .info-panel-header .close:hover {
        opacity: 1;
    }

    .info-panel-header .close:before, .info-panel-header .close:after {
        position: absolute;
        left: 15px;
        content: ' ';
        height: 15px;
        width: 2px;
        background-color: #333;
    }

    .info-panel-header .close:before {
        transform: rotate(45deg);
    }

    .info-panel-header .close:after {
        transform: rotate(-45deg);
    }

    .info-panel-details {
        padding: 10px;
    }

    .sections-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 15px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .sections-model .report-content {
        margin-left: auto;
        margin-right: auto;
        top: 50px;
        display: block;
        height: 200px;
        width: 320px;
    }

    #freeTextSearchDirective {
        top: 0 !important;
    }

    .view-toolbar .ui-select-bootstrap .ui-select-toggle > a.btn {
        position: absolute;
        height: 29px !important;
        right: 10px;
        margin-top: -5px;
        padding: 5px !important;
    }

</style>
<div class="view-container" fitcontent>

    <style scoped>
        .search-form {
            height: 30px;
            border-radius: 15px;
        }

        .btn {
            margin-right: 8px;
        }

        .popover-content {
            padding: 5px !important;
        }

    </style>
    <div class="view-toolbar">
        <div class="col-md-12">
            <div class="col-md-1" style="padding-left: 0;">
                <button class="btn btn-xs" ng-click="planningVm.showPlanningInfoPanel()" title="Click to Planning Info">
                    Planning Info
                </button>
            </div>
            <div class="col-md-1" ng-show="planningVm.dropDown">
                <div class="dropdown" uib-dropdown auto-close="outsideClick"
                     on-toggle="toggledMissile(open)" style="cursor: pointer;">
                    <a class="btn btn-primary" title="Select Missiles"
                       uib-dropdown-toggle>Missile
                        <span class="caret" style="margin-left:5px;border-top-color: #fff cursor: pointer;"></span></a>
                    <ul class="dropdown-menu status-select"
                        uib-dropdown-menu style="cursor: pointer;margin-left: -3px;max-height: 400px;overflow-y: auto;">
                        <li>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" ng-model="planningVm.selectedAll"
                                           ng-click="selectAllMissiles()">
                                    All Missiles
                                </label>
                            </div>
                        </li>
                        <li ng-repeat="missile in planningVm.allMissiles track by $index">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" ng-model="missile.selected"
                                           ng-click="onDropDownMissileSelection(missile)">
                                    {{missile.instanceName}}
                                </label>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="col-md-1 dropdown" ng-show="planningVm.dropDown && planningVm.selectedMissiles.length > 0"
                 uib-dropdown auto-close="outsideClick"
            <%--on-toggle="toggledSection(open)"--%> style="cursor: pointer;">
                <a class="btn btn-success"
                   title="Select Sections"
                   uib-dropdown-toggle>Section
                    <span class="caret" style="margin-left:5px;border-top-color: #fff cursor: pointer;"></span></a>
                <ul class="dropdown-menu status-select"
                    uib-dropdown-menu style="cursor: pointer;max-height: 400px;overflow-y: auto;">
                    <%-- <li>
                         <a href="" ng-click="allSelectSectionsPlanning(true)">CheckAll</a>
                         <a href="" ng-click="allSelectSectionsPlanning(false)">UnCheckAll</a>
                     </li>--%>
                    <li ng-repeat="section in planningVm.sections track by $index">
                        <div class="checkbox">
                            <label>
                                <input type="radio" name="section"
                                       ng-click="onDropDownSectionSelection(section)">
                                {{section.item.typeRef.name}} <span ng-if="section.item.typeRef.versity">(VSPL)</span>
                            </label>
                        </div>
                    </li>
                </ul>
            </div>

            <div class="col-md-7 btn-group"
                 ng-show="planningVm.selectedMissiles.length > 0 && planningVm.selectedSection != null && planningVm.dropDown">
                <%--<button class="btn btn-sm btn-success min-width"
                        ng-show="planningVm.productSelected"
                        ng-click="planningVm.selectItems()">
                    Select Items
                </button>--%>
                <button class="btn btn-sm btn-success min-width"
                        ng-show="planningVm.productSelected && hasPermission('permission.planning.do') && !planningVm.bomSearch"
                        ng-click="planningVm.autoPlan()">
                    Auto-Plan
                </button>
                <button class="btn btn-sm btn-success min-width"
                        ng-show="planningVm.productSelected && hasPermission('permission.planning.do') && !planningVm.bomSearch"
                        ng-click="planningVm.resetPlan()">
                    Reset-Plan
                </button>

                <button class="btn btn-xs" title="Print Shortage"
                        ng-click="planningVm.printShortage()">
                    <i class="fa fa-print" style="color: black;font-size: 18px;"></i>
                </button>
                <button title="Export Shortage Report in Excel"
                        class="btn btn-sm btn-success min-width dropdown-toggle exportButton"
                        ng-click="planningVm.exportReports()">Export
                </button>
                <div class="col-md-1" ng-show="planningVm.dropDown && planningVm.selectedSection != null"
                     style="cursor: pointer;width: 19%;">
                    <ui-select ng-model="planningVm.workCenter" theme="bootstrap"
                               on-select="planningVm.onSelectWorkCenter($item)">
                        <ui-select-match placeholder="Work Center" allow-clear="true">
                            {{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="workCenter in planningVm.workCenters track by $index">
                            <div ng-bind="workCenter"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <%--<div class="col-md-1" ng-show="planningVm.dropDown && planningVm.selectedSection != null"
                 style="cursor: pointer;width: 15%; margin: 0px 0 0 -96px;">
                <ui-select ng-model="planningVm.workCenter" theme="bootstrap"
                           on-select="planningVm.onSelectWorkCenter($item)">
                    <ui-select-match placeholder="Work Center" allow-clear="true">
                        {{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="workCenter in planningVm.workCenters track by $index">
                        <div ng-bind="workCenter"></div>
                    </ui-select-choices>
                </ui-select>
                &lt;%&ndash;<a class="btn btn-success"
                   title="Select Sections"
                   uib-dropdown-toggle>Work Center
                    <span class="caret" style="margin-left:5px;border-top-color: #fff cursor: pointer;"></span></a>
                <ul class="dropdown-menu status-select"
                    uib-dropdown-menu style="cursor: pointer;max-height: 400px;overflow-y: auto;">
                    <li ng-repeat="workCenter in planningVm.workCenters track by $index">
                        <label>
                            {{workCenter}}
                        </label>
                    </li>
                </ul>&ndash;%&gt;
            </div>--%>

            <free-text-search ng-if="planningVm.selectedSection != null" on-clear="planningVm.resetBomSearch"
                              search-term="planningVm.searchBomText"
                              on-search="planningVm.searchBom"></free-text-search>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">

        <div class="planning-info-panel" id="planningInfoPanel" style="padding: 0px">
            <div class="info-panel-header">
                <h3>Planning</h3>
                <a href="" ng-click="planningVm.showPlanningInfoPanel()"
                   class="close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="info-panel-details">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div>
                        <input type="search" class="form-control input-sm search-form" placeholder="Search"
                               ng-model="planningVm.searchValue" ng-change="planningVm.searchTree()">
                    </div>
                    <ul id="classificationMainTree" class="easyui-tree" close-text="Close">
                    </ul>
                </div>
            </div>
        </div>
        <div class="responsive-table" ng-if="planningVm.selectedBom != null">
            <table class="table table-striped highlight-row" style="margin-bottom: 25px;">
                <thead>
                <tr>
                    <th width="60px"<%--ng-show="planningVm.selectItemsView == true"--%>
                        class="auto-column header-row">Select
                    </th>
                    <th class="threeHundred-column header-row">Nomenclature</th>
                    <th class="twoHundred-column header-row">Type</th>
                    <%--<th style="width: 150px;text-align: center;">Drawing Number</th>--%>
                    <th class="hundred-column header-row">Units</th>
                    <th class="hundred-column header-row">Work Center</th>
                    <th class="hundred-column header-row" style="text-align: center;">BOM Qty</th>
                    <th class="hundred-column header-row" style="text-align: center;">Stock</th>
                    <th class="hundred-column header-row" style="text-align: center;">Issued Qty</th>
                    <th class="hundred-column header-row" style="text-align: center;">Allocated Qty</th>
                    <th class="danger hundred-column header-row" ng-if="planningVm.selectedMissiles.length > 0">Shortage
                    </th>
                    <th class="oneFifty-column info header-row"
                        ng-repeat="missile in planningVm.selectedMissiles track by $index">
                        <div style="margin-left: 12px;">
                            <span style="margin-left: 12px;">{{missile.instanceName}}</span><br>
                            <span style="color: blue;">A</span> /
                            <span style="color: blue;">IP</span> /
                            <span style="color: green;">I</span> /
                            <span style="color: darkred">F</span> /
                            <span style="color: red">S</span>
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="planningVm.loading == true">
                    <td colspan="10">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Items...</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="planningVm.loading == false && planningVm.selectedBomItems.length == 0">
                    <td colspan="11">No Items</td>
                </tr>
                <tr ng-repeat="item in planningVm.selectedBomItems" ng-class="{'selected': item.selected}">
                    <td <%--ng-show="planningVm.selectItemsView && item.item.bomItemType == 'PART'"--%>
                            style="text-align: center">
                        <input ng-disabled="item.item.bomItemType != 'PART'" type="checkbox"
                               ng-model="item.reportSelect"
                               ng-click="planningVm.selectItemForReport(item)">
                    </td>
                    <td class="threeHundred-column">
                        <p class="level{{item.level}}" ng-click="planningVm.toggleNode(item, 'item')"
                           title="{{item.expanded ? 'Collapse':'Expand'}}" style="margin: 0;">
                            <i ng-if="item.item.children.length > 0" class="mr5 fa"
                               style="cursor: pointer; color: #909090;font-size: 18px;"
                               title="{{item.expanded ? 'Collapse':'Expand'}}"
                               ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                                                'fa-caret-down': item.expanded == true}"></i>
                            <span ng-if="item.item.bomItemType != 'PART'">{{item.item.typeRef.name}}
                                <span ng-if="item.item.typeRef.versity"> ( VSPL )</span>
                            </span>
                        </p>

                        <p class="level{{item.level}}" ng-if="item.item.bomItemType == 'PART'" style="margin: 0px;">
                            <a ui-sref="app.items.details({itemId: item.item.item.id})"
                               title="Click to show Item details">
                                <span ng-bind-html="item.item.item.itemMaster.itemName | highlightText: planningVm.searchBomText"></span>
                                {{item.item.item.partSpec.specName}}</a>
                        </p>
                    </td>
                    <td class="twoHundred-column">
                        <bom-group-type ng-if="item.item.bomItemType != 'PART'"
                                        object="item.item.typeRef"></bom-group-type>
                        <span ng-if="item.item.bomItemType == 'PART'">{{item.item.item.itemMaster.parentType.name}}</span>
                    </td>
                    <%--<td>{{item.item.item.drawingNumber}}</td>--%>
                    <td class="hundred-column">{{item.item.item.itemMaster.itemType.units}}</td>
                    <td class="hundred-column">
                        <span ng-bind-html="item.item.workCenter | highlightText: planningVm.workCenter"></span>
                    </td>
                    <td class="hundred-column" style="text-align: center;">
                                <span class="badge badge-red" style="font-size: 14px;" title="BOM Quantity"
                                      ng-if="item.item.bomItemType == 'PART' && !item.item.item.itemMaster.itemType.hasLots">{{item.item.quantity}}</span>
                                <span class="badge badge-red" style="font-size: 14px;" title="BOM Quantity"
                                      ng-if="item.item.bomItemType == 'PART' && item.item.item.itemMaster.itemType.hasLots">{{item.item.fractionalQuantity}}</span>
                    </td>

                    <td class="hundred-column" style="text-align: center;">
                        <span ng-if="item.item.bomItemType == 'PART' && !item.item.item.itemMaster.itemType.hasLots">
                            <a ng-if="item.stock > 0"
                               title="Click to show Serial Numbers" style="font-size: 14px;"
                               class="badge badge-primary"
                               uib-popover-template="planningVm.stockInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Stock UPN"
                               popover-trigger="'outsideClick'">{{item.stock}}
                            </a>
                            <span class="badge badge-primary" style="font-size: 14px;" ng-if="item.stock == 0">{{item.stock}}</span>
                        </span>
                        <span ng-if="item.item.bomItemType == 'PART' && item.item.item.itemMaster.itemType.hasLots">
                            <a ng-if="item.fractionalStock > 0"
                               title="Click to show Serial Numbers" style="font-size: 14px;"
                               class="badge badge-primary"
                               uib-popover-template="planningVm.stockInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Stock UPN"
                               popover-trigger="'outsideClick'">{{item.fractionalStock}}
                            </a>
                            <span class="badge badge-primary" style="font-size: 14px;"
                                  ng-if="item.fractionalStock == 0">{{item.fractionalStock}}</span>
                        </span>
                    </td>
                    <td class="hundred-column" style="text-align: center;">
                        <span ng-if="item.item.bomItemType == 'PART'" style="font-size: 14px;" title="Total Issued Qty"
                              class="badge badge-success">
                            {{item.itemIssued}}</span>
                        </span>
                    </td>
                    <td class="hundred-column" style="text-align: center;">
                        <%--<a ng-if="item.item.bomItemType == 'PART' && (item.allocated + item.commonAllocated) > 0"
                           title="Show Allocated Quantity" style="font-size: 14px;"
                           class="badge badge-warning"
                           uib-popover-template="planningVm.allocatePopOver.templateUrl"
                           popover-append-to-body="true"
                           popover-popup-delay="50"
                           popover-placement="top-right"
                           popover-title="Allocation Qty"
                           popover-trigger="'outsideClick'">{{item.allocated + item.commonAllocated}}
                        </a>
                            <span class="badge badge-warning" style="font-size: 14px;"
                                  ng-if="item.item.bomItemType == 'PART' && (item.allocated + item.commonAllocated) == 0">
                                {{item.allocated + item.commonAllocated}}</span>--%>
                        <span ng-if="item.item.bomItemType == 'PART'" style="font-size: 14px;"
                              title="Current Allocation Qty"
                              class="badge badge-warning">
                            {{(item.allocated - item.issuedQty)  + (item.commonAllocated-item.commonIssuedQty)}}</span>
                        </span>
                    </td>

                    <td class="hundred-column danger" style="text-align: center"
                        ng-if="planningVm.selectedMissiles.length > 0">
                        <span class="badge badge-danger" title="Total Shortage"
                              ng-if="item.shortage < 0" style="font-size: 14px;"
                              ng-show="item.item.bomItemType == 'PART'">0</span>
                        <span title="Total Shortage"
                              ng-if="item.shortage >= 0" style="font-size: 14px;"
                              ng-show="item.item.bomItemType == 'PART'"
                              class="badge badge-danger">{{item.shortage}}
                        </span>
                    </td>
                    <td class="info"
                        ng-repeat="missile in planningVm.selectedMissiles">
                        <span ng-if='item.listMap[""+ missile.id] != undefined && item.item.bomItemType == "PART"'>
                            <span ng-show="hasPermission('permission.planning.do') && !item.item.item.itemMaster.itemType.hasLots && (item.listMap[missile.id].allocateQty - item.listMap[missile.id].failedQty) < item.item.quantity"
                                  ng-class="{'disabled-row': item.systemInventory.stockOnHand == 0}">
                                    <a href="" ng-click="planningVm.manualAllocate(item,missile)"
                                       ng-show="item.item.bomItemType == 'PART'"
                                       title="Click to allocate items"><i
                                            class="fa fa-plus" aria-hidden="true"></i></a>
                            </span>
                            <span ng-show="hasPermission('permission.planning.do') && item.item.item.itemMaster.itemType.hasLots && (item.listMap[missile.id].allocateQty - item.listMap[missile.id].failedQty) < item.item.fractionalQuantity"
                                  ng-class="{'disabled-row': item.systemInventory.stockOnHand == 0}">
                                    <a href="" ng-click="planningVm.manualAllocate(item,missile)"
                                       ng-show="item.item.bomItemType == 'PART'"
                                       title="Click to allocate items"><i
                                            class="fa fa-plus" aria-hidden="true"></i></a>
                            </span>
                            <span ng-show="hasPermission('permission.planning.do') && item.item.bomItemType == 'PART' && (item.listMap[missile.id].allocateQty - item.listMap[missile.id].issuedQty) > 0"
                                  ng-class="{'disabled-row': item.systemInventory.stockOnHand == 0}">
                                    <a href="" ng-click="planningVm.manualReAllocation(item,missile)"
                                       ng-show="item.item.bomItemType == 'PART'"
                                       title="Click to Re-allocate items"><i
                                            class="fa fa-minus" aria-hidden="true"></i></a>
                            </span>
                        <%--<span ng-hide="item.item.hasBom"
                              ng-init="aloct = item.listMap[missile.id].allocateQty"
                              ng-show="item.item.bomItemType == 'PART'"
                              ng-class="{'disabled-row': item.systemInventory.stockOnHand == 0}">
                            <a href="" ng-click="planningVm.manualReAllocate(item,missile)"
                               ng-show="item.item.bomItemType == 'PART'"
                               title="Click to allocate items"><i
                                    class="fa fa-hand-pointer-o" style="color:red" aria-hidden="true"></i></a>
                        </span>--%>
                        <span ng-show="item.item.bomItemType == 'PART'"
                              href=""
                              ng-class="{'disabled-row': item[missile.id].allocateQty == 0.0}"
                              title="{{item.listMap[missile.id].allocateQty >= 0.0 ? 'Allocated':'No allocations'}}"
                              class="sm badge badge-warning" style="font-size: 14px;"
                              ng-click="planningVm.showBatchNumbers(item,missile)">{{(item.listMap[""+ missile.id].allocateQty - item.listMap[""+ missile.id].issuedQty)}}
                        </span>
                        <span ng-show="item.item.bomItemType == 'PART'"
                              href=""
                              ng-class="{'disabled-row': item[missile.id].issueProcessQty == 0.0}"
                              title="{{item.listMap[missile.id].issueProcessQty >= 0.0 ? 'Issue Process Qty':'No process quantities'}}"
                              class="sm badge badge-primary" style="font-size: 14px;">{{item.listMap[""+ missile.id].issueProcessQty}}
                        </span>
                        <span ng-show="item.item.bomItemType == 'PART'"
                              href=""
                              ng-class="{'disabled-row': item.listMap[missile.id].issuedQty == 0.0}"
                              title="{{item.listMap[missile.id].issuedQty >= 0.0 ? 'Issued':'No issues'}}"
                              class="sm badge badge-success" style="font-size: 14px;"
                              ng-click="">{{item.listMap[""+ missile.id].issuedQty}}
                        </span>
                        <span ng-show="item.item.bomItemType == 'PART'"
                              href="" style="font-size: 14px;"
                              ng-class="{'disabled-row': item.listMap[missile.id].failedQty == 0.0}"
                              title="{{item.listMap[missile.id].failedQty >= 0.0 ? 'Failed':'No Failures'}}"
                              class="sm badge badge-danger"
                              ng-click="">{{item.listMap[""+ missile.id].failedQty}}
                        </span>
                        <span ng-show="item.item.bomItemType == 'PART'"
                              href="" style="font-size: 14px;"
                              ng-class="{'disabled-row': item.listMap[missile.id].failedQty == 0.0}"
                              title="Shortage"
                              class="sm badge badge-danger"
                              ng-click="">
                            <span ng-if="!item.item.item.itemMaster.itemType.hasLots">{{(item.item.quantity - (item.listMap[""+ missile.id].allocateQty))}}</span>
                            <span ng-if="item.item.item.itemMaster.itemType.hasLots">{{(item.item.fractionalQuantity - (item.listMap[""+ missile.id].allocateQty))}}</span>
                        </span>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div id="re-allocation" class="sections-model modal" style="padding-top: 170px">
            <div class="report-content" style="background: white;">
                <div style="height: 28px;background: #dad1d1;padding-left: 18px;padding-top: 0px;">
                    <h4>Re-Allocate Quantity</h4>
                </div>
                <div style="padding: 20px">
                    <input id="count" class="form-control" style="margin-left: 20px;margin-bottom: 10px;width: 200px"
                           maxlength="4"
                           ng-enter="planningVm.reAllocate()" placeholder="Enter reallocate Quantity"
                           ng-model="planningVm.quantity" type="text">
                </div>
                <p ng-show="planningVm.errorMessage != null"
                   style="color: darkred;font-weight: 600;font-size: 14px;margin-left:15px">
                    {{planningVm.errorMessage}}</p>

                <div class="row">
                    <div class="btn-group pull-right" style="margin-right:10px;" role="group"
                         aria-label="Basic example">
                        <button style="margin-right: 10px" type="submit" ng-click="planningVm.cancel()"
                                class="btn btn-danger">Cancel
                        </button>
                        <button type="submit" ng-click="planningVm.reAllocate()" class="btn btn-success">OK
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%-----------------------------------------For Report------------------------------------%>

<div style="display: none">
    <table class="table table-striped highlight-row" style="margin-bottom: 20px;" id="printSelectedType">
        <thead>
        <tr>
            <th class="threeHundred-column">Nomenclature</th>
            <th class="oneFifty-column">Type</th>
            <th class="hundred-column">Units</th>
            <th class="hundred-column" style="text-align: center;">Quantity</th>
            <th class="hundred-column">Stock</th>
            <th class="oneFifty-column info"
                ng-repeat="missile in planningVm.selectedMissiles track by $index">
                <div>
                    <span>{{missile.instanceName}} (A/I/F)</span>
                </div>
            </th>
            <th style="background-color: rgba(245, 15, 15, 0.37);" class="hundred-column">Shortage</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="item in planningVm.selectedBomItems | filter: printFilter"
            ng-class="{'selected': item.selected}" ng-if="!$first">
            <td class="threeHundred-column">
                                <span class="level{{(item.level-1)}}" ng-click="planningVm.toggleNode(item, 'item')"
                                      title="{{item.expanded ? 'Collapse':'Expand'}}">
                                    <i ng-if="item.item.children.length > 0" class="mr5 fa"
                                       style="cursor: pointer; color: #909090;font-size: 18px;"
                                       title="{{item.expanded ? 'Collapse':'Expand'}}"
                                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                                                'fa-caret-down': item.expanded == true}"></i>
                                    <span ng-if="item.item.bomItemType != 'PART'">{{item.item.typeRef.name}}
                                    <span ng-if="item.item.typeRef.versity"> ( VSPL )</span>
                                    </span>
                                </span>
                                <span ng-if="item.item.bomItemType == 'PART'">
                                    <a ui-sref="app.items.details({itemId: item.item.item.id})"
                                       title="Click to show Item details">
                                        {{item.item.item.itemMaster.itemName}} {{item.item.item.partSpec.specName}}</a>
                                </span>
            </td>
            <td class="twoHundred-column">
                <bom-group-type ng-if="item.item.bomItemType != 'PART'"
                                object="item.item.typeRef"></bom-group-type>
                <span ng-if="item.item.bomItemType == 'PART'">{{item.item.item.itemMaster.parentType.name}}</span>
            </td>
            <td class="hundred-column">{{item.item.item.itemMaster.itemType.units}}</td>
            <td class="hundred-column" style="text-align: center;">
                                <span class="badge badge-primary"
                                      ng-if="item.item.bomItemType == 'PART' && !item.item.item.itemMaster.itemType.hasLots">{{item.item.quantity}}</span>
                                <span class="badge badge-primary"
                                      ng-if="item.item.bomItemType == 'PART' && item.item.item.itemMaster.itemType.hasLots">{{item.item.fractionalQuantity}}</span>
            </td>

            <td class="hundred-column" style="text-align: center;">
                        <span>
                            <a ng-if="item.item.bomItemType == 'PART' && !item.item.item.itemMaster.itemType.hasLots"
                               title="Click to show Serial No.s" style="font-size: 14px;"
                               class="badge badge-success"
                               uib-popover-template="planningVm.stockInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Stock UPN"
                               popover-trigger="'outsideClick'">{{item.stock}}
                            </a>
                        </span>
                        <span>
                            <a ng-if="item.item.bomItemType == 'PART' && item.item.item.itemMaster.itemType.hasLots"
                               title="Click to show Serial No.s" style="font-size: 14px;"
                               class="badge badge-success"
                               uib-popover-template="planningVm.stockInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Stock UPN"
                               popover-trigger="'outsideClick'">{{item.fractionalStock}}
                            </a>
                        </span>
            </td>
            <td class="info oneFifty-column"
                ng-repeat="missile in planningVm.selectedMissiles">
                        <span ng-if='item.listMap[""+ missile.id] != undefined && item.item.item.itemMaster.itemType.hasLots'>
                            <span ng-show="item.item.bomItemType == 'PART'"
                                  class="badge badge-primary">{{(item.listMap[""+ missile.id].allocateQty - item.listMap[""+ missile.id].issuedQty)}}/{{item.listMap[""+ missile.id].issuedQty}}/{{item.listMap[""+ missile.id].failedQty}}
                            </span>
                        </span>
                        <span ng-if='item.listMap[""+ missile.id] != undefined && !item.item.item.itemMaster.itemType.hasLots'>
                            <span ng-show="item.item.bomItemType == 'PART'"
                                  class="badge badge-primary">{{(item.listMap[""+ missile.id].allocateQty - item.listMap[""+ missile.id].issuedQty)}}/{{item.listMap[""+ missile.id].issuedQty}}/{{item.listMap[""+ missile.id].failedQty}}
                            </span>
                        </span>
            </td>
            <td class="danger" style="text-align: center;background-color: rgba(245, 15, 15, 0.37);"
                ng-if="planningVm.selectedMissiles.length > 0 && item.item.bomItemType == 'PART'">
                        <span class="badge badge-red" title="presentShortage"
                              ng-if="item.shortage < 0"
                              ng-show="item.item.bomItemType == 'PART'">0</span>
                        <span title="presentShortage" class="shortage"
                              ng-if="item.shortage >= 0"
                              ng-show="item.item.bomItemType == 'PART'"
                              class="badge badge-red">{{item.shortage}}
                        </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>


<%-----------------------------------------For Item Report------------------------------------%>

<div style="display: none">
    <table class="table table-striped highlight-row" style="margin-bottom: 20px;" id="printSelectedItem">
        <thead>
        <tr>
            <th class="threeHundred-column">Nomenclature</th>
            <%--<th class="oneFifty-column">Type</th>--%>
            <th class="hundred-column">Units</th>
            <th class="hundred-column" style="text-align: center;">Qty</th>
            <%--<th class="hundred-column">Stock</th>--%>
            <th class="oneFifty-column info"
                ng-repeat="missile in planningVm.selectedMissiles track by $index">
                <div>
                    <span>{{missile.instanceName}} (S)</span>
                </div>
            </th>
            <th style="background-color: rgba(245, 15, 15, 0.37);" class="hundred-column">Shortage</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="item in planningVm.selectedReportItems | filter: printFilter"
        <%--ng-class="{'selected': item.selected}" ng-if="!$first"--%>>
            <td class="threeHundred-column">
                                <span class="level{{(item.level-1)}}" ng-click="planningVm.toggleNode(item, 'item')"
                                      title="{{item.expanded ? 'Collapse':'Expand'}}">
                                    <i ng-if="item.item.children.length > 0" class="mr5 fa"
                                       style="cursor: pointer; color: #909090;font-size: 18px;"
                                       title="{{item.expanded ? 'Collapse':'Expand'}}"
                                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                                                'fa-caret-down': item.expanded == true}"></i>
                                    <span ng-if="item.item.bomItemType != 'PART'">{{item.item.typeRef.name}}
                                        <span ng-if="item.item.typeRef.versity"> ( VSPL )</span>
                                    </span>
                                </span>
                                <span ng-if="item.item.bomItemType == 'PART'">
                                    <a ui-sref="app.items.details({itemId: item.item.item.id})"
                                       title="Click to show Item details">
                                        {{item.item.item.itemMaster.itemName}} {{item.item.item.partSpec.specName}}</a>
                                </span>
            </td>
            <%--<td class="twoHundred-column">
                <bom-group-type ng-if="item.item.bomItemType != 'PART'"
                                object="item.item.typeRef"></bom-group-type>
                <span ng-if="item.item.bomItemType == 'PART'">{{item.item.item.itemMaster.parentType.name}}</span>
            </td>--%>
            <td class="hundred-column">{{item.item.item.itemMaster.itemType.units}}</td>
            <td class="hundred-column" style="text-align: center;">
                                <span class="badge badge-primary"
                                      ng-if="item.item.bomItemType == 'PART' && !item.item.item.itemMaster.itemType.hasLots">{{item.item.quantity}}</span>
                                <span class="badge badge-primary"
                                      ng-if="item.item.bomItemType == 'PART' && item.item.item.itemMaster.itemType.hasLots">{{item.item.fractionalQuantity}}</span>
            </td>

            <%--<td class="hundred-column" style="text-align: center;">
                        <span>
                            <a ng-if="item.item.bomItemType == 'PART' && !item.item.item.itemMaster.itemType.hasLots"
                               title="Click to show Issued" style="font-size: 14px;"
                               class="badge badge-success"
                               uib-popover-template="planningVm.stockInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Stock UPN"
                               popover-trigger="'outsideClick'">{{item.stock}}
                            </a>
                        </span>
                        <span>
                            <a ng-if="item.item.bomItemType == 'PART' && item.item.item.itemMaster.itemType.hasLots"
                               title="Click to show Issued" style="font-size: 14px;"
                               class="badge badge-success"
                               uib-popover-template="planningVm.stockInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Stock UPN"
                               popover-trigger="'outsideClick'">{{item.fractionalStock}}
                            </a>
                        </span>
            </td>--%>
            <%--<td class="info oneFifty-column"
                ng-repeat="missile in planningVm.selectedMissiles">
                        <span ng-if='item.listMap[""+ missile.id] != undefined && item.item.item.itemMaster.itemType.hasLots'>
                            <span ng-show="item.item.bomItemType == 'PART'"
                                  class="badge badge-primary">{{&lt;%&ndash;item.item.fractionalQuantity - &ndash;%&gt;item.listMap[""+ missile.id].allocateQty}}/{{item.listMap[""+ missile.id].issuedQty}}/{{item.listMap[""+ missile.id].failedQty}}
                            </span>
                        </span>
                        <span ng-if='item.listMap[""+ missile.id] != undefined && !item.item.item.itemMaster.itemType.hasLots'>
                            <span ng-show="item.item.bomItemType == 'PART'"
                                  class="badge badge-primary">{{&lt;%&ndash;item.item.quantity - &ndash;%&gt;item.listMap[""+ missile.id].allocateQty}}/{{item.listMap[""+ missile.id].issuedQty}}/{{item.listMap[""+ missile.id].failedQty}}
                            </span>
                        </span>
            </td>--%>
            <td class="info oneFifty-column"
                ng-repeat="missile in planningVm.selectedMissiles">
                        <span ng-if='item.listMap[""+ missile.id] != undefined && item.item.item.itemMaster.itemType.hasLots'>
                            <span ng-show="item.item.bomItemType == 'PART'"
                                  ng-class="{'badge-danger': (item.item.fractionalQuantity - item.listMap[''+ missile.id].allocateQty) > 0.0}"
                                  class="sm badge"
                                  class="badge">{{item.item.fractionalQuantity - item.listMap[""+ missile.id].allocateQty}}
                            </span>
                        </span>
                        <span ng-if='item.listMap[""+ missile.id] != undefined && !item.item.item.itemMaster.itemType.hasLots'>
                            <span ng-show="item.item.bomItemType == 'PART'"
                                  ng-class="{'badge-danger': (item.item.quantity - item.listMap[''+ missile.id].allocateQty) > 0}"
                                  class="badge">{{item.item.quantity - item.listMap[""+ missile.id].allocateQty}}
                            </span>
                        </span>
            </td>

            <td class="danger" style="text-align: center;background-color: rgba(245, 15, 15, 0.37);"
                ng-if="planningVm.selectedMissiles.length >= 0 && item.item.bomItemType == 'PART'">
                        <span class="badge badge-red" title="presentShortage"
                              ng-if="item.shortage < 0"
                              ng-show="item.item.bomItemType == 'PART'">0</span>
                        <span title="presentShortage" class="shortage"
                              ng-if="item.shortage >= 0"
                              ng-show="item.item.bomItemType == 'PART'"
                              class="badge badge-red">{{item.shortage}}
                        </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
