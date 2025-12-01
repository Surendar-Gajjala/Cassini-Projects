<div class="view-container">
    <div class="view-content">
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div style="padding: 10px;">
                <div class="pull-right text-center">

                <%--<span ng-if="departmentPersonsVm.loading == false"><small>Page {{departmentPersonsVm.persons.number+1}} of
                    {{departmentPersonsVm.persons.totalPages}}
                </small></span>
                    <br>

                    <div class="btn-group" style="margin-bottom: 0">
                        <button class="btn btn-xs btn-default"
                                ng-click="departmentPersonsVm.previousPage()"
                                ng-disabled="departmentPersonsVm.perosns.first">
                            <i class="fa fa-chevron-left"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="departmentPersonsVm.nextPage()"
                                ng-disabled="departmentPersonsVm.persons.last">
                            <i class="fa fa-chevron-right"></i>
                        </button>
                    </div>
                    <br>--%>
                    <span ng-if="departmentPersonsVm.loading == false">Total: {{departmentPersonsVm.persons.length}} Department Persons
                    </span>
                </div>
            </div>
        </div>

        <table class="table table-striped">
            <thead>

            <tr>
                <th>FirstName</th>
                <th>LastName</th>
                <th>PhoneNo</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="departmentPersonsVm.loading == true">
                <td colspan="12">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading Persons...
                           </span>
                </td>
            </tr>

            <tr ng-if="departmentPersonsVm.loading == false && departmentPersonsVm.persons.content.length == 0">
                <td colspan="12">No Persons</td>

            </tr>
            <tr ng-repeat="person in departmentPersonsVm.persons">
                <td style="width: 150px;">
                    <%--<a href="" ui-sref="app.task.details({taskId: task.id, projectId: task.project.id})">--%>
                        {{person.firstName}}<%--</a>--%></td>
                <td>{{person.lastName}}</td>
                <td>{{person.phoneMobile}}</td>
                <td>{{person.email}}</td>
                <td>
                    <button title="Change Department" class="btn btn-xs btn-warning"
                            ng-click="departmentPersonsVm.openDepartmentDialogue(person)">Change Department</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
