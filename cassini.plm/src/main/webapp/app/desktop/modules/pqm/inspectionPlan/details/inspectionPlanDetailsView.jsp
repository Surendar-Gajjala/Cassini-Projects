<div>
    <style scoped>
        .item-number {
            display: inline-block;
        }

        .item-rev {
            font-size: 16px;
            font-weight: normal;
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
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.pqm.inspectionPlan.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}"
                        ng-click="inspectionPlanDetailsVm.showPlanRevisionHistory()">
                    <i class="fa fa-history" aria-hidden="true" style=""></i>
                </button>
                <button class="btn btn-sm btn-success" title="{{inspectionPlanDetailsVm.newRevisionTitle}}"
                        ng-if="(inspectionPlanRevision.released || inspectionPlanRevision.rejected) && inspectionPlanRevision.id == inspectionPlan.latestRevision"
                        ng-click="inspectionPlanDetailsVm.reviseInspectionPlan()">
                    <i class="fa fa-random"></i>
                </button>
                <button ng-if="inspectionPlanDetailsVm.tabs.files.active && hasPermission('inspectionplan','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="inspectionPlanDetailsVm.tabs.workflow.active"
                        ng-show="inspectionPlanRevision.startWorkflow != true"
                        title="{{inspectionPlanDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="inspectionPlanDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
                <button class="btn btn-default btn-sm"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style="font-size: 16px;"></i>
                </button>
                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm"
                        ng-click="showPrintOptions(inspectionPlanDetailsVm.planId, inspectionPlanRevision.plan.objectType)">
                    <i class="fa fa-print" aria-hidden="true" style=""></i>
                </button>
                <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href=""
                                                                               translate>CLEAR_AND_ADD_FILES</a>
                        </li>
                        <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span
                                translate>ADD_TO_EXISTING_FILES</span> ({{clipBoardObjectFiles.length}})</a></li>
                    </ul>
                </div>
            </div>
            <free-text-search ng-if="inspectionPlanDetailsVm.tabs.files.active"
                              on-clear="inspectionPlanDetailsVm.onClear"
                              on-search="inspectionPlanDetailsVm.freeTextSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                            <uib-tabset active="inspectionPlanDetailsVm.active">
                                <uib-tab heading="{{inspectionPlanDetailsVm.tabs.basic.heading}}"
                                         active="inspectionPlanDetailsVm.tabs.basic.active"
                                         select="inspectionPlanDetailsVm.planDetailsTabActivated(inspectionPlanDetailsVm.tabs.basic.id)">
                                    <div ng-include="inspectionPlanDetailsVm.tabs.basic.template"
                                         ng-controller="PlanBasicInfoController as planBasicVm"></div>
                                </uib-tab>

                                <%--<uib-tab heading="{{inspectionPlanDetailsVm.tabs.attributes.heading}}"
                                         active="inspectionPlanDetailsVm.tabs.attributes.active"
                                         select="inspectionPlanDetailsVm.planDetailsTabActivated(inspectionPlanDetailsVm.tabs.attributes.id)">
                                    <div ng-include="inspectionPlanDetailsVm.tabs.attributes.template"
                                         ng-controller="PlanAttributesController as planAttributesVm"></div>
                                </uib-tab>--%>
                                <uib-tab id="checklist"
                                         heading="{{inspectionPlanDetailsVm.tabs.checklist.heading}}"
                                         active="inspectionPlanDetailsVm.tabs.checklist.active"
                                         select="inspectionPlanDetailsVm.planDetailsTabActivated(inspectionPlanDetailsVm.tabs.checklist.id)">
                                    <div ng-include="inspectionPlanDetailsVm.tabs.checklist.template"
                                         ng-controller="PlanChecklistController as planChecklistVm"></div>
                                </uib-tab>
                                <uib-tab id="workflow"
                                         heading="{{inspectionPlanDetailsVm.tabs.workflow.heading}}"
                                         active="inspectionPlanDetailsVm.tabs.workflow.active"
                                         select="inspectionPlanDetailsVm.planDetailsTabActivated(inspectionPlanDetailsVm.tabs.workflow.id)">
                                    <div ng-include="inspectionPlanDetailsVm.tabs.workflow.template"
                                         ng-controller="PlanWorkflowController as planWfVm"></div>
                                </uib-tab>
                                <uib-tab id="files"
                                         heading="{{inspectionPlanDetailsVm.tabs.files.heading}}"
                                         active="inspectionPlanDetailsVm.tabs.files.active"
                                         select="inspectionPlanDetailsVm.planDetailsTabActivated(inspectionPlanDetailsVm.tabs.files.id)">
                                    <div ng-include="inspectionPlanDetailsVm.tabs.files.template"
                                         ng-controller="PlanFilesController as planFilesVm"></div>
                                </uib-tab>
                                <plugin-tabs tabs="inspectionPlanDetailsVm.tabs" custom-tabs="inspectionPlanDetailsVm.customTabs"
                                             object-value="inspectionPlanDetailsVm.inspectionPlan" tab-id="inspectionPlanDetailsVm.tabId" active="inspectionPlanDetailsVm.active"></plugin-tabs>
                                <uib-tab id="timeline"
                                         heading="{{inspectionPlanDetailsVm.tabs.timelineHistory.heading}}"
                                         active="inspectionPlanDetailsVm.tabs.timelineHistory.active"
                                         select="inspectionPlanDetailsVm.planDetailsTabActivated(inspectionPlanDetailsVm.tabs.timelineHistory.id)">
                                    <div ng-include="inspectionPlanDetailsVm.tabs.timelineHistory.template"
                                         ng-controller="PlanTimelineHistoryController as planTimelineHistoryVm"></div>
                                </uib-tab>
                            </uib-tabset>
                        </scrollable-tabset>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>