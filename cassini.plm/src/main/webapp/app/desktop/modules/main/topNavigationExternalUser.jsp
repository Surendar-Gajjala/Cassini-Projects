<div>
    <style scoped>
        .messages-notification {
            text-decoration: none;
            position: relative;
            display: inline-block;
            border-radius: 2px;
        }

        .messages-notification .badge1 {
            position: absolute;
            top: 10px;
            right: -3px;
            padding: 5px;
            border-radius: 50%;
            background-color: orange;
            color: white;
            font-size: 14px;
        }

        .profile-dropdown {
            width: 250px;
        }

        .profile-dropdown .profile-avatar {
            margin-left: 80px;
            margin-top: 20px;
        }

        .profile-dropdown .user-profile-name {
            text-align: center;
            font-size: 18px;
            margin-top: 10px;
        }

        .profile-dropdown .person-email {
            text-align: center;
            margin-top: 5px;
            color: #959bab;
            word-break: break-all;
        }

        .profile-dropdown .profile-buttons {
            margin-top: 20px;
            margin-bottom: 20px;
        }

        .profile-dropdown .profile-buttons .btn {
            width: 100px;
        }

        .profile-dropdown .profile-buttons .btn:first-child {
            margin-right: 10px;
        }

    </style>
    <div class="header-left" style="height: 50px;">
        <div class="topnav" id="app-navigation">
            <ul class="nav nav-horizontal">
                <li ng-if="companyImage != null" style="margin-left: 5px;">
                    <img ng-src="{{companyImage}}"
                         style="max-width: 120px;height: 50px; border-radius: 5px;padding: 10px;"/>
                </li>
                <li ng-if="companyImage == null" class="active hidden-xs hidden-sm">
                    <img src="/app/assets/images/cassini-logo-moons.png"
                         style="width: 45px;margin-top: -10px;margin-left: 8px;border: 0"/>
                    <a ng-click="mainVm.showHomePage()"
                       style="cursor: pointer;display: inline-block;padding-left: 0;background: inherit; border: 0"
                       class="topmenu-item-link topmenu-item-home">
                        <span style="">Cassini<span style="color: #00b5a4;">PLM</span></span>
                    </a>
                </li>
            </ul>
        </div>
    </div>


    <div class="header-right">
        <ul class="headermenu">
            <li style="height:50px;">
                <div class="btn-group" style="height:100%;" uib-dropdown>
                    <button type="button" class="btn btn-default" uib-dropdown-toggle
                            style="height:100%;" ng-click="toggleAdmin()">
                        <i class="glyphicon glyphicon-user mr5"></i>
                        <span class="hidden-xs hidden-sm">{{personInfo.fullName}}</span>
                        <span class="caret" style="border-top-color:#f7fbff"></span>
                    </button>
                    <ul id="user-info" class="uib-dropdown-menu dropdown-menu-usermenu pull-right"
                        ng-click="toggleAdmin()"
                        uib-dropdown-menu>

                        <li>
                            <div class="profile-dropdown">
                                <div class="profile-avatar">
                                    <person-avatar person-id="mainVm.$application.login.person.id"
                                                   ng-if="mainVm.$application.loadProfileImage"
                                                   display="'normal'"></person-avatar>
                                </div>
                                <div class="user-profile-name">
                                    {{personInfo.fullName}}
                                </div>
                                <div class="person-email">
                                    {{personInfo.email}}
                                </div>
                                <div class="text-center profile-buttons">
                                    <button class="btn btn-xs btn-default" ng-click="mainVm.showProfile(mainVm.user)"
                                            translate>MY_PROFILE
                                    </button>
                                    <button class="btn btn-xs btn-primary" ng-click="mainVm.logout()" translate>LOGOUT
                                    </button>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </li>
            <li style="height: 50px;padding: 12px 10px;">
                <a href="" style="color: white; padding-right: 20px" class="fa fa-envelope messages-notification"
                   title="Assigned Tasks" ng-click="showAssignedTasks()">
                    <span class="badge1" ng-if="externalUserTasks.length > 0">{{externalUserTasks.length}}</span>
                </a>
            </li>
        </ul>
    </div>
</div>



