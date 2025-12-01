<div style="position: relative;">
    <div style="padding: 20px">
        <div class="row" style="margin: 0;">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>WORKFLOW</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="changeWorkflowVm.changeWf.workflowDefinition"
                                   on-select="changeWorkflowVm.onSelectWorkflow()" theme="bootstrap"
                                   title="{{changeWorkflowVm.changeWf.workflowDefinition.name}} [ Revision : {{changeWorkflowVm.changeWf.workflowDefinition.revision}} ]"
                                   style="width:100%">
                            <ui-select-match placeholder={{changeWorkflowVm.selectWorkflow}}>{{$select.selected.name}}
                                [ Revision : {{$select.selected.revision}} ]
                            </ui-select-match>
                            <ui-select-choices repeat="workflow in changeWorkflowVm.wfs | filter: $select.search">
                                <div title="{{workflow.name}} [ Revision : {{workflow.revision}} ]">
                                    {{workflow.name}} [ Revision : {{workflow.revision}} ]
                                </div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <div class="form-group"
                     ng-if="changeWorkflowVm.changeWf.workflowDefinition != null && (changeWorkflowVm.type == 'ECOS' || changeWorkflowVm.type == 'DCOS')">
                    <label class="col-sm-4 control-label">
                        <span translate>REVISION_CREATION_RULE</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7" style="margin: 12px 0 0 0 !important;">

                        <div class="form-check"
                             style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                            <label class="form-check-label" style="margin-right: 5px">
                                <input class="form-check-input" type="radio" name="revisionCreationRule"
                                       id="ecoWorkflowStart"
                                       ng-click="changeWorkflowVm.selectRevisionCreationRule('workflowStart', $event)">
                                    <span style="padding: 2px;margin-left: 5px;"
                                          translate>WORKFLOW_START</span>
                            </label>
                            <label class="form-check-label" style="margin-right: 5px">
                                <input class="form-check-input" type="radio" name="revisionCreationRule"
                                       id="ecoWorkflowActivity"
                                       ng-click="changeWorkflowVm.selectRevisionCreationRule('activityCompletion', $event)"><span
                                    style="padding: 2px;margin-left: 5px;"
                                    translate>WORKFLOW_ACTIVITY_COMPLETION</span>
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-group" ng-if="changeWorkflowVm.changeWf.revisionCreationType == 'ACTIVITY_COMPLETION'">
                    <label class="col-sm-4 control-label">
                        <span translate>WORKFLOW_ACTIVITY</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="changeWorkflowVm.changeWf.workflowStatus" theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match placeholder={{selectWorkflowActivityTitle}}>
                                {{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="workflowStatus.id as workflowStatus in changeWorkflowVm.workflowStatuses | filter: $select.search">
                                <div ng-bind="workflowStatus.name"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
