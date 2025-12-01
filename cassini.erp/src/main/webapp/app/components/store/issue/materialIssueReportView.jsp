<div class="row" style="margin-bottom: 20px">
    <div class="col-md-12" style="text-align: right" ng-show="materials.numberOfElements >0">
        <div style="text-align: right;">
            <pagination total-items="materials.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{materials.totalElements}} materials</small>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Date</th>
                <th>SKU</th>
                <th>Name</th>
                <th>Issued Qty</th>
                <th>Consume Qty</th>
                <th>Remaining Qty</th>
                <th style="width:100px; text-align: center">Actions</th>
            </tr>
            </thead>

            <tbody>

            <tr>
                <td style="text-align: center">
                    <input placeholder="Date"
                           style="width: 100%; text-align: left"
                           options="dateRangeOptions"
                           date-range-picker
                           clearable="true"
                           class="form-control date-picker"
                           ng-enter="applyFilters()"
                           ng-class="{ hasFilter: (filters.timestamp.startDate != null && filters.timestamp.endDate != '') }"
                           type="text" ng-model="filters.timestamp"/>
                </td>
                <td>
                    <table-filter placeholder="SKU" filters="filters"
                                  property="sku" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Name" filters="filters"
                                  property="name" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Issue Qty" filters="filters"
                                  property="issuedQty" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Consume Qty" filters="filters"
                                  property="consumeQty" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Remaining Qty" filters="filters"
                                  property="remainingQty" apply-filters="applyFilters()"/>
                </td>
                <td style="width:100px; text-align: center;">
                    <div class="btn-group" style="margin-bottom: 0px;">
                        <button title="Apply Filters" type="button" class="btn btn-sm btn-success"
                                ng-click="applyFilters()">
                            <i class="fa fa-search"></i>
                        </button>
                        <button title="Clear Filters" type="button" class="btn btn-sm btn-default"
                                ng-click="resetFilters()">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </td>
            </tr>

            <tr ng-if="materials.totalElements == 0 || loading == true">
                <td colspan="7">
                    <span ng-hide="loading">There are no materials</span>
                    <span ng-show="loading">
                        <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading materials...
                    </span>
                </td>
            </tr>

            <tr ng-repeat="material in materials.content">
                <td>{{material.timestamp}}</td>
                <td>{{material.materialObject.sku}}</td>
                <td>{{material.materialObject.name}}</td>
                <td>
                    {{material.quantity}}
                </td>
                <td>
                    <span ng-hide="material.addConsume">{{material.consumeQty}}</span>
                    <input class="form-control" style="width: 80px;"
                           type="number" ng-escape="hideAddConsume(material)"
                           ng-change="validateConsumeQuantity(material)"
                           ng-show="material.addConsume" ng-model="material.newConsumeQty">
                </td>
                <td>
                    {{material.remainingQty}}
                </td>
                <td class="col-center actions-col">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;"
                         ng-hide="material.addConsume">
                        <button title="Update Consume Qty" type="button" class="btn btn-xs btn-warning"
                                ng-click="addConsumeQty(material)">
                            <i class="fa fa-edit"></i>
                        </button>
                    </div>
                    <div class="btn-group" style="margin-bottom: 0px;" ng-if="material.addConsume">
                        <button type="button" title="Ok" class="btn btn-sm btn-success" ng-disabled="!okButton"
                                ng-click="acceptConsumeChanges(material)"><i class="fa fa-check"></i></button>
                        <button type="button" title="Cancel" class="btn btn-sm btn-default"
                                ng-click="hideAddConsume(material);"><i class="fa fa-times"></i></button>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>