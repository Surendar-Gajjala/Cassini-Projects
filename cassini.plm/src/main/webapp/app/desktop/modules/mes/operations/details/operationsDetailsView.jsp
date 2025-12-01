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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{operation.number}}
            <span title="{{operation.name.length > 30 ? operation.name : ' '}}"> {{operation.name | limitTo:30}} {{operation.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.operation.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="operationDetailsVm.tabs.workflow.active"
                ng-show="!plant.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(operationDetailsVm.operationId,'OPERATION')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="operationDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!operationDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!operationDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="operationDetailsVm.tabs.files.active" on-clear="operationDetailsVm.onClear"
                          on-search="operationDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="operationDetailsVm.active">
                        <uib-tab heading="{{operationDetailsVm.tabs.basic.heading}}"
                                 active="operationDetailsVm.tabs.basic.active"
                                 select="operationDetailsVm.tabActivated(operationDetailsVm.tabs.basic.id)">
                            <div ng-include="operationDetailsVm.tabs.basic.template"
                                 ng-controller="OperationBasicInfoController as operationBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{operationDetailsVm.tabs.resources.heading}}"
                        active="operationDetailsVm.tabs.resources.active"
                        select="operationDetailsVm.tabActivated(operationDetailsVm.tabs.resources.id)">
                   <div ng-include="operationDetailsVm.tabs.resources.template"
                        ng-controller="OperationResourcesController as operationResourcesVm"></div>
               </uib-tab>
                        <%--<uib-tab heading="{{operationDetailsVm.tabs.attributes.heading}}"
                                 active="operationDetailsVm.tabs.attributes.active"
                                 select="operationDetailsVm.tabActivated(operationDetailsVm.tabs.attributes.id)">
                            <div ng-include="operationDetailsVm.tabs.attributes.template"
                                 ng-controller="OperationsAttributesController as operationAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="files" heading="{{operationDetailsVm.tabs.files.heading}}"
                                 active="operationDetailsVm.tabs.files.active"
                                 select="operationDetailsVm.tabActivated(operationDetailsVm.tabs.files.id)">
                            <div ng-include="operationDetailsVm.tabs.files.template"
                                 ng-controller="OperationFilesController as operationFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="operationDetailsVm.tabs" custom-tabs="operationDetailsVm.customTabs"
                                     object-value="operationDetailsVm.operation" tab-id="operationDetailsVm.tabId" active="operationDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{operationDetailsVm.tabs.timelineHistory.heading}}"
                                 active="operationDetailsVm.tabs.timelineHistory.active"
                                 select="operationDetailsVm.tabActivated(operationDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="operationDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="OperationTimeLineController as operationTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
