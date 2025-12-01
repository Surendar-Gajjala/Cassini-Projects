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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{machine.number}}
            <span title="{{machine.name.length > 30 ? machine.name : ' '}}"> {{machine.name | limitTo:30}} {{machine.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.machine.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="machineDetailsVm.tabs.workflow.active"
                ng-show="!machine.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>
        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(machineDetailsVm.machineId,'MACHINE')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="machineDetailsVm.tabs.files.active && hasFiles == true"
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
                <li ng-click="clearAndCopyChangeFilesToClipBoard()"><a href=""
                                                                       translate>CLEAR_AND_ADD_FILES</a>
                </li>
                <li ng-click="copyChangeFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                    ({{clipBoardObjectFiles.length}})</a></li>
            </ul>
        </div>
        <comments-btn ng-if="!machineDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!machineDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="machineDetailsVm.tabs.files.active" on-clear="machineDetailsVm.onClear"
                          on-search="machineDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="machineDetailsVm.active">
                        <uib-tab heading="{{machineDetailsVm.tabs.basic.heading}}"
                                 active="machineDetailsVm.tabs.basic.active"
                                 select="machineDetailsVm.tabActivated(machineDetailsVm.tabs.basic.id)">
                            <div ng-include="machineDetailsVm.tabs.basic.template"
                                 ng-controller="MachineBasicInfoController as machineBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{machineDetailsVm.tabs.attributes.heading}}"
                                 active="machineDetailsVm.tabs.attributes.active"
                                 select="machineDetailsVm.tabActivated(machineDetailsVm.tabs.attributes.id)">
                            <div ng-include="machineDetailsVm.tabs.attributes.template"
                                 ng-controller="MachineAttributesController as machineAttributesVm"></div>
                        </uib-tab>--%>
                        <!-- <uib-tab heading="{{machineDetailsVm.tabs.relatedItem.heading}}"
                                 active="machineDetailsVm.tabs.relatedItem.active"
                                 select="machineDetailsVm.tabActivated(machineDetailsVm.tabs.relatedItem.id)">
                            <div ng-include="machineDetailsVm.tabs.relatedItem.template"
                                 ng-controller="MachineRelatedItemController as machineRelatedItemVm"></div>
                        </uib-tab> -->
                        <uib-tab id="files" heading="{{machineDetailsVm.tabs.files.heading}}"
                                 active="machineDetailsVm.tabs.files.active"
                                 select="machineDetailsVm.tabActivated(machineDetailsVm.tabs.files.id)">
                            <div ng-include="machineDetailsVm.tabs.files.template"
                                 ng-controller="MachineFilesController as machineFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="machineDetailsVm.tabs" custom-tabs="machineDetailsVm.customTabs"
                                     object-value="machineDetailsVm.machine" tab-id="machineDetailsVm.tabId" active="machineDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{machineDetailsVm.tabs.timelineHistory.heading}}"
                                 active="machineDetailsVm.tabs.timelineHistory.active"
                                 select="machineDetailsVm.tabActivated(machineDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="machineDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="MachineTimeLineController as machineTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
