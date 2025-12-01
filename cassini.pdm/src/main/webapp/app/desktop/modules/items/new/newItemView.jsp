<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title">Basic Info</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Item Type <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        Select <span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <classification-tree
                                                    on-select-type="newItemVm.onSelectType"></classification-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newItemVm.newItem.itemType.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-3 control-label">Item Number <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newItemVm.autoNumber()">Auto
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newItemVm.newItem.itemNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Description <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newItemVm.newItem.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Units <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" ng-model="newItemVm.newItem.units">
                        </div>
                    </div>
                </form>

                <br>
                <h4 class="section-title">Attributes</h4>
                <br>

                <div>
                    <form class="form-horizontal" ng-if="newItemVm.newItem.itemType != null">
                        <div class="form-group" ng-repeat="attribute in newItemVm.requiredAttributes">
                            <label class="col-sm-3 control-label">{{attribute.attributeDef.name}}
                                <span class="asterisk"
                                      ng-if="attribute.attributeDef.required == true">*</span> :
                            </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'STRING'"
                                       ng-model="attribute.stringValue">
                                <input type="number" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                                       ng-model="attribute.integerValue">
                                <input type="number" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                                       ng-model="attribute.doubleValue">
                                <input type="checkbox" class="form-control" name="title"
                                       style="width: 25px;margin-top: 12px;"
                                       ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                                       ng-model="attribute.booleanValue">
                                <input type="text" class="form-control" name="title" date-picker
                                       ng-if="attribute.attributeDef.dataType == 'DATE'"
                                       ng-model="attribute.dateValue">

                                <div ng-if="attribute.attributeDef.dataType == 'LIST'">
                                    <ul>
                                        <ol ng-repeat="value in attribute.listValue">{{value}}</ol>
                                    </ul>
                                    <div ng-if="attribute.listValueEditMode">
                                        <div class="input-group input-group-sm mb15">
                                            <input class="form-control ng-pristine ng-valid ng-touched" name="title"
                                                   ng-model="attribute.newListValue" style="" type="text">

                                            <div class="input-group-btn">
                                                <button class="btn btn-default" type="button" style="width: 85px"
                                                        ng-click="newItemVm.addToListValue(attribute)"><i
                                                        class="fa fa-check"></i>
                                                </button>
                                                <button class="btn btn-default" type="button" style="width: 85px"
                                                        ng-click="newItemVm.cancelToListValue(attribute)"><i
                                                        class="fa fa-times"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="" ng-click="attribute.listValueEditMode = true">Click to add</a>
                                </div>
                                <%-- <input type="text" class="form-control" name="title"
                                        ng-if="attribute.attributeDef.dataType == 'LIST'"
                                        ng-model="attribute.listValue">--%>
                            </div>
                        </div>

                        <div class="form-group" ng-repeat="attribute in newItemVm.attributes">
                            <label class="col-sm-3 control-label">{{attribute.attributeDef.name}}
                                <span class="asterisk"
                                      ng-if="attribute.attributeDef.required == true">*</span> :
                            </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'STRING'"
                                       ng-model="attribute.stringValue">
                                <input type="number" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                                       ng-model="attribute.integerValue">
                                <input type="number" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                                       ng-model="attribute.doubleValue">
                                <input type="checkbox" class="form-control" name="title"
                                       style="width: 25px;margin-top: 12px;"
                                       ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                                       ng-model="attribute.booleanValue">
                                <input type="text" class="form-control" name="title" date-picker
                                       ng-if="attribute.attributeDef.dataType == 'DATE'"
                                       ng-model="attribute.dateValue">

                                <div ng-if="attribute.attributeDef.dataType == 'LIST'">
                                    <ul>
                                        <ol ng-repeat="value in attribute.listValue">{{value}}</ol>
                                    </ul>
                                    <div ng-if="attribute.listValueEditMode">
                                        <div class="input-group input-group-sm mb15">
                                            <input class="form-control ng-pristine ng-valid ng-touched" name="title"
                                                   ng-model="attribute.newListValue" style="" type="text">

                                            <div class="input-group-btn">
                                                <button class="btn btn-default" type="button" style="width: 85px"
                                                        ng-click="newItemVm.addToListValue(attribute)"><i
                                                        class="fa fa-check"></i>
                                                </button>
                                                <button class="btn btn-default" type="button" style="width: 85px"
                                                        ng-click="newItemVm.cancelToListValue(attribute)"><i
                                                        class="fa fa-times"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="" ng-click="attribute.listValueEditMode = true">Click to add</a>
                                </div>
                                <%-- <input type="text" class="form-control" name="title"
                                        ng-if="attribute.attributeDef.dataType == 'LIST'"
                                        ng-model="attribute.listValue">--%>
                            </div>
                        </div>
                    </form>
                </div>
                <br><br>
            </div>
        </div>
    </div>
</div>
