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
        overflow: auto;
    }

    .tab-pane {
        position: relative;
    }

    .tab-content .tab-pane .mbom-items-view {
        height: 100%;
        position: absolute;
    }

    .tab-content .tab-pane .mbom-items-view .mbom-items-left,
    .tab-content .tab-pane .mbom-items-view .mbom-items-right {
        height: 100%;
        position: absolute;
    }

    .tab-content .tab-pane .mbom-items-view .mbom-items-left .responsive-table,
    .tab-content .tab-pane .mbom-items-view .mbom-items-right .responsive-table {
        height: calc(100% - 25px);
        position: absolute;
        overflow: auto;
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
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.mbom.all')"
                        title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}"
                        ng-click="mbomDetailsVm.showMBOMRevisionHistory()">
                    <i class="fa fa-history" aria-hidden="true" style=""></i>
                </button>
            </div>
            <button ng-if="mbomDetailsVm.tabs.workflow.active"
                    ng-show="!mbomRevision.workflowStarted"
                    class="btn btn-sm btn-success" ng-click="mbomDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="mbomDetailsVm.tabs.bom.active" title="Validate EBOM"
                    class="btn btn-sm btn-success" ng-click="validateEBOM()">
                <i class="fa fa-shield"></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="showCopyObjectFilesToClipBoard && clipBoardChangesFiles.length == 0"
                    ng-click="copyChangeFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>
            <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardChangesFiles.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyChangeFilesToClipBoard()"><a href=""
                                                                           translate>CLEAR_AND_ADD_FILES</a>
                    </li>
                    <li ng-click="copyChangeFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                        ({{clipBoardObjectFiles.length}})</a></li>
                </ul>
            </div>
            <button ng-if="mbomDetailsVm.tabs.changes.active && mbomRevision.id == mbom.latestRevision && mbomRevision.released"
                    title="{{mbom.pendingMco ? 'MBOM has pending MCO':'Create MCO'}}" class="btn btn-sm btn-success"
                    ng-click="createMBOMMco()" ng-disabled="mbom.pendingMco">
                New MCO
            </button>
            <button ng-if="mbomDetailsVm.tabs.whereUsed.active"
                    title="Create BOP" class="btn btn-sm btn-success" ng-click="createMBOMBOP()">
                New BOP
            </button>
            <div class="pull-right">

            </div>
        </div>
        <free-text-search ng-if="mbomDetailsVm.tabs.files.active" on-clear="mbomDetailsVm.onClear"
                          on-search="mbomDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="mbomDetailsVm.active">
                        <uib-tab heading="{{mbomDetailsVm.tabs.basic.heading}}"
                                 active="mbomDetailsVm.tabs.basic.active"
                                 select="mbomDetailsVm.tabActivated(mbomDetailsVm.tabs.basic.id)">
                            <div ng-include="mbomDetailsVm.tabs.basic.template"
                                 ng-controller="MBOMBasicInfoController as mbomBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{mbomDetailsVm.tabs.bom.heading}}"
                                 active="mbomDetailsVm.tabs.bom.active"
                                 select="mbomDetailsVm.tabActivated(mbomDetailsVm.tabs.bom.id)">
                            <div ng-include="mbomDetailsVm.tabs.bom.template"
                                 ng-controller="MBOMItemsController as mbomItemsVm"></div>
                        </uib-tab>

                        <uib-tab id="files" heading="{{mbomDetailsVm.tabs.files.heading}}"
                                 active="mbomDetailsVm.tabs.files.active"
                                 select="mbomDetailsVm.tabActivated(mbomDetailsVm.tabs.files.id)">
                            <div ng-include="mbomDetailsVm.tabs.files.template"
                                 ng-controller="MBOMFilesController as mbomFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="mbom-changes" heading="{{mbomDetailsVm.tabs.changes.heading}}"
                                 active="mbomDetailsVm.tabs.changes.active"
                                 select="mbomDetailsVm.tabActivated(mbomDetailsVm.tabs.changes.id)">
                            <div ng-include="mbomDetailsVm.tabs.changes.template"
                                 ng-controller="MBOMChangesController as mbomChangesVm"></div>
                        </uib-tab>
                        <uib-tab id="mbom-whereUsed" heading="{{mbomDetailsVm.tabs.whereUsed.heading}}"
                                 active="mbomDetailsVm.tabs.whereUsed.active"
                                 select="mbomDetailsVm.tabActivated(mbomDetailsVm.tabs.whereUsed.id)">
                            <div ng-include="mbomDetailsVm.tabs.whereUsed.template"
                                 ng-controller="MBOMWhereUsedController as mbomWhereUsedVm"></div>
                        </uib-tab>
                        <!-- <uib-tab id="workflow"
                                 heading="{{mbomDetailsVm.tabs.workflow.heading}}"
                                 active="mbomDetailsVm.tabs.workflow.active"
                                 select="mbomDetailsVm.tabActivated(mbomDetailsVm.tabs.workflow.id)">
                            <div ng-include="mbomDetailsVm.tabs.workflow.template"
                                 ng-controller="MBOMWorkflowController as mbomWfVm"></div>
                        </uib-tab> -->
                        <uib-tab id="" heading="{{mbomDetailsVm.tabs.timelineHistory.heading}}"
                                 active="mbomDetailsVm.tabs.timelineHistory.active"
                                 select="mbomDetailsVm.tabActivated(mbomDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="mbomDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="MBOMTimeLineController as mbomTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
