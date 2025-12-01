<div style="padding: 20px;">
    <label class="control-label" translate>WORKFLOW_TYPE</label> <span class="asterisk">*</span> :

    <div class="input-group mb15">
        <div class="input-group-btn" uib-dropdown>
            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                    style="">
                <span translate>SELECT</span><span class="caret" style="margin-left: 4px;"></span>
            </button>
            <div class="dropdown-menu" role="menu">
                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 305px;max-height: 350px;">
                    <workflow-type-tree
                            on-select-type="saveAsVm.onSelectType"></workflow-type-tree>
                </div>
            </div>
        </div>
        <input type="text" class="form-control" name="title"
               ng-model="saveAsVm.newWorkflow.workflowType.name" readonly>
    </div>

    <div class="form-group" ng-if="saveAsVm.newWorkflow.workflowType != null && saveAsVm.newWorkflow.workflowType.assignable == 'REQUIREMENT DOCUMENTS'">
        <label class="control-label"   translate>Requirement Document Type</label>
        <input type="text" class="form-control" ng-model="reqTypeName" readonly>
    </div>
    <div class="form-group" ng-if="saveAsVm.newWorkflow.workflowType != null && saveAsVm.newWorkflow.workflowType.assignable == 'REQUIREMENT'">
        <label class="control-label" translate>Requirement Type</label>
        <input type="text" class="form-control" ng-model="reqTypeName" readonly>
    </div>

    <div class="form-group">

        <label class="control-label">WorkFlow Number: </label>

        <div class="input-group mb15">
            <div class="input-group-btn">
                <button class="btn btn-default" type="button" style="width: 85px"
                        ng-click="wfEditorVm.autoNumber()">Auto
                </button>
            </div>
            <input type="text" class="form-control" name="title"
                   ng-model="saveAsVm.newWorkflow.number">
        </div>

    </div>

    <div class="form-group">
        <label class="control-label" translate>WORKFLOW_ALL_NAME</label> <span class="asterisk">*</span> :
        <input type="text" class="form-control"
               ng-model="saveAsVm.newWorkflow.name">
    </div>
    <div class="form-group">
        <label class="control-label" translate>DESCRIPTION</label> :
        <textarea class="form-control" rows="3" style="resize: none;"
                  ng-model="saveAsVm.newWorkflow.description"></textarea>
    </div>
</div>