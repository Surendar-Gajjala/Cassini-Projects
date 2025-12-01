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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{assemblyLine.number}}
            <span title="{{assemblyLine.name.length > 30 ? assemblyLine.name : ' '}}"> {{assemblyLine.name | limitTo:30}} {{assemblyLine.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.assemblyline.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(assemblyLineDetailsVm.assemblyLineId,'ASSEMBLYLINE')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="assemblyLineDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!assemblyLineDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!assemblyLineDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="assemblyLineDetailsVm.tabs.files.active" on-clear="assemblyLineDetailsVm.onClear"
                          on-search="assemblyLineDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="assemblyLineDetailsVm.active">
                        <uib-tab heading="{{assemblyLineDetailsVm.tabs.basic.heading}}"
                                 active="assemblyLineDetailsVm.tabs.basic.active"
                                 select="assemblyLineDetailsVm.tabActivated(assemblyLineDetailsVm.tabs.basic.id)">
                            <div ng-include="assemblyLineDetailsVm.tabs.basic.template"
                                 ng-controller="AssemblyLineBasicInfoController as assemblyLineBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="workCenters" heading="{{assemblyLineDetailsVm.tabs.workCenters.heading}}"
                                 active="assemblyLineDetailsVm.tabs.workCenters.active"
                                 select="assemblyLineDetailsVm.tabActivated(assemblyLineDetailsVm.tabs.workCenters.id)">
                            <div ng-include="assemblyLineDetailsVm.tabs.workCenters.template"
                                 ng-controller="AssemblyLineWorkCentersController as assemblyLineWorkCentersVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{assemblyLineDetailsVm.tabs.files.heading}}"
                                 active="assemblyLineDetailsVm.tabs.files.active"
                                 select="assemblyLineDetailsVm.tabActivated(assemblyLineDetailsVm.tabs.files.id)">
                            <div ng-include="assemblyLineDetailsVm.tabs.files.template"
                                 ng-controller="AssemblyLineFilesController as assemblyLineFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="assemblyLineDetailsVm.tabs" custom-tabs="assemblyLineDetailsVm.customTabs"
                                     object-value="assemblyLineDetailsVm.assemblyLine" tab-id="assemblyLineDetailsVm.tabId" active="assemblyLineDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{assemblyLineDetailsVm.tabs.timelineHistory.heading}}"
                                 active="assemblyLineDetailsVm.tabs.timelineHistory.active"
                                 select="assemblyLineDetailsVm.tabActivated(assemblyLineDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="assemblyLineDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="AssemblyLineTimeLineController as assemblyLineTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
