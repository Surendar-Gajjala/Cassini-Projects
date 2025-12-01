<div style="position: relative;">
    <div style="padding: 20px">
        <div class="row" style="margin: 0;">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>WORKFLOW</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="addWorkflowVm.addWf.workflowDefinition"
                                   on-select="addWorkflowVm.onSelectWorkflow()" theme="bootstrap"
                                   title="{{addWorkflowVm.addWf.workflowDefinition.name}}"
                                   style="width:100%">
                            <ui-select-match placeholder={{addWorkflowVm.selectWorkflow}}>{{$select.selected.name}}
                                [ Revision : {{$select.selected.revision}} ]
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="workflow in addWorkflowVm.wfs | filter: {name:$select.search}">
                                <div title="{{workflow.name}} [ Revision : {{workflow.revision}} ]">
                                    {{workflow.name}} [ Revision : {{workflow.revision}} ]
                                </div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
