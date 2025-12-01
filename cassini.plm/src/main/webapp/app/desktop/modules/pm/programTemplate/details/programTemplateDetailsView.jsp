<style>
    .project-progress.progress {
        background-color: #B0C7CF;
        height: 25px !important;
        margin: 0 !important;
    }

    .tab-content {
        padding: 0px !important;
    }

    .tab-content .tab-pane {
        overflow: auto !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 260px;
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

    .tab-content .tab-pane #gantt_here {
        height: 100%;
        position: absolute;
    }

    .tab-content .tab-pane .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px !important;
        z-index: 5;
    }

</style>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="programTemplateDetailsVm.back()"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{programTemplateDetailsVm.addWorkflowTitle}}"
                    ng-show="programTemplateDetailsVm.tabs.workflow.active && template.workflow == null"
                    ng-click="programTemplateDetailsVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="programTemplateDetailsVm.tabs.workflow.active"
                    ng-show="template.workflow != null && template.startWorkflow != true"
                    title="{{programTemplateDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="programTemplateDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>
        </div>
        <div ng-show="programTemplateDetailsVm.tabs.files.active">
            <free-text-search   on-clear="programTemplateDetailsVm.onClear"
                on-search="programTemplateDetailsVm.freeTextSearch"></free-text-search>
       </div>
                          
    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="programTemplateDetailsVm.active">
                        <uib-tab heading="{{programTemplateDetailsVm.tabs.basic.heading}}"
                                 active="programTemplateDetailsVm.tabs.basic.active"
                                 select="programTemplateDetailsVm.templateDetailsTabActivated(programTemplateDetailsVm.tabs.basic.id)">
                            <div ng-include="programTemplateDetailsVm.tabs.basic.template"
                                 ng-controller="ProgramTemplateBasicInfoController as programTemplateBasicInfoVm"></div>
                        </uib-tab>
                        <uib-tab id="program-resources"
                                 heading="{{programTemplateDetailsVm.tabs.resources.heading}}"
                                 active="programTemplateDetailsVm.tabs.resources.active"
                                 select="programTemplateDetailsVm.templateDetailsTabActivated(programTemplateDetailsVm.tabs.resources.id)">
                            <div ng-include="programTemplateDetailsVm.tabs.resources.template"
                                 ng-controller="ProgramTemplateResourcesController as programTemplateResourcesVm"></div>
                        </uib-tab>
                        <uib-tab id="program-projects"
                                 heading="{{programTemplateDetailsVm.tabs.projects.heading}}"
                                 active="programTemplateDetailsVm.tabs.projects.active"
                                 select="programTemplateDetailsVm.templateDetailsTabActivated(programTemplateDetailsVm.tabs.projects.id)">
                            <div ng-include="programTemplateDetailsVm.tabs.projects.template"
                                 ng-controller="ProgramTemplateProjectsController as programTemplateProjectsVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{programTemplateDetailsVm.tabs.files.heading}}"
                                 active="programTemplateDetailsVm.tabs.files.active"
                                 select="programTemplateDetailsVm.templateDetailsTabActivated(programTemplateDetailsVm.tabs.files.id)">
                            <div ng-include="programTemplateDetailsVm.tabs.files.template"
                                 ng-controller="ProgramTemplateFilesController as programTemplateFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow" heading="{{programTemplateDetailsVm.tabs.workflow.heading}}"
                                 active="programTemplateDetailsVm.tabs.workflow.active"
                                 select="programTemplateDetailsVm.templateDetailsTabActivated(programTemplateDetailsVm.tabs.workflow.id)">
                            <div ng-include="programTemplateDetailsVm.tabs.workflow.template"
                                 ng-controller="ProgramTemplateWorkflowController as programTemplateWorkflowVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="programTemplateDetailsVm.tabs"
                                     custom-tabs="programTemplateDetailsVm.customTabs"
                                     object-value="programTemplateDetailsVm.template"
                                     tab-id="programTemplateDetailsVm.tabId"
                                     active="programTemplateDetailsVm.active"></plugin-tabs>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

