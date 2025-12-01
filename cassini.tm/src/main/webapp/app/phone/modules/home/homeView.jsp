<div class="view-container md-whiteframe-z5">
    <style scoped>
        .text-right {
            text-align: right;
        }
        .text-left {
            text-align: left;
        }
        .text-center {
            text-align: center;
        }

        .date-label {
            padding-top: 15px;
        }

        .task-list {
            overflow-y: auto;
        }
        .task {
            background-color:#FFFFFF;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 10px;
        }
        .task-container {
            background-color:#FFFFFF;
            padding: 10px;
            border-bottom: 1px solid #ddd;
            margin-bottom: 10px;
        }

        .task-container:last-child {
            border-bottom: 0px;
            margin-bottom: 0px;
        }
        .task-label {
            color: dodgerblue;
        }
        .margin-bottom {
            margin-bottom: 10px;
        }
        .margin-right {
            margin-right: 10px;
        }
        .buttons-row {
            background-color: #c9d5e4;
            margin-bottom: 10px;
            border: 1px solid #eee;
        }
    </style>

    <div ng-if="homeVm.isAdmin"
         ng-include="'app/phone/modules/home/adminHomeView.jsp'"
         ng-controller="AdminHomeController as adminHomeVm"></div>

    <div ng-if="!homeVm.isAdmin" style="padding: 10px" class="home-view">
        <div layout="column">
            <div layout="row">
                <div flex class="text-left">
                    <md-button md-no-link class="md-raised" ng-click="homeVm.showDailyTasks()">
                        <div>Daily</div>
                        <div style="margin-top: -15px;">Tasks</div>
                    </md-button>
                </div>
                <div flex class="text-center">
                    <md-button md-no-link class="md-raised md-primary" ng-click="homeVm.showCompletedTasks()">
                        <div>Completed</div>
                        <div style="margin-top: -15px;">Tasks</div>
                    </md-button>
                </div>
                <div flex class="text-right">
                    <md-button md-no-link class="md-raised md-warn" ng-click="homeVm.showPendingTasks()">
                        <div>Pending</div>
                        <div style="margin-top: -15px;">Tasks</div>
                    </md-button>
                </div>
            </div>
            <br>
            <div ng-if="homeVm.viewName == 'daily'">
                <div layout="row" class="buttons-row">
                    <div flex class="text-left">
                        <md-button md-no-link class="md-primary" ng-click="homeVm.previousDay()">Prev</md-button>
                    </div>
                    <div flex class="text-center date-label">
                        <span>{{homeVm.currentDateLabel}}</span>
                    </div>
                    <div flex class="text-right">
                        <md-button md-no-link class="md-primary" ng-click="homeVm.nextDay()">Next</md-button>
                    </div>
                </div>

                <div class="row task-list">
                    <div ng-if="homeVm.loading ==  true">
                        <md-progress-linear md-mode="indeterminate"></md-progress-linear>
                    </div>
                    <div class="md-whiteframe-3dp task" layout="column" ng-if="homeVm.loading ==  false && homeVm.tasks.length == 0">
                        <div style="text-align: center; padding: 10px;">
                            No tasks
                        </div>
                    </div>

                    <div class="md-whiteframe-3dp task" layout="column" ng-repeat="task in homeVm.tasks">
                        <div flex>
                            <div layout="row">
                                <div flex class="task-label margin-bottom">
                                    {{task.name}}
                                </div>
                                <div flex style="text-align: right">
                                    <div class="task-status"
                                         ng-class="{'completed': (task.status == 'FINISHED' || task.status == 'VERIFIED' || task.status == 'APPROVED'),
                                                'pending': task.status == 'ASSIGNED'}"></div>
                                    <div class="task-status"
                                         ng-class="{'completed': (task.status == 'VERIFIED' || task.status == 'APPROVED'),
                                                'pending': task.status == 'ASSIGNED' || task.status == 'FINISHED'}"></div>
                                    <div class="task-status"
                                         ng-class="{'completed': task.status == 'APPROVED',
                                                'pending': task.status != 'APPROVED'}"></div>
                                </div>
                            </div>
                        </div>
                        <div flex class="margin-bottom">
                            {{task.description}}
                        </div>
                        <div flex>
                            <div layout="row">
                                <div flex>
                                    <div style="font-size: 12px; color: gray">
                                        Assigned To
                                    </div>
                                    <div style="font-size: 15px;">{{task.assignedToObject.firstName}}</div>
                                </div>
                                <div flex class="text-center">
                                    <div style="font-size: 12px; color: gray">
                                        Verified By
                                    </div>
                                    <div style="font-size: 15px;">{{task.verifiedByObject.firstName}}</div>
                                </div>
                                <div flex class="text-right">
                                    <div style="font-size: 12px; color: gray">
                                        Approved By
                                    </div>
                                    <div style="font-size: 15px;">{{task.approvedByObject.firstName}}</div>
                                </div>
                            </div>
                        </div>
                        <br>
                        <div flex>
                            <div layout="row">
                                <div flex>
                                    <md-button md-no-link class="md-raised md-primary"
                                               ng-if="task.status == 'ASSIGNED' && homeVm.loginPerson == task.assignedTo"
                                               ng-click="homeVm.finishTask(task)">Finish</md-button>
                                    <md-button md-no-link class="md-raised md-primary"
                                               ng-if="task.status == 'FINISHED'  && homeVm.loginPerson == task.verifiedBy"
                                               ng-click="homeVm.verifyTask(task)">Verify</md-button>
                                    <md-button md-no-link class="md-raised md-primary"
                                               ng-if="task.status == 'VERIFIED'  && homeVm.loginPerson == task.approvedBy"
                                               ng-click="homeVm.approveTask(task)">Approve</md-button>
                                </div>
                                <!--
                                <div flex style="text-align: center;">
                                    <md-button md-no-link class="md-raised md-warn"
                                               ng-if="task.status == 'ASSIGNED' && homeVm.loginPerson == task.assignedTo"
                                               ng-click="homeVm.pendingTask(task)">Pending</md-button>
                                </div>
                                -->
                                <div flex style="text-align: right;">
                                    <md-button md-no-link class="md-raised md-primary md-hue-2"
                                               ng-click="homeVm.showTaskDetails(task)">Details</md-button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div ng-if="homeVm.viewName == 'completed'">
                <div class="md-whiteframe-2dp" layout="column">
                    <div style="line-height: 20px;background-color: #1565c0">
                        <div layout="row">
                            <div flex style="padding-left: 10px">
                                <h2 class="md-flex" style="display: inline-block; font-size: 20px;text-align: center; color: #fff">Completed Tasks</h2>
                            </div>
                        </div>
                    </div>
                    <div>
                        <md-content layout-padding class="tasks" ng-show="homeVm.completedTasks">
                            <div layout="column">
                                <div flex ng-if="homeVm.completedTasks.length == 0" style="padding: 10px;">
                                    No tasks
                                </div>
                                <div class="task-container" ng-repeat="task in homeVm.completedTasks">
                                    <div flex>
                                        <div layout="row" class="margin-bottom">
                                            <div flex style="text-align: left">
                                                <div style="font-size: 12px; color: gray">
                                                    Name
                                                </div>
                                                <div style="font-size: 15px;">{{task.name}}</div>
                                            </div>
                                            <div flex style="text-align: right">
                                                <div style="font-size: 12px; color: gray">
                                                    Assigned Date
                                                </div>
                                                <div style="font-size: 15px;">{{task.assignedDate}}</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div flex class="margin-bottom">
                                        <div style="font-size: 12px; color: gray">
                                            Details
                                        </div>
                                        <div style="font-size: 15px;">{{task.description}}</div>
                                    </div>
                                    <div flex>
                                        <div layout="row">
                                            <div flex>
                                                <div style="font-size: 12px; color: gray">
                                                    Assigned To
                                                </div>
                                                <div style="font-size: 15px;">{{task.assignedToObject.firstName}}</div>
                                            </div>
                                            <div flex class="text-center">
                                                <div style="font-size: 12px; color: gray">
                                                    Verified By
                                                </div>
                                                <div style="font-size: 15px;">{{task.verifiedByObject.firstName}}</div>
                                            </div>
                                            <div flex class="text-right">
                                                <div style="font-size: 12px; color: gray">
                                                    Approved By
                                                </div>
                                                <div style="font-size: 15px;">{{task.approvedByObject.firstName}}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </md-content>
                    </div>
                </div>
            </div>
            <div ng-if="homeVm.viewName == 'pending'">
                <div class="md-whiteframe-2dp" layout="column">
                    <div style="line-height: 20px;background-color: #1565c0">
                        <div layout="row">
                            <div flex style="padding-left: 10px">
                                <h2 class="md-flex" style="display: inline-block; font-size: 20px;text-align: center; color: #fff">Pending Tasks</h2>
                            </div>
                        </div>
                    </div>
                    <div>
                        <md-content layout-padding class="tasks" ng-show="homeVm.pendingTasks">
                            <div layout="column">
                                <div flex ng-if="homeVm.pendingTasks.length == 0" style="padding: 10px;">
                                    No tasks
                                </div>
                                <div class="task-container" ng-repeat="task in homeVm.pendingTasks">
                                    <div flex>
                                        <div layout="row" class="margin-bottom">
                                            <div flex style="text-align: left">
                                                <div style="font-size: 12px; color: gray">
                                                    Name
                                                </div>
                                                <div style="font-size: 15px;">{{task.name}}</div>
                                            </div>
                                            <div flex style="text-align: right">
                                                <div style="font-size: 12px; color: gray">
                                                    Assigned Date
                                                </div>
                                                <div style="font-size: 15px;">{{task.assignedDate}}</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div flex class="margin-bottom">
                                        <div style="font-size: 12px; color: gray">
                                            Details
                                        </div>
                                        <div style="font-size: 15px;">{{task.description}}</div>
                                    </div>
                                    <div flex>
                                        <div layout="row">
                                            <div flex>
                                                <div style="font-size: 12px; color: gray">
                                                    Assigned To
                                                </div>
                                                <div style="font-size: 15px;">{{task.assignedToObject.firstName}}</div>
                                            </div>
                                            <div flex class="text-center">
                                                <div style="font-size: 12px; color: gray">
                                                    Verified By
                                                </div>
                                                <div style="font-size: 15px;">{{task.verifiedByObject.firstName}}</div>
                                            </div>
                                            <div flex class="text-right">
                                                <div style="font-size: 12px; color: gray">
                                                    Approved By
                                                </div>
                                                <div style="font-size: 15px;">{{task.approvedByObject.firstName}}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </md-content>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>