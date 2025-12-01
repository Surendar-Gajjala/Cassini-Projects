<div style="position: relative;">
    <style scoped>
        .log-timeline-container .no-timeline-container {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
        }

        .log-timeline-container .no-timeline-container .no-timeline {
            text-align: center;
            position: absolute;
            width: 100%;
            margin-top: 200px;
        }

        .log-timeline-container .no-timeline-container .no-timeline img {
            width: 400px;
        }

        .log-timeline-container .no-timeline-container .no-timeline .no-timeline-message {
            font-size: 20px;
            font-weight: 300 !important;
        }

    </style>
    <div class="log-timeline-container" style="">
        <style scoped>
            .popover {
                width: 250px !important;
                max-width: 250px !important;
                min-width: 250px !important;
                z-index: 9999;
            }

            .popover-content {
                padding: 5px;
            }
        </style>
        <div>
            <form class="log-filter-panel form-horizontal" style="border-left: 0 !important;background-color: #f9fbfe">
                <div class="form-group" style="width: 50%;margin: 0;padding: 0 10px;">
                    <label class="control-label" style="text-align: right;">
                        <span translate>DATE</span> </label>
                    <br>

                    <div class="">
                        <input type="text" class="form-control input-sm" ng-model="selectedDate.date"
                               placeholder="{{'SELECT_DATE' | translate}}" style="margin-top: 0;" inward-date-picker/>
                        <i class="fa fa-times" ng-if="activityStreamFilter.date != null"
                           style="margin-top: -25px;margin-right: 10px;float: right;cursor: pointer;"
                           ng-click="selectedDate.date = null;logHistoryVm.loadAllHistoryLogs()"></i>
                    </div>
                </div>
                <div class="form-group" style="width: 50%;margin: 0;padding: 0 10px;">
                    <label class="control-label">
                        <span translate>USER</span> </label>

                    <div class="">
                        <ui-select ng-model="selectedUser" theme="bootstrap"
                                   on-select="logHistoryVm.selectUser($item)"
                                   on-remove="logHistoryVm.selectUser()">
                            <ui-select-match allow-clear="true" placeholder="{{'SELECT_USER' | translate}}">
                                {{$select.selected.fullName}}
                            </ui-select-match>
                            <ui-select-choices repeat="person in logHistoryVm.persons">
                                <div ng-bind-html="person.fullName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group" style="width: 50%;margin: 0;padding: 0 10px;">
                    <label class="control-label">
                        <span translate>OBJECT_TYPE</span> </label>

                    <div class="">
                        <ui-select ng-model="selectedType" theme="bootstrap"
                                   on-select="logHistoryVm.selectType($item)"
                                   on-remove="logHistoryVm.selectType()">
                            <ui-select-match allow-clear="true" placeholder="Select type">
                                {{$select.selected | uppercase}}
                            </ui-select-match>
                            <ui-select-choices repeat="objectType in logHistoryVm.objectTypes | uppercase | orderBy:objectTypes | filter: $select.search">
                                <div title="{{objectType.length > 15 ? objectType : '' }}" ng-bind="objectType | uppercase"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
        <div class="log-timeline-panel" style="">
            <div class="no-timeline-container"
                 ng-if="logHistoryVm.historyCount == 0">
                <div class="no-timeline">
                    <img src="app/assets/images/time.jpg" alt="">

                    <div class="no-timeline-message">No activity stream records.</div>
                </div>
            </div>
            <ul class="log-timeline" ng-if="logHistoryVm.historyCount > 0">
                <div id="activityStreamContainer" ng-repeat="(key,histories) in logHistoryVm.activityStreams">
                    <li style="padding-bottom: 20px;">
                        <div class="log-direction-l">
                            <hr style="margin-bottom: 0;margin-top: 13px;border-top: 1px solid lightgrey;width: calc(100% - 20px);float: left;">
                        </div>
                        <div class="log-direction-r">
                            <div class="log-flag-wrapper" style="text-align: left !important;">
                                <span class="log-date-flag" style=""></span>
                        <span class="log-time-wrapper">
                            <span class="time" style="font-size: 16px;font-weight: bold;">
                             <span ng-if="key == logHistoryVm.todayDate">TODAY</span>
                             <span ng-if="key != logHistoryVm.todayDate">{{key}}</span>
                            </span>
                        </span>
                            </div>
                            <hr style="margin-bottom: 0;margin-top: 13px;border-top: 1px solid lightgrey;width: calc(100% - 100px);float: right;">
                        </div>
                    </li>
                    <li ng-repeat="history in histories" class="log-type-{{history.type.toLowerCase()}}">
                        <div class="log-direction-l">
                            <div class="log-flag-wrapper" style="text-align: left !important;">
                                <span class="log-time-wrapper">
                                    <span class="time" style="font-size: 14px;">
                                         {{history.time}}
                                    </span>
                                </span>
                            </div>
                        </div>
                        <div class="log-direction-r">
                            <div class="log-flag-wrapper" style="text-align: left !important;">
                                <span class="log-flag" style=""></span>

                                <div class="log-time-wrapper">
                                    <div class="time" style="font-size: 14px;">
                                        <p style="color: #2a6fa8;font-size: 15px;">
                                            <span>{{history.actor.fullName}}</span>
                                            <a class="icon fa fa-info-circle"
                                               style="padding-left: 5px; overflow-y: hidden !important;vertical-align: middle;"
                                               title="Session Info"
                                               ng-if="history.sessionObject != null && history.sessionObject != '' && hasPermission('admin','all')"
                                               uib-popover-template="sessionPopover.templateUrl"
                                               popover-append-to-body="true"
                                               popover-popup-delay="50"
                                               popover-placement="right"
                                               popover-trigger="'outsideClick'">
                                            </a>
                                        </p>

                                        <span style="line-height: 20px;" ng-bind-html="history.summary"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </div>
                <li id="loadMore" ng-if="logHistoryVm.lastPage == false" style="padding: 25px 0" >
                    <div class="log-direction-r">
                        <div class="log-flag-wrapper" style="text-align: left !important;">
                             <a href="" ng-click="logHistoryVm.loadMoreHistory()" style="">&#x2193; Load More</a>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>