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
                <h4 class="heading">Latest Failures</h4>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="margin-top: 5px !important;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{failureWidgetVm.failureItems.numberOfElements}} of
                                            {{failureWidgetVm.failureItems.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{failureWidgetVm.failureItems.totalElements != 0 ? failureWidgetVm.failureItems.number+1:0}} of {{failureWidgetVm.failureItems.totalPages}}</span>
                        <a href="" ng-click="failureWidgetVm.previousPage()"
                           ng-class="{'disabled': failureWidgetVm.failureItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="failureWidgetVm.nextPage()"
                           ng-class="{'disabled': failureWidgetVm.failureItems.last}"><i
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
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="failureWidgetVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Failure Items...
                        </span>
                    </td>
                </tr>

                <tr ng-if="failureWidgetVm.loading == false && failureWidgetVm.failureItems.content.length == 0">
                    <td colspan="7">No Failure Items</td>
                </tr>
                <tr ng-repeat="failureItem in failureWidgetVm.failureItems.content">
                    <td>
                        {{failureItem.item.itemMaster.itemName}} {{failureItem.item.partSpec.specName}}
                    </td>
                    <td>{{failureItem.item.itemMaster.parentType.name}}</td>
                    <td>
                        <item-instance-status object="failureItem"></item-instance-status>
                    </td>
                    <td><span class="badge badge-danger" style="font-size: 13px;">
                             <a href="" style="color: white !important;" title="Click to history"
                                ng-click="showUpnHistory(failureItem)">{{failureItem.upnNumber}}</a>
                        </span>
                    </td>
                    <td>{{failureItem.serialNumber}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>