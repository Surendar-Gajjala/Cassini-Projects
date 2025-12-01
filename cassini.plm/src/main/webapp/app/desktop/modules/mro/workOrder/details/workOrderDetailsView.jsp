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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{workOrder.number}}
            <span title="{{workOrder.name.length > 30 ? workOrder.name : ' '}}"> {{workOrder.name | limitTo:30}} {{workOrder.name.length > 30 ? '...' : ' '}}</span>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mro.workOrder.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <button ng-if="workOrderDetailsVm.tabs.workflow.active"
                ng-show="!workOrder.startWorkflow && workOrder.workflow != null"
                title="{{workOrderDetailsVm.changeWorkflowTitle}}"
                class="btn btn-sm btn-success"
                ng-click="workOrderDetailsVm.changeWorkflow()">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button class="btn btn-sm btn-success"
                title="{{workOrderDetailsVm.addWorkflowTitle}}"
                ng-show="workOrderDetailsVm.tabs.workflow.active && workOrder.workflow == null && workOrder.status == 'OPEN'"
                ng-click="workOrderDetailsVm.addWorkflow()">
            <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
        </button>
        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm"
                ng-click="showPrintOptions(workOrderDetailsVm.workOrderId,'MROWORKORDER')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="workOrderDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy"></i>
        </button>
        <div class="btn-group" title="{{hasPermission('mroworkorder','demote') ? '' : noPermission}}"
             ng-show="workOrder.status == 'OPEN' && (workOrder.workflow == null || workOrder.workflow == '')"
             ng-class="{'cursor-override': !hasPermission('mroworkorder','promote')}">
            <button class="btn btn-default btn-sm"
                    ng-class="{'permission-text-disabled': !hasPermission('mroworkorder','promote')}"
                    ng-click="workOrderDetailsVm.promoteWorkOrder()" title="Start work order" translate>START
            </button>

            <button ng-if="workOrderDetailsVm.tabs.instructions.active" title="Save" class="btn btn-sm btn-default"
            ng-click="saveWorkOrderInstructions()">
           <i class="fa fa-save"></i>
            </button>
        </div>

        <div class="btn-group"
             ng-show="workOrder.status == 'INPROGRESS' && (workOrder.workflow == null || workOrder.workflow == '')"
             title="{{hasPermission('mroworkorder','promote') ? '' : noPermission}}"
             ng-class="{'cursor-override': !hasPermission('mroworkorder','promote')}">

            <button class="btn btn-default btn-sm"
                    ng-class="{'permission-text-disabled': !hasPermission('mroworkorder','promote')}"
                    ng-click="workOrderDetailsVm.promoteWorkOrder()" title="Finish work order" translate>FINISH
            </button>
        </div>


        <button class="btn btn-default btn-sm" title="Hold work order"
                ng-show="workOrder.status == 'INPROGRESS' && (workOrder.workflow == null || workOrder.workflow == '')"
                ng-click="workOrderDetailsVm.holdWorkOrder()" translate>HOLD
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="workOrder.status == 'ONHOLD' && (workOrder.workflow == null || workOrder.workflow == '')"
                title="Remove hold"
                ng-click="workOrderDetailsVm.removeHold()" translate>REMOVE_HOLD
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
        <comments-btn ng-if="!workOrderDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!workOrderDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="workOrderDetailsVm.tabs.files.active" on-clear="workOrderDetailsVm.onClear"
                          on-search="workOrderDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="workOrderDetailsVm.active">
                        <uib-tab heading="{{workOrderDetailsVm.tabs.basic.heading}}"
                                 active="workOrderDetailsVm.tabs.basic.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.basic.id)">
                            <div ng-include="workOrderDetailsVm.tabs.basic.template"
                                 ng-controller="WorkOrderBasicInfoController as workOrderBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{workOrderDetailsVm.tabs.attributes.heading}}"
                                 active="workOrderDetailsVm.tabs.attributes.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.attributes.id)">
                            <div ng-include="workOrderDetailsVm.tabs.attributes.template"
                                 ng-controller="WorkOrderAttributesController as workOrderAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="operations" heading="{{workOrderDetailsVm.tabs.operations.heading}}"
                                 ng-show="workOrder.type.type == 'MAINTENANCE'"
                                 active="workOrderDetailsVm.tabs.operations.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.operations.id)">
                            <div ng-include="workOrderDetailsVm.tabs.operations.template"
                                 ng-controller="WorkOrderOperationsController as workOrderOperationVm"></div>
                        </uib-tab>
                        <uib-tab id="resources" heading="{{workOrderDetailsVm.tabs.resources.heading}}"
                                 active="workOrderDetailsVm.tabs.resources.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.resources.id)">
                            <div ng-include="workOrderDetailsVm.tabs.resources.template"
                                 ng-controller="WorkOrderResourcesController as workOrderResourcesVm"></div>
                        </uib-tab>
                        <uib-tab id="spareParts" heading="{{workOrderDetailsVm.tabs.spareParts.heading}}"
                                 active="workOrderDetailsVm.tabs.spareParts.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.spareParts.id)">
                            <div ng-include="workOrderDetailsVm.tabs.spareParts.template"
                                 ng-controller="WorkOrderSparePartsController as workOrderSparePartVm"></div>
                       </uib-tab>   
                        <uib-tab id="instructions" heading="{{workOrderDetailsVm.tabs.instructions.heading}}"
                                active="workOrderDetailsVm.tabs.instructions.active"
                                select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.instructions.id)">
                           <div ng-include="workOrderDetailsVm.tabs.instructions.template"
                            ng-controller="WorkOrderInstructionsController as workOrderInstructionsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{workOrderDetailsVm.tabs.files.heading}}"
                                 active="workOrderDetailsVm.tabs.files.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.files.id)">
                            <div ng-include="workOrderDetailsVm.tabs.files.template"
                                 ng-controller="WorkOrderFilesController as workOrderFilesVm"></div>
                        </uib-tab>

                        <uib-tab id="workflow" heading="{{workOrderDetailsVm.tabs.workflow.heading}}"
                                 active="workOrderDetailsVm.tabs.workflow.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.workflow.id)">
                            <div ng-include="workOrderDetailsVm.tabs.workflow.template"
                                 ng-controller="WorkOrderWorkflowController as workOrderWorkflowVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="workOrderDetailsVm.tabs" custom-tabs="workOrderDetailsVm.customTabs"
                                     object-value="workOrderDetailsVm.workOrder" tab-id="workOrderDetailsVm.tabId" active="workOrderDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{workOrderDetailsVm.tabs.timelineHistory.heading}}"
                                 active="workOrderDetailsVm.tabs.timelineHistory.active"
                                 select="workOrderDetailsVm.tabActivated(workOrderDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="workOrderDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="WorkOrderTimeLineController as workOrderTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
