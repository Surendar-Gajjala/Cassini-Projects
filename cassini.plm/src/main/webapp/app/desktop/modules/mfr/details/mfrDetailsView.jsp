<div class="view-container" fitcontent>
    <style scoped>
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
            top: 8px !important;
        }

    </style>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group" ng-if="loginPersonDetails.external == false">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.mfr.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>

                <button class="btn btn-default btn-sm"
                        ng-click="mfrDetailsVm.refreshDetails()"
                        title="{{mfrDetailsVm.refreshTitle}}">
                    <i class="fa fa-refresh"></i>
                </button>
                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm" ng-click="showPrintOptions(mfrDetailsVm.mfrId,'MANUFACTURER')">
                    <i class="fa fa-print" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="mfrDetailsVm.tabs.files.active && hasPermission('manufacturer','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download"></i>
                </button>
                <button class="btn btn-sm btn-success"
                        title="{{hasPermission('manufacturer','demote') ? 'Demote' : noPermission}}"
                        ng-hide="mfr.lifeCyclePhase.phase == mfrDetailsVm.firstLifecyclePhase.phase"
                        ng-class="{'cursor-override': !hasPermission('manufacturer','demote')}"
                        ng-click="demoteMfr()">
                    <i class="fa fa-toggle-left"
                       ng-class="{'disabled': !hasPermission('manufacturer','demote')}"
                       style=""></i>
                </button>
                <button class="btn btn-sm btn-success"
                        title="{{hasPermission('manufacturer','promote') ? 'Promote' : noPermission}}"
                        ng-if="mfr.lifeCyclePhase.phase != mfrDetailsVm.lastLifecyclePhase.phase"
                        ng-class="{'cursor-override': !hasPermission('manufacturer','promote')}"
                        ng-click="promoteMfr()">
                    <i class="fa fa-toggle-right"
                       ng-class="{'disabled': !hasPermission('manufacturer','promote')}"
                       style=""></i>
                </button>

                <button class="btn btn-default btn-sm"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy"></i>
                </button>
                <button class="btn btn-sm btn-success"
                        title="{{mfrDetailsVm.addWorkflowTitle}}"
                        ng-show="mfrDetailsVm.tabs.workflow.active && mfr.workflow == null && hasPermission('manufacturer','edit')"
                        ng-click="mfrDetailsVm.addWorkflow()">
                    <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="mfrDetailsVm.tabs.workflow.active"
                        ng-show="mfr.workflow != null && mfr.startWorkflow != true && hasPermission('manufacturer','edit')"
                        title="{{mfrDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="mfrDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
                <%--<button class="btn btn-sm btn-default" ng-if="hasPermission('manufacturer','edit')"
                        ng-click="mfrDetailsVm.shareMfr()"
                        style=""
                        title="{{mfrDetailsVm.detailsShareTitle}}">
                    <i class="fa fa-share-alt" style=""></i></button>--%>
                <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy"></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href="" translate>CLEAR_AND_ADD_FILES</a>
                        </li>
                        <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span
                                translate>ADD_TO_EXISTING_FILES</span> ({{clipBoardObjectFiles.length}})</a></li>
                    </ul>
                </div>
            </div>
            <div ng-if="loginPersonDetails.external == true">
                <div class="btn-group">
                    <button class="btn btn-sm btn-default" ng-click='mfrDetailsVm.showExternalUserMfrs()'
                            title="{{'SHOW_ALL' | translate}}">
                        <i class="fa fa-table" aria-hidden="true"></i>
                    </button>
                </div>
            </div>
            <div ng-show="mfrDetailsVm.tabs.parts.active">
                <free-text-search on-clear="mfrDetailsVm.resetPage" search-term="mfrPartFreeTextSearchText"
                                  on-search="mfrDetailsVm.freeTextSearch"></free-text-search>
            </div>

            <div ng-show="mfrDetailsVm.tabs.files.active">
                <free-text-search on-clear="mfrDetailsVm.resetPage"
                                  search-term="freeTextQuerys"
                                  on-search="mfrDetailsVm.freeTextSearchFile"></free-text-search>
            </div>
        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="mfrDetailsVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC'| translate}}"
                                 active="mfrDetailsVm.tabs.basic.active"
                                 select="mfrDetailsVm.mfrDetailsTabActivated(mfrDetailsVm.tabs.basic.id)">
                            <div ng-include="mfrDetailsVm.tabs.basic.template"
                                 ng-controller="MfrBasicController as mfrBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                 active="mfrDetailsVm.tabs.attributes.active"
                                 select="mfrDetailsVm.mfrDetailsTabActivated(mfrDetailsVm.tabs.attributes.id)">
                            <div ng-include="mfrDetailsVm.tabs.attributes.template"
                                 ng-controller="MfrAttributesController as mfrAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="parts1" heading="{{'MANUFACTURER_DETAILS_TAB_PARTS' | translate}}"
                                 active="mfrDetailsVm.tabs.parts.active"
                                 select="mfrDetailsVm.mfrDetailsTabActivated(mfrDetailsVm.tabs.parts.id)">
                            <div ng-include="mfrDetailsVm.tabs.parts.template"
                                 ng-controller="MfrPartsController as mfrPartsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 active="mfrDetailsVm.tabs.files.active"
                                 select="mfrDetailsVm.mfrDetailsTabActivated(mfrDetailsVm.tabs.files.id)">
                            <div ng-include="mfrDetailsVm.tabs.files.template"
                                 ng-controller="MfrFilesController as mfrFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="Workflow"
                                 active="mfrDetailsVm.tabs.workflow.active"
                                 select="mfrDetailsVm.mfrDetailsTabActivated(mfrDetailsVm.tabs.workflow.id)">
                            <div ng-include="mfrDetailsVm.tabs.workflow.template"
                                 ng-controller="ManufacturerWorkflowController as mfrWfVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="mfrDetailsVm.tabs" custom-tabs="mfrDetailsVm.customTabs"
                                     object-value="mfrDetailsVm.mfr" tab-id="mfrDetailsVm.tabId" active="mfrDetailsVm.active"></plugin-tabs>
                        <uib-tab id="timeline"
                                 heading="{{mfrDetailsVm.tabs.timelineHistory.heading}}"
                                 active="mfrDetailsVm.tabs.timelineHistory.active"
                                 select="mfrDetailsVm.mfrDetailsTabActivated(mfrDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="mfrDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="MFRTimelineHistoryController as mfrTimelineHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
