<div style="padding: 20px;">
    <div>
        <button class='btn btn-xs btn-success' ng-click='advancedSearchVm.onAddItem()'>Add Filter</button>
        <hr>

        <div class="row" ng-repeat="item in advancedSearchVm.items">
            <div class="col-md-5">
                <ui-select ng-model="item.field">
                    <ui-select-match placeholder="Select">{{$select.selected.field}}</ui-select-match>
                    <ui-select-choices repeat="item in advancedSearchVm.itemNames | filter: $select.search">
                        <div ng-bind="item.field"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="col-md-3">
                <ui-select ng-model="item.operator">
                    <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                    <ui-select-choices repeat="operator in item.field.operator | filter: $select.search">
                        <div ng-bind="operator"></div>
                    </ui-select-choices>
                </ui-select>
            </div>

            <div class="col-md-3">
               <span>
                   <input ng-model="item.value" type="text" class="form-control" placeholder="Enter Value"><br>
               </span>
            </div>
            <div class="col-md-1">
                <button title="Delete this item" class="btn btn-danger"
                        ng-click="advancedSearchVm.deleteItem(item)">
                    <i class="fa fa-trash"></i>
                </button>
            </div>
        </div>
        <br>
        <br>
    </div>
</div>
