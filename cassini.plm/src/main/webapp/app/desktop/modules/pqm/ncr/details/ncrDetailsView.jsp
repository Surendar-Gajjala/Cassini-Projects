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
            <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{ncr.ncrNumber}}
            <span title="{{ncr.title.length > 30 ? ncr.title : ' '}}"> {{ncr.title | limitTo:30}} {{ncr.title.length > 30 ? '...' : ' '}}</span>
        </span> -->

            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.pqm.ncr.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>

                <button ng-if="ncrDetailsVm.tabs.files.active && hasPermission('ncr','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="ncrDetailsVm.tabs.workflow.active"
                        ng-show="ncr.startWorkflow != true"
                        title="{{ncrDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="ncrDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
                <button class="btn btn-default btn-sm"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style="font-size: 16px;"></i>
                </button>
                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm" ng-click="showPrintOptions(ncrDetailsVm.ncrId, 'NCR')">
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
            <!-- <comments-btn ng-if="!ncrDetailsVm.tabs.files.active" id="details-comment"
                          object-type="mainVm.comments.objectType"
                          object-id="mainVm.comments.objectId"
                          comment-count="mainVm.comments.commentCount"></comments-btn>
            <tags-btn ng-if="!ncrDetailsVm.tabs.files.active" id="details-tag"
                      object-type="mainVm.tags.objectType"
                      object="mainVm.tags.object"
                      tags-count="mainVm.tags.tagsCount"></tags-btn> -->
            <free-text-search ng-if="ncrDetailsVm.tabs.files.active"
                              on-clear="ncrDetailsVm.onClear"
                              on-search="ncrDetailsVm.freeTextSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                            <uib-tabset active="ncrDetailsVm.active">
                                <uib-tab heading="{{ncrDetailsVm.tabs.basic.heading}}"
                                         active="ncrDetailsVm.tabs.basic.active"
                                         select="ncrDetailsVm.ncrDetailsTabActivated(ncrDetailsVm.tabs.basic.id)">
                                    <div ng-include="ncrDetailsVm.tabs.basic.template"
                                         ng-controller="NcrBasicInfoController as ncrBasicVm"></div>
                                </uib-tab>

                                <%--<uib-tab heading="{{ncrDetailsVm.tabs.attributes.heading}}"
                                         active="ncrDetailsVm.tabs.attributes.active"
                                         select="ncrDetailsVm.ncrDetailsTabActivated(ncrDetailsVm.tabs.attributes.id)">
                                    <div ng-include="ncrDetailsVm.tabs.attributes.template"
                                         ng-controller="NcrAttributesController as ncrAttributesVm"></div>
                                </uib-tab>--%>
                                <uib-tab id="problemItems"
                                         heading="{{ncrDetailsVm.tabs.problemItem.heading}}"
                                         active="ncrDetailsVm.tabs.problemItem.active"
                                         select="ncrDetailsVm.ncrDetailsTabActivated(ncrDetailsVm.tabs.problemItem.id)">
                                    <div ng-include="ncrDetailsVm.tabs.problemItem.template"
                                         ng-controller="NcrProblemItemsController as ncrProblemItemsVm"></div>
                                </uib-tab>
                                <uib-tab id="relatedItems"
                                         heading="{{ncrDetailsVm.tabs.relatedItem.heading}}"
                                         active="ncrDetailsVm.tabs.relatedItem.active"
                                         select="ncrDetailsVm.ncrDetailsTabActivated(ncrDetailsVm.tabs.relatedItem.id)">
                                    <div ng-include="ncrDetailsVm.tabs.relatedItem.template"
                                         ng-controller="NcrRelatedItemsController as ncrRelatedItemsVm"></div>
                                </uib-tab>
                                <uib-tab id="workflow"
                                         heading="{{ncrDetailsVm.tabs.workflow.heading}}"
                                         active="ncrDetailsVm.tabs.workflow.active"
                                         select="ncrDetailsVm.ncrDetailsTabActivated(ncrDetailsVm.tabs.workflow.id)">
                                    <div ng-include="ncrDetailsVm.tabs.workflow.template"
                                         ng-controller="NcrWorkflowController as ncrWfVm"></div>
                                </uib-tab>
                                <uib-tab id="files"
                                         heading="{{ncrDetailsVm.tabs.files.heading}}"
                                         active="ncrDetailsVm.tabs.files.active"
                                         select="ncrDetailsVm.ncrDetailsTabActivated(ncrDetailsVm.tabs.files.id)">
                                    <div ng-include="ncrDetailsVm.tabs.files.template"
                                         ng-controller="NcrFilesController as ncrFilesVm"></div>
                                </uib-tab>
                                <plugin-tabs tabs="ncrDetailsVm.tabs" custom-tabs="ncrDetailsVm.customTabs"
                                             object-value="ncrDetailsVm.ncr" tab-id="ncrDetailsVm.tabId" active="ncrDetailsVm.active"></plugin-tabs>
                                <uib-tab heading="{{ncrDetailsVm.tabs.timelineHistory.heading}}"
                                         active="ncrDetailsVm.tabs.timelineHistory.active"
                                         select="ncrDetailsVm.ncrDetailsTabActivated(ncrDetailsVm.tabs.timelineHistory.id)">
                                    <div ng-include="ncrDetailsVm.tabs.timelineHistory.template"
                                         ng-controller="NcrHistoryController as ncrHistoryVm"></div>
                                </uib-tab>
                            </uib-tabset>
                        </scrollable-tabset>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>