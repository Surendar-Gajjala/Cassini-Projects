<md-dialog aria-label="Assign Bed">
    <style scoped>

    </style>
    <form>
        <div style="line-height: 20px;background-color: #607D8B">
            <div layout="row">
                <div flex style="padding-left: 10px">
                    <h2 class="md-flex" style="width: 100%;display: inline-block; font-size: 20px;text-align: center; color: #fff">Assign Bed</h2>
                </div>
                <div class="text-right" style="padding-right: 10px; padding-top: 15px;">
                    <ng-md-icon icon="close" style="fill: #fff" ng-click="onCancel()"></ng-md-icon>
                </div>
            </div>
        </div>
        <md-dialog-content style="max-width:500px;max-height:500px;">
            <div style="padding: 20px;">
                <br>
                <md-input-container class="md-block">
                    <label>Select Bed</label>
                    <md-select ng-model="selectedBed">
                        <md-option ng-repeat="bed in beds" ng-value="bed">{{bed.name}}</md-option>
                    </md-select>
                </md-input-container>
                <br>
                <md-input-container class="md-block">
                    <label>Select Person</label>
                    <md-select ng-model="selectedPerson">
                        <md-option ng-repeat="person in persons" ng-value="person">{{person.firstName}}, {{person.lastName}}</md-option>
                    </md-select>
                </md-input-container>
            </div>
        </md-dialog-content>
        <md-dialog-actions layout="row">
            <md-button ng-click="onCancel()" >
                Cancel
            </md-button>
            <md-button ng-click="onOk()">
                Assign
            </md-button>
        </md-dialog-actions>
    </form>
</md-dialog>