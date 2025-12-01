<style>

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
    }

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-y: visible;
        overflow-x: visible;
    }

    .view-content {
        position: relative;
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

    .view-content .responsive-table table tbody #search {
        position: -webkit-sticky;
        position: sticky;
        z-index: 5;
        top: 25px;
        background-color: #fff;
    }



    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }


    .responsive-table .dropdown-content a {
        text-decoration: none;
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content a:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    .responsive-table .dropdown-content i:hover {
        background-color: #0f3ff3;
        color: white !important;

    }


    .responsive-table .dropdown:hover .dropdown-content {
        display: block;
        color: black !important;
    }



</style>
<link href="app/assets/css/app/desktop/searchBox.css" rel="stylesheet" type="text/css">
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <button ng-disabled="storeRepVm.selectedStore == null  || storeRepVm.reportRows.length == 0"
                    class="btn btn-sm btn-default min-width" ng-click="storeRepVm.exportReport()">Export
            </button>
            <div class="search-element1 search-input-container inner-addon right-addon">
                <input type="search" style="border: 1px solid lightgrey;"
                       class="form-control input-sm search-form"
                       placeholder="Search"
                       onfocus="this.setSelectionRange(0, this.value.length)"
                       ng-model="storeRepVm.filters.searchQuery"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="storeRepVm.loadStores()">
                <i class="fa fa-search" ng-click="storeRepVm.loadStores()"></i>
            </div>
            <div id="search-results-container1" class="search-element1 search-results-container"
                 ng-mouseover="selectedRow = null"
                 style="overflow-y: auto;">
                <div ng-if="storeRepVm.stores.length == 0" style="padding: 20px;">
                    <h5>No results found</h5>
                </div>
                <div class="result-item" ng-repeat="item in storeRepVm.stores"
                     ng-click="storeRepVm.openItemDetails(item)">
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
                       ng-model="storeRepVm.startDate"
                       name="startDate" placeholder="Start Date">
                <i class="fa fa-calendar"></i>
            </div>
            <div class="search-date2 inner-addon right-addon">
                <input style="border:1px solid #ddd;background: #fdfdfd;" type="text"
                       class="form-control input-sm search-form"
                       date-picker
                       ng-model="storeRepVm.endDate"
                       name="endDate" placeholder="Finish Date">
                <i class="fa fa-calendar"></i>
            </div>
            <button ng-show="storeRepVm.selectedStore != null"
                    style="margin-right: 10px;" title="Search Records"
                    class="btn btn-sm btn-default min-width pull-right"
                    ng-click="storeRepVm.searchReport()">Search<i style="margin-left: 10px" class="fa fa-search"></i>
            </button>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Item No.</th>
                    <th class="titleData">Item</th>
                    <th>Unit</th>
                    <th style="text-align: center">Opening Qty</th>
                    <th style="text-align: center">Received Qty</th>
                    <th style="text-align: center">Issued Qty</th>
                    <th style="text-align: center">Returned Qty</th>
                    <th style="text-align: center">Closing Qty</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="storeRepVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Stores...
                        </span>
                    </td>
                </tr>
                <tr ng-if="storeRepVm.selectedStore == null && storeRepVm.searchMode == false">
                    <td colspan="12">Please select store and click on search</td>
                </tr>
                <tr ng-if="storeRepVm.searchMode == false && storeRepVm.selectedStore != null">
                    <td colspan="12">Please click on search</td>
                </tr>
                <tr ng-if="storeRepVm.loading == false && storeRepVm.reportRows.length == 0 && storeRepVm.selectedStore != null && storeRepVm.searchMode == true">
                    <td colspan="12">No Records found</td>
                </tr>
                <tr ng-repeat="row in storeRepVm.reportRows">
                    <td>{{row.itemNumber}}</td>
                    <td class="titleData">{{row.item}}</td>
                    <td>{{row.units}}</td>
                    <td style="text-align: center">{{row.openingStock}}</td>
                    <td style="text-align: center">{{row.received}}</td>
                    <td style="text-align: center">{{row.issued}}</td>
                    <td style="text-align: center">{{row.returned}}</td>
                    <td style="text-align: center">{{row.closingStock}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>