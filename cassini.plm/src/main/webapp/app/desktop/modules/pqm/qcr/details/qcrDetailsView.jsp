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
            <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{qcr.qcrNumber}}
            <span title="{{qcr.title.length > 30 ? qcr.title : ' '}}"> {{qcr.title | limitTo:30}} {{qcr.title.length > 30 ? '...' : ' '}}</span>
        </span> -->

            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.pqm.qcr.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>

                <button ng-if="qcrDetailsVm.tabs.files.active && hasPermission('qcr','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true" style=""></i>
                </button>
                <button class="btn btn-default btn-sm"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style="font-size: 16px;"></i>
                </button>
                <button ng-if="qcrDetailsVm.tabs.workflow.active"
                        ng-show="qcr.startWorkflow != true"
                        title="{{qcrDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="qcrDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm" ng-click="showPrintOptions(qcrDetailsVm.qcrId, 'QCR')">
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
            <!-- <comments-btn ng-if="!qcrDetailsVm.tabs.files.active" id="details-comment"
                          object-type="mainVm.comments.objectType"
                          object-id="mainVm.comments.objectId"
                          comment-count="mainVm.comments.commentCount"></comments-btn>
            <tags-btn ng-if="!qcrDetailsVm.tabs.files.active" id="details-tag"
                      object-type="mainVm.tags.objectType"
                      object="mainVm.tags.object"
                      tags-count="mainVm.tags.tagsCount"></tags-btn> -->
            <free-text-search ng-if="qcrDetailsVm.tabs.files.active"
                              on-clear="qcrDetailsVm.onClear"
                              on-search="qcrDetailsVm.freeTextSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                            <uib-tabset active="qcrDetailsVm.active">
                                <uib-tab heading="{{qcrDetailsVm.tabs.basic.heading}}"
                                         active="qcrDetailsVm.tabs.basic.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.basic.id)">
                                    <div ng-include="qcrDetailsVm.tabs.basic.template"
                                         ng-controller="QcrBasicInfoController as qcrBasicVm"></div>
                                </uib-tab>

                                <%--<uib-tab heading="{{qcrDetailsVm.tabs.attributes.heading}}"
                                         active="qcrDetailsVm.tabs.attributes.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.attributes.id)">
                                    <div ng-include="qcrDetailsVm.tabs.attributes.template"
                                         ng-controller="QcrAttributesController as qcrAttributesVm"></div>
                                </uib-tab>--%>
                                <uib-tab id="problemSources"
                                         heading="{{qcrDetailsVm.tabs.problemSource.heading}}"
                                         active="qcrDetailsVm.tabs.problemSource.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.problemSource.id)">
                                    <div ng-include="qcrDetailsVm.tabs.problemSource.template"
                                         ng-controller="QcrProblemSourcesController as qcrProblemSourcesVm"></div>
                                </uib-tab>
                                <uib-tab id="problemItems"
                                         heading="{{qcrDetailsVm.tabs.problemItem.heading}}"
                                         active="qcrDetailsVm.tabs.problemItem.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.problemItem.id)">
                                    <div ng-include="qcrDetailsVm.tabs.problemItem.template"
                                         ng-controller="QcrProblemItemsController as qcrProblemItemsVm"></div>
                                </uib-tab>
                                <uib-tab id="relatedItems"
                                         heading="{{qcrDetailsVm.tabs.relatedItem.heading}}"
                                         active="qcrDetailsVm.tabs.relatedItem.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.relatedItem.id)">
                                    <div ng-include="qcrDetailsVm.tabs.relatedItem.template"
                                         ng-controller="QcrRelatedItemsController as qcrRelatedItemsVm"></div>
                                </uib-tab>
                                <uib-tab id="capaTab"
                                         heading="{{qcrDetailsVm.tabs.capa.heading}}"
                                         active="qcrDetailsVm.tabs.capa.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.capa.id)">
                                    <div ng-include="qcrDetailsVm.tabs.capa.template"
                                         ng-controller="QcrCaPaController as qcrCaPaVm"></div>
                                </uib-tab>
                                <uib-tab id="workflow"
                                         heading="{{qcrDetailsVm.tabs.workflow.heading}}"
                                         active="qcrDetailsVm.tabs.workflow.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.workflow.id)">
                                    <div ng-include="qcrDetailsVm.tabs.workflow.template"
                                         ng-controller="QcrWorkflowController as qcrWfVm"></div>
                                </uib-tab>
                                <uib-tab id="files"
                                         heading="{{qcrDetailsVm.tabs.files.heading}}"
                                         active="qcrDetailsVm.tabs.files.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.files.id)">
                                    <div ng-include="qcrDetailsVm.tabs.files.template"
                                         ng-controller="QcrFilesController as qcrFilesVm"></div>
                                </uib-tab>
                                <plugin-tabs tabs="qcrDetailsVm.tabs" custom-tabs="qcrDetailsVm.customTabs"
                                             object-value="qcrDetailsVm.qcr" tab-id="qcrDetailsVm.tabId" active="qcrDetailsVm.active"></plugin-tabs>
                                <uib-tab heading="{{qcrDetailsVm.tabs.timelineHistory.heading}}"
                                         active="qcrDetailsVm.tabs.timelineHistory.active"
                                         select="qcrDetailsVm.qcrDetailsTabActivated(qcrDetailsVm.tabs.timelineHistory.id)">
                                    <div ng-include="qcrDetailsVm.tabs.timelineHistory.template"
                                         ng-controller="QcrHistoryController as qcrHistoryVm"></div>
                                </uib-tab>
                            </uib-tabset>
                        </scrollable-tabset>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>