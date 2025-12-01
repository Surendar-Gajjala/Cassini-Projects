<div>
    <style scoped>
        .activity-progress.progress {
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

        .tab-pane {
            position: relative;
        }

        .tab-content .tab-pane .responsive-table {
            height: 100%;
            position: absolute;
            overflow: auto !important;
            padding: 5px;
        }

        .tab-content .tab-pane .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px !important;
            z-index: 5;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

        #freeTextSearchDirective i.fa-times-circle {
            margin-left: -20px !important;
        }

    </style>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm btn-default"
                        ng-if="loginPersonDetails.external == false && taskDetailsVm.fromMyTaskWidget == null"
                        ng-click="taskDetailsVm.showProject()"
                        title="{{taskDetailsVm.showAll}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
                <button class="btn btn-sm btn-default"
                        ng-click="taskDetailsVm.shareProjectTask()"
                        style=""
                        ng-if="loginPersonDetails.external == false && !projectShared"
                        title="{{taskDetailsVm.detailsShareTitle}}">
                    <i class="las la-share" style=""></i></button>
                <%--   <button ng-if="taskDetailsVm.tabs.files.active && hasPermission('project','edit')"
                           title="{{taskDetailsVm.addFiles}}" ng-disabled="taskPercentage == 100"
                           class="btn btn-sm btn-success min-width" ng-click="addTaskFiles()" translate>
                       DETAILS_ADD_FILES
                   </button>--%>
                <%--<button ng-if="taskDetailsVm.tabs.deliverables.active && hasPermission('project','edit')"
                        title="{{'DETAILS_TAB_DELIVERABLES' | translate}}" ng-disabled="taskPercentage == 100"
                        class="btn btn-sm btn-success min-width" ng-click="addTaskDeliverable()" translate>
                    ADD_DELIVERABLE
                </button>--%>
                <%--<button ng-if="taskDetailsVm.tabs.itemReferences.active && hasPermission('project','edit')"
                        ng-disabled="taskPercentage == 100"
                        class="btn btn-sm btn-success min-width" ng-click="addTaskReferenceItems()" translate>
                    ADD_ITEM
                </button>--%>
                <button ng-if="hasPermission('item','edit') && taskComplete == true"
                        ng-hide="taskPercentage == 100"
                        title="{{finishTask}}"
                        class="btn btn-sm btn-success" ng-click="taskDetailsVm.complateTask()">
                    <i class="fa fa-flag-checkered" aria-hidden="true"></i>
                </button>
                <button ng-if="taskDetailsVm.tabs.files.active && hasPermission('item','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true"></i>
                </button>

                <button class="btn btn-default btn-sm"
                        ng-show="taskDetailsVm.tabs.files.active && showShareAllFileAndFoldersToUsers"
                        ng-click="shareAllFilesAndFoldersToUsers()" title="Share files & folders">
                    <i class="fa fa-share-alt" style="font-size: 16px;"></i>
                </button>

                <button class="btn btn-default btn-sm"
                        ng-show="taskDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style="font-size: 16px;"></i>
                </button>
                <div class="btn-group"
                     ng-show="taskDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href="" translate>CLEAR_AND_ADD_FILES</a>
                        </li>
                        <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span
                                translate>ADD_TO_EXISTING_FILES</span> ({{clipBoardObjectFiles.length}})</a></li>
                    </ul>
                </div>

                <button class="btn btn-default btn-sm"
                        ng-show="showCopyDeliverablesToClipBoard && objectDeliverables == false"
                        ng-click="copyDelivarablesToClipBoard()" title="Copy Deliverables to Clipboard">
                    <i class="fa fa-copy" style="font-size: 16px;"></i>
                </button>
                <div class="btn-group" ng-show="showCopyDeliverablesToClipBoard && objectDeliverables == true">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopyDelivarablesToClipBoard()">
                            <a href="">Clear and Add Deliverables</a></li>
                        <li ng-click="copyDelivarablesToClipBoard()"><a href="">Add to Existing Deliverables</a>
                        </li>
                    </ul>
                </div>

                <button ng-if="taskDetailsVm.tabs.workflow.active"
                        ng-show="task.workflow != null && task.startWorkflow != true"
                        title="{{taskDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="taskDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
                <button class="btn btn-sm btn-success"
                        title="{{taskDetailsVm.addWorkflowTitle}}"
                        ng-show="taskDetailsVm.tabs.workflow.active && task.workflow == null && task.status != 'FINISHED'"
                        ng-click="taskDetailsVm.addWorkflow()">
                    <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
                </button>

            </div>
            <div class="pull-right" style="width: 250px;margin-right: 10px;height: 10px !important;padding-top: 5px;">
                <free-text-search ng-if="taskDetailsVm.tabs.files.active" on-clear="taskDetailsVm.onClear"
                                  on-search="taskDetailsVm.freeTextSearch"></free-text-search>
                <div class="activity-progress progress text-center" ng-hide="taskDetailsVm.tabs.files.active"
                     ng-if="taskPercentage < 100">
                    <div style="width:{{taskPercentage}}%"
                         class="progress-bar progress-bar-primary progress-bar-striped active" title="Percent Completed"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 10px;line-height: 25px !important;">{{taskPercentage}}%</span>
                    </div>
                </div>
                <div class="activity-progress progress text-center" ng-hide="taskDetailsVm.tabs.files.active"
                     ng-if="taskPercentage == 100">
                    <div style="width:{{taskPercentage}}%"
                         class="progress-bar progress-bar-success progress-bar-striped active" title="Percent Completed"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 10px;line-height: 25px !important;">{{taskPercentage}}%</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <uib-tabset active="taskDetailsVm.active">
                            <uib-tab heading="{{taskDetailsVm.tabs.basic.heading}}"
                                     active="taskDetailsVm.tabs.basic.active"
                                     select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.basic.id)">
                                <div ng-include="'app/desktop/modules/pm/project/task/details/basic/taskBasicInfoView.jsp'"
                                     ng-controller="TaskBasicInfoController as taskBasicVm"></div>
                            </uib-tab>
                            <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                     active="taskDetailsVm.tabs.files.active"
                                     ng-show="taskDetailsVm.hasDisplayTab('files')"
                                     select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.files.id)">
                                <div ng-include="taskDetailsVm.tabs.files.template"
                                     ng-controller="TaskFilesController as taskFilesVm"></div>
                            </uib-tab>
                            <uib-tab id="deliverables" heading="{{'DETAILS_TAB_DELIVERABLES' | translate}}"
                                     active="taskDetailsVm.tabs.deliverables.active"
                                     ng-show="taskDetailsVm.hasDisplayTab('deliverables')"
                                     select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.deliverables.id)">
                                <div ng-include="taskDetailsVm.tabs.deliverables.template"
                                     ng-controller="TaskDeliverableController as taskDeliverableVm"></div>
                            </uib-tab>
                            <uib-tab id="referenceItems" heading="{{taskDetailsVm.tabs.itemReferences.heading}}"
                                     active="taskDetailsVm.tabs.itemReferences.active"
                                     ng-show="taskDetailsVm.hasDisplayTab('referenceItems')"
                                     select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.itemReferences.id)">
                                <div ng-include="taskDetailsVm.tabs.itemReferences.template"
                                     ng-controller="TaskReferenceItemsController as taskReferenceItemVm"></div>
                            </uib-tab>
                            <uib-tab id="workflow" heading="{{taskDetailsVm.tabs.workflow.heading}}"
                                     active="taskDetailsVm.tabs.workflow.active"
                                     ng-show="taskDetailsVm.hasDisplayTab('workflow')"
                                     select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.workflow.id)">
                                <div ng-include="taskDetailsVm.tabs.workflow.template"
                                     ng-controller="TaskWorkflowController as taskWorkflowVm"></div>
                            </uib-tab>
                            <uib-tab id="taskHistory" heading="{{taskDetailsVm.tabs.taskHistory.heading}}"
                                     active="taskDetailsVm.tabs.taskHistory.active"
                                     select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.taskHistory.id)">
                                <div ng-include="taskDetailsVm.tabs.taskHistory.template"
                                     ng-controller="TaskHistoryController as taskHistoryVm"></div>
                            </uib-tab>
                        </uib-tabset>
                    </div>
                </div>


            </div>
        </div>
    </div>
</div>
