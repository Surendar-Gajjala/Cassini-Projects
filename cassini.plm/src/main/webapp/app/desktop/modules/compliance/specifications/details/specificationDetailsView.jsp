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
        <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{specification.number}}
            <span title="{{specification.name.length > 30 ? specification.name : ' '}}"> {{specification.name | limitTo:30}} {{specification.name.length > 30 ? '...' : ' '}}</span>
        </span> -->

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.compliance.specification.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm"
                ng-click="showPrintOptions(specificationDetailsVm.specification.id,'PGCSPECIFICATION')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="specificationDetailsVm.tabs.workflow.active"
                ng-show="!specification.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="specificationDetailsVm.tabs.files.active && hasFiles == true"
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
        <!-- <comments-btn ng-if="!specificationDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!specificationDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn> -->
        <free-text-search ng-if="specificationDetailsVm.tabs.files.active" on-clear="specificationDetailsVm.onClear"
                          on-search="specificationDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="specificationDetailsVm.active">
                        <uib-tab heading="{{specificationDetailsVm.tabs.basic.heading}}"
                                 active="specificationDetailsVm.tabs.basic.active"
                                 select="specificationDetailsVm.tabActivated(specificationDetailsVm.tabs.basic.id)">
                            <div ng-include="specificationDetailsVm.tabs.basic.template"
                                 ng-controller="SpecificationBasicInfoController as specificationBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{specificationDetailsVm.tabs.attributes.heading}}"
                                 active="specificationDetailsVm.tabs.attributes.active"
                                 select="specificationDetailsVm.tabActivated(specificationDetailsVm.tabs.attributes.id)">
                            <div ng-include="specificationDetailsVm.tabs.attributes.template"
                                 ng-controller="SpecificationAttributesController as specificationAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="Substances" heading="{{specificationDetailsVm.tabs.substances.heading}}"
                                 active="specificationDetailsVm.tabs.substance.active"
                                 select="specificationDetailsVm.tabActivated(specificationDetailsVm.tabs.substances.id)">
                            <div ng-include="specificationDetailsVm.tabs.substances.template"
                                 ng-controller="SpecificationSubstancesController as specSubstanceVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{specificationDetailsVm.tabs.files.heading}}"
                                 active="specificationDetailsVm.tabs.files.active"
                                 select="specificationDetailsVm.tabActivated(specificationDetailsVm.tabs.files.id)">
                            <div ng-include="specificationDetailsVm.tabs.files.template"
                                 ng-controller="SpecificationFilesController as specificationFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="specificationDetailsVm.tabs" custom-tabs="specificationDetailsVm.customTabs"
                                     object-value="specificationDetailsVm.specification" tab-id="specificationDetailsVm.tabId" active="specificationDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{specificationDetailsVm.tabs.timelineHistory.heading}}"
                                 active="specificationDetailsVm.tabs.timelineHistory.active"
                                 select="specificationDetailsVm.tabActivated(specificationDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="specificationDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="SpecificationTimeLineController as specificationTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
