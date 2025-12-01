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

        <h4>{{entryVersionHistoryVm.entryName}} ( {{entryVersionHistoryVm.selectedLanguage}} )</h4>

        <div ng-if='entryVersionHistoryVm.selectedMode == "VERSION"'>
            <p class="text-muted" style="font-style: italic">( {{entryVersionHistoryVm.clickOnVersionMsg}} )</p>

            <ul class="timeline">
                <li ng-repeat="version in entryVersionHistoryVm.entryVersions">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag" style="" ng-click="entryVersionHistoryVm.showVersionDetails(version)">
                                Version <span ng-if="version.version > 0">{{version.version}}</span>
                                        <span ng-if="version.version == 0">-</span>
                            </span>

                    <span class="time-wrapper">
                        <button class="btn btn-xs btn-primary" title="{{entryVersionHistoryVm.showEditHistoryTitle}}"
                                ng-click="entryVersionHistoryVm.showEditHistory(version)">
                            <i class="fa fa-history"></i>
                        </button>
                    <span class="time">
                        {{version.createdDate}} ( <span style="font-style: italic;">{{version.createdByObject.firstName}}</span> )
                    </span>
                </span>
                        </div>
                        <div class="desc" style="font-size: 14px;width: 450px;">
                            Comments : <br> <span class="">{{version.defaultDetail.notes}}</span>
                        </div>
                    </div>
                </li>
            </ul>
        </div>

        <div ng-if='entryVersionHistoryVm.selectedMode == "EDIT_HISTORY"'>

            <%--<button ng-if="entryVersionHistoryVm.showAcceptButton == true && entryVersionHistoryVm.atleastOneAccepted == true"
                    class="btn btn-xs btn-success"
                    ng-click="entryVersionHistoryVm.acceptEntry()">Accept as Final
            </button>--%>
            <h6 ng-if="entryVersionHistoryVm.entryEditHistories.length == 0">No Edits</h6>
            <ul class="timeline" ng-if="entryVersionHistoryVm.entryEditHistories.length > 0">
                <li ng-repeat="entryEdit in entryVersionHistoryVm.entryEditHistories">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag"
                                  ng-class="{'noneColor':entryEdit.status == 'NONE',
                                     'acceptedColor':entryEdit.status == 'ACCEPTED',
                                     'rejectedColor':entryEdit.status == 'REJECTED'}"
                                  style="">{{entryEdit.status}}</span>
                            <span class="time-wrapper">
                                <span class="time">
                                        <span ng-if="entryEdit.status == 'NONE'">{{entryEdit.updatedDate}} ( <span
                                                style="font-style: italic;">{{entryEdit.person.fullName}}</span> )</span>
                                        <span ng-if="entryEdit.status != 'NONE'">{{entryEdit.acceptedDate}} ( <span
                                                style="font-style: italic;">{{entryEdit.person.fullName}}</span> )</span>

                                    <span ng-if="entryEdit.status == 'NONE'">
                                        <i ng-click="entryVersionHistoryVm.acceptEntryEdit(entryEdit)"
                                           class="fa fa-check-circle"
                                           ng-if="hasPermission('admin','all') || selectGlossaryPermission.acceptRejectPermission == true"
                                           style="cursor: pointer;font-size: 16px;color: blue;"
                                           title="{{entryVersionHistoryVm.clickToAccept}}"></i>
                                        <i ng-click="entryVersionHistoryVm.rejectEntryEdit(entryEdit)"
                                           ng-if="hasPermission('admin','all') || selectGlossaryPermission.acceptRejectPermission == true"
                                           class="fa fa-times-circle"
                                           style="cursor: pointer;font-size: 16px;color: darkred"
                                           title="{{entryVersionHistoryVm.clickToReject}}"></i>
                                    </span>
                                    <span ng-if="entryEdit.lastAcceptedEdit == true">
                                        <i class="fa fa-check-square-o" title="{{entryVersionHistoryVm.acceptAsFinal}}"
                                           ng-if="hasPermission('admin','all') || selectGlossaryPermission.acceptRejectPermission == true"
                                           style="cursor: pointer;font-size: 16px;color: green;"
                                           ng-click="entryVersionHistoryVm.acceptEntry()"></i>
                                    </span>
                                </span>
                            </span>
                        </div>
                        <div class="desc" style="font-size: 14px;width: 450px;">
                            <span style="font-weight: 600;" translate>DESCRIPTION</span> : <br>
                            <%-- <span class="" id="entryEdit{{entryEdit.id}}">
                                 {{entryVersionHistoryVm.getEditedDescriptionDiff(entryEdit)}}
                                 <span ng-bind-html="entryEdit.editDiff"></span>
                             </span>--%>
                            <span>
                                {{entryEdit.editedDescription}}
                            </span>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>