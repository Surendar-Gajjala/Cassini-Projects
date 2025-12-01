<div>
    <style scoped>
        .basic_content {
            position: absolute;
            top: 20px;
            bottom: 0px;
            left: 20px;
            right: 20px;
        }
    </style>
    <div class="basic_content">
        <div ng-if="selectedClassificationType !=null  || (workflowTypeBasicVm.workflowType != null && !newItemTypeCreating)" style="height: 100%;">
            <div style="width: 100px;margin: 10px 0 0 0 !important;">
                <button class="btn btn-sm btn-success min-width" ng-click="workflowTypeBasicVm.onSave()"
                        ng-if="hasPermission('workflowtype','edit')" translate>SAVE
                </button>
            </div>
            <div class="">

                <div class="row">
                    <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label"><span translate>NAME</span>
                                    <span class="asterisk">*</span> :</label>

                                <div class="col-sm-7">
                                    <input type="text" class="form-control" name="title"
                                           ng-model="workflowTypeBasicVm.workflowType.name"
                                           ng-disabled="workflowTypeBasicVm.workflowTypeWorkflowsExist == true || !hasPermission('workflowtype','edit')">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> : </label>

                                <div class="col-sm-7">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  ng-disabled="!hasPermission('workflowtype','edit')"
                                  ng-model="workflowTypeBasicVm.workflowType.description"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>AUTO_NUMBER_SOURCE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select <%--ng-disabled="workflowTypeBasicVm.workflowTypeWorkflowsExist == true"--%>
                                            ng-model="workflowTypeBasicVm.workflowType.numberSource" theme="bootstrap"
                                            style="width:100%"
                                            title="{{workflowTypeBasicVm.workflowType.numberSource.name}}"
                                            ng-disabled="!hasPermission('workflowtype','edit')">
                                        <ui-select-match placeholder="Select">{{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="source in workflowTypeBasicVm.autoNumbers | filter: $select.search">
                                            <div title="{{source.name}}">{{source.name}}
                                            </div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>LIFE_CYCLE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-disabled="workflowTypeBasicVm.workflowTypeWorkflowsExist == true"
                                               ng-model="workflowTypeBasicVm.workflowType.lifecycle" theme="bootstrap"
                                               style="width:100%"
                                               title="{{workflowTypeBasicVm.workflowType.lifecycle.name}}">
                                        <ui-select-match placeholder="Select">{{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="lifecycle in workflowTypeBasicVm.lifecycles | filter: $select.search">
                                            <div title="{{lifecycle.name}}">{{lifecycle.name}}
                                            </div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>WORKFLOW_NEW_ASSIGNABLE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="workflowTypeBasicVm.workflowType.assignable" theme="bootstrap"
                                               on-select="workflowTypeBasicVm.changeAssignable($item)"
                                               style="width:100%"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               title="{{workflowTypeBasicVm.assignable}}">
                                        <ui-select-match placeholder="Select">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="source in workflowTypeBasicVm.selectedTypes | filter: $select.search">
                                            <div title="{{source}}">{{source}}
                                            </div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group" ng-if="workflowTypeBasicVm.workflowType.assignable == 'ITEMS'">
                                <label class="col-sm-4 control-label">
                                    <span translate>ITEM_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <item-classification
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></item-classification>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>


                                    </div>
                                </div>
                            </div>
                            <div class="form-group" ng-show="workflowTypeBasicVm.workflowType.assignable == 'CHANGES'">
                                <label class="col-sm-4 control-label">
                                    <span translate>CHANGE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <workflow-change
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></workflow-change>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>


                                    </div>
                                </div>
                            </div>


                            <div class="form-group"
                                 ng-show="workflowTypeBasicVm.workflowType.assignable == 'MANUFACTURERS'">
                                <label class="col-sm-4 control-label">
                                    <span translate>MANUFACTURER_TYPE</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span>
                                                <span class="caret" style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <workflow-manufacturer
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></workflow-manufacturer>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>
                            </div>

                            <div class="form-group"
                                 ng-show="workflowTypeBasicVm.workflowType.assignable == 'MANUFACTURER PARTS'">
                                <label class="col-sm-4 control-label">
                                    <span translate>MANUFACTURER_PART_TYPE</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span>
                                                <span class="caret" style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <workflow-part
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></workflow-part>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="type"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>
                            </div>

                            <div class=form-group ng-show="workflowTypeBasicVm.workflowType.assignable == 'QUALITY'">
                                <label class="col-sm-4 control-label">
                                    <span translate>Quality Type</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <workflow-quality
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></workflow-quality>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>
                            </div>

                            <div class=form-group
                                 ng-show="workflowTypeBasicVm.workflowType.assignable == 'MAINTENANCE&REPAIR'">
                                <label class="col-sm-4 control-label">
                                    <span translate>TYPE</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <mro-type-tree
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></mro-type-tree>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>

                            </div>


                            <div class=form-group
                                 ng-show="workflowTypeBasicVm.workflowType.assignable == 'REQUIREMENT DOCUMENTS'">
                                <label class="col-sm-4 control-label">
                                    <span translate>TYPE</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <mro-type-tree
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></mro-type-tree>

                                                    <project-management-type-tree
                                                            on-select-type="workflowTypeBasicVm.onSelectType"
                                                            object-type="REQUIREMENTDOCUMENTTYPE"></project-management-type-tree>

                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>
                            </div>


                            <div class=form-group
                                 ng-show="workflowTypeBasicVm.workflowType.assignable == 'REQUIREMENT'">
                                <label class="col-sm-4 control-label">
                                    <span translate>TYPE</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">

                                                    <req-type-tree
                                                            on-select-type="workflowTypeBasicVm.onSelectType"
                                                            object-type="REQUIREMENTTYPE"></project-management-type-tree>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>
                            </div>


                            <div class=form-group
                                 ng-show="workflowTypeBasicVm.workflowType.assignable == 'MANUFACTURING'">
                                <label class="col-sm-4 control-label">
                                    <span translate>MANUFACTURING_TYPE</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <manufacturing-workflow-type-tree
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></manufacturing-workflow-type-tree>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>
                            </div>

                            <div class=form-group
                                 ng-show="workflowTypeBasicVm.workflowType.assignable == 'CUSTOM OBJECTS'">
                                <label class="col-sm-4 control-label">
                                    <span translate>CUSTOM_OBJECT_TYPE</span>
                                    <span class="asterisk">*</span>:
                                </label>

                                <div class="col-sm-7">
                                    <div class="input-group mb15">
                                        <div class="input-group-btn" uib-dropdown>
                                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                    type="button"
                                                    ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0">
                                                <span translate>SELECT</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                                            </button>
                                            <div class="dropdown-menu" role="menu">
                                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                    <custom-type-tree
                                                            on-select-type="workflowTypeBasicVm.onSelectType"></custom-type-tree>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="title"
                                               ng-disabled="workflowTypeBasicVm.usedWorkflows.length > 0"
                                               ng-model="workflowTypeBasicVm.workflowType.itemType.name" readonly>

                                    </div>
                                </div>
                            </div>


                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>