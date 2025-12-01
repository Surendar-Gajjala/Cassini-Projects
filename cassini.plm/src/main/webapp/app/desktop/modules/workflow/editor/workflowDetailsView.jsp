<div id="workflow-details" style="overflow-y: auto;padding: 10px;">
    <%--<div style="margin-bottom: 20px;">
        <h4 translate>WORKFLOW_NEW_HEAD_DETAILS</h4>
    </div>--%>

    <div class="form-group">
        <label class="control-label" translate>WORKFLOW_TYPE</label>
        <span class="asterisk">*</span> :

        <div class="input-group mb15">
            <div class="input-group-btn" uib-dropdown>
                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                        ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released  || wfEditorVm.workflow.id != null">
                    <span translate>SELECT</span><span class="caret" style="margin-left: 4px;"></span>
                </button>
                <div class="dropdown-menu" role="menu" style="padding: 0 5px;">
                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 305px;max-height: 350px;">
                        <workflow-type-tree
                                on-select-type="wfEditorVm.onSelectType"></workflow-type-tree>
                    </div>
                </div>
            </div>
            <input type="text" class="form-control" name="title"
                   ng-model="wfEditorVm.workflow.workflowType.name" readonly>
        </div>
    </div>

    <div class="form-group" ng-if="wfEditorVm.workflow.workflowType != null && wfEditorVm.workflow.workflowType.assignable == 'REQUIREMENT DOCUMENTS'">
        <label class="control-label"   translate>Requirement Document Type</label>
        <input type="text" class="form-control" ng-model="reqTypeName" readonly>
    </div>
    <div class="form-group" ng-if="wfEditorVm.workflow.workflowType != null && wfEditorVm.workflow.workflowType.assignable == 'REQUIREMENT'">
        <label class="control-label" translate>Requirement Type</label>
        <input type="text" class="form-control" ng-model="reqTypeName" readonly>
    </div>

    <div class="form-group">

        <label class="control-label">WorkFlow Number: </label>

        <div class="input-group mb15">
            <div class="input-group-btn">
                <button class="btn btn-default" type="button"
                        style="width: 85px;"
                        ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released || wfEditorVm.workflow.id != null"
                        ng-click="wfEditorVm.autoNumber()">Auto
                </button>
            </div>
            <input type="text" class="form-control" name="title"
                   ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released || wfEditorVm.workflow.id != null"
                   ng-model="wfEditorVm.workflow.number">
        </div>

    </div>


    <div class="form-group">
        <label class="control-label" translate>WORKFLOW_ALL_NAME</label>
        <span class="asterisk">*</span> :
        <input type="text" class="form-control" ng-model="wfEditorVm.workflow.name"
               placeholder="{{'ENTER_WORKFLOW_NAME'| translate}}"
               ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released">
    </div>

    <div class="form-group">
        <label class="control-label" translate>DESCRIPTION</label> :
                        <textarea class="form-control" rows="3" style="resize: none;"
                                  ng-model="wfEditorVm.workflow.description"
                                  placeholder="{{'ENTER_DESCRIPTION_TITLE'| translate}}"
                                  ng-disabled="!hasPermission('plmworkflowdefinition','edit') || wfEditorVm.workflow.released">
                        </textarea>
    </div>
</div>