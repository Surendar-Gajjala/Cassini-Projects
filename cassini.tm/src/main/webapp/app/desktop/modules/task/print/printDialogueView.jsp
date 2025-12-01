<div class="view-container">
    <div class="view-toolbar">
        <h3 style="margin-top: 5px;">Daily Tasks</h3>
    </div>

    <div class="view-content">

        <form class="form-horizontal">

            <div class="form-group">
                <label class="col-sm-3 control-label">Person: </label>

                <div class="col-sm-7">
                    <ui-select ng-model="printDialogueVm.printTask.name" theme="bootstrap">
                        <ui-select-match placeholder="Select">{{$select.selected.fullName}}</ui-select-match>
                        <ui-select-choices repeat="staff in printDialogueVm.staffs | filter: {fullName: $select.search} | orderBy:'fullName'">
                            <div ng-bind="staff.fullName | highlight: $select.fullName.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>


                <%--<div class="col-sm-7">
                    <input type="text" class="form-control" placeholder="Person"
                           ng-model="printDialogueVm.printTask.name">
                </div>--%>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label">Date: </label>

                <div class="col-sm-7">
                    <div class="input-group">
                        <input type="text" class="form-control" date-picker
                               ng-model="printDialogueVm.printTask.date"
                               placeholder="dd/mm/yyyy">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                </div>
            </div>

        </form>
    </div>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" data-ng-click="printDialogueVm.cancel();">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success" ng-click="printDialogueVm.getTasksByPersonAndDate();">Print
            </button>

        </div>
    </div>
</div>


