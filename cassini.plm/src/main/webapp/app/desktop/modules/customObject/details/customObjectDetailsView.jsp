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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{customObject.number}}
           <span title="{{customObject.name.length > 30 ? eco.title : ' '}}"> {{eco.name | limitTo:30}} {{eco.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-if="!loginPersonDetails.external"
                    ng-click="customObjectVm.showAllCustomObjects()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-default" ng-if="loginPersonDetails.external == true"
                    ng-click='customObjectVm.showExternalUserSuppliers()' title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{customObjectVm.addWorkflowTitle}}"
                    ng-show="customObjectVm.tabs.workflow.active && customObject.workflow == null"
                    ng-click="customObjectVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>
            <button ng-show="customObjectVm.tabs.workflow.active && customObject.startWorkflow != true && customObject.workflow != null"
                    title="{{customObjectVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="customObjectVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>

            <button ng-if="customObjectVm.tabs.files.active && hasFiles == true"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="downloadCustomFilesFilesAsZip()">
                <i class="fa fa-download" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="showCopyCustomFilesToClipBoard && clipBoardCustomFiles.length == 0"
                    ng-click="copyCustomFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>
            <div class="btn-group" ng-show="showCopyCustomFilesToClipBoard && clipBoardCustomFiles.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyCustomFilesToClipBoard()"><a href=""
                                                                           translate>CLEAR_AND_ADD_FILES</a>
                    </li>
                    <li ng-click="copyCustomFilesToClipBoard()"><a href=""><span
                            translate>ADD_TO_EXISTING_FILES</span> ({{clipBoardCustomFiles.length}})</a></li>
                </ul>
            </div>
        </div>


        <div class="pull-right" ng-class="{'pull-right-300':customObjectVm.tabs.files.active == true}">

            <%--<button class="btn btn-sm btn-default" title="Share"
                    ng-click="customObjectVm.shareCustomObject()"
                    title="{{customObjectVm.detailsShareTitle}}">
                <i class="las la-share"></i></button>--%>
        </div>

        <comments-btn ng-if="!customObjectVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!customObjectVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="customObjectVm.tabs.files.active" on-clear="customObjectVm.onClear"
                          on-search="customObjectVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="customObjectVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="customObjectVm.tabs.basic.active"
                                 select="tabActivated(customObjectVm.tabs.basic.id)">
                            <div ng-include="customObjectVm.tabs.basic.template"
                                 ng-controller="CustomObjectBasicInfoController as customObjectBasicVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="customObjectVm.tabs" custom-tabs="customObjectVm.customTabs"
                                     object-value="customObjectVm.customObject" tab-id="customObjectVm.tabId"
                                     active="customObjectVm.active"></plugin-tabs>

                        <uib-tab id="custom-bom" heading="{{'BOM' | translate}}"
                                 ng-show="customObjectVm.hasDisplayTab('bom')"
                                 active="customObjectVm.tabs.bom.active"
                                 select="tabActivated(customObjectVm.tabs.bom.id)">
                            <div ng-include="customObjectVm.tabs.bom.template"
                                 ng-controller="CustomObjectBomController as customObjectBomVm"></div>
                        </uib-tab>
                        <uib-tab id="custom-whereused" heading="{{'DETAILS_TAB_WHERE_USED' | translate}}"
                                 ng-show="customObjectVm.hasDisplayTab('whereUsed')"
                                 active="customObjectVm.tabs.whereUsed.active"
                                 select="tabActivated(customObjectVm.tabs.whereUsed.id)">
                            <div ng-include="customObjectVm.tabs.whereUsed.template"
                                 ng-controller="CustomObjectWhereUsedController as customObjectWhereUsedVm"></div>
                        </uib-tab>
                        <uib-tab id="custom-related" heading="{{'RELATED_DETAILS_TAB_OBJECT' | translate}}"
                                 ng-show="customObjectVm.hasDisplayTab('relatedObjects')"
                                 active="customObjectVm.tabs.related.active"
                                 select="tabActivated(customObjectVm.tabs.related.id)">
                            <div ng-include="customObjectVm.tabs.related.template"
                                 ng-controller="CustomObjectRelatedController as customObjectRelatedVm"></div>
                        </uib-tab>
                        <uib-tab id="custom-files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 active="customObjectVm.tabs.files.active"
                                 select="tabActivated(customObjectVm.tabs.files.id)">
                            <div ng-include="customObjectVm.tabs.files.template"
                                 ng-controller="CustomObjectFilesController as customObjectFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="Workflow"
                                 active="customObjectVm.tabs.workflow.active"
                                 select="tabActivated(customObjectVm.tabs.workflow.id)">
                            <div ng-include="customObjectVm.tabs.workflow.template"
                                 ng-controller="CustomObjectWorkflowController as customObjectWorkflowVm"></div>
                        </uib-tab>


                        <uib-tab id="" heading="{{customObjectVm.tabs.timelineHistory.heading}}"
                                 active="customObjectVm.tabs.timelineHistory.active"
                                 select="tabActivated(customObjectVm.tabs.timelineHistory.id)">
                            <div ng-include="customObjectVm.tabs.timelineHistory.template"
                                 ng-controller="CustomObjectTimeLineController as customObjectTimeLineVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
