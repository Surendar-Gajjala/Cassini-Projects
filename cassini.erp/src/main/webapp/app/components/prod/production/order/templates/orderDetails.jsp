<div class="row" style="padding:20px;">
    <div class="col-md-12">
        <div style="text-align: right; margin-bottom: 20px;">
            <pagination total-items="prodOrderItems.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>

            <div style="margin-top: -25px;">
                <small>Total {{prodOrderItems.totalElements}} Order Items</small>
            </div>
        </div>

        <table class="table table-striped">
            <thead>
            <tr>
                <th ng-show="onViewOrder">Actions</th>
                <th>SKU</th>
                <th>Name</th>
                <th>Process</th>
                <th>Bom</th>
                <th>Quantity</th>
            </tr>
            </thead>
            <tbody>
            <tr data-ng-repeat="orderItem in prodOrderItems.content">
                <td ng-show="onViewOrder">
                    <div class="btn-group" dropdown ng-hide="orderItem.editMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showEditMode(orderItem)">Edit</a></li>
                            <li><a href="" ng-click="removeItem($index,orderItem);">Delete</a></li>
                        </ul>
                    </div>

                    <div class="btn-group" ng-show="orderItem.editMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(orderItem)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(orderItem);"><i
                                class="fa fa-times"></i></button>
                    </div>
                </td>
                <td>
                    <span>{{orderItem.product.sku}}</span>
                </td>
                <td>
                    <span>{{orderItem.product.name}}</span>
                </td>
                <td>
                    <span ng-hide="orderItem.editMode">{{orderItem.process.name}}</span>
                    <div ng-show="orderItem.editMode">
                        <ui-select ng-model="orderItem.process" theme="bootstrap" style="width: 100%;"
                                   title="Choose a Process">
                            <ui-select-match placeholder="Choose a Process">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="process in processList | filter: $select.search">
                                <div ng-bind-html="process.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </td>
                <td>
                    <span ng-hide="orderItem.editMode">{{orderItem.bom.name}}</span>

                    <div ng-show="orderItem.editMode">
                        <ui-select ng-model="orderItem.bom" theme="bootstrap" style="width: 100%;" title="Choose a Bom">
                            <ui-select-match placeholder="Choose a Bom">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="bom in bomsList | filter: $select.search">
                                <div ng-bind-html="bom.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </td>
                <td>
                    <span ng-hide="orderItem.editMode">{{orderItem.quantity}}</span>
                    <input ng-show="orderItem.editMode" placeholder="Enter Qty" class="form-control" type="text"
                           data-ng-model="orderItem.quantity">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>