<style scoped>
    .editable-buttons {
        margin-top: 3px;
    }

    .form-control {
        height: 33px !important;
    }

    .white-color {
        color: white !important;
    }
</style>
<div class="view-container wbs-view" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group" style="margin-right: 10px;">
            <button title="Add node" class="btn btn-sm btn-default" ng-click="wbsVm.addRow()"
                    ng-disabled="selectedProject.locked == true || (hasPermission('permission.wbs.create') == false)">
                <i class="fa fa-plus"></i></button>
            <button title="Delete selected node" class="btn btn-sm btn-default" ng-click="wbsVm.deleteRow()"
                    ng-disabled="selectedProject.locked == true || hasPermission('permission.wbs.delete') == false">
                <i class="fa fa-minus"></i></button>
        </div>
        <%--<button class="btn btn-sm btn-primary" ng-click="wbsVm.showWbsAttributes()">Add Attributes</button>--%>
    </div>

    <div class="progress progress-striped active" ng-if="wbsVm.loading"
         style="border-radius: 0 !important; height: 10px; margin: 0">
        <div class="progress-bar progress-bar-primary" role="progressbar" style="width: 100%">
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class='responsive-table wbs-table' style="padding: 10px;">
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th>WBS Name</th>
                    <th>Weightage</th>
                    <th style="width: 150px">Assigned To</th>
                    <th>Planned Start Date</th>
                    <th>Planned Finish Date</th>
                    <th>Actual Start Date</th>
                    <th>Actual Finish Date</th>
                    <th>%</th>
                    <th style="width: 150px;"
                        ng-repeat="attribute in wbsVm.attributesList">
                        {{attribute.attributeDef.name}}
                    </th>
                    <th>Actions</th>

                </tr>
                </thead>
                <tbody>
                <tr ng-if="wbsVm.loading == true">
                    <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading WBS...
                        </span>
                    </td>
                </tr>
                <tr ng-if="wbsVm.loading == false && wbsVm.wbsData.length == 0">
                    <td colspan="12">No WBS is available to view</td>
                </tr>

                <tr ng-repeat="wbs in wbsVm.wbsData track by $index " ng-class="{'selected': wbs.selected}"
                    ng-click="wbsVm.selectItem(wbs)">
                    <td>
                        <span class="level{{wbs.level}}">
                        <i ng-if="(wbs.hasBom == true && wbs.isNew == false) && (hasPermission('permission.wbs.create') == true)"
                           style="cursor: pointer; color: #909090"
                           ng-class="{'fa fa-plus-square': wbs.expanded == false || wbs.expanded == undefined || wbs.expanded == null, 'fa fa-minus-square': wbs.expanded == true}"
                           ng-click="wbsVm.toggleNode(wbs)"></i>

                            <a href="#" editable-text="wbs.name"
                               ng-class="{'white-color': wbs.selected}"
                               ng-if="wbs.isNew != true && selectedProject.locked == false && hasPermission('permission.wbs.edit')"
                               onaftersave="wbsVm.updateWbs(wbs)">{{wbs.name}}</a>
                            <p ng-if="selectedProject.locked == false && hasPermission('permission.wbs.edit') == false">
                                {{wbs.name}}</p>
                            <p ng-if="selectedProject.locked == true">{{wbs.name}}</p>
                                 <span ng-if="wbs.isNew == true">
                                    <div class="input-group">
                                        <input ng-model="wbs.name" style="height: 30px;" class="form-control input-sm"
                                               type="text">
                                    </div>
                                </span>

                        </span>
                    </td>
                    <td><a href="#" editable-text="wbs.weightage"
                           ng-class="{'white-color': wbs.selected}"
                           ng-if="wbs.isNew != true && selectedProject.locked == false && hasPermission('permission.wbs.edit')"
                           onaftersave="wbsVm.updateWbs(wbs)">{{wbs.weightage}}</a>

                        <p ng-if="selectedProject.locked == false && hasPermission('permission.wbs.edit') == false">
                            {{wbs.weightage}}</p>

                        <p ng-if="selectedProject.locked == true">{{wbs.weightage}}</p>
                        <span ng-if="wbs.isNew == true">
                              <div class="input-group">
                                  <input style="height: 30px;" type="text" ng-model="wbs.weightage"
                                         class="form-control input-sm">
                              </div>
                             </span>
                    </td>


                    <td style="width: 150px;">
                        <%--<select ng-if="wbs.isNew == true" class="form-control input-sm"
                                ng-model=""
                                ng-options="person.name for person in wbsVm.persons">
                        </select>--%>
                        <span ng-if="wbs.isNew == true">
                            <ui-select class="required-field" ng-model="wbs.assignedTo"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Person">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in wbsVm.persons | filter: $select.search | orderBy : 'fullName'">
                                    <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </span>
                        <span ng-if="wbs.isNew != true">
                             <a style="width: 200px;"
                                href="#" ng-class="{'white-color': wbs.selected}"
                                editable-select="wbs.assignedToObject"
                                onaftersave="wbsVm.updateWbs(wbs)"
                                e-ng-options="person as person.fullName for person in wbsVm.persons | orderBy: ['fullName'] track by person.id">
                                 {{wbs.assignedToObject.fullName || 'Click to assign person'}}
                             </a>
                        </span>
                    </td>


                    <td style=" border-bottom: 1px #eee solid;"><span
                            ng-if="wbs.isNew != true">{{wbs.plannedStartDate}}</span>
                    <span ng-if="wbs.isNew == true">
                             <div class="input-group">
                                 <input style="height: 30px;" id="plannedStartDate" type="text" class="form-control"
                                        start-finish-date-picker
                                        ng-model="wbs.plannedStartDate" placeholder="dd/mm/yyyy">
                             </div>
                        </span></td>

                    <td style="border-bottom: 1px #eee solid;"><span
                            ng-if="wbs.isNew != true">{{wbs.plannedFinishDate}}</span>
                    <span ng-if="wbs.isNew == true">
                              <div class="input-group">
                                  <input style="height: 30px;" id="plannedFinishDate" type="text" class="form-control"
                                         start-finish-date-picker
                                         ng-model="wbs.plannedFinishDate" placeholder="dd/mm/yyyy">
                              </div>
                           </span></td>
                    <td style="border-bottom: 1px #eee solid;"><span>{{wbs.actualStartDate}}</span></td>
                    <td style="border-bottom: 1px #eee solid;"><span>{{wbs.actualFinishDate}}</span></td>
                    <td style="padding-left: 72px;border-bottom: 1px #eee solid;">
                        <span>{{wbs.percentageComplete}}</span>
                    </td>
                    <td ng-repeat="attribute in wbsVm.attributesList" ng-if="wbs.editMode == false"
                        style="border-bottom: 1px #eee solid;">
                        <p ng-if="attribute.attributeDef.dataType != 'OBJECT' && attribute.attributeDef.dataType != 'ATTACHMENT'
                                        && attribute.attributeDef.dataType != 'IMAGE' && attribute.attributeDef.dataType != 'CURRENCY'"
                           ng-init="attrName = attribute.attributeDef.name">
                            {{wbs[attrName]}}
                        </p>
                    </td>
                    <td class="added-column" ng-if="wbs.editMode == true" style="border-bottom: 1px #eee solid;"
                        ng-repeat="objectAttribute in wbs.attributes">
                        <input class="form-control" ng-if='objectAttribute.attributeDef.dataType == "STRING"'
                               type="text" ng-model="objectAttribute.stringValue"/>
                        <input class="form-control" ng-if='objectAttribute.attributeDef.dataType == "INTEGER"'
                               type="number" ng-model="objectAttribute.integerValue"/>
                        <input class="form-control" ng-if='objectAttribute.attributeDef.dataType == "DOUBLE"'
                               type="number" ng-model="objectAttribute.doubleValue"/>
                        <input type="text" class="form-control" date-picker
                               ng-model="objectAttribute.dateValue"
                               ng-if='objectAttribute.attributeDef.dataType == "DATE"'
                               placeholder="dd/mm/yyyy">
                        <input class="form-control" name="file" multiple="true"
                               ng-if='objectAttribute.attributeDef.dataType == "ATTACHMENT"'
                               type="file" ng-file-model="objectAttribute.attachmentValues"/>
                        <input class="form-control" name="file" accept="image/*"
                               ng-if='objectAttribute.attributeDef.dataType == "IMAGE"'
                               type="file" ng-file-model="objectAttribute.imageValue"/>
                    </td>
                    <td class="added-column" ng-if="wbs.editMode == true" style="border-bottom: 1px #eee solid;"
                        ng-repeat="objectAttribute in wbs.requiredAttributes">
                        <input class="form-control" ng-if='objectAttribute.attributeDef.dataType == "STRING"'
                               type="text" ng-model="objectAttribute.stringValue"/>
                        <input class="form-control" ng-if='objectAttribute.attributeDef.dataType == "INTEGER"'
                               type="number" ng-model="objectAttribute.integerValue"/>
                        <input class="form-control" ng-if='objectAttribute.attributeDef.dataType == "DOUBLE"'
                               type="number" ng-model="objectAttribute.doubleValue"/>
                        <input type="text" class="form-control" date-picker
                               ng-model="objectAttribute.dateValue"
                               ng-if='objectAttribute.attributeDef.dataType == "DATE"'
                               placeholder="dd/mm/yyyy">
                        <input class="form-control" name="file" multiple="true"
                               ng-if='objectAttribute.attributeDef.dataType == "ATTACHMENT"'
                               type="file" ng-file-model="objectAttribute.attachmentValues"/>
                        <input class="form-control" name="file" accept="image/*"
                               ng-if='objectAttribute.attributeDef.dataType == "IMAGE"'
                               type="file" ng-file-model="objectAttribute.imageValue"/>
                    </td>
                    <td style="border-bottom: 1px #eee solid;">
                        <span ng-if="wbs.isNew == true">
                            <button style="margin-top: 5px;" title="Save" class="btn btn-xs btn-default"
                                    type="button" ng-click="wbsVm.createWbs(wbs)"><i class="fa fa-check"></i>
                            </button>
                        <button style="margin-top: 5px;" title="Delete" ng-click="wbsVm.cancelRow(wbs);"
                                class="btn btn-xs btn-default">
                            <i class="fa fa-times"></i>
                        </button>
                            </span>
                        <%--   </span>
                             <span ng-if="wbs.isNew != true">
                               <button style="margin-top: 5px;" title="Save" class="btn btn-xs btn-default"
                                       type="button" ng-click="wbsVm.createWbs(wbs)">Finish Wbs
                               </button>
                           </span>--%>

                               <span ng-hide="wbs.percentageComplete ==100"
                                     ng-if="wbs.isNew != true && wbs.assignedTo != null" class="row-menu" uib-dropdown
                                     dropdown-append-to-body
                                     style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="wbsVm.finishWbsItem(wbs)" ng-show="wbs.percentageComplete != 100">
                                            <a class="dropdown-item" type="button"
                                               title="Finish Wbs">
                                                <span style="padding-left: 3px; cursor: pointer">Finish</span>
                                            </a>
                                        </li>
                                    </ul>
                               </span>

                    </td>
                </tr>
                <tr style="font-weight: bold" ng-hide="wbsVm.loading == false && wbsVm.wbsData.length == 0">
                    <td>Total Weightage :</td>
                    <td> {{wbsVm.sum}}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
