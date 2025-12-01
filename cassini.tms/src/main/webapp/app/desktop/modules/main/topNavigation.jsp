<div class="header-left" style="height: 50px">
    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li class="active hidden-xs hidden-sm">
                <a href="" class="topmenu-item-link topmenu-item-home">
                    <span style="">Cassini.<span style="color: #7BB7EB;">TMS</span></span>
                </a>
            </li>

            <li id="nav-home">
                <a ui-sref="app.home" class="topmenu-item-link">
                    <i class="fa fa-home" style="font-size: 15px;"></i> <span class="hidden-xs hidden-sm">Home</span>
                </a>
            </li>

            <li>
                <a href="" ng-click='showTrips()'>
                    <i class="fa mr5"
                       ng-class="{'fa-arrow-circle-right':!$parent.tripsVisible, 'fa-arrow-circle-left':$parent.tripsVisible}"></i>
                    <span class="hidden-xs hidden-sm"></span>
                    {{$parent.tripsVisible ? 'Hide Trips' : 'Show Trips'}}
                </a>
            </li>
            <li>
                <a href="" ng-click='$rootScope.methodName()'><i
                        class="fa fa-upload mr5"></i> <span class="hidden-xs hidden-sm"></span>Upload Trip</a>
            </li>
            <li id="nav-admin">
                <a ui-sref="app.admin.usersettings" class="topmenu-item-link">
                    <i class="fa fa-lock nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Admin</span>
                </a>
            </li>
        </ul>
        </li>
    </div>
</div>

<div class="header-right">
    <ul class="headermenu">
        <li style="height: 50px">
            <div class="btn-group" style="height: 100%; width: auto" uib-dropdown>
                <button type="button" class="btn btn-default btn-administrator" uib-dropdown-toggle
                        style="height: 100%;width: auto">
                    <i class="glyphicon glyphicon-user mr5"></i>
                    <span class="hidden-xs hidden-sm" title="{mainVm.user.name}">{{mainVm.user.firstName}}</span>
                    <span class="caret"></span>
                </button>
                <ul class="uib-dropdown-menu dropdown-menu usermenu pullright" uib-dropdown-menu>
                    <li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                            class="glyphicon glyphicon-user"></i>My Profile</a>
                    </li>
                    <li><a href="" ng-click="mainVm.logout()"><i class="glyphicon glyphicon-log-out"></i>Log Out</a>
                    </li>
                    <li><a href="" ng-click="mainVm.feedback()"><i class="glyphicon glyphicon-log-out"></i>Feedback</a>
                    </li>
                </ul>
            </div>
        </li>
    </ul>
</div>