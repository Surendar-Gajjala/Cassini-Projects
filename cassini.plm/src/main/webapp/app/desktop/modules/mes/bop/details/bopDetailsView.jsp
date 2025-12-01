<div class="view-container" fitcontent>
    <style>
        .item-details-tabs .tab-content {
            padding: 0 !important;
        }

        #freeTextSearchDirective {
            top: 7px !important;
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

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.bop.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-default" title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}"
                    ng-click="bopDetailsVm.showBOPRevisionHistory()">
                <i class="fa fa-history" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="bopDetailsVm.tabs.plan.active" title="Validate Resources and Parts"
                    class="btn btn-sm btn-success" ng-click="showValidatePlanResources()">
                <i class="fa fa-shield"></i>
            </button>
            <button class="btn btn-sm btn-success" title="{{bopDetailsVm.reviseBopTitle}}"
                    ng-if="bopDetailsVm.bopRevision.released"
                    ng-click="bopDetailsVm.reviseBOP()">
                <i class="fa fa-random"></i>
            </button>
        </div>

        <button class="btn btn-sm btn-success"
                title="{{bopDetailsVm.addWorkflowTitle}}"
                ng-show="bopDetailsVm.tabs.workflow.active && bopRevision.workflow == null"
                ng-click="bopDetailsVm.addWorkflow()">
            <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
        </button>
        <button ng-if="bopDetailsVm.tabs.workflow.active && bopRevision.workflow != null && bopRevision.startWorkflow != true"
                ng-show="bopRevision.workflow != null && !bopRevision.workflowStarted"
                title="{{bopDetailsVm.changeWorkflowTitle}}"
                class="btn btn-sm btn-success" ng-click="bopDetailsVm.changeWorkflow()">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(bopDetailsVm.bopId,'BOP')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="bopDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy" style="font-size: 16px;"></i>
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
                <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                    ({{clipBoardObjectFiles.length}})</a></li>
            </ul>
        </div>
        <free-text-search ng-if="bopDetailsVm.tabs.files.active" on-clear="bopDetailsVm.onClear"
                          on-search="bopDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="bopDetailsVm.active">
                        <uib-tab heading="{{bopDetailsVm.tabs.basic.heading}}"
                                 active="bopDetailsVm.tabs.basic.active"
                                 select="bopDetailsVm.tabActivated(bopDetailsVm.tabs.basic.id)">
                            <div ng-include="bopDetailsVm.tabs.basic.template"
                                 ng-controller="BOPBasicInfoController as bopBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{bopDetailsVm.tabs.plan.heading}}"
                                 active="bopDetailsVm.tabs.plan.active"
                                 select="bopDetailsVm.tabActivated(bopDetailsVm.tabs.plan.id)">
                            <div ng-include="bopDetailsVm.tabs.plan.template"
                                 ng-controller="BOPPlanController as bopPlanVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{bopDetailsVm.tabs.files.heading}}"
                                 active="bopDetailsVm.tabs.files.active"
                                 select="bopDetailsVm.tabActivated(bopDetailsVm.tabs.files.id)">
                            <div ng-include="bopDetailsVm.tabs.files.template"
                                 ng-controller="BOPFilesController as bopFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow"
                                 heading="{{bopDetailsVm.tabs.workflow.heading}}"
                                 active="bopDetailsVm.tabs.workflow.active"
                                 select="bopDetailsVm.tabActivated(bopDetailsVm.tabs.workflow.id)">
                            <div ng-include="bopDetailsVm.tabs.workflow.template"
                                 ng-controller="BOPWorkflowController as bopWfVm"></div>
                        </uib-tab>
                        <uib-tab id="" heading="{{bopDetailsVm.tabs.timelineHistory.heading}}"
                                 active="bopDetailsVm.tabs.timelineHistory.active"
                                 select="bopDetailsVm.tabActivated(bopDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="bopDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="BOPTimeLineController as bopTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
