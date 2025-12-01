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

        table {
            table-layout: fixed;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{problemReport.prNumber}}</span> -->

            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.pqm.pr.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>

                <button ng-if="problemReportDetailsVm.tabs.files.active && hasPermission('problemreport','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="problemReportDetailsVm.tabs.workflow.active"
                        ng-show="problemReport.startWorkflow != true"
                        title="{{problemReportDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="problemReportDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
                <button class="btn btn-default btn-sm"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style="font-size: 16px;"></i>
                </button>
                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm" ng-click="showPrintOptions(problemReportDetailsVm.problemReportId, 'PR')">
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
            <!-- <comments-btn ng-if="!problemReportDetailsVm.tabs.files.active" id="details-comment"
                          object-type="mainVm.comments.objectType"
                          object-id="mainVm.comments.objectId"
                          comment-count="mainVm.comments.commentCount"></comments-btn>
            <tags-btn ng-if="!problemReportDetailsVm.tabs.files.active" id="details-tag"
                      object-type="mainVm.tags.objectType"
                      object="mainVm.tags.object"
                      tags-count="mainVm.tags.tagsCount"></tags-btn> -->
            <free-text-search ng-if="problemReportDetailsVm.tabs.files.active"
                              on-clear="problemReportDetailsVm.onClear"
                              on-search="problemReportDetailsVm.freeTextSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                            <uib-tabset active="problemReportDetailsVm.active">
                                <uib-tab heading="{{problemReportDetailsVm.tabs.basic.heading}}"
                                         active="problemReportDetailsVm.tabs.basic.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(problemReportDetailsVm.tabs.basic.id)">
                                    <div ng-include="problemReportDetailsVm.tabs.basic.template"
                                         ng-controller="ProblemReportBasicInfoController as prBasicVm"></div>
                                </uib-tab>

                                <%--<uib-tab heading="{{problemReportDetailsVm.tabs.attributes.heading}}"
                                         active="problemReportDetailsVm.tabs.attributes.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(problemReportDetailsVm.tabs.attributes.id)">
                                    <div ng-include="problemReportDetailsVm.tabs.attributes.template"
                                         ng-controller="PrAttributesController as prAttributesVm"></div>
                                </uib-tab>--%>
                                <uib-tab id="problemItems"
                                         heading="{{problemReportDetailsVm.tabs.problemItem.heading}}"
                                         active="problemReportDetailsVm.tabs.problemItem.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(problemReportDetailsVm.tabs.problemItem.id)">
                                    <div ng-include="problemReportDetailsVm.tabs.problemItem.template"
                                         ng-controller="PrProblemItemsController as prProblemItemsVm"></div>
                                </uib-tab>
                                <uib-tab id="relatedItems"
                                         heading="{{problemReportDetailsVm.tabs.relatedItem.heading}}"
                                         active="problemReportDetailsVm.tabs.relatedItem.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(problemReportDetailsVm.tabs.relatedItem.id)">
                                    <div ng-include="problemReportDetailsVm.tabs.relatedItem.template"
                                         ng-controller="PrRelatedItemsController as prRelatedItemsVm"></div>
                                </uib-tab>
                             <%--   <uib-tab ng-repeat="customTab in problemReportDetailsVm.customTabs"
                                         id="{{customTab.id}}"
                                         class="custom-tab"
                                         heading="{{customTab.heading}}"
                                         active="customTab.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(customTab.id)">
                                    <div ng-include="customTab.template"
                                         dynamic-ctrl="customTab.controller"></div>
                                </uib-tab>--%>
                                <plugin-tabs tabs="problemReportDetailsVm.tabs" custom-tabs="problemReportDetailsVm.customTabs"
                                             object-value="problemReportDetailsVm.problemReport" tab-id="problemReportDetailsVm.tabId" active="problemReportDetailsVm.active"></plugin-tabs>
                                <uib-tab id="workflow"
                                         heading="{{problemReportDetailsVm.tabs.workflow.heading}}"
                                         active="problemReportDetailsVm.tabs.workflow.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(problemReportDetailsVm.tabs.workflow.id)">
                                    <div ng-include="problemReportDetailsVm.tabs.workflow.template"
                                         ng-controller="PrWorkflowController as prWfVm"></div>
                                </uib-tab>
                                <uib-tab id="files"
                                         heading="{{problemReportDetailsVm.tabs.files.heading}}"
                                         active="problemReportDetailsVm.tabs.files.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(problemReportDetailsVm.tabs.files.id)">
                                    <div ng-include="problemReportDetailsVm.tabs.files.template"
                                         ng-controller="PrFilesController as prFilesVm"></div>
                                </uib-tab>
                                <uib-tab heading="{{problemReportDetailsVm.tabs.timelineHistory.heading}}"
                                         active="problemReportDetailsVm.tabs.timelineHistory.active"
                                         select="problemReportDetailsVm.problemReportDetailsTabActivated(problemReportDetailsVm.tabs.timelineHistory.id)">
                                    <div ng-include="problemReportDetailsVm.tabs.timelineHistory.template"
                                         ng-controller="PrHistoryController as prHistoryVm"></div>
                                </uib-tab>
                            </uib-tabset>
                        </scrollable-tabset>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>