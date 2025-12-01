<div style="position: relative;">
    <style>
        .search-form {
            height: 26px;
            border-radius: 15px;
        }

        .search-box {
            width: 100%;
            margin: 0;
            float: right;
        }

        tr.disabled-reviewd td {
            background-color: red !important;
        }

        .search-box input {
            height: 34px !important;
        }

        .search-box button {
            margin-top: 3px !important;
            line-height: 0px;
            width: 75px;
            height: 34px;
            padding: 0px;
        }

        .searchResults {
            display: none;
            position: absolute;
            top: 37px;
            left: 0;
            width: 100%;
            max-height: 340px;
            min-height: 100px;
            border: 1px solid #ddd;
            z-index: 100;
            background-color: #fff;
            overflow-y: auto;
        }

        .searchResults table th, .searchResults table td {
            padding: 5px !important;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 0px 20px;min-height: 378px;">
        <button ng-if="newItemBomButton == true" type="button" class="btn btn-sm btn-info" title="Click to Search Items"
                ng-click="newBomItemVm.cancelNew()">Search
        </button>
        <div class="col-md-12 text-right" style="padding: 0px;">
            <div class="input-group mb15 search-box" ng-if="newItemBomButton == false">
                <input id="newItemSearchBox" type="text" class="form-control input-sm"
                       placeholder="Search Item"
                       onfocus="this.setSelectionRange(0, this.value.length)"
                       ng-click="newBomItemVm.preventClick($event)"
                       ng-model="newBomItemVm.filters.searchQuery"
                       ng-model-options="{ debounce: 100 }"
                       ng-change="newBomItemVm.performSearch()">
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-primary"
                                ng-disabled="newBomItemVm.enableNewItemButton == false"
                                title="Click to New Item"
                                ng-click="newBomItemVm.addNew()">New Item
                        </button>
                    </span>
            </div>

            <div id="newItemSearchResults" class="searchResults">
                <table class="table table-striped table-condensed">
                    <thead>
                    <tr>
                        <th style="min-width: 150px;">Nomenclature</th>
                        <th style="min-width: 85px">Type</th>
                        <th>Description</th>
                        <th style="width: 50px">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="newBomItemVm.searchResults.content.length == 0">
                        <td colspan="15" style="text-align: left;">No items found</td>
                    </tr>
                    <tr ng-repeat="result in newBomItemVm.searchResults.content">
                        <td class="text-left">
                            <span ng-bind-html="result.itemName | highlightText: newBomItemVm.filters.searchQuery"></span>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.itemType.name | highlightText: newBomItemVm.filters.searchQuery"></span>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.description | highlightText: newBomItemVm.filters.searchQuery"></span>
                        </td>
                        <td class="text-center" style="width: 50px">
                            <a href="" title="Add item"
                               ng-click="newBomItemVm.addItemToBom(result);newBomItemVm.preventClick($event)"><i
                                    class="fa fa-plus-circle" style="font-size: 18px;"></i></a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <%-----------------------------------   New Item View ---------------------------------%>

        <div ng-if="newItemBomButton == true">
            <h4 class="section-title" style="color: black;">Basic Info</h4>

            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Type</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <div class="input-group mb15">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                        ng-click="newBomItemVm.showDropMenu()">
                                    <span>Select</span> <span class="caret"
                                                              style="margin-left: 4px;"></span>
                                </button>
                                <div class="dropdown-menu" role="menu" id="typeTree">
                                    <input type="search" class="form-control input-sm search-form"
                                           placeholder="Search Type"
                                           ng-model="newBomItemVm.searchValue" ng-change="newBomItemVm.searchTree()"
                                           uib-dropdown-toggle/>

                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <classification-tree system="false"
                                                             on-select-type="newBomItemVm.onSelectType"></classification-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="newBomItemVm.newBomItem.newItem.itemType.name" readonly>


                        </div>
                    </div>
                </div>

                <%--<div class="form-group">

                    <label class="col-sm-4 control-label">
                        <span>Item Number</span>
                        <span class="asterisk">*</span> :
                    </label>

                    <div class="col-sm-7">
                        <div class="input-group mb15">
                            <div class="input-group-btn">
                                <button class="btn btn-default" type="button" style="width: 85px"
                                        ng-click="newBomItemVm.autoNumber()">Auto
                                </button>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="newBomItemVm.newBomItem.newItem.itemNumber">
                        </div>
                    </div>
                </div>--%>


                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Nomenclature</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomItemVm.newBomItem.newItem.itemName">
                    </div>
                </div>

                <div class="form-group" ng-if="newBomItemVm.newBomItem.newItem.itemType.hasSpec">
                    <label class="col-sm-4 control-label">
                        Specifications <span class="asterisk">*</span>:
                    </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="newBomItemVm.newBomItem.newItem.itemTypeSpec" theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match placeholder="{{newBomItemVm.specificationTitle}}">
                                {{$select.selected.specName}}
                            </ui-select-match>
                            <ui-select-null-choice></ui-select-null-choice>
                            <ui-select-choices
                                    repeat="spec in newBomItemVm.itemTypeSpecs | filter: $select.search">
                                <div ng-bind="spec.specName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group" ng-if="newBomItemVm.newBomItem.newItem.itemType != null">
                    <label class="col-sm-4 control-label">
                        <span>Item Code</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input ng-if="newBomItemVm.typeParentNode.parentNode && (newBomItemVm.typeParentNode.name == 'Section' || newBomItemVm.typeParentNode.name == 'Sub System')"
                               type="text" class="form-control" name="title"
                               ng-model="newBomItemVm.newBomItem.newItem.itemCode"
                               maxlength="1" style="text-transform: uppercase;">
                        <input ng-if="newBomItemVm.typeParentNode.parentNode && (newBomItemVm.typeParentNode.name == 'System' || newBomItemVm.typeParentNode.name == 'Unit' || newBomItemVm.typeParentNode.name == 'Part')"
                               type="text" class="form-control" name="title"
                               ng-model="newBomItemVm.newBomItem.newItem.itemCode"
                               minlength="1" maxlength="2" style="text-transform: uppercase;">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Description</span>: </label>

                    <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newBomItemVm.newBomItem.newItem.description"></textarea>
                    </div>
                </div>

                <div class="form-group"
                     ng-if="newBomItemVm.typeParentNode.name == 'Part' && newBomItemVm.newBomItem.newItem.itemType != null">
                    <label class="col-sm-4 control-label">
                        <span>Quantity</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input ng-if="!newBomItemVm.newBomItem.newItem.itemType.hasLots" type="text"
                               class="form-control"
                               name="title" ng-model="newBomItemVm.newBomItem.quantity">
                        <input ng-if="newBomItemVm.newBomItem.newItem.itemType.hasLots" type="text" class="form-control"
                               name="title" ng-model="newBomItemVm.newBomItem.fractionalQuantity">

                    </div>
                </div>

                <div class="form-group" ng-if="newBomVm.newBomItem.newItem.itemType.parentNodeItemType == 'Part'">
                    <label class="col-sm-4 control-label">
                        <span>Units</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomItemVm.newBomItem.newItem.itemType.units" readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Drawing Number</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomItemVm.newBomItem.newItem.drawingNumber">
                    </div>
                </div>

                <attributes-view show-objects="selectObjectValues"
                                 attributes="newBomItemVm.newItemAttributes"></attributes-view>
                <br>
                <attributes-view show-objects="selectObjectValues"
                                 attributes="newBomItemVm.newItemRevisionAttributes"></attributes-view>
            </form>

            <h4 ng-if="newBomItemVm.requiredAttributes.length > 0 || newBomItemVm.attributes.length > 0"
                class="section-title" style="color: black;">Attributes
            </h4>
            <br>

            <div>
                <form class="form-horizontal" ng-if="newBomItemVm.newBomItem.newItem.itemType != null">
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newBomItemVm.requiredAttributes"></attributes-view>
                    <br>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newBomItemVm.attributes"></attributes-view>
                </form>
            </div>
        </div>

    </div>
</div>