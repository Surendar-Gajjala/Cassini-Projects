<style>
    table, th, td {
        border: 1px solid #ddd;
        border-collapse: collapse;
    }

    th, td {
        padding: 5px;
    }

    th {
        text-align: left;
    }

    table#t01 th {
        background-color: #1D2939;
    }

    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }
</style>
<div class="row row-eq-height" style="margin: 0;">
    <div ng-if="taskBasicVm.loading == true">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading details..
                            </span>
    </div>
    <div class="item-details col-sm-12" style="padding: 30px;" ng-if="taskBasicVm.loading == false">
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Name: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca;"
                   ng-if="(selectedProject.locked == false) && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'"
                   href="#"
                   editable-text="taskBasicVm.task.name"
                   onaftersave="taskBasicVm.updateTask()">
                    {{taskBasicVm.task.name}}</a>

                <p ng-if="(selectedProject.locked == false) && !(hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner)  && task.status != 'FINISHED'">
                    {{taskBasicVm.task.name}}</p>

                <p ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status == 'FINISHED'">
                    {{taskBasicVm.task.name}}</p>

                <p ng-if="selectedProject.locked == true">{{taskBasicVm.task.name}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Description: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca;"
                   ng-if="(selectedProject.locked == false) && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'"
                   href="#"
                   editable-text="taskBasicVm.task.description"
                   onaftersave="taskBasicVm.updateTask()">
                    {{taskBasicVm.task.description || 'Click to enter description'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'">
                    {{taskBasicVm.task.description}}</p>

                <p ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status == 'FINISHED'">
                    {{taskBasicVm.task.description}}</p>

                <p ng-if="selectedProject.locked == true">{{taskBasicVm.task.description}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Status: </span>
            </div>
            <div class="value col-sm-8 col-md-9"><span class="label" style="color: white"
                                                       ng-class="{ 'label-success': taskBasicVm.task.status == 'NEW','label-info': taskBasicVm.task.status == 'ASSIGNED','label-warning': taskBasicVm.task.status == 'INPROGRESS', 'label-primary': taskBasicVm.task.status == 'FINISHED'}">{{taskBasicVm.task.status}}</span>
            </div>
        </div>
        <div class="row" ng-if="!taskBasicVm.task.subContract">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Assigned To: </span>
            </div>

            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca; width: 150px;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'"
                   href="#"
                   editable-select="taskBasicVm.task.assignedToPerson"
                   onaftersave="taskBasicVm.updateTask()"
                   e-ng-options="person as person.fullName for person in taskBasicVm.persons |orderBy: ['fullName'] track by person.id">
                    {{taskBasicVm.task.assignedToPerson.fullName || 'Click to assign person'}}
                </a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'">
                    {{taskBasicVm.task.assignedToPerson.fullName}}</p>

                <p ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status == 'FINISHED'">
                    {{taskBasicVm.task.assignedToPerson.fullName}}</p>

                <p ng-if="selectedProject.locked == true">{{taskBasicVm.task.assignedToPerson.fullName}}</p>
            </div>

        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Site: </span>
            </div>
            <%--  <div class="value col-sm-8 col-md-9">
                  <span>{{taskBasicVm.task.siteName}}</span>
              </div>--%>


            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca; width: 150px;"
                   ng-if="selectedProject.locked == false && task.percentComplete == 0"
                   href="#"
                   editable-select="taskBasicVm.task.projectSite"
                   onaftersave="taskBasicVm.updateTask()"
                   e-ng-options="site as site.name for site in taskBasicVm.sites track by site.id">
                    {{taskBasicVm.task.siteName || 'Click to assign person'}}
                </a>

                <p ng-if="task.percentComplete > 0 && selectedProject.locked != true">
                    {{taskBasicVm.task.siteName}}</p>

                <p ng-if="selectedProject.locked == true">{{taskBasicVm.task.siteName}}</p>
            </div>


        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Created By: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                {{taskBasicVm.task.createdByPerson.fullName}}
            </div>
        </div>
        <div class="row" ng-if="taskBasicVm.task.subContract">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Sub Contract: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                {{taskBasicVm.task.assignedToPerson.fullName}}
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Planned Start Date: </span>
            </div>
            <div class="value col-sm-8 col-md-9">{{taskBasicVm.task.plannedStartDate}}
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Planned Finish Date: </span>
            </div>
            <div class="value col-sm-8 col-md-9">{{taskBasicVm.task.plannedFinishDate}}
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Actual Start Date: </span>
            </div>
            <div class="value col-sm-8 col-md-9">{{taskBasicVm.task.actualStartDate}}
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Actual Finish Date: </span>
            </div>
            <div class="value col-sm-8 col-md-9">{{taskBasicVm.task.actualFinishDate}}
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Percent Complete: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                <div class="task-progress progress text-center progress-bar-striped active"
                     style="width: 25%; display: inline-block">
                    <div style="width: {{taskBasicVm.task.percentComplete}}%"
                         class="progress-bar" role="progressbar"
                         aria-valuenow="40"
                         aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 2px;">{{taskBasicVm.task.percentComplete}}%</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Unit of Work: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'"
                   href="#"
                   editable-text="taskBasicVm.task.unitOfWork"
                   onaftersave="taskBasicVm.updateTask()">
                    {{taskBasicVm.task.unitOfWork || 'Click to enter unit of work'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'">
                    {{taskBasicVm.task.unitOfWork}}</p>

                <p ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status == 'FINISHED'">
                    {{taskBasicVm.task.unitOfWork}}</p>

                <p ng-if="selectedProject.locked == true">{{taskBasicVm.task.unitOfWork}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Total Units of Work: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status != 'FINISHED'"
                   href="#"
                   editable-text="taskBasicVm.task.totalUnits"
                   onaftersave="taskBasicVm.updateTask()">
                    {{taskBasicVm.task.totalUnits || 'Click to enter total units'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner)  && task.status != 'FINISHED'">
                    {{taskBasicVm.task.totalUnits}}</p>

                <p ng-if="selectedProject.locked == false && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner) && task.status == 'FINISHED'">
                    {{taskBasicVm.task.totalUnits}}</p>

                <p ng-if="selectedProject.locked == true">{{taskBasicVm.task.totalUnits}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Units Completed: </span>
            </div>
            <div class="value col-sm-8 col-md-9">{{taskBasicVm.totalUnitsCompleted}}
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Pending Units: </span>
            </div>
            <div class="value col-sm-8 col-md-9">{{taskBasicVm.task.totalUnits - taskBasicVm.totalUnitsCompleted}}
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Task Completion History: </span>
            </div>
            <div class="value col-sm-8 col-md-9" style="width: 800px;">
                <a style="color:#428bca;" href="" ng-click="taskBasicVm.showResources()" title="Add an entry"
                   ng-if="(((taskBasicVm.totalUnitsCompleted < taskBasicVm.task.totalUnits ) && !selectedProject.locked) && (hasPermission('permission.tasks.edit') || login.person.isTaskOwner || login.person.isProjectOwner))">
                    <i class="fa fa-plus-circle"></i>
                </a>

                <div>
                    <style scoped>
                        td {
                            vertical-align: middle !important;
                        }

                        .table-condensed, th, .table-condensed td {
                            padding: 5px !important;
                        }
                    </style>
                    <table class="table table-striped highlight-row table-condensed"
                           style="max-height: 100px; overflow-y: scroll">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Person</th>
                            <th>Units Completed</th>
                            <th style="text-align: left; text-indent: 30px ">Notes</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="taskBasicVm.hasUpdate">
                            <td>{{taskBasicVm.newUnitOfWorkCompleted.timeStamp}}</td>
                            <td>{{taskBasicVm.newUnitOfWorkCompleted.completedBy.fullName}}</td>
                            <td>
                                <div class="row">
                                    <input placeholder="Qty" class="form-control input-sm"
                                           ng-enter="taskBasicVm.updateTaskCompletion()"
                                           type="number" style="width: 100px; display: inline-block"
                                           data-ng-model="taskBasicVm.newUnitOfWorkCompleted.unitsCompleted">
                                </div>
                            </td>
                            <td>
                                <div class="row" style="padding-right: 8px;">
                                    <input placeholder="Enter Notes" class="form-control input-sm"
                                           type="text" style="width: 150px; display: inline-block"
                                           data-ng-model="taskBasicVm.newUnitOfWorkCompleted.notes">
                                    <a href="" ng-click="taskBasicVm.updateTaskCompletion()" title="Save"
                                       style="line-height: 35px;padding-left: 12px;"><i class="fa fa-check-circle"></i></a>
                                    <a href="" ng-click="taskBasicVm.cancelNewRow()" title="Cancel"
                                       style="line-height: 35px;"><i class="fa fa-times-circle"></i></a>
                                </div>
                            </td>
                        </tr>
                        <tr ng-if="!taskBasicVm.hasUpdate && taskBasicVm.taskHistory.length == 0">
                            <td colspan="4">No units completed</td>
                        </tr>
                        <tr ng-repeat="row in taskBasicVm.taskHistory">
                            <td>{{row.timeStamp}}</td>
                            <td>{{row.completedByObject.fullName}}</td>
                            <td>{{row.unitsCompleted}}</td>
                            <td title="{{row.notes}}" id="description"
                                style="text-align:inherit; vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">
                                {{row.notes}}
                            </td>
                            <td ng-if="row.resources == true"><i class="fa fa-info-circle" style="color: #337ab7"
                                                                 title="view task resources"
                                                                 ng-click="taskBasicVm.showTaskResources(row)"></i>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Inspection Person: </span>
            </div>

            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (login.person.isTaskOwner || login.person.isProjectOwner) && (taskBasicVm.task.inspectionResult == 'REJECTED' || (taskBasicVm.task.inspectionResult == null && (taskBasicVm.task.status != 'ASSIGNED' || taskBasicVm.task.inspectionResult == 'REJECTED')) || (taskBasicVm.task.inspectionResult == 'ACCEPTED'  && taskBasicVm.task.percentComplete != 100))"
                   href="#"
                   editable-select="taskBasicVm.task.inspectedByPerson"
                   onaftersave="taskBasicVm.updateTask()"
                   e-ng-options="person.fullName for person in taskBasicVm.persons |orderBy:['fullName'] track by person.fullName">
                    {{taskBasicVm.task.inspectedByPerson.fullName || 'click to select person' }}
                </a>

                <p ng-if="selectedProject.locked == false && !((login.person.isTaskOwner || login.person.isProjectOwner) && (taskBasicVm.task.inspectionResult == 'REJECTED' || (taskBasicVm.task.inspectionResult == null && (taskBasicVm.task.status != 'ASSIGNED' || taskBasicVm.task.inspectionResult == 'REJECTED')) || (taskBasicVm.task.inspectionResult == 'ACCEPTED'  && taskBasicVm.task.percentComplete != 100)))">
                    {{taskBasicVm.task.inspectedByPerson.fullName}}</p>

                <p ng-if="selectedProject.locked == true">{{taskBasicVm.task.inspectedByPerson.fullName}}</p>
            </div>

        </div>
        <div class="row" ng-if="taskBasicVm.task.inspectionResult != null">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Inspection Result: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                <span class="label" style="color: white"
                      ng-class="{ 'label-success': taskBasicVm.task.inspectionResult == 'ACCEPTED', 'label-danger': taskBasicVm.task.inspectionResult == 'REJECTED'}">{{taskBasicVm.task.inspectionResult}}</span>
            </div>
        </div>
        <div class="row" ng-if="taskBasicVm.task.inspectionResult != null">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Inspected On: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                {{taskBasicVm.task.inspectedOn}}
            </div>
        </div>
        <div class="row" ng-if="taskBasicVm.task.inspectionResult != null">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Inspection Remarks: </span>
            </div>
            <div class="value col-sm-8 col-md-9">
                {{taskBasicVm.task.inspectionRemarks}}
            </div>
        </div>
        <attributes-details-view attribute-id="taskId" attribute-type="TASK"
                                 has-permission="(hasPermission('permission.home.editBasic') || login.person.isProjectOwner || login.person.isTaskOwner)"></attributes-details-view>
    </div>
</div>
</div>


