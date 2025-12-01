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

    </style>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{variance.varianceNumber}}
            <span title="{{variance.title.length > 30 ? variance.title : ' '}}"> {{variance.title | limitTo:30}} {{variance.title.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ui-sref="app.changes.variance.all({varianceMode:'deviation'})"
                    ng-if="loginPersonDetails.external == false && varianceType == 'Deviation'"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

            <button class="btn btn-sm btn-default" ui-sref="app.changes.variance.all({varianceMode:'waiver'})"
                    ng-if="loginPersonDetails.external == false && varianceType == 'Waiver'"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

            <button ng-if="varianceDetailsVm.tabs.workflow.active"
                    ng-show="!variance.startWorkflow"
                    title="{{varianceDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="varianceDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="varianceDetailsVm.tabs.files.active && hasPermission('change','variance','edit') && hasFiles == true"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                <i class="fa fa-download" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                    ng-click="copyChangeFilesToClipBoard()" title="{{copyFileToClipboard}}">
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

            <button ng-if="varianceDetailsVm.variance.varianceType == 'DEVIATION'"
                    title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                    class="btn btn-sm" ng-click="showPrintOptions(varianceDetailsVm.varianceId,'DEVIATION')">
                <i class="fa fa-print" aria-hidden="true" style=""></i>
            </button>

            <button ng-if="varianceDetailsVm.variance.varianceType == 'WAIVER'"
                    title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                    class="btn btn-sm" ng-click="showPrintOptions(varianceDetailsVm.varianceId,'WAIVER')">
                <i class="fa fa-print" aria-hidden="true" style=""></i>
            </button>

        </div>
        <div class="pull-right">

        </div>

        <comments-btn ng-if="!varianceDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!varianceDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="varianceDetailsVm.tabs.files.active" on-clear="varianceDetailsVm.onClear"
                          on-search="varianceDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="varianceDetailsVm.active">
                        <uib-tab heading="{{varianceDetailsVm.tabs.basic.heading}}"
                                 active="varianceDetailsVm.tabs.basic.active"
                                 select="varianceDetailsVm.tabActivated(varianceDetailsVm.tabs.basic.id)">
                            <div ng-include="varianceDetailsVm.tabs.basic.template"
                                 ng-controller="VarianceBasicInfoController as varianceBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{varianceDetailsVm.tabs.attributes.heading}}"
                                 active="varianceDetailsVm.tabs.attributes.active"
                                 select="varianceDetailsVm.tabActivated(varianceDetailsVm.tabs.attributes.id)">
                            <div ng-include="varianceDetailsVm.tabs.attributes.template"
                                 ng-controller="VarianceAttributesController as varianceAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="affectedItems" heading="{{varianceDetailsVm.tabs.affecteditems.heading}}"
                                 active="varianceDetailsVm.tabs.affecteditems.active"
                                 select="varianceDetailsVm.tabActivated(varianceDetailsVm.tabs.affecteditems.id)">
                            <div ng-include="varianceDetailsVm.tabs.affecteditems.template"
                                 ng-controller="VarianceAffectedItemsController as varianceAffectedItemsVm"></div>
                        </uib-tab>
                        <uib-tab id="relatedItems" heading="{{varianceDetailsVm.tabs.relateditems.heading}}"
                                 active="varianceDetailsVm.tabs.relateditems.active"
                                 select="varianceDetailsVm.tabActivated(varianceDetailsVm.tabs.relateditems.id)">
                            <div ng-include="varianceDetailsVm.tabs.relateditems.template"
                                 ng-controller="VarianceRelatedItemsController as varianceRelatedItemsVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{varianceDetailsVm.tabs.workflow.heading}}"
                                 active="varianceDetailsVm.tabs.workflow.active"
                                 select="varianceDetailsVm.tabActivated(varianceDetailsVm.tabs.workflow.id)">
                            <div ng-include="varianceDetailsVm.tabs.workflow.template"
                                 ng-controller="VarianceWorkflowController as varianceWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{varianceDetailsVm.tabs.files.heading}}"
                                 active="varianceDetailsVm.tabs.files.active"
                                 select="varianceDetailsVm.tabActivated(varianceDetailsVm.tabs.files.id)">
                            <div ng-include="varianceDetailsVm.tabs.files.template"
                                 ng-controller="VarianceFilesController as varianceFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="varianceDetailsVm.tabs" custom-tabs="varianceDetailsVm.customTabs"
                                     object-value="varianceDetailsVm.variance" tab-id="varianceDetailsVm.tabId" active="varianceDetailsVm.active"></plugin-tabs>
                        <uib-tab id="History" heading="{{varianceDetailsVm.tabs.history.heading}}"
                                 active="varianceDetailsVm.tabs.history.active"
                                 select="varianceDetailsVm.tabActivated(varianceDetailsVm.tabs.history.id)">
                            <div ng-include="varianceDetailsVm.tabs.history.template"
                                 ng-controller="VarianceTimeLineController as varianceTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
