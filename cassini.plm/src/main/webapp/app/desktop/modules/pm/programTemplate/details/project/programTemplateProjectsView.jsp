<div>
    <style scoped>
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
                    <i class="la la-plus" title="Add Group" ng-click="programTemplateProjectsVm.addGroup()"></i>
                </th>
                <th class="col-width-250" translate>NAME</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th class="col-width-150" translate>MANAGER</th>
                <th style="width: 150px" translate>CREATED_BY</th>
                <th style="width: 150px" translate>CREATED_DATE</th>
                <th style="width: 150px" translate>MODIFIED_BY</th>
                <th style="width: 150px" translate>MODIFIED_DATE</th>
                <th class="actions-col" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="programTemplateProjectsVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_PROJECTS</span>
                </td>
            </tr>
            <tr ng-if="programTemplateProjectsVm.loading == false && programTemplateProjectsVm.programProjects.length == 0">
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
            <tr ng-repeat="programProject in programTemplateProjectsVm.programProjects">
                <td>
                    <i class="la la-plus" ng-if="programProject.type == 'GROUP' && !programProject.editMode"
                       ng-click="programTemplateProjectsVm.createNewTemplate(programProject)"></i>
                </td>
                <td class="col-width-250">
                    <a href="" ng-click="programTemplateProjectsVm.openProjectDetails(programProject)"
                       title="{{allProjectsVm.projectOpenTitle}}" ng-if="programProject.type == 'PROJECT'"
                       ng-show="hasPermission('project','view')">
                        <span class="level{{programProject.level}}">
                            <span ng-class="{'ml8': programProject.children.length == 0}">{{programProject.name}}</span>
                        </span>
                    </a>
                    <span ng-show="hasPermission('programProject','view') == false"
                          title="You don't have permission to see programProject"
                          ng-if="programProject.type == 'PROJECT'">
                            <span ng-bind-html="programProject.name | highlightText: freeTextQuery"></span>
                    </span>

                    <span class="level{{programProject.level}}"
                          ng-if="programProject.type == 'GROUP' && !programProject.editMode">
                        <i ng-if="programProject.children.length > 0 && programProject.editMode == false" class="fa"
                           style="cursor: pointer;margin-right: 0;"
                           ng-class="{'fa-caret-right': (programProject.expanded == false || programProject.expanded == null || programProject.expanded == undefined),
                                          'fa-caret-down': programProject.expanded == true}"
                           ng-click="programTemplateProjectsVm.toggleItemNode(bomItem)"></i>
                        <span ng-class="{'ml8': programProject.children.length == 0}">{{programProject.name}}</span>
                    </span>
                    <input type="text" class="form-control" ng-model="programProject.name" placeholder="Enter name"
                           ng-enter="programTemplateProjectsVm.saveProgramProject(programProject)"
                           ng-if="programProject.editMode && programProject.type == 'GROUP'">
                </td>
                <td class="description-column">
                    <span title="{{programProject.description}}" ng-if="!programProject.editMode"><span
                            ng-bind-html="programProject.description | highlightText: freeTextQuery"></span>
                    </span>
                    <input type="text" class="form-control" ng-model="programProject.description"
                           placeholder="Enter description"
                           ng-enter="programTemplateProjectsVm.saveProgramProject(programProject)"
                           ng-if="programProject.editMode && programProject.type == 'GROUP'">
                </td>
                <td style="width: 150px">{{programProject.managerName}}</td>
                <td style="width: 150px">{{programProject.createdByName}}</td>
                <td style="width: 150px">{{programProject.createdDate}}</td>
                <td style="width: 150px">{{programProject.modifiedByName}}</td>
                <td style="width: 150px">{{programProject.modifiedDate}}</td>
                <td class="text-center">
                    <span class="btn-group" ng-if="programProject.editMode == true" style="margin: 0">
                        <i title="{{'SAVE' | translate}}" style="cursor: pointer"
                           ng-click="programTemplateProjectsVm.saveProgramProject(programProject)"
                           class="la la-check">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="programProject.isNew" style="cursor: pointer"
                           ng-click="programTemplateProjectsVm.removeProgramProject(programProject)"
                           class="la la-times">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="!programProject.isNew" style="cursor: pointer"
                           ng-click="programTemplateProjectsVm.cancelChanges(programProject)"
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
                               ng-click="programTemplateProjectsVm.editProgramProject(programProject)"
                               translate>EDIT</a>
                        </li>
                        <li ng-if="hasPermission('program','delete')">
                            <a href=""
                               ng-click="programTemplateProjectsVm.deleteProgramProject(programProject)">
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