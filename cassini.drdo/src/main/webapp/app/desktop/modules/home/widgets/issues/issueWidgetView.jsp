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
                <h4 class="heading">Latest Issues</h4>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="margin-top: 5px !important;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{issueWidgetVm.issues.numberOfElements}} of
                                            {{issueWidgetVm.issues.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{issueWidgetVm.issues.totalElements != 0 ? issueWidgetVm.issues.number+1:0}} of {{issueWidgetVm.issues.totalPages}}</span>
                        <a href="" ng-click="issueWidgetVm.previousPage()"
                           ng-class="{'disabled': issueWidgetVm.issues.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="issueWidgetVm.nextPage()"
                           ng-class="{'disabled': issueWidgetVm.issues.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel-body" style="overflow-x: hidden">
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;overflow-x: auto">
            <table class="table table striped highlight-row">
                <thead>
                <tr>
                    <th>Issue No</th>
                    <th>Issued Date</th>
                    <%-- <th>Status</th>--%>
                    <th>Issued To</th>
                    <th>Issued Date</th>
                    <th>Missile</th>
                    <th>Request</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="issueWidgetVm.loading == true">
                    <td colspan="6">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading issues...
                        </span>
                    </td>
                </tr>

                <tr ng-if="issueWidgetVm.loading == false && issueWidgetVm.issues.content.length == 0">
                    <td colspan="6">No issues</td>
                </tr>
                <tr ng-repeat="issue in issueWidgetVm.issues.content">
                    <td><a ui-sref="app.issue.details({issueId: issue.id, mode: 'home'})">{{issue.number}}</a></td>
                    <td>{{issue.createdDate}}</td>
                    <%--                    <td style="width: 150px">
                                            <div class="inward-status" style="width: 100px; text-align: center;"
                                                 ng-class="{'new': issue.status == 'INVENTORY',
                                                 'store': issue.status == 'CAS_MANAGER',
                                                 'finish':issue.status == 'FINISH'}">
                                                {{issue.status}}
                                            </div>
                                        </td>--%>
                    <td>{{issue.issuedToObject.firstName}}</td>
                    <td>
                        <span>{{issue.createdDate}}</span>
                    </td>
                    <td>{{issue.referenceItemObject.itemNumber}}</td>
                    <td>{{issue.requisitionObject.reqNumber}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
