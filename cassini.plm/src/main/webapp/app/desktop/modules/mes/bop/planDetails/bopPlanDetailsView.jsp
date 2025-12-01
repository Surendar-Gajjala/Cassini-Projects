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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{bopPlan.sequenceNumber}}
            <span title="{{bopPlan.name.length > 30 ? bopPlan.name : ' '}}"> {{bopPlan.name | limitTo:30}} {{bopPlan.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    ng-click="bopPlanDetailsVm.showBopDetails()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button ng-if="bopPlanDetailsVm.tabs.instructions.active" title="Save" class="btn btn-sm btn-default"
                    ng-click="saveBopPlanInstructions()">
                <i class="fa fa-save"></i>
            </button>
        </div>

        <%--<button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(bopPlanDetailsVm.equipmentId,'EQUIPMENT')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>--%>

        <button ng-if="bopPlanDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!bopPlanDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!bopPlanDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="bopPlanDetailsVm.tabs.files.active" on-clear="bopPlanDetailsVm.onClear"
                          on-search="bopPlanDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="bopPlanDetailsVm.active">
                        <uib-tab heading="{{bopPlanDetailsVm.tabs.basic.heading}}"
                                 active="bopPlanDetailsVm.tabs.basic.active"
                                 select="bopPlanDetailsVm.tabActivated(bopPlanDetailsVm.tabs.basic.id)">
                            <div ng-include="bopPlanDetailsVm.tabs.basic.template"
                                 ng-controller="BOPPlanBasicInfoController as bopPlanBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="plan-parts" heading="{{bopPlanDetailsVm.tabs.items.heading}}"
                                 active="bopPlanDetailsVm.tabs.items.active"
                                 select="bopPlanDetailsVm.tabActivated(bopPlanDetailsVm.tabs.items.id)">
                            <div ng-include="bopPlanDetailsVm.tabs.items.template"
                                 ng-controller="BOPPlanItemsController as bopPlanItemsVm"></div>
                        </uib-tab>
                        <uib-tab id="plan-resources" heading="{{bopPlanDetailsVm.tabs.resources.heading}}"
                                 active="bopPlanDetailsVm.tabs.resources.active"
                                 select="bopPlanDetailsVm.tabActivated(bopPlanDetailsVm.tabs.resources.id)">
                            <div ng-include="bopPlanDetailsVm.tabs.resources.template"
                                 ng-controller="BOPPlanResourcesController as bopPlanResourcesVm"></div>
                        </uib-tab>
                        <uib-tab id="instructions"
                                 heading="{{bopPlanDetailsVm.tabs.instructions.heading}}"
                                 active="bopPlanDetailsVm.tabs.instructions.active"
                                 select="bopPlanDetailsVm.tabActivated(bopPlanDetailsVm.tabs.instructions.id)">
                            <div ng-include="bopPlanDetailsVm.tabs.instructions.template"
                                 ng-controller="BOPPlanInstructionsController as bopPlanInstructionsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{bopPlanDetailsVm.tabs.files.heading}}"
                                 active="bopPlanDetailsVm.tabs.files.active"
                                 select="bopPlanDetailsVm.tabActivated(bopPlanDetailsVm.tabs.files.id)">
                            <div ng-include="bopPlanDetailsVm.tabs.files.template"
                                 ng-controller="BOPPlanFilesController as bopPlanFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="" heading="{{bopPlanDetailsVm.tabs.timeline.heading}}"
                                 active="bopPlanDetailsVm.tabs.timeline.active"
                                 select="bopPlanDetailsVm.tabActivated(bopPlanDetailsVm.tabs.timeline.id)">
                            <div ng-include="bopPlanDetailsVm.tabs.timeline.template"
                                 ng-controller="BOPPlanTimeLineController as bopPlanTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
