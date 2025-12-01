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
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="mbomInstanceDetailsVm.showProductionOrderDetails()"
                        title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
            </div>
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

            <div class="pull-right">

            </div>
        </div>
        <free-text-search ng-if="mbomInstanceDetailsVm.tabs.files.active" on-clear="mbomInstanceDetailsVm.onClear"
                          on-search="mbomInstanceDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="mbomInstanceDetailsVm.active">
                        <uib-tab heading="{{mbomInstanceDetailsVm.tabs.basic.heading}}"
                                 active="mbomInstanceDetailsVm.tabs.basic.active"
                                 select="mbomInstanceDetailsVm.tabActivated(mbomInstanceDetailsVm.tabs.basic.id)">
                            <div ng-include="mbomInstanceDetailsVm.tabs.basic.template"
                                 ng-controller="MBOMInstanceBasicInfoController as mbomInstanceBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{mbomInstanceDetailsVm.tabs.bom.heading}}"
                                 active="mbomInstanceDetailsVm.tabs.bom.active"
                                 select="mbomInstanceDetailsVm.tabActivated(mbomInstanceDetailsVm.tabs.bom.id)">
                            <div ng-include="mbomInstanceDetailsVm.tabs.bom.template"
                                 ng-controller="MBOMInstanceItemsController as mbomInstanceItemsVm"></div>
                        </uib-tab>

                        <uib-tab id="operations" heading="{{mbomInstanceDetailsVm.tabs.operations.heading}}"
                                 active="mbomInstanceDetailsVm.tabs.operations.active"
                                 select="mbomInstanceDetailsVm.tabActivated(mbomInstanceDetailsVm.tabs.operations.id)">
                            <div ng-include="mbomInstanceDetailsVm.tabs.operations.template"
                                 ng-controller="MBOMInstanceOperationsController as mbomInstanceOperationsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{mbomInstanceDetailsVm.tabs.files.heading}}"
                                 active="mbomInstanceDetailsVm.tabs.files.active"
                                 select="mbomInstanceDetailsVm.tabActivated(mbomInstanceDetailsVm.tabs.files.id)">
                            <div ng-include="mbomInstanceDetailsVm.tabs.files.template"
                                 ng-controller="MBOMInstanceFilesController as mbomInstanceFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="" heading="{{mbomInstanceDetailsVm.tabs.timelineHistory.heading}}"
                                 active="mbomInstanceDetailsVm.tabs.timelineHistory.active"
                                 select="mbomInstanceDetailsVm.tabActivated(mbomInstanceDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="mbomInstanceDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="MBOMInstanceTimeLineController as mbomInstanceTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
