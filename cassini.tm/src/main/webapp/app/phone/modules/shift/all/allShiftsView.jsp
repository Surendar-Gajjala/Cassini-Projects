<div class="view-container md-whiteframe-z5" style="padding: 10px;">
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
        .person {
            background-color:#FFFFFF;
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        .person:last-child {
            border-bottom: 0;
        }
        .person-label {
            color: dodgerblue;
        }
        .margin-bottom {
            margin-bottom: 10px;
        }
    </style>

    <div class="md-whiteframe-2dp" layout="column">
        <div style="line-height: 20px;background-color: #F57F17">
            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Morning Shift</h2>
        </div>
        <div>
            <md-content layout-padding>
                <div layout="column">
                    <div class="person" ng-repeat="person in ['1', '2', '3', '4', '5']">
                        <div flex class="person-label margin-bottom">
                            Raghuram Tera
                        </div>
                        <div flex class="margin-bottom">
                            <div layout="row">
                                <div flex>
                                    <div style="font-size: 12px; color: gray">
                                        Phone Number
                                    </div>
                                    <div style="font-size: 15px;">+91 9701030235</div>
                                </div>
                                <div flex class="text-right">
                                    <div style="font-size: 12px; color: gray">
                                        Department
                                    </div>
                                    <div style="font-size: 15px;">S&T</div>
                                </div>
                            </div>
                        </div>
                        <div layout="row">
                            <div flex class="text-left">
                                <div style="font-size: 12px; color: gray">
                                    Accommodation
                                </div>
                                <div style="font-size: 15px;">Building 2, Suite 1, Bed 101</div>
                            </div>
                            <div flex class="text-right">
                                <md-button md-no-link class="md-primary" ng-click="allPersonsVm.showPersonDetails()">Details</md-button>
                            </div>
                        </div>
                    </div>
                </div>
            </md-content>
        </div>
    </div>
</div>