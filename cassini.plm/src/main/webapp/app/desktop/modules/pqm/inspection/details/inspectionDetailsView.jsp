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

        .panels-container {
            margin-top: -4px;
            float: right;
            margin-right: 30px;
        }

        .panels-container span:first-child {
            border-radius: 3px 0 0 3px;
        }

        .panel-summary-total {
            height: 34px;
            margin: 3px -20px 0px 8px;
            display: inline-block;
            width: 230px;
            border-radius: 0 3px 3px 0;
            padding-left: 10px;
            line-height: 34px;
        }

        .panel-summary-total span:first-child {
            width: auto;
            height: 34px;
        }

        .panel-summary-total span:first-child h2 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 17px;
            display: inline-block;
            padding-right: 10px;
            border-right: 1px solid #fff;
            width: 160px;
        }

        .panel-summary-total span:nth-child(2) h1 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 18px;
            display: inline-block;
            width: 40px;
        }

        .panel-summary-total h2,
        .panel-summary-total h1 {
            text-align: center;
        }

        .panel-summary-total h1 {
            font-size: 16px;
        }

        .panel-summary {
            height: 34px;
            margin: 3px -20px 0px 8px;
            display: inline-block;
            width: 180px;
            border-radius: 0 3px 3px 0;
            padding-left: 10px;
            line-height: 34px;
        }

        .panel-summary span:first-child {
            width: 150px;
            height: 34px;
        }

        .panel-summary span:first-child h2 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 17px;
            display: inline-block;
            padding-right: 10px;
            border-right: 1px solid #fff;
            width: 120px;
        }

        .panel-summary span:nth-child(2) h1 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 18px;
            display: inline-block;
            width: 40px;
        }

        .panel-summary h2,
        .panel-summary h1 {
            text-align: center;
        }

        .panel-summary h1 {
            font-size: 16px;
        }

        .panel-total {
            background: #005C97; /* fallback for old browsers */
            background: -webkit-linear-gradient(to left, #363795, #005C97); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to left, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-finish {
            background: #159957; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #159957, #155799); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #159957, #155799); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-pass {
            background: #5cb85c; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #5cb85c, #12b80b); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #5cb85c, #12b80b); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-fail {
            background: #d9534f; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #d98e8e, #d9534f); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #d98e8e, #d9534f); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-inprogress {
            background: #fdc830; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.pqm.inspection.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>

                <button ng-if="inspectionDetailsVm.tabs.files.active && hasPermission('inspection','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="inspectionDetailsVm.tabs.workflow.active"
                        ng-show="inspection.startWorkflow != true"
                        title="{{inspectionDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="inspectionDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
                <button class="btn btn-default btn-sm"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style="font-size: 16px;"></i>
                </button>
                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm" ng-click="showPrintOptions(inspectionDetailsVm.inspectionId, inspection.objectType)">
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

            <span class="panels-container" ng-if="!inspectionDetailsVm.tabs.files.active">
            <span class="panel-summary-total panel-total">
                <span><h2 translate>CHECKLISTS</h2></span>
                <span><h1>{{inspectionCounts.totalChecklists}}</h1></span>
            </span>

            <span class="panel-summary panel-inprogress">
                <span><h2 translate>PENDING</h2></span>
                <span><h1>{{inspectionCounts.pendingChecklists}}</h1></span>
            </span>

            <span class="panel-summary panel-finish">
                <span><h2 translate>FINISHED</h2></span>
                <span><h1>{{inspectionCounts.finishedChecklists}}</h1></span>
            </span>
            <span class="panel-summary panel-pass">
                <span><h2 translate>PASSED</h2></span>
                <span><h1>{{inspectionCounts.passChecklists}}</h1></span>
            </span>
            <span class="panel-summary panel-fail">
                <span><h2 translate>FAILED</h2></span>
                <span><h1>{{inspectionCounts.failChecklist}}</h1></span>
            </span>
            </span>
            <free-text-search ng-if="inspectionDetailsVm.tabs.files.active"
                              on-clear="inspectionDetailsVm.onClear"
                              on-search="inspectionDetailsVm.freeTextSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                            <uib-tabset active="inspectionDetailsVm.active">
                                <uib-tab heading="{{inspectionDetailsVm.tabs.basic.heading}}"
                                         active="inspectionDetailsVm.tabs.basic.active"
                                         select="inspectionDetailsVm.inspectionDetailsTabActivated(inspectionDetailsVm.tabs.basic.id)">
                                    <div ng-include="inspectionDetailsVm.tabs.basic.template"
                                         ng-controller="InspectionBasicInfoController as inspectionBasicVm"></div>
                                </uib-tab>
                                <uib-tab id="checklist"
                                         heading="{{inspectionDetailsVm.tabs.checklist.heading}}"
                                         active="inspectionDetailsVm.tabs.checklist.active"
                                         select="inspectionDetailsVm.inspectionDetailsTabActivated(inspectionDetailsVm.tabs.checklist.id)">
                                    <div ng-include="inspectionDetailsVm.tabs.checklist.template"
                                         ng-controller="InspectionChecklistController as inspectionChecklistVm"></div>
                                </uib-tab>
                                <uib-tab id="relatedItems"
                                         heading="{{inspectionDetailsVm.tabs.relatedItem.heading}}"
                                         active="inspectionDetailsVm.tabs.relatedItem.active"
                                         select="inspectionDetailsVm.inspectionDetailsTabActivated(inspectionDetailsVm.tabs.relatedItem.id)">
                                    <div ng-include="inspectionDetailsVm.tabs.relatedItem.template"
                                         ng-controller="InspectionRelatedItemsController as inspectionRelatedItemsVm"></div>
                                </uib-tab>
                                <uib-tab id="workflow"
                                         heading="{{inspectionDetailsVm.tabs.workflow.heading}}"
                                         active="inspectionDetailsVm.tabs.workflow.active"
                                         select="inspectionDetailsVm.inspectionDetailsTabActivated(inspectionDetailsVm.tabs.workflow.id)">
                                    <div ng-include="inspectionDetailsVm.tabs.workflow.template"
                                         ng-controller="InspectionWorkflowController as inspectionWfVm"></div>
                                </uib-tab>
                                <uib-tab id="files"
                                         heading="{{inspectionDetailsVm.tabs.files.heading}}"
                                         active="inspectionDetailsVm.tabs.files.active"
                                         select="inspectionDetailsVm.inspectionDetailsTabActivated(inspectionDetailsVm.tabs.files.id)">
                                    <div ng-include="inspectionDetailsVm.tabs.files.template"
                                         ng-controller="InspectionFilesController as inspectionFilesVm"></div>
                                </uib-tab>
                                <plugin-tabs tabs="inspectionDetailsVm.tabs" custom-tabs="inspectionDetailsVm.customTabs"
                                             object-value="inspectionDetailsVm.inspection" tab-id="inspectionDetailsVm.tabId" active="inspectionDetailsVm.active"></plugin-tabs>
                                <uib-tab id="timeline"
                                         heading="{{inspectionDetailsVm.tabs.timelineHistory.heading}}"
                                         active="inspectionDetailsVm.tabs.timelineHistory.active"
                                         select="inspectionDetailsVm.inspectionDetailsTabActivated(inspectionDetailsVm.tabs.timelineHistory.id)">
                                    <div ng-include="inspectionDetailsVm.tabs.timelineHistory.template"
                                         ng-controller="InspectionTimelineHistoryController as inspectionTimelineHistoryVm"></div>
                                </uib-tab>
                            </uib-tabset>
                        </scrollable-tabset>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>