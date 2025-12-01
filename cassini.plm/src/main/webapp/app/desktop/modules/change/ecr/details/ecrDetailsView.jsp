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
        <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{ecr.crNumber}}
            <span title="{{ecr.title.length > 30 ? ecr.title : ' '}}"> {{ecr.title | limitTo:30}} {{ecr.title.length > 30 ? '...' : ' '}}</span>
        </span> -->

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.changes.ecr.all')"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>


            <button ng-if="ecrDetailsVm.tabs.files.active && hasPermission('change','ecr','edit') && hasFiles == true"
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

            <button ng-if="ecrDetailsVm.tabs.workflow.active"
                    ng-show="!ecr.startWorkflow"
                    title="{{ecrDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="ecrDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <button
                    title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                    class="btn btn-sm" ng-click="showPrintOptions(ecrDetailsVm.ecrId,'ECR')">
                <i class="fa fa-print" aria-hidden="true" style=""></i>
            </button>
        </div>
        <!-- <comments-btn ng-if="!ecrDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!ecrDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn> -->
        <free-text-search ng-if="ecrDetailsVm.tabs.files.active" on-clear="ecrDetailsVm.onClear"
                          on-search="ecrDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="ecrDetailsVm.active">
                        <uib-tab heading="{{ecrDetailsVm.tabs.basic.heading}}"
                                 active="ecrDetailsVm.tabs.basic.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.basic.id)">
                            <div ng-include="ecrDetailsVm.tabs.basic.template"
                                 ng-controller="ECRBasicInfoController as ecrBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab id="attributes"
                                 heading="{{ecrDetailsVm.tabs.attributes.heading}}"
                                 active="ecrDetailsVm.tabs.attributes.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.attributes.id)">
                            <div ng-include="ecrDetailsVm.tabs.attributes.template"
                                 ng-controller="ECRAttributesController as ecrAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="problemReports" heading="{{ecrDetailsVm.tabs.problemReports.heading}}"
                                 active="ecrDetailsVm.tabs.problemReports.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.problemReports.id)">
                            <div ng-include="ecrDetailsVm.tabs.problemReports.template"
                                 ng-controller="ECRProblemReportsController as ecrProblemReportsVm"></div>
                        </uib-tab>
                        <uib-tab id="affectedItems" heading="{{ecrDetailsVm.tabs.affectedItems.heading}}"
                                 active="ecrDetailsVm.tabs.affectedItems.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.affectedItems.id)">
                            <div ng-include="ecrDetailsVm.tabs.affectedItems.template"
                                 ng-controller="ECRAffectedItemsController as ecrAffectedItemsVm"></div>
                        </uib-tab>
                        <uib-tab id="relatedItems" heading="{{ecrDetailsVm.tabs.relatedItems.heading}}"
                                 active="ecrDetailsVm.tabs.relatedItems.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.relatedItems.id)">
                            <div ng-include="ecrDetailsVm.tabs.relatedItems.template"
                                 ng-controller="ECRRelatedItemsController as ecrRelatedItemsVm"></div>
                        </uib-tab>

                        <uib-tab id="workflow" heading="{{ecrDetailsVm.tabs.workflow.heading}}"
                                 active="ecrDetailsVm.tabs.workflow.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.workflow.id)">
                            <div ng-include="ecrDetailsVm.tabs.workflow.template"
                                 ng-controller="ECRWorkflowController as ecrWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{ecrDetailsVm.tabs.files.heading}}"
                                 active="ecrDetailsVm.tabs.files.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.files.id)">
                            <div ng-include="ecrDetailsVm.tabs.files.template"
                                 ng-controller="ECRFilesController as ecrFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="ecrDetailsVm.tabs" custom-tabs="ecrDetailsVm.customTabs"
                                     object-value="ecrDetailsVm.ecr" tab-id="ecrDetailsVm.tabId" active="ecrDetailsVm.active"></plugin-tabs>
                        <uib-tab id="ecoHistory" heading="{{ecrDetailsVm.tabs.timeLine.heading}}"
                                 active="ecrDetailsVm.tabs.timeLine.active"
                                 select="ecrDetailsVm.tabActivated(ecrDetailsVm.tabs.timeLine.id)">
                            <div ng-include="ecrDetailsVm.tabs.timeLine.template"
                                 ng-controller="ECRTimeLineController as ecrTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
