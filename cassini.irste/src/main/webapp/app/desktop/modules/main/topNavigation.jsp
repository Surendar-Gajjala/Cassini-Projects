<style>

    .glyphicon {
        position: relative;
        top: 5px;
        display: inline-block;
        font-family: 'Glyphicons Halflings';
        font-style: normal;
        font-weight: 400;
        line-height: 1;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }

    .dropdown-toggle::after {
        display: none !important;
        width: 0px;
        height: 0px;
        margin-left: 0.255em;
        vertical-align: 0.255em;
        content: "";
        border-width: 0.3em 0.3em 0px;
        border-style: solid solid none;
    }

</style>

<div class="header-left" style="height: 50px;">
    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li class="active hidden-xs hidden-sm">
                <a class="topmenu-item-link topmenu-item-home">
                    <span style="">Complaint Redressal System</span>
                </a>
            </li>
        </ul>
    </div>
    <div class="topnav" id="app-navigation">

        <ul class="nav nav-horizontal" style="margin-left: 40px;" ng-if="loginPersonDetails != null">
            <%--<li id="nav-dashboard">
                <a ui-sref="app.dashboard" class="topmenu-item-link" style="border-right: none !important;">
                    <span class="hidden-xs hidden-sm">Dashboard</span>
                </a>
            </li>--%>
            <li id="nav-home">
                <a ui-sref="app.complaints.all" class="topmenu-item-link" style="border-right: none !important;">
                    <span class="hidden-xs hidden-sm"><i class="fa fa-wpforms"></i>Complaints</span>
                </a>
            </li>
            <li id="nav-users"
                ng-if="showWelcomePage == false && showUsersTab == true && loginPersonDetails.person.id == 1">
                <a ui-sref="app.users.all" class="topmenu-item-link" style="border-right: none !important;">
                    <span class="hidden-xs hidden-sm"><i class="fa fa-users"></i>Users</span>
                </a>
            </li>
            <li id="nav-faq" ng-if="loginPersonDetails.person.id == 1">
                <a ui-sref="app.settings" class="topmenu-item-link" style="border-right: none !important;">
                    <span class="hidden-xs hidden-sm"><i class="fa fa-wrench"></i>Settings</span>
                </a>
            </li>
        </ul>
    </div>
</div>

<div class="header-right">
    <div class="topnav">
        <ul class="nav nav-horizontal">
            <li id="nav-application" ng-if="showWelcomePage == true">
                <a ng-click="mainVm.newComplaint()" class="topmenu-item-link"
                   style="cursor:pointer;border-left: none !important;border-right: none !important;">
                    <span class="hidden-xs hidden-sm">New Complaint</span>
                </a>
            </li>

            <li id="nav-login" ng-if="showWelcomePage == true">
                <a ui-sref="app.irsteLogin" class="topmenu-item-link"
                   style="border-left: none !important;border-right: none !important;">
                    <span class="hidden-xs hidden-sm">Sign In</span>
                </a>
            </li>

            <li style="height:50px;border-left: none;" ng-if="loginPersonDetails != null">
                <div class="btn-group" style="height:100%; width: auto" uib-dropdown>
                    <button type="button" class="btn btn-default btn-administrator" uib-dropdown-toggle
                            style="height:100%; width: auto;background-color: #343A40 !important;border-color: #343A40">
                        <i class="glyphicon glyphicon-user mr5" style="color: white;font-size: 14px !important;"></i>
                    <span class="hidden-xs hidden-sm" title="{{mainVm.user.name}}"
                          style="color: white;font-size: 14px !important;">{{mainVm.user.name}}<span
                            ng-if="personTypeDetails != 'Administrator'">( {{loginTypeDetails}} )</span></span>
                        <span class="caret" style="border-top-color:#f7fbff"></span>
                    </button>
                    <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" uib-dropdown-menu>
                        <li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                                class="glyphicon glyphicon-user"></i>My
                            Profile</a></li>
                        <li><a href="" ng-click="mainVm.logout()"><i class="fa fa-power-off"></i>Log Out</a>
                        </li>
                    </ul>
                </div>
            </li>
        </ul>
    </div>
</div>
