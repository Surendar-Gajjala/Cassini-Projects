<div style="position: relative;height: 100%;">
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
                            <span translate>PROJECT_TYPE</span>
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
                                                    on-select-type="newProjectVm.onSelectType"
                                                    object-type="PROJECT"></project-management-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newProjectVm.project.projectType.name" readonly>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_PROJECT_NAME' | translate}}"
                                   ng-model="newProjectVm.project.name"
                                   autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newProjectVm.project.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROJECT_MANAGER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectVm.project.projectManager" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newProjectVm.select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newProjectVm.persons | filter: {fullName:$select.search}">
                                    <div ng-bind-html="trustAsHtml((person.fullName | highlight: $select.search))">
                                    </div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"> <span translate>PLANNED_START_DATE</span><span
                                class="asterisk">*</span>:</label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedStartDate" class="form-control" placeholder="dd/mm/yyyy"
                                   ng-model="newProjectVm.project.plannedStartDate" start-finish-date-picker>
                        </div>


                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>PLANNED_FINISH_DATE</span><span
                                class="asterisk">*</span>:</label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedFinishDate" class="form-control" placeholder="dd/mm/yyyy"
                                   ng-model="newProjectVm.project.plannedFinishDate" start-finish-date-picker>
                        </div>
                    </div>





                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SELECT_TEMPLATE</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectVm.selectedTemplate" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{newProjectVm.selectTemplate}}">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="template in newProjectVm.templates | filter: {name:$select.search}">
                                    <div title="{{template.description}}"
                                         ng-bind-html="template.name | highlight:$select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newProjectVm.projectCreationType == 'PROGRAM'">
                        <label class="col-sm-4 control-label">
                            <span translate>PROGRAM</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectVm.project.program" theme="bootstrap" style="width:100%"
                                       ng-disabled="newProjectVm.projectCreationType == 'PROGRAM'">
                                <ui-select-match placeholder="Select Program">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="program.id as program in newProjectVm.programs | filter: {name:$select.search}">
                                    <div ng-bind-html="program.name | highlight:$select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProjectVm.selectedTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_TEAM</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-click="newProjectVm.selectTeam(newProjectVm.project)"
                                   ng-model="newProjectVm.project.team">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProjectVm.selectedTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_ASSIGNED_TO</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer"
                             ng-class="{'disabled': newProjectVm.project.team == false}">
                            <input type="checkbox" ng-model="newProjectVm.project.assignedTo">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProjectVm.selectedTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_FOLDERS</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProjectVm.project.copyFolders">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newProjectVm.project.copyFolders">
                        <label class="col-sm-4 control-label"><span translate>FOLDERS_TITLE</span> :</label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="all" name="folderType" type="radio" checked
                                       ng-click="newProjectVm.selectFolder('All', $event)">
                                <label for="all" onclick="" translate>All</label>

                                <input id="project" name="folderType" type="radio"
                                       ng-click="newProjectVm.selectFolder('Project', $event)">
                                <label for="project" onclick="" translate>Project</label>

                                <input id="activity" name="folderType" type="radio"
                                       ng-click="newProjectVm.selectFolder('Activity', $event)">
                                <label for="activity" onclick="" translate>Activity</label>

                                <input id="task" name="folderType" type="radio"
                                       ng-click="newProjectVm.selectFolder('Task', $event)">
                                <label for="task"
                                       onclick="" translate>Task</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProjectVm.selectedTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>Copy Workflows</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProjectVm.project.copyWorkflows"
                                      ng-change="newProjectVm.selectCopyWorkflows()">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newProjectVm.project.copyWorkflows">
                        <label class="col-sm-4 control-label"><span translate>Workflows</span> :</label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="allWf" name="workflowType" type="radio" checked
                                       ng-click="newProjectVm.selectWorkflow('All', $event)">
                                <label for="allWf" onclick="" translate>All</label>

                                <input id="projectWf" name="workflowType" type="radio"
                                       ng-click="newProjectVm.selectWorkflow('Project', $event)">
                                <label for="projectWf" onclick="" translate>Project</label>

                                <input id="activityWf" name="workflowType" type="radio"
                                       ng-click="newProjectVm.selectWorkflow('Activity', $event)">
                                <label for="activityWf" onclick="" translate>Activity</label>

                                <input id="taskWf" name="workflowType" type="radio"
                                       ng-click="newProjectVm.selectWorkflow('Task', $event)">
                                <label for="taskWf"
                                       onclick="" translate>Task</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectVm.project.workflowDefinition" theme="bootstrap"
                                       style="width:100%" ng-disabled="newProjectVm.project.copyWorkflows">
                                <ui-select-match placeholder="{{selectWorkflow}}">{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow in newProjectVm.wfs | filter: {name:$select.search}">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues" ng-if="newProjectVm.attributes.length > 0"
                                     attributes="newProjectVm.attributes"></attributes-view>


                    <%-- <div ng-show="newProjectVm.creating" ng-cloak>
                        <span>Creating project...</span>

                        <div class="progress progress-striped active">
                            <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                aria-valuemax="100" style="width: 100%">
                            </div>
                        </div>
                        <br>
            </div>--%>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>