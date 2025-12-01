<script type="text/ng-template" id="products-view-tb">
    <div>
        <button class="btn btn-sm btn-primary mr20" ng-if="hasRole('Administrator')"
                ng-click="newProduct()">New Product</button>
		<button class="btn btn-sm btn-primary" ng-click="addProductCategory()" style="width: 100px">New Category</button>

        <div class="btn-group">
            <button ng-repeat="view in viewTypes"
                    title="{{view.tooltip}}"
                    class="btn btn-sm btn-default"
                    ng-class="{ 'active': view.active }"
                    ng-click="setView(view)">
                <i class="{{view.icon}}"></i>
            </button>
        </div>
    </div>
</script>

<div ng-show="!hasProducts && !loading">
    There are no products
</div>
<div ng-show="loading">
    <span style="font-size: 15px;">
        <img src="app/assets/images/loaders/loader9.gif" class="mr5">Loading product categories...
    </span>
</div>
<div class="row" ng-show="hasProducts">
    <div id="productsView" class="col-sm-12">
        <div class="row" style="padding-left: 20px;padding-right: 20px;">
             <div class="col-md-12 styled-panel" style="height: 65px; padding: 9px;">
                <div class="input-group input-group-lg mb15">
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
                    <input class="form-control" type="text" placeholder="Enter search text..." ng-model="searchText" ng-enter="performTextSearch()">
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-primary" ng-click="performTextSearch()">Search</button>
                    </span>
                </div>
            </div>
        </div>

        <br/>
        <div ng-show="showNoResults">
            No results
        </div>

        <div ng-show="pagedResults.numberOfElements > 0" class="row">
            <div class="col-md-2">

            </div>
            <div class="col-md-10" style="text-align: right">
                <div style="text-align: right; margin-right: 10px">
                    <pagination total-items="pagedResults.totalElements"
                                items-per-page="pagedResults.size"
                                max-size="5"
                                boundary-links="true"
                                ng-model="page.page"
                                ng-change="pageChanged()">
                    </pagination>
                </div>

                <div style="margin-top: -25px; margin-right: 10px">
                    <small>Total {{pagedResults.totalElements}} items</small>
                </div>
            </div>
        </div>

        <div ng-if="viewStorage.lastViewType.name == 'grid'" ng-show="pagedResults.numberOfElements > 0">
            <div ng-repeat="row in results.rows" class="row">
                <div ng-repeat="col in row track by $index" class="col-xs-12 col-sm-12 col-md-6 col-lg-3">
                    <div class="product-search-result styled-panel" style="padding: 10px;height: 170px">
                        <div class="pull-left">
                            <img style="width: 150px; height: 150px" ng-src="{{getPictureUrl(col)}}" alt="" class="product-image"/>
                        </div>

                        <div style="margin-left: 150px;padding-left: 10px;">
                            <a href="#" editable-text="col.name" ng-if="hasRole('Administrator')" title="Click to edit"
                               onaftersave="saveProductChanges(col)">
                                <h4 class="text-primary" style="margin-top: 0;margin-bottom: 0;">{{col.name}}</h4>
                            </a>
                            <h4 class="text-primary" style="margin-top: 0;margin-bottom: 0;"
                                ng-if="!hasRole('Administrator')">{{col.name}}</h4>
                            <span class="text-muted" ng-if="col.businessUnit != null">{{col.businessUnit.name}}</span>

                            <div>
                                <span class="text-muted">SKU: {{col.sku}}</span>
                            </div>
                            <div class="price-editable">
                                <span class="mr5">Price: </span>
                                <a href="#" editable-text="col.unitPrice" ng-if="hasRole('Administrator')" title="Click to edit"
                                   onaftersave="saveProductChanges(col)">
                                    <span style="line-height: 30px;font-size: 20px;">&#x20b9; {{col.unitPrice}}</span>
                                </a>
                                <span style="line-height: 30px;font-size: 20px;" ng-if="!hasRole('Administrator')">
                                    &#x20b9; {{col.unitPrice}}</span>
                            </div>
                            <div class="row" style="position: absolute; bottom: 20px;">
                                <div class="col-sm-12">
                                    <button type="button" ng-show="mode == 'buy' && !checkCartIfExists(col)" style="width: 65px;"
                                            class="btn btn-sm btn-primary"
                                            ng-click="buyProduct(col)">Add</button>
                                    <div class="btn-group" ng-if="checkCartIfExists(col)" style="margin-bottom: 0">
                                        <button type="button" class="btn btn-sm btn-info" ng-click="buyProduct(col)"><i class="fa fa-check"></i></button>
                                        <button type="button" class="btn btn-sm btn-danger" ng-click="removeItem(col)"><i class="fa fa-times"></i></button>
                                    </div>
                                    <input ng-if="mode == 'buy'" class="form-control" type="text" placeholder="Qty"
                                           ng-model="col.quantity"
                                           ng-enter="buyProduct(col)"
                                           style="text-align: center;height: 33px; width: 65px;
                                                display: inline-block;padding: 5px !important;"/>

                                    <input ng-if="mode == 'buy' && col.type != 'category'" class="form-control" type="text" placeholder="Price"
                                           ng-model="col.newPrice"
                                           ng-enter="buyProduct(col)"
                                           style="text-align: center;height: 33px; width: 65px;
                                                display: inline-block;padding: 5px !important;"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="viewStorage.lastViewType.name == 'list'" style="margin: 0px 10px 0px 10px;" ng-show="pagedResults.numberOfElements > 0">
            <table class="table table-striped table-responsive">
                <thead>
                    <tr>
                        <th style="width: 50px; text-align: center;">SKU</th>
                        <th><span style="margin-left: 10px;">Name</span></th>
                        <th style="width: 100px; text-align: center;">Price</th>
                        <th ng-if="mode == 'buy'" style="text-align: center; width:100px">Quantity</th>
                        <th ng-if="mode == 'buy'" style="text-align: center; width:200px">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="prod in pagedResults.content">
                        <td style="width: 50px; text-align: center;">
                            <span ng-if="prod.type == 'category'" style="cursor: pointer;"
                                  ng-click="toggleRow(prod)">
                                <span title="Show Details" ng-if="prod.expanded == false"><i class="fa fa-plus-circle"></i></span>
                                <span title="Hide Details" ng-if="prod.expanded == true"><i class="fa fa-minus-circle"></i></span>
                            </span>
                            <span>{{prod.sku}}</span>
                        </td>
                        <td style="vertical-align: middle;">
                            <span ng-if="prod.type == 'category'" class="text-primary" style="margin-left: 10px;cursor: pointer">{{prod.name}}</span>
                            <span ng-if="prod.type != 'category' && prod.child == true" style="margin-left: 50px;">{{prod.name}}</span>
                            <span ng-if="prod.type != 'category' && prod.child != true" style="margin-left: 10px;">{{prod.name}}</span>
                        </td>
                        <td style="width: 100px; text-align: center; vertical-align: middle;">
                            <!--<span class="mr5">&#x20b9;</span>{{prod.unitPrice}}-->
                            <input ng-if="mode == 'buy' && prod.type != 'category'" tabindex="{{$index}}"
                                   class="form-control input-sm" type="number" min="0"
                                   style="text-align: center;" ng-model="prod.newPrice"/>
                            <span ng-if="mode != 'buy' && prod.type != 'category'">{{prod.newPrice | currency:"&#x20b9; ":0}}</span>
                        </td>
                        <td ng-if="mode == 'buy'" style="text-align: center; width:100px">
                            <input id="qty{{$index}}" name="qty{{$index}}"
                                   ng-if="mode == 'buy'" class="form-control input-sm" type="number" min="0"
                                   ng-enter="buyProduct(prod)"
                                   style="text-align: center;" ng-model="prod.quantity"/>
                        </td>
                        <td ng-if="mode == 'buy'" style="text-align: center; vertical-align: middle;width:200px">
                            <button ng-if="!checkCartIfExists(prod)" class="btn btn-sm btn-primary" style="width: 88px"
                                    ng-click="buyProduct(prod)">Add</button>
                            <div class="btn-group" ng-if="checkCartIfExists(prod)" style="margin-bottom: 0;">
                                <button type="button" class="btn btn-sm btn-info" ng-click="buyProduct(prod)" tabindex="-1">
                                    <i class="fa fa-check"></i>
                                </button>
                                <button type="button" class="btn btn-sm btn-danger" ng-click="removeItem(prod)" tabindex="-1">
                                    <i class="fa fa-times"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div ng-show="pagedResults.numberOfElements > 0" class="row">
            <div class="col-md-12">
                <div style="text-align: right; margin-right: 10px">
                    <pagination total-items="pagedResults.totalElements"
                                items-per-page="pagedResults.size"
                                max-size="5"
                                boundary-links="true"
                                ng-model="page.page"
                                ng-change="pageChanged()">
                    </pagination>
                </div>
            </div>
        </div>
    </div>
</div>