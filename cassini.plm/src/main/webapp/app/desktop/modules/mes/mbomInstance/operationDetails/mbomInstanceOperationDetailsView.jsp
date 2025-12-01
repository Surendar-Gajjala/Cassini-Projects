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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{mbomInstanceOperationDetailsVm.sequenceNumber}}
            <span title="{{mbomInstanceOperation.name.length > 30 ? mbomInstanceOperation.name : ' '}}"> {{mbomInstanceOperationDetailsVm.name | limitTo:30}} {{name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    ng-click="mbomInstanceOperationDetailsVm.showBopDetails()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <!-- <button ng-if="mbomInstanceOperationDetailsVm.tabs.instructions.active" title="Save" class="btn btn-sm btn-default"
                    ng-click="savembomInstanceOperationInstructions()">
                <i class="fa fa-save"></i>
            </button> -->
        </div>

        <%--<button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(mbomInstanceOperationDetailsVm.equipmentId,'EQUIPMENT')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>--%>

        <button ng-if="mbomInstanceOperationDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!mbomInstanceOperationDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!mbomInstanceOperationDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="mbomInstanceOperationDetailsVm.tabs.files.active" on-clear="mbomInstanceOperationDetailsVm.onClear"
                          on-search="mbomInstanceOperationDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="mbomInstanceOperationDetailsVm.active">
                        <uib-tab heading="{{mbomInstanceOperationDetailsVm.tabs.basic.heading}}"
                                 active="mbomInstanceOperationDetailsVm.tabs.basic.active"
                                 select="mbomInstanceOperationDetailsVm.tabActivated(mbomInstanceOperationDetailsVm.tabs.basic.id)">
                            <div ng-include="mbomInstanceOperationDetailsVm.tabs.basic.template"
                                 ng-controller="MBOMInstanceOperationBasicInfoController as mbomInstanceOperationBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="plan-parts" heading="{{mbomInstanceOperationDetailsVm.tabs.items.heading}}"
                                 active="mbomInstanceOperationDetailsVm.tabs.items.active"
                                 select="mbomInstanceOperationDetailsVm.tabActivated(mbomInstanceOperationDetailsVm.tabs.items.id)">
                            <div ng-include="mbomInstanceOperationDetailsVm.tabs.items.template"
                                 ng-controller="MBOMInstanceOperationItemsController as mbomInstanceOperationItemsVm"></div>
                        </uib-tab>
                        <uib-tab id="plan-resources" heading="{{mbomInstanceOperationDetailsVm.tabs.resources.heading}}"
                                 active="mbomInstanceOperationDetailsVm.tabs.resources.active"
                                 select="mbomInstanceOperationDetailsVm.tabActivated(mbomInstanceOperationDetailsVm.tabs.resources.id)">
                            <div ng-include="mbomInstanceOperationDetailsVm.tabs.resources.template"
                                 ng-controller="MBOMInstanceOperationResourcesController as mbomInstanceOperationResourcesVm"></div>
                        </uib-tab>
                        <uib-tab id="instructions"
                                 heading="{{mbomInstanceOperationDetailsVm.tabs.instructions.heading}}"
                                 active="mbomInstanceOperationDetailsVm.tabs.instructions.active"
                                 select="mbomInstanceOperationDetailsVm.tabActivated(mbomInstanceOperationDetailsVm.tabs.instructions.id)">
                            <div ng-include="mbomInstanceOperationDetailsVm.tabs.instructions.template"
                                 ng-controller="MBOMInstanceOperationInstructionsController as mbomInstanceOperationInstructionsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{mbomInstanceOperationDetailsVm.tabs.files.heading}}"
                                 active="mbomInstanceOperationDetailsVm.tabs.files.active"
                                 select="mbomInstanceOperationDetailsVm.tabActivated(mbomInstanceOperationDetailsVm.tabs.files.id)">
                            <div ng-include="mbomInstanceOperationDetailsVm.tabs.files.template"
                                 ng-controller="MBOMInstanceOperationFilesController as mbomInstanceOperationFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="" heading="{{mbomInstanceOperationDetailsVm.tabs.timeline.heading}}"
                                 active="mbomInstanceOperationDetailsVm.tabs.timeline.active"
                                 select="mbomInstanceOperationDetailsVm.tabActivated(mbomInstanceOperationDetailsVm.tabs.timeline.id)">
                            <div ng-include="mbomInstanceOperationDetailsVm.tabs.timeline.template"
                                 ng-controller="MBOMInstanceOperationTimeLineController as mbomInstanceOperationTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
