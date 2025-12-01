<style>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 0px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
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

    .itemName {
        display: run-in;
        word-wrap: break-word;
        min-width: 400px !important;
        white-space: normal !important;
        text-align: left;
    }

</style>
<link href="app/assets/css/app/desktop/searchBox.css" rel="stylesheet" type="text/css">
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button ng-disabled="projRepVm.selectedProject == null || projRepVm.reportRows.length == 0"
                        class="btn btn-sm btn-default min-width"
                        ng-click="projRepVm.exportReport()">Export
                </button>
            </div>
            <div class="search-element1 search-input-container inner-addon right-addon">
                <input type="search" style="border: 1px solid lightgrey;"
                       class="form-control input-sm search-form"
                       placeholder="Search"
                       onfocus="this.setSelectionRange(0, this.value.length)"
                       ng-model="projRepVm.filters.searchQuery"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="projRepVm.loadProjects()">
                <a><i class="fa fa-search" ng-click="projRepVm.searchReport()"></i></a>
            </div>
            <div id="search-results-container" class="search-element1 search-results-container"
                 ng-mouseover="selectedRow = null"
                 style="overflow-y: auto;">
                <div ng-if="projRepVm.projects.length == 0" style="padding: 20px;">
                    <h5>No results found</h5>
                </div>
                <div class="result-item" ng-repeat="item in projRepVm.projects"
                     ng-click="projRepVm.openProjectDetails(item)">
                    <div class="result-item-row" ng-class="{'selected':$index == selectedRow}"
                         style="padding: 5px 10px;">
                        <span>{{item.name}}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th></th>
                    <th>Item No.</th>
                    <th class="itemName">Item</th>
                    <th>Unit</th>
                    <th>BOQ Qty</th>
                    <th>Supplied Qty</th>
                    <th>Supplied Challan</th>
                    <th>Supplied Date</th>
                    <th>Iss Qty</th>
                    <th>Iss Challan</th>
                    <th>Issued Date</th>
                    <th>Return Qty</th>
                    <th>Return Challan</th>
                    <th>Return Date</th>
                    <th>Balance Qty</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="projRepVm.loading == true">
                    <td colspan="15">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Details...
                        </span>
                    </td>
                </tr>
                <tr ng-if="projRepVm.loading == false && projRepVm.reportRows.length == 0 && projRepVm.selectedProject != null">
                    <td colspan="15">No Records found</td>
                </tr>
                <tr ng-if="projRepVm.selectedProject == null">
                    <td colspan="15">Please select project</td>
                </tr>
                <tr ng-repeat="row in projRepVm.reportRows" ng-if="!row.hideRow">
                    <td>
                        <a style="color:#428bca;" href="" ng-click="projRepVm.toggleRow(row)"
                           ng-if="!row.showDetails && !row.hideRow && row.noOfRows > 0">
                            <i class="fa fa-plus-circle"></i>
                        </a>
                        <a style="color:#428bca;" href="" ng-click="projRepVm.toggleRow(row)"
                           ng-if="row.showDetails && row.noOfRows > 0">
                            <i class="fa fa-minus-circle"></i>
                        </a>
                    </td>
                    <td>{{row.itemNo}}</td>
                    <td class="itemName">{{row.item}}</td>
                    <td>{{row.unit}}</td>
                    <td style="text-align: center">{{row.boqQty}}</td>
                    <td style="text-align: center">{{row.suppliedQty}}</td>
                    <td>{{row.suppliedChallan}}</td>
                    <td>{{row.suppliedDate}}</td>
                    <td style="text-align: center">{{row.isuQty}}</td>
                    <td>{{row.isuChallan}}</td>
                    <td>{{row.isuDate}}</td>
                    <td style="text-align: center">{{row.returnQty}}</td>
                    <td>{{row.returnChallan}}</td>
                    <td>{{row.returnDate}}</td>
                    <td style="text-align: center">{{row.balanceQty}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>