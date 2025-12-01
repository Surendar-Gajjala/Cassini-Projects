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

        .ml8 {
            margin-left: 8px;
        }

        .ml12 {
            margin-left: 12px !important;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;">
                    <i class="la la-plus" title="Add Group" ng-click="programProjectsDrillDownVm.addGroup()"
                       ng-if="hasPermission('program', 'edit') || loginPersonDetails.isAdmin || loginPersonDetails.person.id == programInfo.programManager"></i>
                </th>
                <th class="col-width-250" translate>NAME</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th style="width: 50px"></th>
                <th translate>TYPE</th>
                <th style="width: 150px;z-index: auto !important;">
                    <span ng-if="selectedPerson != null"
                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{selectedPerson.fullName}})
                            <i class="fa fa-times-circle" ng-click="clearProgramManager()"
                               title="{{removeTitle}}"></i>
                    </span>
                    <br>

                    <div class="dropdown" uib-dropdown style="display: inline-block">
                        <span uib-dropdown-toggle><span translate>ASSIGNED_TO</span>
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
                <th translate>DURATION</th>
                <th style="width: 150px;text-align: center;" translate>STATUS</th>
                <th style="width: 150px" translate>PLANNED_START_DATE</th>
                <th style="width: 150px" translate>PLANNED_FINISH_DATE</th>
                <th style="width: 150px" translate>ACTUAL_START_DATE</th>
                <th style="width: 150px" translate>ACTUAL_FINISH_DATE</th>
                <th class="actions-col" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="programProjectsDrillDownVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_PROJECTS</span>
                </td>
            </tr>
            <tr ng-if="programProjectsDrillDownVm.loading == false && programProjectsDrillDownVm.programProjects.length == 0">
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
            <tr ng-repeat="programProject in programProjectsDrillDownVm.programProjects">
                <td>
                    <i class="la la-plus"
                       ng-if="programProject.objectType == 'GROUP' && !programProject.editMode && (hasPermission('program', 'edit') || loginPersonDetails.isAdmin || loginPersonDetails.person.id == programInfo.programManager)"
                       title="{{programProject.childTitle}}"
                       ng-click="programProjectsDrillDownVm.addProjects(programProject)"></i>
                </td>
                <td class="col-width-250">
                    <span class="level{{programProject.level}}" style="display: inline-flex"
                          ng-if="!programProject.editMode">
                        <i ng-if="programProject.editMode == false && programProject.childCount > 0" class="fa"
                           style="cursor: pointer;margin-right: 0;margin-top: 3px;"
                           title="{{programProject.expanded ? 'Collapse':'Expand'}}"
                           ng-class="{'fa-caret-right': (programProject.expanded == false || programProject.expanded == null || programProject.expanded == undefined),
                                      'fa-caret-down': programProject.expanded == true}"
                           ng-click="programProjectsDrillDownVm.toggleNode(programProject)"></i>
                        <span ng-bind-html="programProject.name | highlightText: programProjectsDrillDownVm.searchText"
                              class="ml8"
                              ng-if="programProject.objectType == 'GROUP'"></span>
                        <a href=""
                           ng-click="programProjectsDrillDownVm.openProjectDetails(programProject)"
                           title="Click to show details"
                           ng-if="programProject.objectType == 'PROJECT' || programProject.objectType == 'PROJECTACTIVITY' || programProject.objectType == 'PROJECTTASK'"
                           ng-show="hasPermission('project','view') && (loginPersonDetails.external == false ||(loginPersonDetails.external == true && sharedPermission == 'WRITE'))">
                            <div ng-class="{'ml12': programProject.childCount == 0}"
                                 class="ml8 col-width-250"
                                 ng-bind-html="programProject.name | highlightText: programProjectsDrillDownVm.searchText"></div>
                        </a>
                        
                        <div ng-show="hasPermission('project','view') == false || ( loginPersonDetails.external == true && sharedPermission == 'READ')"
                             title="You don't have permission to see programProject" class="ml8 col-width-250"
                             ng-if="programProject.objectType == 'PROJECT' || programProject.objectType == 'PROJECTACTIVITY' || programProject.objectType == 'PROJECTTASK'">
                            <span ng-bind-html="programProject.name | highlightText: programProjectsDrillDownVm.searchText"></span>
                        </div>
                        <div ng-bind-html="programProject.name | highlightText: programProjectsDrillDownVm.searchText"
                             class="ml8 col-width-250" ng-class="{'ml12': programProject.childCount == 0}"
                             ng-if="programProject.objectType == 'PROJECTPHASEELEMENT' || programProject.objectType == 'PROJECTMILESTONE'"></div>
                        <%--<span class="label label-info label-count"
                              ng-if="programProject.editMode == false"
                              style="font-size: 12px;padding: 1px 4px !important;"
                              ng-bind-html="programProject.childCount">
                        </span>--%>
                    </span>
                    <input type="text" class="form-control" ng-model="programProject.name" placeholder="Enter name"
                           ng-enter="programProjectsDrillDownVm.saveProgramProject(programProject)"
                           ng-if="programProject.editMode">
                </td>
                <td class="description-column">
                    <span title="{{programProject.description}}" ng-if="!programProject.editMode"><span
                            ng-bind-html="programProject.description | highlightText: programProjectsDrillDownVm.searchText"></span>
                    </span>
                    <input type="text" class="form-control" ng-model="programProject.description"
                           placeholder="Enter description"
                           ng-enter="programProjectsDrillDownVm.saveProgramProject(programProject)"
                           ng-if="programProject.editMode">
                </td>
                <td>
                    <i class='fa fa-exclamation-triangle'
                       ng-if="programProject.overDue && programProject.percentComplete < 100"
                       style='cursor: pointer;color: red;padding-right: 5px;' title='This task is overdue'></i>
                    <i class='fa fa-user' aria-hidden='true' style='cursor: pointer;color: red;padding-right: 5px;'
                       ng-if="!programProject.validUser"
                       title='Re Assign this task to another person'></i>
                    <i class='fa fa-share-alt' style='cursor: pointer;color: darkgrey;padding-right: 5px;'
                       ng-if="programProject.isShared"
                       title="{{programProject.objectType == 'PROJECTACTIVITY' ? 'This activity is shared':'This task is shared'}}"></i>
                    <i class='fa fa-paperclip' style='cursor: pointer;color: darkgrey;' ng-if="programProject.hasFiles"
                       ng-click="programProjectsDrillDownVm.openFilesView(programProject)"
                       title="{{programProject.objectType == 'PROJECTACTIVITY' ? 'This activity has files':'This task has files'}}"></i>
                </td>
                <td style="width: 100px;">
                    <project-type object="programProject"></project-type>
                </td>
                <td style="width: 150px">
                    <span title="{{programProject.assignedToName}}"><span
                            ng-bind-html="programProject.assignedToName | highlightText: programProjectsDrillDownVm.searchManager"></span>
                    </span>
                </td>
                <td style="text-align: center;">{{programProject.duration}}</td>
                <td style="text-align: center;">
                    <span ng-if="programProject.objectType == 'PROJECT'">{{programProject.percent}}</span>
                    <task-status task="programProject"></task-status>
                </td>
                <td style="width: 150px">
                    {{programProject.plannedStartDate}}
                </td>
                <td style="width: 150px">
                    {{programProject.plannedFinishDate}}
                </td>
                <td style="width: 150px">{{programProject.actualStartDate}}</td>
                <td style="width: 150px">{{programProject.actualFinishDate}}</td>
                <td class="text-center actions-col">
                    <span class="btn-group"
                          ng-if="programProject.editMode == true"
                          style="margin: 0">
                        <i title="{{'SAVE' | translate}}" style="cursor: pointer"
                           ng-click="programProjectsDrillDownVm.saveProgramProject(programProject)"
                           class="la la-check">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="programProject.isNew" style="cursor: pointer"
                           ng-click="programProjectsDrillDownVm.removeProgramProject(programProject)"
                           class="la la-times">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="!programProject.isNew" style="cursor: pointer"
                           ng-click="programProjectsDrillDownVm.cancelChanges(programProject)"
                           class="la la-times">
                        </i>
                    </span>
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                          ng-if="programProject.editMode == false && (programProject.objectType == 'GROUP' || programProject.objectType == 'PROJECT') && (hasPermission('program','edit') || hasPermission('program','delete'))">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="hasPermission('program','edit') && programProject.objectType == 'GROUP'">
                            <a href=""
                               ng-click="programProjectsDrillDownVm.editProgramProject(programProject)"
                               translate>EDIT</a>
                        </li>
                        <li ng-if="hasPermission('program','delete')">
                            <a href=""
                               ng-click="programProjectsDrillDownVm.deleteProgramProject(programProject)">
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