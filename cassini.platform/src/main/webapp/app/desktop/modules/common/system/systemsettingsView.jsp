<style scoped>
    .lic-width-330 {
        word-wrap: break-word;
        min-width: 250px;
        width: 335px !important;
        white-space: normal !important;
        text-align: left;
    }

    .switch {
        position: relative;
        display: inline-block;
        width: 60px;
        height: 34px;
    }

    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }

    .switch .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: .4s;
        transition: .4s;
    }

    .switch .slider:before {
        position: absolute;
        content: "";
        height: 26px;
        width: 26px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
    }

    .switch input:checked + .slider {
        background-color: #2196F3;
    }

    .switch input:focus + .slider {
        box-shadow: 0 0 1px #2196F3;
    }

    .switch input:checked + .slider:before {
        -webkit-transform: translateX(26px);
        -ms-transform: translateX(26px);
        transform: translateX(26px);
    }

    /* Rounded sliders */
    .switch .slider.round {
        border-radius: 34px;
    }

    .switch .slider.round:before {
        border-radius: 50%;
    }
</style>
<div id="system-settings" style="overflow: auto;position:relative;">
    <h4 class="section-title" style="padding-left: 10px;" translate>TWO_FACTOR_AUTHENTICATION</h4>

    <form class="form-horizontal">
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label">
                <span translate>TWO_FACTOR_AUTHENTICATION_ENABLED</span> :
                <%--<span class="asterisk">*</span> :--%>
            </label>

            <div class="col-sm-7" style="margin-top: 10px;display: flex;">
                <input type="radio" name="two-factor-authentication"
                       ng-model="systemVm.twoFactorAuthentication.booleanValue" ng-value="true"
                       style="cursor: pointer;">
                <span style="padding-left: 2px;padding-right: 10px;" translate>YES</span>
                <input type="radio" name="two-factor-authentication"
                       ng-model="systemVm.twoFactorAuthentication.booleanValue" ng-value="false"
                       style="cursor: pointer;">
                <span style="padding-left: 2px;padding-right: 10px;" translate>NO</span>
            </div>
        </div>
        <div class="form-group" ng-if="systemVm.twoFactorAuthentication.booleanValue">
            <label class="col-sm-4 control-label">
                <span translate>TWO_FACTOR_AUTHENTICATION_PASSCODE_EXPIRATION</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-4" style="margin-top: 10px;">
                <ui-select ng-model="systemVm.twoFactorAuthentication.integerValue" theme="bootstrap"
                           style="width:100%">
                    <ui-select-match placeholder="Select">{{$select.selected}} Minutes</ui-select-match>
                    <ui-select-choices repeat="expiration in systemVm.passcodeExpirations">
                        <div>{{expiration}} Minutes</div>
                    </ui-select-choices>
                </ui-select>
            </div>
        </div>
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label"></label>

            <div class="col-sm-4">
                <button type="button" class="btn btn-success btn-xs" title="{{save}}"
                        ng-click="systemVm.saveTwoFactorAuthentication()" translate>SAVE
                </button>
            </div>
        </div>
    </form>

    <br>
    <h4 class="section-title" style="padding-left: 10px;" translate>PASSWORD_STRENGTH</h4>

    <form class="form-horizontal">
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label" style="margin-top: 10px;">
                <span translate>MINIMUM_LENGTH</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-4" style="margin-top: 10px;">
                <ui-select ng-model="data.selectedOption" theme="bootstrap"
                           on-select="systemVm.changeMinLen()" style="width:100%" id="minLen" name="minLen">
                    <ui-select-match placeholder="Select">{{$select.selected.value}}</ui-select-match>
                    <ui-select-choices repeat="option in data.minLen | filter: $select.search">
                        <div ng-bind="option.value"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
        </div>
        <div class="form-group" style="margin: 10px 0 0 30%">
            <h5 translate>MUST_INCLUDE_NUMBERS_AND_SPECIAL_CHARACTERS</h5>

            <div id="specialChar" ng-repeat="char in data.specialChar">
                <input type="radio" name="specialChar"
                       ng-model="systemVm.system.password.specialChar"
                       ng-checked="char.id == systemVm.oldSettings.password.specialChar"
                       ng-value="char.id"> {{char.value}}<br>
            </div>
        </div>
        <div class="form-group" style="margin: 10px 0 0 30%">
            <h5 translate>MUST_INCLUDE_LETTERS_IN_MIXED_CASE</h5>
            <input type="radio" name="cases" ng-model="systemVm.system.password.cases" ng-value="'Yes'"
                    > <span translate> YES</span><br>
            <input type="radio" name="cases" ng-model="systemVm.system.password.cases" ng-value="'No'"
                    > <span translate> NO</span><br>
        </div>
        <div class="form-group" style="margin: 10px 0 0 40%">
            <div style="margin-top: 10px;margin-left: 20px;">
                <button type="button" class="btn btn-success btn-xs" title="{{save}}"
                        ng-click="systemVm.savePasswordStrength()" translate>SAVE
                </button>
            </div>
        </div>
    </form>

    <br>
    <h4 class="section-title" style="padding-left: 10px;" translate>MAIL_SETTINGS</h4>

    <form class="form-horizontal">
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label" for="mail-username" style="margin-top: 10px;">
                <span translate>EMAIL</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-7" style="margin-top: 10px;">
                <input ng-if="systemVm.editMailValue == true" type="text" class="form-control" id="mail-username"
                       placeholder="{{enterEmail}}" name="mail-username"
                       ng-model="systemVm.emailSettings.userName"/>

                <div ng-if="systemVm.editMailValue == false" style="margin-top: 5px;">
                    {{systemVm.emailSettings.userName}}
                </div>
            </div>
        </div>
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label" for="mail-password" style="margin-top: 10px;">
                <span translate>PASSWORD</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-7" style="margin-top: 10px;">
                <input ng-if="systemVm.editMailValue == true" type="password" class="form-control"
                       id="mail-password" name="mail-password"
                       placeholder="{{enterPassword}}"
                       ng-model="systemVm.emailSettings.password"/>

                <div ng-if="systemVm.editMailValue == false && systemVm.emailSettings.password != null && systemVm.emailSettings.password != ''"
                     style="margin-top: 5px;">*********
                </div>
            </div>
        </div>
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label" style="margin-top: 10px;">
                <span translate>HOST</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-7" style="margin-top: 10px;">
                <input ng-if="systemVm.editMailValue == true" type="text" class="form-control" name="title"
                       placeholder="{{enterHost}}"
                       ng-model="systemVm.emailSettings.host"/>

                <div ng-if="systemVm.editMailValue == false" style="margin-top: 5px;">
                    {{systemVm.emailSettings.host}}
                </div>
            </div>
        </div>

        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label" style="margin-top: 10px;">
                <span translate>PORT</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-7" style="margin-top: 10px;">
                <input ng-if="systemVm.editMailValue == true" type="text" class="form-control" name="title"
                       placeholder="{{enterPort}}"
                       ng-model="systemVm.emailSettings.port"/>

                <div ng-if="systemVm.editMailValue == false" style="margin-top: 5px;">
                    {{systemVm.emailSettings.port}}
                </div>
            </div>
        </div>

        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label" style="margin-top: 10px;">
                <span translate>SSL Trust</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-7" style="margin-top: 10px;">
                <input ng-if="systemVm.editMailValue == true" type="text" class="form-control" name="title"
                       placeholder="{{enterSslTrust}}"
                       ng-model="systemVm.emailSettings.sslTrust"/>

                <div ng-if="systemVm.editMailValue == false" style="margin-top: 5px;">
                    {{systemVm.emailSettings.sslTrust}}
                </div>
            </div>
        </div>

        <div class="form-group" style="margin: 10px 0">

            <label class="col-sm-4 control-label" style="margin-top: 10px;"></label>

            <div class="col-sm-7" style="margin-top: 10px;">
                <button type="button" class="btn btn-warning btn-xs" ng-if="systemVm.editMailValue == false"
                        ng-click="systemVm.editMailSettings()"
                        translate>EDIT
                </button>
                <button type="button" class="btn btn-success btn-xs" ng-click="systemVm.saveEmailSettings()"
                        ng-if="systemVm.editMailValue == true"
                        translate>SAVE
                </button>
                <button type="button" class="btn btn-default btn-xs" ng-if="systemVm.editMailValue == true"
                        ng-click="systemVm.editMailValue = false;systemVm.emailSettings = systemVm.oldMailSettings"
                        translate>CANCEL
                </button>
            </div>
        </div>

        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label" for="mail-username" style="margin-top: 10px;">
                <span translate>SUPPLIER_AUDIT_EMAIL_REMINDER</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-7" style="margin-top: 10px;">
                <input type="checkbox" id="email-reminder" switch="none" checked=""
                       ng-model="systemVm.supplierAuditEmailReminder.booleanValue"
                       ng-change="systemVm.saveAuditEmailReminder()">
                <label for="email-reminder" data-on-label="Yes" data-off-label="No"></label>
            </div>
        </div>
    </form>

    <br>
    <h4 class="section-title" style="padding-left: 10px;" translate>WHITELIST_IP</h4>

    <div class="form-group" style="margin: 10px 0">

        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 10px;">
            <span translate>WHITELIST_ACTIVE</span> :
            <%--<span class="asterisk">*</span> :--%>
        </label>

        <div class="col-sm-7" style="margin-top: 10px;">
            <input type="radio" name="addressActive" ng-model="systemVm.addressActive" ng-value="true"
                   ng-click="systemVm.saveAddress()" style="cursor: pointer;">
            <span style="padding-left: 2px;padding-right: 10px;" translate>YES</span>
            <input type="radio" name="addressActive" ng-model="systemVm.addressActive" ng-value="false"
                   ng-click="systemVm.saveAddress()" style="cursor: pointer;">
            <span style="padding-left: 2px;padding-right: 10px;" translate>NO</span>
        </div>
    </div>
    <div class="col-md-12" style="padding-bottom: 50px;">
        <div class="responsive-table">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 20px;">
                        <i class="la la-plus" ng-click='systemVm.addAddress()'
                           title="{{'NEW_ADDRESS' | translate}}"></i>
                    </th>
                    <th style="" translate>DESCRIPTION</th>
                    <th style="" translate>IP_ADDRESS</th>
                    <th style="width:120px; text-align: center" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="systemVm.ipAddress.length == 0">
                    <td colspan="10"><span translate>NO_IP_ADDRESS</span></td>
                </tr>

                <tr ng-repeat="address in systemVm.ipAddress"
                    ng-class="{'autonumber-editmode': address.editMode}">
                    <td style="">
                    <td style="vertical-align: middle;">
                        <span ng-show="address.showValues"
                              ng-bind-html="address.description  | highlightText: freeTextQuery"></span>
                        <input placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}" class="form-control" type="text"
                               autofocus
                               ng-show="address.editMode" ng-model="address.newDescription">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-show="address.showValues"
                              ng-bind-html="address.address  | highlightText: freeTextQuery"></span>
                        <input placeholder="xx.xx.xx.xx" class="form-control" type="text"
                               ng-show="address.editMode" autocomplete="off" ng-model="address.newAddress">
                    </td>

                    <td style="width:120px; text-align: center; vertical-align: middle;">
                        <div ng-if="!address.editMode">
                            <i ng-click="systemVm.showEditMode(address)"
                               title="{{systemVm.editTitle}}"
                               class="la la-pencil"></span>
                            </i>
                            <i ng-click="systemVm.deleteAddress(address)"
                               title="{{systemVm.deleteTitle}}"
                               class="la la-trash"></span>
                            </i>
                        </div>


                        <div class="btn-group" style="margin-bottom: 0px;" ng-if="address.editMode">
                            <i ng-click="systemVm.saveAddress()" title="{{save}}"
                               class="la la-check"></i>
                            <i ng-if="!address.isNew"
                               title="CancelChanges"
                               ng-click="systemVm.cancelEditMode(address)" class="la la-times">
                            </i>
                            <i ng-if="address.isNew"
                               title="Cancel"
                               ng-click="systemVm.hideEditMode(address)"
                               class="la la-times">
                            </i>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>


    <br>
    <h4 class="section-title" style="padding-left: 10px;" translate>LICENSE_SETTINGS</h4>

    <div class="form-group" style="margin: 10px 0">
        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 10px;">
            <span translate>LICENSE_KEY</span> :
        </label>

        <div class="col-sm-6 lic-width-330" style="margin-top: 10px">
                    <span ng-if="systemVm.changeLicenseKey == false" ng-click="systemVm.changeSystemLicenseKey()">
                        <a href="">
                            {{systemVm.licenseKey || 'Click to set license key'}}
                        </a>
                    </span>

            <div class="lic-width-330" style="display: flex;" ng-if="systemVm.changeLicenseKey == true">
                    <textarea rows="5" class="form-control" name="license"
                              placeholder="Licence Key" style="resize: none"
                              ng-model="systemVm.licenseKey">
                    </textarea>
            </div>
            <div style="text-align: right;margin-top: 3px;" ng-if="systemVm.changeLicenseKey == true">
                <button class="btn btn-sm btn-primary" style="height:32px;"
                        type="button" title="{{save}}"
                        ng-click="systemVm.saveLicenseKey()"><i class="fa fa-check"></i>
                </button>

                <button class="btn btn-sm btn-default" style="height: 32px;"
                        type="button" title="{{systemVm.reset}}" ng-click="systemVm.cancelLicenseKey()"><i
                        class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>


    <br>
    <h4 class="section-title" style="padding-left: 10px;" translate>Other Settings</h4>

    <div class="form-group" style="margin: 15px 0">
        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 10px;">
            <span translate>LOGOUT_TIME</span>
        </label>

        <div class="col-sm-4" style="margin-top: 10px;">
                    <span ng-if="systemVm.changeTime == false" ng-click="systemVm.changeSystemTime()">
                        <a href="">
                            {{systemVm.system.logoutTime || 'Click to set logoutTime'}}
                        </a>
                    </span>

            <div style="display: flex;" ng-if="systemVm.changeTime == true">
                <input type="number" class="form-control" name="title"
                       placeholder="{{'ENTER_NUMBER' | translate}}"
                       ng-model="systemVm.system.logoutTime" style="width:200px;">

                <button class="btn btn-sm btn-primary" style="height:32px;margin-top:6px;margin-left:5px;"
                        type="button"
                        ng-click="systemVm.submitLogOutTime()"><i class="fa fa-check"></i>
                </button>

                <button class="btn btn-sm btn-default" style="height: 32px;margin-top:6px;margin-left:5px;"
                        type="button" title="{{cancelChangesTitle}}" ng-click="systemVm.cancelTime()"><i
                        class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>
    <div class="form-group" style="margin: 15px 0">
        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 10px;">
            <span translate>COMPANY_LOGO</span>
        </label>

        <div class="col-sm-4" style="margin-top: 10px;">
            <div ng-if="systemVm.customImagePath != null || systemVm.system.customLogo != null">
                <img id="customImage" ng-src="{{systemVm.customImagePath}}"
                     style="width: 58px;height: 50px;cursor: pointer;margin-left: -2px;margin-top: 1px;"/>
            </div>
            <a href="">
                <span ng-if="systemVm.logo == false" ng-hide="systemVm.addLogo"
                      ng-click="systemVm.addImage()" translate>ADD_CUSTOM_LOGO
                </span>
            </a>

            <form>
                <div style="display: flex;">
                    <button ng-if="systemVm.logo" ng-hide="systemVm.addLogo"
                            style="height:32px;"
                            class="btn btn-sm btn-warning"
                            title="{{systemVm.editImage}}"
                            ng-click="systemVm.addImage()">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button ng-if="systemVm.logo" ng-hide="systemVm.addLogo"
                            style="height:32px;"
                            class="btn btn-sm btn-danger" type="reset"
                            ng-click="systemVm.deleteImage()"
                            title="{{systemVm.deleteImg}}">
                        <i class="fa fa-trash"></i>
                    </button>
                    <span ng-show="systemVm.addLogo">
                            <input type="file" id="image" class="form-control" value="file" id="files" accept="image/*"
                                   style="width: 200px"
                                   ng-file-model="systemVm.system.customLogo"/>
                        </span>
                    <button ng-if="systemVm.addLogo" class="btn btn-sm btn-primary"
                            style="height: 32px;margin-top:6px;margin-left:5px;"
                            title="{{'SAVE' | translate}}"
                            ng-click="systemVm.submitLogo()"><i
                            class="fa fa-check"></i>
                    </button>
                    <button ng-if="systemVm.addLogo" class="btn btn-sm btn-default" type="reset"
                            style="height: 32px;margin-top:6px;margin-left:5px;"
                            title="{{cancelChangesTitle}}"
                            ng-click="systemVm.cancelLogo()"><i
                            class="fa fa-times"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>
    <div class="form-group" style="margin: 15px 0">
        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 18px;">
            <span translate>CUSTOM_DATE_FORMAT </span> :
        </label>

        <div class="col-sm-6" style="margin-top: 10px;">
            <ui-select ng-model="systemVm.preference.stringValue" theme="bootstrap"
                       on-select="systemVm.submitDateFormat()" style="width:80%" id="minLen" name="minLen">
                <ui-select-match placeholder="Select">{{$select.selected}}
                </ui-select-match>
                <ui-select-choices repeat="format in formats track by format | filter: $select.search">
                    <div ng-bind="format"></div>
                </ui-select-choices>
            </ui-select>
        </div>
    </div>

    <br>
    <h4 class="section-title" style="padding-left: 10px;" translate>Default Values</h4>

    <div class="form-group" style="margin: 15px 0" ng-repeat="preference in systemVm.defaultPreferences">
        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 18px;">
            <span translate>{{preference.preferenceKey}}</span>
            <span class="asterisk">*</span> :
        </label>

        <div class="col-sm-6" style="margin-top: 10px;">
            <ui-select ng-model="preference.defaultValue.typeId" theme="bootstrap" style="width:80%"
                       ng-if="preference.defaultValue.type == 'LOV'"
                       on-select="systemVm.saveDefaultValue(preference)">
                <ui-select-match placeholder="Select LOV">{{$select.selected.name}}
                </ui-select-match>
                <ui-select-choices
                        repeat="listOfValue.id as listOfValue in systemVm.listOfValues | filter: $select.search">
                    <div ng-bind="listOfValue.name"></div>
                </ui-select-choices>
            </ui-select>
            <ui-select ng-model="preference.defaultValue.typeId" theme="bootstrap" style="width:80%"
                       ng-if="preference.defaultValue.type == 'AUTONUMBER'"
                       on-select="systemVm.saveDefaultValue(preference)">
                <ui-select-match placeholder="Select Auto Number">{{$select.selected.name}}
                </ui-select-match>
                <ui-select-choices
                        repeat="autoNumber.id as autoNumber in systemVm.autoNumbers | filter: $select.search">
                    <div ng-bind="autoNumber.name"></div>
                </ui-select-choices>
            </ui-select>
            <ui-select ng-model="preference.defaultValue.typeId" theme="bootstrap" style="width:80%"
                       ng-if="preference.defaultValue.type == 'ROLE'"
                       on-select="systemVm.saveDefaultValue(preference)">
                <ui-select-match placeholder="Select Role">{{$select.selected.name}}
                </ui-select-match>
                <ui-select-choices
                        repeat="personGroup.groupId as personGroup in systemVm.personGroups | filter: $select.search">
                    <div ng-bind="personGroup.name"></div>
                </ui-select-choices>
            </ui-select>
            <ui-select ng-model="preference.defaultValue.typeId" theme="bootstrap" style="width:80%"
                       ng-if="preference.defaultValue.type == 'LIFECYCLE'"
                       on-select="systemVm.saveDefaultValue(preference)">
                <ui-select-match placeholder="Select Lifecycle">{{$select.selected.name}}
                </ui-select-match>
                <ui-select-choices
                        repeat="lifecycle.id as lifecycle in systemVm.lifecycles | filter: $select.search">
                    <div ng-bind="lifecycle.name"></div>
                </ui-select-choices>
            </ui-select>

            <div ng-if="preference.defaultValue.type == 'WORKFLOWSTATUS'" style="margin: 10px !important;">
                <input type="radio" name="default-workflow-status"
                       ng-change="systemVm.saveDefaultValue(preference)"
                       ng-model="preference.booleanValue" ng-value="false"
                       style="cursor: pointer;">
                <span style="padding-left: 2px;padding-right: 10px;" translate>Current</span>
                <input type="radio" name="default-workflow-status"
                       ng-change="systemVm.saveDefaultValue(preference)"
                       ng-model="preference.booleanValue" ng-value="true"
                       style="cursor: pointer;">
                <span style="padding-left: 2px;padding-right: 10px;" translate>Finished</span>
            </div>
        </div>
    </div>


    <div class="form-group" style="margin: 15px 0">
        <label class="col-sm-4 control-label" style="text-align: right;margin-top: 18px;">
            <span translate>DEFAULT_REVISION_CONFIGURATION </span> :
        </label>

        <div class="col-sm-6" style="margin-top: 10px;">
            <ui-select ng-model="systemVm.defaultRevisionConfiguration" theme="bootstrap" style="width:80%"
                       on-select="systemVm.saveRevisionConfiguration()">
                <ui-select-match placeholder="{{selectConfigurationTitle}}">{{$select.selected.label}}
                </ui-select-match>
                <ui-select-choices repeat="bomRule in systemVm.bomRules">
                    <div ng-bind="bomRule.label"></div>
                </ui-select-choices>
            </ui-select>
        </div>
    </div>
</div>
