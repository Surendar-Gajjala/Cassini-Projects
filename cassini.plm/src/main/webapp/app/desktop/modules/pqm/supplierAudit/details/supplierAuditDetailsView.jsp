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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{supplierAudit.number}}
            <span title="{{supplierAudit.name.length > 30 ? supplierAudit.name : ' '}}"> {{supplierAudit.name | limitTo:30}} {{supplierAudit.name.length > 30 ? '...' : ' '}}</span>
         </span>
        <free-text-search ng-if="supplierAuditDetailsVm.tabs.files.active"
                          on-search="supplierAuditDetailsVm.freeTextSearch"></free-text-search>
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-if="loginPersonDetails.external == false"
                    ng-click="showAll('app.pqm.supplierAudit.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-default" ng-if="loginPersonDetails.external == true"
                    ng-click='supplierAuditDetailsVm.showExternalUserSuppliers()' title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
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
            <button class="btn btn-sm btn-success"
                    ng-if="supplierAudit.status.phaseType != 'RELEASED' && !loginPersonDetails.external"
                    title="{{supplierAuditDetailsVm.addWorkflowTitle}}"
                    ng-show="supplierAuditDetailsVm.tabs.workflow.active && supplierAudit.workflow == null"
                    ng-click="supplierAuditDetailsVm.addPartWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="supplierAuditDetailsVm.tabs.workflow.active && supplierAudit.status.phaseType != 'RELEASED' && !loginPersonDetails.external"
                    ng-show="supplierAudit.workflow != null && supplierAudit.startWorkflow != true"
                    title="{{supplierAuditDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="supplierAuditDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>

        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="supplierAuditDetailsVm.active">
                        <uib-tab id="{{supplierAuditDetailsVm.tabs.basic.id}}"
                                 heading="{{supplierAuditDetailsVm.tabs.basic.heading}}"
                                 active="supplierAuditDetailsVm.tabs.basic.active"
                                 select="supplierAuditDetailsVm.tabActivated(supplierAuditDetailsVm.tabs.basic.id)">
                            <div ng-include="supplierAuditDetailsVm.tabs.basic.template"
                                 ng-controller="SupplierAuditBasicInfoController as supplierAuditBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="plan-tab" heading="{{supplierAuditDetailsVm.tabs.plan.heading}}"
                                 active="supplierAuditDetailsVm.tabs.plan.active"
                                 select="supplierAuditDetailsVm.tabActivated(supplierAuditDetailsVm.tabs.plan.id)">
                            <div ng-include="supplierAuditDetailsVm.tabs.plan.template"
                                 ng-controller="SupplierAuditPlanController as supplierAuditPlanVm"></div>
                        </uib-tab>
                        <uib-tab id="files"
                                 heading="{{supplierAuditDetailsVm.tabs.files.heading}}"
                                 active="supplierAuditDetailsVm.tabs.files.active"
                                 select="supplierAuditDetailsVm.tabActivated(supplierAuditDetailsVm.tabs.files.id)">
                            <div ng-include="supplierAuditDetailsVm.tabs.files.template"
                                 ng-controller="SupplierAuditFilesController as supplierAuditFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="{{supplierAuditDetailsVm.tabs.workflow.id}}"
                                 heading="{{supplierAuditDetailsVm.tabs.workflow.heading}}"
                                 active="supplierAuditDetailsVm.tabs.workflow.active"
                                 select="supplierAuditDetailsVm.tabActivated(supplierAuditDetailsVm.tabs.workflow.id)">
                            <div ng-include="supplierAuditDetailsVm.tabs.workflow.template"
                                 ng-controller="SupplierAuditWorkflowController as supplierAuditWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="{{supplierAuditDetailsVm.tabs.timeline.id}}"
                                 heading="{{supplierAuditDetailsVm.tabs.timeline.heading}}"
                                 active="supplierAuditDetailsVm.tabs.timeline.active"
                                 select="supplierAuditDetailsVm.tabActivated(supplierAuditDetailsVm.tabs.timeline.id)">
                            <div ng-include="supplierAuditDetailsVm.tabs.timeline.template"
                                 ng-controller="SupplierAuditTimelineController as supplierAuditTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

