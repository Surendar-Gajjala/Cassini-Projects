<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Select Products</h3>
</div>
<div class="modal-body" style="max-height:500px; overflow: auto;">
    <br>
    <div class="row">
        <div class="col-md-12"  style="text-align: center" ng-show="products.numberOfElements > 0">
            <div>
                <pagination total-items="products.totalElements"
                            items-per-page="20"
                            max-size="5"
                            boundary-links="true"
                            ng-model="pageable.page"
                            ng-change="loadProducts()">
                </pagination>
            </div>

            <div style="margin-top: -25px;">
                <small>Total {{products.totalElements}} products</small>
            </div>
        </div>
    </div>

    <br/><br/>
    <div class="row">
        <div class="col-md-12" style="padding:0px">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th style="width: 80px; text-align: center">
                            <input id="prodColumn" name="prodSelected" type="checkbox" value="" style="display:none">
                            <label for="prodColumn"></label>
                        </th>
                        <th style="">Name</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
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
                    </tr>

                    <tr ng-if="products.content.length == 0">
                        <td colspan="2">
                            <span ng-hide="loading">No Products</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading products...
                            </span>
                        </td>
                    </tr>


                    <tr ng-repeat="product in products.content">
                        <td style="width:50px; text-align:center; vertical-align: middle;" align="center">
                            <div ng-if="selectedType!='radio'" class="ckbox ckbox-default" style="margin-left: 25px;">
                                <input id="prod{{$index}}" name="prodSelected" ng-value="true" type="checkbox"
                                       ng-model="product.selected" ng-click="toggleSelection(product)">
                                <label for="prod{{$index}}" class="product-selection-checkbox"></label>
                            </div>
                            <div ng-if="selectedType=='radio'" class="rdio rdio-default" style="margin-left: 25px;">
                                <input id="prodd{{$index}}" name="prodSelected" ng-value="true" type="radio"
                                       ng-model="product.selected" ng-click="toggleSelection(product)">
                                <label for="prodd{{$index}}" ></label>
                            </div>
                        </td>


                            <td style="vertical-align: middle;">
                            {{ product.name }}
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br/>
</div>
<div class="modal-footer" style="background-color: #F9F9F9;" style="text-align: left;">
    <div class="row">
        <div class="col-md-12" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel</button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()" ng-disabled="selectedProducts.length == 0">Select</button>
        </div>
    </div>
</div>