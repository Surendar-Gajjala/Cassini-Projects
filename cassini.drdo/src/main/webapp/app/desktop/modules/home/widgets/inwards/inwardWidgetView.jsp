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
                <h4 class="heading">Latest Inwards</h4>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="margin-top: 5px !important;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{inwardWidgetVm.inwards.numberOfElements}} of
                                            {{inwardWidgetVm.inwards.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{inwardWidgetVm.inwards.totalElements != 0 ? inwardWidgetVm.inwards.number+1:0}} of {{inwardWidgetVm.inwards.totalPages}}</span>
                        <a href="" ng-click="inwardWidgetVm.previousPage()"
                           ng-class="{'disabled': inwardWidgetVm.inwards.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="inwardWidgetVm.nextPage()"
                           ng-class="{'disabled': inwardWidgetVm.inwards.last}"><i
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
                    <th style="width: 150px;">Inward Number</th>
                    <th style="width: 150px;">System</th>
                    <th style="width: 75px;">Status</th>
                    <th>Gate Pass</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="inwardWidgetVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading inwards...
                        </span>
                    </td>
                </tr>

                <tr ng-if="inwardWidgetVm.loading == false && inwardWidgetVm.inwards.content.length == 0">
                    <td colspan="7">No inwards</td>
                </tr>
                <tr ng-repeat="inward in inwardWidgetVm.inwards.content">
                    <td><a ng-click="inwardWidgetVm.showInward(inward)">{{inward.number}}
                        <span title="inward has provisional accepted inwards" ng-if="inward.provisionalAccept == true">
                            <%--<img src="app/assets/images/prov.gif">--%>
                        </span>
                         <span ng-if="inward.marquee == true" style="font-size:19px;">
                             <i class="fa fa-eye" aria-hidden="true" style="color: #db6758;"></i></span>
                     <span ng-if="inward.reReview == true" style="font-size:19px;"><i class="fa fa-eye"
                                                                                      aria-hidden="true"
                                                                                      style="color: #154edb;"></i></span></a>
                    </td>
                    <td>{{inward.bom.item.itemMaster.itemName}}</td>
                    <td>
                        <inward-status inward="inward"></inward-status>
                    </td>
                    <td>
                          <span>
                              <span><a title="{{inward.gPassFile.name}}" href="" style="color: #11278e;"
                                       title="downloadAttachment"
                                       ng-click="inwardWidgetVm.downloadAttachment(inward.gatePass)">{{inward.gatePass.gatePass.name
                                  | limitTo: 25 }}{{inward.gatePass.gatePass.name.length > 25 ? '...' : ''}}</a>
                              </span>
                          </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>