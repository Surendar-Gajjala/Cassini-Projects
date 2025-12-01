<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CHANGE_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <change-type-tree
                                                    on-select-type="newDcoVm.onSelectType"
                                                    change-type="DCO"></change-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newDcoVm.type.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>DCO_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newDcoVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newDcoVm.newDCO.dcoNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" ng-model="newDcoVm.newDCO.title"
                                   placeholder="{{'ENTER_DCO_TITLE' | translate}}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newDcoVm.newDCO.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REASON_FOR_CHANGE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterReasonForChange}}"
                                      ng-model="newDcoVm.newDCO.reasonForChange"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>OWNER</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcoVm.newDCO.dcoOwnerObject" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newDcoVm.changeAnalysts | filter: $select.search | orderBy:'firstName'">
                                    <div ng-bind-html="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcoVm.newDCO.workflow"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newDcoVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="newDcoVm.newDCO.workflow != null">
                        <label class="col-sm-4 control-label">
                            <span translate>REVISION_CREATION_RULE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin: 12px 0 0 0 !important;">

                            <div class="form-check"
                                 style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="revisionCreationRules"
                                           id="workflowStart"
                                           ng-click="newDcoVm.selectRevisionCreationRule('workflowStart', $event)"
                                           checked>
                                    <span style="padding: 2px;margin-left: 5px;"
                                          translate>WORKFLOW_START</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="revisionCreationRules"
                                           id="workflowActivity"
                                           ng-click="newDcoVm.selectRevisionCreationRule('activityCompletion', $event)"><span
                                        style="padding: 2px;margin-left: 5px;"
                                        translate>WORKFLOW_ACTIVITY_COMPLETION</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newDcoVm.newDCO.revisionCreationType == 'ACTIVITY_COMPLETION'">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW_ACTIVITY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcoVm.newDCO.workflowStatus" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowActivityTitle}}>
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflowStatus.id as workflowStatus in newDcoVm.workflowStatuses | filter: $select.search">
                                    <div ng-bind="workflowStatus.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newDcoVm.dcoRequiredProperties.length > 0"
                                     attributes="newDcoVm.dcoRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newDcoVm.dcoProperties.length > 0"
                                     attributes="newDcoVm.dcoProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newDcoVm.attributes.length > 0"
                                     attributes="newDcoVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
