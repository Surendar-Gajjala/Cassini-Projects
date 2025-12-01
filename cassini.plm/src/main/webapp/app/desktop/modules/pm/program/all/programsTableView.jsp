<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th class="col-width-250" translate>NAME</th>
            <!-- <th class="col-width-250" translate>TYPE</th> -->
            <th>
                <span ng-if="allProgramsVm.selectedProgramType != null"
                      style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                    ({{allProgramsVm.selectedProgramType.name}})
                    <i class="fa fa-times-circle" ng-click="allProgramsVm.clearTypeSelection()"
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
                                    on-select-type="allProgramsVm.onSelectType"
                                    object-type="PROGRAM"></project-type-tree>
                        </div>
                    </div>
                </div>
            </th>
            <th class="description-column" translate>DESCRIPTION</th>
            <th style="width: 150px;z-index: auto !important;">
                <span ng-if="selectedPerson != null"
                      style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                        ({{selectedPerson.fullName}})
                        <i class="fa fa-times-circle" ng-click="clearProgramManager()"
                           title="{{removeTitle}}"></i>
                </span>
                <br>

                <div class="dropdown" uib-dropdown style="display: inline-block">
                    <span uib-dropdown-toggle><span translate>PROGRAM_MANAGER</span>
                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                    </span>
                    <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                        style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                        <li ng-repeat=" person in programManagers"
                            ng-click="onSelectProgramManager(person)"><a
                                href="">{{person.fullName}}</a>
                        </li>
                    </ul>
                </div>
            </th>
            <th style="width: 150px" translate>PROGRAM_PROGRESS</th>
            <th style="width: 150px" translate>MODIFIED_BY</th>
            <th style="width: 150px" translate>MODIFIED_DATE</th>
            <th class='added-column'
                style="width: 150px;"
                ng-repeat="selectedAttribute in allProgramsVm.selectedAttributes">
                {{selectedAttribute.name}}
                <i class="fa fa-times-circle"
                   ng-click="allProgramsVm.removeAttribute(selectedAttribute)"
                   title="{{RemoveColumnTitle}}"></i>
            </th>
            <th style="width: 100px;text-align: center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="allProgramsVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PROGRAMS</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="allProgramsVm.loading == false && allProgramsVm.programs.content.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Project.png" alt="" class="image">

                    <div class="message">{{ 'NO_PROGRAMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>

        </tr>

        <tr ng-repeat="program in allProgramsVm.programs.content">
            <td class="col-width-250">
                <a href="" ng-click="allProgramsVm.showProgram(program)"
                   title="{{allProgramsVm.projectOpenTitle}}"
                   ng-show="hasPermission('program','view')">
                    <span ng-bind-html="program.name | highlightText: freeTextQuery"></span>
                </a>
                <span ng-show="hasPermission('program','view') == false"
                      title="You don't have permission to see program">
                            <span ng-bind-html="program.name | highlightText: freeTextQuery"></span>
                        </span>
            </td>
            <td class="col-width-250">
                <span title="{{program.type}}"><span
                        ng-bind-html="program.type  | highlightText: freeTextQuery"></span>
               </span>
            </td>
            <td class="description-column">
                         <span title="{{program.description}}"><span
                                 ng-bind-html="program.description  | highlightText: freeTextQuery"></span>
                        </span>
            </td>
            <td style="width: 150px">
                <span title="{{program.managerFullName}}"><span
                        ng-bind-html="program.managerFullName  | highlightText: selectedPerson.fullName"></span>
                        </span>
            </td>
            <td style="text-align: center">{{program.percent}}
                <%--<div ng-if="program.percentComplete < 100"
                     class="project-progress progress text-center">
                    <div style="width:{{program.percentComplete}}%"
                         class="progress-bar progress-bar-primary progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 2px;">{{program.percentComplete}}%</span>
                    </div>
                </div>
                <div ng-if="program.percentComplete == 100"
                     class="project-progress progress text-center">
                    <div style="width:{{program.percentComplete}}%"
                         class="progress-bar progress-bar-success progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 10px;">{{program.percentComplete}}%</span>
                    </div>
                </div>--%>
            </td>
            <td>{{program.modifiedByName}}</td>
            <td>{{program.modifiedDate}}</td>
            <td class="added-column" ng-repeat="objectAttribute in allProgramsVm.selectedAttributes">
                <all-view-attributes object="program"
                                     object-attribute="objectAttribute"></all-view-attributes>
            </td>
            <td class="text-center" style="width: 100px;text-align: center">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <%--<li ng-if="hasPermission('program','edit')"
                                    ng-class="{'disabled':program.percentComplete == 100}"
                                    title={{allProgramsVm.editProjectTitle}}
                                    ng-click="allProgramsVm.editProject(program)">
                                    <a href="" translate>EDIT_PROJECTS</a>
                                </li>--%>
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(program.id,'PROGRAM')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'PROGRAM'" object="program.id"
                                             tags-count="program.tagsCount"></tags-button>
                                <li ng-if="hasPermission('program','delete')"
                                    title="{{allProgramsVm.deleteProgramTitle}}"
                                    ng-class="{'disabled':program.started || program.percentComplete > 0}"
                                    ng-click="allProgramsVm.deleteProgram(program)">
                                    <a href="" translate>DELETE_PROGRAM</a>
                                </li>
                                <plugin-table-actions context="program.all"
                                                      object-value="program"></plugin-table-actions>
                            </ul>
                        </span>
            </td>


        </tr>
        </tbody>
    </table>
</div>