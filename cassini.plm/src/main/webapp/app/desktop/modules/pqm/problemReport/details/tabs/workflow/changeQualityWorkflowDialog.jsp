<div style="position: relative;">
    <div style="padding: 20px">
        <div class="row" style="margin: 0;">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>WORKFLOW</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="changeQualityWorkflowVm.changeWf.workflowDefinition"
                                   on-select="changeQualityWorkflowVm.onSelectWorkflow()" theme="bootstrap"
                                   title="{{changeQualityWorkflowVm.changeWf.workflowDefinition.name}} [ Revision : {{changeQualityWorkflowVm.changeWf.workflowDefinition.revision}} ]"
                                   style="width:100%">
                            <ui-select-match placeholder={{changeQualityWorkflowVm.selectWorkflow}}>{{$select.selected.name}}
                                [ Revision : {{$select.selected.revision}} ]
                            </ui-select-match>
                            <ui-select-choices repeat="workflow in changeQualityWorkflowVm.wfs | filter: $select.search">
                                <div title="{{workflow.name}} [ Revision : {{workflow.revision}} ]">{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
