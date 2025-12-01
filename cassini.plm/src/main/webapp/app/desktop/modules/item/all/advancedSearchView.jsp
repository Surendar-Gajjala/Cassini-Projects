<div style="padding: 20px;">
    <div>
        <div ng-if="advancedSearchVm.items.length == 0" style="line-height: 40px;font-size: 30px;cursor: pointer"
             ng-click='advancedSearchVm.onAddItem()'>
            <i class="la la-plus" style="color: #1CAF9A"></i>
        </div>

        <div class="row" ng-repeat="item in advancedSearchVm.items">
            <div class="col-md-1" style="line-height: 40px;font-size: 30px;cursor: pointer"
                 ng-click='advancedSearchVm.onAddItem()'>
                <i class="la la-plus" style="color: #1CAF9A"></i>
            </div>
            <div class="col-md-4">
                <ui-select ng-model="item.field" on-select="advancedSearchVm.itemTree($item)">
                    <ui-select-match placeholder="{{advancedSearchVm.selectTitle}}">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices repeat="item in advancedSearchVm.itemNames | filter: $select.search">
                        <div ng-bind="item.label"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="col-md-3" <%--id="operator"--%>>
                <ui-select ng-model="item.operator">
                    <ui-select-match placeholder="{{advancedSearchVm.selectTitle}}">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices repeat="operator in item.field.operator | filter: $select.search">
                        <div ng-bind="operator.label"></div>
                    </ui-select-choices>
                </ui-select>
            </div>

            <div class="col-md-3" ng-show="advancedSearchVm.textValue == true">
               <span>
                   <input ng-model="item.value" type="text" class="form-control"
                          placeholder="{{advancedSearchVm.enterValueTitle}}"><br>
               </span>
            </div>
            <div class="col-md-3" ng-show="advancedSearchVm.itemTree1 == true">
                <div class="input-group mb15">
                    <div class="input-group-btn" uib-dropdown>
                        <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                            <span translate>SELECT</span> <span class="caret"
                                                                style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                <classification-tree
                                        on-select-type="advancedSearchVm.onSelectType"></classification-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control" name="title"
                           ng-model="advancedSearchVm.itemName" readonly>
                </div>
            </div>

            <div class="col-md-3" ng-show="advancedSearchVm.part == true">
                <ui-select ng-model="advancedSearchVm.phase">
                    <ui-select-match placeholder="{{advancedSearchVm.selectTitle}}">{{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices style="width: 250px !important;left: auto;right: 0;"
                                       repeat="phase in advancedSearchVm.lifeCyclePhase | filter: $select.search">
                        <div ng-bind="phase"></div>
                    </ui-select-choices>
                </ui-select>
            </div>

            <div class="col-md-1" style="line-height: 40px;font-size: 30px;cursor: pointer"
                 ng-click='advancedSearchVm.deleteItem(item)'>
                <i class="fa fa-minus-circle" style="color: #d9534f"></i>
            </div>
        </div>
        <br>
        <br>
    </div>
</div>
