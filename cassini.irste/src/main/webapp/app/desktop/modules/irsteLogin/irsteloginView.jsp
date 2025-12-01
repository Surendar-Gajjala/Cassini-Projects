<div class="view-container" applicationfitcontent>
    <style>
        .signin-wrapper {
            position: relative;
            margin: 50px auto 5px;
            padding: 20px;
            width: 450px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.4);
        }

        .btn {
            background: #191717cc;
        }

        .btn:hover {
            background: rgba(8, 15, 17, 0.78);
        }
    </style>
    <div class="view-content no-padding" style="overflow-y: auto;"
    <div id="loginContainer">
        <div class="signin-wrapper">
            <%--<div class="irste-login"></div>
            <hr>--%>


            <div ng-if="view == 'login'">
                <form id="signinForm" onsubmit="return false;" role="form" style="margin:20px;">

                    <div class="alert alert-danger" ng-show="irsteLoginVm.hasError" ng-cloak>
                        {{ irsteLoginVm.errorMessage }}
                    </div>

                    <p class="lead" style="text-align: center;">SIGN IN</p>

                    <div class="form-group">
                        <div class="input-group input-group-in">
                            <span class="input-group-addon"><i class="fa fa-user"></i></span>
                            <input name="username" id="username" class="form-control"
                                   placeholder="Username"
                                   ng-model="irsteLoginVm.userName" autofocus>
                        </div>
                    </div>

                    <!-- /.form-group -->
                    <div class="form-group">
                        <div class="input-group input-group-in">
                            <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                            <input type="password" name="passwd" id="passwd" class="form-control"
                                   placeholder="Password" ng-model="irsteLoginVm.password">
                        </div>
                    </div>
                    <!-- /.form-group -->

                    <br>

                    <div ng-show="irsteLoginVm.loggingIn" ng-cloak>
                        <span>Logging in.. Please wait</span>

                        <div class="progress progress-striped active">
                            <div class="progress-bar"
                                 role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 100%">
                            </div>
                        </div>
                        <br>
                    </div>

                    <div class="form-group clearfix">
                        <div class="pull-right">
                            <button id="btnSignin" class="btn btn-sm btn-primary"
                                    ng-click="irsteLoginVm.login()">
                                <span>Sign In</span>
                                <i class="fa fa-chevron-circle-right"></i>
                            </button>
                        </div>
                        <div class="nice-checkbox nice-checkbox-inline">
                            <input type="checkbox" name="rememberMe" id="rememberMe" ng-model="irsteLoginVm.remember"
                                   ng-true-value="true" ng-false-value="false">
                            <label for="rememberMe">Remember Me</label>
                        </div>
                    </div>

                    <div class="text-center">
                        <a href="" ng-click="irsteLoginVm.resetPassword()">Forgot Password</a>
                    </div>

                </form>
            </div>

            <div ng-if="view == 'reset'"
                 ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/resetPasswordView.jsp'"
                 ng-controller="ResetPasswordController as resetPwdVm">
            </div>

            <div ng-if="view == 'newPassword'"
                 ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/newPasswordView.jsp'"
                 ng-controller="NewPasswordController as newPwdVm">

            </div>
        </div>
        <!--/#wrapper-->
        <%--<%--%>
        <%--Date date = new Date();--%>
        <%--Calendar cal = Calendar.getInstance();--%>
        <%--cal.setTime(date);--%>
        <%--Integer year = cal.get(Calendar.YEAR);--%>
        <%--;--%>
        <%--%>--%>
        <%--<div style="color: black" class="signin-cr">&copy; <%= year %> IRSTELO</div>--%>
    </div>
</div>
</div>