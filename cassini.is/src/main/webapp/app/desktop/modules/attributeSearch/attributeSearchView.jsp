<div style="overflow-y: auto; overflow-x: hidden; padding: 20px;" id="attribute-search">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-4 control-label">
                <span>Attributes</span>
                <span class="asterisk">*</span> : </label>

            <div class="col-sm-7">
                <ui-select ng-model="attributeSearchVm.attribute"
                           on-select="attributeSearchVm.selectAttribute($item)" theme="bootstrap"
                           style="width:100%">
                    <ui-select-match placeholder="Select Attribute">Select Attribute
                    </ui-select-match>
                    <ui-select-choices
                            repeat="attribute in attributeSearchVm.allAttributes | filter: $select.search">
                        <div>{{attribute.name}}<span ng-if="attribute.itemType != undefined"> ( {{attribute.itemTypeObject.name}} )</span>
                        </div>

                    </ui-select-choices>
                </ui-select>
            </div>
        </div>

        <hr>

        <div class="form-group" ng-repeat="attribute in attributeSearchVm.receiveAttributes">
            <label class="col-sm-4 control-label">
                <span>{{attribute.objectTypeAttribute.name}}</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-6">
                <input type="text" ng-if="attribute.objectTypeAttribute.dataType == 'TEXT'" ng-model="attribute.text"
                       class="form-control">
                <input type="number" ng-if="attribute.objectTypeAttribute.dataType == 'INTEGER'"
                       ng-model="attribute.integer" class="form-control">
                <input type="text" ng-if="attribute.objectTypeAttribute.dataType == 'DOUBLE'"
                       ng-model="attribute.aDouble" class="form-control">
                <input type="text" ng-if="attribute.objectTypeAttribute.dataType == 'DATE'"
                       ng-model="attribute.date" date-picker-edit
                       class="form-control">

                <div ng-if="attribute.objectTypeAttribute.dataType == 'LIST'"
                     style="padding-right: 0px;">
                    <select class="form-control" ng-model="attribute.list"
                            placeholder="select"
                            ng-options="value for value in attribute.objectTypeAttribute.lovValues">
                    </select>
                </div>
                <div ng-if="attribute.objectTypeAttribute.dataType == 'BOOLEAN'">
                    <select class="form-control" ng-model="attribute.aBoolean"
                            placeholder="select"
                            ng-options="value for value in ['true', 'false']">
                    </select>
                </div>
                <div ng-if="attribute.objectTypeAttribute.dataType == 'CURRENCY'">
                    <input class="form-control" name="currencyValue" type="number" placeholder="Enter currency value"
                           ng-model="attribute.currency"/>
                </div>
            </div>

            <div class="col-sm-1" style="padding: 10px;">
                <i class="fa fa-minus-circle" title="Click to remove"
                   ng-click="attributeSearchVm.removeAttribute(attribute.objectTypeAttribute)"
                   style="font-size: 20px;cursor: pointer;"></i>
            </div>
        </div>
    </form>
</div>