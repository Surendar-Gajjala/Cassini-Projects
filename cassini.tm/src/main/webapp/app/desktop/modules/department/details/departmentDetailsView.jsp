<div class="view-container">
    <div class="view-toolbar">

        <div ng-if="departmentsDetailsVm.tabs.tasks.active">
            <button class="btn btn-sm btn-success min-width" ng-click="departmentsDetailsVm.back();">Back</button>
          <%--  <button class="btn btn-sm btn-success" ng-click="departmentsDetailsVm.newTask();">Add Task</button>--%>
            <free-text-search on-clear="departmentsDetailsVm.resetPage" on-search="departmentsDetailsVm.freeTextSearch"></free-text-search>
        </div>
        <div ng-if="departmentsDetailsVm.tabs.persons.active">
            <button class="btn btn-sm btn-success min-width" ng-click="departmentsDetailsVm.back();">Back</button>
            <%--<button class="btn btn-sm btn-success" ng-click="departmentsDetailsVm.newTask();">Add Person</button>--%>
            <free-text-search on-clear="departmentsDetailsVm.resetPage" on-search="departmentsDetailsVm.freeTextSearch"></free-text-search>
        </div>
        <div ng-if="departmentsDetailsVm.tabs.basic.active">
            <button class="btn btn-sm btn-success min-width" ng-click="departmentsDetailsVm.back();" >Back</button>
            <button class="btn btn-sm btn-info min-width" ng-click="departmentsDetailsVm.updateDepartment();">Save</button>
        </div>
    </div>

    <div class="view-content no-padding">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="department-details-tabs">
                    <uib-tabset>
                        <uib-tab heading="{{departmentsDetailsVm.tabs.basic.heading}}"
                                 active="departmentsDetailsVm.tabs.basic.active"
                                 select="departmentsDetailsVm.departmentDetailsTabActivated(departmentsDetailsVm.tabs.basic.id)">
                            <div ng-include="departmentsDetailsVm.tabs.basic.template"
                                 ng-controller="DepartmentBasicInfoController as departmentBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{departmentsDetailsVm.tabs.tasks.heading}}"
                                 active="departmentsDetailsVm.tabs.tasks.active"
                                 select="departmentsDetailsVm.departmentDetailsTabActivated(departmentsDetailsVm.tabs.tasks.id)">
                            <div ng-include="departmentsDetailsVm.tabs.tasks.template"
                                 ng-controller="DepartmentTasksController as departmentTasksVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{departmentsDetailsVm.tabs.persons.heading}}"
                                 active="departmentsDetailsVm.tabs.persons.active"
                                 select="departmentsDetailsVm.departmentDetailsTabActivated(departmentsDetailsVm.tabs.persons.id)">
                            <div ng-include="departmentsDetailsVm.tabs.persons.template"
                                 ng-controller="DepartmentPersonsController as departmentPersonsVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>






















<%--
<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success min-width" ng-click="departmentsDetailsVm.back();">Back</button>
        <button class="btn btn-sm btn-info min-width" ng-click="departmentsDetailsVm.updateDepartment();">Save</button>
    </div>

    <div class="view-content">
        <div class="item-details" style="padding: 30px">
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Name :</span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" editable-text="departmentsDetailsVm.department.name">{{departmentsDetailsVm.department.name}}</a>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Description : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" editable-textarea="departmentsDetailsVm.department.description" e-rows="4" e-cols="60">
                        {{departmentsDetailsVm.department.description}}</a>
                </div>
            </div>
        </div>
    </div>
</div>

--%>
