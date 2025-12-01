<div class="panel panel-default panel-alt widget-messaging" >
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px; border: 1px solid #dddddd;">
        <div class="row">
            <div class="panel-title col-xs-12 col-sm-12 col-md-3 col-lg-3"
                 style="font-size:15px; padding: 20px 0 0 20px">
                Persons
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="widget-panel">
            <div class="pull-right text-center" style="padding: 5px;">
                <%--<div class="btn-group" style="margin-top: 10px;">
                    <button class="btn btn-xs btn-default"
                            ng-click="PersonsWidgetVm.previousPage()"
                            ng-disabled="PersonsWidgetVm.persons.first">
                        Previous
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="PersonsWidgetVm.nextPage()"
                            ng-disabled="PersonsWidgetVm.persons.last">
                        Next
                    </button>
                </div>--%>
                <br>
                    <span ng-if="PersonsWidgetVm.loading == false"><small>
                        {{PersonsWidgetVm.persons.length}} persons
                    </small></span>
            </div>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 200px;">Name</th>
                    <th style="width: 200px;">Designation</th>

                </tr>

                </thead>
                <tbody>
                <tr ng-repeat="person in PersonsWidgetVm.persons">
                    <td><a href="" ng-click="PersonsWidgetVm.showPersonDetails(person)">{{person.firstName}}</a>
                    </td>
                    <td>{{person.otherInfo.designation}}</td>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
    <!-- panel-body -->
</div>

