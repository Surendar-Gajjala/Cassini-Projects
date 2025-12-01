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

        .item-rev {
            font-size: 16px;
            font-weight: normal;
        }

        .item-number {
            display: inline-block;
        }

        .req-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 100px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .req-model .reqModel-content {
            margin: auto;
            display: block;
            height: 235px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #req-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #req-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        .req-header {
            font-weight: bold;
            font-size: 22px;
        }

        #req-footer {
            border-top: 1px solid lightgrey;
            padding: 5px;
            text-align: right;
            height: 40px;
            width: 100%;
            background-color: #edeeef;
            border-bottom-left-radius: 7px;
            border-bottom-right-radius: 7px;
        }

        .center {
            display: block;
            margin-left: auto;
            margin-right: auto;
            margin-top: 4%;
            width: 300px;
        }

        .version-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 100px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .version-model .versionModel-content {
            margin: auto;
            display: block;
            height: 235px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #version-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #version-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        .version-header {
            font-weight: bold;
            font-size: 22px;
        }

    </style>
    <div class="view-toolbar">
        <div class="btn-group" ng-if="loginPersonDetails.external == false">
            <button class="btn btn-sm btn-default" ng-click="reqDetailsVm.showAllRequirements()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <div class="btn-group" ng-if="loginPersonDetails.external == true">
            <button class="btn btn-sm btn-default" ng-click='reqDetailsVm.showExternalUserItems()'
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
        </div>

        <button class="btn btn-sm btn-default" ng-click="reqDetailsVm.submitReqVersion()"
                ng-if="reqDocumentRevision.lifeCyclePhase.phaseType == 'REVIEW' && reqDetailsVm.reqVersion.lifeCyclePhase.phaseType == 'PRELIMINARY'"
                title="{{ 'SUBMIT' | translate}}">
            <i class="fa fa-check-square-o" aria-hidden="true"></i>
        </button>

        <button class="btn btn-sm btn-success" title="{{itemVm.reviseItemTitle}}"
                ng-if="reqDetailsVm.reqVersion.lifeCyclePhase.phaseType == 'RELEASED'"
                ng-click="reqDetailsVm.showReqVersionDialog()">
            <i class="fa fa-random"></i>
        </button>

        <button class="btn btn-sm btn-default" title="{{'ITEM_DETAILS_VERSION_HISTORY' | translate}}"
                ng-click="reqDetailsVm.showReqVersionHistory()">
            <i class="fa fa-history" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="reqDetailsVm.reqVersion.lifeCyclePhase.phaseType == 'REVIEW' && reqDetailsVm.reqVersion.reviewer != null
        && reqDetailsVm.reqVersion.reviewer.notes == null && reqDetailsVm.reqVersion.reviewer.approver == true"
                class="btn btn-sm btn-default"
                ng-click="reqDetailsVm.showReqDialog('Approve')"
                translate>APPROVE
        </button>

        <button ng-if="reqDetailsVm.reqVersion.lifeCyclePhase.phaseType == 'REVIEW' && reqDetailsVm.reqVersion.reviewer != null
        && reqDetailsVm.reqVersion.reviewer.notes == null && reqDetailsVm.reqVersion.reviewer.approver == true"
                class="btn btn-sm btn-default"
                ng-click="reqDetailsVm.showReqDialog('Reject')"
                translate>REJECT
        </button>

        <button ng-if="reqDetailsVm.tabs.reviewers.active && reqDetailsVm.reqVersion.lifeCyclePhase.phaseType == 'REVIEW'
        && reqDetailsVm.reqVersion.reviewer != null && reqDetailsVm.reqVersion.reviewer.notes == null
        && reqDetailsVm.reqVersion.reviewer.approver == false"
                class="btn btn-sm btn-default"
                ng-click="reqDetailsVm.showReqDialog('Review')"
                translate>Add Notes
        </button>

        <button ng-if="reqDetailsVm.tabs.workflow.active"
        ng-show="reqquirementChild.workflow != null && reqquirementChild.startWorkflow != true"
        title="{{reqDetailsVm.changeWorkflowTitle}}"
        class="btn btn-sm btn-success" ng-click="reqDetailsVm.changeWorkflow()">
    <i class="fa fa-indent" aria-hidden="true" style=""></i>
</button>
<button class="btn btn-sm btn-success"
        title="{{reqDetailsVm.addWorkflowTitle}}"
        ng-show="reqDetailsVm.tabs.workflow.active && reqquirementChild.workflow == null && reqDetailsVm.reqVersion.lifeCyclePhase.phaseType == 'PRELIMINARY'"
        ng-click="reqDetailsVm.addWorkflow()">
    <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
</button>

        <button ng-if="reqDetailsVm.tabs.files.active && hasFiles == true"
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

        <free-text-search ng-if="reqDetailsVm.tabs.files.active" on-clear="reqDetailsVm.onClear"
                          on-search="reqDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="reqDetailsVm.active">
                        <uib-tab heading="{{reqDetailsVm.tabs.basic.heading}}"
                                 active="reqDetailsVm.tabs.basic.active"
                                 select="reqDetailsVm.tabActivated(reqDetailsVm.tabs.basic.id)">
                            <div ng-include="reqDetailsVm.tabs.basic.template"
                                 ng-controller="ReqBasicInfoController as reqBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="reviewers" heading="Reviewers"
                                ng-show="reqDetailsVm.hasDisplayTab('reviewers')"
                                 active="reqDetailsVm.tabs.reviewers.active"
                                 select="reqDetailsVm.tabActivated(reqDetailsVm.tabs.reviewers.id)">
                            <div ng-include="reqDetailsVm.tabs.reviewers.template"
                                 ng-controller="ReqReviewersController as reqReviewersVm"></div>
                        </uib-tab>
                        <uib-tab id="req-items" heading="{{reqDetailsVm.tabs.items.heading}}"
                                ng-show="reqDetailsVm.hasDisplayTab('items')"
                                 active="reqDetailsVm.tabs.items.active"
                                 select="reqDetailsVm.tabActivated(reqDetailsVm.tabs.items.id)">
                            <div ng-include="reqDetailsVm.tabs.items.template"
                                 ng-controller="ReqItemsController as reqItemsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{reqDetailsVm.tabs.files.heading}}"
                                ng-show="reqDetailsVm.hasDisplayTab('files')"
                                 active="reqDetailsVm.tabs.files.active"
                                 select="reqDetailsVm.tabActivated(reqDetailsVm.tabs.files.id)">
                            <div ng-include="reqDetailsVm.tabs.files.template"
                                 ng-controller="ReqFilesController as reqFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="{{reqDetailsVm.tabs.workflow.heading}}"
                                 ng-show="reqDetailsVm.hasDisplayTab('workflow')"
                                 active="reqDetailsVm.tabs.workflow.active"
                                 select="reqDetailsVm.tabActivated(reqDetailsVm.tabs.workflow.id)">
                            <div ng-include="reqDetailsVm.tabs.workflow.template"
                                 ng-controller="RequirementWorkflowController as reqWorkflowVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="reqDetailsVm.tabs" custom-tabs="reqDetailsVm.customTabs"
                                     object-value="reqDetailsVm.reqVersion" tab-id="reqDetailsVm.tabId" active="reqDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{reqDetailsVm.tabs.timelineHistory.heading}}"
                                 active="reqDetailsVm.tabs.timelineHistory.active"
                                 select="reqDetailsVm.tabActivated(reqDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="reqDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="ReqTimeLineController as reqTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
    <div id="req-modal" class="req-model modal">
        <div class="reqModel-content">
            <div id="req-header">
                <span class="req-header" ng-show="reqDetailsVm.reqType == 'Approve'">{{requirementApproverTitle}}</span>
                <span class="req-header" ng-show="reqDetailsVm.reqType == 'Reject'">{{requirementRejectTitle}}</span>
                <span class="req-header" ng-show="reqDetailsVm.reqType == 'Review'">{{requirementReviewTitle}}</span>

                <div id="req-content">
                    <p ng-if="req.rejectReason == null && error != ''"
                       style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{error}}
                    </p>

                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                            <span translate>NOTES</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                        <textarea type="req" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_NOTES' | translate}}"
                                  ng-model="reqDetailsVm.reqVersion.reviewer.notes"></textarea>
                        </div>
                    </div>
                </div>
                <div style="text-align: right">
                    <button class="btn btn-xs btn-default"
                            ng-click="reqDetailsVm.hideReqDialog()" translate>
                        CANCEL
                    </button>
                    <button ng-show="reqDetailsVm.reqType == 'Approve'" class="btn btn-xs btn-success"
                            ng-click="reqDetailsVm.approveReq()" translate>
                        SUBMIT
                    </button>
                    <button ng-show="reqDetailsVm.reqType == 'Reject'" class="btn btn-xs btn-success"
                            ng-click="reqDetailsVm.rejectReq()" translate>
                        SUBMIT
                    </button>
                    <button ng-show="reqDetailsVm.reqType == 'Review'" class="btn btn-xs btn-success"
                            ng-click="reqDetailsVm.reviewReq()" translate>
                        Review
                    </button>
                </div>
            </div>
        </div>
    </div>


    <div id="version-modal" class="version-model modal">
        <div class="versionModel-content">
            <div id="version-header">
                <span class="version-header">{{requirementReviseTitle}}</span>

                <div id="version-content">
                    <p ng-if="req.rejectReason == null && error != ''"
                       style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{error}}
                    </p>

                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                            <span translate>NOTES</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                        <textarea type="req" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_NOTES' | translate}}"
                                  ng-model="reqDetailsVm.reqVersion.comment"></textarea>
                        </div>
                    </div>
                </div>
                <div style="text-align: right">
                    <button class="btn btn-xs btn-default"
                            ng-click="reqDetailsVm.hideReqVersionDialog()" translate>
                        CANCEL
                    </button>
                    <button class="btn btn-xs btn-success"
                            ng-click="reqDetailsVm.reviseReqVersion()" translate>
                        SUBMIT
                    </button>
                </div>
            </div>
        </div>
    </div>

</div>