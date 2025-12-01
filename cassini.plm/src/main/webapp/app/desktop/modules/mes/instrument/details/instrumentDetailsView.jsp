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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{instrument.number}}
            <span title="{{instrument.name.length > 30 ? instrument.name : ' '}}"> {{instrument.name | limitTo:30}} {{instrument.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.instrument.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="instrumentDetailsVm.tabs.workflow.active"
                ng-show="!instrument.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(instrumentDetailsVm.instrumentId,'INSTRUMENT')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="instrumentDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!instrumentDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!instrumentDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="instrumentDetailsVm.tabs.files.active" on-clear="instrumentDetailsVm.onClear"
                          on-search="instrumentDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="instrumentDetailsVm.active">
                        <uib-tab heading="{{instrumentDetailsVm.tabs.basic.heading}}"
                                 active="instrumentDetailsVm.tabs.basic.active"
                                 select="instrumentDetailsVm.tabActivated(instrumentDetailsVm.tabs.basic.id)">
                            <div ng-include="instrumentDetailsVm.tabs.basic.template"
                                 ng-controller="InstrumentBasicInfoController as instrumentBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{instrumentDetailsVm.tabs.attributes.heading}}"
                                 active="instrumentDetailsVm.tabs.attributes.active"
                                 select="instrumentDetailsVm.tabActivated(instrumentDetailsVm.tabs.attributes.id)">
                            <div ng-include="instrumentDetailsVm.tabs.attributes.template"
                                 ng-controller="InstrumentAttributesController as instrumentAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab heading="{{instrumentDetailsVm.tabs.maintenance.heading}}"
                                 active="instrumentDetailsVm.tabs.maintenance.active"
                                 select="instrumentDetailsVm.tabActivated(instrumentDetailsVm.tabs.maintenance.id)">
                            <div ng-include="instrumentDetailsVm.tabs.maintenance.template"
                                 ng-controller="InstrumentMaintenanceController as instrumentMaintenanceVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{instrumentDetailsVm.tabs.relatedItem.heading}}"
                                 active="instrumentDetailsVm.tabs.relatedItem.active"
                                 select="instrumentDetailsVm.tabActivated(instrumentDetailsVm.tabs.relatedItem.id)">
                            <div ng-include="instrumentDetailsVm.tabs.relatedItem.template"
                                 ng-controller="InstrumentRelatedItemController as instrumentRelatedItemVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{instrumentDetailsVm.tabs.files.heading}}"
                                 active="instrumentDetailsVm.tabs.files.active"
                                 select="instrumentDetailsVm.tabActivated(instrumentDetailsVm.tabs.files.id)">
                            <div ng-include="instrumentDetailsVm.tabs.files.template"
                                 ng-controller="InstrumentFilesController as instrumentFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="instrumentDetailsVm.tabs" custom-tabs="instrumentDetailsVm.customTabs"
                                     object-value="instrumentDetailsVm.instrument" tab-id="instrumentDetailsVm.tabId" active="instrumentDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{instrumentDetailsVm.tabs.timelineHistory.heading}}"
                                 active="instrumentDetailsVm.tabs.timelineHistory.active"
                                 select="instrumentDetailsVm.tabActivated(instrumentDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="instrumentDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="InstrumentTimeLineController as instrumentTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
