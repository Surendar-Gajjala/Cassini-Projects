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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
              title="{{maintenancePlan.number}} {{maintenancePlan.name}}">{{maintenancePlan.number}}
            <span title="{{maintenancePlan.name.length > 30 ? maintenancePlan.name : ' '}}"> {{maintenancePlan.name | limitTo:30}} {{maintenancePlan.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mro.maintenancePlan.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="maintenancePlanDetailsVm.tabs.workflow.active"
                ng-show="!maintenancePlan.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>
        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm"
                ng-click="showPrintOptions(maintenancePlanDetailsVm.maintenancePlanId,'MROMAINTENANCEPLAN')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="maintenancePlanDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!maintenancePlanDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!maintenancePlanDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="maintenancePlanDetailsVm.tabs.files.active" on-clear="maintenancePlanDetailsVm.onClear"
                          on-search="maintenancePlanDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="maintenancePlanDetailsVm.active">
                        <uib-tab heading="{{maintenancePlanDetailsVm.tabs.basic.heading}}"
                                 active="maintenancePlanDetailsVm.tabs.basic.active"
                                 select="maintenancePlanDetailsVm.tabActivated(maintenancePlanDetailsVm.tabs.basic.id)">
                            <div ng-include="maintenancePlanDetailsVm.tabs.basic.template"
                                 ng-controller="MaintenancePlanBasicInfoController as maintenancePlanBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="operations" heading="{{maintenancePlanDetailsVm.tabs.operations.heading}}"
                                 active="maintenancePlanDetailsVm.tabs.operations.active"
                                 select="maintenancePlanDetailsVm.tabActivated(maintenancePlanDetailsVm.tabs.operations.id)">
                            <div ng-include="maintenancePlanDetailsVm.tabs.operations.template"
                                 ng-controller="MaintenancePlanOperationsController as maintenancePlanOperationVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{maintenancePlanDetailsVm.tabs.files.heading}}"
                                 active="maintenancePlanDetailsVm.tabs.files.active"
                                 select="maintenancePlanDetailsVm.tabActivated(maintenancePlanDetailsVm.tabs.files.id)">
                            <div ng-include="maintenancePlanDetailsVm.tabs.files.template"
                                 ng-controller="MaintenancePlanFilesController as maintenancePlanFilesVm"></div>
                        </uib-tab>

                        <%--<uib-tab id="workflow" heading="{{maintenancePlanDetailsVm.tabs.workflow.heading}}"
                                 active="maintenancePlanDetailsVm.tabs.workflow.active"
                                 select="maintenancePlanDetailsVm.tabActivated(maintenancePlanDetailsVm.tabs.workflow.id)">
                            <div ng-include="maintenancePlanDetailsVm.tabs.workflow.template"
                                 ng-controller="MaintenancePlanWorkflowController as maintenancePlanWorkflowVm"></div>
                        </uib-tab>--%>

                        <plugin-tabs tabs="maintenancePlanDetailsVm.tabs"
                                     custom-tabs="maintenancePlanDetailsVm.customTabs"
                                     object-value="maintenancePlanDetailsVm.maintenancePlan"
                                     tab-id="maintenancePlanDetailsVm.tabId"
                                     active="maintenancePlanDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{maintenancePlanDetailsVm.tabs.timelineHistory.heading}}"
                                 active="maintenancePlanDetailsVm.tabs.timelineHistory.active"
                                 select="maintenancePlanDetailsVm.tabActivated(maintenancePlanDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="maintenancePlanDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="MaintenancePlanTimeLineController as maintenancePlanTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
