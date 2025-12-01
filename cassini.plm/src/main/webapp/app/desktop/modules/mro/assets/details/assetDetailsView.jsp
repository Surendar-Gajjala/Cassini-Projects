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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" >{{asset.number}}
            <span title="{{asset.name.length > 30 ? asset.name : ' '}}"> {{asset.name | limitTo:30}} {{asset.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mro.asset.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="assetDetailsVm.tabs.workflow.active"
                ng-show="!asset.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>
        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(assetDetailsVm.assetId,'MROASSET')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="assetDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy" style="font-size: 16px;"></i>
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
                <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                    ({{clipBoardObjectFiles.length}})</a></li>
            </ul>
        </div>
        <comments-btn ng-if="!assetDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!assetDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="assetDetailsVm.tabs.files.active" on-clear="assetDetailsVm.onClear"
                          on-search="assetDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="assetDetailsVm.active">
                        <uib-tab heading="{{assetDetailsVm.tabs.basic.heading}}"
                                 active="assetDetailsVm.tabs.basic.active"
                                 select="assetDetailsVm.tabActivated(assetDetailsVm.tabs.basic.id)">
                            <div ng-include="assetDetailsVm.tabs.basic.template"
                                 ng-controller="AssetBasicInfoController as assetBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{assetDetailsVm.tabs.attributes.heading}}"
                                 active="assetDetailsVm.tabs.attributes.active"
                                 select="assetDetailsVm.tabActivated(assetDetailsVm.tabs.attributes.id)">
                            <div ng-include="assetDetailsVm.tabs.attributes.template"
                                 ng-controller="AssetAttributesController as assetAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="SpareParts" heading="{{assetDetailsVm.tabs.spareParts.heading}}"
                                 active="assetDetailsVm.tabs.spareParts.active"
                                 select="assetDetailsVm.tabActivated(assetDetailsVm.tabs.spareParts.id)">
                            <div ng-include="assetDetailsVm.tabs.spareParts.template"
                                 ng-controller="AssetSparePartsController as assetSparePartVm"></div>
                        </uib-tab>
                        <uib-tab id="maintenance" heading="{{assetDetailsVm.tabs.maintenance.heading}}"
                                 active="assetDetailsVm.tabs.maintenance.active"
                                 select="assetDetailsVm.tabActivated(assetDetailsVm.tabs.maintenance.id)">
                            <div ng-include="assetDetailsVm.tabs.maintenance.template"
                                 ng-controller="AssetMaintenanceController as assetMaintenanceVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{assetDetailsVm.tabs.files.heading}}"
                                 active="assetDetailsVm.tabs.files.active"
                                 select="assetDetailsVm.tabActivated(assetDetailsVm.tabs.files.id)">
                            <div ng-include="assetDetailsVm.tabs.files.template"
                                 ng-controller="AssetFilesController as assetFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="assetDetailsVm.tabs" custom-tabs="assetDetailsVm.customTabs"
                                     object-value="assetDetailsVm.asset" tab-id="assetDetailsVm.tabId" active="assetDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{assetDetailsVm.tabs.timelineHistory.heading}}"
                                 active="assetDetailsVm.tabs.timelineHistory.active"
                                 select="assetDetailsVm.tabActivated(assetDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="assetDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="AssetTimeLineController as assetTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
