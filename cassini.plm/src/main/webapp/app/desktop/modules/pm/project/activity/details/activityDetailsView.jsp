<style>
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

</style>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-if="external.external == false"
                    ng-click="activityDetailsVm.showProject()"
                    title="{{activityDetailsVm.showAll}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button ng-if="activityDetailsVm.tabs.tasks.active && hasPermission('project','edit')"
                    class="btn btn-sm btn-success" ng-click="showTaskTypeAttributes()"
                    title="{{activityDetailsVm.showTaskAttributes}}">
                <i class="fa fa-newspaper-o" style="" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-default"
                    ng-if="loginPersonDetails.external == false && !projectShared"
                    ng-click="activityDetailsVm.shareProjectActivity()"
                    style=""
                    title="{{activityDetailsVm.detailsShareTitle}}">
                <i class="las la-share" style=""></i></button>
            <%-- <button ng-if="activityDetailsVm.tabs.files.active && hasPermission('project','edit')"
                     title="{{activityDetailsVm.addFiles}}" ng-disabled="activityPercent == 100"
                     class="btn btn-sm btn-success min-width" ng-click="addActivityFiles()" translate>
                 DETAILS_ADD_FILES
             </button>--%>
            <%--<button ng-if="activityDetailsVm.tabs.deliverables.active && hasPermission('project','edit')"
                    title="{{'DETAILS_TAB_DELIVERABLES' | translate}}" ng-disabled="activityPercent == 100"
                    class="btn btn-sm btn-success min-width" ng-click="addActivityDeliverable()" translate>
                ADD_DELIVERABLE
            </button>--%>
            <%--<button ng-if="activityDetailsVm.tabs.itemReferences.active && hasPermission('project','edit')"
                    ng-disabled="activityPercent == 100"
                    class="btn btn-sm btn-success min-width" ng-click="addReferenceItems()" translate>
                ADD_ITEM
            </button>--%>
            <button ng-if="activityDetailsVm.tabs.files.active && hasPermission('item','edit') && hasFiles == true"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                <i class="fa fa-download" style="" aria-hidden="true"></i>
            </button>
            <button ng-if="activityDetailsVm.tabs.workflow.active"
                    ng-show="activityInfo.workflow != null && activityInfo.startWorkflow != true"
                    title="{{activityDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="activityDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{activityDetailsVm.addWorkflowTitle}}"
                    ng-show="activityDetailsVm.tabs.workflow.active && activityInfo.workflow == null
                    && activityPercent != 100 && loginPersonDetails.external == false"
                    ng-click="activityDetailsVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="activityDetailsVm.tabs.files.active && showShareAllFileAndFoldersToUsers"
                    ng-click="shareAllFilesAndFoldersToUsers()" title="Share files & folders">
                <i class="fa fa-share-alt" style="font-size: 16px;"></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="activityDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                    ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>
            <div class="btn-group"
                 ng-show="activityDetailsVm.tabs.files.active && showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
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
        </div>
        <div class="pull-right" style="width: 250px;margin-right: 10px;height: 10px !important;padding-top: 5px;">
            <free-text-search ng-if="activityDetailsVm.tabs.files.active" on-clear="activityDetailsVm.onClear"
                              on-search="activityDetailsVm.freeTextSearch"></free-text-search>
            <div class="activity-progress progress text-center" ng-hide="activityDetailsVm.tabs.files.active"
                 ng-if="activityDetailsVm.activityPercentage < 100">
                <div style="width:{{activityDetailsVm.activityPercentage}}%"
                     class="progress-bar progress-bar-primary progress-bar-striped active" title="Percent Completed"
                     role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                    <span style="margin-left: 10px;line-height: 25px !important;">{{activityDetailsVm.activityPercentage}}%</span>
                </div>
            </div>
            <div class="activity-progress progress text-center" ng-hide="activityDetailsVm.tabs.files.active"
                 ng-if="activityDetailsVm.activityPercentage == 100">
                <div style="width:{{activityDetailsVm.activityPercentage}}%"
                     class="progress-bar progress-bar-success progress-bar-striped active" title="Percent Completed"
                     role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                    <span style="margin-left: 10px;line-height: 25px !important;">{{activityDetailsVm.activityPercentage}}%</span>
                </div>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="activityDetailsVm.active">
                        <uib-tab heading="{{activityDetailsVm.tabs.basic.heading}}"
                                 active="activityDetailsVm.tabs.basic.active"
                                 select="activityDetailsVm.activityDetailsTabActivated(activityDetailsVm.tabs.basic.id)">
                            <div ng-include="'app/desktop/modules/pm/project/activity/details/basic/activityBasicInfoView.jsp'"
                                 ng-controller="ActivityBasicInfoController as activityBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 active="activityDetailsVm.tabs.files.active"
                                 ng-show="activityDetailsVm.hasDisplayTab('files')"
                                 select="activityDetailsVm.activityDetailsTabActivated(activityDetailsVm.tabs.files.id)">
                            <div ng-include="activityDetailsVm.tabs.files.template"
                                 ng-controller="ActivityFilesController as activityFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="deliverables" heading="{{'DETAILS_TAB_DELIVERABLES' | translate}}"
                                 active="activityDetailsVm.tabs.deliverables.active"
                                 ng-show="activityDetailsVm.hasDisplayTab('deliverables')"
                                 select="activityDetailsVm.activityDetailsTabActivated(activityDetailsVm.tabs.deliverables.id)">
                            <div ng-include="activityDetailsVm.tabs.deliverables.template"
                                 ng-controller="ActivityDeliverableController as activityDeliverableVm"></div>
                        </uib-tab>
                        <uib-tab id="referenceItems" heading="{{activityDetailsVm.tabs.itemReferences.heading}}"
                                 active="activityDetailsVm.tabs.itemReferences.active"
                                 ng-show="activityDetailsVm.hasDisplayTab('referenceItems')"
                                 select="activityDetailsVm.activityDetailsTabActivated(activityDetailsVm.tabs.itemReferences.id)">
                            <div ng-include="activityDetailsVm.tabs.itemReferences.template"
                                 ng-controller="ActivityReferenceItemsController as activityReferenceItemVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="{{activityDetailsVm.tabs.workflow.heading}}"
                                 active="activityDetailsVm.tabs.workflow.active"
                                 ng-show="activityDetailsVm.hasDisplayTab('workflow')"
                                 select="activityDetailsVm.activityDetailsTabActivated(activityDetailsVm.tabs.workflow.id)">
                            <div ng-include="activityDetailsVm.tabs.workflow.template"
                                 ng-controller="ActivityWorkflowController as activityWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="activityHistory" heading="{{activityDetailsVm.tabs.activityHistory.heading}}"
                                 active="activityDetailsVm.tabs.activityHistory.active"
                                 select="activityDetailsVm.activityDetailsTabActivated(activityDetailsVm.tabs.activityHistory.id)">
                            <div ng-include="activityDetailsVm.tabs.activityHistory.template"
                                 ng-controller="ActivityHistoryController as activityHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>


        </div>
    </div>
</div>

