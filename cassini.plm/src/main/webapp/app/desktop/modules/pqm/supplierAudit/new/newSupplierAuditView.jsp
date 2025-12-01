<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SUPPLIER_AUDIT_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="Select" translate>SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <quality-type-tree on-select-type="newSupplierAuditVm.onSelectType"
                                                               quality-type="SUPPLIERAUDITTYPE"></quality-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newSupplierAuditVm.newSupplierAudit.type.name" readonly>

                            </div>
                        </div>

                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <!-- <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newSupplierAuditVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newSupplierAuditVm.newPpap.number">
                            </div>
                        </div> -->


                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_AUTO_NUMBER' | translate }}"
                                   name="title"
                                   ng-model="newSupplierAuditVm.newSupplierAudit.number">
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TITLE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_TITLE' | translate }}"
                                   name="title"
                                   ng-model="newSupplierAuditVm.newSupplierAudit.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newSupplierAuditVm.newSupplierAudit.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSIGNED_TO</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newSupplierAuditVm.newSupplierAudit.assignedTo" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{'SELECT_ASSIGNED_TO' | translate}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newSupplierAuditVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANNED_YEAR</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" year-picker
                                   placeholder="{{'SELECT_PLANNED_YEAR' | translate}}"
                                   ng-model="newSupplierAuditVm.newSupplierAudit.plannedYear"/>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newSupplierAuditVm.newSupplierAudit.workflow"
                                       on-select="newSupplierAuditVm.onSelectWorkflow($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowTitle}}>
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices style="height: 120px"
                                                   repeat="workflow.id as workflow in newSupplierAuditVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newSupplierAuditVm.qcrRequiredProperties.length > 0"
                                     attributes="newSupplierAuditVm.qcrRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newSupplierAuditVm.qcrProperties.length > 0"
                                     attributes="newSupplierAuditVm.qcrProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newSupplierAuditVm.attributes.length > 0"
                                     attributes="newSupplierAuditVm.attributes"></attributes-view>


                </form>
            </div>
        </div>
    </div>
</div>