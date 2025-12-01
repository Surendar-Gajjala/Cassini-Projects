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
        <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{eco.ecoNumber}}
           <span title="{{eco.title.length > 30 ? eco.title : ' '}}"> {{eco.title | limitTo:30}} {{eco.title.length > 30 ? '...' : ' '}}</span>
        </span> -->

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.changes.eco.all')"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

            <button ng-if="ecoVm.tabs.files.active && (hasPermission('change','eco','edit') || hasPermission('change','edit')) && hasFiles == true"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                <i class="fa fa-download" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-click="ecoVm.refreshDetails()"
                    title="{{ecoVm.refreshTitle}}">
                <i class="fa fa-refresh"></i>
            </button>

            <button ng-if="ecoVm.tabs.workflow.active"
                    ng-show="!eco.startWorkflow"
                    title="{{ecoVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="ecoVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>

            <button class="btn btn-default btn-sm"
                    ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                    ng-click="copyChangeFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>
            <button
                    title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                    class="btn btn-sm" ng-click="showPrintOptions(ecoVm.ecoId,'ECO')">
                <i class="fa fa-print" aria-hidden="true" style=""></i>
            </button>
            <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyChangeFilesToClipBoard()"><a href="" translate>CLEAR_AND_ADD_FILES</a>
                    </li>
                    <li ng-click="copyChangeFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                        ({{clipBoardObjectFiles.length}})</a></li>
                </ul>
            </div>
        </div>

        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    title="{{action.tooltip}}"
                    ng-click="ecoVm.performCustomAction(action)"
                    ng-repeat="action in ecoVm.customActions">
                <i class="fa {{action.icon}}" style=""></i>
            </button>
        </div>
        <!-- <comments-btn ng-if="!ecoVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!ecoVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn> -->
        <free-text-search ng-if="ecoVm.tabs.files.active" on-clear="ecoVm.onClear"
                          on-search="ecoVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="ecoVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="ecoVm.tabs.basic.active"
                                 select="tabActivated(ecoVm.tabs.basic.id)">
                            <div ng-include="ecoVm.tabs.basic.template"
                                 ng-controller="ECOBasicInfoController as ecoBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                 active="ecoVm.tabs.attributes.active"
                                 select="tabActivated(ecoVm.tabs.attributes.id)">
                            <div ng-include="ecoVm.tabs.attributes.template"
                                 ng-controller="ECOAttributesController as ecoAttributesVm"></div>
                        </uib-tab>--%>

                        <uib-tab id="changeRequests" heading="changeRequests"
                                 active="ecoVm.tabs.changeRequests.active"
                                 select="tabActivated(ecoVm.tabs.changeRequests.id)">
                            <div ng-include="ecoVm.tabs.changeRequests.template"
                                 ng-controller="ECOChangeRequestController as ecoChangeRequestVm"></div>
                        </uib-tab>
                        <uib-tab id="affectedItems" heading="{{'ECO_AFFECTED_ITEMS' | translate}}"
                                 active="ecoVm.tabs.affecteditems.active"
                                 select="tabActivated(ecoVm.tabs.affecteditems.id)">
                            <div ng-include="ecoVm.tabs.affecteditems.template"
                                 ng-controller="ECOAffectedItemsController as ecoAffectedItemsVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{'WORKFLOW' | translate}}"
                                 active="ecoVm.tabs.workflow.active"
                                 select="tabActivated(ecoVm.tabs.workflow.id)">
                            <div ng-include="ecoVm.tabs.workflow.template"
                                 ng-controller="ECOWorkflowController as ecoWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 active="ecoVm.tabs.files.active"
                                 select="tabActivated(ecoVm.tabs.files.id)">
                            <div ng-include="ecoVm.tabs.files.template"
                                 ng-controller="ECOFilesController as ecoFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="ecoVm.tabs" custom-tabs="ecoVm.customTabs"
                                     object-value="ecoVm.eco" tab-id="ecoVm.tabId" active="ecoVm.active"></plugin-tabs>
                        <uib-tab id="ecoHistory" heading="{{'TIMELINE' | translate}}"
                                 active="ecoVm.tabs.history.active"
                                 select="tabActivated(ecoVm.tabs.history.id)">
                            <div ng-include="ecoVm.tabs.history.template"
                                 ng-controller="ECOHistoryController as ecohistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
