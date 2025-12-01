<style>
    .updates-notification.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 10; /* Sit on top */
        left: 0;
        top: 50px;
        overflow: hidden; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .updates-notification .modal-content {
        margin-left: auto;
        display: block;
        border-radius: 0px;
    }

    .messages-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .messages-notification .badge1 {
        position: absolute;
        top: 10px;
        right: -20px;
        padding: 5px;
        border-radius: 50%;
        background-color: orange;
        color: white;
        font-size: 14px;
    }
</style>
<div class="header-left" style="height: 50px;">
    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li class="active hidden-xs hidden-sm">
                <a ng-click="mainVm.showHomePage()" style="cursor: pointer;"
                   class="topmenu-item-link topmenu-item-home">
                    <span style="">CAS.<span style="color: #7BB7EB;">MS</span></span>
                </a>
            </li>
            <span ng-hide="hideToggleNode">
            <a id="sideToggler" class="menutoggle" onclick="event.preventDefault()"
               ng-click="toggleSideNav($event)">
                <i class="fa fa-bars"></i>
            </a>
            </span>
        </ul>
    </div>
</div>

<div ng-if="hasPermission('permission.top.search')"
     ng-include="'app/desktop/modules/main/searchBarView.jsp'"
     ng-controller="SearchBarController as searchVm">
</div>

<div class="header-right">
    <ul class="headermenu">
        <li style="height: 50px;padding: 15px;font-size: 20px;">
            <a href="" style="color: white;" class="fa fa-envelope messages-notification" ng-click="showUpdates()">
                <span class="badge1" ng-if="personUnreadMessages > 0">{{personUnreadMessages}}</span>
            </a>
        </li>
        <li style="height:50px;">
            <div class="btn-group" style="height:100%;" uib-dropdown>
                <button type="button" class="btn btn-default" uib-dropdown-toggle
                        style="height:100%;" ng-click="toggleAdmin()">
                    <i class="glyphicon glyphicon-user mr5"></i>
                    <span class="hidden-xs hidden-sm">{{mainVm.user.person.firstName}}</span>
                    <span class="caret" style="border-top-color:#f7fbff"></span>
                </button>
                <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" ng-click="toggleAdmin()"
                    uib-dropdown-menu>
                    <li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                            class="glyphicon glyphicon-user"></i><span translate>MY_PROFILE</span> </a></li>
                    <li><a href="" ng-click="mainVm.logout()"><i class="fa fa-power-off"></i><span
                            translate>LOGOUT</span> </a>
                    </li>
                    <li><a href="" ng-click="mainVm.feedback()"><i class="glyphicon glyphicon-comment"></i><span
                            translate>FEEDBACK</span></a>
                    </li>
                    <li><a ng-click="mainVm.help()"><i class="glyphicon glyphicon-exclamation-sign"></i><span translate>HELP</span></a>
                    </li>
                </ul>
            </div>
        </li>
    </ul>
</div>

<div id="updatesPanel" class="updates-notification modal">
    <div id="updates-content" class="modal-content">
        <div style="height: 50px;width: 100%;text-align: center;padding: 10px;border-bottom: 1px solid lightgrey;">
            <span class="text-center text-primary" style="font-size: 22px;">Updates</span>
                <span class="fa fa-times" ng-click="unreadMessages()" title="Click to Close"
                      style="float: right;cursor: pointer;font-size: 30px;"></span>
        </div>
        <div id="updates" class="updates-content" style="overflow-y: auto;">
            <%--<div style="padding: 10px;">
                <button class="btn btn-xs btn-danger" ng-click="deleteUpdates()">Clear</button>
            </div>--%>
            <hr style="margin: 0;">
                <div ng-repeat="update in mainVm.drdoUpdates" title="{{!update.read ?'Click to read' : ''}}"
                 style="padding: 10px 15px;border-bottom: 1px solid lightgrey;">
                <h5 style="margin:0;cursor: pointer;" ng-if="!update.read"
                    ng-click="mainVm.updateMessage(update)">{{update.message}}</h5>

                <p style="margin:0;" ng-if="update.read">{{update.message}}</p>

                <p style="text-align: right;margin: 0">{{update.date}}</p>
            </div>
        </div>
    </div>
</div>
