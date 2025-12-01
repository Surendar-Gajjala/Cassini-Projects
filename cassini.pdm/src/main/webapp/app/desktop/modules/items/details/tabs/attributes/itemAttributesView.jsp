<div class="item-details" style="padding: 30px">
    <div>
        <h4 style="color: black;">Item Attributes :</h4>
    </div>
    <div ng-if="itemAttributesVm.attributes.length == 0">
        No Attributes
    </div>
    <div class="row" ng-if="itemAttributesVm.attributes.length != 0"
         ng-repeat="attribute in itemAttributesVm.attributes">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>{{attribute.attributeDef.name}}: </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-if="attribute.attributeDef.dataType == 'STRING'">
                <a href="#"
                   editable-text="attribute.value.stringValue"
                   onaftersave="itemAttributesVm.saveItemAttribute(attribute)">
                    {{attribute.value.stringValue || 'Click to set value'}}
                </a>
            </span>
            <span ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                <a href="#"
                   editable-number="attribute.value.integerValue" e-step="any"
                   onaftersave="itemAttributesVm.saveItemAttribute(attribute)">
                    {{attribute.value.integerValue || 'Click to set value'}}
                </a>
            </span>
            <span ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                <a href="#"
                   editable-number="attribute.value.doubleValue" e-step="any"
                   onaftersave="itemAttributesVm.saveItemAttribute(attribute)">
                    {{attribute.value.doubleValue || 'Click to set value'}}
                </a>
            </span>
            <span ng-if="attribute.attributeDef.dataType == 'DATE'">
                <a href=""
                   editable-bsdate="attribute.value.dateValue"
                   e-is-open="opened.$data"
                   ng-model="attribute.value.dateValue"
                   e-ng-click="open($event,'$data')"
                   data-format="DD/MM/YYYY"
                   onaftersave="itemAttributesVm.saveItemAttribute(attribute)">
                    {{ (attribute.value.dateValue | date:"dd/MM/yyyy") || 'Click to set value' }}
                </a>
            </span>
            <span ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                <a href="" e-style="width: 100%"
                   editable-select="attribute.value.booleanValue"
                   e-ng-options="flag for flag in itemAttributesVm.flags"
                   onaftersave="itemAttributesVm.saveItemAttribute(attribute)">
                    {{attribute.value.booleanValue || 'Click to set value'}}
                </a>
            </span>
        </div>
        <div ng-if="attribute.attributeDef.dataType == 'LIST'">
            <ul>
                <ol ng-repeat="value in attribute.value.listValue">{{value}}</ol>
            </ul>
            <div ng-if="attribute.listValueEditMode">
                <button ng-click="itemAttributesVm.saveItemAttribute(attribute)">save</i>
                </button>
                <div class="input-group input-group-sm mb15">
                    <input class="form-control ng-pristine ng-valid ng-touched" name="title"
                           ng-model="attribute.newListValue" style="" type="text">

                    <div class="input-group-btn" style="width: 100px;">
                        <button class="btn btn-default" type="button" style="width: 85px"
                                ng-click="itemAttributesVm.addToListValue(attribute)"><i class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-default" type="button" style="width: 85px"
                                ng-click="itemAttributesVm.cancelToListValue(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
            </div>
            <a href="" ng-click="attribute.listValueEditMode = true">Click to add</a>
        </div>
    </div>
</div>
</div>