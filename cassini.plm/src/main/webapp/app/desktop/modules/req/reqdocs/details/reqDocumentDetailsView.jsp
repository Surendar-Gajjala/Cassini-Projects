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

        .reqDoc-model.modal {
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

        .reqDoc-model .reqDocModel-content {
            margin: auto;
            display: block;
            height: 235px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #reqDoc-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #reqDoc-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        .reqDoc-header {
            font-weight: bold;
            font-size: 22px;
        }

        .rev-model.modal {
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

        .rev-model .revModel-content {
            margin: auto;
            display: block;
            height: 235px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #rev-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #rev-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        .rev-header {
            font-weight: bold;
            font-size: 22px;
        }


    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.req.document.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <button class="btn btn-sm btn-default" title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}"
                ng-click="reqDocDetailsVm.showDocumentRevisionHistory()">
            <i class="fa fa-history" aria-hidden="true" style=""></i>
        </button>

        <button class="btn btn-sm btn-success" title="{{itemVm.reviseItemTitle}}"
                ng-if="reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phaseType == 'RELEASED' && reqDocDetailsVm.reqDocumentRevision.latest"
                ng-click="reqDocDetailsVm.showReviseDialog()">
            <i class="fa fa-random"></i>
        </button>

        <button class="btn btn-sm btn-default" ng-click="reqDocDetailsVm.submitReqDoc()"
                ng-if="reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phase == reqDocDetailsVm.firstLifecyclePhase.phase
                && requirements >0 && reqDocumentRevision.owner.id ==loginPersonDetails.person.id"
                title="{{ 'SUBMIT' | translate}}">
            <i class="fa fa-check-square-o" aria-hidden="true"></i>
        </button>
        <button class="btn btn-sm btn-default" ng-click="reqDocDetailsVm.releasedReqDoc()"
                ng-if="reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phase == 'Review' 
                && reqDocumentRevision.owner.id ==loginPersonDetails.person.id"
                ng-hide="reqDocumentRevision.workflow != null && reqDocumentRevision.finishWorkflow != true"
                title="{{ 'RELEASE' | translate}}">
            <i class="fa fa-external-link" aria-hidden="true"></i>
        </button>

        <button ng-if="reqDocDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy" style="font-size: 16px;"></i>
        </button>

        <button class="btn btn-sm btn-default" ng-if="requirements >0"
                ng-click="reqDocDetailsVm.newReqDocTemplate()"
                title="{{ 'SAVE_REQ_DOC_TEMPLATE_TOOLTIP' | translate }} ">
            <i class="fa fa-wpforms" aria-hidden="true"></i>
        </button>

        <button class="btn btn-sm btn-default" ng-click="reqDocDetailsVm.approvedAllRequirements()"
                ng-if="reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phase == 'Review' && reqDocumentRevision.requirementApproveButton == true
                && reqDocDetailsVm.reqDocumentRevision.reviewer != null && reqDocDetailsVm.reqDocumentRevision.reviewer.approver == true"
                ng-hide="reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phase == 'Released'"
                title="{{ 'APPROVE_ALL_REQ_TOOLTIP' | translate}}">
            <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
        </button>

        <button ng-if="reqDocDetailsVm.tabs.reviewers.active && reqDocDetailsVm.reqDocumentRevision.reviewer != null && reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phaseType == 'REVIEW' &&
                reqDocDetailsVm.reqDocumentRevision.reviewer.notes == null && reqDocDetailsVm.reqDocumentRevision.reviewer.approver == true"
                class="btn btn-sm btn-default"
                ng-click="reqDocDetailsVm.showReqDocDialog('Approve')"
                translate>APPROVE
        </button>

        <button ng-if="reqDocDetailsVm.tabs.reviewers.active && reqDocDetailsVm.reqDocumentRevision.reviewer != null && reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phaseType == 'REVIEW' &&
                reqDocDetailsVm.reqDocumentRevision.reviewer.notes == null && reqDocDetailsVm.reqDocumentRevision.reviewer.approver == true"
                class="btn btn-sm btn-default"
                ng-click="reqDocDetailsVm.showReqDocDialog('Reject')"
                translate>REJECT
        </button>
        <button ng-if="reqDocDetailsVm.tabs.reviewers.active && reqDocDetailsVm.reqDocumentRevision.reviewer != null && reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phaseType == 'REVIEW' &&
                reqDocDetailsVm.reqDocumentRevision.reviewer.notes == null && reqDocDetailsVm.reqDocumentRevision.reviewer.approver == false"
                class="btn btn-sm btn-default"
                ng-click="reqDocDetailsVm.showReqDocDialog('Review')"
                translate>Add Notes
        </button>

        <button ng-if="reqDocDetailsVm.tabs.workflow.active"
                ng-show="reqDocumentRevision.workflow != null && reqDocumentRevision.startWorkflow != true"
                title="{{reqDocDetailsVm.changeWorkflowTitle}}"
                class="btn btn-sm btn-success" ng-click="reqDocDetailsVm.changeWorkflow()">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-sm btn-success"
                title="{{reqDocDetailsVm.addWorkflowTitle}}"
                ng-show="reqDocDetailsVm.tabs.workflow.active && reqDocumentRevision.workflow == null && reqDocDetailsVm.reqDocumentRevision.lifeCyclePhase.phaseType == 'PRELIMINARY'"
                ng-click="reqDocDetailsVm.addWorkflow()">
            <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
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

        <free-text-search ng-if="reqDocDetailsVm.tabs.requirements.active" on-clear="reqDocDetailsVm.onClear"
                          on-search="reqDocDetailsVm.freeTextSearch"></free-text-search>
        <free-text-search ng-if="reqDocDetailsVm.tabs.files.active" on-clear="reqDocDetailsVm.onClear"
                          on-search="reqDocDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="reqDocDetailsVm.active">
                        <uib-tab heading="{{reqDocDetailsVm.tabs.basic.heading}}"
                                 active="reqDocDetailsVm.tabs.basic.active"
                                 select="reqDocDetailsVm.tabActivated(reqDocDetailsVm.tabs.basic.id)">
                            <div ng-include="reqDocDetailsVm.tabs.basic.template"
                                 ng-controller="ReqDocumentBasicInfoController as reqDocBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="reviewers" heading="Reviewers"
                                 ng-show="reqDocDetailsVm.hasDisplayTab('reviewers')"
                                 active="reqDocDetailsVm.tabs.reviewers.active"
                                 select="reqDocDetailsVm.tabActivated(reqDocDetailsVm.tabs.reviewers.id)">
                            <div ng-include="reqDocDetailsVm.tabs.reviewers.template"
                                 ng-controller="ReqDocumentReviewersController as reqDocumentReviewersVm"></div>
                        </uib-tab>
                        <uib-tab id="reqs" heading="{{reqDocDetailsVm.tabs.requirements.heading}}"
                                 ng-show="reqDocDetailsVm.hasDisplayTab('requirements')"
                                 active="reqDocDetailsVm.tabs.requirements.active" style="height: 100%"
                                 select="reqDocDetailsVm.tabActivated(reqDocDetailsVm.tabs.requirements.id)">
                            <div ng-include="reqDocDetailsVm.tabs.requirements.template" style="height: 100%"
                                 ng-controller="RequirementsController as requirementsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{reqDocDetailsVm.tabs.files.heading}}"
                                 ng-show="reqDocDetailsVm.hasDisplayTab('files')"
                                 active="reqDocDetailsVm.tabs.files.active"
                                 select="reqDocDetailsVm.tabActivated(reqDocDetailsVm.tabs.files.id)">
                            <div ng-include="reqDocDetailsVm.tabs.files.template"
                                 ng-controller="ReqDocumentFilesController as reqDocFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="{{reqDocDetailsVm.tabs.workflow.heading}}"
                                 ng-show="reqDocDetailsVm.hasDisplayTab('workflow')"
                                 active="reqDocDetailsVm.tabs.workflow.active"
                                 select="reqDocDetailsVm.tabActivated(reqDocDetailsVm.tabs.workflow.id)">
                            <div ng-include="reqDocDetailsVm.tabs.workflow.template"
                                 ng-controller="ReqDocumentWorkflowController as reqDocWorkflowVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="reqDocDetailsVm.tabs" custom-tabs="reqDocDetailsVm.customTabs"
                                     object-value="reqDocDetailsVm.reqDocumentRevision" tab-id="reqDocDetailsVm.tabId"
                                     active="reqDocDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{reqDocDetailsVm.tabs.timelineHistory.heading}}"
                                 active="reqDocDetailsVm.tabs.timelineHistory.active"
                                 select="reqDocDetailsVm.tabActivated(reqDocDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="reqDocDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="ReqDocumentTimeLineController as reqDocTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
    <div id="reqDoc-modal" class="reqDoc-model modal">
        <div class="reqDocModel-content">
            <div id="reqDoc-header">
                <span class="reqDoc-header" ng-show="reqDocDetailsVm.reqDocType == 'Approve'">{{requirementDocApproverTitle}}</span>
                <span class="reqDoc-header"
                      ng-show="reqDocDetailsVm.reqDocType == 'Reject'">{{requirementDocRejectTitle}}</span>
                <span class="reqDoc-header"
                      ng-show="reqDocDetailsVm.reqDocType == 'Review'">{{requirementDocReviewTitle}}</span>

                <div id="reqDoc-content">
                    <p ng-if="reqDocDetailsVm.reqDocumentRevision.reviewer.notes == null && error != ''"
                       style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{error}}
                    </p>

                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                            <span translate>NOTES</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                        <textarea type="req" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_NOTES' | translate}}"
                                  ng-model="reqDocDetailsVm.reqDocumentRevision.reviewer.notes"></textarea>
                        </div>
                    </div>
                </div>
                <div style="text-align: right">
                    <button class="btn btn-xs btn-default"
                            ng-click="reqDocDetailsVm.hideReqDocDialog()" translate>
                        CANCEL
                    </button>
                    <button ng-show="reqDocDetailsVm.reqDocType == 'Approve'" class="btn btn-xs btn-success"
                            ng-click="reqDocDetailsVm.approveReqDoc()" translate>
                        SUBMIT
                    </button>
                    <button ng-show="reqDocDetailsVm.reqDocType == 'Reject'" class="btn btn-xs btn-success"
                            ng-click="reqDocDetailsVm.rejectReqDoc()" translate>
                        SUBMIT
                    </button>
                    <button ng-show="reqDocDetailsVm.reqDocType == 'Review'" class="btn btn-xs btn-success"
                            ng-click="reqDocDetailsVm.reviewReqDoc()" translate>
                        Review
                    </button>
                </div>
            </div>
        </div>
    </div>


    <div id="rev-modal" class="rev-model modal">
        <div class="revModel-content">
            <div id="rev-header">
                <span class="rev-header">{{requirementDocReviseTitle}}</span>

                <div id="rev-content">
                    <p ng-if="reqDocDetailsVm.reqDocumentRevision.comment == null && error != ''"
                       style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{error}}
                    </p>

                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                            <span translate>NOTES</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                        <textarea type="req" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_NOTES' | translate}}"
                                  ng-model="reqDocDetailsVm.reqDocumentRevision.comment"></textarea>
                        </div>
                    </div>
                </div>
                <div style="text-align: right">
                    <button class="btn btn-xs btn-default"
                            ng-click="reqDocDetailsVm.hideReviseDialog()" translate>
                        CANCEL
                    </button>
                    <button class="btn btn-xs btn-success"
                            ng-click="reqDocDetailsVm.reviseReqDocument()" translate>
                        SUBMIT
                    </button>
                </div>
            </div>
        </div>
    </div>

</div>