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
        /*position: sticky;*/
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

    .filter-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
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

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 0 !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button ng-if="hasPermission('permission.dispatch.new')" class="btn btn-sm btn-success"
                    ng-click="allDispatchVm.newDispatch()">New Dispatch
            </button>
        </div>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;" ng-click="allDispatchVm.addFilter()"
           ng-if="!allDispatchVm.filterMode">
            <i style="font-size: 30px;" title="Date Filter" class="fa fa-filter"></i></a>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;" ng-click="allDispatchVm.clearDateFilter()"
           ng-if="allDispatchVm.filterMode">
            <i title="Clear Filter" style="font-size: 30px;color:red;" class="fa fa-filter"></i></a>
        <free-text-search on-clear="allDispatchVm.resetPage" search-term="allDispatchVm.searchText"
                          on-search="allDispatchVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Dispatch Number</th>
                    <th>BOM</th>
                    <th>Gate Pass Number</th>
                    <th>Status</th>
                    <th>Dispatch Type</th>
                    <th>Dispatch Date</th>
                    <th>Created By</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allDispatchVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Dispatches...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allDispatchVm.loading == false && allDispatchVm.dispatches.content.length == 0">
                    <td colspan="25" ng-if="!allDispatchVm.filterMode">No Dispatches</td>
                    <td colspan="25"
                        ng-if="allDispatchVm.filterMode && allDispatchVm.dispatchFilter.fromDate != null && allDispatchVm.dispatchFilter.toDate != null">
                        No Dispatches found between {{allDispatchVm.dispatchFilter.fromDate}} -
                        {{allDispatchVm.dispatchFilter.toDate}}
                    </td>
                    <td colspan="25" ng-if="allDispatchVm.filterMode && allDispatchVm.dispatchFilter.month != null">No
                        Dispatches found in {{allDispatchVm.dispatchFilter.month}}
                    </td>
                </tr>

                <tr ng-repeat="dispatch in allDispatchVm.dispatches.content">
                    <td>
                        <a href="" title="Click to show dispatch details"
                           ng-click="allDispatchVm.showDispatchDetails(dispatch)">{{dispatch.number}}</a>
                    </td>
                    <td>{{dispatch.bom.item.itemMaster.itemName}}</td>
                    <td>{{dispatch.gatePassNumber}}</td>
                    <td>
                        <span ng-if="dispatch.status == 'NEW'" class="badge badge-primary">{{dispatch.status}}</span>
                        <span ng-if="dispatch.status == 'DISPATCHED'"
                              class="badge badge-success">{{dispatch.status}}</span>
                    </td>
                    <td>
                        <span ng-if="dispatch.type == 'REJECTED'" class="badge badge-danger">{{dispatch.type}}</span>
                        <span ng-if="dispatch.type == 'FAILURE'"
                              class="badge badge-warning">{{dispatch.type}}</span>
                         <span ng-if="dispatch.type == 'FABRICATION'"
                               class="badge badge-success">{{dispatch.type}}</span>
                    </td>
                    <td>{{dispatch.dispatchDate}}</td>
                    <td>{{dispatch.createdByObject.fullName}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>Displaying {{allDispatchVm.dispatches.numberOfElements}} of
                        {{allDispatchVm.dispatches.totalElements}}</h5>--%>
                    <h5>
                        <span style="padding-right: 5px">Displaying</span>
                        <span ng-if="allDispatchVm.dispatches.totalElements == 0">
                            {{(allDispatchVm.pageable.page*allDispatchVm.pageable.size)}}
                        </span>
                        <span ng-if="allDispatchVm.dispatches.totalElements > 0">
                            {{(allDispatchVm.pageable.page*allDispatchVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="allDispatchVm.dispatches.last == false">{{((allDispatchVm.pageable.page+1)*allDispatchVm.pageable.size)}}</span>
                        <span ng-if="allDispatchVm.dispatches.last == true">{{allDispatchVm.dispatches.totalElements}}</span>


                        <span> of </span>{{allDispatchVm.dispatches.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{allDispatchVm.dispatches.totalElements != 0 ? allDispatchVm.dispatches.number+1:0}} of {{allDispatchVm.dispatches.totalPages}}</span>
                    <a href="" ng-click="allDispatchVm.previousPage()"
                       ng-class="{'disabled': allDispatchVm.dispatches.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="allDispatchVm.nextPage()"
                       ng-class="{'disabled': allDispatchVm.dispatches.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
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
                             style="border-right: 1px solid black;padding-right: 10px;margin-right: 0px !important;width: 21%;">
                            <h3 style="margin-top: 5px;">Date Range</h3>
                        </div>
                        <div class="form-group" style="margin-right: 0px !important;width: 35%;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">From Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="allDispatchVm.dispatchFilter.fromDate"
                                       placeholder="Select From Date" style="width: 100%"
                                       class="form-control" date-time-picker/>
                            </div>
                        </div>
                        <div class="form-group" style="margin-right: 0px !important;width: 34%;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">To Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="allDispatchVm.dispatchFilter.toDate"
                                       placeholder="Select To Date" style="width: 100%"
                                       class="form-control" date-time-picker>
                            </div>


                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="allDispatchVm.getFilterResults()">Search
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
                                <input type="text" ng-model="allDispatchVm.dispatchFilter.month"
                                       placeholder="Select From Date" style="width: 100%;"
                                       class="form-control" month-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width: 25%;margin-right: 0px !important;">

                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="allDispatchVm.getMonthResults()">Search
                            </button>
                        </div>
                    </form>
                </div>

                <p ng-show="allDispatchVm.errorMessage != null"
                   style="color: darkred;font-weight: 600;font-size: 14px;margin-left:15px">
                    {{allDispatchVm.errorMessage}}</p>
            </div>
            <div style="height: 10%;background-color: lightgrey;text-align: center;">
                <div class="btn-group" style="margin-top: 5px;">
                    <button ng-click="allDispatchVm.cancelFilter()"
                            class="btn btn-xs btn-danger">Close
                    </button>
                </div>
            </div>


            <%-- <div style="height: 41px;background: #dad1d1;padding-left: 20px;padding-top: 1px;">
                 <h4>Date Range Filter</h4>
             </div>
             <div class="row" style="padding: 10px">
                 From Date: <input date-picker type="text" class="form-control"
                                   placeholder="Select From Date" style="width: 77%"
                                   ng-model="allDispatchVm.dispatchFilter.fromDate"/>
             </div>
             <div class="row" style="padding: 10px">
                 To Date: <input date-picker type="text" class="form-control"
                                 placeholder="Select To Date" style="width: 77%"
                                 ng-model="allDispatchVm.dispatchFilter.toDate"/>
             </div>
             <p ng-show="allDispatchVm.errorMessage != null"
                style="color: darkred;font-weight: 600;font-size: 14px;margin-left:15px">
                 {{allDispatchVm.errorMessage}}</p>

             <div class="row">
                 <div class="btn-group pull-right" style="margin-right:10px;" role="group"
                      aria-label="Basic example">
                     <button style="margin-right: 10px" type="submit" ng-click="allDispatchVm.cancelFilter()"
                             class="btn btn-danger">Cancel
                     </button>
                     <button type="submit" ng-click="allDispatchVm.getFilterResults()" class="btn btn-success">OK
                     </button>
                 </div>
             </div>--%>
        </div>
    </div>
</div>
