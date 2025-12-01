<style scoped>
    .heading {
        margin-bottom: 5px !important;
        margin-top: 5px !important;
    }

    .panel-heading {
        height: 40px !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .col-md-4 {
        padding-left: 0px;
    }
</style>
<div class="panel panel-default panel-alt widget-messaging" style="padding: 20px 5px 10px;">
    <div class="panel-heading">
        <div class="col-md-12">
            <div class="col-md-4">
                <h4 class="heading">Latest Requests</h4>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="margin-top: 5px !important;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{requestWidgetVm.reqs.numberOfElements}} of
                                            {{requestWidgetVm.reqs.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{requestWidgetVm.reqs.totalElements != 0 ? requestWidgetVm.reqs.number+1:0}} of {{requestWidgetVm.reqs.totalPages}}</span>
                        <a href="" ng-click="requestWidgetVm.previousPage()"
                           ng-class="{'disabled': requestWidgetVm.reqs.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="requestWidgetVm.nextPage()"
                           ng-class="{'disabled': requestWidgetVm.reqs.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel-body" style="overflow-x: hidden">
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;overflow-x: auto">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 200px">Req.No</th>
                    <th style="width: 200px">Status</th>
                    <th style="width: 200px">Requested By</th>
                    <th style="width: 200px">Requested On</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="requestWidgetVm.loading == true">
                    <td colspan="6">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading requests...
                        </span>
                    </td>
                </tr>

                <tr ng-if="requestWidgetVm.loading == false && requestWidgetVm.reqs.content.length == 0">
                    <td colspan="6">No requests</td>
                </tr>
                <tr ng-repeat="req in requestWidgetVm.reqs.content">
                    <td style="width: 200px">
                        <a href="" ng-click="requestWidgetVm.showRequest(req)">{{req.reqNumber}}</a>
                    </td>
                    <td style="width: 150px">
                        <div style="width: 100px;">
                            <request-status request="req"></request-status>
                        </div>
                    </td>
                    <td style="width: 200px">{{req.requestedBy.firstName}}</td>
                    <td style="width: 200px">{{req.requestedDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>