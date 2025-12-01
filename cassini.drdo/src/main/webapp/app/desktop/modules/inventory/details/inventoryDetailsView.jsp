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

    .inventory-info-panel {
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

    .inventory-info-panel .info-panel-header {
        border-bottom: 1px solid #ddd;
        padding-left: 10px;
    }

    .inventory-info-panel .info-panel-header h3 {
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

    #freeTextSearchDirective {
        top: 7px !important;
        right: 0 !important;
    }
</style>
<div class="view-container" fitcontent>

    <style scoped>
        .search-form {
            height: 30px;
            border-radius: 15px;
        }

        .popover-content {
            padding: 5px !important;
        }

    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-xs" ng-click="inventoryDetailsVm.showInventoryInfoPanel()"
                    title="Click to Inventory Info">
                Inventory Info
            </button>
            <button class="btn btn-sm btn-primary dropdown-toggle reportButton" type="button"
                    ng-if="inventoryDetailsVm.selectedBom != null && hasPermission('permission.inventory.reports')"
                    data-toggle="dropdown">
                <i class="fa fa-bar-chart" aria-hidden="true"></i>Reports
                <span class="caret"></span></button>
            <ul class="dropdown-menu" style="margin-left: 110px;">
                <li>
                    <a href="" ng-show="bom" ng-click="inventoryDetailsVm.showInwardReport()"> Inward Report</a></li>
                <li>
                    <a href="" ng-show="instance" ng-click="inventoryDetailsVm.showRequestReport()"> Request Report</a>
                </li>
                <li>
                    <a href="" ng-show="instance" ng-click="inventoryDetailsVm.showIssueReport()"> Issue Report</a></li>
            </ul>

            <button ng-if="bom && hasPermission('permission.inventory.reports')" class="btn btn-xs"
                    title="Click to Print BOM Inventory Report" ng-click="inventoryDetailsVm.getInventoryReportByBom()">
                <i style="font-size: 18px;" class="fa fa-print"></i>
            </button>
            <button ng-if="instance  && hasPermission('permission.inventory.reports')" class="btn btn-xs"
                    title="Click to Print Instance Inventory Report"
                    ng-click="inventoryDetailsVm.getInventoryReportByInstance()">
                <i style="font-size: 18px;" class="fa fa-print"></i>
            </button>
        </div>
        <free-text-search ng-show="inventoryDetailsVm.selectedBom != null"
                          on-clear="inventoryDetailsVm.resetBomSearch"
                          search-term="inventoryDetailsVm.searchBomText"
                          on-search="inventoryDetailsVm.searchBom"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <%--<div class="split-pane fixed-left">
            <div class="split-pane-component" style="width: 250px; padding: 10px;">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div>
                        <input type="search" class="form-control input-sm search-form" placeholder="Search"
                               ng-model="inventoryDetailsVm.searchValue" ng-change="inventoryDetailsVm.searchTree()">
                    </div>
                    <ul id="classificationMainTree" class="easyui-tree" close-text="Close">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider" style="left: 250px;"></div>
            <div class="split-pane-component split-right-pane noselect"
                 style="left: 250px;right: -10px !important;top: -10px !important;height: 103%;">
                
            </div>
        </div>--%>
        <div class="inventory-info-panel" id="inventoryInfoPanel" style="padding: 0px">
            <div class="info-panel-header">
                <h3>Inventory</h3>
                <a href="" ng-click="inventoryDetailsVm.showInventoryInfoPanel()"
                   class="close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="info-panel-details">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div>
                        <input type="search" class="form-control input-sm search-form" placeholder="Search"
                               ng-model="inventoryDetailsVm.searchValue" ng-change="inventoryDetailsVm.searchTree()">
                    </div>
                    <ul id="classificationMainTree" class="easyui-tree" close-text="Close">
                    </ul>
                </div>
            </div>
        </div>
        <div class="responsive-table" style="padding-left: 10px;" ng-show="inventoryDetailsVm.selectedBom != null">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 25px;"></th>
                    <th class="threeHundred-column">Nomenclature</th>
                    <%--<th style="width: 100px;">Item Code</th>--%>
                    <th>Type</th>
                    <th>Drawing Number</th>
                    <th style="width: 100px;">Revision</th>
                    <th style="width: 100px;">Units</th>
                    <th style="text-align: center;width: 100px;">BOM Qty</th>

                    <%--------------------------------  BOM Columns  -------------------------------%>

                    <%--<th ng-show="bom" style="text-align: center;">UPN</th>--%>
                    <th ng-show="bom" style="text-align: center;width: 100px;">Stock</th>
                    <th ng-show="bom" style="text-align: center;width: 100px;">On Hold</th>
                    <th ng-show="bom" style="text-align: center;width: 100px;">Returned</th>

                    <%--------------------------------  Bom Instance Columns  ----------------------%>

                    <th ng-show="instance" style="text-align: center;width: 100px;">Allocated</th>
                    <th ng-show="instance" style="text-align: center;width: 100px;">Requested</th>
                    <th ng-show="instance" style="text-align: center;width: 100px;">Issued</th>
                    <%--<th ng-show="instance" style="width: 120px;text-align: center;">Serial Numbers</th>--%>

                </tr>
                </thead>
                <tbody>
                <tr ng-if="inventoryDetailsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Items...</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="inventoryDetailsVm.loading == false && inventoryDetailsVm.bomItems.length == 0">
                    <td colspan="25">No Items</td>
                </tr>
                <tr ng-repeat="inv in inventoryDetailsVm.bomItems" ng-class="{'selected': item.selected}">
                    <td style="width: 25px">
                        <i ng-if="(inv.item.bomItemType == 'SECTION' || inv.item.bomItemType == 'COMMONPART') && bom"
                           style="font-size: 18px;" class="fa fa-print"
                           title="Click to Print Section Inventory Report"
                           ng-click="inventoryDetailsVm.getInventoryReportBySection(inv.item)"></i>
                        <i ng-if="(inv.item.bomItemType == 'SECTION' || inv.item.bomItemType == 'COMMONPART') && instance"
                           style="font-size: 18px;" class="fa fa-print"
                           title="Click to Print Section Inventory Report"
                           ng-click="inventoryDetailsVm.getInventoryReportByInstanceSection(inv.item)"></i>
                    </td>
                    <td class="threeHundred-column">
                        <p class="level{{inv.level}}" ng-click="inventoryDetailsVm.toggleNode(inv)"
                           title="{{inv.expanded ? 'Collapse':'Expand'}}" style="margin: 0">
                            <i ng-if="inv.item.children.length > 0" class="mr5 fa"
                               style="cursor: pointer; color: #909090;font-size: 18px;"
                               title="{{inv.expanded ? 'Collapse':'Expand'}}"
                               ng-class="{'fa-caret-right': (inv.expanded == false || inv.expanded == null || inv.expanded == undefined),
                                                'fa-caret-down': inv.expanded == true}"></i>
                            <span ng-if="inv.item.bomItemType != 'PART'">{{inv.item.typeRef.name}}
                                <span ng-if="inv.item.typeRef.versity"> ( VSPL )</span>
                            </span>
                        </p>

                        <p class="level{{inv.level}}" ng-if="inv.item.bomItemType == 'PART'" style="margin: 0">
                            <a ui-sref="app.items.details({itemId: inv.item.item.id})"
                               title="Click to show Item details">
                                <span ng-bind-html="inv.item.item.itemMaster.itemName | highlightText: inventoryDetailsVm.searchBomText"></span>
                                {{inv.item.item.partSpec.specName}}</a>
                        </p>
                    </td>
                    <%-- <td style="width: 100px;">
                         <span ng-if="inv.item.bomItemType != 'PART'">{{inv.item.typeRef.code}}</span>
                         <span ng-if="inv.item.bomItemType == 'PART'">{{inv.item.item.itemMaster.itemCode}}</span>
                     </td>--%>
                    <td>
                        <bom-group-type ng-if="inv.item.bomItemType != 'PART'"
                                        object="inv.item.typeRef"></bom-group-type>
                        <span ng-if="inv.item.bomItemType == 'PART'">{{inv.item.item.itemMaster.parentType.name}}</span>
                    </td>
                    <td>{{inv.item.item.drawingNumber}}</td>
                    <td>{{inv.item.item.revision}}</td>
                    <td>{{inv.item.item.itemMaster.itemType.units}}</td>
                    <td style="text-align: center;">
                                <span class="badge badge-primary" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.item.quantity}}</span>
                                <span class="badge badge-primary" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.item.fractionalQuantity}}</span>
                    </td>

                    <%--------------------------------------------  BOM Columns -------------------------------------------------------%>

                    <%--<td ng-show="bom" style="text-align: center;">
                        <a ng-if="inv.item.bomItemType == 'PART' && inv.itemInstances.length > 0"
                           title="Click to show Part Numbers"
                           class="badge badge-secondary" style="font-size: 14px;"
                           uib-popover-template="inventoryDetailsVm.partNumberPopOver.templateUrl"
                           popover-append-to-body="true"
                           popover-popup-delay="50"
                           popover-placement="top-right"
                           popover-title="Part Numbers"
                           popover-trigger="'outsideClick'">{{inv.itemInstances.length}}
                        </a>
                                <span ng-if="inv.item.bomItemType == 'PART' && inv.itemInstances.length == 0"
                                      class="badge badge-secondary" style="font-size: 14px;">{{inv.itemInstances.length}}
                                </span>
                    </td>--%>
                    <%--<td ng-show="bom" style="text-align: center;">
                                <span class="badge badge-success" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.stock}}</span>
                                <span class="badge badge-success" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.fractionalStock}}</span>
                    </td>--%>
                    <td class="hundred-column" ng-show="bom" style="text-align: center;">
                        <span>
                            <a ng-if="inv.item.bomItemType == 'PART' && inv.itemInstances.length > 0"
                               title="Click to show Part Numbers"
                               class="badge badge-success" style="font-size: 14px;"
                               uib-popover-template="inventoryDetailsVm.partNumberPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Part Numbers"
                               popover-trigger="'outsideClick'">
                                <span ng-if="!inv.item.item.itemMaster.itemType.hasLots" style="font-size: 14px;">{{inv.stock}}</span>
                                <span ng-if="inv.item.item.itemMaster.itemType.hasLots" style="font-size: 14px;">{{inv.fractionalStock}}</span>
                            </a>
                            <span ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots && inv.itemInstances.length == 0"
                                  class="badge badge-success" style="font-size: 14px;">{{inv.stock}}
                            </span>
                            <span ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.itemInstances.length == 0"
                                  class="badge badge-success" style="font-size: 14px;">{{inv.fractionalStock}}
                            </span>
                        </span>
                        <%--<span>
                            <a ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.stockInstances.length > 0"
                               title="Click to show UPNs" style="font-size: 14px;"
                               class="badge badge-success"
                               uib-popover-template="inventoryDetailsVm.stockInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="Stock UPN"
                               popover-trigger="'outsideClick'">{{inv.fractionalStock}}
                            </a>
                             <span ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.stockInstances.length == 0"
                                   class="badge badge-success" style="font-size: 14px;">{{inv.fractionalStock}}
                            </span>
                        </span>--%>
                    </td>

                    <td class="hundred-column" ng-show="bom" style="text-align: center;">
                        <span>
                            <a ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots && inv.onHoldInstances.length > 0"
                               title="Click to show UPNs" style="font-size: 14px;"
                               class="badge badge-warning"
                               uib-popover-template="inventoryDetailsVm.onHoldInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="OnHold UPN"
                               popover-trigger="'outsideClick'">{{inv.onHold}}
                            </a>
                            <span ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots && inv.onHoldInstances.length == 0"
                                  class="badge badge-warning" style="font-size: 14px;">{{inv.onHold}}
                            </span>
                        </span>
                        <span>
                            <a ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.onHoldInstances.length > 0"
                               title="Click to show UPNs" style="font-size: 14px;"
                               class="badge badge-warning"
                               uib-popover-template="inventoryDetailsVm.onHoldInstancesPopOver.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="top-right"
                               popover-title="OnHold UPN"
                               popover-trigger="'outsideClick'">{{inv.fractionalOnHold}}
                            </a>
                            <span ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.onHoldInstances.length == 0"
                                  class="badge badge-warning" style="font-size: 14px;">{{inv.fractionalOnHold}}
                            </span>
                        </span>
                    </td>
                    <%--<td ng-show="bom" style="text-align: center;">
                                <span class="badge badge-warning" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.onHold}}</span>
                                <span class="badge badge-warning" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.fractionalOnHold}}</span>
                    </td>--%>
                    <td ng-show="bom" style="text-align: center;">
                        <a ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots && inv.returnInstances.length > 0"
                           title="Click to show Returned" style="font-size: 14px;"
                           class="badge badge-danger"
                           uib-popover-template="inventoryDetailsVm.returnPopOver.templateUrl"
                           popover-append-to-body="true"
                           popover-popup-delay="50"
                           popover-placement="top-right"
                           popover-title="Part Numbers"
                           popover-trigger="'outsideClick'">{{inv.returnInstances.length}}
                        </a>
                        <a ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.fractionalReturned > 0.0"
                           title="Click to show Returned" style="font-size: 14px;"
                           class="badge badge-danger"
                           uib-popover-template="inventoryDetailsVm.returnPopOver.templateUrl"
                           popover-append-to-body="true"
                           popover-popup-delay="50"
                           popover-placement="top-right"
                           popover-title="Part Numbers"
                           popover-trigger="'outsideClick'">{{inv.fractionalReturned}}
                        </a>
                        <span ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots && inv.returnInstances.length == 0"
                              class="badge badge-danger" style="font-size: 14px;">0
                        </span>
                        <span ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.fractionalReturned == 0.0"
                              class="badge badge-danger" style="font-size: 14px;">0
                        </span>
                    </td>

                    <%-----------------------------------------  BOM Instance Columns ---------------------------------------------%>

                    <td ng-show="instance" style="text-align: center;">
                        <span class="badge badge-secondary" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART'">{{inv.allocatedQty}}</span>

                    </td>
                    <td ng-show="instance" style="text-align: center;">
                                <span class="badge badge-warning" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.requested}}</span>
                                <span class="badge badge-warning" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.fractionalRequested}}</span>
                    </td>
                    <td ng-show="instance" style="text-align: center;">
                        <a ng-if="inv.item.bomItemType == 'PART' && inv.issuedInstances.length > 0 && !inv.item.item.itemMaster.itemType.hasLots"
                           title="Click to show Issued" style="font-size: 14px;"
                           class="badge badge-success"
                           uib-popover-template="inventoryDetailsVm.issuedInstancesPopOver.templateUrl"
                           popover-append-to-body="true"
                           popover-popup-delay="50"
                           popover-placement="top-right"
                           popover-title="Issued UPN"
                           popover-trigger="'outsideClick'">{{inv.issuedInstances.length}}
                        </a>
                        <a ng-if="inv.item.bomItemType == 'PART' && inv.issuedLotInstances.length > 0 && inv.item.item.itemMaster.itemType.hasLots"
                           title="Click to show Issued" style="font-size: 14px;"
                           class="badge badge-success"
                           uib-popover-template="inventoryDetailsVm.issuedInstancesPopOver.templateUrl"
                           popover-append-to-body="true"
                           popover-popup-delay="50"
                           popover-placement="top-right"
                           popover-title="Issued UPN"
                           popover-trigger="'outsideClick'">{{inv.totalLotIssued}}
                        </a>
                                <span class="badge badge-success" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && inv.issuedInstances.length == 0 && inv.issuedLotInstances.length == 0">0</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>


        <div style="display: none">
            <table class="table table-striped highlight-row" id="printInventoryReport">
                <thead>
                <tr>
                    <th class="threeHundred-column">Nomenclature</th>
                    <th>Type</th>
                    <%--<th>Drawing Number</th>--%>
                    <th>Units</th>
                    <th style="text-align: center;">BOM Qty</th>

                    <%--------------------------------  BOM Columns  -------------------------------%>

                    <th ng-show="bom" style="text-align: center;">Stock</th>
                    <th ng-show="bom" style="text-align: center;">On Hold</th>
                    <th ng-show="bom" style="text-align: center;">Returned</th>

                    <%--------------------------------  Bom Instance Columns  ----------------------%>

                    <th ng-show="instance" style="text-align: center;">Allocated</th>
                    <th ng-show="instance" style="text-align: center;">Requested</th>
                    <th ng-show="instance" style="text-align: center;">Issued</th>

                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="inv in inventoryDetailsVm.printBomItems">
                    <td class="threeHundred-column">
                        <p class="level{{inv.item.level}}" ng-if="inv.item.bomItemType != 'PART'"
                           style="margin: 0;">
                            {{inv.item.typeRef.name}}<span ng-if="inv.item.typeRef.versity"> ( VSPL )</span>
                        </p>

                        <p class="level{{inv.item.level}}" ng-if="inv.item.bomItemType == 'PART'" style="margin: 0">
                            {{inv.item.item.itemMaster.itemName}} {{inv.item.item.partSpec.specName}}
                        </p>
                    </td>
                    <td>
                        <bom-group-type ng-if="inv.item.bomItemType != 'PART'"
                                        object="inv.item.typeRef"></bom-group-type>
                        <span ng-if="inv.item.bomItemType == 'PART'">{{inv.item.item.itemMaster.parentType.name}}</span>
                    </td>
                    <%--<td>{{inv.item.item.drawingNumber}}</td>--%>
                    <td>{{inv.item.item.itemMaster.itemType.units}}</td>
                    <td style="text-align: center;">
                        <span class="badge badge-primary" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.item.quantity}}</span>
                        <span class="badge badge-primary" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.item.fractionalQuantity}}</span>
                    </td>

                    <%--------------------------------------------  BOM Columns -------------------------------------------------------%>

                    <td ng-show="bom" style="text-align: center;">
                        <span class="badge badge-success" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.stock}}</span>
                        <span class="badge badge-success" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.fractionalStock}}</span>
                    </td>
                    <td ng-show="bom" style="text-align: center;">
                        <span class="badge badge-warning" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.onHold}}</span>
                        <span class="badge badge-warning" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.fractionalOnHold}}</span>
                    <td ng-show="bom" style="text-align: center;">
                        <span ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots && inv.returnInstances.length > 0">
                            {{inv.returnInstances.length}}
                        </span>
                        <span ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots && inv.returnInstances.length == 0"
                              class="badge badge-danger" style="font-size: 14px;">{{inv.returnInstances.length}}
                        </span>

                        <span ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.fractionalReturned > 0">
                            {{inv.fractionalReturned}}
                        </span>
                        <span ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots && inv.fractionalReturned == 0.0"
                              class="badge badge-danger" style="font-size: 14px;">0
                        </span>
                    </td>

                    <%-----------------------------------------  BOM Instance Columns ---------------------------------------------%>

                    <td ng-show="instance" style="text-align: center;">
                        <span class="badge badge-secondary" style="font-size: 14px;"
                              ng-if="inv.item.bomItemType == 'PART'">{{inv.allocatedQty}}</span>

                    </td>
                    <td ng-show="instance" style="text-align: center;">
                                <span class="badge badge-warning" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && !inv.item.item.itemMaster.itemType.hasLots">{{inv.requested}}</span>
                                <span class="badge badge-warning" style="font-size: 14px;"
                                      ng-if="inv.item.bomItemType == 'PART' && inv.item.item.itemMaster.itemType.hasLots">{{inv.fractionalRequested}}</span>
                    </td>
                    <td ng-show="instance" style="text-align: center;">
                            <span ng-if="inv.item.bomItemType == 'PART' && inv.issuedInstances.length > 0 && !inv.item.item.itemMaster.itemType.hasLots">
                                {{inv.issuedInstances.length}}
                            </span>
                            <span ng-if="inv.item.bomItemType == 'PART' && inv.issuedLotInstances.length > 0 && inv.item.item.itemMaster.itemType.hasLots">
                                {{inv.issuedLotInstances.length}}
                            </span>
                            <span class="badge badge-success" style="font-size: 14px;"
                                  ng-if="inv.item.bomItemType == 'PART' && inv.issuedInstances.length == 0 && inv.issuedLotInstances.length == 0">0</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
