<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px; border: 1px solid #dddddd" ;>
        <div class="row">
            <div class="panel-title col-xs-12 col-sm-12 col-md-3 col-lg-3"
                 style="font-size:15px; padding: 20px 0 0 20px">
                Departments
            </div>
        </div>
    </div>

    <div class="panel-body">
        <div class="widget-panel">
            <div class="pull-right text-center" style="padding: 2px;">
                <div class="btn-group" style="margin-bottom: 0">
                    <button class="btn btn-xs btn-default"
                            ng-click="DepartmentWidgetVm.previousPage()"
                            ng-disabled="DepartmentWidgetVm.departments.first">
                        Previous
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="DepartmentWidgetVm.nextPage()"
                            ng-disabled="DepartmentWidgetVm.departments.last">
                        Next
                    </button>
                </div>
                <br>
                    <span ng-if="DepartmentWidgetVm.loading == false"><small>
                        {{DepartmentWidgetVm.departments.totalElements}} departments
                    </small></span>
            </div>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="department in DepartmentWidgetVm.departments.content">
                    <td>{{department.name}}</td>
                    <td>{{department.description}}</td>
                </tr>
                </tbody>
            </table>


        </div>

    </div>
</div>