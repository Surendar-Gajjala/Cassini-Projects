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
        <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{dco.dcoNumber}}
            <span title="{{dco.title.length > 30 ? dco.title : ' '}}"> {{dco.title | limitTo:30}} {{dco.title.length > 30 ? '...' : ' '}}</span>
        </span> -->

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.changes.dco.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="dcoVm.tabs.workflow.active"
                ng-show="!dco.startWorkflow"
                title="{{dcoVm.changeWorkflowTitle}}"
                class="btn btn-sm btn-success" ng-click="dcoVm.changeWorkflow()">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="dcoVm.tabs.files.active && (hasPermission('change','dcr','edit') || hasPermission('change','edit')) && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyChangeFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy" style="font-size: 16px;"></i>
        </button>
        <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
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

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(dcoVm.dcoId,'DCO')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <div class="pull-right">

        </div>

        <!-- <comments-btn ng-if="!dcoVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!dcoVm.tabs.files.active"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn> -->
        <free-text-search ng-if="dcoVm.tabs.files.active" on-clear="dcoVm.onClear"
                          on-search="dcoVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="dcoVm.active">
                        <uib-tab heading="{{dcoVm.tabs.basic.heading}}"
                                 active="dcoVm.tabs.basic.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.basic.id)">
                            <div ng-include="dcoVm.tabs.basic.template"
                                 ng-controller="DCOBasicInfoController as dcoBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{dcoVm.tabs.attributes.heading}}"
                                 active="dcoVm.tabs.attributes.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.attributes.id)">
                            <div ng-include="dcoVm.tabs.attributes.template"
                                 ng-controller="DCOAttributesController as dcoAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="changeRequests" heading="{{dcoVm.tabs.changeRequest.heading}}"
                                 active="dcoVm.tabs.changeRequest.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.changeRequest.id)">
                            <div ng-include="dcoVm.tabs.changeRequest.template"
                                 ng-controller="DCOChangeRequestController as dcoChangeRequestVm"></div>
                        </uib-tab>
                        <uib-tab id="affectedItems" heading="{{dcoVm.tabs.affectedItems.heading}}"
                                 active="dcoVm.tabs.affectedItems.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.affectedItems.id)">
                            <div ng-include="dcoVm.tabs.affectedItems.template"
                                 ng-controller="DCOAffectedItemsController as dcoAffectedItemsVm"></div>
                        </uib-tab>
                        <uib-tab id="relatedItems" heading="{{dcoVm.tabs.relatedItems.heading}}"
                                 active="dcoVm.tabs.relatedItems.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.relatedItems.id)">
                            <div ng-include="dcoVm.tabs.relatedItems.template"
                                 ng-controller="DCORelatedItemsController as dcoRelatedItemsVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{dcoVm.tabs.workflow.heading}}"
                                 active="dcoVm.tabs.workflow.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.workflow.id)">
                            <div ng-include="dcoVm.tabs.workflow.template"
                                 ng-controller="DCOWorkflowController as dcoWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{dcoVm.tabs.files.heading}}"
                                 active="dcoVm.tabs.files.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.files.id)">
                            <div ng-include="dcoVm.tabs.files.template"
                                 ng-controller="DCOFilesController as dcoFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="dcoVm.tabs" custom-tabs="dcoVm.customTabs"
                                     object-value="dcoVm.dco" tab-id="dcoVm.tabId" active="dcoVm.active"></plugin-tabs>
                        <uib-tab id="ecoHistory" heading="{{dcoVm.tabs.timeLine.heading}}"
                                 active="dcoVm.tabs.timeLine.active"
                                 select="dcoVm.tabActivated(dcoVm.tabs.timeLine.id)">
                            <div ng-include="dcoVm.tabs.timeLine.template"
                                 ng-controller="DCOTimeLineController as dcoTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
