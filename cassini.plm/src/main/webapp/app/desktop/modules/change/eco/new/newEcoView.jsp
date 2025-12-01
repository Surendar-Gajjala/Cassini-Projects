<div style="position: relative;">
    <style scoped>
        .open > .dropdown-toggle.btn {
            color: #091007 !important;
        }

        form .form-group:last-child {
            margin-bottom: 100px;
        }
    </style>
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
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
                                        <span translate id="select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <%--  <change-tree
                                                      on-select-type="newEcoVm.onSelectType"></change-tree>--%>
                                            <change-type-tree id="changeType"
                                                              on-select-type="newEcoVm.onSelectType"
                                                              change-type="ECO"></change-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newEcoVm.changeType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>ECO_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newEcoVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newEcoVm.newECO.ecoNumber">
                            </div>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>OWNER</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcoVm.newECO.ecoOwnerObject" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newEcoVm.changeAnalysts | filter: $select.search | orderBy:'firstName'">
                                    <div ng-bind-html="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_ECO_TITLE' | translate}}"
                                   name="title" ng-model="newEcoVm.newECO.title">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newEcoVm.newECO.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REASON_FOR_CHANGE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterReasonForChange}}"
                                      ng-model="newEcoVm.newECO.reasonForChange"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcoVm.newECO.workflow" on-select="newEcoVm.onSelectWorkflow($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{newEcoVm.selectWorkflow}}>
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices style="height: 120px"
                                        repeat="workflow.id as workflow in newEcoVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="newEcoVm.newECO.workflow != null && newEcoVm.workflowStatuses.length >0">
                        <label class="col-sm-4 control-label">
                            <span translate>REVISION_CREATION_RULE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin: 12px 0 0 0 !important;">

                            <div class="form-check"
                                 style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="revisionCreationRules"
                                           id="workflowStart"
                                           ng-click="newEcoVm.selectRevisionCreationRule('workflowStart', $event)"
                                           checked>
                                    <span style="padding: 2px;margin-left: 5px;"
                                          translate>WORKFLOW_START</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="revisionCreationRules"
                                           id="workflowActivity"
                                           ng-click="newEcoVm.selectRevisionCreationRule('activityCompletion', $event)"><span
                                        style="padding: 2px;margin-left: 5px;"
                                        translate>WORKFLOW_ACTIVITY_COMPLETION</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="newEcoVm.newECO.revisionCreationType == 'ACTIVITY_COMPLETION' && newEcoVm.workflowStatuses.length >0">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW_ACTIVITY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcoVm.newECO.workflowStatus" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowActivityTitle}}>
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflowStatus.id as workflowStatus in newEcoVm.workflowStatuses | filter: $select.search">
                                    <div ng-bind="workflowStatus.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newEcoVm.ecoRequiredProperties.length > 0"
                                     attributes="newEcoVm.ecoRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newEcoVm.ecoProperties.length > 0"
                                     attributes="newEcoVm.ecoProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newEcoVm.attributes.length > 0"
                                     attributes="newEcoVm.attributes"></attributes-view>

                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>
</div>