<style>
    .project-progress.progress {
        background-color: #B0C7CF;
        height: 25px !important;
        margin: 0 !important;
    }

    .tab-content {
        padding: 0px !important;
    }

    .tab-content .tab-pane {
        overflow: auto !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 260px;
    }

    .tab-pane {
        position: relative;
    }

    .tab-content .tab-pane .responsive-table {
        height: 100%;
        position: absolute;
        overflow: auto !important;
        padding: 5px;
    }

    .tab-content .tab-pane #gantt_here {
        height: 100%;
        position: absolute;
    }

    .tab-content .tab-pane .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px !important;
        z-index: 5;
    }

</style>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="programDetailsVm.showAllPrograms()"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button ng-if="programDetailsPermission"
                    class="btn btn-sm btn-warning" ng-click="programDetailsVm.createNewTemplate()"
                    title="{{programDetailsVm.saveProjectTemplate}}">
                <i class="fa fa-wpforms" aria-hidden="true"></i>
            </button>
            <button ng-if="programDetailsVm.tabs.files.active && hasFiles == true && programDetailsPermission"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                <i class="fa fa-download" aria-hidden="true"></i>
            </button>

            <button class="btn btn-default btn-sm"
                    ng-click="programDetailsVm.refreshDetails()"
                    title="{{programDetailsVm.refreshTitle}}">
                <i class="fa fa-refresh" style="margin-top:-6px;"></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="programDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0 && programDetailsPermission"
                    ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>

            <button class="btn btn-default btn-sm"
                    ng-show="programDetailsVm.tabs.files.active && showShareAllFileAndFoldersToUsers && programDetailsPermission"
                    ng-click="shareAllFilesAndFoldersToUsers()" title="Share files & folders">
                <i class="fa fa-share-alt" style="font-size: 16px;"></i>
            </button>
            <button class="btn btn-sm btn-default"
                    ng-click="shareProgram(programInfo)"
                    ng-if="hasPermission('program','edit') && external.external== false"
                    title="{{programDetailsVm.detailsShareTitle}}">
                <i class="las la-share" style=""></i></button>
            <div class="btn-group"
                 ng-show="programDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0 && programDetailsPermission">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href="" translate>CLEAR_AND_ADD_FILES</a>
                    </li>
                    <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                        ({{clipBoardObjectFiles.length}})</a></li>
                </ul>
            </div>

            <button
                    ng-if="programInfo.workflow != null && programInfo.startWorkflow != true && programDetailsVm.tabs.workflow.active && programDetailsPermission"
                    title="{{programDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="programDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{programDetailsVm.addWorkflowTitle}}"
                    ng-if="programDetailsVm.tabs.workflow.active && programInfo.workflow == null && programDetailsPermission"
                    ng-click="programDetailsVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>

            <button class="btn btn-sm btn-success"
                    ng-if="programDetailsPermission"
                    title="{{programDetailsVm.programCounts.tasks > 0 ? programDetailsVm.sendNotificationTitle : programDetailsVm.notTasksNotificationTitle}}"
                    ng-click="programDetailsVm.sendTasksNotification()"
                    ng-disabled="programDetailsVm.programCounts.tasks == 0">
                <i class="fa fa-envelope-o" aria-hidden="true" style=""></i>
            </button>

            <%--<button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm" ng-click="showPrintOptions(programDetailsVm.projectId,'PROJECT')">
                    <i class="fa fa-print" aria-hidden="true" style=""></i>
                </button>--%>
        </div>

        <div class="pull-right" style="width: 250px;margin-right: 10px;padding-top: 5px;">
            <free-text-search ng-show="programDetailsVm.tabs.files.active"
                              on-clear="programDetailsVm.onClear" search-term="freeTextQuerys"
                              on-search="programDetailsVm.freeTextSearch"></free-text-search>
            <%--<div class="project-progress progress text-center" ng-hide="programDetailsVm.tabs.files.active"
                 ng-if="programDetailsVm.program.percentComplete < 100">
                <div style="width:{{programDetailsVm.program.percentComplete}}%"
                     class="progress-bar progress-bar-primary progress-bar-striped active"
                     role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                    <span style="margin-left: 10px;line-height: 25px !important;">{{programDetailsVm.program.percent}}</span>
                </div>
            </div>
            <div class="project-progress progress text-center"
                 ng-if="programDetailsVm.program.percentComplete == 100">
                <div style="width:{{programDetailsVm.program.percentComplete}}%"
                     class="progress-bar progress-bar-success progress-bar-striped active"
                     role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                    <span style="margin-left: 10px;line-height: 25px !important;">{{programDetailsVm.program.percent}}</span>
                </div>
            </div>--%>
        </div>

    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="programDetailsVm.active">
                        <uib-tab heading="{{programDetailsVm.tabs.basic.heading}}"
                                 active="programDetailsVm.tabs.basic.active"
                                 select="programDetailsVm.programDetailsTabActivated(programDetailsVm.tabs.basic.id)">
                            <div ng-include="programDetailsVm.tabs.basic.template"
                                 ng-controller="ProgramBasicController as programBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="program-resources" heading="{{'RESOURCES' | translate}}"
                                 ng-show="programDetailsVm.hasDisplayTab('resources')"
                                 select="programDetailsVm.programDetailsTabActivated(programDetailsVm.tabs.resources.id)"
                                 active="programDetailsVm.tabs.resources.active">
                            <div ng-include="programDetailsVm.tabs.resources.template"
                                 ng-controller="ProgramResourcesController as programResourcesVm"></div>
                        </uib-tab>
                        <uib-tab id="program-projects" heading="{{programDetailsVm.tabs.project.heading}}"
                                 active="programDetailsVm.tabs.project.active"
                                 select="programDetailsVm.programDetailsTabActivated(programDetailsVm.tabs.project.id)">
                            <div ng-include="programDetailsVm.tabs.project.template"
                                 ng-controller="ProgramProjectsDrillDownController as programProjectsDrillDownVm"></div>
                        </uib-tab>
                        <uib-tab id="program-files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 ng-show="programDetailsVm.hasDisplayTab('files')"
                                 active="programDetailsVm.tabs.files.active"
                                 select="programDetailsVm.programDetailsTabActivated(programDetailsVm.tabs.files.id)">
                            <div ng-include="programDetailsVm.tabs.files.template"
                                 ng-controller="ProgramFilesController as programFilesVm"></div>
                        </uib-tab>

                        <uib-tab id="workflow" heading="{{programDetailsVm.tabs.workflow.heading}}"
                                 ng-show="programDetailsVm.hasDisplayTab('workflow')"
                                 active="programDetailsVm.tabs.workflow.active"
                                 select="programDetailsVm.programDetailsTabActivated(programDetailsVm.tabs.workflow.id)">
                            <div ng-include="programDetailsVm.tabs.workflow.template"
                                 ng-controller="ProgramWorkflowController as programWorkflowVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="programDetailsVm.tabs" custom-tabs="programDetailsVm.customTabs"
                                     object-value="programDetailsVm.program" tab-id="programDetailsVm.tabId"
                                     active="programDetailsVm.active"></plugin-tabs>
                        <uib-tab id="projectHistory" heading="{{programDetailsVm.tabs.timeline.heading}}"
                                 active="programDetailsVm.tabs.timeline.active"
                                 select="programDetailsVm.programDetailsTabActivated(programDetailsVm.tabs.timeline.id)">
                            <div ng-include="programDetailsVm.tabs.timeline.template"
                                 ng-controller="ProgramTimelineController as programTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>


        </div>
    </div>
</div>

