<style>
    th {
        vertical-align: middle;
        text-align: center;
        border: none !important;
    }

    td {
        text-align: center;
        border: none !important;
    }

    .table {
        border: none !important;
    }
</style>
<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th>Title</th>
            <th>Description</th>
            <th>Assigned To</th>
            <th>Status</th>
            <th>Priority</th>
            <th>Reported By</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="problemVm.loading == true">
            <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Task Problems..
                        </span>
            </td>
        </tr>
        <tr ng-if="problemVm.taskProblems.length == 0 && problemVm.loading == false">
            <td colspan="12">No Problems are available to view</td>
        </tr>

        <tr ng-repeat="problem in problemVm.taskProblems" ng-if="problemVm.loading == false">
            <td ng-if="hasPermission('permission.tasks.edit') || login.person.isProjectOwner || login.person.isTaskOwner">
                <a href="" ng-click="problemVm.openProblem(problem)" title="Click to view problem details"><span
                        ng-bind-html="problem.title | highlightText: freeTextQuery"></span></a></td>
            <td ng-if="!(hasPermission('permission.tasks.edit') || login.person.isProjectOwner || login.person.isTaskOwner)">
                {{problem.title}}
            </td>
            <td><span
                    ng-bind-html="problem.description.length > 50 ? problem.description.trunc(50,true) : problem.description | highlightText: freeTextQuery"></span>
            </td>
            <td>{{problem.assignedToObject.fullName}}</td>
            <td> <span class="label" style="color: white" ng-class="{
                                    'label-success': problem.status == 'NEW',
                                    'label-info': problem.status == 'ASSIGNED',
                                    'label-warning': problem.status == 'INPROGRESS',
                                    'label-danger': problem.status == 'CLOSED'}">  {{problem.status}} </span>
            </td>
            <td> <span class="label" style="color: white" ng-class="{
                                    'label-info': problem.priority == 'LOW',
                                    'label-warning': problem.priority == 'MEDIUM',
                                    'label-danger': problem.priority == 'HIGH'}"> {{problem.priority}} </span>
            </td>
            <td>{{problem.createdByObject.fullName}}</td>
        </tr>
        </tbody>
    </table>
</div>
