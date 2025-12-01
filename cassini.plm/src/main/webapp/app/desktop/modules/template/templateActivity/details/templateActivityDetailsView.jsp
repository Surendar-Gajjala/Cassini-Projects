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
            <button class="btn btn-sm btn-default" ng-click="templateActivityDetailsVm.back()"
                    title="{{backTitle}}">
                <i class="fa fa-arrow-left" aria-hidden="true"></i>
            </button>
            <%--   <button ng-if="templateActivityDetailsVm.tabs.tasks.active && hasPermission('project','edit')"
                       title="{{templateActivityDetailsVm.addTask}}"
                       class="btn btn-sm btn-success min-width" ng-click="addActivityTasks()" translate>
                   ADD_TASK
               </button>--%>
        </div>
        <div ng-show="templateActivityDetailsVm.tabs.files.active">
            <free-text-search on-clear="templateActivityDetailsVm.onClear"
                              on-search="templateActivityDetailsVm.freeTextSearch"></free-text-search>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="templateActivityDetailsVm.active">
                        <uib-tab heading="{{templateActivityDetailsVm.tabs.basic.heading}}"
                                 active="templateActivityDetailsVm.tabs.basic.active"
                                 select="templateActivityDetailsVm.templateDetailsTabActivated(templateActivityDetailsVm.tabs.basic.id)">
                            <div ng-include="templateActivityDetailsVm.tabs.basic.template"
                                 ng-controller="TemplateActivityBasicInfoController as templateActivityBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{templateActivityDetailsVm.tabs.files.heading}}"
                                 select="templateActivityDetailsVm.templateDetailsTabActivated(templateActivityDetailsVm.tabs.files.id)"
                                 active="templateActivityDetailsVm.tabs.files.active">
                            <div ng-include="templateActivityDetailsVm.tabs.files.template"
                                 ng-controller="TemplateActivityFilesController as templateActivityFilesVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{templateActivityDetailsVm.tabs.workflow.heading}}"
                                 select="templateActivityDetailsVm.templateDetailsTabActivated(templateActivityDetailsVm.tabs.workflow.id)"
                                 active="templateActivityDetailsVm.tabs.workflow.active">
                            <div ng-include="templateActivityDetailsVm.tabs.workflow.template"
                                 ng-controller="TemplateActivityWorkflowController as templateActivityWorkflowVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

