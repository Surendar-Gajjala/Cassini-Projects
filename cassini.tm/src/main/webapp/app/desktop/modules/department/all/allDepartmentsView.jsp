<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="allDepartmentsVm.createDepartment();">Add Department</button>
        <free-text-search on-clear="allDepartmentsVm.resetPage"
                          on-search="allDepartmentsVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content">
        <div style="padding: 10px;">
            <div class="pull-right text-center">
                <span ng-if="allDepartmentsVm.loading == false"><small>Page {{allDepartmentsVm.departments.number+1}} of
                    {{allDepartmentsVm.departments.totalPages}}
                </small></span>
                <br>

                <div class="btn-group" style="margin-bottom: 0">
                    <button class="btn btn-xs btn-default"
                            ng-click="allDepartmentsVm.previousPage()"
                            ng-disabled="allDepartmentsVm.departments.first">
                        <i class="fa fa-chevron-left"></i>
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="allDepartmentsVm.nextPage()"
                            ng-disabled="allDepartmentsVm.departments.last">
                        <i class="fa fa-chevron-right"></i>
                    </button>
                </div>
                <br>
                    <span ng-if="allDepartmentsVm.loading == false"><small>
                        {{allDepartmentsVm.departments.totalElements}}
                        Departments
                    </small></span>
            </div>
            <div>
                <a href="" class="btn btn-xs btn-danger" ng-if="allDepartmentsVm.clear == true"
                   ng-click="allDepartmentsVm.clearFilter()">Clear Filters</a>
            </div>


        </div>


        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 200px;">Name</th>
                <th style="width: 200px;">Description</th>
                <%-- <th style="width: 150px;">Actions</th>--%>
            </tr>

            </thead>
            <tbody>
            <tr ng-repeat="department in allDepartmentsVm.departments.content">
                <td><a href="" ng-click="allDepartmentsVm.showDepartmentDetails(department)">{{department.name}}</a>
                </td>
                <td>{{department.description}}</td>
                <%-- <td>
                     <button title="Delete Department" class="btn btn-xs btn-danger"
                             ng-click="allDepartmentsVm.deleteDepartment(department)"><i class="fa fa-trash"></i></button>
                 </td>--%>
            </tr>
            </tbody>
        </table>

    </div>
</div>





