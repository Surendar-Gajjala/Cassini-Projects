<script type="text/ng-template" id="new-materialpo-view-tb">
    <div>
        <button ng-if="newPO" class="btn btn-sm btn-primary mr20" ng-click="createPO()">Create</button>
        <button ng-if="!newPO" class="btn btn-sm btn-primary mr20" ng-click="updatePO()">Update</button>
        <button class="btn btn-sm btn-danger mr20" ng-click="cancelPo()" style="margin-left: -16px;">Cancel</button>
    </div>
</script>
<br>


<div class="row">
    <div class="col-sm-9">
        <div class="table-responsive">
            <h4 class="section-title text-primary">Items:</h4>

            <div>
                <button class="btn btn-primary" ng-click="selectMaterials()" ng-hide="returnCreated">Select</button>
                <button class="btn btn-danger" ng-click="clearPO()" ng-disabled="poMaterials.length == 0">Clear All
                </button>
            </div>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Item</th>
                    <th style="width:120px;">Notes</th>
                    <th>SKU</th>
                    <th>Units</th>
                    <th>Supplier</th>
                    <th>Units Price</th>
                    <th>Received Qty</th>
                    <th style="width:120px;text-align: center">Quantity</th>
                    <th>Item Total</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-show="poMaterials.length == 0">
                    <td colspan="4" style="text-align: left">
                        <span>No items in the PO.</span>
                    </td>
                </tr>
                <tr ng-repeat="item in poMaterials">
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.material.name}}</strong></div>
                        <small class="text-danger">
                            <a title="Remove this item" href="" ng-click="removeItem(item)" style="color: #FF4500">Remove</a>
                        </small>
                    </td>

                    <td align="center" style="width:120px; text-align: center; vertical-align: middle;">
                        <span ng-if="item.notes != undefined && item.notes != null">{{item.notes}}</span>
                        <input ng-if="item.notes == undefined || item.notes == null"
                               type="text" class="form-control"
                               style="display: inline; width: 80px; text-align: center"
                               ng-model="item.notes"/>
                    </td>


                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.material.sku}}</strong></div>
                    </td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.material.units}}</strong></div>
                    </td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.supplier.name}}</strong></div>
                    </td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.unitPrice}}</strong></div>
                    </td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.issQuantity}}</strong></div>
                    </td>
                    <td align="center" style="width:120px; text-align: center; vertical-align: middle;">
                        <span ng-show="returnCreated">{{item.quantity}}</span>
                        <input type="text" class="form-control"
                               style="display: inline; width: 80px; text-align: center"
                               ng-model="item.quantity" ng-blur="addToTotal(item)" value="{{item.quantity}}"
                               ng-hide="returnCreated"/>
                    </td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.itemTotal}}</strong></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" align="right">Order Total</td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{materialPO.orderTotal}}</strong></div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>