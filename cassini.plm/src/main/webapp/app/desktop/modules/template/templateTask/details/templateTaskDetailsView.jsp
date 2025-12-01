<style>
    .activity-progress.progress {
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
            <button class="btn btn-sm btn-default" ng-click="templateTaskDetailsVm.back()"
                    title="{{backTitle}}">
                <i class="fa fa-arrow-left" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-success"
                    title="{{templateTaskDetailsVm.addWorkflowTitle}}"
                    ng-show="templateTaskDetailsVm.tabs.workflow.active && task.workflow == null"
                    ng-click="templateTaskDetailsVm.addWorkflow()">
                <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
            </button>
            <button ng-if="templateTaskDetailsVm.tabs.workflow.active"
                    ng-show="task.workflow != null && task.startWorkflow != true"
                    title="{{templateTaskDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="templateTaskDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>

        </div>
        <div ng-show="templateTaskDetailsVm.tabs.files.active">
            <free-text-search   on-clear="templateTaskDetailsVm.onClear"
                on-search="templateTaskDetailsVm.freeTextSearch"></free-text-search>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="templateTaskDetailsVm.active">
                        <uib-tab heading="{{templateTaskDetailsVm.tabs.basic.heading}}"
                                 active="templateTaskDetailsVm.tabs.basic.active"
                                 select="templateTaskDetailsVm.templateDetailsTabActivated(templateTaskDetailsVm.tabs.basic.id)">
                            <div ng-include="templateTaskDetailsVm.tabs.basic.template"
                                 ng-controller="TemplateTaskBasicInfoController as templateTaskBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{templateTaskDetailsVm.tabs.files.heading}}"
                                 select="templateTaskDetailsVm.templateDetailsTabActivated(templateTaskDetailsVm.tabs.files.id)"
                                 active="templateTaskDetailsVm.tabs.files.active">
                            <div ng-include="templateTaskDetailsVm.tabs.files.template"
                                 ng-controller="TemplateTaskFilesController as templateTaskFilesVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{templateTaskDetailsVm.tabs.workflow.heading}}"
                                 select="templateTaskDetailsVm.templateDetailsTabActivated(templateTaskDetailsVm.tabs.workflow.id)"
                                 active="templateTaskDetailsVm.tabs.workflow.active">
                            <div ng-include="templateTaskDetailsVm.tabs.workflow.template"
                                 ng-controller="TemplateTaskWorkflowController as templateTaskWorkflowVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

