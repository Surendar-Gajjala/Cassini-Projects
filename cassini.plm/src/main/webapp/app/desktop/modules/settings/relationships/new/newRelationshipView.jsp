<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }
    </style>
    <div style="overflow-x: hidden; padding: 20px;height: 476px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>BASIC_INFO</h4>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>RELATIONSHIP_NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newRelationshipVm.relationship.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="newRelationshipVm.relationship.description"></textarea>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>FIRST_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span id="selectFirstType" translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <classification-tree
                                                    on-select-type="newRelationshipVm.onSelectFromType"></classification-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newRelationshipVm.relationship.fromType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SECOND_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span id="selectSecondType" translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <relation-classification-tree
                                                    on-select-type="newRelationshipVm.onSelectToType"></relation-classification-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newRelationshipVm.relationship.toType.name" readonly>


                            </div>
                        </div>
                    </div>
                </form>

                <br>
            </div>
        </div>

        <br>
        <h4 ng-if="newRelationshipVm.showAttributes" class="section-title" translate>ATTRIBUTES</h4>
        <br>

        <div class="row">
            <div ng-if="newRelationshipVm.showAttributes">
                <div>
                    <button class="btn btn-xs btn-primary" ng-click="newRelationshipVm.addAttribute()" translate>
                        NEW_ATTRIBUTE
                    </button>
                    <%--<span class="btn btn-sm btn-default" ng-hide="newRelationshipVm.hide == false"
                          style="margin-left: 249px; color: red;width:auto;font-size: 14px;">{{newRelationshipVm.error}}
                    </span>--%>
                </div>
                <br>

                <div class="responsive-table">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 200px" translate>NAME</th>
                            <th translate>DESCRIPTION</th>
                            <th style="width: 120px" translate>DATA_TYPE</th>
                            <th style="width: auto" translate>DEFAULT_VALUE</th>
                            <th style="width: auto" translate>Multi List</th>
                            <th style="width: 100px; text-align: center" translate>IS_REQUIRED</th>
                            <th style="width: 100px; text-align: center" translate>ACTIONS</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-if="newRelationshipVm.relationship.attributes.length == 0">
                            <td colspan="7"><span translate>NO_ATTRIBUTES</span></td>
                        </tr>
                        <tr ng-repeat="attribute in newRelationshipVm.relationship.attributes">
                            <td style="width: 200px">
                                <span ng-if="attribute.showValues == true">{{attribute.name}}</span>
                                <input ng-if="attribute.editMode == true" type=" text" class="form-control input-sm"
                                       ng-model="attribute.newName">
                            </td>
                            <td>
                                <span ng-if="attribute.showValues == true">{{attribute.description}}</span>
                                <input ng-if="attribute.editMode == true" type="text" class="form-control input-sm"
                                       ng-model="attribute.newDescription">
                            </td>
                            <td style="width: 120px">
                                <span ng-if="attribute.showValues == true">
                                    <span>{{attribute.dataType}}</span>
                                    <span ng-if="attribute.dataType == 'OBJECT'" style="margin-left: 5px;">( {{attribute.refType}} )</span>
                                    <span ng-if="attribute.dataType == 'LIST'" style="margin-left: 5px;">( {{attribute.lov.name}} )</span>
                                </span>
                                <select ng-if="attribute.editMode == true" class="form-control input-sm"
                                        ng-model="attribute.newDataType"
                                        ng-options="dataType for dataType in newRelationshipVm.dataTypes">
                                </select>
                                <select ng-if="attribute.editMode == true && attribute.newDataType == 'OBJECT'"
                                        class="form-control input-sm"
                                        ng-model="attribute.newRefType"
                                        ng-options="refType for refType in newRelationshipVm.refTypes">
                                </select>
                                <select ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'"
                                        class="form-control input-sm"
                                        ng-model="attribute.newLov"
                                        ng-options="lov.name for lov in newRelationshipVm.lovs">
                                </select>
                            </td>
                            <td style="width: auto; text-align: center">
                                  <span ng-if="attribute.editMode == true && attribute.newDataType == 'TEXT'">
                                     <input type="text" class="form-control input-sm" style="width: 140px !important;"
                                            ng-model="attribute.newDefaultTextValue"
                                            ng-disabled="attribute.editMode == false">
                                </span>
                                  <span ng-if="attribute.editMode == false && attribute.dataType == 'TEXT'">
                                      <span ng-disabled="attribute.editMode == false">{{attribute.defaultTextValue}}</span>
                                </span>

                                <span ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'">
                                      <select ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'"
                                              style="width: 140px !important;"
                                              class="form-control input-sm"
                                              ng-model="attribute.defaultListValue"
                                              ng-disabled="attribute.editMode == false"
                                              ng-options="value for value in attribute.newLov.values track by value">
                                      </select>
                                </span>
                                  <span ng-if="attribute.editMode == false && attribute.dataType == 'LIST'">
                                      <span ng-disabled="attribute.editMode == false">{{attribute.defaultListValue}}</span>
                                </span>
                            </td>


                            <td style="width: auto; text-align: center">
                                <span ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'">
                                     <input type="checkbox" class="form-control input-sm"
                                            ng-model="attribute.listMultiple" ng-disabled="attribute.editMode == false">
                                </span>
                                  <span ng-if="attribute.editMode == false && attribute.dataType == 'LIST'">
                                     <input type="checkbox" class="form-control input-sm"
                                            ng-model="attribute.listMultiple" ng-disabled="attribute.editMode == false">
                                </span>

                            </td>
                            <td style="width: 100px; text-align: center">
                                <input type="checkbox" class="form-control input-sm" ng-model="attribute.required"
                                       ng-disabled="attribute.editMode == false">
                            </td>
                            <td style="width: 100px; text-align: center">
                                <button ng-if="attribute.editMode == true" class="btn btn-xs btn-success"
                                        ng-click="newRelationshipVm.applyChanges(attribute)"><i class="fa fa-check"></i>
                                </button>
                                <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default"
                                        ng-click="newRelationshipVm.cancelChanges(attribute)"><i
                                        class="fa fa-times"></i>
                                </button>
                                <button title="Edit" ng-if="attribute.showValues == true" class="btn btn-xs btn-warning"
                                        ng-click="newRelationshipVm.editAttribute(attribute)"><i class="fa fa-edit"></i>
                                </button>
                                <button title="Delete" ng-if="attribute.showValues == true"
                                        class="btn btn-xs btn-danger"
                                        ng-click="newRelationshipVm.deleteAttribute(attribute)"><i
                                        class="fa fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
