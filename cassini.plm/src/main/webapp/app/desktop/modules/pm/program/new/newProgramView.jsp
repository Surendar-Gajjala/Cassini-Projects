<div style="position: relative;height: 100%">
    <style scoped>
        form .form-group:last-child {
            margin-bottom: 100px;
        }
    </style>
    <div style="overflow-x: hidden; padding: 20px;height: 100%;">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROGRAM_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span translate id="select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div
                                                style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <project-management-type-tree
                                                    on-select-type="newProgramVm.onSelectType"
                                                    object-type="PROGRAM"></project-management-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newProgramVm.program.programType.name" readonly>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_PROGRAM_NAME' | translate}}"
                                   ng-model="newProgramVm.program.name" autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                          <textarea name="description" rows="3" class="form-control" style="resize: none"
                                    placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                    ng-model="newProgramVm.program.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROGRAM_MANAGER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProgramVm.program.programManager" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newProgramVm.select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newProgramVm.persons | filter: {fullName:$select.search}">
                                    <div ng-bind-html="person.fullName | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TEMPLATE_SINGULAR</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProgramVm.program.programTemplate" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newProgramVm.selectTemplate}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="programTemplate.id as programTemplate in newProgramVm.programTemplates | filter: {name:$select.search}">
                                    <div title="{{programTemplate.description}}"
                                         ng-bind-html="programTemplate.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProgramVm.program.programTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_RESOURCES</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox"
                                   ng-model="newProgramVm.program.resources">
                        </div>
                    </div>
                    <div class="form-group" ng-if="newProgramVm.program.programTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_TEAM</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox"
                                   ng-click="newProgramVm.selectTeam(newProgramVm.program)"
                                   ng-disabled="newProgramVm.program.projects == false"
                                   ng-model="newProgramVm.program.team">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProgramVm.program.programTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_ASSIGNED_TO</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProgramVm.program.assignedTo"
                                   ng-disabled="newProgramVm.program.team == false">
                        </div>
                    </div>
                    <div class="form-group" ng-if="newProgramVm.program.programTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_FOLDERS</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProgramVm.program.copyFolders">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newProgramVm.program.copyFolders">
                        <label class="col-sm-4 control-label"><span translate>FOLDERS_TITLE</span> :</label>

                        <div class="col-sm-8" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="all" name="folderType" type="radio" checked
                                       ng-click="newProgramVm.selectFolder('All', $event)">
                                <label for="all" onclick="" translate>All</label>

                                <input id="program" name="folderType" type="radio"
                                       ng-click="newProgramVm.selectFolder('Program', $event)">
                                <label for="program" onclick="" translate>Program</label>

                                <input id="project" name="folderType" type="radio"
                                       ng-click="newProgramVm.selectFolder('Project', $event)">
                                <label for="project"
                                       onclick="" translate>Project</label>
                                <input id="activity" name="folderType" type="radio"
                                       ng-click="newProgramVm.selectFolder('Activity', $event)">
                                <label for="activity"
                                       onclick="" translate>Activity</label>
                                <input id="task" name="folderType" type="radio"
                                       ng-click="newProgramVm.selectFolder('Task', $event)">
                                <label for="task"
                                       onclick="" translate>Task</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newProgramVm.program.programTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>Copy Workflows</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProgramVm.program.copyWorkflows"
                                   ng-change="newProgramVm.selectCopyWorkflows()">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newProgramVm.program.copyWorkflows">
                        <label class="col-sm-4 control-label"><span translate>Workflows</span> :</label>

                        <div class="col-sm-8" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="workflowAll" name="type" type="radio" checked
                                       ng-click="newProgramVm.selectWorkflow('All', $event)">
                                <label for="workflowAll" onclick="" translate>All</label>

                                <input id="programWorkflow" name="type" type="radio"
                                <%--ng-class="{'disable-cursor':newProgramVm.program.workflowDefinition}"--%>
                                       ng-click="newProgramVm.selectWorkflow('Program', $event)">
                                <label for="programWorkflow" onclick="" translate>Program</label>

                                <input id="projectWorkflow" name="type" type="radio"
                                       ng-click="newProgramVm.selectWorkflow('Project', $event)">
                                <label for="projectWorkflow"
                                       onclick="" translate>Project</label>

                                <input id="activityWorkflow" name="type" type="radio"
                                       ng-click="newProgramVm.selectWorkflow('Activity', $event)">
                                <label for="activityWorkflow"
                                       onclick="" translate>Activity</label>

                                <input id="taskWorkflow" name="type" type="radio"
                                       ng-click="newProgramVm.selectWorkflow('Task', $event)">
                                <label for="taskWorkflow"
                                       onclick="" translate>Task</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProgramVm.program.workflowDefinition" theme="bootstrap"
                                       style="width:100%" ng-disabled="newProgramVm.program.copyWorkflows">
                                <ui-select-match placeholder="{{selectWorkflow}}">{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow in newProgramVm.wfs | filter: {name:$select.search}">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues" ng-if="newProgramVm.attributes.length > 0"
                                     attributes="newProgramVm.attributes"></attributes-view>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

