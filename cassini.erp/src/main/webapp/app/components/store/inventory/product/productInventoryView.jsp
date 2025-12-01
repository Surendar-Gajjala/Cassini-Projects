<div class="row" style="margin-bottom: 20px">
    <div class="col-md-12" style="text-align: right" ng-show="products.numberOfElements > 0">
        <div style="text-align: right;">
            <pagination total-items="products.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{products.totalElements}} products</small>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width:100px; text-align: center">Actions</th>
                <th style="width:100px; text-align: center">SKU</th>
                <th style="cursor: pointer;" ng-click="sortColumn('name')">Name
                    <table-sorter label="name" sort="pageable.sort"/>
                </th>
                <th style="width:150px; text-align:center;">Inventory</th>
                <th ng-if="addInvMode" style="width:150px; text-align:center;">Add Inventory</th>
                <th ng-if="issueInvMode" style="width:150px; text-align:center;">Issue Inventory</th>
                <th style="width:150px; text-align:center;">Restock Level</th>
            </tr>
            </thead>

            <tbody>
            <tr>
                <td style="width:100px; text-align: center;">
                    <table-filters-actions apply-filters="applyFilters()" reset-filters="resetFilters()"/>
                </td>
                <td style="width:100px; text-align: center;">
                    <table-filter placeholder="SKU" align="center" filters="filters" property="sku"
                                  apply-filters="applyFilters()"/>
                </td>
                <td>
                    <table-filter placeholder="Name" filters="filters" property="name" apply-filters="applyFilters()"/>
                </td>
                <td style="width:150px">
                    <table-filter align="center" placeholder="Inventory" filters="filters" property="inventory"
                                  apply-filters="applyFilters()"/>
                </td>
                <td ng-if="addInvMode" style="text-align:center;">
                </td>
                <td ng-if="issueInvMode" style="text-align:center;">
                </td>
                <td style="width:150px">
                    <table-filter align="center" placeholder="Restock level" filters="filters" property="threshold"
                                  apply-filters="applyFilters()"/>
                </td>
            </tr>

            <tr ng-if="products.totalElements == 0 || loading == true">
                <td colspan="4">
                    <span ng-hide="loading">There are no products</span>
                    <span ng-show="loading">
                        <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading products...
                    </span>
                </td>
            </tr>

            <tr ng-repeat="product in products.content">

                <td style="background-color:#F7F7F7" colspan="5"
                    ng-include="product.showHistory ? 'app/components/store/inventory/product/productInventoryHistoryView.jsp' : null">
                </td>

                <td ng-if="!product.showHistory" class="col-center actions-col">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;"
                         ng-hide="product.editMode || product.addMode || product.issueMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showAddMode(product)" ng-if="hasRole('Administrator')">Add
                                Inventory</a></li>
                            <li><a href="" ng-click="showIssueMode(product)" ng-if="hasRole('Administrator')">Issue
                                Inventory</a></li>
                            <%--<li><a href="" ng-click="showEditMode(product)" ng-if="hasRole('Administrator')">Edit Inventory</a></li>--%>
                            <li><a href="" ng-click="showProductInventoryHistory(product)">Show History</a></li>
                        </ul>
                    </div>

                    <div class="btn-group" style="margin-bottom: 0px;" ng-if="product.editMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(product)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(product);"><i
                                class="fa fa-times"></i></button>
                    </div>
                    <div class="btn-group" style="margin-bottom: 0px;" ng-if="product.addMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptAddInvChanges(product)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideAddMode(product);"><i
                                class="fa fa-times"></i></button>
                    </div>
                    <div class="btn-group" style="margin-bottom: 0px;" ng-if="product.issueMode">
                        <button type="button" title="Ok" class="btn btn-sm btn-success" ng-disabled="!okButton"
                                ng-click="acceptIssueInvChanges(product)"><i class="fa fa-check"></i></button>
                        <button type="button" title="Cancel" class="btn btn-sm btn-default"
                                ng-click="hideIssueMode(product);"><i class="fa fa-times"></i></button>
                    </div>

                </td>
                <td style="width: 100px; text-align: center;" ng-if="!product.showHistory">{{product.sku}}</td>
                <td ng-if="!product.showHistory">{{product.name}}</td>

                <td ng-if="!product.showHistory" style="width:150px; text-align:center;">
                    <span ng-show="product.showValues">{{product.inventory.inventory | currency:"":0}}</span>
                    <input placeholder="Enter inventory" class="form-control"
                           type="text" style="text-align:center"
                           ng-show="product.editMode" ng-model="product.inventory.inventory">
                </td>
                <td ng-if="addInvMode" style="width:150px; text-align:center;">
                    <input class="form-control" ng-if="!product.showHistory && product.addMode"
                           ng-enter="acceptAddInvChanges(product)" ng-escape="hideAddMode(product)"
                           type="text" style="text-align:center"
                           ng-show="product.addMode" ng-model="product.inventory.newInventory">
                </td>
                <td ng-if="issueInvMode" style="width:150px; text-align:center;">
                    <input class="form-control" ng-if="!product.showHistory && product.issueMode"
                           ng-escape="hideIssueMode(product)"
                           type="text" style="text-align:center"
                           ng-change="validateIssueQuantity(product)"
                           ng-show="product.issueMode" ng-model="product.inventory.issueInv">
                </td>
                <td ng-if="!product.showHistory" style="width: 200px">

                    <span ng-show="product.showValues">
                        <a href="#" editable-text="product.inventory.threshold" onaftersave="saveRestockLevel(product)">
                            {{product.inventory.threshold || 'Click to set value'}}
                        </a>
                    </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>