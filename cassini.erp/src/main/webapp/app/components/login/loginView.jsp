<div class="signin-wrapper" ng-controller="LoginController">
    <div class="login-logo"></div>
    <hr>

    <form id="signinForm" onsubmit="return false;" role="form" style="margin:20px;">

        <div class="alert alert-danger" ng-show="hasError" ng-cloak>
            {{ errorMessage }}
        </div>

        <p class="lead">Login to your account</p>

        <div class="form-group">
            <div class="input-group input-group-in">
                <span class="input-group-addon"><i class="fa fa-user"></i></span>
                <input name="username" id="username" class="form-control" placeholder="Username" ng-model="userName">
            </div>
        </div>

        <!-- /.form-group -->
        <div class="form-group">
            <div class="input-group input-group-in">
                <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                <input type="password" name="passwd" id="passwd" class="form-control" placeholder="Password" ng-model="password">
            </div>
        </div>
        <!-- /.form-group -->

        <br>

        <div ng-show="loggingIn" ng-cloak>
            <span>Logging in. Please wait...</span>

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
                <button id="btnSignin" class="btn btn-primary"
                        ng-click="login()">
                    Login <i class="fa fa-chevron-circle-right"></i>
                </button>
            </div>
            <div class="nice-checkbox nice-checkbox-inline">
                <input type="checkbox" name="rememberMe" id="rememberMe" checked="checked">
                <label for="rememberMe">Remember me</label>
            </div>
        </div>

    </form>
</div>
<!--/#wrapper-->
<div class="signin-cr">&copy; 2015 Cassini Systems</div>