<div class="header-left" style="height: 50px;">
    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li id="sideToggler" class="menutoggle" onclick="event.preventDefault()" ng-click="toggleSideNav($event)"
                style="">
                <i class="las la-bars" style="font-size: 30px;margin: 10px;"></i>
            </li>
            <li ng-if="companyImage != null" style="margin-left: 5px;" ng-click="mainVm.showHomePage()">
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

<div ng-include="'app/desktop/modules/main/searchBarView.jsp'"
     ng-controller="SearchBarController as searchVm">
</div>

<div class="header-right">
    <style scoped>
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
    <ul class="headermenu">
        <li style="height:50px;">
            <div class="btn-group" style="height:100%;"
                 ng-if="loginPersonDetails.groups.length > 1 && !loginPersonDetails.isAdmin && !loginPersonDetails.external"
                 uib-dropdown>
                <button type="button" class="btn btn-default" uib-dropdown-toggle style="height:100%;"
                        ng-click="toggleRole()">
                    <i class="la la-users-cog"></i>
                    <span class="hidden-xs hidden-sm">{{loginPersonDetails.defaultGroup.name}}</span>
                    <span class="caret" style="border-top-color:#f7fbff"></span>
                </button>
                <ul id="user-switch-roles" class="uib-dropdown-menu dropdown-menu-usermenu pull-right"
                    ng-click="toggleRole()" uib-dropdown-menu style="max-height: 500px;overflow: auto;">
                    <li ng-repeat="group in loginPersonDetails.groups"
                        ng-hide="loginPersonDetails.defaultGroup.groupId == group.groupId">
                        <a href="" ng-click="mainVm.switchRole(group)"><span>{{group.name}}</span> </a>
                    </li>
                </ul>
            </div>
            <div class="btn-group" style="height:100%;" uib-dropdown>
                <button type="button" class="btn btn-default" uib-dropdown-toggle
                        style="height:100%;" ng-click="toggleAdmin()">
                    <i class="glyphicon glyphicon-user mr5"></i>
                    <span class="hidden-xs hidden-sm">{{personInfo.fullName}}</span>
                    <span class="caret" style="border-top-color:#f7fbff"></span>
                </button>
                <ul id="user-info" class="uib-dropdown-menu dropdown-menu-usermenu pull-right" ng-click="toggleAdmin()"
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
                    <%--<li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                            class="glyphicon glyphicon-user"></i><span translate>MY_PROFILE</span> </a></li>
                    <li ng-show="hasPermission('about','view') || hasPermission('about','edit')"><a
                            ng-click="mainVm.about()"><i class="glyphicon glyphicon-list-alt"></i><span
                            translate>ABOUT</span></a>
                    </li>--%>
                    <%--<li><a href="" ng-click="mainVm.feedback()"><i class="glyphicon glyphicon-comment"></i><span
                            translate>SUPPORT</span></a>
                    </li>--%>
                    <%--<li class="divider"></li>
                    <li><a href="" ng-click="mainVm.logout()"><i class="fa fa-power-off"></i><span
                            translate>LOGOUT</span> </a>
                    </li>--%>
                    <!--<li><a ng-click="mainVm.help()"><i class="glyphicon glyphicon-exclamation-sign"></i><span translate>HELP</span></a>
                    </li>-->
                </ul>
            </div>
        </li>
        <li id="nav-activities" ng-show="hasPermission('activity','all')"
            style="height: 50px;padding: 12px 10px;">
            <a ng-click="mainVm.showLogHistory()" style="font-size: 16px;vertical-align: sub;color: #fff;"
               title="Activity Stream">
                <i class="fa fa-history" style="font-size: 16px;"></i>
            </a>
        </li>
    </ul>
</div>
