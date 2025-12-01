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

        .npr-model.modal {
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

        .npr-model .nprModel-content {
            margin: auto;
            display: block;
            height: 235px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #npr-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #npr-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        .npr-header {
            font-weight: bold;
            font-size: 22px;
        }

        #npr-footer {
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
    </style>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{npr.number}}</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.nprs.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <button ng-if="nprDetailsVm.tabs.files.active && hasFiles == true"
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

        <button ng-if="nprDetailsVm.tabs.workflow.active"
                ng-show="!npr.startWorkflow && npr.workflow != null"
                title="{{nprDetailsVm.changeWorkflowTitle}}"
                class="btn btn-sm btn-success"
                ng-click="nprDetailsVm.changeNprWorkflow()">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button class="btn btn-sm btn-success"
                title="{{nprDetailsVm.addWorkflowTitle}}"
                ng-show="nprDetailsVm.tabs.workflow.active && npr.workflow == null && npr.status == 'OPEN'"
                ng-click="nprDetailsVm.addNprWorkflow()">
            <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
        </button>

        <button class="btn btn-sm btn-default" ng-click="nprDetailsVm.submitNpr()"
                ng-disabled="nprDetailsVm.tabCounts.requestedItems == 0"
                title="{{nprDetailsVm.tabCounts.requestedItems == 0 ? 'Add Items':'Submit'}}"
                ng-if="npr.status == 'OPEN' && npr.workflow == null" translate>SUBMIT
        </button>
        <button class="btn btn-sm btn-default" ng-click="nprDetailsVm.approveNpr()"
                ng-disabled="nprDetailsVm.tabCounts.unAssignedItems > 0"
                title="{{nprDetailsVm.tabCounts.unAssignedItems > 0 ? 'Assign Item Numbers':'Submit'}}"
                ng-if="npr.status == 'PENDING' && npr.workflow == null" translate>APPROVE
        </button>
        <button class="btn btn-sm btn-default" ng-click="nprDetailsVm.showNprDialog()"
                ng-if="npr.status == 'PENDING' && npr.workflow == null" translate>REJECT
        </button>

        <comments-btn ng-if="!nprDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!nprDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="nprDetailsVm.tabs.files.active" on-clear="nprDetailsVm.onClear"
                          on-search="nprDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="nprDetailsVm.active">
                        <uib-tab heading="{{nprDetailsVm.tabs.basic.heading}}"
                                 active="nprDetailsVm.tabs.basic.active"
                                 select="nprDetailsVm.tabActivated(nprDetailsVm.tabs.basic.id)">
                            <div ng-include="nprDetailsVm.tabs.basic.template"
                                 ng-controller="NprBasicInfoController as nprBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="requestedItems" heading="{{nprDetailsVm.tabs.requestedItems.heading}}"
                                 active="nprDetailsVm.tabs.requestedItems.active"
                                 select="nprDetailsVm.tabActivated(nprDetailsVm.tabs.requestedItems.id)">
                            <div ng-include="nprDetailsVm.tabs.requestedItems.template"
                                 ng-controller="NprRequestedItemsController as nprRequestedItemsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{nprDetailsVm.tabs.files.heading}}"
                                 active="nprDetailsVm.tabs.files.active"
                                 select="nprDetailsVm.tabActivated(nprDetailsVm.tabs.files.id)">
                            <div ng-include="nprDetailsVm.tabs.files.template"
                                 ng-controller="NprFilesController as nprFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="Workflow"
                                 active="nprDetailsVm.tabs.workflow.active"
                                 select="nprDetailsVm.tabActivated(nprDetailsVm.tabs.workflow.id)">
                            <div ng-include="nprDetailsVm.tabs.workflow.template"
                                 ng-controller="NprWorkflowController as nprWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="" heading="{{nprDetailsVm.tabs.timelineHistory.heading}}"
                                 active="nprDetailsVm.tabs.timelineHistory.active"
                                 select="nprDetailsVm.tabActivated(nprDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="nprDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="NprTimeLineController as nprTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
    <div id="npr-modal" class="npr-model modal">
        <div class="nprModel-content">
            <div id="npr-header">
                <span class="npr-header" translate>NPR_REJECTED_REASON</span>

                <div id="npr-content">
                    <p ng-if="npr.rejectReason == null && error != ''"
                       style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{error}}
                    </p>

                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                            <span translate>REJECT_REASON</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                        <textarea type="npr" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_REJECTED_REASON' | translate}}"
                                  ng-model="npr.rejectReason"></textarea>
                        </div>
                    </div>
                </div>
                <div style="text-align: right">
                    <button class="btn btn-xs btn-default"
                            ng-click="hideNprDialog()" translate>
                        CANCEL
                    </button>
                    <button class="btn btn-xs btn-success"
                            ng-click="nprDetailsVm.rejectNpr()" translate>
                        SUBMIT
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>