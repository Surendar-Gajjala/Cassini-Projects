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

        .search-box {
            padding-left: 15px;
            background-color: #4E342E;
            border-bottom: 1px solid #ddd;
            border-radius: 2px;
        }
        .search-box .md-block {
            margin: 0;
        }

        .search-box .md-input {
            border-color: #fff !important;
        }
        .search-box input {
            color: #fff !important;
        }

        .search-box button {
            margin-left: -10px !important;
        }
    </style>

    <div style="padding: 10px" class="home-view">
        <div layout="column">
            <div layout="row" class="buttons-row">
                <div flex class="text-left">
                    <md-button md-no-link class="md-primary" ng-click="$parent.goToState('app.task.new');">New Task</md-button>
                </div>
            </div>
            <div class="search-box">
                <div layout="row">
                    <div flex>
                        <md-input-container class="md-block">
                            <label>&nbsp;</label>
                            <input ng-enter="allTasksVm.loadTasks()" ng-model="allTasksVm.filters.searchQuery">
                        </md-input-container>
                    </div>
                    <div>
                        <md-button class="md-icon-button" ng-click="allTasksVm.loadTasks()">
                            <ng-md-icon icon="search" style="fill: #fff;"></ng-md-icon>
                        </md-button>
                    </div>
                </div>
            </div>

            <br>

            <div class="row task-list">
                <div ng-if="allTasksVm.loading ==  true">
                    <md-progress-linear md-mode="indeterminate"></md-progress-linear>
                </div>
                <div class="md-whiteframe-3dp task" layout="column" ng-if="allTasksVm.loading ==  false && allTasksVm.tasks.length == 0">
                    <div style="text-align: center; padding: 10px;">
                        No tasks
                    </div>
                </div>

                <div class="md-whiteframe-3dp task" layout="column" ng-repeat="task in allTasksVm.tasks"
                     ng-click="allTasksVm.showTaskDetails(task)">
                    <div flex>
                        <div layout="row">
                            <div flex class="task-label margin-bottom">
                                {{task.name}}
                            </div>
                            <div flex style="text-align: right">
                                <div style="font-size: 15px;">
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
                </div>
            </div>
        </div>
    </div>
</div>