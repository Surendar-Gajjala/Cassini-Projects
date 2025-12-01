<div style="padding: 20px;">
    <div>
        <div ng-if="advancedEcoSearchVm.ecos.length == 0" style="line-height: 40px;font-size: 30px;cursor: pointer"
             ng-click='advancedEcoSearchVm.onAddeco()'>
            <i class="la la-plus" style="color: #1CAF9A"></i>
        </div>
        <div class="row" ng-repeat="change in advancedEcoSearchVm.ecos">
            <div class="col-md-1" style="line-height: 40px;font-size: 30px;cursor: pointer"
                 ng-click='advancedEcoSearchVm.onAddeco()'>
                <i class="la la-plus" style="color: #1CAF9A"></i>
            </div>
            <div class="col-md-4">
                <ui-select ng-model="change.field">
                    <ui-select-match placeholder="{{advancedEcoSearchVm.selectTitle}}">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices repeat="change in advancedEcoSearchVm.ecoNames | filter: $select.search">
                        <div ng-bind="change.label"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="col-md-3">
                <ui-select ng-model="change.operator">
                    <ui-select-match placeholder="{{advancedEcoSearchVm.selectTitle}}">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices repeat="operator in change.field.operator | filter: $select.search">
                        <div ng-bind="operator.label"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="col-md-3">
               <span>
                   <input ng-model="change.value" type="text" class="form-control"
                          placeholder="{{advancedEcoSearchVm.enterValueTitle}}"><br>
               </span>
            </div>

            <div class="col-md-1" style="line-height: 40px;font-size: 30px;cursor: pointer"
                 ng-click='advancedEcoSearchVm.deleteeco(change)'>
                <i class="fa fa-minus-circle" style="color: #d9534f"></i>
            </div>
        </div>
        <br>
        <br>
    </div>
</div>
</div>


