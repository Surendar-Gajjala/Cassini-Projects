<div style="position: relative;height: 100%">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;height: 100%">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}" ng-model="newTemplateVm.template.name"
                                   autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newTemplateVm.template.description">
                            </textarea>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newTemplateVm.project == null">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newTemplateVm.template.workflowDefinition" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow in newTemplateVm.wfs | filter: {name:$select.search}">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newTemplateVm.template.programTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>MANAGER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newTemplateVm.template.manager" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newTemplateVm.selectManager}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newTemplateVm.persons | filter: {fullName:$select.search}">
                                    <div>{{person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newTemplateVm.project != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_TEAM</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox"
                                   ng-click="newTemplateVm.selectTeam(newTemplateVm.template)"
                                   ng-model="newTemplateVm.template.team">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newTemplateVm.project != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_ASSIGNED_TO</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer"
                             ng-class="{'disabled': newTemplateVm.template.team == false}">
                            <input type="checkbox" ng-model="newTemplateVm.template.assignedTo">
                        </div>
                    </div>
                    <div class="form-group" ng-if="newTemplateVm.project != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_FOLDERS</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newTemplateVm.template.copyFolders">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newTemplateVm.template.copyFolders">
                        <label class="col-sm-4 control-label"><span translate>FOLDERS_TITLE</span> :</label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="all" name="folderType" type="radio" checked
                                       ng-click="newTemplateVm.selectFolder('All', $event)">
                                <label for="all" onclick="" translate>All</label>

                                <input id="project" name="folderType" type="radio"
                                       ng-click="newTemplateVm.selectFolder('Project', $event)">
                                <label for="project" onclick="" translate>Project</label>

                                <input id="activity" name="folderType" type="radio"
                                       ng-click="newTemplateVm.selectFolder('Activity', $event)">
                                <label for="activity" onclick="" translate>Activity</label>

                                <input id="task" name="folderType" type="radio"
                                       ng-click="newTemplateVm.selectFolder('Task', $event)">
                                <label for="task"
                                       onclick="" translate>Task</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newTemplateVm.project != null">
                        <label class="col-sm-4 control-label">
                            <span translate>Copy Workflows</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newTemplateVm.template.copyWorkflows">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newTemplateVm.template.copyWorkflows">
                        <label class="col-sm-4 control-label"><span translate>Copy Workflows</span> :</label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="allWf" name="workflowType" type="radio" checked
                                       ng-click="newTemplateVm.selectWorkflow('All', $event)">
                                <label for="allWf" onclick="" translate>All</label>

                                <input id="projectWf" name="workflowType" type="radio"
                                       ng-click="newTemplateVm.selectWorkflow('Project', $event)">
                                <label for="projectWf" onclick="" translate>Project</label>

                                <input id="activityWf" name="workflowType" type="radio"
                                       ng-click="newTemplateVm.selectWorkflow('Activity', $event)">
                                <label for="activityWf" onclick="" translate>Activity</label>

                                <input id="taskWf" name="workflowType" type="radio"
                                       ng-click="newTemplateVm.selectWorkflow('Task', $event)">
                                <label for="taskWf"
                                       onclick="" translate>Task</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

