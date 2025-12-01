<div>
    <style scoped>

        .view-details {

        }

        .view-details .view-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .view-details .view-header .view-title {
            font-size: 16px;
            font-weight: 600;
        }

        .view-details .view-header .view-summary {
            color: #707d91;
            font-weight: 300;
        }

        .view-details .view-content {

        }

    </style>

    <div class="view-details">
        <div class="view-header d-flex">
            <div style="flex: 1">
                <div class="view-title" translate>USER_SETTINGS</div>
                <div class="view-summary" translate>VIEW_UPDATE_USER_SETTINGS</div>
            </div>

            <div class="header-buttons">
                <button class="btn btn-sm btn-new"
                        ng-disabled="!userSettingsVm.userPreferences.userDateFormat && !userSettingsVm.preference.jsonValue"
                        ng-click="userSettingsVm.updatePreference()"
                        translate>SAVE
                </button>
            </div>
        </div>
        <div class="view-content">
            <form class="form-horizontal" style="margin-top: 30px;">
                <div class="form-group">
                    <label class="col-sm-4 control-label" <%--for="dFormat"--%>>
                        <span translate>DATE_FORMAT</span>
                        <span class="asterisk"> </span> :
                    </label>

                    <div class="col-sm-6">
                        <ui-select ng-model="userSettingsVm.userPreferences.userDateFormat" theme="bootstrap"
                                   ng-disabled="loginDetails.person.id != loginPersonDetails.person.id"
                                   on-select="userSettingsVm.onSelectDateFormat($item)">
                            <ui-select-match placeholder="{{select}}">{{$select.selected.dateFormat}}</ui-select-match>
                            <ui-select-choices repeat="format.dateFormat as format in userSettingsVm.dateFormats">
                                <div ng-bind="format.dateFormat"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <%--<div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>Application Theme :</span>
                    </label>

                    <div class="col-sm-6">
                        <ui-select ng-model="userSettingsVm.userPreferences.userTheme" theme="bootstrap"
                                   style="width:100%"
                                   ng-disabled="loginDetails.person.id != loginPersonDetails.person.id">
                            <ui-select-match placeholder="Application theme">
                                {{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="theme in userSettingsVm.themes | filter: $select.search">
                                <div>{{theme}}</div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>--%>

                <div class="form-group">
                    <label class="col-sm-4 control-label" for="personalPin">
                        <span translate>PERSONAL_PIN</span>:
                    </label>

                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="personalPin" autocomplete="new-password"
                               ng-disabled="loginDetails.person.id != loginPersonDetails.person.id"
                               placeholder="{{personalPin}}" ng-model="userSettingsVm.preference.jsonValue"/>
                    </div>
                </div>

                <div class="form-group" ng-hide="loginDetails.external == true">
                    <label class="col-sm-4 control-label">
                        <span translate>PREFERRED_START_PAGE</span>:
                    </label>

                    <div class="col-sm-6">
                        <button class="btn btn-sm btn-light-primary"
                                ng-click="userSettingsVm.resetPreferredPage()"
                                ng-disabled="loginDetails.person.id != loginPersonDetails.person.id" translate>RESET
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>