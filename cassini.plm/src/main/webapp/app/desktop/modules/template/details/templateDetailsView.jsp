<style>
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
        right: 260px;
    }
    
</style>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="templateDetailsVm.back()"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{templateDetailsVm.addWorkflowTitle}}"
                    ng-show="templateDetailsVm.tabs.workflow.active && projectTemplate.workflow == null"
                    ng-click="templateDetailsVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="templateDetailsVm.tabs.workflow.active"
                    ng-show="projectTemplate.workflow != null && projectTemplate.startWorkflow != true"
                    title="{{templateDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="templateDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>

            <div class="btn-group" ng-if="templateDetailsVm.tabs.plan.active">
                <button class="btn btn-sm btn-default"
                        title="{{save}}"
                        ng-click="templateDetailsVm.saveGantt()">
                    <i class="fa fa-save" style=""></i>
                </button>
                <button class="btn btn-sm btn-default"
                        title="{{expandAll}}"
                        ng-click="templateDetailsVm.expandAll()"
                        ng-if="templateDetailsVm.expand">
                    <i class="fa fa-expand" style=""></i>
                </button>
                <button class="btn btn-sm btn-default"
                        title="{{collapseAll}}"
                        ng-click="templateDetailsVm.collapseAll()"
                        ng-if="!templateDetailsVm.expand">
                    <i class="fa fa-compress" style=""></i>
                </button>

            </div>
            <%--   <button ng-if="templateDetailsVm.tabs.plan.active && showTemplateWbsButton == true && hasPermission('template','edit')"
                       class="btn btn-sm btn-success min-width" ng-click="addTemplateWbs()" translate>
                   ADD_WBS
               </button>
               <button ng-if="templateDetailsVm.tabs.plan.active && showTemplateActivityAndMilestone == true && hasPermission('template','edit')"
                       class="btn btn-sm btn-primary min-width" ng-click="addTemplateActivity()" translate>
                   ADD_ACTIVITY
               </button>
               <button ng-if="templateDetailsVm.tabs.plan.active && showTemplateActivityAndMilestone == true && hasPermission('template','edit')"
                       class="btn btn-sm btn-warning min-width" ng-click="addTemplateMilestone()" translate>
                   ADD_MILESTONE
               </button>--%>
        </div>
        <div class="pull-right" style="width: 250px;margin-right: 10px;padding-top: 5px;" ng-show="templateDetailsVm.tabs.files.active">
            <free-text-search   on-clear="templateDetailsVm.onClear" search-term="freeTextQuerys"
                on-search="templateDetailsVm.freeTextSearch"></free-text-search>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="templateDetailsVm.active">
                        <uib-tab heading="{{templateDetailsVm.tabs.basic.heading}}"
                                 active="templateDetailsVm.tabs.basic.active"
                                 select="templateDetailsVm.templateDetailsTabActivated(templateDetailsVm.tabs.basic.id)">
                            <div ng-include="templateDetailsVm.tabs.basic.template"
                                 ng-controller="TemplateBasicInfoController as templateBasicInfoVm"></div>
                        </uib-tab>
                        <uib-tab id="teamId" heading="{{templateDetailsVm.tabs.team.heading}}"
                                 active="templateDetailsVm.tabs.team.active"
                                 select="templateDetailsVm.templateDetailsTabActivated(templateDetailsVm.tabs.team.id)">
                            <div ng-include="templateDetailsVm.tabs.team.template"
                                 ng-controller="TemplateTeamController as templateTeamVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{'PLAN' | translate}}" active="templateDetailsVm.tabs.plan.active"
                                 select="templateDetailsVm.templateDetailsTabActivated(templateDetailsVm.tabs.plan.id)">
                            <div ng-include="templateDetailsVm.tabs.plan.template"
                                 ng-controller="TemplatePlanController as templatePlanVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{templateDetailsVm.tabs.files.heading}}"
                                 active="templateDetailsVm.tabs.files.active"
                                 select="templateDetailsVm.templateDetailsTabActivated(templateDetailsVm.tabs.files.id)">
                            <div ng-include="templateDetailsVm.tabs.files.template"
                                 ng-controller="ProjectTemplateFilesController as templateFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="Workflow"
                                 active="templateDetailsVm.tabs.workflow.active"
                                 select="templateDetailsVm.templateDetailsTabActivated(templateDetailsVm.tabs.workflow.id)">
                            <div ng-include="templateDetailsVm.tabs.workflow.template"
                                 ng-controller="ProjectTemplateWorkflowController as projectTemplateWorkflowVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="templateDetailsVm.tabs" custom-tabs="templateDetailsVm.customTabs"
                                     object-value="templateDetailsVm.template" tab-id="templateDetailsVm.tabId"
                                     active="templateDetailsVm.active"></plugin-tabs>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

