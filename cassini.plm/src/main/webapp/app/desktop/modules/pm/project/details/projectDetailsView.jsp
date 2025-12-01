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

    .gantt-freetext input.form-control[type=text], input.form-control[type=password], .ui-select-container input.form-control[type=search] {
        padding: 7.5px 27px;
    }

</style>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="pull-right gantt-freetext">
    <free-text-search ng-show="projectDetailsVm.tabs.files.active || projectDetailsVm.tabs.plan.active"
                      on-clear="projectDetailsVm.onClear"
                      on-search="projectDetailsVm.freeTextSearch"></free-text-search>
</div>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="projectDetailsVm.showAllProjects()"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

            <%--<button ng-if="projectDetailsVm.tabs.plan.active && showActivityAndMilestone == true && hasPermission('project','edit')"
                    class="btn btn-sm btn-primary min-width" ng-click="addActivity()" translate>
                ADD_ACTIVITY
            </button>
            <button ng-if="projectDetailsVm.tabs.plan.active && showActivityAndMilestone == true && hasPermission('project','edit')"
                    class="btn btn-sm btn-warning min-width" ng-click="addMilestone()" translate>
                ADD_MILESTONE
            </button>
            <button ng-if="projectDetailsVm.tabs.plan.active && showActivityAndMilestone == true && hasPermission('project','edit')"
                    class="btn btn-sm btn-warning min-width" ng-click="createDuplicateWbs()" translate>
                COPY_WBS
            </button>--%>
            <button ng-if="projectDetailsVm.tabs.plan.active && projectDetailsPermission"
                    class="btn btn-sm btn-warning" ng-click="createNewTemplate()"
                    title="{{projectDetailsVm.saveProjectTemplate}}">
                <i class="fa fa-wpforms" aria-hidden="true"></i>
            </button>
            <button ng-if="projectDetailsVm.tabs.files.active && projectDetailsPermission && hasFiles == true"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                <i class="fa fa-download" aria-hidden="true"></i>
            </button>

            <button class="btn btn-default btn-sm"
                    ng-click="projectDetailsVm.refreshDetails()"
                    title="{{projectDetailsVm.refreshTitle}}">
                <i class="fa fa-refresh" style="font-size: 16px;margin-top:-6px;"></i>
            </button>

            <div class="btn-group"
                 ng-if="projectDetailsVm.tabs.files.active && showShareAllFileAndFoldersToUsers && projectDetailsPermission ">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-share-alt" style="font-size: 16px;" title="Share files"></span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="shareAllFilesAndFoldersToUsers('PERSON')"><a href="" translate>Share to users</a></li>
                    <li ng-click="shareAllFilesAndFoldersToUsers('PROJECT')"><a href="">Share to projects</a></li>
                </ul>
            </div>

            <button class="btn btn-default btn-sm"
                    ng-if="projectDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0 && projectDetailsPermission"
                    ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>

            <button class="btn btn-sm btn-default"
                    ng-click="shareProject(projectInfo)"
                    ng-if="projectDetailsPermission"
                    title="{{projectDetailsVm.detailsShareTitle}}">
                <i class="las la-share" style=""></i>
            </button>
            <div class="btn-group"
                 ng-if="projectDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0 && projectDetailsPermission">
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

            <div class="btn-group" ng-if="projectDetailsVm.tabs.plan.active">

                <button class="btn btn-sm btn-default"
                        title="{{save}}"
                        ng-if="projectDetailsPermission"
                        ng-click="projectDetailsVm.saveGantt()">
                    <i class="fa fa-save" style=""></i>
                </button>
                <button class="btn btn-sm btn-default"
                        title="{{expandAll}}"
                        ng-click="projectDetailsVm.expandAll()"
                        ng-if="projectDetailsVm.expand">
                    <i class="fa fa-expand" style=""></i>
                </button>
                <button class="btn btn-sm btn-default"
                        title="{{collapseAll}}"
                        ng-click="projectDetailsVm.collapseAll()"
                        ng-if="!projectDetailsVm.expand">
                    <i class="fa fa-compress" style=""></i>
                </button>
                <button id="showGantt"
                        class="btn btn-sm btn-default"
                        style="margin-top: 1px;"
                        title="{{showGantt ? 'Hide Gantt' : 'Show Gantt'}}"
                        ng-click="projectDetailsVm.toggleGrid(showGantt)">
                    <i class="fa fa-list" style=""></i>
                </button>
                <button class="btn btn-sm btn-default"
                        style="margin-top: 1px;"
                        title="{{projectDetailsVm.zoomed ? 'Reset Zoom' : 'Zoom to Fit'}}"
                        ng-click="projectDetailsVm.toggleMode()"
                        ng-disabled="!showGantt">
                    <!-- {{projectDetailsVm.zoomed ? 'Reset Zoom' : 'Zoom to Fit'}} -->
                    <i class="fa fa-search-plus" style=""></i>
                </button>
                <button class="btn btn-sm btn-default"
                        style="margin-top: 1px;"
                        title="{{projectDetailsVm.exportGanttMessage}}"
                        ng-click="projectDetailsVm.exportGantt()">
                    <i class="fa fa-file-excel-o" aria-hidden="true" style=""></i>
                </button>
            </div>

            <button ng-if="projectDetailsVm.tabs.workflow.active && projectInfo.workflow != null && projectInfo.startWorkflow != true && projectDetailsPermission"
                    title="{{projectDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="projectDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{projectDetailsVm.addWorkflowTitle}}"
                    ng-if="projectDetailsVm.tabs.workflow.active && projectInfo.workflow == null && projectPercentComplete < 100 && projectDetailsPermission"
                    ng-click="projectDetailsVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>

            <button class="btn btn-sm btn-success"
                    ng-if="projectDetailsPermission"
                    title="{{projectDetailsVm.projectCounts.tasks > 0 ? projectDetailsVm.sendNotificationTitle : projectDetailsVm.notTasksNotificationTitle}}"
                    ng-click="projectDetailsVm.sendProjectTasksNotification()"
                    ng-disabled="projectDetailsVm.projectCounts.tasks == 0">
                <i class="fa fa-envelope-o" aria-hidden="true" style=""></i>
            </button>

            <button class="btn btn-default btn-sm"
                    ng-show="showCopyDeliverablesToClipBoard && objectDeliverables == false && projectDetailsPermission"
                    ng-click="copyDelivarablesToClipBoard()" title="Copy Files to Clipboard">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>
            <div class="btn-group"
                 ng-show="showCopyDeliverablesToClipBoard && objectDeliverables == true && projectDetailsPermission">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyDelivarablesToClipBoard()">
                        <a href="">Clear and Add Deliverables</a></li>
                    <li ng-click="copyDelivarablesToClipBoard()"><a href="">Add to Existing Deliverables</a></li>
                </ul>
            </div>
            <button
                    title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                    class="btn btn-sm" ng-click="showPrintOptions(projectDetailsVm.projectId,'PROJECT')">
                <i class="fa fa-print" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="ganttData.length == 0 && projectTemplates.length > 0 && projectDetailsVm.tabs.plan.active && projectDetailsPermission"
                    class="btn btn-sm btn-warning" ng-click="addNewTemplate()"
                    title="{{projectDetailsVm.addProjectTemplate}}">
                <i class="fa fa-download" aria-hidden="true"></i>
            </button>

            <div class="btn-group" ng-show="showGantt && projectDetailsVm.tabs.plan.active" style="width: 220px;">
                <div class="switch-toggle switch-candy">
                    <input id="day" name="scale" type="radio" checked
                           ng-click="projectDetailsVm.setScales('day', $event)">
                    <label for="day" onclick="" translate>DAY</label>

                    <input id="week" name="scale" type="radio"
                           ng-click="projectDetailsVm.setScales('week', $event)">
                    <label for="week" onclick="" translate>WEEK</label>

                    <input id="month" name="scale" type="radio"
                           ng-click="projectDetailsVm.setScales('month', $event)">
                    <label for="month" onclick="" translate>MONTH</label>

                    <input id="year" name="scale" type="radio"
                           ng-click="projectDetailsVm.setScales('year', $event)">
                    <label for="year" onclick="" translate>YEAR</label>
                    <a href=""></a>
                </div>
            </div>
        </div>
        <div class="pull-right" style="width: 250px;margin-right: 10px;padding-top: 5px;"
             ng-hide="projectDetailsVm.tabs.files.active">
            <div class="project-progress progress text-center"
                 ng-if="projectDetailsVm.projectPercentage < 100">
                <div style="width:{{projectDetailsVm.projectPercentage}}%"
                     class="progress-bar progress-bar-primary progress-bar-striped active"
                     role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                    <span style="margin-left: 10px;line-height: 25px !important;">{{projectDetailsVm.projectPercentage}}%</span>
                </div>
            </div>
            <div class="project-progress progress text-center" ng-if="projectDetailsVm.projectPercentage == 100">
                <div style="width:{{projectDetailsVm.projectPercentage}}%"
                     class="progress-bar progress-bar-success progress-bar-striped active"
                     role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                    <span style="margin-left: 10px;line-height: 25px !important;">{{projectDetailsVm.projectPercentage}}%</span>
                </div>
            </div>
        </div>

    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="projectDetailsVm.active">
                        <uib-tab heading="{{projectDetailsVm.tabs.basic.heading}}"
                                 active="projectDetailsVm.tabs.basic.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.basic.id)">
                            <div ng-include="'app/desktop/modules/pm/project/details/tabs/basic/projectBasicView.jsp'"
                                 ng-controller="ProjectBasicController as projectBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="team" heading="{{'TEAM' | translate}}"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.members.id)"
                                 active="projectDetailsVm.tabs.members.active">
                            <div ng-include="'app/desktop/modules/pm/project/details/tabs/team/all/teamView1.jsp'"
                                 ng-controller="TeamController as teamVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{'PLAN' | translate}}" active="projectDetailsVm.tabs.plan.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.plan.id)">
                            <div ng-include="projectDetailsVm.tabs.plan.template"
                                 ng-controller="ProjectPlanController as projectPlanVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 ng-show="projectDetailsVm.hasDisplayTab('files')"
                                 active="projectDetailsVm.tabs.files.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.files.id)">
                            <div ng-include="projectDetailsVm.tabs.files.template"
                                 ng-controller="ProjectFilesController as projectFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="project-reqDocuments" heading="{{'REQUIREMENTS' | translate}}"
                                 ng-show="projectDetailsVm.hasDisplayTab('requirements')"
                                 active="projectDetailsVm.tabs.reqDocuments.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.reqDocuments.id)">
                            <div ng-include="projectDetailsVm.tabs.reqDocuments.template"
                                 ng-controller="ProjectReqDocumentController as projectReqDocumentsVm"></div>
                        </uib-tab>
                        <uib-tab id="deliverables" heading="{{'DETAILS_TAB_DELIVERABLES' | translate}}"
                                 ng-show="projectDetailsVm.hasDisplayTab('deliverables')"
                                 active="projectDetailsVm.tabs.deliverables.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.deliverables.id)">
                            <div ng-include="projectDetailsVm.tabs.deliverables.template"
                                 ng-controller="DeliverablesController as deliverablesVm"></div>
                        </uib-tab>

                        <uib-tab id="referenceItems" heading="{{projectDetailsVm.tabs.itemReferences.heading}}"
                                 ng-show="projectDetailsVm.hasDisplayTab('referenceItems')"
                                 active="projectDetailsVm.tabs.itemReferences.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.itemReferences.id)">
                            <div ng-include="projectDetailsVm.tabs.itemReferences.template"
                                 ng-controller="ProjectItemReferenceController as projectItemReferenceVm"></div>
                        </uib-tab>

                        <uib-tab id="workflow" heading="{{projectDetailsVm.tabs.workflow.heading}}"
                                 ng-show="projectDetailsVm.hasDisplayTab('workflow')"
                                 active="projectDetailsVm.tabs.workflow.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.workflow.id)">
                            <div ng-include="projectDetailsVm.tabs.workflow.template"
                                 ng-controller="ProjectWorkflowController as projectWorkflowVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="projectDetailsVm.tabs" custom-tabs="projectDetailsVm.customTabs"
                                     object-value="projectDetailsVm.project" tab-id="projectDetailsVm.tabId"
                                     active="projectDetailsVm.active"></plugin-tabs>
                        <uib-tab id="projectHistory" heading="{{projectDetailsVm.tabs.projectHistory.heading}}"
                                 active="projectDetailsVm.tabs.projectHistory.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.projectHistory.id)">
                            <div ng-include="projectDetailsVm.tabs.projectHistory.template"
                                 ng-controller="ProjectHistoryController as projectHistoryVm"></div>
                        </uib-tab>

                        <%--                        <uib-tab heading="{{projectDetailsVm.tabs.oldPlan.heading}}"
                                                         active="projectDetailsVm.tabs.oldPlan.active"
                                                         select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.oldPlan.id)">
                                                    <div ng-include="projectDetailsVm.tabs.oldPlan.template"
                                                         ng-controller="OldProjectPlanController as oldProjectPlanVm"></div>
                                                </uib-tab>--%>

                    </uib-tabset>
                </div>
            </div>


        </div>
    </div>
</div>

