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

    </style>
    <div class="view-toolbar">
        <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{dcr.crNumber}}
            <span title="{{dcr.title.length > 30 ? dcr.title : ' '}}"> {{dcr.title | limitTo:30}} {{dcr.title.length > 30 ? '...' : ' '}}</span>
        </span> -->

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.changes.dcr.all')"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="dcrVm.tabs.workflow.active"
                ng-show="!dcr.startWorkflow"
                title="{{dcrVm.changeWorkflowTitle}}"
                class="btn btn-sm btn-success" ng-click="dcrVm.changeWorkflow()">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="dcrVm.tabs.files.active && (hasPermission('change','dcr','edit') || hasPermission('change','edit')) && hasFiles == true"
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
                class="btn btn-sm" ng-click="showPrintOptions(dcrVm.dcrId,'DCR')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <div class="pull-right">

        </div>

        <!-- <comments-btn ng-if="!dcrVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!customerDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn> -->
        <free-text-search ng-if="dcrVm.tabs.files.active" on-clear="dcrVm.onClear"
                          on-search="dcrVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="dcrVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="dcrVm.tabs.basic.active"
                                 select="dcrVm.tabActivated(dcrVm.tabs.basic.id)">
                            <div ng-include="dcrVm.tabs.basic.template"
                                 ng-controller="DCRBasicInfoController as dcrBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                 active="dcrVm.tabs.attributes.active"
                                 select="dcrVm.tabActivated(dcrVm.tabs.attributes.id)">
                            <div ng-include="dcrVm.tabs.attributes.template"
                                 ng-controller="DCRAttributesController as dcrAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="affectedItems" heading="{{dcrVm.tabs.affectedItems.heading}}"
                                 active="dcrVm.tabs.affectedItems.active"
                                 select="dcrVm.tabActivated(dcrVm.tabs.affectedItems.id)">
                            <div ng-include="dcrVm.tabs.affectedItems.template"
                                 ng-controller="DCRAffectedItemsController as dcrAffectedItemsVm"></div>
                        </uib-tab>

                        <uib-tab id="relatedItems" heading="{{dcrVm.tabs.relatedItems.heading}}"
                                 active="dcrVm.tabs.relatedItems.active"
                                 select="dcrVm.tabActivated(dcrVm.tabs.relatedItems.id)">
                            <div ng-include="dcrVm.tabs.relatedItems.template"
                                 ng-controller="DCRRelatedItemsController as dcrRelatedItemsVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'WORKFLOW' | translate}}"
                                 active="dcrVm.tabs.workflow.active"
                                 select="dcrVm.tabActivated(dcrVm.tabs.workflow.id)">
                            <div ng-include="dcrVm.tabs.workflow.template"
                                 ng-controller="DCRWorkflowController as dcrWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 active="dcrVm.tabs.files.active"
                                 select="dcrVm.tabActivated(dcrVm.tabs.files.id)">
                            <div ng-include="dcrVm.tabs.files.template"
                                 ng-controller="DCRFilesController as dcrFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="dcrVm.tabs" custom-tabs="dcrVm.customTabs"
                                     object-value="dcrVm.dcr" tab-id="dcrVm.tabId" active="dcrVm.active"></plugin-tabs>
                        <uib-tab id="ecoHistory" heading="{{'TIMELINE' | translate}}"
                                 active="dcrVm.tabs.timeLine.active"
                                 select="dcrVm.tabActivated(dcrVm.tabs.timeLine.id)">
                            <div ng-include="dcrVm.tabs.timeLine.template"
                                 ng-controller="DCRTimeLineController as dcrTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
