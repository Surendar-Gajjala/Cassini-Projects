<button class="btn btn-primary" ng-click="createReturn()" ng-hide="returnCreated">Submit</button>
<button class="btn btn-success" ng-click="createAnotherReturn()" ng-show="createAnother">Create Another Return</button>
<button class="btn btn-default" onclick="window.history.back()">Cancel</button>
<hr>
<div class="row" style="margin-left: 2px;margin-right: 2px;">
    <div class="col-sm-3 styled-panel">
        <div>
            <h4 class="section-title text-primary" style="margin-top: 0;">Customer:</h4>
            <div ng-show="selectedCustomer != null">
                <h4>Name: <span class="text-success">{{selectedCustomer.name}}</span></h4>
                <h4>Region: <span class="text-success">{{selectedCustomer.salesRegion.name}}</span></h4>
                <h4>Sales Rep: <span class="text-success">{{selectedCustomer.salesRep.firstName}}</span></h4>
            </div>
            <button class="btn btn-sm btn-default" ng-hide="returnCreated" ng-click="showCustomerSelectionDialog()">{{selectText}}</button>
        </div>

        <br/><br/>

        <div>
            <h4 class="section-title text-primary">Reason:</h4>
            <div ng-show="returnCreated">{{reason}}</div>
            <textarea name="reason" class='form-control'  ng-hide="returnCreated"
                      style="resize: none;" rows="5" ng-model="reason"></textarea>
        </div>
    </div>
    <div class="col-sm-9" style="padding-top: 10px;padding-left: 25px;">
        <div class="table-responsive">
            <h4 class="section-title text-primary">Items:</h4>
            <br>
            <div>
                <button class="btn btn-primary" ng-click="selectProducts()" ng-hide="returnCreated">Select</button>
                <button class="btn btn-danger" ng-click="clearReturns()"
                        ng-hide="returnCreated" ng-disabled="returnItems.length == 0">Clear All</button>
            </div>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Item</th>
                    <th style="width:120px;text-align: center">Quantity</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-show="returnItems.length == 0">
                    <td colspan="4" style="text-align: left">
                        <span>No items in the return.</span>
                    </td>
                </tr>
                <tr ng-repeat="item in returnItems">
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.product.name}}</strong></div>
                        <small class="text-danger">
                            <a title="Remove this item" href="" ng-click="removeItem($index)" style="color: #FF4500">Remove</a>
                        </small>
                    </td>
                    <td align="center" style="width:120px; text-align: center; vertical-align: middle;">
                        <span ng-show="returnCreated">{{item.quantity}}</span>
                        <input type="number" class="form-control"
                               style="display: inline; width: 80px; text-align: center"
                               ng-model="item.quantity" value="{{item.quantity}}" ng-hide="returnCreated"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>