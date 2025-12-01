<div style="position: relative;height: 100%;">
    <style scoped>
        .disable-cursor {
            cursor: not-allowed !important;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;height: 100%;">
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
                                   placeholder="{{'ENTER_NAME' | translate}}"
                                   ng-model="newProgramTemplateVm.template.name"
                                   autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newProgramTemplateVm.template.description">
                            </textarea>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProgramTemplateVm.template.program == null">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProgramTemplateVm.template.workflowDefinition" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow in newProgramTemplateVm.wfs | filter: {name:$select.search}">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProgramTemplateVm.template.program != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_RESOURCES</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox"
                                   ng-model="newProgramTemplateVm.template.resources">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProgramTemplateVm.template.program != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_PROJECTS</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProgramTemplateVm.template.projects"
                                   ng-click="newProgramTemplateVm.selectProjects(newProgramTemplateVm.template)">
                        </div>
                    </div>
                    <div class="form-group" ng-if="newProgramTemplateVm.template.projects">
                        <label class="col-sm-4 control-label"></label>

                        <div class="col-sm-7" style="cursor: pointer;padding-left: 5px;">
                            <table class='table table-striped'>
                                <thead>
                                <tr>
                                    <th style="width: 20px;">
                                        <input type="checkbox" ng-model="newProgramTemplateVm.selectAllCheck"
                                               ng-change="newProgramTemplateVm.selectAll()" class="form-control"/>
                                    </th>
                                    <th translate>NAME</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-if="newProgramTemplateVm.loading == true">
                                    <td colspan="14"><img
                                            src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                            class="mr5"><span translate>LOADING_PROJECTS</span>
                                    </td>
                                </tr>
                                <tr ng-if="newProgramTemplateVm.loading == false && newProgramTemplateVm.programProjects.length == 0">
                                    <td colspan="12"
                                        style="background-color: #f9fbfe  !important;color: unset !important;">
                                        <div class="no-data">
                                            <img src="app/assets/no_data_images/ManufacturerParts.png" alt=""
                                                 class="image">

                                            <div class="message" translate>NO_PROJECTS</div>
                                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;"
                                                 translate>
                                                NO_PERMISSION_MESSAGE
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr ng-repeat="programProject in newProgramTemplateVm.programProjects">
                                    <td style="width: 20px;text-align: center;background-color: #f9fbfe  !important;">
                                        <input type="checkbox" ng-model="programProject.selected"
                                               ng-change="newProgramTemplateVm.selectCheck(programProject)"
                                               class="form-control"/>
                                    </td>
                                    <td style="background-color: #f9fbfe  !important;">
                                            <span class="level{{programProject.level}}">
                                                <i ng-if="programProject.children.length > 0"
                                                   class="fa"
                                                   style="cursor: pointer;margin-right: 0;"
                                                   ng-class="{'fa-caret-right': (programProject.expanded == false || programProject.expanded == null || programProject.expanded == undefined),
                                                              'fa-caret-down': programProject.expanded == true}"
                                                   ng-click="newProgramTemplateVm.toggleNode(programProject)">
                                                </i>
                                                <span ng-class="{'ml8':programProject.children.length == 0}">{{programProject.name}}</span>
                                            </span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newProgramTemplateVm.template.program != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_TEAM</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox"
                                   ng-click="newProgramTemplateVm.selectTeam(newProgramTemplateVm.template)"
                                   ng-disabled="newProgramTemplateVm.template.projects == false"
                                   ng-model="newProgramTemplateVm.template.team">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProgramTemplateVm.template.program != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_ASSIGNED_TO</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProgramTemplateVm.template.assignedTo"
                                   ng-disabled="newProgramTemplateVm.template.team == false">
                        </div>
                    </div>
                    <div class="form-group" ng-if="newProgramTemplateVm.template.program != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_FOLDERS</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProgramTemplateVm.template.copyFolders"
                                   ng-change="newProgramTemplateVm.selectCopyFolders()">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newProgramTemplateVm.template.copyFolders">
                        <label class="col-sm-4 control-label"><span translate>FOLDERS_TITLE</span> :</label>

                        <div class="col-sm-8" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="all" name="folderType" type="radio" checked
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectFolder('All', $event)">
                                <label for="all"
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
                                       onclick="" translate>All</label>

                                <input id="program" name="folderType" type="radio"
                                       ng-click="newProgramTemplateVm.selectFolder('Program', $event)">
                                <label for="program" onclick="" translate>Program</label>

                                <input id="project" name="folderType" type="radio"
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectFolder('Project', $event)">
                                <label for="project"
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
                                       onclick="" translate>Project</label>
                                <input id="activity" name="folderType" type="radio"
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectFolder('Activity', $event)">
                                <label for="activity"
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
                                       onclick="" translate>Activity</label>
                                <input id="task" name="folderType" type="radio"
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectFolder('Task', $event)">
                                <label for="task"
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
                                       onclick="" translate>Task</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>


                    <div class="form-group" ng-if="newProgramTemplateVm.template.program != null">
                        <label class="col-sm-4 control-label">
                            <span translate>Copy Workflows</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newProgramTemplateVm.template.copyWorkflows"
                                   ng-change="newProgramTemplateVm.selectCopyWorkflows()">
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newProgramTemplateVm.template.copyWorkflows">
                        <label class="col-sm-4 control-label"><span translate>Workflows</span> :</label>

                        <div class="col-sm-8" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="workflowAll" name="type" type="radio" checked
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectWorkflow('All', $event)">
                                <label for="workflowAll" onclick=""
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
                                       translate>All</label>

                                <input id="programWorkflow" name="type" type="radio"
                                       ng-click="newProgramTemplateVm.selectWorkflow('Program', $event)">
                                <label for="programWorkflow" onclick="" translate>Program</label>

                                <input id="projectWorkflow" name="type" type="radio"
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectWorkflow('Project', $event)">
                                <label for="projectWorkflow"
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
                                       onclick="" translate>Project</label>
                                <input id="activityWorkflow" name="type" type="radio"
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectWorkflow('Activity', $event)">
                                <label for="activityWorkflow"
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
                                       onclick="" translate>Activity</label>
                                <input id="taskWorkflow" name="type" type="radio"
                                       ng-disabled="!newProgramTemplateVm.template.projects"
                                       ng-click="newProgramTemplateVm.selectWorkflow('Task', $event)">
                                <label for="taskWorkflow"
                                       ng-class="{'disable-cursor':!newProgramTemplateVm.template.projects}"
                                       title="{{newProgramTemplateVm.template.projects ? '':'Select copy projects'}}"
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

