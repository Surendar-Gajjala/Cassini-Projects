<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px;">
        <style scoped>
            .predicates-container {
                display: flex;
                flex-direction: column;
            }

            .predicate {
                display: flex;
                margin-top: 10px;
            }

            .flex-item {
                flex: 1;
            }
        </style>
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <div ng-if="newCriteriaVm.permission.objectType != null" class="row">
                    <div style="line-height: 40px;font-size: 30px;cursor: pointer"
                         ng-if="newCriteriaVm.mode == 'edit' || newCriteriaVm.mode == 'add'">
                        <i class="la la-plus" style="color: #1CAF9A" ng-click='newCriteriaVm.onAddItem()'></i>
                    </div>

                    <div class="predicates-container">
                        <div class="predicate" ng-if="newCriteriaVm.predicates.length > 0"
                             ng-repeat="criteria in newCriteriaVm.predicates track by $index">
                            <div class="flex-item" ng-if="criteria.newRow.length > 0"
                                 style="margin-bottom: 10px;margin-right: 10px;"
                                 ng-repeat="item in criteria.newRow track by $index">
                                <div style="margin-bottom: 10px;">
                                    <ui-select ng-model="item.type" ng-disabled="newCriteriaVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="type in newCriteriaVm.types | filter: $select.search">
                                            <div ng-bind="type"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                                <div ng-if="item.type == 'Properties' && item.properties.length > 0" style="margin-bottom: 10px;">
                                    <ui-select ng-model="item.property" ng-disabled="newCriteriaVm.mode == 'view'"
                                               on-select="newCriteriaVm.checkProperty(criteria, item.property, $index)">
                                        <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="prop in item.properties | filter: $select.search">
                                            <div ng-bind="prop.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                                <div ng-if="item.type == 'Attributes' && item.attributes.length > 0" style="margin-bottom: 10px;">
                                    <ui-select ng-model="item.attribute" ng-disabled="newCriteriaVm.mode == 'view'"
                                               on-select="newCriteriaVm.checkAttribute(criteria, item.attribute, $index)">
                                        <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="attr in item.attributes | filter: $select.search">
                                            <div ng-bind="attr.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="flex-item" style="margin-bottom: 10px;margin-right: 10px;">
                                <ui-select ng-model="criteria.operator"
                                           ng-disabled="newCriteriaVm.mode == 'view'">
                                    <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices repeat="opr in newCriteriaVm.operators | filter: $select.search">
                                        <div ng-bind="opr.name"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>

                            <div ng-if="criteria.operator.name != 'isEmpty' && criteria.operator.name != 'isNotEmpty'"
                                 class="flex-item" style="margin-bottom: 10px;margin-right: 10px;">
                               <span ng-if="(newCriteriaVm.dataType=='string' || criteria.selectedDataType=='string') && criteria.isEnum == false && criteria.isPerson == false">
                                   <input ng-model="criteria.rhs" type="text" class="form-control"
                                          placeholder="{{newCriteriaVm.enterValueTitle}}"
                                          ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='integer' || criteria.selectedDataType=='integer') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input type="text" placeholder="{{newCriteriaVm.enterValueTitle}}" class="form-control"
                                           name="int"
                                           ng-model="criteria.rhs" ng-disabled="newCriteriaVm.mode == 'view'" numbers-only><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='date' || criteria.selectedDataType=='date') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input type="text" placeholder="{{newCriteriaVm.enterValueTitle}}" class="form-control"
                                           name="date"
                                           ng-model="criteria.rhs" ng-disabled="newCriteriaVm.mode == 'view'" date-picker-edit><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='string[]' || criteria.selectedDataType=='string[]') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='object' || criteria.selectedDataType=='object') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='boolean' || criteria.selectedDataType=='boolean') && criteria.isEnum == false && criteria.isPerson == false">
                                    <ui-select ng-model="criteria.rhs" ng-disabled="newCriteriaVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="val in newCriteriaVm.values | filter: $select.search">
                                            <div ng-bind="val"></div>
                                        </ui-select-choices>
                                    </ui-select><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='byte[]'  || criteria.selectedDataType=='byte[]') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='integer[]' || criteria.selectedDataType=='integer[]') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='double' || criteria.selectedDataType=='double') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control" valid-number
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='long' || criteria.selectedDataType=='long') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}" valid-number
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='float' || criteria.selectedDataType=='float') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}" valid-number
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='byte' || criteria.selectedDataType=='byte') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='text' || criteria.selectedDataType=='text') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='longtext' || criteria.selectedDataType=='longtext') && criteria.isEnum == false && criteria.isPerson == false">
                                    <textarea ng-model="criteria.rhs" type="text" class="form-control"
                                              placeholder="{{newCriteriaVm.enterValueTitle}}"
                                              ng-disabled="newCriteriaVm.mode == 'view'"></textarea><br>
                               </span>

                                <span ng-if="(newCriteriaVm.dataType=='richtext' || criteria.selectedDataType=='richtext') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='list' || criteria.selectedDataType=='list') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='time' || criteria.selectedDataType=='time') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}" time-picker
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(newCriteriaVm.dataType=='timestamp' || criteria.selectedDataType=='timestamp') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}" uib-timepicker
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>

                                <span ng-if="(newCriteriaVm.dataType=='currency' || newCriteriaVm.dataType=='image' || newCriteriaVm.dataType=='attachment' || newCriteriaVm.dataType=='hyperlink' || newCriteriaVm.dataType=='formula') ||
                                 (criteria.selectedDataType=='currency' || criteria.selectedDataType=='image' || criteria.selectedDataType=='attachment' || criteria.selectedDataType=='hyperlink' || criteria.selectedDataType=='formula')
                                 && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newCriteriaVm.enterValueTitle}}"
                                           ng-disabled="newCriteriaVm.mode == 'view'"><br>
                               </span>
                                <span>
                                    <ui-select ng-if="criteria.isEnum == true" ng-model="criteria.rhs" ng-disabled="newCriteriaVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="enum in criteria.enums | filter: $select.search">
                                            <div ng-bind="enum"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </span>
                                 <span>
                                    <ui-select ng-if="criteria.isPerson == true" ng-model="criteria.person" ng-disabled="newCriteriaVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="person in criteria.persons | filter: $select.search">
                                            <div ng-bind-html="person.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </span>
                            </div>
                            <div ng-if="newCriteriaVm.mode == 'add' ||newCriteriaVm.mode == 'edit'" class="flex-item"
                                 style="line-height: 30px;font-size: 20px;cursor: pointer;max-width: 25px;text-align: center">
                                <i class="la la-times" style="color: #d9534f" ng-click='newCriteriaVm.deleteItem($index)'></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>