<script type="text/ng-template" id="new-product-view-tb">
    <div>
        <button class="btn btn-sm btn-primary mr20" ng-click="createProduct()">Create</button>
        <button class="btn btn-sm btn-primary mr20" ng-click="cancel()">Cancel</button>
    </div>
</script>
<br>
<div>
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-4">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">Product Type: </label>
                    <div class="col-sm-8">
                        <ui-select ng-model="newProduct.typee" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select type">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="type in productTypes | filter: $select.search">
                                <div ng-bind-html="type.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

              <%--  <div class="form-group">
                    <label class="col-sm-3 control-label">Business Unit: </label>
                    <div class="col-sm-8">
                        <ui-select ng-model="newProduct.businessUnit" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select business unit">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="bu in businessUnits | filter: $select.search">
                                <div ng-bind-html="bu.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>--%>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Category: </label>
                    <div class="col-sm-8">
                        <div class="input-group">
                            <input class="form-control" type="text" placeholder="Select category..." disabled
                                   ng-model="newProduct.category.name">

                            <div class="input-group-btn" dropdown>
                                <button dropdown-toggle class="btn btn-success dropdown-toggle" type="button">
                                    Categories <span class="caret" style="margin-left: 5px;"></span>
                                </button>
                                <div class="dropdown-menu" role="menu" id="productCategoriesDropdown">
                                    <div id="productCategoriesTree" class="easyui-tree"
                                         style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 500px;height: 400px;">
                                        categories
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">SKU: </label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="sku" ng-model="newProduct.sku" ng-blur="checkSKU()" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">Name: </label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="name" ng-model="newProduct.name">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Unit Price: </label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="unitPrice" ng-model="newProduct.unitPrice">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Inventory: </label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="inventory" ng-model="inventory.inventory">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">Restock Level: </label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="threshold" ng-model="inventory.threshold">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<br>