<div>
    <style scoped>
        meter {
            /* Reset the default appearance */
            -moz-appearance: none;
            appearance: none;

            margin: 0 auto 1em;
            width: 100%;
            height: 0.7em;

            /* Applicable only to Firefox */
            background: none;
            background-color: rgba(0, 0, 0, 0.1);
        }

        meter::-webkit-meter-bar {
            background: none;
            background-color: rgba(0, 0, 0, 0.1);
        }

        meter[value="1"]::-webkit-meter-optimum-value {
            background: red;
        }

        meter[value="2"]::-webkit-meter-optimum-value {
            background: yellow;
        }

        meter[value="3"]::-webkit-meter-optimum-value {
            background: orange;
        }

        meter[value="4"]::-webkit-meter-optimum-value {
            background: green;
        }

        meter[value="1"]::-moz-meter-bar {
            background: red;
        }

        meter[value="2"]::-moz-meter-bar {
            background: yellow;
        }

        meter[value="3"]::-moz-meter-bar {
            background: orange;
        }

        meter[value="4"]::-moz-meter-bar {
            background: green;
        }

        .panel-heading {
            height: 50px !important;
            background-color: white !important;
            border: 1px solid lightgrey !important;
            padding: 8px;
            display: inline-flex !important;
            width: 100%;
        }

    </style>

    <div class="panel panel-primary">
        <div id="admin-rightView-bar" class="panel-heading">
            <div class="btn-group" style="width: 40%;">
                <button type="button" class="btn btn-sm btn-default" ng-click="userVm.cancel()" translate>CANCEL
                </button>
                <button type="button" class="btn btn-sm btn-success" ng-click="userVm.createUser()" translate>CREATE
                </button>
            </div>

            <div style="width: 60%;">
                <h3 style="margin-top: 3px;" translate>NEW_USER</h3>
            </div>
        </div>

        <div id="admin-rightView" class="panel-body"
             style="overflow-x: hidden;overflow-y: auto;font-size: 14px;">
            <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-1">
                <div class="row">
                    <form class="form-horizontal" style="margin-top: 20px;">

                        <div class="form-group">
                            <label class="col-sm-4 control-label" for="fname"><span translate>FIRST_NAME</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" id="fname" placeholder="{{'FIRST_NAME_TITLE' | translate}}"
                                       ng-model="userVm.person.firstName">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label" for="Lname"><span translate>LAST_NAME</span> :
                            </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" id="Lname"  placeholder="{{'LAST_NAME_TITLE' | translate}}"
                                       ng-model="userVm.person.lastName">
                            </div>
                        </div>


                        <div class="form-group" id="content">
                            <label class="col-sm-4 control-label"><span translate>USERNAME</span> <span
                                    class="asterisk">*</span> :</label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="{{'USER_NAME_TITLE' | translate}}"
                                       autocomplete='new-password' ng-model="userVm.login.loginName">
                            </div>
                        </div>


                        <div ng-show="userVm.login.loginName != null && userVm.login.loginName != ''" class="form-group"
                             style="margin-top: -16px;">
                            <div class="col-sm-4"></div>
                            <div class="col-sm-7">
                                <div class="alert alert-info" style="margin: 0px;">
                                    <i class="fa fa-info mr10"></i>
                                    <span ng-bind-html="invalidUsername"></span>
                                </div>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>PASSWORD</span>
                                <span class="asterisk">*</span> :</label>

                            <div class="col-sm-7">
                                <%--<span ng-if="safeChars == true">The password cannot contains any of the following canracters</span>--%>
                                <input type="password" id="password" name="password" class="form-control" placeholder="{{'PASSWORD' | translate}}"
                                       autocomplete='new-password' ng-change="passwordStrengthValid()"
                                <%-- ng-keyup="searchKeyup()"--%>
                                       ng-model="userVm.login.password">
                        <span id="showPassword" class="fa fa-fw fa-eye-slash" ng-click="userVm.showUserPassword()"
                              title="{{'SHOW_PASSWORD' | translate}}"
                              style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;
                              font-size: 18px;margin-right: 5px;color: black;"></span>
                                <meter max="4" id="password-strength-meter"></meter>
                            </div>
                        </div>

                        <div ng-if="safeCharsPassword == true" class="form-group"
                             style="margin-top: -16px;">
                            <div class="col-sm-4"></div>
                            <div class="col-sm-7">
                                <div class="alert alert-danger" style="margin: 0px;">
                                    <%--<i class="fa fa-danger mr10"></i>--%>
                                    <span ng-bind-html="invalidPassword"></span></div>
                            </div>
                        </div>
                        <div class="form-group" style="margin-top: -16px;" ng-if="userVm.login.password">
                            <div class="col-sm-4"></div>
                            <div class="col-sm-7">
                                <div class="alert alert-info" style="margin: 0px;">
                                    <i class="fa fa-info mr10"></i>
                                    <span ng-bind-html="passwordInformation"></span>
                                </div>
                            </div>
                        </div>
                        <%-- <div ng-show="userVm.login.password != null && userVm.login.password != '' && userVm.passwordStrength == 'medium'"
                              class="form-group"
                              style="margin-top: -16px;">
                             <div class="col-sm-4"></div>
                             <div class="col-sm-7">
                                 <div class="alert alert-info" style="margin: 0px;">
                                     <i class="fa fa-info mr10"></i>
                                     <span ng-bind-html="userVm.passwordMediumInformation"></span>
                                 </div>
                             </div>
                         </div>
                         <div ng-show="userVm.login.password != null && userVm.login.password != '' && userVm.passwordStrength == 'low'"
                              class="form-group"
                              style="margin-top: -16px;">
                             <div class="col-sm-4"></div>
                             <div class="col-sm-7">
                                 <div class="alert alert-info" style="margin: 0px;">
                                     <i class="fa fa-info mr10"></i>
                                     <span ng-bind-html="userVm.passwordLowInformation"></span>
                                 </div>
                             </div>
                         </div>--%>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>DEFAULT_GROUP </span> <span
                                    class="asterisk">*</span>:</label>

                            <div class="col-sm-7" style="border: 0;">
                                <ui-select class="required-field" ng-model="userVm.personDefaultGrp"
                                           on-select="userVm.onSelectGroup($item)"
                                           theme="bootstrap">
                                    <ui-select-match placeholder="{{'SELECT_GROUP_TITLE' | translate}}">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices repeat="group in userVm.groups | filter: $select.search"
                                                       style="max-height: 191px;">
                                        <div ng-if="group.isActive"
                                             ng-bind="group.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>PHONE_NUMBER</span>
                                <%--<span class="asterisk">*</span>--%>:</label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="{{'PHONE_NUMBER_TITLE' | translate}}"
                                       ng-model="userVm.person.phoneMobile" valid-number
                                       pattern="[0-9]*">
                                <%--<span ng-show="myForm.mobile_number.$error.pattern">Please enter valid number!</span>--%>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>EMAIL</span>
                                <span class="asterisk">*</span> :</label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="{{'EMAIL' | translate}}" ng-model="userVm.person.email">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>IS_ACTIVE</span> :</label>

                            <div class="col-sm-2">
                                <input style="margin-top: 10px" type="checkbox" class="form-control"
                                       ng-init="userVm.login.isActive = true"
                                       ng-change="userVm.userActivation()" ng-model="userVm.login.isActive">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>EXTERNAL</span> :</label>

                            <div class="col-sm-2">
                                <input ng-disabled="true"
                                       style="margin-top: 10px"
                                       type="checkbox"
                                       class="form-control"
                                       ng-init="userVm.login.external = false"
                                       ng-model="userVm.login.external">
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>