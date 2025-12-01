<div style="position: relative;">
    <style scoped>
        .session-timeline-container .log-direction-r .session-flag::before,
        .session-timeline-container .log-direction-r .log-date-flag::before {
            cursor: pointer !important;
        }

        .session-filter-panel.form-horizontal {
            display: flex;
        }

        .session-filter-panel.form-horizontal .form-group {
            /*flex: 1;*/
        }

        .session-timeline-container .log-timeline .log-direction-r .session-info {
            padding: 5px !important;
            font-size: 14px !important;
            color: #929293 !important;
        }

        .session-timeline-container .no-timeline-container {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
        }

        .session-timeline-container .no-timeline-container .no-timeline {
            text-align: center;
            position: absolute;
            width: 100%;
            margin-top: 200px;
        }

        .session-timeline-container .no-timeline-container .no-timeline img {
            width: 400px;
        }

        .session-timeline-container .no-timeline-container .no-timeline .no-timeline-message {
            font-size: 20px;
            font-weight: 300 !important;
        }
    </style>
    <div class="session-timeline-container" style="">
        <div>
            <form class="session-filter-panel form-horizontal"
                  style="border-left: 0 !important; background-color: #f9fbfe">
                <div class="form-group" style="width: 50%;margin: 0;padding: 0 10px;">
                    <label class="control-label" style="text-align: right;">
                        <span translate>DATE</span> </label>
                    <br>

                    <div class="">
                        <input type="text" class="form-control input-sm" ng-model="sessionFilter.loginTime"
                               placeholder="Select Date" style="margin-top: 0;" inward-date-picker/>
                        <i class="fa fa-times" ng-if="sessionFilter.loginTime != null && sessionFilter.loginTime != ''"
                           style="margin-top: -25px;margin-right: 10px;float: right;cursor: pointer;"
                           ng-click="sessionFilter.loginTime = null;logHistoryVm.loadSessions()"></i>
                    </div>
                </div>
                <div class="form-group" style="width: 50%;margin: 0;padding: 0 10px;">
                    <label class="control-label">
                        <span translate>USER</span> </label>

                    <div class="">
                        <ui-select ng-model="selectedUser" theme="bootstrap"
                                   on-select="logHistoryVm.selectSessionUser($item)"
                                   on-remove="logHistoryVm.selectSessionUser()">
                            <ui-select-match allow-clear="true" placeholder="Select User">
                                {{$select.selected.fullName}}
                            </ui-select-match>
                            <ui-select-choices repeat="person in logHistoryVm.sessionPersons">
                                <div ng-bind-html="person.fullName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
        <div class="session-timeline-panel" style="">
            <div class="no-timeline-container"
                 ng-if="logHistoryVm.loading == false && logHistoryVm.sessions.length == 0">
                <div class="no-timeline">
                    <img src="app/assets/images/time.jpg" alt="">

                    <div class="no-timeline-message">No session records.</div>
                </div>
            </div>
            <ul class="log-timeline" ng-if="logHistoryVm.loading == false && logHistoryVm.sessions.length > 0"
                style="margin-bottom: 30px;">
                <div ng-repeat="session in logHistoryVm.sessions">
                    <li>
                        <div class="log-direction-l">
                            <div class="log-flag-wrapper" style="text-align: left !important;">
                                <span class="log-time-wrapper">
                                    <span class="time" style="font-size: 14px;">
                                         {{session.session.loginDate}}
                                    </span>
                                </span>
                            </div>
                        </div>
                        <div class="log-direction-r">
                            <div class="log-flag-wrapper" style="text-align: left !important;">
                                <span ng-class="{'session-flag':session.session.showLogs,'log-date-flag':!session.session.showLogs}"
                                      ng-click="showSessionLogs(session)">
                                </span>
                                <span class="log-time-wrapper" style="margin: 0;">
                                    <span class="time" style="font-size: 14px;line-height: 21px;padding: 1px 6px;">
                                        <span>{{session.session.login.person.firstName}} ( {{session.activityStreams.length}} {{activitiesTitle}}) </span>
                                        <span ng-if="session.duration != null"
                                              style="font-size: 12px; color: #929293; font-weight: normal;">(Session Duration: {{session.duration}})</span>
                                     <a class="icon fa fa-info-circle"
                                        style="padding-left: 5px; overflow-y: hidden !important;vertical-align: middle;"
                                        title="Session Info"
                                        ng-if="session.session.showLogs"
                                        uib-popover-template="sessionPopover.templateUrl"
                                        popover-append-to-body="true"
                                        popover-popup-delay="50"
                                        popover-placement="right"
                                        popover-trigger="'outsideClick'">
                                     </a>
                                    </span><br>
                                    <%--<span ng-if="session.session.showLogs"
                                          class="session-info">IP Address : {{session.session.ipAddress}}</span>
                                    <span ng-if="session.session.showLogs"
                                          class="session-info">Login Time : {{session.session.time}}</span>
                                    <span ng-if="session.session.showLogs"
                                          class="session-info">Logout Time : {{session.session.logout}}</span>--%>
                                </span>
                            </div>
                        </div>
                    </li>
                    <li ng-if="session.session.showLogs" ng-repeat="history in session.activityStreams"
                        class="log-type-{{history.type.toLowerCase()}}">
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
                                <span class="log-flag"></span>

                                <div class="log-time-wrapper">
                                    <div class="time" style="font-size: 14px;">
                                        <p style="color: #2a6fa8;">
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
                                        </p><br>

                                        <span style="line-height: 20px;" ng-bind-html="history.summary"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </div>
                <li ng-if="logHistoryVm.lastPage == false" style="padding: 25px 0">
                    <div class="log-direction-r">
                        <div class="log-flag-wrapper" style="text-align: left !important;">
                            <a href="" ng-click="logHistoryVm.loadMoreSessions()" style="">&#x2193; Load
                                More</a>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>