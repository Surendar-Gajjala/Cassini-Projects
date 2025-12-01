<div class="view-container md-whiteframe-z5" id="newTaskView">
    <style scoped>
        .md-block {
            margin: 0 !important;
        }
        .custom-md-datepicker {
            margin-bottom: 15px;
        }
        .custom-md-datepicker > button {
            display: none;
        }
        .custom-md-datepicker > .md-datepicker-input-container {
            width: 100% !important;
            margin-left: 2px !important;
        }
        .date-label {
            margin-bottom: 0;
            color: rgba(0,0,0, 0.3) !important;
            font-size: 15px;
            margin-left: 0px;
        }
    </style>

    <div layout="column" style="padding: 10px;">
        <div layout="row" class="buttons-row">
            <div flex class="text-left">
                <md-button md-no-link class="md-primary" ng-click="newTaskVm.create()">Create</md-button>
            </div>
            <div flex>

            </div>
            <div flex class="text-right">
                <md-button md-no-link class="md-primary" ng-click="newTaskVm.cancel()">Cancel</md-button>
            </div>
        </div>
        <md-content layout-padding class="md-whiteframe-3dp">
            <div layout="column" style="padding: 10px;">
                <md-input-container class="md-block">
                    <label>Project</label>
                    <md-select ng-model="newTaskVm.newTask.projectObject" ng-model-options="{trackBy: '$value.id'}">
                        <md-option ng-repeat="project in newTaskVm.projects" ng-value="project">{{project.name}}</md-option>
                    </md-select>
                </md-input-container>
                <br>
                <md-input-container class="md-block">
                    <label>Name</label>
                    <input ng-model="newTaskVm.newTask.name">
                </md-input-container>
                <md-input-container class="md-block">
                    <label>Description</label>
                    <textarea ng-model="newTaskVm.newTask.description" md-maxlength="1000" rows="10" md-select-on-focus></textarea>
                </md-input-container>
                <div layout="column" flex>
                    <label class="md-caption date-label" style="margin-left: 3px;">Assigned Date</label>
                    <md-datepicker ng-model="newTaskVm.newTask.assignedDateObject" md-placeholder="" class="custom-md-datepicker"></md-datepicker>
                </div>
                <md-input-container class="md-block">
                    <label>Shift</label>
                    <md-select ng-model="newTaskVm.newTask.shiftObject">
                        <md-option ng-repeat="shift in newTaskVm.shifts" ng-value="shift">{{shift.name}}</md-option>
                    </md-select>
                </md-input-container>
                <br>
                <md-input-container class="md-block">
                    <label>Location</label>
                    <md-select ng-model="newTaskVm.newTask.location">
                        <md-option ng-repeat="location in newTaskVm.locations" ng-value="location">{{location}}</md-option>
                    </md-select>
                </md-input-container>
                <br>
                <md-input-container class="md-block">
                    <label>Assigned To</label>
                    <md-select ng-model="newTaskVm.newTask.assignedToObject">
                        <md-option ng-repeat="person in newTaskVm.staff" ng-value="person">{{person.firstName}}, {{person.lastName}}</md-option>
                    </md-select>
                </md-input-container>
                <br>
                <md-input-container class="md-block">
                    <label>Verified By</label>
                    <md-select ng-model="newTaskVm.newTask.verifiedByObject">
                        <md-option ng-repeat="person in newTaskVm.supervisors" ng-value="person">{{person.firstName}}, {{person.lastName}}</md-option>
                    </md-select>
                </md-input-container>
                <br>
                <md-input-container class="md-block">
                    <label>Approved By</label>
                    <md-select ng-model="newTaskVm.newTask.approvedByObject">
                        <md-option ng-repeat="person in newTaskVm.officers" ng-value="person">{{person.firstName}}, {{person.lastName}}</md-option>
                    </md-select>
                </md-input-container>
            </div>
        </md-content>
    </div>
</div>