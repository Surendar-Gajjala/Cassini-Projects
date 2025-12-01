<div style="padding: 10px">
    <style scoped>
        .timeline {
            width: 80px;
        }

        .timeline::before {
            left: -250px !important;
        }

        .timeline::before {
            left: -80px;
        }

        .timeline .flag {
            cursor: pointer;
        }

        .timeline .time-wrapper {
            margin-top: -24px;
            margin-left: 85px;
            width: 375px;
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
    </style>

    <div>

        <h4>{{entryVersionEditHistoryVm.entryName}} ( {{entryVersionEditHistoryVm.selectedLanguage}} )</h4>

        <h6 ng-if="entryVersionEditHistoryVm.entryEditHistories.length == 0">No Edits</h6>
        <ul class="timeline" ng-if="entryVersionEditHistoryVm.entryEditHistories.length > 0">
            <li ng-repeat="entryEdit in entryVersionEditHistoryVm.entryEditHistories">
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
                                </span>
                            </span>
                    </div>
                    <div class="desc" style="font-size: 14px;width: 575px;">
                        <span style="font-weight: 600;" translate>DESCRIPTION</span> : <br> <span
                            class="">{{entryEdit.editedDescription}}</span><br>
                        <span style="font-weight: 600;" translate>NOTES</span> : <br> <span
                            class="">{{entryEdit.editedNotes}}</span>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>