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
                <h4 class="heading">Latest Dispatches</h4>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="margin-top: 5px !important;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{dispatchWidgetVm.dispatches.numberOfElements}} of
                                            {{dispatchWidgetVm.dispatches.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{dispatchWidgetVm.dispatches.totalElements != 0 ? dispatchWidgetVm.dispatches.number+1:0}} of {{dispatchWidgetVm.dispatches.totalPages}}</span>
                        <a href="" ng-click="dispatchWidgetVm.previousPage()"
                           ng-class="{'disabled': dispatchWidgetVm.dispatches.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="dispatchWidgetVm.nextPage()"
                           ng-class="{'disabled': dispatchWidgetVm.dispatches.last}"><i
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
                    <th>Dispatch Number</th>
                    <th>BOM</th>
                    <th>Status</th>
                    <th>Gate Pass Number</th>
                    <th>Dispatch Date</th>
                    <th>Created By</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="dispatchWidgetVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading dispatches...
                        </span>
                    </td>
                </tr>

                <tr ng-if="dispatchWidgetVm.loading == false && dispatchWidgetVm.dispatches.content.length == 0">
                    <td colspan="7">No dispatches</td>
                </tr>
                <tr ng-repeat="dispatch in dispatchWidgetVm.dispatches.content">
                    <td>
                        <a href="" ng-click="dispatchWidgetVm.showDispatchDetails(dispatch)">{{dispatch.number}}</a>
                    </td>
                    <td>{{dispatch.bom.item.itemMaster.itemName}}</td>
                    <td>
                        <item-instance-status object="dispatch"></item-instance-status>
                    </td>
                    <td>{{dispatch.gatePassNumber}}</td>
                    <td>{{dispatch.dispatchDate}}</td>
                    <td>{{dispatch.createdByObject.fullName}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>