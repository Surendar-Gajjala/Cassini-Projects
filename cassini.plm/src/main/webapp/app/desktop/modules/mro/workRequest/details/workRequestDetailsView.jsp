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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{workRequest.number}}
            <span title="{{workRequest.name.length > 30 ? workRequest.name : ' '}}"> {{workRequest.name | limitTo:30}} {{workRequest.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mro.workRequest.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm"
                ng-click="showPrintOptions(workRequestDetailsVm.workRequestId,'MROWORKREQUEST')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>
        <button ng-if="workRequestDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy"></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="workRequestDetailsVm.workOrders.length == 0"
                ng-click="workRequestDetailsVm.newWorkOrder()" title="Create work order">
            <i class="fa fa-plus"></i>
        </button>
        <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
            <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <span class="fa fa-copy"></span><span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href=""
                                                                       translate>CLEAR_AND_ADD_FILES</a>
                </li>
                <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                    ({{clipBoardObjectFiles.length}})</a></li>
            </ul>
        </div>
        <comments-btn ng-if="!workRequestDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!workRequestDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="workRequestDetailsVm.tabs.files.active" on-clear="workRequestDetailsVm.onClear"
                          on-search="workRequestDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="workRequestDetailsVm.active">
                        <uib-tab heading="{{workRequestDetailsVm.tabs.basic.heading}}"
                                 active="workRequestDetailsVm.tabs.basic.active"
                                 select="workRequestDetailsVm.tabActivated(workRequestDetailsVm.tabs.basic.id)">
                            <div ng-include="workRequestDetailsVm.tabs.basic.template"
                                 ng-controller="WorkRequestBasicInfoController as workRequestBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{workRequestDetailsVm.tabs.attributes.heading}}"
                                 active="workRequestDetailsVm.tabs.attributes.active"
                                 select="workRequestDetailsVm.tabActivated(workRequestDetailsVm.tabs.attributes.id)">
                            <div ng-include="workRequestDetailsVm.tabs.attributes.template"
                                 ng-controller="WorkRequestAttributesController as workRequestAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="files" heading="{{workRequestDetailsVm.tabs.files.heading}}"
                                 active="workRequestDetailsVm.tabs.files.active"
                                 select="workRequestDetailsVm.tabActivated(workRequestDetailsVm.tabs.files.id)">
                            <div ng-include="workRequestDetailsVm.tabs.files.template"
                                 ng-controller="WorkRequestFilesController as workRequestFilesVm"></div>
                        </uib-tab>
                        <%--<uib-tab id="workflow" heading="{{workRequestDetailsVm.tabs.workflow.heading}}"
                                 active="workRequestDetailsVm.tabs.workflow.active"
                                 select="workRequestDetailsVm.tabActivated(workRequestDetailsVm.tabs.workflow.id)">
                            <div ng-include="workRequestDetailsVm.tabs.workflow.template"
                                 ng-controller="WorkRequestWorkflowController as workRequestWorkflowVm"></div>
                        </uib-tab>--%>
                        <plugin-tabs tabs="workRequestDetailsVm.tabs" custom-tabs="workRequestDetailsVm.customTabs"
                                     object-value="workRequestDetailsVm.workRequest" tab-id="workRequestDetailsVm.tabId" active="workRequestDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{workRequestDetailsVm.tabs.timelineHistory.heading}}"
                                 active="workRequestDetailsVm.tabs.timelineHistory.active"
                                 select="workRequestDetailsVm.tabActivated(workRequestDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="workRequestDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="WorkRequestTimeLineController as workRequestTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
