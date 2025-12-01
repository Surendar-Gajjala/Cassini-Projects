<md-dialog aria-label="Assigned Beds">
    <style scoped>
        .bed {
            border-bottom: 1px solid #ddd;
            padding: 10px;
        }s
    </style>
    <form>
        <div style="line-height: 20px;background-color: #607D8B">
            <div layout="row">
                <div flex style="padding-left: 10px">
                    <h2 class="md-flex" style="width: 100%;display: inline-block; font-size: 20px;text-align: center; color: #fff">Assigned Beds</h2>
                </div>
                <div class="text-right" style="padding-right: 10px; padding-top: 15px;">
                    <ng-md-icon icon="close" style="fill: #fff" ng-click="onCancel()"></ng-md-icon>
                </div>
            </div>
        </div>
        <md-dialog-content style="max-width:500px;max-height:500px; min-height: 100px;">
            <div style="padding: 0px;">
                <div ng-if="loading ==  true">
                    <md-progress-linear md-mode="indeterminate"></md-progress-linear>
                </div>
                <div layout="column" class="bed" ng-repeat="bed in assignedBeds">
                    <div layout="row">
                        <div>
                            <div style="font-size: 12px; color: gray">
                                Bed
                            </div>
                            <div style="font-size: 15px;">{{bed.name}}</div>
                        </div>
                        <span flex></span>
                        <div class="text-right">
                            <div style="font-size: 12px; color: gray">
                                Person
                            </div>
                            <div style="font-size: 15px;">{{bed.assignedToObject.firstName}}, {{bed.assignedToObject.lastName}}</div>
                        </div>
                    </div>
                </div>
            </div>
        </md-dialog-content>
        <md-dialog-actions layout="row">
            <md-button class="md-primary" ng-click="onOk()">OK</md-button>
        </md-dialog-actions>
    </form>
</md-dialog>