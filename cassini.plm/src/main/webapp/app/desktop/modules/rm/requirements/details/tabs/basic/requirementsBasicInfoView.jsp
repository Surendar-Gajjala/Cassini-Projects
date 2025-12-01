<style scoped>

</style>

<div>
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedRequirement.type.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedRequirement.objectNumber}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NAME</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" e-style="width:250px"
                           ng-if="hasPermission('admin','all') || specPermission.editPermission == true || hasPermission('pgcspecification','edit')"
                           ng-hide="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || selectedSpecification.lifecyclePhase.phaseType == 'REVIEW'"
                           onaftersave="reqBasicVm.updateRequirement()"
                           editable-text="selectedRequirement.name">
                            <span style="white-space: normal;word-wrap: break-word;"
                                  ng-bind-html="selectedRequirement.name"></span>
                        </a>

                        <span style="white-space: normal;word-wrap: break-word;"
                              ng-if="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || selectedSpecification.lifecyclePhase.phaseType == 'REVIEW'"
                              ng-bind-html="selectedRequirement.name">

                        </span>
                        <span ng-if="!hasPermission('admin','all') && specPermission.editPermission != true && !hasPermission('pgcspecification','edit')"
                              style="white-space: normal;word-wrap: break-word;"
                              ng-bind-html="selectedRequirement.name"></span>

                    </div>

                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DESCRIPTION</span> :
                    </div>


                    <div class="value col-xs-8 col-sm-9">

                        <a href="" e-style="width:250px"
                           ng-if="hasPermission('admin','all') || specPermission.editPermission == true || hasPermission('pgcspecification','edit')"
                           ng-hide="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || selectedSpecification.lifecyclePhase.phaseType == 'REVIEW' || selectedRequirement.editMode == true"
                           ng-click="reqBasicVm.editDescription(selectedRequirement)">
                            <span style="white-space: normal;word-wrap: break-word;"
                                  ng-bind-html="selectedRequirement.description | highlightText: reqBasicVm.searchText"></span>
                        </a>

                        <a ng-if="selectedRequirement.description == null || selectedRequirement.description == ''"
                           href="" ng-click="reqBasicVm.editDescription(selectedRequirement)">
                        <span
                                ng-bind-html="selectedRequirement.description || 'CLICK_TO_ADD_DESCRIPTION' |
                            translate"></span>
                        </a>

                         <span style="white-space: normal;word-wrap: break-word;"
                               ng-if="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || selectedSpecification.lifecyclePhase.phaseType == 'REVIEW'"
                               ng-bind-html="selectedRequirement.description | highlightText: reqBasicVm.searchText">

                        </span>
                        <span ng-if="!hasPermission('admin','all') && specPermission.editPermission != true && !hasPermission('pgcspecification','edit')"
                              style="white-space: normal;word-wrap: break-word;"
                              ng-bind-html="selectedRequirement.description | highlightText: reqBasicVm.searchText"></span>


                        <div ng-show="selectedRequirement.editMode == true">
                            <summernote ng-model="selectedRequirement.description"></summernote>
                        </div>

                        <button ng-show="selectedRequirement.editMode == true"
                                class="btn btn-sm btn-primary"
                                ng-click="reqBasicVm.updateRequirement()"><i class="fa fa-check"></i>
                        </button>

                        <button ng-show="selectedRequirement.editMode == true"
                                class="btn btn-sm btn-default"
                                ng-click="reqBasicVm.cancelEditDescription(selectedRequirement)"><i
                                class="fa fa-times"></i>
                        </button>


                    </div>
                </div>


                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ASSIGNED_TO</span> :
                    </div>

                    <div class="value col-xs-8 col-sm-9">
                        <a href="" e-style="width:250px" ng-show="!reqBasicVm.editAssignedTo"
                           ng-if="hasPermission('admin','all') || specPermission.editPermission == true || hasPermission('pgcspecification','edit')"
                           ng-hide="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                           ng-click="reqBasicVm.changeAssignedTo()">
                            <span ng-bind-html="selectedRequirement.assignedTo.fullName || 'CLICK_TO_ADD_PERSON' |
                            translate"></span>
                        </a>
                          <span ng-if="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                                ng-bind-html="selectedRequirement.assignedTo.fullName">
                          </span>

                        <div style="display: flex;" ng-if="reqBasicVm.editAssignedTo">
                            <ui-select ng-model="selectedRequirement.assignedTo" theme="bootstrap"
                                       style="width:200px" ng-if="reqBasicVm.editAssignedTo">
                                <ui-select-match placeholder=Select>{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="assignedTo in reqBasicVm.persons | filter: $select.search">
                                    <div ng-bind="assignedTo.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                            <button class="btn btn-xs btn-primary" title="{{SAVE | translate}}"
                                    ng-click="reqBasicVm.updateRequirement()">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-xs btn-default" title="{{CANCEL | translate}}"
                                    ng-click="reqBasicVm.cancelAssignedTo()">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                        <a href=""
                           ng-if="selectedRequirement.status != 'FINISHED' && ((specPermission != null && specPermission.editPermission == true) || hasPermission('pgcspecification','edit'))"
                           ng-hide="reqBasicVm.editAssignedTo || selectedRequirement.status == 'FINISHED' || selectedRequirement.assignedTo.fullName == null || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                           ng-click="reqBasicVm.deleteAssignedTo()">
                            <i class="fa fa-times"
                               style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                        </a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>STATUS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span><task-status task="selectedRequirement"></task-status></span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>VERSION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-if="selectedRequirement.version == 0">-</span>
                        <span ng-if="selectedRequirement.version > 0">{{selectedRequirement.version}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>PLANNED_FINISH_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" e-style="width:250px"
                           ng-class="{disabled:selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'}"
                           ng-if="reqBasicVm.editPlannedDate == false  && (hasPermission('admin','all') || specPermission.editPermission == true || hasPermission('pgcspecification','edit'))"
                           ng-hide="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || reqBasicVm.editPlannedDate == true"

                           ng-click="reqBasicVm.changeFinishDate()">
                            <span ng-if="currentLang == 'en'">{{selectedRequirement.plannedFinishDate || 'SET_PLANNED_FINISH_DATE' | translate}}</span>
                            <span ng-if="currentLang == 'de'">{{selectedRequirement.plannedFinishDatede || 'SET_PLANNED_FINISH_DATE' | translate}}</span>

                        </a>
                        <span ng-if="selectedRequirement.status == 'FINISHED' || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                              style="white-space: normal;word-wrap: break-word;"
                              ng-bind-html="selectedRequirement.plannedFinishDate"></span>

                        <span href="" ng-class="{disabled:selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'}"
                              ng-if="reqBasicVm.editPlannedDate == false && selectedRequirement.status != 'FINISHED' && !hasPermission('pgcspecification','edit') && (specPermission == null || specPermission.editPermission == false)"
                              ng-hide="selectedRequirement.status == 'FINISHED'">
                            <span>{{selectedRequirement.plannedFinishDate}}</span>
                        </span>


                        <a href=""
                        <%--ng-class="{disabled:selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || selectedRequirement.status == 'FINISHED'}"--%>
                           ng-if="reqBasicVm.editPlannedDate == false"
                           ng-hide="selectedRequirement.plannedFinishDate == null || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || selectedRequirement.status == 'FINISHED'"
                           ng-click="reqBasicVm.deletePlannedFinishDate()">
                            <i class="fa fa-times"
                               style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                        </a>

                        <div ng-if="reqBasicVm.editPlannedDate == true" class="btn-group" style="width: 300px;">
                            <div class="input-group" style="width: 200px;">
                                <input type="text" class="form-control" date-picker-edit
                                       ng-model="selectedRequirement.plannedFinishDate"
                                       name="attDate" placeholder="dd/mm/yyyy">
                                    <span class="input-group-addon">
                                        <i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                            <div class="btn-group" style="margin-left: 202px;margin-top: -38px;">
                                <button class="btn btn-sm btn-primary"
                                        type="button" title="Save"
                                        ng-click="reqBasicVm.updateRequirement()"><i
                                        class="fa fa-check"></i>
                                </button>

                                <button class="btn btn-sm btn-default"
                                        type="button" title="Cancel"
                                        ng-click="reqBasicVm.cancelFinishDate()"><i class="fa fa-times"></i>
                                </button>
                            </div>
                        </div>
                        <span>
                             <span ng-if="selectedRequirement.status == 'FINISHED'">{{selectedRequirement.plannedFinishDate}}</span>
                        </span>

                    </div>
                </div>
                <div class="row" ng-if="selectedRequirement.status == 'FINISHED'">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>Actual Finish Date</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedRequirement.actualFinishDate}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-bind-html="selectedRequirement.createdByObject.fullName"></span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedRequirement.createdDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedRequirement.modifiedByObject.fullName}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{selectedRequirement.modifiedDate}}</span>
                    </div>
                </div>
                <basic-attribute-details-view object-type="REQUIREMENT"
                                              quality-type="REQUIREMENT"
                                              has-permission="hasPermission('pgcspecification','edit')"
                                              object-id="selectedRequirement.id"></basic-attribute-details-view>
            </div>
        </div>
    </div>
</div>