<link href="app/assets/css/app/desktop/searchBox.css" rel="stylesheet" type="text/css">
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <button ng-disabled="workRepVm.selectedProjectId == null  || workRepVm.reportRows.length == 0"
                    class="btn btn-sm btn-default min-width" title="Export"
                    ng-click="workRepVm.exportReport()">Export
            </button>
            <div class="search-element1 search-input-container inner-addon right-addon">
                <input type="search" style="border: 1px solid lightgrey;"
                       class="form-control input-sm search-form"
                       placeholder="Search Projects"
                       onfocus="this.setSelectionRange(0, this.value.length)"
                       ng-model="workRepVm.projFilters.searchQuery"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="workRepVm.loadProjects()">
                <i class="fa fa-search" ng-click="workRepVm.loadProjects()"></i>
            </div>
            <div id="search-results-container1" class="search-element1 search-results-container"
                 ng-mouseover="selectedRow = null"
                 style="overflow-y: auto;">
                <div ng-if="workRepVm.projects.length == 0" style="padding: 20px;">
                    <h5>No results found</h5>
                </div>
                <div class="result-item" ng-repeat="item in workRepVm.projects"
                     ng-click="workRepVm.openProjectDetails(item)">
                    <div class="result-item-row" ng-class="{'selected':$index == selectedRow}"
                         style="padding: 5px 10px;">
                        <span>{{item.name}}</span>
                    </div>
                </div>
            </div>
            <div class="search-date1 inner-addon right-addon">
                <input style="border:1px solid #ddd;background: #fdfdfd;" type="text"
                       class="form-control input-sm search-form"
                       date-picker
                       ng-model="workRepVm.startDate"
                       name="startDate" placeholder="Start Date">
                <i class="fa fa-calendar"></i>
            </div>
            <div class="search-date2 inner-addon right-addon">
                <input style="border:1px solid #ddd;background: #fdfdfd;" type="text"
                       class="form-control input-sm search-form"
                       date-picker
                       ng-model="workRepVm.endDate"
                       name="endDate" placeholder="Finish Date">
                <i class="fa fa-calendar"></i>
            </div>
            <button ng-show="workRepVm.selectedProjectId != null"
                    style="margin-right: 10px;" title="Search Records"
                    class="btn btn-sm btn-default min-width pull-right"
                    ng-click="workRepVm.searchReport()">Search<i style="margin-left: 10px" class="fa fa-search"></i>
            </button>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Serial No.</th>
                    <th calss="titleData">Task Name</th>
                    <th class="titleData">Unit of work</th>
                    <th style="text-align: center">Total Units</th>
                    <th style="text-align: center">Units Completed</th>
                    <th style="text-align: center">Balance Work Units</th>
                    <th>Percentage Completed</th>
                    <th style="min-width: 250px">Remarks</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="workRepVm.loading == true">
                    <td colspan="8">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Report...
                        </span>
                    </td>
                </tr>
                <tr ng-if="workRepVm.selectedProjectId == null && workRepVm.searchMode == false">
                    <td colspan="12">Please select project and click on search</td>
                </tr>
                <tr ng-if="workRepVm.searchMode == false && workRepVm.selectedProjectId != null">
                    <td colspan="12">Please click on search</td>
                </tr>
                <tr ng-if="workRepVm.loading == false && workRepVm.reportRows.length == 0 && workRepVm.selectedProjectId != null && workRepVm.searchMode == true">
                    <td colspan="8">No Records found</td>
                </tr>
                <tr ng-repeat="row in workRepVm.reportRows">
                    <td style="vertical-align: middle;C">{{row.serialNo}}</td>
                    <td class="titleData" style="vertical-align: middle">{{row.task}}</td>
                    <td class="titleData" style="vertical-align: middle">{{row.units}}</td>
                    <td style="vertical-align: middle;text-align: center">{{row.totalQty}}</td>
                    <td style="vertical-align: middle;text-align: center">{{row.completed}}</td>
                    <td style="vertical-align: middle;text-align: center">{{row.balanceQty}}</td>
                    <td style="vertical-align: middle;vertical-align: middle;">
                        <div class="task-progress progress text-center">
                            <div style="width:{{row.percentage | number:2}}%"
                                 class="progress-bar progress-bar-primary"
                                 role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                                <span style="margin-left: 2px;">{{row.percentage | number:2}}%</span>
                            </div>
                        </div>
                    </td>
                    <td style="min-width: 250px">
                        <input class="form-control" type="text" ng-model="row.remarks"
                               ng-change="workRepVm.addRemark(row)"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>