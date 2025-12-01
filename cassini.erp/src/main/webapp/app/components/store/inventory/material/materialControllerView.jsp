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
                <th style="width:100px; text-align: center">Actions</th>
                <th style="width:100px; text-align: center">SKU</th>
                <th style="cursor: pointer;" ng-click="sortColumn('name')">Name
                    <table-sorter label="name" sort="pageable.sort"/>
                </th>
                <th style="width:150px; text-align:center;">Inventory</th>
                <th ng-if="addInvMode" style="width:150px; text-align:center;">Material PO</th>
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
                <td ng-if="addInvMode" style="text-align:center;">
                </td>
                <td ng-if="issueInvMode" style="text-align:center;">
                </td>
                <td style="width:150px">
                    <table-filter align="center" placeholder="Restock level" filters="filters" property="threshold"
                                  apply-filters="applyFilters()"/>
                </td>
            </tr>

            <tr ng-if="materials.totalElements == 0 || loading == true">
                <td colspan="4">
                    <span ng-hide="loading">There are no materials</span>
                    <span ng-show="loading">
                        <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading materials...
                    </span>
                </td>
            </tr>

            <tr ng-repeat="material in materials.content">

                <td style="background-color:#F7F7F7" colspan="5"
                    ng-include="material.showHistory ? 'app/components/store/inventory/material/materialInventoryHistoryView.jsp' : null">
                </td>

                <td ng-if="!material.showHistory" class="col-center actions-col">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;"
                         ng-hide="material.editMode || material.addMode || material.issueMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showAddMode(material)" ng-if="hasRole('Administrator')">Add
                                Inventory</a></li>
                            <li><a href="" ng-click="showIssueMode(material)" ng-if="hasRole('Administrator')">Issue
                                Inventory</a></li>
                            <%--<li><a href="" ng-click="showEditMode(material)" ng-if="hasRole('Administrator')">Edit Inventory</a></li>--%>
                            <li><a href="" ng-click="showMaterialInventoryHistory(material)">Show History</a></li>
                        </ul>
                    </div>

                    <div class="btn-group" style="margin-bottom: 0px;" ng-if="material.editMode">
                        <button type="button" title="Ok" class="btn btn-sm btn-success"
                                ng-click="acceptChanges(material)"><i class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(material);"><i
                                class="fa fa-times"></i></button>
                    </div>
                    <div class="btn-group" style="margin-bottom: 0px;" ng-if="material.addMode">
                        <button type="button" title="Ok" class="btn btn-sm btn-success" ng-disabled="!okButton"
                                ng-click="acceptAddInvChanges(material)"><i class="fa fa-check"></i></button>
                        <button type="button" title="Cancel" class="btn btn-sm btn-default"
                                ng-click="hideAddMode(material);"><i class="fa fa-times"></i></button>
                    </div>
                    <div class="btn-group" style="margin-bottom: 0px;" ng-if="material.issueMode">
                        <button type="button" title="Ok" class="btn btn-sm btn-success"  ng-disabled="!okButton"
                                ng-click="acceptIssueInvChanges(material)"><i class="fa fa-check"></i></button>
                        <button type="button" title="Cancel" class="btn btn-sm btn-default"
                                ng-click="hideIssueMode(material);"><i class="fa fa-times"></i></button>
                    </div>

                </td>
                <td style="width: 100px; text-align: center;" ng-if="!material.showHistory">{{material.sku}}</td>
                <td ng-if="!material.showHistory">{{material.name}}</td>

                <td ng-if="!material.showHistory" style="width:120px; text-align:center;">
                    <span ng-show="material.showValues">{{material.inventory.inventory | currency:"":0}}</span>
                    <input placeholder="Enter inventory" class="form-control"
                           type="text" style="text-align:center"
                           ng-show="material.editMode" ng-model="material.inventory.inventory">
                </td>
                <td ng-if="addInvMode" style="width:150px; text-align:center;">
                    <%--<input class="form-control" ng-if="!material.showHistory && material.addMode"
                          ng-escape="hideAddMode(material)"
                           type="text" style="text-align:center"
                           ng-show="material.addMode" ng-model="material.materialPO">--%>
                    <ui-select ng-model="material.materialPO" on-select="getMaterialPODetails(material)"
                               theme="bootstrap" style="width:200px">
                        <ui-select-match allow-clear="true" placeholder="Select materialPO">
                            {{$select.selected.orderNumber}}
                        </ui-select-match>
                        <ui-select-choices repeat="po in materialPOs | filter: $select.search">
                            <div ng-bind-html="po.orderNumber | highlight: $select.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>
                <td ng-if="addInvMode" style="width:150px; text-align:center;">
                    <input class="form-control" ng-if="!material.showHistory && material.addMode"
                           ng-escape="hideAddMode(material)" ng-disabled="material.materialPO == ''"
                           type="text" style="text-align:center"
                           ng-change="validateQuantity(material)"
                           ng-show="material.addMode" ng-model="material.inventory.newInventory">
                </td>
                <td ng-if="issueInvMode" style="width:150px; text-align:center;">
                    <input class="form-control" ng-if="!material.showHistory && material.issueMode"
                           ng-escape="hideIssueMode(material)"
                           type="text" style="text-align:center"
                           ng-change="validateIssueQuantity(material)"
                           ng-show="material.issueMode" ng-model="material.inventory.issueInv">
                </td>

                <td ng-if="!material.showHistory" style="width:150px; text-align:center;">
                    <span ng-show="material.showValues">{{material.inventory.threshold | currency:"":0}}</span>
                    <input placeholder="Enter restock level" class="form-control" type="text" style="text-align:center"
                           ng-show="material.editMode" ng-model="material.inventory.threshold">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>