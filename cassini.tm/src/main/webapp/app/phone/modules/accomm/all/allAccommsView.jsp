<div class="view-container md-whiteframe-z5">
    <style scoped>
        .md-block {
            margin: 0 !important;
        }
        .text-right {
            text-align: right;
        }
        .text-left {
            text-align: left;
        }
        .text-center {
            text-align: center;
        }

        .suite {
            background-color:#FFFFFF;
            padding: 5px;
            border-bottom: 1px solid #ddd;
        }
        .suite:last-child {
            border-bottom: 0;
        }
        .suite-label {
            color: dodgerblue;
        }
    </style>
    <div style="padding: 10px;">
        <div class="md-whiteframe-2dp" layout="column">
            <div style="padding: 10px; background-color: #fff">
                <div layout="row">
                    <div flex class="text-left">
                        <div style="font-size: 12px; color: gray">
                            Accommodations
                        </div>
                        <div style="font-size: 24px;">{{allAccommsVm.counts.accommodations}}</div>
                    </div>
                    <div flex class="text-center">
                        <div style="font-size: 12px; color: gray">
                            Rooms
                        </div>
                        <div style="font-size: 24px;">{{allAccommsVm.counts.suites}}</div>
                    </div>
                    <div flex class="text-right">
                        <div style="font-size: 12px; color: gray">
                            Beds
                        </div>
                        <div style="font-size: 24px;">{{allAccommsVm.counts.beds}}</div>
                    </div>
                </div>
                <br>
                <div layout="row">
                    <div flex class="text-left">
                        <div style="font-size: 12px; color: gray">
                            Occupied Beds
                        </div>
                        <div style="font-size: 24px;">{{allAccommsVm.counts.occupiedBeds}}</div>
                    </div>
                    <div flex class="text-right">
                        <div style="font-size: 12px; color: gray">
                            Available Beds
                        </div>
                        <div style="font-size: 24px;">{{allAccommsVm.counts.availableBeds}}</div>
                    </div>
                </div>
            </div>
        </div>
        <br>
        <div class="md-whiteframe-2dp" layout="column" ng-repeat="accomm in allAccommsVm.accommodations" style="margin-bottom: 10px;">
            <div style="line-height: 20px;background-color: #607D8B" ng-click="allAccommsVm.toggleAccommodation(accomm)">
                <div layout="row">
                    <div flex style="padding-left: 10px">
                        <h2 class="md-flex" style="display: inline-block; font-size: 20px;text-align: center; color: #fff">{{accomm.name}}</h2>
                    </div>
                    <div class="text-right" style="padding-right: 10px; padding-top: 15px;">
                        <ng-md-icon icon="expand_more" ng-if="accomm.showSuites == false" style="fill: #fff"></ng-md-icon>
                        <ng-md-icon icon="expand_less" ng-if="accomm.showSuites == true" style="fill: #fff"></ng-md-icon>
                    </div>
                </div>
            </div>
            <div>
                <md-content layout-padding class="suites" ng-show="accomm.showSuites">
                    <div ng-if="accomm.loading ==  true">
                        <md-progress-linear md-mode="indeterminate"></md-progress-linear>
                    </div>
                    <div layout="column" ng-if="accomm.loading == false && accomm.suites.length == 0">
                        <div flex style="padding: 10px;">
                            No suites
                        </div>
                    </div>
                    <div class="suite" ng-repeat="suite in accomm.suites">
                        <div layout="column">
                            <div style="margin-bottom: 5px;font-size: 20px;color:#504f4f;">
                                {{suite.name}}
                            </div>
                            <div layout="row">
                                <div flex
                                     ng-click="allAccommsVm.showBedAssignments(suite, suite.occupied)">
                                    <div style="font-size: 12px; color: gray">
                                        Occupied Beds
                                    </div>
                                    <div class="suite-label" style="font-size: 20px;">{{suite.occupied}}</div>
                                </div>
                                <div flex class="text-right"
                                     ng-click="allAccommsVm.assignBed(suite, suite.available)">
                                    <div style="font-size: 12px; color: gray">
                                        Available Beds
                                    </div>
                                    <div class="suite-label" style="font-size: 20px;">{{suite.available}}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </md-content>
            </div>
        </div>
    </div>
</div>