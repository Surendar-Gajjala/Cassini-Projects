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

        <h4>{{requirementRevisionHistoryVm.requirementRevisionHisotry[0].name}}</h4>
        <ul class="timeline">
            <li ng-repeat="version in requirementRevisionHistoryVm.requirementRevisionHisotry">
                <div class="direction-r">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag" style="" title="{{requirementRevisionHistoryVm.showVersionDetails}}"
                                  ng-click="requirementRevisionHistoryVm.showRequirementVersionDetails(version)">
                                Version <span ng-if="version.version > 0">{{version.version}}</span>
                                        <span ng-if="version.version == 0">-</span>
                            </span>

                    <span class="time-wrapper">
                        <button class="btn btn-xs btn-primary"
                                title="{{requirementRevisionHistoryVm.showEditHistoryTitle}}"
                                ng-click="requirementRevisionHistoryVm.showEditHistory(version)">
                            <i class="fa fa-history"></i>
                        </button>
                    <span class="time">
                        {{version.createdDate}} ( <span style="font-style: italic;">{{version.createdByObject.fullName}}</span> )
                    </span>
                </span>
                        </div>
                        <div class="desc" style="font-size: 14px;width: 450px;">
                            <span translate>DESCRIPTION</span> : <br> <span class=""
                                                                            ng-bind-html="version.description"></span>
                        </div>
                    </div>
                </div>
            </li>

        </ul>
    </div>
</div>
