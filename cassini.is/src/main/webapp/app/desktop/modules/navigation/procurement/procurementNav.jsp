<div id="project-headerbar" class="headerbar project-headerbar" project-header-bar ng-show="flag == true">
    <div class="header-left" style="height: 50px;">
        <div class="topnav">
            <ul class="nav nav-horizontal">
                <li ng-repeat="item in procNavVm.navItems">
                    <a href="" ng-click="procNavVm.activateProjectTab(item.id)"
                       class="topmenu-item-link" ng-class="{'active': item.active == true}">
                        <span>{{item.label}}</span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>