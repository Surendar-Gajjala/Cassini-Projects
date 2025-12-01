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
                <h4 class="section-title" style="" translate>ITEM_ALL_PERMISSION</h4>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-6">
                            <input type="text" class="form-control"
                                   placeholder="{{'ENTER_PERMISSION_NAME' | translate}}"
                                   name="title" ng-model="newPermissionsVm.permission.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-6">
                            <textarea class="form-control" placeholder="{{'ENTER_PERMISSION_DESCRIPTION' | translate}}"
                                      rows="3" style="resize: none"
                                      ng-model="newPermissionsVm.permission.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>OBJECT_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-6">
                            <ui-select ng-model="newPermissionsVm.permission.objectType"
                                       on-select="newPermissionsVm.loadSubType(newPermissionsVm.permission.objectType)"
                                       theme="bootstrap" style="width:100%;">
                                <ui-select-match placeholder="{{select}}">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="objectType in newPermissionsVm.objectTypes | filter: $select.search">
                                    <div ng-bind="objectType"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>

                    </div>

                    <div class="form-group" ng-if="newPermissionsVm.typeValue != null">
                        <label class="col-sm-4 control-label">
                            <span translate>SUB_TYPE</span>
                        </label>

                        <div class="col-sm-6">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <classification-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'item'"
                                                    on-select-type="newPermissionsVm.onSelectType"></classification-tree>
                                            <change-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'change'"
                                                    on-select-type="newPermissionsVm.onSelectType"></change-tree>
                                            <quality-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'ncr'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    quality-type="NCRTYPE"></quality-type-tree>
                                            <quality-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'qcr'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    quality-type="QCRTYPE"></quality-type-tree>
                                            <quality-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'problemreport'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    quality-type="PROBLEMREPORTTYPE"></quality-type-tree>
                                            <quality-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'materialinspectionplan'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    quality-type="MATERIALINSPECTIONPLANTYPE"></quality-type-tree>
                                            <quality-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'productinspectionplan'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    quality-type="PRODUCTINSPECTIONPLANTYPE"></quality-type-tree>

                                            <project-management-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'requirement'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="REQUIREMENTTYPE"></project-management-type-tree>
                                            <project-management-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'requirementdocument'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="REQUIREMENTDOCUMENTTYPE"></project-management-type-tree>

                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'plant'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="PLANTTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'workcenter'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="WORKCENTERTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'machine'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="MACHINETYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'equipment'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="EQUIPMENTTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'instrument'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="INSTRUMENTTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'tool'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="TOOLTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'jigfixture'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="JIGFIXTURETYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'material'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="MATERIALTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'manpower'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="MANPOWERTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'operation'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="OPERATIONTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'productionorder'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="PRODUCTIONORDERTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'serviceorder'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="SERVICEORDERTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'mroasset'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="ASSETTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'mrometer'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="METERTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'mrosparepart'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="SPAREPARTTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'mroworkrequest'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="WORKREQUESTTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'mroworkorder'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="WORKORDERTYPE"></manufacturing-type-tree>
                                            <manufacturing-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'assemblyline'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="ASSEMBLYLINETYPE"></manufacturing-type-tree>
                                            <manufacturer-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'manufacturer'"
                                                    on-select-type="newPermissionsVm.onSelectType"></manufacturer-tree>
                                            <manufacturer-part-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'manufacturerpart'"
                                                    on-select-type="newPermissionsVm.onSelectType"></manufacturer-part-tree>
                                            <supplier-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'mfrsupplier'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="SUPPLIERTYPE"></supplier-tree>
                                            <compliance-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'pgcsubstance'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="PGCSUBSTANCETYPE"></compliance-type-tree>
                                            <compliance-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'pgcspecification'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="PGCSPECIFICATIONTYPE"></compliance-type-tree>
                                            <compliance-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'pgcdeclaration'"
                                                    on-select-type="newPermissionsVm.onSelectType"
                                                    object-type="PGCDECLARATIONTYPE"></compliance-type-tree>
                                            <custom-type-tree
                                                    ng-if="newPermissionsVm.permission.objectType == 'customobject'"
                                                    on-select-type="newPermissionsVm.onSelectType"></custom-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newPermissionsVm.permission.subType" readonly>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PRIVILEGE</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-6">
                            <ui-select ng-model="newPermissionsVm.permission.privilege"
                                       theme="bootstrap" style="width:100%;">
                                <ui-select-match placeholder="{{select}}">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="privilege in newPermissionsVm.privileges  | filter: $select.search">
                                    <div ng-bind="privilege"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newPermissionsVm.attributeGroups.length > 0">
                        <label class="col-sm-4 control-label">
                            <span translate>ATTRIBUTE_GROUP</span>
                        </label>

                        <div class="col-sm-6">
                            <ui-select ng-model="newPermissionsVm.permission.attributeGroup"
                                       on-select="newPermissionsVm.getGroupAttributes(newPermissionsVm.permission.attributeGroup)"
                                       theme="bootstrap" style="width:100%;">
                                <ui-select-match placeholder="{{select}}">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="group in newPermissionsVm.attributeGroups  | filter: $select.search">
                                    <div ng-bind="group"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newPermissionsVm.groupAttributes.length > 0">
                        <label class="col-sm-4 control-label">
                            <span translate>ATTRIBUTES</span>
                        </label>

                        <div class="col-sm-6">
                            <ui-select multiple ng-model="newPermissionsVm.attribute"
                                       theme="bootstrap" style="width:100%;text-align: left;">
                                <ui-select-match placeholder="{{select}}">{{$item}}</ui-select-match>
                                <ui-select-choices
                                        repeat="attr in newPermissionsVm.groupAttributes  | filter: $select.search">
                                    <div ng-bind="attr"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PRIVILEGE_TYPE</span>
                        </label>

                        <div class="col-sm-6">
                            <ui-select ng-model="newPermissionsVm.permission.privilegeType">
                                <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="privilegeType in ['GRANTED','DENIED'] | filter: $select.search">
                                    <div ng-bind="privilegeType"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </form>

                <%--Criteria--%>

                <div ng-if="newPermissionsVm.permission.objectType != null && newPermissionsVm.permission.objectType != 'homewidget'">
                    <br>
                    <h4 class="section-title" style="" ng-if="newPermissionsVm.permission.objectType != null" translate>
                        CRITERIA</h4>

                    <div ng-if="newPermissionsVm.permission.objectType != null" class="row">
                        <div style="line-height: 40px;font-size: 30px;cursor: pointer"
                             ng-if="newPermissionsVm.mode == 'edit' || newPermissionsVm.mode == 'add'">
                            <i class="la la-plus" style="color: #1CAF9A" ng-click='newPermissionsVm.onAddItem()'></i>
                        </div>

                        <div class="predicates-container">
                            <div class="predicate" ng-if="newPermissionsVm.predicates.length > 0"
                                 ng-repeat="criteria in newPermissionsVm.predicates track by $index">
                                <div class="flex-item" ng-if="criteria.newRow.length > 0"
                                     style="margin-bottom: 10px;margin-right: 10px;"
                                     ng-repeat="item in criteria.newRow track by $index">
                                    <div style="margin-bottom: 10px;">
                                        <ui-select ng-model="item.type" ng-disabled="newPermissionsVm.mode == 'view'">
                                            <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}">
                                                {{$select.selected}}
                                            </ui-select-match>
                                            <ui-select-choices
                                                    repeat="type in newPermissionsVm.types | filter: $select.search">
                                                <div ng-bind="type"></div>
                                            </ui-select-choices>
                                        </ui-select>
                                    </div>
                                    <div ng-if="item.type == 'Properties' && item.properties.length > 0"
                                         style="margin-bottom: 10px;">
                                        <ui-select ng-model="item.property"
                                                   ng-disabled="newPermissionsVm.mode == 'view'"
                                                   on-select="newPermissionsVm.checkProperty(criteria, item.property, $index)">
                                            <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}">
                                                {{$select.selected.name}}
                                            </ui-select-match>
                                            <ui-select-choices
                                                    repeat="prop in item.properties | filter: $select.search">
                                                <div ng-bind="prop.name"></div>
                                            </ui-select-choices>
                                        </ui-select>
                                    </div>
                                    <div ng-if="item.type == 'Attributes' && item.attributes.length > 0"
                                         style="margin-bottom: 10px;">
                                        <ui-select ng-model="item.attribute"
                                                   ng-disabled="newPermissionsVm.mode == 'view'"
                                                   on-select="newPermissionsVm.checkAttribute(criteria, item.attribute, $index)">
                                            <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}">
                                                {{$select.selected.name}}
                                            </ui-select-match>
                                            <ui-select-choices
                                                    repeat="attr in item.attributes | filter: $select.search">
                                                <div ng-bind="attr.name"></div>
                                            </ui-select-choices>
                                        </ui-select>
                                    </div>
                                </div>

                                <div class="flex-item" style="margin-bottom: 10px;margin-right: 10px;">
                                    <ui-select ng-model="criteria.operator"
                                               ng-disabled="newPermissionsVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}" style="text-transform:capitalize;">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="opr in criteria.operators | filter: $select.search">
                                            <div ng-bind="opr.name" style="text-transform:capitalize;"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>

                                <div ng-if="criteria.operator.name != 'isEmpty' && criteria.operator.name != 'isNotEmpty'"
                                     class="flex-item" style="margin-bottom: 10px;margin-right: 10px;">
                               <span ng-if="(criteria.dataType=='string' || criteria.selectedDataType=='string') && criteria.isEnum == false && criteria.isPerson == false">
                                   <input ng-model="criteria.rhs" type="text" class="form-control"
                                          placeholder="{{newPermissionsVm.enterValueTitle}}"
                                          ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='integer' || criteria.selectedDataType=='integer') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input type="text" placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           class="form-control"
                                           name="int"
                                           ng-model="criteria.rhs" ng-disabled="newPermissionsVm.mode == 'view'"
                                           numbers-only><br>
                               </span>
                                <span ng-if="(criteria.dataType=='date' || criteria.selectedDataType=='date') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input type="text" placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           class="form-control"
                                           name="date"
                                           ng-model="criteria.rhs" ng-disabled="newPermissionsVm.mode == 'view'"
                                           date-picker-edit><br>
                               </span>
                                <span ng-if="(criteria.dataType=='object' || criteria.selectedDataType=='object') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='boolean' || criteria.selectedDataType=='boolean') && criteria.isEnum == false && criteria.isPerson == false">
                                    <ui-select ng-model="criteria.rhs" ng-disabled="newPermissionsVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}">
                                            {{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="val in newPermissionsVm.values | filter: $select.search">
                                            <div ng-bind="val"></div>
                                        </ui-select-choices>
                                    </ui-select><br>
                               </span>
                                <span ng-if="(criteria.dataType=='double' || criteria.selectedDataType=='double') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control" valid-number
                                           placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='long' || criteria.selectedDataType=='long') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}" valid-number
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='float' || criteria.selectedDataType=='float') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}" valid-number
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='text' || criteria.selectedDataType=='text') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='longtext' || criteria.selectedDataType=='longtext') && criteria.isEnum == false && criteria.isPerson == false">
                                    <textarea ng-model="criteria.rhs" type="text" class="form-control"
                                              placeholder="{{newPermissionsVm.enterValueTitle}}"
                                              ng-disabled="newPermissionsVm.mode == 'view'"></textarea><br>
                               </span>

                                <span ng-if="(criteria.dataType=='richtext' || criteria.selectedDataType=='richtext') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='list' || criteria.selectedDataType=='list') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='time' || criteria.selectedDataType=='time') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}" time-picker
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span ng-if="(criteria.dataType=='timestamp' || criteria.selectedDataType=='timestamp') && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}" uib-timepicker
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>

                                <span ng-if="(criteria.dataType=='currency' || criteria.dataType=='image' || criteria.dataType=='attachment' || criteria.dataType=='hyperlink'
                                || criteria.dataType=='formula' || criteria.dataType=='byte' || criteria.dataType=='byte[]' || criteria.dataType=='string[]' || criteria.dataType=='integer[]') ||
                                 (criteria.selectedDataType=='currency' || criteria.selectedDataType=='image' || criteria.selectedDataType=='attachment' || criteria.selectedDataType=='hyperlink'
                                 || criteria.selectedDataType=='formula' || criteria.selectedDataType=='byte' || criteria.selectedDataType=='byte[]' || criteria.selectedDataType=='string[]'
                                 || criteria.selectedDataType=='integer[]')
                                 && criteria.isEnum == false && criteria.isPerson == false">
                                    <input ng-model="criteria.rhs" type="text" class="form-control"
                                           placeholder="{{newPermissionsVm.enterValueTitle}}"
                                           ng-disabled="newPermissionsVm.mode == 'view'"><br>
                               </span>
                                <span>
                                    <ui-select ng-if="criteria.isEnum == true" ng-model="criteria.rhs"
                                               ng-disabled="newPermissionsVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}">
                                            {{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="enum in criteria.enums | filter: $select.search">
                                            <div ng-bind="enum"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </span>
                                 <span>
                                    <ui-select ng-if="criteria.isPerson == true" ng-model="criteria.person"
                                               ng-disabled="newPermissionsVm.mode == 'view'">
                                        <ui-select-match placeholder="{{newPermissionsVm.selectTitle}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="person in criteria.persons | filter: $select.search">
                                            <div ng-bind-html="person.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </span>
                                </div>
                                <div ng-if="newPermissionsVm.mode == 'add' ||newPermissionsVm.mode == 'edit'"
                                     class="flex-item"
                                     style="line-height: 30px;font-size: 20px;cursor: pointer;max-width: 25px;text-align: center">
                                    <i class="la la-times" style="color: #d9534f"
                                       ng-click='newPermissionsVm.deleteItem($index)'></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>