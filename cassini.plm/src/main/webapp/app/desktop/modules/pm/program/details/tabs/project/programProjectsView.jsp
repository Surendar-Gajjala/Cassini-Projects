<div>
    <style scoped>
        table {
            table-layout: fixed;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }

        .project-progress {
            margin-bottom: 10px;
        }

        .project-progress .progress-label {
            font-weight: 300;
            color: #707d91;
        }

        .project-progress .progress-percent {
            width: 30px;
            margin-left: 20px;
            margin-top: -8px;
            font-weight: 400;
            text-align: right;
        }

        .ml8 {
            margin-left: 8px;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;">
                    <i class="la la-plus" title="Add Group"
                       ng-hide="(loginPersonDetails.external && sharedProjectPermission == 'READ')"
                       ng-if="(hasPermission('program','edit') || programTeamAccess ==true || loginPersonDetails.isAdmin || loginPersonDetails.person.id == programInfo.programManager) || (loginPersonDetails.external && sharedProjectPermission == 'WRITE')"
                       ng-click="programProjectsVm.addGroup()"></i>
                </th>
                <th class="col-width-250" translate>NAME</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <!-- <th style="width: 150px" translate>PROGRAM_MANAGER</th> -->
                <th style="width: 150px;z-index: auto !important;">
                    <span ng-if="selectedPerson != null"
                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{selectedPerson.fullName}})
                            <i class="fa fa-times-circle" ng-click="clearProgramManager()"
                               title="{{removeTitle}}"></i>
                    </span>
                    <br>

                    <div class="dropdown" uib-dropdown style="display: inline-block">
                        <span uib-dropdown-toggle><span translate>PROJECT_MANAGER</span>
                            <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                        </span>
                        <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                            style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                            <li ng-repeat="person in programManagers"
                                ng-click="onSelectProgramManager(person)"><a
                                    href="">{{person.fullName}}</a>
                            </li>
                        </ul>
                    </div>
                </th>
                <th style="width: 150px;text-align: center;" translate>STATUS</th>
                <th style="width: 150px" translate>PLANNED_START_DATE</th>
                <th style="width: 150px" translate>PLANNED_FINISH_DATE</th>
                <th style="width: 150px" translate>ACTUAL_START_DATE</th>
                <th style="width: 150px" translate>ACTUAL_FINISH_DATE</th>
                <th class="actions-col" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="programProjectsVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_PROJECTS</span>
                </td>
            </tr>
            <tr ng-if="programProjectsVm.loading == false && programProjectsVm.programProjects.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message" translate>NO_PROJECTS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="programProject in programProjectsVm.programProjects">
                <td>
                    <i class="la la-plus"
                       ng-if="programProject.type == 'GROUP' && !programProject.editMode || (hasPermission('program','edit') && (!loginPersonDetails.external || sharedProjectPermission == 'WRITE') || programTeamAccess ==true || loginPersonDetails.isAdmin || loginPersonDetails.person.id == programInfo.programManager) && (!loginPersonDetails.external || sharedProjectPermission == 'WRITE'))"
                       title="Add Project"
                       ng-click="programProjectsVm.addProjects(programProject)"></i>
                </td>
                <td class="col-width-250">
                    <a href="" ng-click="programProjectsVm.openProjectDetails(programProject)"
                       title="{{allProjectsVm.projectOpenTitle}}" ng-if="programProject.type == 'PROJECT'"
                       ng-show="hasPermission('project','view')">
                        <span class="level{{programProject.level}}">
                            <span ng-class="{'ml8': programProject.children.length == 0}"
                                  ng-bind-html="programProject.name | highlightText: programProjectsVm.searchText"></span>
                        </span>
                    </a>
                        <span ng-show="hasPermission('programProject','view') == false && external.external== false"
                              title="You don't have permission to see programProject"
                              ng-if="programProject.type == 'PROJECT'">
                            <span ng-bind-html="programProject.name | highlightText: programProjectsVm.searchText"></span>
                    </span>

                    <span class="level{{programProject.level}}"
                          ng-if="programProject.type == 'GROUP' && !programProject.editMode">
                        <i ng-if="programProject.children.length > 0 && programProject.editMode == false" class="fa"
                           style="cursor: pointer;margin-right: 0;"
                           ng-class="{'fa-caret-right': (programProject.expanded == false || programProject.expanded == null || programProject.expanded == undefined),
                                      'fa-caret-down': programProject.expanded == true}"
                           ng-click="programProjectsVm.toggleNode(programProject)"></i>
                        <span ng-bind-html="programProject.name | highlightText: programProjectsVm.searchText"
                              ng-class="{'ml8': programProject.children.length == 0}"></span>
                    </span>
                    <input type="text" class="form-control" ng-model="programProject.name" placeholder="Enter name"
                           ng-enter="programProjectsVm.saveProgramProject(programProject)"
                           ng-if="programProject.editMode && programProject.type == 'GROUP'">
                </td>
                <td class="description-column">
                    <span title="{{programProject.description}}" ng-if="!programProject.editMode"><span
                            ng-bind-html="programProject.description | highlightText: programProjectsVm.searchText"></span>
                    </span>
                    <input type="text" class="form-control" ng-model="programProject.description"
                           placeholder="Enter description"
                           ng-enter="programProjectsVm.saveProgramProject(programProject)"
                           ng-if="programProject.editMode && programProject.type == 'GROUP'">
                </td>
                <td style="width: 150px">
                    <span title="{{programProject.managerFullName}}" ng-if="!programProject.editMode"><span
                            ng-bind-html="programProject.managerFullName | highlightText: programProjectsVm.searchManager"></span>
                </span>
                </td>
                <td style="text-align: center;">
                    <span ng-if="programProject.type == 'PROJECT'">{{programProject.percent}}</span>
                    <%--<div ng-if="programProject.percentComplete < 100 && programProject.type == 'PROJECT'"
                         class="project-progress progress text-center">
                        <div style="width:{{programProject.percentComplete}}%"
                             class="progress-bar progress-bar-primary progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{programProject.percentComplete}}%</span>
                        </div>
                    </div>
                    <div ng-if="programProject.percentComplete == 100 && programProject.type == 'PROJECT'"
                         class="programProject-progress progress text-center">
                        <div style="width:{{programProject.percentComplete}}%"
                             class="progress-bar progress-bar-success progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 10px;">{{programProject.percentComplete}}%</span>
                        </div>
                    </div>--%>
                </td>

                <td style="width: 150px">{{programProject.plannedStartDate}}</td>
                <td style="width: 150px">{{programProject.plannedFinishDate}}</td>
                <td style="width: 150px">{{programProject.actualStartDate}}</td>
                <td style="width: 150px">{{programProject.actualFinishDate}}</td>
                <td class="text-center">
                    <span class="btn-group" ng-if="programProject.editMode == true" style="margin: 0">
                        <i title="{{'SAVE' | translate}}" style="cursor: pointer"
                           ng-click="programProjectsVm.saveProgramProject(programProject)"
                           class="la la-check">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="programProject.isNew" style="cursor: pointer"
                           ng-click="programProjectsVm.removeProgramProject(programProject)"
                           class="la la-times">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="!programProject.isNew" style="cursor: pointer"
                           ng-click="programProjectsVm.cancelChanges(programProject)"
                           class="la la-times">
                        </i>
                    </span>
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                          ng-if="programProject.editMode == false">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="hasPermission('program','edit') && programProject.type == 'GROUP'">
                            <a href=""
                               ng-click="programProjectsVm.editProgramProject(programProject)"
                               translate>EDIT</a>
                        </li>
                        <li ng-if="hasPermission('program','delete')">
                            <a href=""
                               ng-click="programProjectsVm.deleteProgramProject(programProject)">
                                <span translate>DELETE</span>
                            </a>
                        </li>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>