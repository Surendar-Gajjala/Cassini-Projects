<style>
    div.border1 {
        border: 0.5px solid #d1c9c9;
        padding: 1px;
    }

</style>

<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Select Materials</h3>
</div>
<div class="modal-body" style="max-height:500px;">
    <span ng-show="errorMessage != null" style="color:#f80b0b; font-size:15px">{{errorMessage}}</span>

    <div class="row">
        <div class="col-md-12" style="text-align: center" ng-show="materials.numberOfElements > 0">
            <div>
                <pagination total-items="materials.totalElements"
                            items-per-page="20"
                            max-size="5"
                            boundary-links="true"
                            ng-model="pageable.page"
                            ng-change="loadProducts()">
                </pagination>
            </div>

            <div style="margin-top: -25px;">
                <small>Total {{materials.totalElements}} materials</small>
            </div>
        </div>
    </div>
    <div class="container col-md-12" style="background-color: #F9F9F9;">
        <div class="row">
            <div class="col-xs-1 text-center">
            </div>
            <div class="col-xs-3"><strong>Name</strong></div>
            <div class="col-xs-3"><strong>SKU</strong></div>
            <div class="col-xs-2"><strong>Unit</strong></div>
            <div class="col-xs-3"><strong>Unit Price</strong></div>
        </div>
        <div class="row" ng-repeat="material in materials.content" ng-click="showDetail(u.user)">
            <div class="col-xs-1 text-center border1"><input id="mat{{$index}}" name="matSelected" ng-value="true"
                                                             type="checkbox"
                                                             ng-model="material.selected"
                                                             ng-click="toggleSelection(material)">
            </div>
            <div class="col-xs-3 border1">{{material.name}}</div>
            <div class="col-xs-3 border1">{{material.sku}}</div>
            <div class="col-xs-2 border1">{{material.units}}</div>
            <div class="col-xs-3 border1">{{material.unitPrice}}</div>
            <div class="row col-md-6 border1" ng-show="material.selected" style="background-color: #d3d3d3;">
                <div class="row">
                    <div class="col-md-4 td-heading">
                    </div>
                    <div class="col-md-4"><strong>Supplier</strong></div>
                    <div class="col-md-4"><strong>Cost</strong></div>
                </div>
                <div ng-if="material.materialSuppliers.length == 0">No Suppliers</div>
                <div ng-if="material.materialSuppliers.length >= 0" class="row"
                     ng-repeat="supplier in material.materialSuppliers" ng-click="showDetail(u.user)">
                    <div class="col-md-4 text-center">
                        <input name="{{material.id}}" ng-value="supplier.cost" type="radio"
                               ng-disabled="supplier.cost == null"
                               ng-model="material.unitPrice" ng-click="material.supplier = supplier.supplierIdObject">
                    </div>
                    <div class="col-md-4">{{supplier.supplierIdObject.name}}</div>
                    <div class="col-md-4" ng-if="edit == false"><a href="" ng-click="editCost()">{{supplier.cost}}</a>
                    </div>
                    <div ng-if="edit == true">
                        <input type="number" style="width: 50px;" title="name" ng-model="supplier.cost">
                        <i class="fa fa-check-circle" title="Click to Save"
                           ng-click="saveCost(supplier)"
                           style="font-size:18px;cursor:pointer;"></i>
                    </div>
                </div>

            </div>
        </div>


    </div>

    <%--<div class="row">
        <div class="col-md-12" style="padding:0px">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 80px; text-align: center">
                        <input id="prodColumn" name="prodSelected" type="checkbox" value="" style="display:none">
                        <label for="prodColumn"></label>
                    </th>
                    <th style="">Name</th>
                    <th style="">SKU</th>
                    <th style="">Units</th>
                    <th style="">Unit Price</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <div class="btn-group" style="margin-bottom: 0px;">
                            <button type="button" class="btn btn-sm btn-success" ng-click="applyCriteria()"
                                    title="Search"><i class="fa fa-search"></i></button>
                            <button type="button" class="btn btn-sm btn-default" ng-click="resetCriteria()"
                                    title="Clear Search"><i class="fa fa-times"></i></button>
                        </div>
                    </td>
                    <td style="vertical-align: middle;">
                        <input placeholder="Enter name" class="form-control" type="text"
                               ng-model="filters.name" ng-enter="applyCriteria()">
                    </td>
                </tr>

                <tr ng-if="materials.content.length == 0">
                    <td colspan="2">
                        <span ng-hide="loading">No materials</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading materials...
                            </span>
                    </td>
                </tr>


                <tr ng-repeat="material in materials.content">
                    <td style="width:50px; text-align:center; vertical-align: middle;" align="center">

                        <div ng-if="selectedType=='check'" class="ckbox ckbox-default" style="margin-left: 25px;">
                            <input id="mat{{$index}}" name="matSelected" ng-value="true" type="checkbox"
                                   ng-model="material.selected" ng-click="toggleSelection(material)">
                            <label for="mat{{$index}}" class="material-selection-checkbox"></label>
                        </div>
                        <div ng-if="selectedType=='radio'" class="rdio rdio-default" style="margin-left: 25px;">
                            <input id="matt{{$index}}" name="matSelected" ng-value="true" type="radio"
                                   ng-model="material.selected" ng-click="toggleSelection(material)">
                            <label for="matt{{$index}}"></label>
                        </div>
                    </td>
                    <td ng-show="material.selected">
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
                            <tr ng-if="material.suppliers.length == 0">
                                <td colspan="3">
                                    <span ng-hide="loading">No suppliers</span>
                                </td>
                            </tr>
                            <tr ng-repeat="supplier in material.suppliers">
                                <td style="width:50px; text-align:center; vertical-align: middle;" align="center">
                                    <div style="margin-left: 25px;">
                                        <input id="supplier{{$index}}" ng-value="true" type="radio"
                                               ng-model="supplier.checked" ng-click="material.unitPrice = supplier.cost">
                                        <label for="supplier{{$index}}"></label>
                                    </div>
                                </td>

                                <td style="vertical-align: middle;">
                                    {{ supplier.name }}
                                </td>
                                <td style="vertical-align: middle;">
                                    <input ng-show="supplier.checked" type="number" ng-model="supplier.cost"
                                           style="width: 80px"/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                    <td style="vertical-align: middle;">
                        {{ material.name }}
                    </td>
                    <td style="vertical-align: middle;">
                        {{ material.sku }}
                    </td>
                    <td style="vertical-align: middle;">
                        {{ material.units }}
                    </td>
                    <td style="vertical-align: middle;">
                        {{ material.unitPrice }}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>--%>
    <br/>
</div>
<div class="modal-footer" style="text-align: left;">
    <div class="row">
        <div class="col-md-12" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel
            </button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()">Select
            </button>
        </div>
    </div>
</div>