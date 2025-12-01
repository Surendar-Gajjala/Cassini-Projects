<style>
    .onoffswitch {
        position: relative;
        width: 60px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
    }

    .onoffswitch-checkbox {
        display: none;
    }

    .onoffswitch-label {
        display: block;
        overflow: hidden;
        cursor: pointer;
        height: 36px;
        padding: 0;
        line-height: 36px;
        border: 2px solid #E3E3E3;
        border-radius: 36px;
        background-color: #FFFFFF;
        transition: background-color 0.3s ease-in;
    }

    .danger {
        background-color: #ffdddd;
        border-left: 6px solid #f44336;
    }

    .onoffswitch-label:before {
        content: "";
        display: block;
        width: 36px;
        margin: 0px;
        background: #FFFFFF;
        position: absolute;
        top: 0;
        bottom: 0;
        right: 22px;
        border: 2px solid #E3E3E3;
        border-radius: 36px;
        transition: all 0.3s ease-in 0s;
    }

    .onoffswitch-checkbox:checked + .onoffswitch-label {
        background-color: #49E845;
    }

    .onoffswitch-checkbox:checked + .onoffswitch-label, .onoffswitch-checkbox:checked + .onoffswitch-label:before {
        border-color: #49E845;
    }

    .onoffswitch-checkbox:checked + .onoffswitch-label:before {
        right: 0;
    }

    .fa-size {
        font-size: 24px;
    }
</style>

<div id="application-settings" style="overflow: auto;position:relative;">
    <h4 class="section-title" style="padding-left: 20px;" translate>Application</h4>

    <form class="form-horizontal">
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label">
                <span translate>REQUIRE_CHANGE_APPROVAL_PASSWORD</span> :
                <%--<span class="asterisk">*</span> :--%>
            </label>

            <div class="col-sm-7" style="margin-top: 10px;display: flex;">
                <input type="radio" name="changeApprovals" ng-model="changeApproval.booleanValue" ng-value="true"
                       style="cursor: pointer;">
                <span style="padding-left: 2px;padding-right: 10px;" translate>YES</span>
                <input type="radio" name="changeApprovals" ng-model="changeApproval.booleanValue" ng-value="false"
                       style="cursor: pointer;">
                <span style="padding-left: 2px;padding-right: 10px;" translate>NO</span>
            </div>
        </div>
    </form>

    <br/>
    <h4 class="section-title" style="padding-left: 20px;" translate>FILES</h4>

    <form class="form-horizontal">
        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label">
                <span translate>TAB_FILES_SIZE</span>
                <span class="asterisk">*</span> :
            </label>

            <div class="col-sm-4" style="margin-top: 10px;">
                <a href="">
                    <span ng-if="appSettingsVm.fileSize == false" ng-click="appSettingsVm.addfileSize()">
                        {{appSettingsVm.application.fileSize || 'ADD_FILE_TYPE'}} (MB)
                    </span>
                </a>

                <div ng-if="appSettingsVm.fileSize == true" style="display: flex;">
                    <input type="number" class="form-control" name="title"
                           placeholder="{{'ENTER_VALUE' | translate}}"
                           ng-model="appSettingsVm.application.fileSize" style="width:200px;">

                    <button class="btn btn-sm btn-primary"
                            type="button"
                            ng-click="appSettingsVm.SubmitFileSize()"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default"
                            type="button" title="{{cancelChangesTitle}}" ng-click="appSettingsVm.cancelFileSize()">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </div>
        </div>

        <div class="form-group" style="margin: 10px 0">
            <label class="col-sm-4 control-label">
                <span translate>NOT_SUPPORTED_FILE_TYPE</span>:
                <%--<span class="asterisk">*</span> :--%>
            </label>

            <div class="col-sm-7" style="margin-top: 10px;display: flex;">
                <a href="">
                    <span ng-if="appSettingsVm.fileType == false && appSettingsVm.application.fileType != null"
                          ng-click="appSettingsVm.addfileType()">
                        {{appSettingsVm.application.fileType}}
                    </span>
                    <span ng-if="appSettingsVm.fileType == false && appSettingsVm.application.fileType == null"
                          ng-click="appSettingsVm.addfileType()">
                        {{appSettingsVm.addFileType}}
                    </span>
                </a>

                <div style="width: 50%;display: flex;" ng-if="appSettingsVm.fileType == true">
                    <textarea class="form-control" name="title"
                              placeholder="{{'ENTER_VALUE' | translate}}"
                              ng-model="appSettingsVm.application.fileType" style="width:180px;" rows="15"
                              cols="30">
                    </textarea>

                    <button class="btn btn-sm btn-primary" style="height: 32px;"
                            type="button" ng-click="appSettingsVm.SubmitFileType()"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" style="height: 32px;"
                            type="button" title="{{cancelChangesTitle}}" ng-click="appSettingsVm.cancelFileType()">
                        <i
                                class="fa fa-times"></i>
                    </button>
                </div>
                <div style="width: 50%;" class="" ng-if="appSettingsVm.fileType == true">
                    <span ng-if="currentLang == 'en'">
                        <img src="app/assets/images/filetype_en.gif" style="width: 296px;">
                    </span>
                    <span ng-if="currentLang == 'de'">
                        <img src="app/assets/images/filetype_de.gif" style="width: 296px;">
                    </span>
                </div>
            </div>
        </div>
    </form>
    <div>
        <div class="row">
            <h4 class="section-title" style="padding-left: 20px;" translate>FORGE</h4>
        </div>
        <form class="form-horizontal">

            <div class="form-group" style="margin: 10px 0">
                <label class="col-sm-4 control-label">
                    <span translate>FORGE_ACTIVE</span> :
                </label>

                <div class="col-sm-7" style="margin-top: 10px;display: flex;">
                    <input type="radio" name="forgeActive" ng-disabled="!appSettingsVm.forgeEdit"
                           ng-model="appSettingsVm.applicationForge.forgeActive.booleanValue" ng-value="true"
                           style="cursor: pointer;">
                    <span style="padding-left: 2px;padding-right: 10px;" translate>YES</span>
                    <input type="radio" name="forgeActive" ng-disabled="!appSettingsVm.forgeEdit"
                           ng-model="appSettingsVm.applicationForge.forgeActive.booleanValue" ng-value="false"
                           style="cursor: pointer;">
                    <span style="padding-left: 2px;padding-right: 10px;" translate>NO</span>
                </div>
            </div>

            <div class="form-group" style="margin: 10px 0">
                <label class="col-sm-4 control-label" style="padding-top: 16px;">
                    <span translate>FORGE_CLIENT_ID</span> :
                </label>

                <div class="col-sm-7" style="margin-top: 16px;display: flex;">

                    <span ng-if="appSettingsVm.forgeEdit == false"
                          translate>
                        {{appSettingsVm.applicationForge.forgeClientId.stringValue || 'ADD_CLIENT_ID'}}
                    </span>

                    <div ng-if="appSettingsVm.forgeEdit == true"
                         style="display: flex;">
                        <input type="text" class="form-control" name="title"
                               placeholder="{{'ENTER_VALUE' | translate}}"
                               ng-model="appSettingsVm.applicationForge.forgeClientId.stringValue"
                               style="width:400px;">
                    </div>
                </div>
            </div>

            <div class="form-group" style="margin: 10px 0">
                <label class="col-sm-4 control-label" style="padding-top: 16px;">
                    <span translate>FORGE_CLIENT_SECRET_KEY</span> :
                </label>

                <div class="col-sm-7" style="margin-top: 16px;display: flex;">

                    <span ng-if="appSettingsVm.forgeEdit == false"
                          translate>
                        {{appSettingsVm.applicationForge.forgeClientSecretKey.stringValue || 'ADD_CLIENT_SECRET_KEY'}}
                    </span>

                    <div ng-if="appSettingsVm.forgeEdit == true"
                         style="display: flex;">
                        <input type="text" class="form-control" name="title"
                               placeholder="{{'ENTER_VALUE' | translate}}"
                               ng-model="appSettingsVm.applicationForge.forgeClientSecretKey.stringValue"
                               style="width:400px;">
                    </div>
                </div>
            </div>

            <%--<div class="form-group" style="margin: 10px 0">
                <label class="col-sm-4 control-label" style="padding-top: 16px;">
                    <span translate>FORGE_BUCKET_NAME</span> :
                </label>

                <div class="col-sm-7" style="margin-top: 10px;display: flex;">
                    <span ng-if="appSettingsVm.forgeEdit == false || !appSettingsVm.applicationForge.forgeActive.booleanValue"
                          translate>
                        {{appSettingsVm.applicationForge.forgeBucketName.stringValue || 'ADD_BUCKET_NAME'}}
                    </span>

                    <div ng-if="appSettingsVm.forgeEdit == true && appSettingsVm.applicationForge.forgeActive.booleanValue"
                         style="display: flex;">
                        <input type="text" class="form-control" name="title"
                               placeholder="{{'ENTER_VALUE' | translate}}"
                               ng-model="appSettingsVm.applicationForge.forgeBucketName.stringValue"
                               style="width:400px;">
                    </div>
                </div>
            </div>--%>
            <div class="form-group" style="margin: 10px 0">

                <label class="col-sm-4 control-label" style="margin-top: 10px;"></label>

                <div class="col-sm-7" style="margin-top: 10px;">
                    <button type="button" class="btn btn-warning btn-sm" ng-if="appSettingsVm.forgeEdit == false"
                            ng-click="appSettingsVm.editForgeDetails()"
                            translate>EDIT
                    </button>
                    <button type="button" class="btn btn-success btn-sm" ng-click="appSettingsVm.submitForgeDetails()"
                            ng-if="appSettingsVm.forgeEdit == true"
                            translate>SAVE
                    </button>
                    <button type="button" class="btn btn-default btn-sm" ng-if="appSettingsVm.forgeEdit == true"
                            ng-click="appSettingsVm.cancelEditForgeDetails()"
                            translate>CANCEL
                    </button>
                </div>
            </div>
        </form>
    </div>

    <div>
        <div class="row">
            <h4 class="section-title" style="padding-left: 20px;" translate>HOLIDAYS_PLAN</h4>
        </div>
        <form class="form-horizontal">

            <div class="form-group" style="margin: 15px 0">
                <label class="col-sm-4 control-label" style="margin-top: 10px;">
                    <span translate>WORKING_DAYS</span> :
                </label>

                <div class="col-sm-4" style="margin-top: 10px;">
                    <ui-select ng-model="appSettingsVm.holidays.workingDays" theme="bootstrap"
                               on-select="appSettingsVm.saveWorkingDays()" style="width:80%" id="workingDays"
                               name="workingDays">
                        <ui-select-match placeholder="Select">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices repeat="day in [5,6,7] | filter: $select.search">
                            <div ng-bind="day"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>

            <div class="col-md-12 headerSticky">
                <div class="responsive-table">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 20px;">
                                <i class="la la-plus" ng-click='appSettingsVm.addHoliday()'
                                   title="{{'NEW_HOLIDAY' | translate}}"></i>
                            </th>
                            <th style="" translate>NAME</th>
                            <th style="" translate>DATE</th>
                            <th style="width:120px; text-align: center" translate>ACTIONS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="appSettingsVm.holidays.holidayList.length == 0">
                            <td colspan="10"><span translate>NO_HOLIDAYS</span></td>
                        </tr>

                        <tr ng-repeat="holiday in appSettingsVm.holidays.holidayList"
                            ng-class="{'autonumber-editmode': holiday.editMode}">
                            <td style="">
                            <td style="vertical-align: middle;">
                        <span ng-show="holiday.showValues"
                              ng-bind-html="holiday.name  | highlightText: freeTextQuery"></span>
                                <input placeholder="Enter name" class="form-control" type="text" autofocus
                                       ng-show="holiday.editMode" ng-model="holiday.newName">
                            </td>

                            <td style="vertical-align: middle;">
                        <span ng-show="holiday.showValues"
                              ng-bind-html="holiday.date  | highlightText: freeTextQuery"></span>
                                <input placeholder="dd/mm/yyyy" class="form-control" type="text"
                                       ng-show="holiday.editMode" autocomplete="off" ng-model="holiday.newDate"
                                       date-picker-edit>
                            </td>

                            <td style="width:120px; text-align: center; vertical-align: middle;">
                                <div ng-if="!holiday.editMode">
                                    <i ng-click="appSettingsVm.showEditMode(holiday)"
                                       title="{{appSettingsVm.editTitle}}"
                                       class="la la-pencil"></span>
                                    </i>
                                    <i ng-click="appSettingsVm.deleteholiday(holiday)"
                                       title="{{appSettingsVm.deleteTitle}}"
                                       class="la la-trash"></span>
                                    </i>
                                </div>


                                <div class="btn-group" style="margin-bottom: 0px;" ng-if="holiday.editMode">
                                    <i ng-click="appSettingsVm.saveHoliday()" title="{{save}}"
                                       class="la la-check"></i>
                                    <i ng-if="!holiday.isNew"
                                       title="CancelChanges"
                                       ng-click="appSettingsVm.cancelEditMode(holiday)" class="la la-times">
                                    </i>
                                    <i ng-if="holiday.isNew"
                                       title="Cancel"
                                       ng-click="appSettingsVm.hideEditMode(holiday)"
                                       class="la la-times">
                                    </i>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    </div>
</div>
