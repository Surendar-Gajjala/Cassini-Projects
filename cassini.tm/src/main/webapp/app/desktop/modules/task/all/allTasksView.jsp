<div class="view-container">
    <div class="view-toolbar">

        <div class="col-sm-1">
            <button ng-if="hasRole('Administrator') == true || hasRole('Officer') == true || hasRole('Supervisor') == true || isAdmin() == true"
                    class="btn btn-sm btn-success" ng-click="allTasksVm.newTask();">New Task</button>
        </div>

        <div class="col-sm-1">
            <button class="btn btn-sm btn-success" ng-click="allTasksVm.print();" style="margin-left: -55px;">Print</button>
        </div>
        <div class="col-sm-3">
            <ui-select ng-model="allTasksVm.newTask.project"
                       on-select="allTasksVm.loadProjectTasks(allTasksVm.newTask.project)" theme="bootstrap"
                       style="width:120px;">
                <ui-select-match allow-clear="true" placeholder="All Projects">{{$select.selected.name}}
                </ui-select-match>
                <ui-select-choices
                        repeat="project in allTasksVm.projects.content | filter: $select.search">
                    <div ng-bind="project.name"></div>
                </ui-select-choices>
            </ui-select>
        </div>
        <free-text-search on-clear="allTasksVm.resetPage" on-search="allTasksVm.freeTextSearch"></free-text-search>

    </div>

    <div class="view-content">
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">


            <div style="padding: 10px;">
                <div class="pull-right text-center">

                <span ng-if="allTasksVm.loading == false"><small>Page {{allTasksVm.tasks.number+1}} of
                    {{allTasksVm.tasks.totalPages}}
                </small></span>
                    <br>

                    <div class="btn-group" style="margin-bottom: 0">
                        <button class="btn btn-xs btn-default"
                                ng-click="allTasksVm.previousPage()"
                                ng-disabled="allTasksVm.tasks.first">
                            <i class="fa fa-chevron-left"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="allTasksVm.nextPage()"
                                ng-disabled="allTasksVm.tasks.last">
                            <i class="fa fa-chevron-right"></i>
                        </button>
                    </div>
                    <br>
                    <span ng-if="allTasksVm.loading == false"><small>{{allTasksVm.tasks.totalElements}} Tasks
                    </small></span>
                </div>
            </div>
            <div>
                <a href="" class="btn btn-xs btn-danger" ng-if="allTasksVm.clear == true"
                   ng-click="allTasksVm.clearFilter()">Clear Filters</a>
            </div>
        </div>

        <table class="table table-striped">
            <thead>

            <tr>
                <th>Project</th>
                <th>Name</th>
                <th>Assigned Date</th>
                <th>Shift</th>
                <th>Work Location</th>
                <th>Status</th>
                <th>Assigned To</th>
                <th>Verified By</th>
                <th>Approved By</th>
                <th style="width: 100px;">Actions</th>
            </tr>
            </thead>

            <tbody>
            <td style="text-align: center; width: 150px;">

                <ui-select ng-model="allTasksVm.filters.projectObject"
                           on-select="allTasksVm.applyFilters()" theme="bootstrap"
                           style="width:120px;">
                    <ui-select-match allow-clear="true" placeholder="All Projects">{{$select.selected.name}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="project in allTasksVm.projects.content | filter: $select.search">
                        <div ng-bind="project.name"></div>
                    </ui-select-choices>
                </ui-select>

            </td>
            <td style="text-align: center; width: 150px;">
                <input type="text" class="form-control" name="Name"
                       ng-enter="allTasksVm.applyFilters()" placeholder="Name"
                       ng-model="allTasksVm.filters.name">
            </td>

            <td style="text-align: center; width: 150px;">
                <div class="input-group">
                    <input type="text" class="form-control" date-picker
                           ng-model="allTasksVm.filters.assignedDate"
                           name="AssignedDate" placeholder="dd/mm/yyyy">
                </div>
            </td>

            <td style="width: 150px;text-align:center;">
                <ui-select ng-model="allTasksVm.filters.shiftObject" on-select="allTasksVm.applyFilters()"
                           theme="bootstrap"
                           style="width:100%">
                    <ui-select-match allow-clear="true" placeholder="Shift">{{$select.selected.name}}
                    </ui-select-match>
                    <ui-select-choices repeat="shift in allTasksVm.shifts | filter: $select.search">
                        <div ng-bind="shift.name | highlight: $select.name.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td style="width: 150px;text-align:center;">
                <ui-select ng-model="allTasksVm.filters.workLocation" on-select="allTasksVm.applyFilters()"
                           theme="bootstrap"
                           style="width:100%">
                    <ui-select-match allow-clear="true" placeholder="Work Location">{{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices repeat="location in allTasksVm.locations.sort() | filter: $select.search">
                        <div ng-bind="location | highlight: $select.location.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td style="width: 150px;text-align:center;">
                <ui-select ng-model="allTasksVm.filters.status" on-select="allTasksVm.applyFilters()"
                           theme="bootstrap"
                           style="width:100%">
                    <ui-select-match allow-clear="true" placeholder="Status">{{$select.selected}}</ui-select-match>
                    <ui-select-choices repeat="status in allTasksVm.listStatus | filter: $select.search">
                        <div ng-bind="status | highlight: $select.status.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td style="width: 150px;text-align:center;">
                <ui-select ng-model="allTasksVm.filters.assignedToObject"
                           on-select="allTasksVm.applyFilters()"
                           theme="bootstrap"
                           style="width:100%">
                    <ui-select-match allow-clear="true" placeholder="Assigned To">{{$select.selected.firstName}}
                    </ui-select-match>
                    <ui-select-choices on-highlight="allTasksVm.sortValues(allTasksVm.staffs)" repeat="staff in allTasksVm.staffs | filter: $select.search">
                        <div ng-bind="staff.firstName | highlight: $select.firstName.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td style="width: 150px;text-align:center;">
                <ui-select ng-model="allTasksVm.filters.verifiedByObject" on-select="allTasksVm.applyFilters()"
                           theme="bootstrap"
                           style="width:100%">
                    <ui-select-match allow-clear="true" placeholder="Verified By">{{$select.selected.firstName}}
                    </ui-select-match>
                    <ui-select-choices on-highlight="allTasksVm.sortValues(allTasksVm.supervisors)" repeat="supervisor in allTasksVm.supervisors | filter: $select.search">
                        <div ng-bind="supervisor.firstName | highlight: $select.firstName.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td style="width: 150px;text-align:center;">
                <ui-select ng-model="allTasksVm.filters.approvedByObject" on-select="allTasksVm.applyFilters()"
                           theme="bootstrap"
                           style="width:100%">
                    <ui-select-match allow-clear="true" placeholder="Approved By">{{$select.selected.firstName}}
                    </ui-select-match>
                    <ui-select-choices on-highlight="allTasksVm.sortValues(allTasksVm.officers)" repeat="officer in allTasksVm.officers | filter: $select.search">
                        <div ng-bind="officer.firstName | highlight: $select.firstName.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td style="width:100px;">
                <div class="btn-group" style="margin-bottom: 0px;">
                    <button title="Apply Filters" type="button" class="btn btn-sm btn-success"
                            ng-click="allTasksVm.applyFilters()">
                        <i class="fa fa-search"></i>
                    </button>
                    <button title="Clear Filters" type="button" class="btn btn-sm btn-default"
                            ng-click="allTasksVm.resetFilters()">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </td>

            </tr>
            <tr ng-if="allTasksVm.loading == true">
                <td colspan="12">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading Tasks...
                           </span>
                </td>
            </tr>

            <tr ng-if="allTasksVm.loading == false && allTasksVm.tasks.content.length == 0">
                <td colspan="12">No Tasks</td>

            </tr>
            <tr ng-repeat="task in allTasksVm.tasks.content">
                <td style="width: 150px;">
                    <a href="" ui-sref="app.project.details({projectId: task.projectObject.id})">
                        {{task.projectObject.name}}</a>
                </td>
                <td style="width: 150px;">
                    <a href="" ui-sref="app.task.details({taskId: task.id, projectId: task.projectObject.id})">
                        {{task.name}}</a></td>
                <td>{{task.assignedDate}}</td>
                <td>{{task.shiftObject.name}}</td>
                <td>{{task.location}}</td>
                <td class="text-center">
                    <div class="task-status"
                         ng-class="{'completed': (task.status == 'FINISHED' || task.status == 'VERIFIED' || task.status == 'APPROVED'),
                                    'pending': task.status == 'ASSIGNED'}"></div>
                    <div class="task-status"
                         ng-class="{'completed': (task.status == 'VERIFIED' || task.status == 'APPROVED'),
                                    'pending': task.status == 'ASSIGNED' || task.status == 'FINISHED'}"></div>
                    <div class="task-status"
                         ng-class="{'completed': task.status == 'APPROVED',
                                    'pending': task.status != 'APPROVED'}"></div>
                </td>
                <td>{{task.assignedToObject.fullName}}</td>
                <td>{{task.verifiedByObject.fullName}}</td>
                <td>{{task.approvedByObject.fullName}}</td>
                <td style="width:100px;">
                    <button title="Delete Task" class="btn btn-xs btn-danger" ng-click="allTasksVm.deleteTask(task)">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
