<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Select Customer</h3>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-md-12"  style="text-align: center" ng-show="customers.numberOfElements > 0">
            <div>
                <pagination total-items="customers.totalElements"
                            items-per-page="20"
                            max-size="5"
                            boundary-links="true"
                            ng-model="pageable.page"
                            ng-change="loadCustomers()">
                </pagination>
            </div>

            <div style="margin-top: -25px;">
                <small>Total {{customers.totalElements}} customers</small>
            </div>
        </div>
    </div>

    <br/>
    <div class="row">
        <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th style="width: 80px; text-align: center">
                            <input id="custColumn" name="custSelected" type="radio" value="" style="display:none">
                            <label for="custColumn"></label>
                        </th>
                        <th style="">Name</th>
                        <th style="">Region</th>
                        <th style="">Customer Type</th>
                        <th style="">Sales Rep</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-hide="salesRepFilter != null">
                        <td style="width:100px; text-align: center; vertical-align: middle;">
                            <div class="btn-group" style="margin-bottom: 0px;">
                                <button type="button" class="btn btn-sm btn-success" ng-click="applyCriteria()" title="Search"><i class="fa fa-search"></i></button>
                                <button type="button" class="btn btn-sm btn-default" ng-click="resetCriteria()" title="Clear Search"><i class="fa fa-times"></i></button>
                            </div>
                        </td>

                        <td style="vertical-align: middle;">
                            <input placeholder="Enter name" class="form-control" type="text"
                                   ng-model="filters.name" ng-enter="applyCriteria()" >
                        </td>

                        <td style="vertical-align: middle;">
                            <input placeholder="Enter region" class="form-control" type="text"
                                   ng-model="filters.region" ng-enter="applyCriteria()" >
                        </td>

                        <td style="vertical-align: middle;">
                            <input placeholder="Customer Type" class="form-control" type="text"
                                   ng-model="filters.customerType" ng-enter="applyCriteria()" >
                        </td>

                        <td style="vertical-align: middle;">
                            <input placeholder="Enter sales rep" class="form-control" type="text"
                                   ng-model="filters.salesRep" ng-enter="applyCriteria()" >
                        </td>
                    </tr>

                    <tr ng-if="customers.content.length == 0">
                        <td colspan="4">
                            <span ng-hide="loading">No customers</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customers...
                            </span>
                        </td>
                    </tr>


                    <tr ng-repeat="customer in customers.content">
                        <td style="width:50px; text-align:center; vertical-align: middle;" align="center">
                            <div class="rdio rdio-default" style="margin-left: 25px;">
                                <input id="cust{{$index}}" name="custSelected" ng-value="true" type="radio"
                                       ng-model="customer.selected" ng-click="selectCustomer(customer)">
                                <label for="cust{{$index}}"></label>
                            </div>
                        </td>

                        <td style="vertical-align: middle;">
                            {{ customer.name }}
                        </td>

                        <td style="vertical-align: middle;">
                            {{ customer.salesRegion.name }}
                        </td>

                        <td style="vertical-align: middle;">
                            {{ customer.customerType.name }}
                        </td>

                        <td style="vertical-align: middle;">
                            {{ customer.salesRep.firstName }}
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br/>
</div>
<div class="modal-footer" style="background-color: #F9F9F9;text-align: left;">
    <div class="row">
        <div class="col-md-8" style="text-align: left">
            <h4>Selected Customer: <span class="text-success">{{selectedCustomer.name}}</span></h4>
        </div>
        <div class="col-md-4" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel</button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()" ng-disabled="selectedCustomer == null">Select</button>
        </div>
    </div>
</div>