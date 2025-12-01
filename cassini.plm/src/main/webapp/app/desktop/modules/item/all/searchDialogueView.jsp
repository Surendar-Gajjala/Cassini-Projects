<div style="padding: 20px;">
    <style scoped>

    </style>
    <div class="row">
        <div class="col-sm-9 col-sm-offset-1">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label"><span
                            translate>Search Type </span><span> : </span></label>

                    <div class="col-sm-8" <%--style="margin-left: 200px"--%>>
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
        <div class="col-sm-9 col-sm-offset-1" ng-show="searchDialogueVm.itemSimpleSearch == true">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>ITEM_ALL_ITEM_TYPE</span> <span> : </span>
                    </label>

                    <div class="col-sm-8">
                        <div class="input-group mb15">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                    <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;width: 230px;">
                                        <classification-tree
                                                ng-if="itemsMode == null || itemsMode == '' || itemsMode == undefined"
                                                on-select-type="searchDialogueVm.onSelectType"></classification-tree>
                                        <item-class-tree item-class="{{itemsMode}}"
                                                         ng-if="itemsMode != null && itemsMode != '' && itemsMode != undefined"
                                                         on-select-type="searchDialogueVm.onSelectType"></item-class-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title" style=""
                                   ng-model="searchDialogueVm.selectedType.name" readonly>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><span
                            translate>ITEM_ALL_ITEM_NUMBER </span><span> : </span></label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="title"
                               ng-model="searchDialogueVm.filters.itemNumber"
                               ng-enter="searchDialogueVm.search()" style="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>ITEM_ALL_ITEM_NAME</span> <span> : </span>
                    </label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" name="title"
                               ng-model="searchDialogueVm.filters.itemName"
                               ng-enter="searchDialogueVm.search()" style="">
                    </div>
                </div>
            </form>
        </div>


        <%--------------------- itemAdvanceSearch ----------------------%>
        <div>
            <div ng-show="searchDialogueVm.itemAdvanceSearch == true" style="">
                <div ng-if="searchDialogueVm.items.length == 0"
                     style="line-height: 25px;font-size: 30px;width: 30px;cursor: pointer"
                     ng-click='searchDialogueVm.onAddItem()'>
                    <i class="la la-plus" style="color: #1CAF9A"></i>
                </div>

                <div class="row" ng-repeat="item in searchDialogueVm.items">
                    <div class="col-md-1" style="line-height: 25px;font-size: 30px;width: 30px;cursor: pointer"
                         ng-click='searchDialogueVm.onAddItem()'>
                        <i class="la la-plus" style="color: #1CAF9A"></i>
                    </div>
                    <div class="col-md-4">
                        <ui-select ng-model="item.field" on-select="searchDialogueVm.itemTree($item, item)">
                            <ui-select-match placeholder="{{searchDialogueVm.selectTitle}}">{{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices repeat="item1 in searchDialogueVm.itemNames | filter: $select.search">
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
                    <div class="col-md-3" ng-show="item.itemTree1 == true">
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
                    </div>

                    <div class="col-md-3" ng-show="item.part == true">
                        <ui-select ng-model="searchDialogueVm.phase">
                            <ui-select-match placeholder="{{searchDialogueVm.selectTitle}}">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices style="width: 250px !important;left: auto;right: 0;"
                                               repeat="phase in searchDialogueVm.lifeCyclePhase | filter: $select.search">
                                <div ng-bind="phase"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>

                    <div style="line-height: 30px;font-size: 20px;cursor: pointer"
                         ng-click='searchDialogueVm.deleteItem(item)'>
                        <i class="la la-times" style="color: #d9534f"></i>
                    </div>
                </div>

            </div>
        </div>

        <%--------------------- ecoSimpleSearch ----------------------%>
        <div class="row">
            <div class="col-sm-9 col-sm-offset-1" ng-show="searchDialogueVm.ecoSimpleSearch == true">
                <div style="">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>ECO_NUMBER</span> : </label>

                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="ecoNumber"
                                       placeholder="{{searchDialogueVm.numberTitle}}"
                                       ng-model="searchDialogueVm.ecoFilters.ecoNumber"
                                       ng-enter="searchDialogueVm.search()" style="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>TITLE</span> : </label>

                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="title" placeholder="{{searchDialogueVm.title}}"
                                       ng-model="searchDialogueVm.ecoFilters.title"
                                       ng-enter="searchDialogueVm.search()" style="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="description"
                                       placeholder="{{searchDialogueVm.descriptionTitle}}"
                                       ng-model="searchDialogueVm.ecoFilters.description"
                                       ng-enter="searchDialogueVm.search()" style="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>STATUS</span> :</label>

                            <div class="col-sm-8">
                                <ui-select ng-model="searchDialogueVm.ecoFilters.statusType" theme="bootstrap"
                                           style="">
                                    <ui-select-match placeholder="{{searchDialogueVm.selectTitle}}">{{$select.selected}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="status in ['NORMAL', 'RELEASED', 'REJECTED'] | filter: $select.search">
                                        <div ng-bind="status"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>OWNER</span> : </label>

                            <div class="col-sm-8">
                                <ui-select ng-model="searchDialogueVm.ecoFilters.ecoOwnerObject" theme="bootstrap"
                                           style="">
                                    <ui-select-match placeholder="{{searchDialogueVm.selectTitle1}}">
                                        {{$select.selected.firstName}}
                                    </ui-select-match>
                                    <ui-select-choices repeat="person in searchDialogueVm.persons | filter: $select.search">
                                        <div ng-bind="person.firstName"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <%--------------------- ecoAdvanceSearch ----------------------%>
        <div>
            <div ng-show="searchDialogueVm.ecoAdvanceSearch == true">
                <div ng-if="searchDialogueVm.ecos.length == 0"
                     style="line-height: 25px;font-size: 30px;width: 30px;cursor: pointer"
                     ng-click='searchDialogueVm.onAddeco()'>
                    <i class="la la-plus" style="color: #1CAF9A"></i>
                </div>
                <div class="row" ng-repeat="change in searchDialogueVm.ecos">
                    <div class="col-md-1" style="line-height: 25px;font-size: 30px;width: 30px;cursor: pointer"
                         ng-click='searchDialogueVm.onAddeco()'>
                        <i class="la la-plus" style="color: #1CAF9A"></i>
                    </div>
                    <div class="col-md-4">
                        <ui-select ng-model="change.field">
                            <ui-select-match placeholder="{{searchDialogueVm.selectTitle}}">{{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices repeat="change in searchDialogueVm.ecoNames | filter: $select.search">
                                <div ng-bind="change.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div class="col-md-4">
                        <ui-select ng-model="change.operator">
                            <ui-select-match placeholder="{{searchDialogueVm.selectTitle}}">{{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices style="overflow-x: auto !important;"
                                               repeat="operator in change.field.operator | filter: $select.search">
                                <div ng-bind="operator.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div class="col-md-3">
                   <span>
                       <input style="" ng-model="change.value" type="text" class="form-control"
                              placeholder="{{searchDialogueVm.enterValueTitle}}"><br>
                   </span>
                    </div>

                    <div style="line-height: 30px;font-size: 20px;cursor: pointer"
                         ng-click='searchDialogueVm.deleteeco(change)'>
                        <i class="la la-times" style="color: #d9534f"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
