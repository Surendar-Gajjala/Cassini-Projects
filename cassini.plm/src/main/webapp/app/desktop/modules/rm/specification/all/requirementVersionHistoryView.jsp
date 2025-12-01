<div style="padding: 10px">
    <style scoped>
        .timeline {
            width: 145px;
        }

        .timeline::before {
            left: -185px !important;
        }

        .timeline .flag {
            cursor: pointer;
        }

        .timeline .time-wrapper {
            margin-top: -24px;
            margin-left: 85px;
            width: 400px;
        }

        .timeline .time {
            font-size: 12px;
        }

        .notes {
            word-wrap: break-word;
            width: 350px;
            white-space: normal !important;
            text-align: left;
        }

        .noneColor {

            background: #F3E8E8;
        }

        .noneColor::after {
            border: solid transparent;
            border-width: 6px !important;
            border-right-color: #F3E8E8 !important;
        }

        .acceptedColor {
            color: white;
            background: #28a745;

        }

        .acceptedColor::after {
            border: solid transparent;
            border-width: 6px !important;
            border-right-color: #28a745 !important;
        }

        .finalColor {
            color: white;
            background: #003eff;

        }

        .finalColor::after {
            border: solid transparent;
            border-width: 6px !important;
            border-right-color: #003eff !important;
        }

        .rejectedColor {
            color: white;
            background: #dc3545;

        }

        .rejectedColor::after {
            border: solid transparent;
            border-width: 6px !important;
            border-right-color: #dc3545 !important;
        }

        .text-edited {
            padding: 3px 3px;
            margin: 3px 3px;
            color: white;
            border-radius: 3px;
        }

        .text-added {
            background-color: green;
        }

        .text-removed {
            background-color: red;
        }

    </style>

    <div>

        <h4>{{requirementEditHistoryVm.entryName}}</h4>
        <h6 ng-if="requirementEditHistoryVm.entryEditHistories.length == 0">{{requirementEditHistoryVm.noEdits}}</h6>
        <ul class="timeline" ng-if="requirementEditHistoryVm.entryEditHistories.length > 0">
            <li ng-repeat="entryEdit in requirementEditHistoryVm.entryEditHistories">
                <div class="direction-r">
                    <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag"
                                  ng-class="{'noneColor':entryEdit.status == 'NEW',
                                     'acceptedColor':entryEdit.status == 'ACCEPTED',
                                     'finalColor':entryEdit.status == 'FINAL',
                                     'rejectedColor':entryEdit.status == 'REJECTED'}"
                                  style="">{{entryEdit.status}}</span>
                            <span class="time-wrapper">
                                <span class="time">
                                        <span ng-if="entryEdit.status == 'NEW'" ng-hide="entryEdit.status != 'NEW'">{{entryEdit.updatedDate}} ( <span
                                                style="font-style: italic;">{{entryEdit.person.fullName}}</span> )</span>
                                        <span ng-if="entryEdit.status != 'NEW'" ng-hide="entryEdit.status == 'NEW'">{{entryEdit.acceptedDate}} ( <span
                                                style="font-style: italic;">{{entryEdit.person.fullName}}</span> )</span>

                                    <span ng-if="entryEdit.status == 'NEW'">
                                        <i ng-click="requirementEditHistoryVm.acceptEntryEdit(entryEdit)"
                                           class="fa fa-check-circle"
                                           ng-if="hasPermission('admin','all') || specPermission.acceptRejectPermission == true"
                                           style="cursor: pointer;font-size: 16px;color: blue;"
                                           title="{{requirementEditHistoryVm.clickToAccept}}"></i>
                                        <i ng-click="requirementEditHistoryVm.rejectEntryEdit(entryEdit)"
                                           class="fa fa-times-circle"
                                           ng-if="hasPermission('admin','all') || specPermission.acceptRejectPermission == true"
                                           style="cursor: pointer;font-size: 16px;color: darkred"
                                           title="{{requirementEditHistoryVm.clickToReject}}"></i>
                                    </span>
                                    <span ng-if="entryEdit.lastAcceptedEdit == true">
                                        <i class="fa fa-check-square-o"
                                           ng-if="hasPermission('admin','all') || specPermission.acceptRejectPermission == true"
                                           title="{{requirementEditHistoryVm.acceptAsFinal}}"
                                           style="cursor: pointer;font-size: 16px;color: green;"
                                           ng-click="requirementEditHistoryVm.acceptEntry(entryEdit)"></i>
                                    </span>
                                </span>
                            </span>
                    </div>
                    <div class="desc" style="font-size: 14px;width: 450px;">
                        <span style="font-weight: 600;" translate>DESCRIPTION</span> : <br>
                            <span class="" id="entryEdit{{entryEdit.id}}">
                                <span ng-bind-html="entryEdit.editedDescription"></span>
                            </span>
                    </div>
                    <div class="desc" style="font-size: 14px;width: 450px;">
                        <span style="font-weight: 600;" translate>NOTES</span> : <br>
                            <span class="">
                                {{entryEdit.editNotes}}
                            </span>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>