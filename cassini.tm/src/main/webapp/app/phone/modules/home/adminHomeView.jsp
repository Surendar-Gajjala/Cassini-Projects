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
        .task-location {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
    </style>
    <div style="padding: 10px;">
        <div class="task-locations">
            <div class="md-whiteframe-2dp" layout="column" style="">
                <div style="padding: 10px; background-color: #fff; color:red !important;">
                    <div layout="row">
                        <div flex class="text-left">
                            <div style="font-size: 12px; color: gray">
                                Completed Tasks
                            </div>
                            <div style="padding-left: 40px;font-size: 24px; color: green">{{adminHomeVm.totalCounts.completed}}</div>
                        </div>
                        <div flex class="text-center">
                            <div style="font-size: 12px; color: gray">
                                Pending Tasks
                            </div>
                            <div style="font-size: 24px; color: red">{{adminHomeVm.totalCounts.pending}}</div>
                        </div>
                        <div flex class="text-right">
                            <div style="font-size: 12px; color: gray">
                                All Tasks
                            </div>
                            <div style="font-size: 24px; color: black">{{adminHomeVm.totalCounts.all}}</div>
                        </div>
                    </div>
                </div>
            </div>
            <br>
            <div class="md-whiteframe-2dp" layout="column" style="background-color: #fff;">
                <div class="task-location" ng-repeat="location in adminHomeVm.locations">
                    <div layout="column">
                        <div flex class="task-stats" layout="row">
                            <div flex style="margin-bottom: 5px;">
                                <div style="font-size: 12px; color: gray">
                                    Location
                                </div>
                                <div style="font-size: 15px; color: dodgerblue">{{location.name}}</div>
                            </div>
                            <div flex style="margin-bottom: 5px;" class="text-center">
                                <div style="font-size: 12px; color: gray">
                                    Completed Tasks
                                </div>
                                <div style="font-size: 15px; color: dodgerblue">{{location.counts.completed}}</div>
                            </div>
                            <div flex class="text-right">
                                <div style="font-size: 12px; color: gray">
                                    Pending Tasks
                                </div>
                                <div style="font-size: 15px; color: dodgerblue">{{location.counts.pending}}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="user-tasks">

        </div>
    </div>
</div>

