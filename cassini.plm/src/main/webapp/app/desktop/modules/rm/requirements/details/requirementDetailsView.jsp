<style>
    .activity-progress.progress {
        background-color: #B0C7CF;
        height: 25px !important;
        margin: 0 !important;
    }

    .item-number {
        display: inline-block;
    }

    .item-rev {
        font-size: 16px;
        font-weight: normal;
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

    #freeTextSearchDirective {
        top: 7px !important;
    }

</style>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="requirementDetailsVm.showAllRequirements()"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

            <button class="btn btn-sm btn-success" ng-click="requirementDetailsVm.showVersionHistory()"
                    title="{{requirementDetailsVm.reqVersionHistory}}">
                <i class="fa fa-history" aria-hidden="true" style=""></i>
            </button>

            <button class="btn btn-sm btn-default"
                    ng-if="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' && selectedRequirement.status != 'FINISHED'  && (hasPermission('admin','all') || hasPermission('pgcspecification','edit') || specPermission.editPermission == true)"
                    title="{{requirementDetailsVm.clickToFinish}}"
                    ng-click="finishRequirement()">
                <i class="fa fa-check-circle"></i>
            </button>

            <%--   <button class="btn btn-sm btn-success min-width"
                       ng-hide="selectedRequirement.status == 'FINISHED'"
                       ng-show="requirementDetailsVm.tabs.deliverables.active"
                       ng-if="hasPermission('pgcspecification','edit') || specPermission.editPermission"
                       ng-click="showDeliverables()"
                       translate>ADD_DELIVERABLE
               </button>--%>

            <button class="btn btn-default btn-sm" title="{{subscribeButtonTitle}}"
                    ng-click="requirementDetailsVm.subscribeRequirement(selectedRequirement)">
                <i ng-if="requirementDetailsVm.subscribe == null || (requirementDetailsVm.subscribe != null && !requirementDetailsVm.subscribe.subscribe)"
                   class="la la-bell"></i>
                <i ng-if="requirementDetailsVm.subscribe != null && requirementDetailsVm.subscribe.subscribe"
                   class="la la-bell-slash"></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{requirementDetailsVm.addWorkflowTitle}}"
                    ng-show="requirementDetailsVm.tabs.workflow.active && selectedRequirement.workflow  == null && selectedRequirement.status != 'FINISHED'"
                    ng-click="requirementDetailsVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="requirementDetailsVm.tabs.workflow.active"
                    ng-show="selectedRequirement.workflow  != null && selectedRequirement.startWorkflow != true && selectedRequirement.status != 'FINISHED'"
                    title="{{requirementDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="requirementDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
            <%--<button ng-if="requirementDetailsVm.tabs.deliverables.active" &lt;%&ndash;&& hasPermission('project','edit')&ndash;%&gt;
                    title="{{'DETAILS_TAB_DELIVERABLES' | translate}}" &lt;%&ndash;ng-disabled="projectPercentComplete == 100"&ndash;%&gt;
                    class="btn btn-sm btn-success min-width" ng-click="requirementDetailsVm.newDeliverable()" translate>
                ADD_DELIVERABLE
            </button>--%>
            <%-- <button class="btn btn-sm btn-success"
                     title="{{addFolder}}"
                     ng-if="requirementDetailsVm.tabs.files.active"
                     ng-hide="selectedRequirement.status == 'FINISHED'"
                     ng-click="addReqFolder()">
                 <i class="fa fa-folder"></i>
             </button>--%>
            <button ng-if="requirementDetailsVm.tabs.files.active && hasPermission('item','edit') && hasFiles == true"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="requirementDetailsVm.downloadFilesAsZip()">
                <i class="fa fa-download"></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="showCopyReqFilesToClipBoard && clipBoardReqFiles.length == 0"
                    ng-click="copyReqFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 20px;"></i>
            </button>
            <div class="btn-group" ng-show="showCopyReqFilesToClipBoard && clipBoardReqFiles.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 20px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyReqFilesToClipBoard()"><a href="">Clear and Add Files</a></li>
                    <li ng-click="copyReqFilesToClipBoard()"><a href="">Add to Existing Files</a></li>
                </ul>
            </div>
        </div>
        <div class="pull-right">
            <free-text-search ng-if="requirementDetailsVm.tabs.files.active" on-clear="requirementDetailsVm.onClear"
                              on-search="requirementDetailsVm.freeTextSearch"></free-text-search>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="requirementDetailsVm.active">
                        <uib-tab heading="{{requirementDetailsVm.tabs.basic.heading}}"
                                 active="requirementDetailsVm.tabs.basic.active"
                                 select="requirementDetailsVm.reqDetailsTabActivated(requirementDetailsVm.tabs.basic.id)">
                            <div ng-include="requirementDetailsVm.tabs.basic.template"
                                 ng-controller="RequirementsBasicInfoController as reqBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                 active="requirementDetailsVm.tabs.attributes.active"
                                 select="requirementDetailsVm.reqDetailsTabActivated(requirementDetailsVm.tabs.attributes.id)">
                            <div ng-include="requirementDetailsVm.tabs.attributes.template"
                                 ng-controller="RequirementAttributesController as requirementAttributesVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 active="requirementDetailsVm.tabs.files.active"
                                 select="requirementDetailsVm.reqDetailsTabActivated(requirementDetailsVm.tabs.files.id)">
                            <div ng-include="requirementDetailsVm.tabs.files.template"
                                 ng-controller="RequirementsFilesController as reqFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="deliverables" heading="{{'DETAILS_TAB_DELIVERABLES' | translate}}"
                                 active="requirementDetailsVm.tabs.deliverables.active"
                                 select="requirementDetailsVm.reqDetailsTabActivated(requirementDetailsVm.tabs.deliverables.id)">
                            <div ng-include="requirementDetailsVm.tabs.deliverables.template"
                                 ng-controller="RequirementsDeliverablesController as reqDeliverablesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow"
                                 heading="{{requirementDetailsVm.tabs.workflow.heading}}"
                                 active="requirementDetailsVm.tabs.workflow.active"
                                 select="requirementDetailsVm.reqDetailsTabActivated(requirementDetailsVm.tabs.workflow.id)">
                            <div ng-include="requirementDetailsVm.tabs.workflow.template"
                                 ng-controller="RequirementWorkflowController as reqWfVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

