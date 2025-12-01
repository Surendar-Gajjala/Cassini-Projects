<div class="view-container">
    <div class="view-toolbar">

        <div ng-if="projectDetailsVm.tabs.tasks.active">
            <button class="btn btn-sm btn-success" ng-click="projectDetailsVm.newTask();">New Task</button>
            <free-text-search on-clear="projectDetailsVm.resetPage" on-search="projectDetailsVm.freeTextSearch"></free-text-search>
        </div>
        <div ng-if="projectDetailsVm.tabs.basic.active">
            <button class="btn btn-sm btn-success min-width" ng-click="projectDetailsVm.back();" >Back</button>
            <button class="btn btn-sm btn-info min-width" ng-click="projectDetailsVm.updateProject();">Save</button>
        </div>
    </div>

    <div class="view-content no-padding">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="project-details-tabs">
                    <uib-tabset>
                        <uib-tab heading="{{projectDetailsVm.tabs.basic.heading}}"
                                 active="projectDetailsVm.tabs.basic.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.basic.id)">
                            <div ng-include="projectDetailsVm.tabs.basic.template"
                                 ng-controller="ProjectBasicInfoController as projectBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{projectDetailsVm.tabs.tasks.heading}}"
                                 active="projectDetailsVm.tabs.tasks.active"
                                 select="projectDetailsVm.projectDetailsTabActivated(projectDetailsVm.tabs.tasks.id)">
                            <div ng-include="projectDetailsVm.tabs.tasks.template"
                                 ng-controller="ProjectTasksController as projectTasksVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
