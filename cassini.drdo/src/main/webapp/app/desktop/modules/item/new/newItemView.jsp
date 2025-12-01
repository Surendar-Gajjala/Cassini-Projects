<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="color: black;">Basic Info</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Type</span>
                            <span class="asterisk">*</span> : </label>

                        <%--<div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                            ng-click="newItemVm.showDropMenu()">
                                        <span>Select</span> <span class="caret"
                                                                  style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu" id="typeTree">
                                        <input type="search" class="form-control input-sm search-form"
                                               placeholder="Search Type"
                                               ng-model="newItemVm.searchValue" ng-change="newItemVm.searchTree()"
                                               uib-dropdown-toggle>
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <classification-tree system="true"
                                                                 on-select-type="newItemVm.onSelectType"></classification-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newItemVm.newItem.itemType.name" readonly>


                            </div>
                        </div>--%>

                        <div class="col-sm-7">
                            <ui-select ng-model="newItemVm.newItem.itemType"
                                       on-select="newItemVm.onSelectType($item)" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Type">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="itemType in newItemVm.itemTypes | filter: $select.search">
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
                            <input type="text" class="form-control" name="title" ng-model="newItemVm.newItem.itemName">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newItemVm.newItem.itemType != null">
                        <label class="col-sm-4 control-label">
                            <span>Item Code</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" ng-model="newItemVm.newItem.itemCode"
                                   maxlength="1" style="text-transform: uppercase;" readonly/>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newItemVm.newItem.itemType.hasSpec">
                        <label class="col-sm-4 control-label">
                            Specifications <span class="asterisk">*</span>:
                        </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newItemVm.newItem.partSpec" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{newItemVm.specificationTitle}}">
                                    {{$select.selected.specName}}
                                </ui-select-match>
                                <ui-select-null-choice></ui-select-null-choice>
                                <ui-select-choices
                                        repeat="spec in newItemVm.itemTypeSpecs | filter: $select.search">
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
                                      ng-model="newItemVm.newItem.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Units</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newItemVm.newItem.itemType.units" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Material </span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newItemVm.newItem.material">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Drawing Number</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newItemVm.newItem.drawingNumber">
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newItemVm.newItemAttributes"></attributes-view>
                    <br>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newItemVm.newItemRevisionAttributes"></attributes-view>

                </form>
                <h4 ng-if="newItemVm.requiredAttributes.length > 0 || newItemVm.attributes.length > 0"
                    class="section-title" style="color: black;">Attributes
                </h4>
                <br>

                <div>
                    <form class="form-horizontal" ng-if="newItemVm.newItem.itemType != null">
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newItemVm.requiredAttributes"></attributes-view>
                        <br>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newItemVm.attributes"></attributes-view>
                    </form>
                </div>
                <br><br><br>
                <br><br><br>
            </div>
        </div>
    </div>
</div>
