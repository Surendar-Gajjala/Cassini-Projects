<md-sidenav layout="column" class="md-sidenav-left md-whiteframe-z2" md-component-id="left" md-is-locked-open="$mdMedia('gt-md')">
    <md-toolbar class="md-tall md-hue-2" style="text-align: center;background-color:{{backgroundColor}}">
        <div layout="column" class="md-toolbar-tools-bottom inset">
            <span style="margin-top: -20px"><ng-md-icon icon="person" size="100"></ng-md-icon></span>
            <div>{{mainVm.user.name}}</div>
            <div>Phone: {{mainVm.user.phone}}</div>
        </div>
    </md-toolbar>

    <md-list>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;" ng-click="mainVm.toggleSidenav('left');mainVm.showProfile()">
                <ng-md-icon icon="dns" size="24" style="margin-right: 10px"></ng-md-icon>
                <span>My Profile</span>
            </md-button>
        </md-list-item>
        <md-list-item ng-if="mainVm.isAdmin == true">
            <md-button style="width: 100%; text-align: left;"
                       ng-click="mainVm.toggleSidenav('left');mainVm.goToState('app.task.all')">
                <ng-md-icon icon="list" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">All Tasks</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;" ng-click="mainVm.toggleSidenav('left');mainVm.goToState('app.person.all')">
                <ng-md-icon icon="group" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Persons</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;" ng-click="mainVm.toggleSidenav('left');mainVm.goToState('app.dept.all')">
                <ng-md-icon icon="store" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Departments</span>
            </md-button>
        </md-list-item>
        <!--
        <md-list-item>
            <md-button style="width: 100%; text-align: left;" ng-click="mainVm.toggleSidenav('left');mainVm.goToState('app.shift.all')">
                <ng-md-icon icon="access_time" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Shifts</span>
            </md-button>
        </md-list-item>
        -->
        <md-list-item ng-if="mainVm.isAdmin == true">
            <md-button style="width: 100%; text-align: left;"
                       ng-click="mainVm.toggleSidenav('left');mainVm.goToState('app.accomm.all')">
                <ng-md-icon icon="domain" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Accommodation</span>
            </md-button>
        </md-list-item>
    </md-list>
</md-sidenav>

<div layout="column" class="relative" layout-fill role="main">
    <md-toolbar style="background-color:{{backgroundColor}}">
        <div class="md-toolbar-tools">
            <md-button class="md-icon-button" ng-click="mainVm.toggleSidenav('left')" aria-label="Settings"
                       style="margin-left: -18px;padding-top: 8px;">
                <ng-md-icon icon="menu"></ng-md-icon>
            </md-button>
            <md-button class="md-icon-button" ng-click="mainVm.goBack()" aria-label="Back"
                       style="margin-left: -15px;padding-top: 8px;">
                <ng-md-icon icon="arrow_back"></ng-md-icon>
            </md-button>
            <h1 flex style="text-align: center;">
                <span>{{viewName}}</span>
            </h1>
            <md-button ng-click="mainVm.showHome()" class="md-icon-button" aria-label="More"
                       style="margin-right: -20px;padding-top: 8px;">
                <ng-md-icon icon="home"></ng-md-icon>
            </md-button>
            <md-menu style="padding: 0;">
                <md-button ng-click="$mdOpenMenu()" class="md-icon-button" aria-label="More"
                           style="margin-right: -15px; padding-top: 8px;">
                    <ng-md-icon icon="more_vert"></ng-md-icon>
                </md-button>
                <md-menu-content width="4">
                    <md-menu-item>
                        <md-button ng-click="mainVm.showProfile()">
                            <ng-md-icon icon="person"></ng-md-icon>
                            My Profile
                        </md-button>
                    </md-menu-item>
                    <md-menu-item>
                        <md-button ng-click="mainVm.logout()">
                            <ng-md-icon icon="logout"></ng-md-icon>
                            Logout
                        </md-button>
                    </md-menu-item>
                </md-menu-content>
            </md-menu>
        </div>
    </md-toolbar>

    <md-content flex md-scroll-y>
        <div class="main-view" ui-view style=""></div>
    </md-content>
</div>
