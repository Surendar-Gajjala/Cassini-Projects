<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th class="col-width-250" translate>NAME</th>
            <!-- <th class="col-width-250" translate>TYPE</th> -->
            <th>
                <span ng-if="allProjectsVm.selectedProjectType != null"
                      style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                    ({{allProjectsVm.selectedProjectType.name}})
                    <i class="fa fa-times-circle" ng-click="allProjectsVm.clearTypeSelection()"
                       title="{{removeTitle}}"></i>
              </span>
                <br>

                <div class="dropdown" uib-dropdown style="display: inline-block">
                        <span uib-dropdown-toggle><span translate>TYPE</span>
                            <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                        </span>

                    <div class="dropdown-menu" role="menu">
                        <div
                                style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                            <project-type-tree
                                    on-select-type="allProjectsVm.onSelectType"
                                    object-type="PROJECT"></project-type-tree>
                        </div>
                    </div>
                </div>
            </th>
            <th class="description-column" translate>DESCRIPTION</th>
            <th style="width: 150px;z-index: auto !important;">
                <span ng-if="selectedPerson != null"
                      style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                        ({{selectedPerson.fullName}})
                        <i class="fa fa-times-circle" ng-click="clearProjectManager()"
                           title="{{removeTitle}}"></i>
                </span>
                <br>

                <div class="dropdown" uib-dropdown style="display: inline-block">
                    <span uib-dropdown-toggle><span translate>PROJECT_MANAGER</span>
                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                    </span>
                    <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                        style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                        <li ng-repeat=" person in projectManagers"
                            ng-click="onSelectProjectManager(person)"><a
                                href="">{{person.fullName}}</a>
                        </li>
                    </ul>
                </div>
            </th>
            <th style="width: 150px" translate>PERCENT_COMPLETE</th>
            <th style="width: 150px" translate>PLANNED_START_DATE</th>
            <th style="width: 150px" translate>PLANNED_FINISH_DATE</th>
            <th style="width: 150px" translate>ACTUAL_START_DATE</th>
            <th style="width: 150px" translate>ACTUAL_FINISH_DATE</th>
            <th class='added-column'
                style="width: 150px;"
                ng-repeat="selectedAttribute in allProjectsVm.selectedAttributes">
                {{selectedAttribute.name}}
                <i class="fa fa-times-circle"
                   ng-click="allProjectsVm.removeAttribute(selectedAttribute)"
                   title="{{RemoveColumnTitle}}"></i>
            </th>
            <th style="width: 100px;text-align: center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="allProjectsVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PROJECTS</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="allProjectsVm.loading == false && allProjectsVm.projects.content.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Project.png" alt="" class="image">

                    <div class="message">{{ 'NO_PROJECTS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>

        </tr>

        <tr ng-repeat="project in allProjectsVm.projects.content">
            <td class="col-width-250">
                <a href="" ng-click="allProjectsVm.openProjectDetails(project)"
                   title="{{allProjectsVm.projectOpenTitle}}"
                   ng-show="hasPermission('project','view')">
                    <span ng-bind-html="project.name | highlightText: freeTextQuery"></span>
                </a>
                <span ng-show="hasPermission('project','view') == false"
                      title="You don't have permission to see project">
                            <span ng-bind-html="project.name | highlightText: freeTextQuery"></span>
                        </span>
            </td>
            <td class="col-width-250">
                <span title="{{project.type}}"><span
                        ng-bind-html="project.type | highlightText: freeTextQuery"></span>
               </span>
            </td>
            <td class="description-column">
                <span title="{{project.description}}"><span
                        ng-bind-html="project.description | highlightText: freeTextQuery"></span>
                </span>
            </td>
            <td style="width: 150px">
                <span ng-bind-html="project.projectManagerObject.fullName | highlightText: selectedPerson.fullName"></span>
            </td>
            <td>
                <div ng-if="project.percentComplete < 100"
                     class="project-progress progress text-center">
                    <div style="width:{{project.percentComplete}}%"
                         class="progress-bar progress-bar-primary progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 2px;">{{project.percentComplete}}%</span>
                    </div>
                </div>
                <div ng-if="project.percentComplete == 100"
                     class="project-progress progress text-center">
                    <div style="width:{{project.percentComplete}}%"
                         class="progress-bar progress-bar-success progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 10px;">{{project.percentComplete}}%</span>
                    </div>
                </div>
            </td>

            <td style="width: 150px">{{project.plannedStartDate}}</td>

            <%--<td ng-if="currentLang == 'de'" style="width: 150px">{{project.plannedStartDatde}}</td>--%>
            <td style="width: 150px">{{project.plannedFinishDate}}</td>

            <%--<td ng-if="currentLang == 'de'" style="width: 150px">{{project.plannedFinishDatede}}</td>--%>
            <td style="width: 150px">{{project.actualStartDate}}</td>

            <td style="width: 150px">{{project.actualFinishDate}}</td>

            <td class="added-column" ng-repeat="objectAttribute in allProjectsVm.selectedAttributes">
                <all-view-attributes object="project"
                                     object-attribute="objectAttribute"></all-view-attributes>
            </td>
            <td class="text-center" style="width: 100px;text-align: center">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="hasPermission('project','edit')"
                                    ng-class="{'disabled':project.percentComplete == 100}"
                                    title={{allProjectsVm.editProjectTitle}}
                                    ng-click="allProjectsVm.editProject(project)">
                                    <a href="" translate>EDIT_PROJECTS</a>
                                </li>
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(project.id,'PROJECT')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'PROJECT'" object="project.id"
                                             tags-count="project.tagsCount"></tags-button>
                                <li ng-if="hasPermission('project','delete')"
                                    ng-class="{'disabled':project.percentComplete > 0}"
                                    title={{allProjectsVm.deleteProjectTitle}}
                                    ng-click="allProjectsVm.deleteProject(project)">
                                    <a href="" translate>DELETE_PROJECT_TITLE</a>
                                </li>
                                <plugin-table-actions context="project.all"
                                                      object-value="project"></plugin-table-actions>
                            </ul>
                        </span>
            </td>


        </tr>
        </tbody>
    </table>
</div>