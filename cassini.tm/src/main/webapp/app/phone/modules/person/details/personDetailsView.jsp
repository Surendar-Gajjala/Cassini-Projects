<div class="view-container md-whiteframe-z5" style="padding: 10px;">
    <style scoped>
        .md-block {
            margin: 0 !important;
        }
        .buttons-row {
            padding: 10px;
        }
    </style>

    <div layout="row" class="buttons-row">
        <div flex class="text-left">
            <md-button md-no-link class="md-primary" ng-click="newTaskVm.create()">Save</md-button>
        </div>
        <div flex>

        </div>
        <div flex class="text-right">
            <md-button md-no-link class="md-primary" ng-click="newTaskVm.cancel()">Cancel</md-button>
        </div>
    </div>

    <div class="md-whiteframe-2dp" layout="column">
        <div style="line-height: 20px;background-color: #00bcd4">
            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Personal Info</h2>
        </div>
        <div>
            <md-content layout-padding>
                <div layout="column">
                    <md-input-container class="md-block">
                        <label>First Name</label>
                        <input ng-model="personDetailsVm.person.firstName" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Last Name</label>
                        <input ng-model="personDetailsVm.person.lastName" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Phone Number</label>
                        <input ng-model="personDetailsVm.person.phoneMobile" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Designation</label>
                        <input ng-model="personDetailsVm.otherInfo.designation" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Division</label>
                        <input ng-model="personDetailsVm.otherInfo.devision" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Department</label>
                        <md-select ng-model="personDetailsVm.otherInfo.department" ng-disabled="personDetailsVm.disableFields">
                            <md-option ng-repeat="department in personDetailsVm.departments" ng-value="department">{{department.name}}</md-option>
                        </md-select>
                    </md-input-container>
                </div>
            </md-content>
        </div>
    </div>

    <div class="md-whiteframe-2dp" style="margin-top: 20px;" layout="column">
        <div style="line-height: 20px;background-color: #00bcd4">
            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Emergency Contact</h2>
        </div>
        <div>
            <md-content layout-padding>
                <div layout="column">
                    <md-input-container class="md-block">
                        <label>First Name</label>
                        <input ng-model="personDetailsVm.emergencyContact.firstName" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Last Name</label>
                        <input ng-model="personDetailsVm.emergencyContact.lastName" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Phone Number</label>
                        <input  ng-model="personDetailsVm.emergencyContact.phoneMobile" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Relationship</label>
                        <input  ng-model="personDetailsVm.emergencyContact.relation" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>
                </div>
            </md-content>
        </div>
    </div>

    <div class="md-whiteframe-2dp" style="margin-top: 20px;" layout="column">
        <div style="line-height: 20px;background-color: #00bcd4">
            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Other Info</h2>
        </div>
        <div>
            <md-content layout-padding>
                <div layout="column">
                    <md-input-container class="md-block">
                        <label>Parent Unit</label>
                        <input ng-model="personDetailsVm.otherInfo.parentUnit" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Controlling Officer</label>
                        <input ng-model="personDetailsVm.otherInfo.controllingOfficer" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Controlling Officer Phone Number</label>
                        <input ng-model="personDetailsVm.otherInfo.controllingOfficerContact" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Blood Group</label>
                        <md-select ng-model="personDetailsVm.otherInfo.bloodGroup" ng-disabled="personDetailsVm.disableFields">
                            <md-option value="NA">NA</md-option>
                            <md-option value="OP">O+</md-option>
                            <md-option value="ON">O-</md-option>
                            <md-option value="AP">A+</md-option>
                            <md-option value="AN">A-</md-option>
                            <md-option value="BP">B+</md-option>
                            <md-option value="BN">B-</md-option>
                            <md-option value="ABP">AB+</md-option>
                            <md-option value="ABN">AB-</md-option>
                        </md-select>
                    </md-input-container>
                    <br>
                    <md-input-container class="md-block">
                        <label>Medical Problems</label>
                        <textarea ng-model="personDetailsVm.otherInfo.medicalProblems"
                                  md-maxlength="150" rows="5"
                                  ng-disabled="personDetailsVm.disableFields"
                                  md-select-on-focus></textarea>
                    </md-input-container>
                </div>
            </md-content>
        </div>
    </div>

    <div class="md-whiteframe-2dp" style="margin-top: 20px;" layout="column">
        <div style="line-height: 20px;background-color: #00bcd4">
            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Accommodation</h2>
        </div>
        <div>
            <md-content layout-padding>
                <div layout="column">
                    <md-input-container class="md-block">
                        <label>Accommodation</label>
                        <input ng-model="personDetailsVm.assignedAccommodation.accommodation.name" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Suite</label>
                        <input ng-model="personDetailsVm.assignedAccommodation.suite.name" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>

                    <md-input-container class="md-block">
                        <label>Bed</label>
                        <input  ng-model="personDetailsVm.assignedAccommodation.bed.name" ng-disabled="personDetailsVm.disableFields">
                    </md-input-container>
                </div>
            </md-content>
        </div>
    </div>
</div>