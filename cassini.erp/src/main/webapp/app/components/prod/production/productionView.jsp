<script type="text/ng-template" id="prod-view-tb">
    <div>
        <button class="btn btn-sm mr20 btn-primary" ng-click="newPon()">New</button>
        <div class="btn-group">
            <a class="btn btn-sm btn-default" ng-class="{ 'active': viewType == 'calendar' }" ng-click="setViewType('calendar')"><i class="glyphicon glyphicon-calendar"></i></a>
            <a class="btn btn-sm btn-default" ng-class="{ 'active': viewType == 'grid' }" ng-click="setViewType('grid')"><i class="fa fa-th"></i></a>
        </div>
    </div>
</script>
<div>
    <div class="view-content" ng-if="isCalendar">
        <div ui-calendar="uiConfig.calendar" ng-model="orders"></div>
    </div>

    <div ng-if="!isCalendar">
        <div class="row" style="margin-bottom: 20px">
            <div class="col-md-12"  style="text-align: right" ng-show="productionOrders.totalElements > 0">
                <div style="text-align: right;">
                    <pagination total-items="productionOrders.totalElements"
                                items-per-page="pageable.size"
                                max-size="5"
                                boundary-links="true"
                                ng-model="pageable.page"
                                ng-change="pageChanged()">
                    </pagination>
                </div>

                <div style="margin-top: -25px;">
                    <small>Total {{productionOrders.totalElements}} Orders</small>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="table-responsive">
                    <table class="table table-striped mb20">
                        <thead>
                        <tr>
                            <th>Product Id</th>
                            <th>Status</th>
                            <th>Ordered Date</th>
                            <th>OrderedBy</th>
                            <th>Approved Date</th>
                            <th>ApprovedBy</th>
                            <th>Start Date</th>
                            <th>Completed Date</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-repeat="order in productionOrders.content">
                            <td><a ui-sref="app.prod.productionorder({createDisplay:'display',orderId: order.id })">{{order.id}}</a></td>
                            <td>{{order.status}}</td>
                            <td>{{order.orderedDate}}</td>
                            <td>{{order.orderedBy}}</td>
                            <td>{{order.approvedDate}}</td>
                            <td>{{order.approvedBy}}</td>
                            <td>{{order.startDate}}</td>
                            <td>{{order.completedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
</div>



