<div class="view-container">
    <div class="view-toolbar">
    </div>
    <div class="view-content">
        <div class="row">
            <div class="col-md-6" ng-controller="ReportsWidgetController as ReportsWidgetVm">
                <div ng-include="templates.reportsWidget"></div>
            </div>

            <div class="col-md-6" ng-controller="TasksWidgetController as TasksWidgetVm">
                <div ng-include="templates.pendingTasksWidget"></div>
            </div>

            <%--<div class="col-md-6" ng-controller="PersonsWidgetController as PersonsWidgetVm">
                <div ng-include="templates.personsWidget"></div>
            </div>--%>
        </div>
        <br>

        <div class="row">
            <%--<div class="col-md-6" ng-controller="DepartmentWidgetController as DepartmentWidgetVm">
                <div ng-include="templates.departmentsWidget"></div>
            </div>--%>
            <div class="col-md-6" ng-controller="TasksWidgetController as TasksWidgetVm">
                <div ng-include="templates.completedTasksWidget"></div>
            </div>
            <div class="col-md-6" ng-controller="TasksWidgetController as TasksWidgetVm">
                <div ng-include="templates.tasksWidget"></div>
            </div>

            <%--<div class="col-md-6"  ng-controller="AccommodationWidgetController as AccommodationWidgetVm">
                <div ng-include="templates.accommodationWidget"></div>--%>
        </div>
        <br>
        <div class="row">
            <div class="col-md-6" ng-controller="TasksWidgetController as TasksWidgetVm">
                <div ng-include="templates.totalTasksWidget"></div>
            </div>
        </div>
    </div>
</div>
</div>
</div>
