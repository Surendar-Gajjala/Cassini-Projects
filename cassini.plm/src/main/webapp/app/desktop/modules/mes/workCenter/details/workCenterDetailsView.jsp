<div>
    <style scoped>
        .item-number {
            display: inline-block;
        }

        .item-rev {
            font-size: 16px;
            font-weight: normal;
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

        #freeTextSearchDirective {
            top: 7px !important;
        }
    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{workCenter.number}}
            <span title="{{workCenter.name.length > 30 ? workCenter.name : ' '}}"> {{workCenter.name | limitTo:30}} {{workCenter.name.length > 30 ? '...' : ' '}}</span>
        </span>

            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.workcenter.all')"
                        title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>

            </div>
            <button ng-if="workCenterDetailsVm.tabs.workflow.active"
                    ng-show="!workcenter.startWorkflow"
                    class="btn btn-sm btn-success">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <button
                    title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                    class="btn btn-sm" ng-click="showPrintOptions(workCenterDetailsVm.workCenterId,'WORKCENTER')">
                <i class="fa fa-print" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="workCenterDetailsVm.tabs.files.active && hasFiles == true"
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
            <comments-btn ng-if="!workCenterDetailsVm.tabs.files.active" id="details-comment"
                          object-type="mainVm.comments.objectType"
                          object-id="mainVm.comments.objectId"
                          comment-count="mainVm.comments.commentCount"></comments-btn>
            <tags-btn ng-if="!workCenterDetailsVm.tabs.files.active" id="details-tag"
                      object-type="mainVm.tags.objectType"
                      object="mainVm.tags.object"
                      tags-count="mainVm.tags.tagsCount"></tags-btn>
            <free-text-search ng-if="workCenterDetailsVm.tabs.files.active" on-clear="workCenterDetailsVm.onClear"
                              on-search="workCenterDetailsVm.freeTextSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <uib-tabset active="workCenterDetailsVm.active">
                            <uib-tab heading="{{workCenterDetailsVm.tabs.basic.heading}}"
                                     active="workCenterDetailsVm.tabs.basic.active"
                                     select="workCenterDetailsVm.tabActivated(workCenterDetailsVm.tabs.basic.id)">
                                <div ng-include="workCenterDetailsVm.tabs.basic.template"
                                     ng-controller="WorkCenterBasicInfoController as workCenterBasicVm"></div>
                            </uib-tab>
                            <%--<uib-tab heading="{{workCenterDetailsVm.tabs.attributes.heading}}"
                                     active="workCenterDetailsVm.tabs.attributes.active"
                                     select="workCenterDetailsVm.tabActivated(workCenterDetailsVm.tabs.attributes.id)">
                                <div ng-include="workCenterDetailsVm.tabs.attributes.template"
                                     ng-controller="WorkCenterAttributesController as workCenterAttributesVm"></div>
                            </uib-tab>--%>
                            <uib-tab heading="{{workCenterDetailsVm.tabs.resources.heading}}"
                                     active="workCenterDetailsVm.tabs.resources.active"
                                     select="workCenterDetailsVm.tabActivated(workCenterDetailsVm.tabs.resources.id)">
                                <div ng-include="workCenterDetailsVm.tabs.resources.template"
                                     ng-controller="WorkCenterResourcesController as workCenterResourcesVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{workCenterDetailsVm.tabs.operations.heading}}"
                                     active="workCenterDetailsVm.tabs.operations.active"
                                     select="workCenterDetailsVm.tabActivated(workCenterDetailsVm.tabs.operations.id)">
                                <div ng-include="workCenterDetailsVm.tabs.operations.template"
                                     ng-controller="WorkCenterOperationsController as workCenterOperationsVm"></div>
                            </uib-tab>
                            <uib-tab id="files" heading="{{workCenterDetailsVm.tabs.files.heading}}"
                                     active="workCenterDetailsVm.tabs.files.active"
                                     select="workCenterDetailsVm.tabActivated(workCenterDetailsVm.tabs.files.id)">
                                <div ng-include="workCenterDetailsVm.tabs.files.template"
                                     ng-controller="WorkCenterFilesController as workCenterFilesVm"></div>
                            </uib-tab>
                            <plugin-tabs tabs="workCenterDetailsVm.tabs" custom-tabs="workCenterDetailsVm.customTabs"
                                         object-value="workCenterDetailsVm.workCenter" tab-id="workCenterDetailsVm.tabId" active="workCenterDetailsVm.active"></plugin-tabs>
                            <uib-tab id="" heading="{{workCenterDetailsVm.tabs.timelineHistory.heading}}"
                                     active="workCenterDetailsVm.tabs.timelineHistory.active"
                                     select="workCenterDetailsVm.tabActivated(workCenterDetailsVm.tabs.timelineHistory.id)">
                                <div ng-include="workCenterDetailsVm.tabs.timelineHistory.template"
                                     ng-controller="WorkCenterTimeLineController as workCenterTimeLineVm"></div>
                            </uib-tab>

                        </uib-tabset>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
