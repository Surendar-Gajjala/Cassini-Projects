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
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.productionOrder.all')"
                        title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
            </div>
            <button ng-if="productionOrderDetailsVm.tabs.workflow.active && hasPermission('productionorder','edit') && !productionOrder.approved && !productionOrder.rejected"
                    ng-show="!productionOrder.startWorkflow"
                    class="btn btn-sm btn-success">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
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
                    <li ng-click="clearAndCopyChangeFilesToClipBoard()"><a href="" translate>CLEAR_AND_ADD_FILES</a>
                    </li>
                    <li ng-click="copyChangeFilesToClipBoard()"><a href=""><span
                            translate>ADD_TO_EXISTING_FILES</span>
                        ({{clipBoardObjectFiles.length}})</a></li>
                </ul>
            </div>
            <button class="btn btn-sm btn-success"
                    ng-hide="productionOrderDetailsVm.productionOrder.lifeCyclePhase.phase == productionOrderDetailsVm.firstLifecyclePhase.phase"
                    title="{{productionOrderDetailsVm.demotePOTitle}}"
                    ng-click="productionOrderDetailsVm.demoteProductionOrder()">
                <i class="fa fa-toggle-left" style=""></i>
            </button>
            <button class="btn btn-sm btn-success" title="{{productionOrderDetailsVm.promotePOTitle}}"
                    ng-if="productionOrderDetailsVm.productionOrder.lifeCyclePhase.phase != productionOrderDetailsVm.lastLifecyclePhase.phase"
                    ng-class="{'cursor-override': !promoteItemPermission}"
                    ng-click="productionOrderDetailsVm.promoteProductionOrder()">
                <i class="fa fa-toggle-right" style=""></i>
            </button>
            <div class="pull-right">

            </div>
        </div>
        <free-text-search ng-if="productionOrderDetailsVm.tabs.files.active" on-clear="productionOrderDetailsVm.onClear"
                          on-search="productionOrderDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="productionOrderDetailsVm.active">
                        <uib-tab heading="{{productionOrderDetailsVm.tabs.basic.heading}}"
                                 active="productionOrderDetailsVm.tabs.basic.active"
                                 select="productionOrderDetailsVm.tabActivated(productionOrderDetailsVm.tabs.basic.id)">
                            <div ng-include="productionOrderDetailsVm.tabs.basic.template"
                                 ng-controller="ProductionOrderBasicInfoController as productionOrderBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="po-items" heading="{{productionOrderDetailsVm.tabs.items.heading}}"
                                 active="productionOrderDetailsVm.tabs.items.active"
                                 select="productionOrderDetailsVm.tabActivated(productionOrderDetailsVm.tabs.items.id)">
                            <div ng-include="productionOrderDetailsVm.tabs.items.template"
                                 ng-controller="ProductionOrderItemsController as productionOrderItemsVm">
                            </div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{productionOrderDetailsVm.tabs.files.heading}}"
                                 active="productionOrderDetailsVm.tabs.files.active"
                                 select="productionOrderDetailsVm.tabActivated(productionOrderDetailsVm.tabs.files.id)">
                            <div ng-include="productionOrderDetailsVm.tabs.files.template"
                                 ng-controller="ProductionOrderFilesController as productionOrderFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="{{productionOrderDetailsVm.tabs.workflow.heading}}"
                                 active="productionOrderDetailsVm.tabs.workflow.active"
                                 select="productionOrderDetailsVm.tabActivated(productionOrderDetailsVm.tabs.workflow.id)">
                            <div ng-include="productionOrderDetailsVm.tabs.workflow.template"
                                 ng-controller="ProductionOrderWorkflowController as productionOrderWfVm"></div>
                        </uib-tab>
                        <uib-tab id="" heading="{{productionOrderDetailsVm.tabs.timelineHistory.heading}}"
                                 active="productionOrderDetailsVm.tabs.timelineHistory.active"
                                 select="productionOrderDetailsVm.tabActivated(productionOrderDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="productionOrderDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="ProductionOrderTimeLineController as productionOrderTimeLineVm">
                            </div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>