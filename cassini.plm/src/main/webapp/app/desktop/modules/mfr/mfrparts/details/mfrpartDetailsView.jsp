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

        .mfr-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .configuration-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        .promote-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .promote-model .mfrPart-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .config-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .config-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close:before, .config-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .config-close:before {
            transform: rotate(45deg) !important;
        }

        .config-close:after {
            transform: rotate(-45deg) !important;
        }

        .mfr-footer {
            border-bottom: 1px solid lightgrey;
            padding: 5px;
            text-align: center;
            height: 50px;
            width: 100%;
        }

        .pull-right-300 {
            margin-right: 300px !important;
        }
    </style>
    <div class="view-toolbar">
        <div class="row">
            <div class="btn-group" ng-if="loginPersonDetails.external == false">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.mfr.mfrparts.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
                <%--  <button ng-if="mfrpartsDetailsVm.tabs.files.active && hasPermission('permission.manufacturer.edit')"
                          class="btn btn-sm btn-info"
                          ng-click="mfrpartsDetailsVm.onAddMfrPartFiles()"
                          translate>DETAILS_ADD_FILES
                  </button>--%>
                <%--   <button class="btn btn-default btn-sm"
                           ng-click="mfrpartsDetailsVm.refreshDetails()"
                           title="{{'CLICK_TO_REFRESH' | translate}}">
                       <i class="fa fa-refresh" style="font-size: 16px;margin-top:-6px;"></i>
                   </button>--%>

                <%--    <button ng-if="mfrpartsDetailsVm.tabs.files.active && hasPermission('permission.project.edit')"
                            title="{{'NEW_FOLDER' | translate}}"
                            class="btn btn-sm btn-success" ng-click="addMfrPartFolder()">
                        <i class="fa fa-folder"></i>
                    </button>--%>

                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm" ng-click="showPrintOptions(mfrpartsDetailsVm.mfrPartId,'MANUFACTURERPART')">
                    <i class="fa fa-print" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="mfrpartsDetailsVm.tabs.files.active && hasPermission('item','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download"></i>
                </button>

                <button class="btn btn-sm btn-success"
                        title="{{hasPermission('manufacturerpart','demote') ? 'Demote' : noPermission}}"
                        ng-hide="mfrPart.lifeCyclePhase.phase == mfrpartsDetailsVm.firstLifecyclePhase.phase"
                        ng-class="{'cursor-override': !hasPermission('manufacturerpart','demote')}"
                        ng-click="demoteMfrPart()">
                    <i class="fa fa-toggle-left"
                       ng-class="{'disabled': !hasPermission('manufacturerpart','demote')}"
                       style=""></i>
                </button>

                <button class="btn btn-sm btn-success"
                        title="{{hasPermission('manufacturerpart','promote') ? 'Promote' : noPermission}}"
                        ng-if="mfrPart.lifeCyclePhase.phase != mfrpartsDetailsVm.lastLifecyclePhase.phase"
                        ng-class="{'cursor-override': !hasPermission('manufacturerpart','promote')}"
                        ng-click="promoteMfrPart()">
                    <i class="fa fa-toggle-right"
                       ng-class="{'disabled': !hasPermission('manufacturerpart','promote')}"
                       style=""></i>
                </button>


                <button ng-if="mfrpartsDetailsVm.tabs.whereUsed.active" class="btn btn-sm btn-success"
                        title="Item manufacturer part status change"
                        ng-click="itemMfrStatus()">
                    <i class="fa fa-film" style=""></i>
                </button>

                <button class="btn btn-sm btn-success"
                        title="{{mfrpartsDetailsVm.addWorkflowTitle}}"
                        ng-show="mfrpartsDetailsVm.tabs.workflow.active && mfrPart.workflow == null && hasPermission('manufacturerpart','edit')"
                        ng-click="mfrpartsDetailsVm.addPartWorkflow()">
                    <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="mfrpartsDetailsVm.tabs.workflow.active"
                        ng-show="mfrPart.workflow != null && mfrPart.startWorkflow != true && hasPermission('manufacturerpart','edit')"
                        title="{{mfrpartsDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="mfrpartsDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>

                <%--    <button class="btn btn-sm btn-default" ng-if="hasPermission('manufacturerpart','edit')"
                            ng-click="mfrpartsDetailsVm.shareMfrPart()"
                            style=""
                            title="{{mfrpartsDetailsVm.shareTooltip}}">
                        <i class="las la-share" style=""></i></button>--%>

                <button class="btn btn-default"
                        ng-if="mfrpartsDetailsVm.tabs.files.active"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy"></i>
                </button>
                <div class="btn-group" ng-if="mfrpartsDetailsVm.tabs.files.active"
                     ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy"></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href=""
                                                                               translate>CLEAR_AND_ADD_FILES</a></li>
                        <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span
                                translate>ADD_TO_EXISTING_FILES</span> ({{clipBoardObjectFiles.length}})</a></li>
                    </ul>
                </div>

                <button class="btn btn-default" ng-if="mfrpartsDetailsVm.tabs.inspectionReports.active"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyReportFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy"></i>
                </button>
                <div class="btn-group" ng-if="mfrpartsDetailsVm.tabs.inspectionReports.active"
                     ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy"></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopyReportFilesToClipBoard()"><a href=""
                                                                               translate>CLEAR_AND_ADD_FILES</a></li>
                        <li ng-click="copyReportFilesToClipBoard()"><a href=""><span
                                translate>ADD_TO_EXISTING_FILES</span> ({{clipBoardObjectFiles.length}})</a></li>
                    </ul>
                </div>
            </div>
            <div ng-if="loginPersonDetails.external == true">
                <div class="btn-group">
                    <button class="btn btn-sm btn-default" ng-click='mfrpartsDetailsVm.showExternalUserMfrParts()'
                            title="{{'SHOW_ALL' | translate}}">
                        <i class="fa fa-table" aria-hidden="true"></i>
                    </button>
                </div>

            </div>

            <div class="pull-right" ng-class="{'pull-right-300':mfrpartsDetailsVm.tabs.files.active == true}"
                 ng-if="loginPersonDetails.external == false">
                <button class="btn btn-sm btn-default" title="{{subscribeButtonTitle}}"
                        ng-click="mfrpartsDetailsVm.subscribeMfrPart()">
                    <i ng-if="mfrpartsDetailsVm.subscribe == null || (mfrpartsDetailsVm.subscribe != null && !mfrpartsDetailsVm.subscribe.subscribe)"
                       class="la la-bell"></i>
                    <i ng-if="mfrpartsDetailsVm.subscribe != null && mfrpartsDetailsVm.subscribe.subscribe"
                       class="la la-bell-slash"
                       title="{{'UN_SUBSCRIBE_TITLE' | translate }}"></i></button>
                <button class="btn btn-sm btn-default" ng-if="hasPermission('manufacturerpart','edit')"
                        ng-click="mfrpartsDetailsVm.shareMfrPart()"
                        title="{{mfrpartsDetailsVm.shareTooltip}}">
                    <i class="las la-share"></i></button>
                <button class="btn btn-sm btn-default"
                        ng-click="mfrpartsDetailsVm.refreshDetails()"
                        style="margin-right: 10px;"
                        title="{{'CLICK_TO_REFRESH' | translate}}">
                    <i class="fa fa-refresh"></i></button>
            </div>


            <free-text-search on-clear="mfrpartsDetailsVm.resetPage" ng-if="mfrpartsDetailsVm.tabs.files.active"
                              search-term="freeTextQuerys"
                              on-search="mfrpartsDetailsVm.freeTextSearch"></free-text-search>


        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="mfrpartsDetailsVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC'| translate}}"
                                 active="mfrpartsDetailsVm.tabs.basic.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.basic.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.basic.template"
                                 ng-controller="MfrPartsBasicController as mfrPartsBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                 active="mfrpartsDetailsVm.tabs.attributes.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.attributes.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.attributes.template"
                                 ng-controller="MfrPartsAttributesController as mfrPartsAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="whereUsed" heading="{{mfrpartsDetailsVm.whereUsed}}"
                                 active="mfrpartsDetailsVm.tabs.whereUsed.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.whereUsed.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.whereUsed.template"
                                 ng-controller="MfrPartsWhereUsedController as mfrPartsWhereusedVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{mfrpartsDetailsVm.files}}"
                                 active="mfrpartsDetailsVm.tabs.files.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.files.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.files.template"
                                 ng-controller="MfrPartsFilesController as mfrPartsFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="Workflow"
                                 active="mfrpartsDetailsVm.tabs.workflow.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.workflow.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.workflow.template"
                                 ng-controller="ManufacturerPartWorkflowController as mfrPartWfVm"></div>
                        </uib-tab>
                        <uib-tab id="inspectionReportTab" heading="{{mfrpartsDetailsVm.inspectionReportsTitle}}"
                                 active="mfrpartsDetailsVm.tabs.inspectionReports.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.inspectionReports.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.inspectionReports.template"
                                 ng-controller="MfrPartInspectionReportsController as mfrPartInspectionReportsVm"></div>
                        </uib-tab>
                        <uib-tab id="changesTab" heading="{{mfrpartsDetailsVm.changes}}"
                                 active="mfrpartsDetailsVm.tabs.changes.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.changes.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.changes.template"
                                 ng-controller="MfrPartsChangesController as mfrPartsChangesVm"></div>
                        </uib-tab>
                        <uib-tab id="qualityTab" heading="{{mfrpartsDetailsVm.quality}}"
                                 active="mfrpartsDetailsVm.tabs.quality.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.quality.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.quality.template"
                                 ng-controller="MfrPartQualityController as mfrPartsChangesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="mfrpartsDetailsVm.tabs" custom-tabs="mfrpartsDetailsVm.customTabs"
                                     object-value="mfrpartsDetailsVm.mfrPart" tab-id="mfrpartsDetailsVm.tabId"
                                     active="mfrpartsDetailsVm.active"></plugin-tabs>
                        <uib-tab id="timeline"
                                 heading="{{mfrpartsDetailsVm.tabs.timelineHistory.heading}}"
                                 active="mfrpartsDetailsVm.tabs.timelineHistory.active"
                                 select="mfrpartsDetailsVm.mfrDetailsTabActivated(mfrpartsDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="mfrpartsDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="MfrPartTimelineHistoryController as mfrPartTimelineHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="mfr-part" class="promote-model modal">
    <style scoped>
        table .ui-select-choices {
            position: absolute !important;
            top: auto !important;
            left: auto !important;
            width: 100% !important;
            bottom: auto;
        }
    </style>
    <div class="mfrPart-content">
        <div class="mfr-header">
            <span class="configuration-header">{{mfrPart.partNumber}}-{{mfrPart.partName}}</span>
            <a href="" ng-click="mfrpartsDetailsVm.hideMfrPartItem()" class="config-close pull-right"
               style="display: inline-block"></a>
        </div>
        <div class="mfr-content" style="padding: 0">
            <div class='responsive-table'
                 style="height: 100%;overflow:auto;width: 100%;position: relative;padding: 10px;">
                <table class='table table-striped highlight-row'>
                    <thead>
                    <tr>
                        <th style="width: 30%" translate>ItemNumber</th>
                        <th style="width: 30%" translate>ItemName</th>
                        <th style="width: 10%" translate>Revision</th>
                        <th style="width: 30%" translate>PreferredItem</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="part in mfrpartsDetailsVm.itemMfrparts">
                        <td style="width: 30%">{{part.itemNumber}}</td>
                        <td style="width: 30%">{{part.itemName}}</td>
                        <td style="width: 10%">{{part.revision}}</td>
                        <td style="width: 30%">
                            <ui-select ng-model="part.itemManufacturerPart" ng-if="part.itemManufacturerParts.length >0"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select">{{$select.selected.manufacturerPart.partNumber}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="itemPart in part.itemManufacturerParts | filter: $select.search">
                                    <div ng-bind="itemPart.manufacturerPart.partNumber"></div>
                                </ui-select-choices>
                            </ui-select>
                            <span ng-if="part.itemManufacturerParts.length == 0">
                                No Alternate parts
                            </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="mfr-footer">
            <button style="float: right" class="btn-sm btn-success" ng-click="mfrpartsDetailsVm.saveItemMfrPart()">Save
            </button>
        </div>
    </div>
</div>

