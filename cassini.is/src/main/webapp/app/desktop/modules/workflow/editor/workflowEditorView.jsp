<div class="view-container" fitcontent>
    <style scoped>
        .wf-props-panel {
            border-left: 1px dotted #D3D3D3;
            padding: 10px;
            /*background-color: #F9F9F9;*/
            background-color: #FFF;
        }
    </style>
    <div class="view-toolbar">
        <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="wfEditorVm.back()">Back
            </button>
            <button class="btn btn-sm btn-success" ng-if="hasPermission('permission.workflow.edit')"
                    ng-click="wfEditorVm.saveWorkflow()">Save
            </button>
            <%--  <button class="btn btn-sm btn-warning"
                      ng-if="wfEditorVm.mode == 'edit' && hasPermission('permission.workflow.edit')"
                      ng-click="wfEditorVm.showSaveAsPanel()" translate>WORKFLOW_NEW_HEAD_SAVEAS
              </button>--%>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-9" style="padding: 10px;">
                <div id="canvas" style="width: 100%">
                    <iframe id="workflowFrame"
                            src="app/assets/js/bpmn-js/index.html"
                            iframe-onload="wfEditorVm.onEditorLoad()"
                            frameborder="0" height="100%" width="100%"></iframe>
                </div>
            </div>
            <div class="col-sm-3 wf-props-panel">
                <div ng-if="wfEditorVm.workflow != null && wfEditorVm.selectedElement.type == 'bpmn:Process'">
                    <div style="margin-bottom: 20px;">
                        <h4 translate>WORKFLOW_NEW_HEAD_DETAILS</h4>
                    </div>

                    <%--      <div class="form-group">
                              <label class="control-label" translate>WORKFLOW_TYPE</label>
                              <span class="asterisk">*</span> :


                              <div class="input-group mb15">
                                  <div class="input-group-btn" uib-dropdown>
                                      <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                              ng-disabled="!hasPermission('permission.workflow.edit')">
                                          <span translate>SELECT</span><span class="caret" style="margin-left: 4px;"></span>
                                      </button>
                                      <div class="dropdown-menu" role="menu">
                                          <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 250px;height: auto;">
                                              <workflow-type-tree
                                                      on-select-type="wfEditorVm.onSelectType"></workflow-type-tree>
                                          </div>
                                      </div>
                                  </div>
                                  <input type="text" class="form-control" name="title"
                                         ng-model="wfEditorVm.workflow.workflowType.name" readonly>
                              </div>
                          </div>--%>

                    <%--<div class="form-group">

                        <label class="control-label">WorkFlow Number: </label>

                        <div class="input-group mb15">
                            <input type="text" class="form-control" name="title"
                                   ng-model="wfEditorVm.workflow.workflowNumber">

                            <div class="input-group-btn">
                                <button class="btn btn-default" type="button" style="width: 85px"
                                        ng-click="wfEditorVm.autoNumber()">Auto
                                </button>
                            </div>
                        </div>

                    </div>--%>


                    <div class="form-group">
                        <label class="control-label" translate>WORKFLOW_ALL_NAME</label>
                        <span class="asterisk">*</span> :
                        <input type="text" class="form-control" ng-model="wfEditorVm.workflow.name"
                               ng-disabled="!hasPermission('permission.workflow.edit')">
                    </div>

                    <div class="form-group">
                        <label class="control-label" translate>DESCRIPTION</label> :
                        <textarea class="form-control" rows="5" style="resize: none;"
                                  ng-model="wfEditorVm.workflow.description"
                                  ng-disabled="!hasPermission('permission.workflow.edit')">
                        </textarea>
                    </div>
                    <%-- <div class="form-group">
                         <label class="control-label" translate>WORKFLOW_NEW_ASSIGNABLE</label>
                         <span class="asterisk">*</span> :
                         <ui-select multiple ng-model="wfEditorVm.workflow.assignableTo" theme="bootstrap"
                                    style="width:100%" ng-disabled="!hasPermission('permission.workflow.edit')">
                             <ui-select-match placeholder="{{wfEditorVm.select}}">{{$item}}</ui-select-match>
                             <ui-select-choices repeat="type in ['ECO'] | filter: $select.search">
                                 <div ng-bind="type"></div>
                             </ui-select-choices>
                         </ui-select>
                     </div>--%>
                </div>
                <div ng-if="wfEditorVm.selectedElement != null && wfEditorVm.selectedElement.type == 'bpmn:Task' &&
                            wfEditorVm.selectedStatus != null">
                    <div style="margin-bottom: 20px;">
                        <h4 translate>WORKFLOW_NEW_STATUS_DETAILS</h4>
                    </div>
                    <div class="form-group">
                        <label class="control-label" translate>WORKFLOW_ALL_NAME</label>
                        <input type="text" class="form-control" ng-disabled="!hasPermission('permission.workflow.edit')"
                               ng-model="wfEditorVm.selectedStatus.name"
                               ng-change="wfEditorVm.updateStatus()">
                    </div>
                    <div class="form-group">
                        <label class="control-label" translate>DESCRIPTION</label> :
                        <textarea class="form-control" rows="5" style="resize: none;"
                                  ng-model="wfEditorVm.selectedStatus.description"
                                  ng-disabled="!hasPermission('permission.workflow.edit')"></textarea>
                    </div>
                    <div class="form-group">
                        <label class="control-label" translate>WORKFLOW_NEW_STATUS_TYPE</label> :
                        <ui-select ng-model="wfEditorVm.selectedStatus.type" theme="bootstrap" style="width:100%"
                                   ng-disabled="!hasPermission('permission.workflow.edit')">
                            <ui-select-match placeholder="{{wfEditorVm.select}}">{{$select.selected}}</ui-select-match>
                            <ui-select-choices
                                    repeat="type in ['PENDING', 'REVIEW', 'RELEASED', 'COMPLETE', 'CANCELLED', 'HOLD'] | filter: $select.search">
                                <div ng-bind="type"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>