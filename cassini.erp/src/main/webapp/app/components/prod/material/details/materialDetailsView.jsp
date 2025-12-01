<%--
<div class="view-container">

  <div class="view-toolbar">
    <button ng-show="createOrSave" class="btn btn-sm btn-success min-width" ng-click="createSupplier(supplier)">Create</button>
    <button ng-hide="createOrSave" class="btn btn-sm btn-success min-width" ng-click="saveSupplier(supplier)">Save</button>
    <button class="btn btn-sm btn-success min-width" ng-click="cancelSupplier(supplier)">Cancel</button>
  </div>
--%>

<script type="text/ng-template" id="material-view-tb">
    <div>
        <button class="btn btn-sm btn-success min-width" ng-click="save()">Save</button>
        <button class="btn btn-sm btn-success min-width" ng-click="cancel()">Cancel</button>

    </div>
</script>
<br>

<div class="view-content">


    <%--<div class="view-content">--%>
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3">
            <form class="form-horizontal">

                <div class="form-group">
                    <label class="col-sm-3 control-label">SKU: </label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="sku" ng-model="material.sku" ng-blur="checkSKU()">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Material Type: </label>

                    <div class="col-sm-8">
                        <ui-select ng-model="material.type" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select type">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="type in materialTypes | filter: $select.search">
                                <div ng-bind-html="type.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Suppliers: </label>

                    <div class="col-sm-8">
                        <button class="btn btn-sm btn-success" style="width: 100px" ng-click="addSupplier()">Add
                        </button>
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th style="">Supplier</th>
                                <th style="">Cost</th>
                                <th style="">Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="material.materialSuppliers.length == 0">
                                <td colspan="3">
                                    <span ng-hide="loading">No suppliers</span>
                                </td>
                            </tr>
                            <tr ng-repeat="supplier in material.materialSuppliers">
                                <td style="vertical-align: middle;">
                                    {{ supplier.supplierName }}
                                </td>
                                <td style="vertical-align: middle;">
                                    {{supplier.cost}}
                                </td>
                                <td>
                                    <button title="Delete" class="btn btn-xs btn-danger"
                                            ng-click="deleteMaterialSupplier(supplier)">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <%--<ui-select multiple ng-model="material.suppliers" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Suppliers">{{$item.name}}</ui-select-match>
                                <ui-select-choices repeat="supp in allSupplierrs | filter: $select.search">
                                    <div ng-bind-html="supp.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>--%>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">Category: </label>

                    <div class="col-sm-8">
                        <div class="input-group">
                            <input class="form-control" type="text" placeholder="Select category..." disabled
                                   ng-model="material.category.name" ng-enter="performTextSearch()">

                            <div class="input-group-btn" dropdown>
                                <button dropdown-toggle class="btn btn-success dropdown-toggle" type="button">
                                    Categories <span class="caret" style="margin-left: 5px;"></span>
                                </button>
                                <div class="dropdown-menu" role="menu" id="materialCategoriesDropdown">
                                    <div id="materialCategoriesTree" class="easyui-tree"
                                         style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 200px;height: 200px;">
                                        categories
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Name: </label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="name" ng-model="material.name">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Description: </label>

                    <div class="col-sm-8">

                        <textarea class="form-control" rows="2" ng-model="material.description"></textarea>

                    </div>
                </div>


                <div class="form-group">
                    <label class="col-sm-3 control-label">Units: </label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="units" ng-model="material.units">
                    </div>
                </div>

                <%--<div class="form-group">
                    <label class="col-sm-3 control-label">Unit Price: </label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="unitPrice" ng-model="material.unitPrice">
                    </div>
                </div>--%>
            </form>
        </div>
    </div>
</div>

