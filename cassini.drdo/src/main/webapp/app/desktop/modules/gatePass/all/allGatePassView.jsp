<style scoped>
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

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    table {
        table-layout: fixed;
    }

    .gatePassLength {
        display: run-in;
        word-wrap: break-word;
        width: 200px;
        white-space: normal !important;
        text-align: left;
    }

    .widget {
        padding: 10px 20px 0 20px !important;
        cursor: pointer;
        vertical-align: middle;
    }

    .filter-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 10; /* Sit on top */
        padding-top: 50px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .filter-model .report-content {
        margin-left: auto;
        margin-right: auto;
        top: 50px;
        display: block;
        height: 60%;
        width: 60%;
    }

    a.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #121C25;
        font-size: 20px !important;
    }

    .new-gatePass {
        font-weight: 600;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 0 !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button ng-if="hasPermission('permission.gatePass.new')"
                    class="btn btn-sm btn-success" ng-click="allGatePassVm.showNewGatePass()">New Gate Pass
            </button>

            <span class="widget"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.storeApprove')">
                <a ng-class="{'activeView':allGatePassVm.gatePassFilter.finish}"
                   title="{{allGatePassVm.gatePassFilter.finish ? 'Click for Gate Passes' : 'Click for Finished Gate Passes'}}"
                   ng-click="allGatePassVm.changeView()" style="text-decoration: none !important;">
                    <i ng-if="!allGatePassVm.gatePassFilter.finish" class="fa fa-square-o"></i>
                    <i ng-if="allGatePassVm.gatePassFilter.finish" class="fa fa-check-square-o"></i>
                    Finished
                </a>
            </span>
        </div>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;margin-top: 5px;"
           ng-click="allGatePassVm.addFilter()"
           ng-if="!allGatePassVm.filterMode">
            <i title="Date Filter" style="font-size: 24px;" class="fa fa-filter"></i></a>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;" ng-click="allGatePassVm.clearDateFilter()"
           ng-if="allGatePassVm.filterMode">
            <i title="Clear Filter" style="font-size: 30px;color:red;" class="fa fa-filter"></i></a>
        <free-text-search on-clear="allGatePassVm.resetPage" search-term="allGatePassVm.searchText"
                          on-search="allGatePassVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="gatePassLength">Gate Pass Name</th>
                    <th class="gatePassLength">Gate Pass Number</th>
                    <th class="gatePassLength">Gate Pass Date</th>
                    <th class="gatePassLength">Created By</th>
                    <th class="gatePassLength">Created Date</th>
                    <th style="width: 100px;"
                        ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.storeApprove')">
                        Finish
                    </th>
                    <th style="width: 75px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allGatePassVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading GatePass...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allGatePassVm.loading == false && allGatePassVm.gatePasses.content.length == 0">
                    <td colspan="25"
                        ng-if="allGatePassVm.filterMode && allGatePassVm.gatePassFilter.fromDate != null && allGatePassVm.gatePassFilter.toDate != null">
                        No GatePass found between {{allGatePassVm.gatePassFilter.fromDate}} -
                        {{allGatePassVm.gatePassFilter.toDate}}
                    </td>
                    <td colspan="25" ng-if="allGatePassVm.filterMode && allGatePassVm.gatePassFilter.month != null">
                        No GatePass found in {{allGatePassVm.gatePassFilter.month}}
                    </td>
                    <td colspan="25" ng-if="!allGatePassVm.filterMode">No GatePass</td>
                </tr>

                <tr ng-repeat="gatePass in allGatePassVm.gatePasses.content"
                    ng-class="{'new-gatePass':gatePass.inwards == 0}">
                    <td class="twentyPercent-column">
                        <a href="" ng-click="allGatePassVm.downloadGatePass(gatePass)"
                           title="Click to download Gate Pass">
                            <span ng-bind-html="gatePass.gatePass.name | highlightText: freeTextQuery"></span>
                            <span ng-if="gatePass.showNew && gatePass.inwards == 0"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                        </a>
                    </td>
                    <td class="twentyPercent-column"><span
                            ng-bind-html="gatePass.gatePassNumber | highlightText: freeTextQuery"></span></td>
                    <td class="tenPercent-column">{{gatePass.gatePassDate}}</td>
                    <td class="twentyPercent-column">{{gatePass.createdByObject.fullName}}</td>
                    <td class="tenPercent-column">{{gatePass.createdDate}}</td>
                    <td class="tenPercent-column"
                        ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.storeApprove')">
                        <input type="checkbox" ng-model="gatePass.finish"
                               title="{{!gatePass.finish ? 'Click to finish Gate Pass' : ''}}"
                               ng-click="allGatePassVm.finishGatePass(gatePass)">
                    </td>
                    <td class="tenPercent-column">
                        <button class="btn btn-xs btn-primary" ng-click="allGatePassVm.showGatePassItems(gatePass)">
                            <i class="fa fa-list" title="Click to show Details"></i>
                        </button>
                        <button class="btn btn-xs btn-danger" ng-click="allGatePassVm.deleteGatePass(gatePass)"
                                ng-disabled="!hasPermission('permission.gatePass.delete')">
                            <i class="fa fa-trash" title="Delete"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="table-footer">
            <div>
                <div>
                    <h5>
                        Displaying {{allGatePassVm.gatePasses.numberOfElements}} of
                        {{allGatePassVm.gatePasses.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{allGatePassVm.gatePasses.totalElements != 0 ? allGatePassVm.gatePasses.number+1:0}} of {{allGatePassVm.gatePasses.totalPages}}</span>
                            <a href="" ng-click="allGatePassVm.previousPage()"
                               ng-class="{'disabled': allGatePassVm.gatePasses.first}">
                                <i class="fa fa-arrow-circle-left mr10"></i>
                            </a>
                            <a href="" ng-click="allGatePassVm.nextPage()"
                               ng-class="{'disabled': allGatePassVm.gatePasses.last}">
                                <i class="fa fa-arrow-circle-right"></i>
                            </a>
                    </span>
                </div>
            </div>
        </div>
    </div>
    <div id="add-filter" class="filter-model modal">
        <div class="report-content" style="background: white;">

            <div style="height: 10%;background: #dad1d1;">
                <h3 style="margin: 0;text-align: center;">Filters</h3>
            </div>
            <div style="height: 80%">

                <div style="display: inline-flex;width: 98%;border: 1px solid lightgrey;padding: 10px;margin: 1%;">
                    <form class="form-inline">
                        <div class="form-group"
                             style="border-right: 1px solid black;padding-right: 10px;margin-right: 0px !important;width: 20%;">
                            <h3 style="margin-top: 5px;">Date Range</h3>
                        </div>
                        <div class="form-group" style="width:35%;margin-right: 0px !important;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">From Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="allGatePassVm.gatePassFilter.fromDate"
                                       placeholder="Select From Date" style="width: 100%"
                                       class="form-control" date-time-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width:35%;margin-right: 0px !important;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">To Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="allGatePassVm.gatePassFilter.toDate"
                                       placeholder="Select To Date" style="width: 100%"
                                       class="form-control" date-time-picker>
                            </div>
                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="allGatePassVm.getFilterResults()">Search
                            </button>
                        </div>
                    </form>
                </div>

                <div style="display: inline-flex;width: 98%;border: 1px solid lightgrey;padding: 10px;margin: 1%;">
                    <form class="form-inline" style="width: 100%;">
                        <div class="form-group"
                             style="border-right: 1px solid black;padding-right: 10px;margin-right: 0px !important;width: 21%;">
                            <h3 style="margin-top: 5px;">Month Range</h3>
                        </div>
                        <div class="form-group" style="width: 40%;margin-right: 0px !important;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">Select Month : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="allGatePassVm.gatePassFilter.month"
                                       placeholder="Select From Date" style="width: 100%;"
                                       class="form-control" month-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width: 25%;margin-right: 0px !important;">

                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="allGatePassVm.getMonthResults()">Search
                            </button>
                        </div>
                    </form>
                </div>

                <p ng-show="allGatePassVm.errorMessage != null"
                   style="color: darkred;font-weight: 600;font-size: 14px;margin-left:15px">
                    {{allGatePassVm.errorMessage}}</p>
            </div>
            <div style="height: 10%;background-color: lightgrey;text-align: center;">
                <div class="btn-group" style="margin-top: 5px;">
                    <button ng-click="allGatePassVm.cancelFilter()"
                            class="btn btn-xs btn-danger">Close
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
