<div class="view-container">
    <div class="view-toolbar">
        <h3 style="margin-top: 5px;">New Task</h3>
    </div>

    <div class="modal-body" style="padding: 20px;">
        <form class="form-horizontal">

            <div class="form-group">
                <label class="col-sm-3 control-label">Project:</label>

                <div class="col-sm-7" ng-if="newTaskDialogueVm.project == null
                && newTaskDialogueVm.projects.content.length >= 2">
                    <ui-select ng-model="newTaskDialogueVm.newTask.project" theme="bootstrap">
                        <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                        <ui-select-choices
                                repeat="project in newTaskDialogueVm.projects.content | filter: $select.search">
                            <div ng-bind="project.name"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>

                <div class="col-sm-7" ng-if="newTaskDialogueVm.project != null">
                    <span ng-model="newTaskDialogueVm.newTask.project" theme="bootstrap">
                        <span style="display: inline-block; margin-top: 9px;">{{newTaskDialogueVm.newTask.project.name}}</span>
                    </span>
                </div>

                <%--<div class="col-sm-7" ng-if="newTaskDialogueVm.projects.content.length == 1">
                    <span ng-model="newTaskDialogueVm.newTask.project" theme="bootstrap">
                        <span style="display: inline-block; margin-top: 9px;">{{newTaskDialogueVm.project.name}}</span>
                    </span>
                </div>--%>

            </div>

            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.project != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.project}}
            </p>

            <div class="form-group">
                <label class="col-sm-3 control-label">Name: </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" name="Name" placeholder="Name"
                           ng-model="newTaskDialogueVm.newTask.name"
                           ng-enter="newTaskDialogueVm.search()">
                </div>
            </div>
            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.name != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.name}}
            </p>

            <div class="form-group">
                <label class="col-sm-3 control-label">Assigned Date: </label>

                <div class="col-sm-7">
                    <div class="input-group">
                        <input type="text" class="form-control" date-picker
                               ng-model="newTaskDialogueVm.newTask.assignedDate"
                               name="AssignedDate" placeholder="dd/mm/yyyy">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                </div>
            </div>
            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.assignedDate != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.assignedDate}}
            </p>

            <div class="form-group">
                <label class="col-sm-3 control-label">Description: </label>

                <div class="col-sm-7">
                    <textarea class="form-control" rows="5" name="Description" placeholder="Description"
                              style="resize: none;" ng-model="newTaskDialogueVm.newTask.description"></textarea>
                </div>
            </div>

            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.description != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.description}}
            </p>

            <div class="form-group">
                <label class="col-sm-3 control-label">Shift Name:</label>

                <div class="col-sm-7">
                    <ui-select class="required-field" ng-model="newTaskDialogueVm.newTask.shift" theme="bootstrap">
                        <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                        <ui-select-choices repeat="shift in newTaskDialogueVm.shifts | filter: $select.search">
                            <div ng-bind="shift.name | highlight: $select.name.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>

            <div class="form-group"<%-- ng-show="newTaskDialogueVm.newTask.location != 'New Location...'"--%>>
                <label class="col-sm-3 control-label">Work Location:</label>

                <div class="col-sm-7">
                    <ui-select class="required-field" ng-model="newTaskDialogueVm.newTask.location" theme="bootstrap">
                        <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                        <ui-select-choices repeat="location in newTaskDialogueVm.locations.sort() | filter: $select.search">
                            <div ng-bind="location | highlight: $select.location.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <%--<p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.location != null"
               ng-if="newTaskDialogueVm.newTask.location != 'New Location...'" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.location}}
            </p>

            <div ng-if="newTaskDialogueVm.newTask.location == 'New Location...'" class="form-group">
                <label class="col-sm-4 control-label text-danger">Enter New Location: </label>

                <div class="col-sm-6">
                    <input type="text" class="form-control" name="Location" placeholder="Location"
                           ng-model="newTaskDialogueVm.newLocation"
                           ng-blur="newTaskDialogueVm.locationValid(newTaskDialogueVm.newLocation)">
                </div>
            </div>

            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.newLocation != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.newLocation}}
            </p>--%>

            <div class="form-group">
                <label class="col-sm-3 control-label">Assigned To:</label>

                <div class="col-sm-7">
                    <ui-select ng-model="newTaskDialogueVm.newTask.assignedTo" theme="bootstrap">
                        <ui-select-match placeholder="Select">{{$select.selected.fullName}}</ui-select-match>
                        <ui-select-choices repeat="staff in newTaskDialogueVm.staffs | filter: {fullName: $select.search} | orderBy:'fullName'">
                            <div ng-bind="staff.fullName | highlight: $select.fullName.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>

            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.assignedTo != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.assignedTo}}
            </p>

            <div class="form-group">
                <label class="col-sm-3 control-label">Verified By:</label>

                <div class="col-sm-7">
                    <ui-select ng-model="newTaskDialogueVm.newTask.verifiedBy" theme="bootstrap">
                        <ui-select-match placeholder="Select">{{$select.selected.fullName}}</ui-select-match>
                        <ui-select-choices repeat="person in newTaskDialogueVm.supervisors | filter: {fullName: $select.search} | orderBy:'fullName'">
                            <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.verifiedBy != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.verifiedBy}}
            </p>

            <div class="form-group">
                <label class="col-sm-3 control-label">Approved By:</label>

                <div class="col-sm-7">
                    <ui-select ng-model="newTaskDialogueVm.newTask.approvedBy" theme="bootstrap">
                        <ui-select-match placeholder="Select">{{$select.selected.fullName}}</ui-select-match>
                        <ui-select-choices repeat="person in newTaskDialogueVm.officers | filter: {fullName: $select.search} | orderBy:'fullName'">
                            <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newTaskDialogueVm.errorMessage.approvedBy != null" ng-cloak>
                {{ newTaskDialogueVm.errorMessage.approvedBy}}
            </p>
        </form>
    </div>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" data-ng-click="newTaskDialogueVm.cancel();">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success" ng-click="newTaskDialogueVm.create()">Create
            </button>

        </div>
    </div>
</div>
