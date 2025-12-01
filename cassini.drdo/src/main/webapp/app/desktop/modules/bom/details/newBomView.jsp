<div style="position: relative;">
    <style>
        .search-form {
            height: 30px;
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
        <div>
            <button ng-if="newBomItemButton == true" type="button" class="btn btn-sm btn-info"
                    title="Click to enable search" style="margin-top: 5px;"
                    ng-click="newBomVm.cancelNew()">Search
            </button>
        </div>
        <div class="col-md-12 text-right" style="padding: 0px;">
            <div class="input-group mb15 search-box" ng-if="newBomItemButton == false">
                <input id="newItemSearchBox" type="text"
                       class="form-control input-sm"
                       placeholder="Search Item"
                       onfocus="this.setSelectionRange(0, this.value.length)"
                       ng-click="newBomVm.preventClick($event)"
                       ng-model="newBomVm.filters.searchQuery"
                       ng-model-options="{ debounce: 100 }"
                       ng-change="newBomVm.performSearch()">
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-primary"
                        <%--ng-disabled="newBomVm.enableNewItemButton == false"--%>
                                title="Click to New Item"
                                ng-click="newBomVm.addNew()">New Item
                        </button>
                    </span>
            </div>

            <div id="newItemSearchResults" class="searchResults">
                <table class="table table-striped table-condensed">
                    <thead>
                    <tr>
                        <th style="width: 50px">Add</th>
                        <th style="min-width: 150px;">Nomenclature</th>
                        <th style="max-width: 75px;">ItemCode</th>
                        <th style="min-width: 85px">Type</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="newBomVm.searchResults.content.length == 0">
                        <td colspan="15" style="text-align: left;">No items found</td>
                    </tr>
                    <tr ng-repeat="result in newBomVm.searchResults.content">
                        <td style="width: 50px;text-align: left;">
                            <a href="" title="Add item"
                               ng-click="newBomVm.addItemToBom(result);newBomVm.preventClick($event)"><i
                                    class="fa fa-plus-circle" style="font-size: 18px;"></i></a>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.itemName | highlightText: newBomVm.filters.searchQuery"></span>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.itemCode | highlightText: newBomVm.filters.searchQuery"></span>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.itemType.name | highlightText: newBomVm.filters.searchQuery"></span>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.description | highlightText: newBomVm.filters.searchQuery"></span>
                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <%-----------------------------------   New Item View ---------------------------------%>

        <div ng-if="newBomItemButton == true">
            <h4 class="section-title" style="color: black;">Basic Info</h4>

            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Type</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="newBomVm.newBomItem.newItem.itemType"
                                   on-select="newBomVm.onSelectType($item)"
                                   theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match placeholder="Select Type">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-null-choice></ui-select-null-choice>
                            <ui-select-choices
                                    repeat="itemType in newBomVm.itemTypes | filter: $select.search">
                                <div>{{itemType.typeCode}} - {{itemType.name}}</div>
                            </ui-select-choices>
                        </ui-select>
                    </div>

                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Nomenclature</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomVm.newBomItem.newItem.itemName">
                    </div>
                </div>

                <div class="form-group" ng-if="newBomVm.newBomItem.newItem.itemType != null">
                    <label class="col-sm-4 control-label">
                        <span>Item Code</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomVm.newBomItem.newItem.itemCode"
                               maxlength="1" style="text-transform: uppercase;" readonly/>
                    </div>
                </div>

                <div class="form-group" ng-if="newBomVm.newBomItem.newItem.itemType.hasSpec">
                    <label class="col-sm-4 control-label">
                        Specifications <span class="asterisk">*</span>:
                    </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="newBomVm.newBomItem.newItem.partSpec" theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match placeholder="{{newBomVm.specificationTitle}}">{{$select.selected.specName}}
                            </ui-select-match>
                            <ui-select-null-choice></ui-select-null-choice>
                            <ui-select-choices
                                    repeat="spec in newBomVm.itemTypeSpecs | filter: $select.search">
                                <div ng-bind="spec.specName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Description</span>: </label>

                    <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newBomVm.newBomItem.newItem.description"></textarea>
                    </div>
                </div>

                <div class="form-group"
                     ng-if="newBomVm.newBomItem.newItem.itemType != null && newBomVm.typeParentNode.name == 'Part'">
                    <label class="col-sm-4 control-label">
                        <span>Quantity</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input ng-if="!newBomVm.newBomItem.newItem.itemType.hasLots" type="number" class="form-control"
                               name="title" ng-model="newBomVm.newBomItem.quantity">
                        <input ng-if="newBomVm.newBomItem.newItem.itemType.hasLots" type="number" class="form-control"
                               name="title" ng-model="newBomVm.newBomItem.fractionalQuantity">
                    </div>
                </div>

                <div class="form-group"
                     ng-if="newBomVm.newBomItem.newItem.itemType != null && newBomVm.typeParentNode.name == 'Part'">
                    <label class="col-sm-4 control-label">
                        <span>Work Center</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="newBomVm.newBomItem.workCenter" theme="bootstrap">
                            <ui-select-match placeholder="Select Work Center">
                                {{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="center in newBomVm.workCenters track by $index">
                                <div ng-bind="center"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group" ng-if="newBomVm.newBomItem.bomItemType == 'PART'">
                    <label class="col-sm-4 control-label">
                        <span>Units</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomVm.newBomItem.newItem.itemType.units" readonly>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Material </span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomVm.newBomItem.newItem.material">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Drawing Number</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newBomVm.newBomItem.newItem.drawingNumber">
                    </div>
                </div>


                <attributes-view show-objects="selectObjectValues"
                                 attributes="newBomVm.newItemAttributes"></attributes-view>
                <br>
                <attributes-view show-objects="selectObjectValues"
                                 attributes="newBomVm.newItemRevisionAttributes"></attributes-view>
            </form>

            <h4 ng-if="newBomVm.requiredAttributes.length > 0 || newBomVm.attributes.length > 0"
                class="section-title" style="color: black;">Attributes
            </h4>
            <br>

            <div>
                <form class="form-horizontal" ng-if="newBomVm.newBomItem.newItem.itemType != null">
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newBomVm.requiredAttributes"></attributes-view>
                    <br>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newBomVm.attributes"></attributes-view>
                </form>
            </div>
        </div>

    </div>
</div>