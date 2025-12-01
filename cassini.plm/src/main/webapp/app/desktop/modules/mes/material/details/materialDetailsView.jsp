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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{material.number}}
            <span title="{{material.name.length > 30 ? material.name : ' '}}"> {{material.name | limitTo:30}} {{material.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.material.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="materialDetailsVm.tabs.workflow.active"
                ng-show="!material.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>
        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(materialDetailsVm.materialId,'MATERIAL')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="materialDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!materialDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!materialDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="materialDetailsVm.tabs.files.active" on-clear="materialDetailsVm.onClear"
                          on-search="materialDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="materialDetailsVm.active">
                        <uib-tab heading="{{materialDetailsVm.tabs.basic.heading}}"
                                 active="materialDetailsVm.tabs.basic.active"
                                 select="materialDetailsVm.tabActivated(materialDetailsVm.tabs.basic.id)">
                            <div ng-include="materialDetailsVm.tabs.basic.template"
                                 ng-controller="MaterialBasicInfoController as materialBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{materialDetailsVm.tabs.attributes.heading}}"
                                 active="materialDetailsVm.tabs.attributes.active"
                                 select="materialDetailsVm.tabActivated(materialDetailsVm.tabs.attributes.id)">
                            <div ng-include="materialDetailsVm.tabs.attributes.template"
                                 ng-controller="MaterialAttributesController as materialAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="files" heading="{{materialDetailsVm.tabs.files.heading}}"
                                 active="materialDetailsVm.tabs.files.active"
                                 select="materialDetailsVm.tabActivated(materialDetailsVm.tabs.files.id)">
                            <div ng-include="materialDetailsVm.tabs.files.template"
                                 ng-controller="MaterialFilesController as materialFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="materialDetailsVm.tabs" custom-tabs="materialDetailsVm.customTabs"
                                     object-value="materialDetailsVm.material" tab-id="materialDetailsVm.tabId" active="materialDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{materialDetailsVm.tabs.timelineHistory.heading}}"
                                 active="materialDetailsVm.tabs.timelineHistory.active"
                                 select="materialDetailsVm.tabActivated(materialDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="materialDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="MaterialTimeLineController as materialTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
