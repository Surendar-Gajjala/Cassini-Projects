<div class="view-toolbar">
    <button class="btn btn-sm btn-success min-width" ng-click="taskDetailsVm.back();">Back</button>
    <button class="btn btn-sm btn-info min-width" ng-click="taskDetailsVm.updateTask();">Save</button>
    <button class="btn btn-sm btn-warning min-width"
            ng-if="taskDetailsVm.buttonName != null"
            ng-click="taskDetailsVm.updateStatus(taskDetailsVm.buttonName);">{{taskDetailsVm.buttonName}}
    </button>
    <!--
    <button class="btn btn-sm btn-warning min-width"
            ng-if="taskDetailsVm.pendingButton != null"
            ng-click="taskDetailsVm.updateStatus('Pending');">{{taskDetailsVm.pendingButton}}
    </button>
    -->
</div>
<div class="item-details" style="padding: 30px">
    <h4 class="section-title">Basic Info</h4>
    <br>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Name :</span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" editable-text="taskDetailsVm.task.name">{{taskDetailsVm.task.name}}</a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>ProjectName :</span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ui-sref="app.project.details({projectId: taskDetailsVm.task.project})">
                {{taskDetailsVm.task.projectObject.name}}</a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Description : </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" editable-textarea="taskDetailsVm.task.description"
               e-rows="4" e-cols="60">{{taskDetailsVm.task.description}}</a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Location : </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" editable-textarea="taskDetailsVm.task.description"
               e-rows="4" e-cols="60">{{taskDetailsVm.task.location}}</a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Shift : </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" editable-textarea="taskDetailsVm.task.description"
               e-rows="4" e-cols="60">{{taskDetailsVm.task.shiftObject.name}}</a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Status : </span>
        </div>
        <%--<div class="value col-xs-8 col-sm-9">
            <a href="#" editable-select="taskDetailsVm.task.status"
               e-ng-options="s for s in taskDetailsVm.statuses">{{taskDetailsVm.task.status}}</a>
        </div>--%>
        <div class="value col-xs-8 col-sm-9">
            <div class="task-status"
                 ng-class="{'completed': (taskDetailsVm.task.status == 'FINISHED' || taskDetailsVm.task.status == 'VERIFIED' || taskDetailsVm.task.status == 'APPROVED'),
                                    'pending': taskDetailsVm.task.status == 'ASSIGNED'}"></div>
            <div class="task-status"
                 ng-class="{'completed': (taskDetailsVm.task.status == 'VERIFIED' || taskDetailsVm.task.status == 'APPROVED'),
                                    'pending': taskDetailsVm.task.status == 'ASSIGNED' || taskDetailsVm.task.status == 'FINISHED'}"></div>
            <div class="task-status"
                 ng-class="{'completed': taskDetailsVm.task.status == 'APPROVED',
                                    'pending': taskDetailsVm.task.status != 'APPROVED'}"></div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Assigned To : </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 100%" editable-select="taskDetailsVm.task.assignedToObject"
               e-ng-options="person as person.fullName for person in taskDetailsVm.staffs track by person.id">
                {{taskDetailsVm.task.assignedToObject.fullName}}</a>
            <%-- <span  editable-select="taskDetailsVm.task.assignedToObject" e-name="term_code"
                   e-ng-options="person.firstName for person in taskDetailsVm.persons"
                   e-required>
             </span>
                {{taskDetailsVm.task.assignedToObject.firstName}}--%>

        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Verified By : </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 100%" editable-select="taskDetailsVm.task.verifiedByObject"
               e-ng-options="person as person.fullName for person in taskDetailsVm.supervisors track by person.id">
                {{taskDetailsVm.task.verifiedByObject.fullName}}</a>
        </div>

    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Approved By : </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 100%" editable-select="taskDetailsVm.task.approvedByObject"
               e-ng-options="person as person.fullName for person in taskDetailsVm.officers track by person.id">
                {{taskDetailsVm.task.approvedByObject.fullName}}</a>
        </div>
    </div>
    <%--<div class="row" ng-show="taskDetailsVm.task.status == 'PENDING' &&
         taskDetailsVm.task.pendingReason.reason != 'New Reason...'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Reason for Pending : </span>
        </div>
        <div class="col-sm-3" ng-if="taskDetailsVm.task.pendingReason == null">
            <ui-select ng-model="taskDetailsVm.task.pendingReason" theme="bootstrap">
                <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                <ui-select-choices
                        repeat="pendingReason in taskDetailsVm.reasons | filter: $select.search">
                    <div ng-bind="pendingReason.reason"></div>
                </ui-select-choices>
            </ui-select>
        </div>
        <div class="col-sm-7" ng-if="taskDetailsVm.task.pendingReason != null">
            {{taskDetailsVm.task.pendingReason.reason}}
        </div>
    </div>


    <div class="row" ng-if="taskDetailsVm.task.pendingReason.reason == 'New Reason...'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Enter New Reason: </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <input type="text" style="height: 35px; width: 300px; border: 1px solid rgb(170, 164, 164) " name="Reason"
                   placeholder="Enter Reason name"
                   ng-model="taskDetailsVm.newReason.reason"><br>
            <textarea class="form-control" rows="3" name="Description" placeholder="Enter Description"
                      style="margin-top: 20px; width: 300px;" ng-model="taskDetailsVm.newReason.description"></textarea>

            <button style="margin: 10px 0px 0px 250px;" type="button" class="btn btn-sm btn-success"
                    ng-click="taskDetailsVm.createReason()">Create
            </button>
        </div>
    </div>--%>

    <div class="row" ng-show="taskDetailsVm.showNote">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span>Note : </span>
        </div>
        <div class="value col-xs-3 col-sm-4">
                    <textarea class="form-control" rows="4" name="Note" placeholder="Enter Note"
                              style="resize: none;" ng-model="taskDetailsVm.noteDetails"></textarea>
        </div>
        <div class="value col-xs-2 col-sm-2">
            <button class="btn btn-sm btn-success" ng-click="taskDetailsVm.addNote(taskDetailsVm.noteDetails);">Add</button>
        </div>
    </div>
</div>

<div class="item-details" style="padding: 30px">

    <h4 class="section-title">Notes</h4>
    <br>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Added By</th>
            <th>Time</th>
            <th>Notes</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="taskDetailsVm.notesLoading == true">
            <td colspan="12">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading Tasks Notes...
                           </span>
            </td>
        </tr>

        <tr ng-if="taskDetailsVm.notesLoading == false && taskDetailsVm.notes.length == 0">
            <td colspan="12">No Task Notes</td>
        </tr>
        <tr ng-repeat="note in taskDetailsVm.notes">
            <td>{{note.createdByObject.firstName}}</td>
            <td>{{note.createdDate}}</td>
            <td>{{note.details}}</td>
        </tr>
        </tbody>
    </table>
</div>


<div class="item-details" style="padding: 30px">

    <h4 class="section-title">History</h4>
    <br>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Old Status</th>
            <th>New Status</th>
            <th>Updated By</th>
            <th>Updated Time</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="taskDetailsVm.historyLoading == true">
            <td colspan="12">
               <span style="font-size: 15px;">
                   <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                        class="mr5">Loading Tasks History...
               </span>
            </td>
        </tr>

        <tr ng-if="taskDetailsVm.historyLoading == false && taskDetailsVm.taskHistory.length == 0">
            <td colspan="12">No Task History</td>
        </tr>
        <tr ng-repeat="history in taskDetailsVm.taskHistory">
            <td>{{history.oldStatus}}</td>
            <td>{{history.newStatus}}</td>
            <td>{{history.updatedBy}}</td>
            <td>{{history.timeStamp | date:'dd-MM-yyyy HH:mm:ss'}}</td>
        </tr>
        </tbody>
    </table>
</div>
