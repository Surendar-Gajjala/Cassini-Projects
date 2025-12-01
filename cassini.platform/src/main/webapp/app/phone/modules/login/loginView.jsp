<div id="loginContainer" class="login-container" style="width: 100%; height: 100%;position: absolute;display: none"
     md-swipe-left="loginVm.onSwipeLeft()" md-swipe-right="loginVm.onSwipeRight()">
    <div layout="column" ng-cloak style="height: 400px; margin: 50px 40px;overflow: hidden">
        <div style="text-align: center" style="margin-bottom: 100px;">
            <img src="app/assets/images/white-logo-lg.png" height="80" width="80" alt="">
            <h4 style="color: #D9D9D9;margin: 0;font-size: 20px;">Cassini Systems</h4>
        </div>
        <md-content class="md-padding" style="overflow: hidden;margin-top: 50px;">
            <div flex-sm="100" flex-gt-sm="80" layout layout-sm="column">
                <form id="loginForm" name="loginForm" style="width: 100%;">
                    <div layout layout-sm="row">
                        <ng-md-icon icon="person" style="margin-top: 22px; fill: #ddd" size="30"></ng-md-icon>
                        <md-input-container style="width: 100%;" flex class="md-icon-float">
                            <label>User Name</label>
                            <input required ng-model="loginVm.login.username" ng-focus="loginVm.bringFormToView()">
                        </md-input-container>
                    </div>
                    <div layout layout-sm="row">
                        <ng-md-icon icon="https" style="margin-top: 25px;margin-left:2px; fill: #ddd"></ng-md-icon>
                        <md-input-container style="width: 100%;margin-left: 5px;">
                            <label>Password</label>
                            <input type="password" required ng-model="loginVm.login.password" ng-focus="loginVm.bringFormToView()">
                        </md-input-container>
                    </div>
                    <div layout layout-sm="row">
                        <md-checkbox aria-label="Remember Me" ng-model="loginVm.login.remember" style="margin-bottom: 0px;">
                            Remember Me
                        </md-checkbox>
                        <div flex style="text-align: right;">
                            <md-button type="submit" class="md-raised md-primary"
                                       ng-click="loginVm.performLogin($event)"
                                       style="margin-left: auto;"
                                       aria-label="Login">Login</md-button>
                        </div>
                    </div>
                </form>
            </div>
        </md-content>
    </div>
</div>
