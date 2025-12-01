<div class="col-md-12" style="height: 100%;">
    <div>
        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success min-width" ng-click="pmObjectTypeBasicVm.onSave()"
                    ng-if="checkPMPermission('edit')" translate>SAVE
            </button>
        </div>
        <div class="">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>NAME</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-model="resourceType.name"
                                       ng-disabled="mesTypeObjectsExist == true || !checkPMPermission('edit')">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  ng-disabled="!checkPMPermission('edit')"
                                  ng-model="resourceType.description"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>AUTO_NUMBER_SOURCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.autoNumberSource" theme="bootstrap"
                                           style="width:100%"
                                           title="{{resourceType.autoNumberSource.name}}"
                                           ng-disabled="mesTypeObjectsExist == true || !checkPMPermission('edit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="source in autoNumbers | filter: $select.search">
                                        <div title="{{source.name}}">{{source.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-show="selectedObjectType.objectType == 'REQUIREMENTTYPE' || selectedObjectType.objectType == 'REQUIREMENTDOCUMENTTYPE'">
                            <label class="col-sm-4 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.lifecycle" theme="bootstrap"
                                           style="width:100%"
                                           title="{{resourceType.lifecycle.name}}"
                                           ng-disabled="mesTypeObjectsExist == true ||!checkPMPermission('edit')"
                                           on-select="pmObjectTypeBasicVm.onSelectLifecycle($item)">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lifecycle in lifecycles | filter: $select.search">
                                        <div title="{{lifecycle.name}}">{{lifecycle.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group" ng-show="selectedObjectType.objectType == 'REQUIREMENTDOCUMENTTYPE'">
                            <label class="col-sm-4 control-label">
                                <span translate>REVISION_SEQUENCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.revisionSequence" theme="bootstrap"
                                           style="width:100%"
                                           title="{{resourceType.revisionSequence.name}}">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in revSequences | filter: $select.search">
                                        <div title="{{lov.name}}">{{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group" ng-show="selectedObjectType.objectType == 'REQUIREMENTTYPE'">
                            <label class="col-sm-4 control-label">
                                <span translate>Priority List</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.priorityList" theme="bootstrap"
                                           style="width:100%"
                                           title="{{resourceType.priorityList.name}}">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in listOfValues | filter: $select.search">
                                        <div title="{{lov.name}}">{{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="selectedObjectType.type != 'PROJECTPHASEELEMENT' && selectedObjectType.type != 'PROJECTMILESTONE'">
                            <label class="col-sm-4 control-label">
                                <span translate>DISPLAY_TABS</span>: </label>

                            <div ng-show="selectedObjectType.type == 'PROJECT'" class="col-sm-7"
                                 style="padding-left: 8px;">
                                <div class="tabs-checkboxes-container" style="padding: 10px;">
                                    <div ng-repeat="tab in pmObjectTypeBasicVm.tabs" style="display: flex;">
                                        <input type="checkbox" class="form-control input-sm"
                                               style="box-shadow: none !important;width: 16px;margin: 0;height: 20px"
                                               title="{{pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.type,tab) ? 'Mandatory tab':''}}"
                                               ng-disabled="pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.type,tab)"
                                               ng-model="tab.selected"/>
                                        <label style="margin: 0 0 0 5px;">{{tab.label}}</label>
                                    </div>
                                </div>
                            </div>
                            <div ng-show="selectedObjectType.type == 'PROGRAM'" class="col-sm-7"
                                 style="padding-left: 8px;">
                                <div class="tabs-checkboxes-container" style="padding: 10px;">
                                    <div ng-repeat="tab in pmObjectTypeBasicVm.programTabs" style="display: flex;">
                                        <input type="checkbox" class="form-control input-sm"
                                               style="box-shadow: none !important;width: 16px;margin: 0;height: 20px"
                                               title="{{pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.type,tab) ? 'Mandatory tab':''}}"
                                               ng-disabled="pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.type,tab)"
                                               ng-model="tab.selected"/>
                                        <label style="margin: 0 0 0 5px;">{{tab.label}}</label>
                                    </div>
                                </div>
                            </div>
                            <div ng-show="selectedObjectType.type == 'PROJECTACTIVITY' || selectedObjectType.type == 'PROJECTTASK'"
                                 class="col-sm-7" style="padding-left: 8px;">
                                <div class="tabs-checkboxes-container" style="padding: 10px;">
                                    <div ng-repeat="tab in pmObjectTypeBasicVm.activityAndTaskTabs"
                                         style="display: flex;">
                                        <input type="checkbox" class="form-control input-sm"
                                               style="box-shadow: none !important;width: 16px;margin: 0;height: 20px"
                                               title="{{pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.type,tab) ? 'Mandatory tab':''}}"
                                               ng-disabled="pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.type,tab)"
                                               ng-model="tab.selected"/>
                                        <label style="margin: 0 0 0 5px;">{{tab.label}}</label>
                                    </div>
                                </div>
                            </div>
                            <div ng-show="selectedObjectType.objectType == 'REQUIREMENTTYPE'" class="col-sm-7"
                                 style="padding-left: 8px;">
                                <div class="tabs-checkboxes-container" style="padding: 10px;">
                                    <div ng-repeat="tab in pmObjectTypeBasicVm.requirementTabs" style="display: flex;">
                                        <input type="checkbox" class="form-control input-sm"
                                               style="box-shadow: none !important;width: 16px;margin: 0;height: 20px"
                                               ng-model="tab.selected"/>
                                        <label style="margin: 0 0 0 5px;">{{tab.label}}</label>
                                    </div>
                                </div>
                            </div>
                            <div ng-show="selectedObjectType.objectType == 'REQUIREMENTDOCUMENTTYPE'" class="col-sm-7"
                                 style="padding-left: 8px;">
                                <div class="tabs-checkboxes-container" style="padding: 10px;">
                                    <div ng-repeat="tab in pmObjectTypeBasicVm.requirementDocumentTabs"
                                         style="display: flex;">
                                        <input type="checkbox" class="form-control input-sm"
                                               style="box-shadow: none !important;width: 16px;margin: 0;height: 20px"
                                               title="{{pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.objectType,tab) ? 'Mandatory tab':''}}"
                                               ng-disabled="pmObjectTypeBasicVm.checkForMandatoryTab(selectedObjectType.objectType,tab)"
                                               ng-model="tab.selected"/>
                                        <label style="margin: 0 0 0 5px;">{{tab.label}}</label>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>