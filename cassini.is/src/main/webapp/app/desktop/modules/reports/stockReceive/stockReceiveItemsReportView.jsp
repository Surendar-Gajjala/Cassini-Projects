<style scoped>
    .search-element1 {
        right: 0 !important;
    }

    .view-content {
        position: relative;
    }

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-y: visible;
        overflow-x: visible;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -15px;
        background-color: #fff;
    }

</style>
<link href="app/assets/css/app/desktop/searchBox.css" rel="stylesheet" type="text/css">
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <button ng-disabled="stockReceiveVm.selectedStore == null  || stockReceiveVm.reportRows.length == 0"
                    class="btn btn-sm btn-default min-width" ng-click="stockReceiveVm.exportReport()">Export
            </button>
            <button class="min-width btn btn-sm btn-success"
                    ng-disabled="stockReceiveVm.selectedStore == null || stockReceiveVm.reportRows.length == 0"
                    ng-click="stockReceiveVm.receiveAttributeSearch()">Show Attributes
            </button>
            <button class="min-width btn btn-sm btn-success" ng-disabled="stockReceiveVm.searchMode != true"
                    ng-click="stockReceiveVm.loadStores()">Clear Search
            </button>
            <div class="search-element1 search-input-container inner-addon right-addon">
                <input type="search" style="border: 1px solid lightgrey;"
                       class="form-control input-sm search-form"
                       placeholder="Select Store"
                       onfocus="this.setSelectionRange(0, this.value.length)"
                       ng-model="stockReceiveVm.filters.searchQuery"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="stockReceiveVm.loadStores()">
                <i class="fa fa-shopping-cart" ng-click="stockReceiveVm.loadStores()"></i>
            </div>
            <div id="search-results-container1" class="search-element1 search-results-container"
                 ng-mouseover="selectedRow = null"
                 style="overflow-y: auto;">
                <div ng-if="stockReceiveVm.stores.length == 0" style="padding: 20px;">
                    <h5>No results found</h5>
                </div>
                <div class="result-item" ng-repeat="item in stockReceiveVm.stores"
                     ng-click="stockReceiveVm.openItemDetails(item)">
                    <div class="result-item-row" ng-class="{'selected':$index == selectedRow}"
                         style="padding: 5px 10px;">
                        <span>{{item.storeName}}</span>
                    </div>
                </div>
            </div>
            <div class="search-date1 inner-addon right-addon">
                <input style="border:1px solid #ddd;background: #fdfdfd;" type="text"
                       class="form-control input-sm search-form"
                       date-picker
                       ng-model="stockReceiveVm.startDate"
                       ng-change="stockReceiveVm.searchReport()"
                       ng-able="stockReceiveVm.selectedStore == null"
                       name="startDate" placeholder="Start Date">
                <i class="fa fa-calendar"></i>
            </div>
            <div class="search-date2 inner-addon right-addon">
                <input style="border:1px solid #ddd;background: #fdfdfd;" type="text"
                       class="form-control input-sm search-form"
                       date-picker
                       ng-model="stockReceiveVm.endDate"
                       ng-change="stockReceiveVm.searchReport()"
                       ng-able="stockReceiveVm.selectedStore == null"
                       name="endDate" placeholder="Finish Date">
                <i class="fa fa-calendar"></i>
            </div>
            <button ng-show="stockReceiveVm.selectedStore != null"
                    style="margin-right: 10px;" title="Search Records"
                    class="btn btn-sm btn-default min-width pull-right"
                    ng-click="stockReceiveVm.searchReport()">Search<i style="margin-left: 10px"
                                                                      class="fa fa-search"></i>
            </button>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Item No.</th>
                    <th>Item Name</th>
                    <th>Units</th>
                    <th>Received Qty</th>
                    <th style="text-align: center">Supplier Name</th>
                    <th style="text-align: center">Opening Bal as on {{stockReceiveVm.startDate}}</th>
                    <th style="text-align: center">Closing Bal as on {{stockReceiveVm.endDate}}</th>
                    <th style="text-align: center"
                        ng-repeat="attribute in stockReceiveVm.itemAttributesList">
                        {{attribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="stockReceiveVm.removeAttribute(attribute)"
                           title="Remove this column"></i>
                    </th>

                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="width: 50px;"></td>
                    <td style="text-align: center; width: 150px;">
                        <input type="text" style="height: 30px;" class="form-control btn-sm" name="Store Name"
                               ng-change="stockReceiveVm.applyFilters()" placeholder="Number"
                               ng-model="stockReceiveVm.emptyFilters.itemNumber"
                               ng-disabled="stockReceiveVm.selectedStore == null">
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <input type="text" class="form-control btn-sm" name="Store Name"
                               ng-change="stockReceiveVm.applyFilters()" placeholder="Item Name"
                               ng-model="stockReceiveVm.emptyFilters.itemName"
                               ng-disabled="stockReceiveVm.selectedStore == null">
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <input type="text" class="form-control btn-sm" name="Units"
                               ng-change="stockReceiveVm.applyFilters()" placeholder="units"
                               ng-model="stockReceiveVm.emptyFilters.units"
                               ng-disabled="stockReceiveVm.selectedStore == null">
                    </td>

                    <td></td>

                    <td></td>
                    <td></td>
                    <td></td>

                    <td ng-repeat="attribute in stockReceiveVm.itemAttributesList" style="min-width: 150px;">
                        <input ng-if="attribute.dataType == 'TEXT' " type="text" style="height: 30px;"
                               class="btn-sm form-control"
                               ng-change="stockReceiveVm.attributeFilter(attribute)"
                               ng-model="attribute.textValue"
                               ng-disabled="stockReceiveVm.selectedStore == null">
                        <input ng-if="attribute.dataType == 'INTEGER' " type="number" style="height: 30px;"
                               class="btn-sm form-control"
                               ng-change="stockReceiveVm.attributeFilter(attribute)"
                               ng-model="attribute.integerValue"
                               ng-disabled="stockReceiveVm.selectedStore == null">
                        <input type="text" ng-if="attribute.dataType == 'DOUBLE'"
                               ng-model="attribute.doubleValue" class="form-control"
                               ng-change="stockReceiveVm.attributeFilter(attribute)"
                               ng-disabled="stockReceiveVm.selectedStore == null">
                        <%--<input type="date" ng-if="attribute.dataType == 'DATE'"--%>
                        <%--style="padding: 0 !important; line-height: 32px;"--%>
                        <%--ng-model="attribute.dateValue"--%>
                        <%--class="form-control" id="inpDate" name="inpDate"--%>
                        <%--ng-change="stockReceiveVm.attributeFilter(attribute)">--%>
                        <%--<input type="text" ng-if="attribute.dataType == 'TIME'" time-picker--%>
                        <%--style="padding: 0 !important; line-height: 32px;"--%>
                        <%--ng-model="attribute.timeValue"--%>
                        <%--class="form-control"--%>
                        <%--ng-change="stockReceiveVm.attributeFilter(attribute)">--%>

                        <div ng-if="attribute.dataType == 'LIST'"
                             style="padding-right: 0px;">
                            <select class="form-control" ng-model="attribute.listValue"
                                    placeholder="select" style="padding: 6px !important;"
                                    ng-change="stockReceiveVm.attributeFilter(attribute)"
                                    ng-options="value for value in attribute.lov.values"
                                    ng-disabled="stockReceiveVm.selectedStore == null">
                            </select>
                        </div>
                        <div ng-if="attribute.dataType == 'BOOLEAN'">
                            <select class="form-control" ng-model="attribute.booleanValue"
                                    style="padding: 6px !important;"
                                    placeholder="select" ng-change="stockReceiveVm.attributeFilter(attribute)"
                                    ng-options="value for value in ['true', 'false']"
                                    ng-disabled="stockReceiveVm.selectedStore == null">
                            </select>
                        </div>
                        <div ng-if="attribute.dataType == 'CURRENCY'">
                            <input class="form-control" name="currencyValue" type="number"
                                   placeholder="Enter currency value"
                                   ng-change="stockReceiveVm.attributeFilter(attribute)"
                                   ng-model="attribute.currencyValue"
                                   ng-disabled="stockReceiveVm.selectedStore == null"/>
                        </div>
                    </td>

                </tr>
                <%--   <tr ng-if="stockReceiveVm.loading == true">
                       <td colspan="7">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading Report...
                           </span>
                       </td>
                   </tr>--%>
                <tr ng-if="stockReceiveVm.selectedStore == null && stockReceiveVm.searchMode == false">
                    <td colspan="12">Please select store and click on search</td>
                </tr>
                <tr ng-if="stockReceiveVm.loading == false && stockReceiveVm.reportRows.length == 0 && stockReceiveVm.selectedStore != null && stockReceiveVm.searchMode == true">
                    <td colspan="12">No Records found</td>
                </tr>
                <tr ng-repeat="row in stockReceiveVm.reportRows" ng-if="!stockReceiveVm.loading">
                    <td>{{row.date}}</td>
                    <td>{{row.itemNumber}}</td>
                    <td style="vertical-align: middle;width: 300px;max-width:300px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{row.itemName}}">{{row.itemName}}
                    </td>
                    <td>{{row.units}}</td>
                    <td>{{row.quantity}}</td>
                    <td style="text-align: center">{{row.supplierName}}</td>
                    <td style="text-align: center">{{row.openingBalance != null ? row.openingBalance: 0 }}</td>
                    <td style="text-align: center">{{row.closingBalance != null ? row.closingBalance: 0}}</td>
                    <td class="added-column" style="text-align: center"
                        ng-repeat="objectAttribute in stockReceiveVm.itemAttributesList">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="row[attrName].length > 0" href="">
                                    {{row[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in row[attrName]">
                                        <a href="" ng-click="storeInventoryVm.openAttachment(attachment)"
                                           title="Click to download file"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">
                            <a href=""
                               title="Click to show large Image">
                                <img ng-if="row[attrName] != null"
                                     ng-src="{{row[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="myModal2" class="img-model modal">
                                <span class="closeImage12">&times;</span>
                                <img class="modal-content" id="img12">
                            </div>
                        </div>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name">
                            {{row[attrName]}}
                        </p>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name">
                            {{row[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="row[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{row[attrName]}}
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>