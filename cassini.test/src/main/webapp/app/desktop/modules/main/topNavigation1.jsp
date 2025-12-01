<div class="header-left" style="height: 50px;">
    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li class="active hidden-xs hidden-sm">
                <a href="" class="topmenu-item-link topmenu-item-home">
                    <span style="">Cassini.<span style="color: #7BB7EB;">TEST</span></span>
                </a>
            </li>
            <li id="nav-home">
                <a ui-sref="app.home" class="topmenu-item-link">
                    <i class="fa fa-home" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm" translate="HOME"></span>
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
                        style="height:100%;">
                    <i class="glyphicon glyphicon-user mr5"></i>
                    <span class="hidden-xs hidden-sm">{{mainVm.user.person.firstName}}</span>
                    <span class="caret"></span>
                </button>
                <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" uib-dropdown-menu>
                    <li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                            class="glyphicon glyphicon-user"></i><span translate>MY_PROFILE</span> </a></li>
                    <li><a href="" ng-click="mainVm.logout()"><i class="glyphicon glyphicon-log-out"></i><span
                            translate>LOGOUT</span> </a>
                    </li>
                    <li><a href="" ng-click="mainVm.feedback()"><i class="glyphicon glyphicon-comment"></i><span
                            translate>FEEDBACK</span></a>
                    </li>
                    <li><a ui-sref="app.help.main"><i class="glyphicon glyphicon-exclamation-sign"></i><span translate>HELP</span></a>
                    </li>
                </ul>
            </div>
        </li>
    </ul>
</div>

