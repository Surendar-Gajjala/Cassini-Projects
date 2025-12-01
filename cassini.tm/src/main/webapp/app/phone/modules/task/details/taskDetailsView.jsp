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
            margin-bottom: 5px;
        }
        .margin-bottom {
            margin-bottom: 0px;
        }
        .margin-right {
            margin-right: 10px;
        }
        .buttons-row {
            background-color: #c9d5e4;
            margin-bottom: 10px;
            border: 1px solid #eee;
        }

        .history {
            border-bottom: 1px solid #ddd;
        }

        .history:last-child {
            border-bottom: 0px;
        }

        .note {
            border-bottom: 1px solid #ddd;
            padding: 10px;
        }

        .note:last-child {
            border-bottom: 0px;
        }

        .picture {
            padding: 10px;
        }

    </style>

    <div class="md-whiteframe-2dp" layout="column">
        <div style="line-height: 20px;background-color: #4E342E">
            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Details</h2>
        </div>
        <div>
            <md-content layout-padding>
                <div flex>
                    <div layout="row">
                        <div flex>
                            <div style="font-size: 12px; color: gray">
                                Name
                            </div>
                            <div style="font-size: 15px;">{{taskDetailsVm.task.name}}</div>
                        </div>
                    </div>
                </div>
                <div flex>
                    <div style="font-size: 12px; color: gray">
                        Description
                    </div>
                    <div style="font-size: 15px;">{{taskDetailsVm.task.description}}</div>
                </div>
                <div flex>
                    <div layout="row">
                        <div flex>
                            <div style="font-size: 12px; color: gray">
                                Assigned Date
                            </div>
                            <div style="font-size: 15px;">{{taskDetailsVm.task.assignedDate}}</div>
                        </div>
                        <div flex style="text-align: right">
                            <div style="font-size: 12px; color: gray; margin-right: 35px;">
                                Status
                            </div>
                            <div style="font-size: 15px;">
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
                    </div>
                </div>
                <div flex>
                    <div layout="row">
                        <div flex>
                            <div style="font-size: 12px; color: gray">
                                Assigned To
                            </div>
                            <div style="font-size: 15px;">{{taskDetailsVm.task.assignedToObject.firstName}}</div>
                        </div>
                        <div flex class="text-center">
                            <div style="font-size: 12px; color: gray">
                                Verified By
                            </div>
                            <div style="font-size: 15px;">{{taskDetailsVm.task.verifiedByObject.firstName}}</div>
                        </div>
                        <div flex class="text-right">
                            <div style="font-size: 12px; color: gray">
                                Approved By
                            </div>
                            <div style="font-size: 15px;">{{taskDetailsVm.task.approvedByObject.firstName}}</div>
                        </div>
                    </div>
                </div>
            </md-content>
        </div>
    </div>

    <div class="md-whiteframe-2dp" layout="column" style="margin-top: 20px;">
        <div style="line-height: 20px;background-color: #4E342E">
            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">History</h2>
        </div>
        <div>
            <md-content layout-padding>
                <div flex ng-repeat="history in taskDetailsVm.history" class="history">
                    <div layout="column">
                        <div layout="row">
                            <div flex>
                                <div style="font-size: 12px; color: gray">
                                    Old Status
                                </div>
                                <div style="font-size: 15px;">{{history.oldStatus}}</div>
                            </div>
                            <div flex style="text-align: right">
                                <div style="font-size: 12px; color: gray">
                                    New Status
                                </div>
                                <div style="font-size: 15px;">{{history.newStatus}}</div>
                            </div>
                        </div>
                        <br>
                        <div layout="row">
                            <div flex>
                                <div style="font-size: 12px; color: gray">
                                    Updated By
                                </div>
                                <div style="font-size: 15px;">{{history.updatedByObject.firstName}}</div>
                            </div>
                            <div flex style="text-align: right">
                                <div style="font-size: 12px; color: gray">
                                    Timestamp
                                </div>
                                <div style="font-size: 15px;">{{history.timeStamp}}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </md-content>
        </div>
    </div>

    <div class="md-whiteframe-2dp" layout="column" style="margin-top: 20px;">
        <div style="line-height: 20px;background-color: #4E342E">
            <div layout="row">
                <div flex style="padding-left: 10px">
                    <h2 class="md-flex" style="width: 100%;display: inline-block; font-size: 20px;text-align: center; color: #fff">Notes</h2>
                </div>
                <div class="text-right" style="padding-right: 10px; padding-top: 15px;">
                    <ng-md-icon icon="add" style="fill: #fff" ng-click="taskDetailsVm.showNoteDialog()"></ng-md-icon>
                </div>
            </div>
        </div>
        <div>
            <md-content layout-padding>
                <div>
                    <div ng-if="taskDetailsVm.notes.length == 0">
                        No notes
                    </div>
                    <div flex ng-repeat="note in taskDetailsVm.notes" class="note">
                        <div layout="column">
                            <div layout="row">
                                <div>
                                    <div style="font-size: 12px; color: gray">
                                        Added By
                                    </div>
                                    <div style="font-size: 15px;">{{note.createdByObject.firstName}}</div>
                                </div>
                                <div flex style="text-align: right">
                                    <div style="font-size: 12px; color: gray">
                                        Timestamp
                                    </div>
                                    <div style="font-size: 15px;">{{note.createdDate}}</div>
                                </div>
                            </div>
                            <br>
                            <div>
                                <div style="font-size: 12px; color: gray">
                                    Note
                                </div>
                                <div>
                                    {{note.details}}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </md-content>
        </div>
    </div>

    <div class="md-whiteframe-2dp" layout="column" style="margin-top: 20px;">
        <div style="line-height: 20px;background-color: #4E342E">
            <div layout="row">
                <div flex style="padding-left: 10px">
                    <h2 class="md-flex" style="width: 100%;display: inline-block; font-size: 20px;text-align: center; color: #fff">Pictures</h2>
                </div>
                <div class="text-right" style="padding-right: 10px; padding-top: 15px;">
                    <ng-md-icon icon="add" style="fill: #fff" ng-click="taskDetailsVm.takePicture()"></ng-md-icon>
                </div>
            </div>
        </div>
        <div>
            <md-content layout-padding>
                <div>
                    <div ng-if="taskDetailsVm.task.pictures.length == 0">
                        No pictures
                    </div>
                    <div flex ng-repeat="picture in taskDetailsVm.task.pictures" class="picture">
                        <img height="250px" width="100%" ng-src="{{picture.imageData}}"/>
                    </div>
                </div>
            </md-content>
        </div>
    </div>
</div>