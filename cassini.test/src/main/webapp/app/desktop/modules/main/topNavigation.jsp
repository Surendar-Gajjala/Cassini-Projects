<div class="header-left" style="height: 50px;">
    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li class="active hidden-xs hidden-sm">
                <a hui-sref="app.home" class="topmenu-item-link topmenu-item-home">
                    <span style="">Cassini.<span style="color: #7BB7EB;">TEST</span></span>
                </a>
            </li>
            <%--<a id="sideToggler" class="menutoggle" onclick="event.preventDefault()"  ng-click="toggleSideNav($event)"><i class="fa fa-bars"></i></a>--%>
            <li id="nav-home">
                <a ui-sref="app.home">
                    <i class="fa fa-home" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm" translate="Home"></span>
                </a>
            </li>
            <li id="nav-Defination">
                <a ui-sref="app.definition" class="topmenu-item-link">
                    <i class="fa fa-ellipsis-v" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">TEST DEFINITION</span>
                </a>
            </li>
            <li id="nav-onfiguration">
                <a ui-sref="app.config" class="topmenu-item-link">
                    <i class="fa fa-sliders" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">RUN CONFIGURATION</span>
                </a>
            </li>
            <li id="nav-run">
                <a ui-sref="app.run.all" class="topmenu-item-link">
                    <i class="fa fa-play" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">TEST RUN</span>
                </a>
            </li>

            <li id="nav-run">
                <a ui-sref="app.settings" class="topmenu-item-link">
                    <i class="fa fa-wrench nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm" translate="Settings"></span>
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
                    <i class="glyphicon glyphicon-user mr5" style="color: white;"></i>
                    <span class="hidden-xs hidden-sm" style="color: white">{{mainVm.user.name}}</span>
                    <span class="caret" style="border-top-color:#f7fbff"></span>
                </button>
                <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" uib-dropdown-menu>
                    <li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                            class="glyphicon glyphicon-user"></i><span translate>MY_PROFILE</span> </a></li>
                    <li><a href="" ng-click="mainVm.logout()"><i class="glyphicon glyphicon-log-out"></i><span
                            translate>LOGOUT</span> </a>
                    </li>
                    <li><a ui-sref="app.help.main"><i class="glyphicon glyphicon-exclamation-sign"></i><span translate>HELP</span></a>
                    </li>
                </ul>
            </div>
        </li>
    </ul>
</div>

