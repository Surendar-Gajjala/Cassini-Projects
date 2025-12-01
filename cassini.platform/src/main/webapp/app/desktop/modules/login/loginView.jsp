<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>

<%
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    Integer year = cal.get(Calendar.YEAR);
%>

<div style="position: absolute;right: 0; left: 0; top: 0; bottom: 0;overflow-y: auto;padding-bottom: 50px;">
    <style scoped>

        .users-modal .responsive-table {
            top: 0 !important;
            overflow: auto;
            height: 50vh !important;
        }

        .users-modal .responsive-table table thead th {
            position: -webkit-sticky !important;
            position: sticky !important;;
            top: -10px !important;;
            z-index: 5 !important;;
            background-color: #fff !important;;
        }

        .users-modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: hidden !important; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .users-modal .user-content {
            margin: -24px auto;
            display: block;
            background-color: white;
            border-radius: 7px !important;
        }

        .user-header-message {
            text-align: center;
            font-size: 16px;
            margin: -24px 0 0 0;
        }

        .users-modal {
            padding: 10px;
            overflow: hidden;
            min-width: 100%;
            width: auto;
        }

        .active-user-error-message {
            text-align: center;
            color: #a94442;
            margin: 10px 0 0 0px;
        }

        .users-modal-footer {
            border-radius: 0;
            bottom: 0px;
            position: absolute;
        }

        .user-modal-footer {
            width: 87% !important;
        }

        .float-right {
            float: right !important;
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
    <style scoped>
        .login-image-container {
            position: absolute;
            bottom: 0;
            left: 0;
            top: 0;
            width: calc(100% - 500px);
            overflow: hidden;
        }

        @keyframes kenburns {
            0% {
                opacity: 1;
            }
            50% {
                transform: scale3d(1.5, 1.5, 1.5) translate3d(-190px, -120px, 0px);
                opacity: 1;
            }
            100% {
                transform: scale3d(2, 2, 2) translate3d(-170px, -100px, 0px);
                opacity: 1;
            }
        }

        .login-container {
            background-color: #f9fbfe;
        }

        .login-container .lead {
            color: var(--cassini-font-color) !important;
            white-space: nowrap !important;
        }

        .login-container .form-panel {
            transform: translateY(-40%);
            position: relative;
            top: 40%;
            margin-left: 20px;
            margin-right: 20px;
        }

        .login-container .form-panel .cassini-logo {
            margin-bottom: 50px;
        }

        .signin-cr {
            color: var(--cassini-font-color) !important;
            left: 0;
            right: 0;
            text-align: center;
            opacity: 0.6;
        }

        .signin-wrapper .progress-container .progress {
            height: 5px;
        }

        .signin-wrapper .error-panel {
            color: #FF3128;
        }

        .progress-container, .error-panel {
            position: absolute;
            left: 20px;
            right: 20px;
        }

        .progress-container .progress {
            margin-bottom: 0;
        }

        .field-icon {
            position: absolute;
            left: 32px;
            margin-top: 12px;
            opacity: 0.7;
            z-index: 9999;
        }

        .form-field {
            padding-left: 30px !important;
        }

        .langdrop {
            margin-left: -24px !important;
        }

        .mr-5 {
            margin-right: 5px;
        }

        .login-container .dropdown-menu {
            right: 0;
            left: unset;
        }

        .login-container a,
        .login-container a:hover,
        .login-container a:active,
        .login-container a:focus,
        .login-container a:visited {
            text-decoration: none;
        }

        .pd-0 {
            padding: 0;
        }

        .login-image-container .text-banner {
            height: 100px;
            background-color: rgba(0, 0, 0, 0.7);
            position: absolute;
            bottom: 0;
            width: 100%;
            font-size: calc(2vw + 2vh);
            line-height: 100px;
            text-align: center;
            color: #bbb;
        }

        .admin-active-class {
            font-size: 45px;
            cursor: no-drop;
            color: #1ea1d6;
        }

        .login-card {
            border: 1px solid rgba(22, 50, 92, 0.09);
            box-shadow: 0px 5px 10px 0 rgb(0 0 0 / 13%);
            background-color: #fff;
            max-width: 800px;
            width: 800px;
            height: 546px;
            position: absolute;
            top: -30px;
            bottom: 0;
            left: 0;
            right: 0;

            margin: auto;
        }

        .login-card .signin-cr {
            margin-top: 20px;
        }

        .loginCard > * {
            width: 100%;
            min-height: 520px;
        }

        .login-card input.form-control {
            border: 1px solid #D8DFE6 !important;
            background-color: #fff !important;
            padding: 8px 10px;
        }

        .login-card input.form-control:focus {
            border: 1px solid #A5A9AE !important;
        }

        .verifyOtpWrapper {
            display: flex;
            flex-direction: row;
            min-height: 520px;
            width: 100%;
        }

        .verifyOtpWrapper > div {
            flex-grow: 1;
            max-width: 50%;
        }

        .featureWrapper {
            background-image: linear-gradient(0deg, #3893da, #17cea4);
            display: flex;
            flex-direction: column;
        }

        .featureWrapper .subHeading {
            font-size: 30px;
            font-weight: 700 !important;
            color: #fff;
            text-align: center;
            padding: 20px;
            margin-top: 20px;
            margin-bottom: 20px;
        }

        .feature {
            flex-grow: 1;
            margin: 0;
            padding: 7px 23px 30px 24px;
        }

        .feature > li {
            display: flex;
            border: 1px solid #ffffff18;
            border-radius: 4px;
            margin: 0 0 26px;
            padding: 12px 18px;
            background-color: #ffffff18;
            box-shadow: 0px 0px 10px 0px rgb(0 0 0 / 5%);
            flex-direction: row;
            align-items: normal;
            height: 75px;
        }

        .featureTxt {
            margin-left: 30px;
            line-height: 45px;
            font-size: 40px;
        }

        .featureTxt .featureList {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        .featureTxt .featureLabel {
            color: #fff;
            font-size: 25px;
            font-weight: 400;
            margin-bottom: 10px;
        }

        .featureIcon i {
            font-size: 50px;
            color: #fff;
            font-weight: 600 !important;
            margin-left: 10px;
        }

        .featureList {
            color: #fff;
        }

        .login-card .btn-primary {
            background-color: #0070D2;
            border-color: #0070D2;
        }

        .login-card .btn-primary:hover {
            background-color: #198CF0;
            border-color: #198CF0;
            box-shadow: 0 1px 0 0 rgb(171 181 189 / 50%), 0 3px 6px 0 rgb(0 0 0 / 20%);
        }

        .subHeading .arrow-char {
            font-weight: 400;
        }

        .modal-dialog {
            width: 810px;
            margin-left: 26%;
            margin-top: 120px;
        }

    </style>

    <div class="modal fade users-modal" id="active-user-modal" data-backdrop="static"
         data-keyboard="true">
        <div class="modal-dialog">
            <div class="modal-content user-content">
                <div class="modal-header user-header-message">
                    <div>
                        <div class="active-user-error-message" ng-show="hasLicenseError" ng-cloak>
                            {{errorLicenseMessage}}
                        </div>
                    </div>
                </div>
                <div class="modal-body user-modal-body">
                    <div>
                        <div class="row">
                            <div class="responsive-table" style="padding: 10px;">
                                <table class="table table-striped" style="width: 100% !important;"
                                       id="users-table">
                                    <thead>
                                    <tr class="noBorder">
                                        <th class="col-width-550" translate>
                                            NAME
                                        </th>
                                        <th class="col-width-50" translate>
                                            <span></span> IS_ACTIVE
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    <tr ng-repeat="user in activeUsers">
                                        <td class="col-width-550">
                                        <span>
                                          {{user.person.fullName}}
                                        </span>
                                        </td>
                                        <td class="col-width-50">
                                            <input type="checkbox" id="isActive{{$index}}"
                                                   switch="none" checked=""
                                                   ng-model="user.isActive"
                                                   ng-disabled="user.isSuperUser"
                                                   ng-change="updateUserForLicense(user)">
                                            <label for="isActive{{$index}}" data-on-label="Yes"
                                                   data-off-label="No"></label>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="row">
                        <div class="col-md-6">
                        </div>
                        <div class="modal-buttons" class="col-md-6">
                            <button type="button" class="btn btn-sm btn-default"
                                    ng-click="cancelUserModal()" translate>CANCEL
                            </button>
                            <button type="button" class="btn btn-sm btn-primary"
                                    ng-click="validateActiveUsers()" translate>OK
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="login-card">
        <div class="verifyOtpWrapper">
            <div class="featureWrapper">
                <h4 class="subHeading h4 txt-light">
                    <%-- Define<span class="arrow-char">&#8674;</span>Develop<span class="arrow-char">&#8674;</span>Build<span class="arrow-char">&#8674;</span>Operate--%>
                    Unified PLM Platform
                </h4>
                <ul class="feature">
                    <li>
                        <div class="featureIcon">
                            <i class="las la-object-group"></i>
                        </div>
                        <div class="featureTxt">
                            <div class="featureLabel">Engineering</div>
                        </div>
                    </li>
                    <li>
                        <div class="featureIcon">
                            <i class="las la-cogs"></i>
                        </div>
                        <div class="featureTxt">
                            <div class="featureLabel">Manufacturing</div>
                        </div>
                    </li>
                    <li>
                        <div class="featureIcon">
                            <i class="las la-award"></i>
                        </div>
                        <div class="featureTxt">
                            <div class="featureLabel">Quality</div>
                        </div>
                    </li>
                    <li>
                        <div class="featureIcon">
                            <i class="las la-network-wired"></i>
                        </div>
                        <div class="featureTxt">
                            <div class="featureLabel">Service</div>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="login-container">

                <div ng-if="view == 'login'" class="form-panel signin-wrapper">
                    <div class="cassini-logo text-center">
                        <img src="app/assets/bower_components/cassini-platform/images/cassini-logo.png" alt=""
                             height="150">
                    </div>
                    <form id="signinForm" onsubmit="return false;" role="form" style="margin:20px;">
                        <p class="lead" translate-default="Login to your account" translate>LOGIN_MESSAGE</p>

                        <div class="form-group">
                            <i class="fa fa-user field-icon"></i>
                            <input name="username" id="username" class="form-control form-field"
                                   placeholder="{{loginVm.user}}" readonly
                                   onfocus="$(this).removeAttr('readonly');"
                                   translate-default="username" ng-model="loginVm.userName" autofocus>
                        </div>

                        <!-- /.form-group -->
                        <div class="form-group">
                            <i class="fa fa-lock field-icon"></i>
                            <input type="password" name="password" id="password" class="form-control form-field"
                                   placeholder="{{loginVm.pass}}" readonly
                                   onfocus="$(this).removeAttr('readonly');"
                                   translate-default="password" ng-model="loginVm.password">
                        </div>
                        <!-- /.form-group -->

                        <div class="row">
                            <div class="col-sm-6  pd-0">
                                <a href="" style="line-height: 32px" ng-click="loginVm.resetPassword()"
                                   translate-default="Forgot password" translate>FORGOT_PASSWORD</a>
                            </div>
                            <div class="col-sm-6 text-right  pd-0" ng-show="showAppLanguage">
                                <div class="langdrop" style="line-height: 32px; text-align: right !important;">
                                    <span style="margin-right: 5px;">Language:</span>
                                    <span class="dropdown">
                                <a href="" class="dropdown-toggle"
                                   data-toggle="dropdown"
                                   aria-haspopup="true" aria-expanded="false" style="font-size: 15px;">
                                    <span class="flag-icon flag-icon-us mr-5"
                                          ng-if="loginVm.languageKey === 'en'"></span>
                                    <span class="flag-icon flag-icon-de mr-5"
                                          ng-if="loginVm.languageKey === 'de'"></span>
                                    <span class="mr5" translate-default="Language" translate>LANGUAGE</span>
                                    <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu" role="menu" style="min-width:134px;">
                                    <li>
                                        <a ng-click="loginVm.changeLanguage('en')">
                                            <span class="flag-icon flag-icon-us mr-5"></span>
                                            <span translate-default="English" translate>LANGUAGES.English</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a ng-click="loginVm.changeLanguage('de')">
                                            <span class="flag-icon flag-icon-de mr-5"></span>
                                            <span translate-default="German" translate>LANGUAGES.German</span>
                                        </a>
                                    </li>
                                </ul>
                            </span>
                                </div>
                            </div>
                        </div>

                        <div class="progress-container" ng-show="loginVm.loggingIn" ng-cloak>
                            <span translate-default="Logging in.. Please wait" translate>LOADING</span>

                            <div class="progress progress-striped active">
                                <div class="progress-bar"
                                     role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                     aria-valuemax="100" style="width: 100%">
                                </div>
                            </div>
                            <br>
                        </div>

                        <div class="error-panel" ng-show="loginVm.hasError" ng-cloak translate>
                            {{ loginVm.errorMessage }}
                        </div>

                        <div class="form-group clearfix" style="margin-top: 30px">
                            <div class="row">
                                <div class="col-sm-12 pd-0">
                                    <button id="btnSignin" class="btn btn-primary"
                                            style="width: 100%;padding: 8px 15px;"
                                            ng-click="loginVm.login()" translate-default="Login">
                                        <span translate>LOGIN</span>
                                    </button>
                                </div>
                            </div>
                        </div>

                    </form>
                </div>

                <div ng-if="view == 'portal'" class="form-panel"
                     ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/checkPortalView.jsp'"
                     ng-controller="CheckPortalController as checkPortalVm">
                </div>

                <div ng-if="view == 'reset'" class="form-panel"
                     ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/resetPasswordView.jsp'"
                     ng-controller="ResetPasswordController as resetPwdVm">
                </div>

                <div ng-if="view == 'newPassword'" class="form-panel"
                     ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/newPasswordView.jsp'"
                     ng-controller="NewPasswordController as newPwdVm">

                </div>
                <div ng-if="view == 'updateEmail'" class="form-panel"
                     ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/updateEmail.jsp'"
                     ng-controller="UpdateEmailController as updateEmailVm">
                </div>

                <div ng-if="view == 'twoFactorAuthentication'" class="form-panel"
                     ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/twoFactorAuthenticationView.jsp'"
                     ng-controller="TwoFactorAuthenticationController as twoFactorAuthenticationVm">
                </div>

                <div ng-if="view == 'updateLicence'" class="form-panel"
                     ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/updateLicenceView.jsp'"
                     ng-controller="UpdateLicenceController as updateLicenceVm">
                </div>
            </div>
        </div>
        <div class="signin-cr">&copy; <%= year %> CassiniPLM. All Rights Reserved.</div>
    </div>
</div>
