<div style="padding: 20px;">
    <style scoped>

    </style>
    <div class="row">
        <div class="col-sm-11 col-sm-offset-1">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label"><span
                            translate>Search Type </span><span> : </span></label>

                    <div class="col-sm-7" <%--style="margin-left: 200px"--%>>
                        <div class="form-check" style="padding:8px;">
                            <label class="form-check-label" style="margin-right: 5px">
                                <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios2"
                                       ng-click="searchDialogueVm.selectType('Simple', $event)" checked><span
                                    style="padding: 2px;margin-left: 5px;" translate>Simple Search</span>
                            </label>
                            <label class="form-check-label" style="margin-right: 33px;margin-left: 11px">
                                <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios1"
                                       ng-click="searchDialogueVm.selectType('Advance', $event)"><span
                                    style="padding: 2px;margin-left: 5px;" translate>Advance Search</span>
                            </label>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <br><br>

    <div class="row">

        <%--------------------- custom Object simple Search ----------------------%>

        <div class="col-md-11 col-sm-offset-1" ng-show="searchDialogueVm.customSimpleSearch == true">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-md-4 control-label"><span
                            translate>CUSTOM_OBJECT_NUMBER </span><span> : </span></label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="searchDialogueVm.customFilters.number"
                               ng-enter="searchDialogueVm.search()" style="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">
                        <span translate>CUSTOM_OBJECT_NAME</span> <span> : </span>
                    </label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="searchDialogueVm.customFilters.name"
                               ng-enter="searchDialogueVm.search()" style="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">
                        <span translate>CUSTOM_OBJECT_DESCRIPTION</span> <span> : </span>
                    </label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="searchDialogueVm.customFilters.description"
                               ng-enter="searchDialogueVm.search()" style="">
                    </div>
                </div>
            </form>
        </div>


        <%--------------------- custom Object AdvanceSearch ----------------------%>
        <div>
            <div ng-show="searchDialogueVm.customAdvanceSearch == true" style="">
                <div ng-if="searchDialogueVm.customObjects.length == 0"
                     style="line-height: 25px;font-size: 30px;width: 30px;cursor: pointer"
                     ng-click='searchDialogueVm.onAddObject()'>
                    <i class="la la-plus" style="color: #1CAF9A"></i>
                </div>

                <div class="row" ng-repeat="item in searchDialogueVm.customObjects">
                    <div class="col-md-1" style="line-height: 25px;font-size: 30px;width: 30px;cursor: pointer"
                         ng-click='searchDialogueVm.onAddObject()'>
                        <i class="la la-plus" style="color: #1CAF9A"></i>
                    </div>
                    <div class="col-md-4">
                        <ui-select ng-model="item.field" on-select="searchDialogueVm.itemTree($item, item)">
                            <ui-select-match placeholder="{{searchDialogueVm.selectTitle}}">{{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices repeat="item1 in searchDialogueVm.customNames | filter: $select.search">
                                <div ng-bind="item1.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div class="col-md-4" <%--id="operator"--%>>
                        <ui-select ng-model="item.operator">
                            <ui-select-match placeholder="{{searchDialogueVm.selectTitle}}">{{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices style="overflow-x: auto !important;"
                                               repeat="operator in item.field.operator | filter: $select.search">
                                <div ng-bind="operator.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>

                    <div class="col-md-3" ng-show="item.textValue == true">
                   <span>
                       <input style="" ng-model="item.value" type="text" class="form-control"
                              placeholder="{{searchDialogueVm.enterValueTitle}}"><br>
                   </span>
                    </div>
                    <%--<div class="col-md-3" ng-show="item.itemTree1 == true">
                        <div class="input-group mb15">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                    <span translate>SELECT</span> <span class="caret"
                                                                        style="margin-left: 4px;"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <relation-classification-tree
                                                on-select-type="searchDialogueVm.onSelectItemType"></relation-classification-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="searchDialogueVm.itemName" readonly>
                        </div>
                    </div>--%>

                    <div style="line-height: 30px;font-size: 20px;cursor: pointer"
                         ng-click='searchDialogueVm.deleteCustomObject(item)'>
                        <i class="la la-times" style="color: #d9534f"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
