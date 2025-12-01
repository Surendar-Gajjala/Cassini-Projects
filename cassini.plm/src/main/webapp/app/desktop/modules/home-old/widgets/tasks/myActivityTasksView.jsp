<div>
<style scoped>
    .widget-messaging ul li {
        padding: 0px !important;
    }

    .workflows-widget .tab-content, .workflows-widget .nav-tabs {
        border: 0 !important;
        border-radius: 0 !important;
    }

    .workflows-widget .nav-tabs {
        border-bottom: 1px solid #ddd !important;
    }

</style>
    <div class="panel panel-default panel-alt widget-messaging workflows-widget">
    <div class="panel-heading" style="">

        <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>MY_TASKS</span>
        </div>
        <div class="pull-right text-center" style="margin-top: 10px; padding: 2px;">
            <span ng-if="myActivityTasksVm.loading == false"><small>
                {{myActivityTasksVm.personTasks.numberOfElements}} of
                {{myActivityTasksVm.personTasks.totalElements}}
                <span translate>MY_TASKS</span>
            </small></span>

            <div class="btn-group" style="">
                <button class="btn btn-xs btn-default"
                        ng-click="myActivityTasksVm.previousPage()"
                        ng-disabled="myActivityTasksVm.personTasks.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="myActivityTasksVm.nextPage()"
                        ng-disabled="myActivityTasksVm.personTasks.last">
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>
        </div>
    </div>


    <div class="panel-body" style="overflow: hidden !important;">
        <style scoped>
            .tab-content {
                padding: 0px !important;
            }

            .tab-content .tab-pane {
                overflow: hidden !important;
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

            .ui-tabs-scrollable > .spacer > div:first-child > .nav-tabs {
                border-bottom: 1px solid #ddd;
            }

            .ui-tabs-scrollable > .nav-button {
                height: 43px;
            }
        </style>
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;">
            <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                <uib-tabset>
                    <uib-tab id="Projects" heading="Projects" active="myActivityTasksVm.tabs.projects.active"
                             select="myActivityTasksVm.taskTabActivated(myActivityTasksVm.tabs.projects.id)"
                             style="">
                        <div ng-include="myActivityTasksVm.tabs.projects.template"></div>
                    </uib-tab>

                    <uib-tab id="workflows" heading="Workflows"
                             ng-show="myActivityTasksVm.workflowTasks.content.length > 0"
                             active="myActivityTasksVm.tabs.workflows.active"
                             select="myActivityTasksVm.taskTabActivated(myActivityTasksVm.tabs.workflows.id)"
                             style="">
                        <div ng-include="myActivityTasksVm.tabs.workflows.template"></div>
                    </uib-tab>

                    <uib-tab id="InspectionPlan" heading="InspectionPlan"
                             ng-show="myActivityTasksVm.workflowTasks.content.length > 0"
                             active="myActivityTasksVm.tabs.inspectionPlan.active"
                             select="myActivityTasksVm.taskTabActivated(myActivityTasksVm.tabs.inspectionPlan.id)"
                             style="">
                        <div ng-include="myActivityTasksVm.tabs.inspectionPlan.template"></div>
                    </uib-tab>
                </uib-tabset>
            </scrollable-tabset>
        </div>
    </div>
</div>
</div>