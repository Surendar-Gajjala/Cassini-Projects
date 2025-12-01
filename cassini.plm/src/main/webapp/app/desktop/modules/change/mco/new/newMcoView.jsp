<div style="position: relative;height: 100%;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;padding-bottom: 100px;">
            <div>
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
                                                    ng-if="mcoType == 'ITEMMCO'"
                                                    on-select-type="newMcoVm.onSelectType"
                                                    change-type="ITEMMCO"></change-type-tree>
                                            <change-type-tree
                                                    ng-if="mcoType == 'OEMPARTMCO'"
                                                    on-select-type="newMcoVm.onSelectType"
                                                    change-type="OEMPARTMCO"></change-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMcoVm.newMco.mcoType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>MCO_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newMcoVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMcoVm.newMco.mcoNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group" ng-if="allMCOType == 'OEMPARTMCO'">
                        <label class="col-sm-4 control-label"><span translate>QCR</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMcoVm.newMco.qcr" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.title}}</ui-select-match>
                                <ui-select-choices
                                        repeat="qcr.id as qcr in newMcoVm.qcrs | filter: $select.search">
                                    <div ng-bind="qcr.title"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_MCO_TITLE' | translate}}" ng-model="newMcoVm.newMco.title">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newMcoVm.newMco.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REASON_FOR_CHANGE</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterReasonForChange}}"
                                      ng-model="newMcoVm.newMco.reasonForChange"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CHANGE_ANALYST</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMcoVm.newMco.changeAnalyst" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newMcoVm.changeAnalysts | filter: $select.search | orderBy:'fullName'">
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
                            <ui-select ng-model="newMcoVm.newMco.workflow" theme="bootstrap" style="width:100%"
                                       on-select="newMcoVm.onSelectWorkflow($item)">
                                <ui-select-match placeholder="{{selectWorkflowTitle}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newMcoVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="newMcoVm.newMco.workflow != null && newMcoVm.workflowStatuses.length > 0 && mcoType == 'ITEMMCO'">
                        <label class="col-sm-4 control-label">
                            <span translate>REVISION_CREATION_RULE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin: 12px 0 0 0 !important;">

                            <div class="form-check"
                                 style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="revisionCreationRules"
                                           id="workflowStart"
                                           ng-click="newMcoVm.selectRevisionCreationRule('workflowStart', $event)"
                                           checked>
                                    <span style="padding: 2px;margin-left: 5px;"
                                          translate>WORKFLOW_START</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="revisionCreationRules"
                                           id="workflowActivity"
                                           ng-click="newMcoVm.selectRevisionCreationRule('activityCompletion', $event)"><span
                                        style="padding: 2px;margin-left: 5px;"
                                        translate>WORKFLOW_ACTIVITY_COMPLETION</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="newMcoVm.newMco.revisionCreationType == 'ACTIVITY_COMPLETION' && newMcoVm.workflowStatuses.length >0">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW_ACTIVITY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMcoVm.newMco.workflowStatus" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowActivityTitle}}>
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflowStatus.id as workflowStatus in newMcoVm.workflowStatuses | filter: $select.search">
                                    <div ng-bind="workflowStatus.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newMcoVm.mcoProperties.length > 0"
                                     attributes="newMcoVm.mcoProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newMcoVm.attributes.length > 0"
                                     attributes="newMcoVm.attributes"></attributes-view>
                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>
