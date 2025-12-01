<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 07-08-2018
  Time: 15:50
  To change this template use File | Settings | File Templates.
--%>
<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Select Suppliers</h3>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-md-12" style="text-align: center" ng-show="suppliers.numberOfElements > 0">
            <div>
                <pagination total-items="suppliers.totalElements"
                            items-per-page="20"
                            max-size="5"
                            boundary-links="true"
                            ng-model="pageable.page"
                            ng-change="loadSuppliers()">
                </pagination>
            </div>

            <div style="margin-top: -25px;">
                <small>Total {{suppliers.totalElements}} suppliers</small>
            </div>
        </div>
    </div>

    <br/>

    <div class="row">
        <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 80px;">
                    </th>
                    <th style="">Name</th>
                    <th style="">Cost</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="suppliers.content.length == 0">
                    <td colspan="2">
                        <span ng-hide="loading">No suppliers</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading suppliers...
                            </span>
                    </td>
                </tr>


                <tr ng-repeat="supplier in suppliers.content">
                    <td style="width:50px; text-align:center; vertical-align: middle;" align="center">
                        <div style="margin-left: 25px;">
                            <input id="supplier{{$index}}" ng-value="true" type="checkbox"
                                   ng-model="supplier.checked" ng-click="selectSupplier(supplier)">
                            <label for="supplier{{$index}}"></label>
                        </div>
                    </td>

                    <td style="vertical-align: middle;">
                        {{ supplier.name }}
                    </td>
                    <td style="vertical-align: middle;">
                        <input ng-show="supplier.checked" type="number" ng-model="supplier.cost" style="width: 80px"/>
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
            <h4>Selected Suppliers: <span class="text-success">{{selectedSupp.length}}</span></h4>
        </div>
        <div class="col-md-4" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel
            </button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()">Select
            </button>
        </div>
    </div>
</div>