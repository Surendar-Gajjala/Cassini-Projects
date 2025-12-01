<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="allReportsVm.createProject();">Create Report</button>
        <free-text-search on-clear="allReportsVm.resetPage" on-search="allReportsVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content">
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div style="padding: 10px;">
                <div class="pull-right text-center">
                <span ng-if="allReportsVm.loading == false"><small>Page {{allReportsVm.projects.number+1}} of
                    {{allProjectsVm.projects.totalPages}}
                </small></span>
                    <br>

                    <div class="btn-group" style="margin-bottom: 0">
                        <button class="btn btn-xs btn-default"
                                ng-click="allReportsVm.previousPage()"
                                ng-disabled="allReportsVm.reports.first">
                            <i class="fa fa-chevron-left"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="allReportsVm.nextPage()"
                                ng-disabled="allReportsVm.reports.last">
                            <i class="fa fa-chevron-right"></i>
                        </button>
                    </div>
                    <br>
                    <span ng-if="allReportsVm.loading == false"><small>{{allProjectsVm.reports.totalElements}}
                      Reports
                    </small></span>
                </div>
            </div>
        </div>
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 200px;">Date</th>
                <th style="width: 200px;">Task Name</th>
                <th style="width: 150px;">Assigned Staff</th>
                <th style="width: 150px;">Supervisor</th>
                <th style="width: 150px;">Officer</th>
                <th style="width: 150px;">Work Location</th>
                <th style="width: 150px;">Pending Reason</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="report in allReportsVm.reports.content">
                <td><a href="" ng-click="allReportsVm.showReportDetails(report)">{{report.name}}</a></td>
                <td>{{report.description}}</td>
                <td>
                    <button title="Delete Report" class="btn btn-xs btn-danger"
                            ng-click="allReportsVm.deleteReport(report)"><i class="fa fa-trash"></i></button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>