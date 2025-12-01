<md-sidenav layout="column" class="md-sidenav-left md-whiteframe-z2" md-component-id="left" md-is-locked-open="$mdMedia('gt-md')">
    <md-toolbar class="md-tall md-hue-2" style="text-align: center;background-color:{{backgroundColor}}">
        <div layout="column" class="md-toolbar-tools-bottom inset">
            <span style="margin-top: -20px"><ng-md-icon icon="person" size="100"></ng-md-icon></span>
            <div>{{login.person.firstName}}</div>
        </div>
    </md-toolbar>

    <md-list>
        <!--<md-subheader class="md-no-sticky" style="border-bottom: 1px solid #ddd;">CRM</md-subheader>-->
        <md-list-item>
            <md-button style="width: 100%; text-align: left;" ng-click="toggleSidenav('left');showOrders()">
                <ng-md-icon icon="assignment" size="24" style="margin-right: 10px"></ng-md-icon>
                <span>Orders</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;" ng-click="toggleSidenav('left');showCustomers()">
                <ng-md-icon icon="assignment_ind" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Customers</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;" ng-click="toggleSidenav('left');showProducts()">
                <ng-md-icon icon="layers" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Products</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;">
                <ng-md-icon icon="person" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Sales Reps</span>
            </md-button>
        </md-list-item>

        <!--
        <md-subheader class="md-no-sticky" style="border-bottom: 1px solid #ddd;">HRM</md-subheader>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;">
                <ng-md-icon icon="group" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Employees</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;">
                <ng-md-icon icon="access_time" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Payroll</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;">
                <ng-md-icon icon="select_all" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Attendance</span>
            </md-button>
        </md-list-item>

        <md-subheader class="md-no-sticky" style="border-bottom: 1px solid #ddd;">Admin</md-subheader>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;">
                <ng-md-icon icon="https" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Logins</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;">
                <ng-md-icon icon="perm_contact_cal" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Permissions</span>
            </md-button>
        </md-list-item>
        <md-list-item>
            <md-button style="width: 100%; text-align: left;">
                <ng-md-icon icon="receipt" size="24" style="margin-right: 10px"></ng-md-icon>
                <span style="padding-bottom: 5px">Sessions</span>
            </md-button>
        </md-list-item>
    -->
    </md-list>
</md-sidenav>

<div layout="column" class="relative" layout-fill role="main">
    <md-toolbar style="background-color:{{backgroundColor}}">
        <div class="md-toolbar-tools">
            <md-button class="md-icon-button" ng-click="toggleSidenav('left')" aria-label="Settings"
                       style="margin-left: -18px;padding-top: 8px;">
                <ng-md-icon icon="menu"></ng-md-icon>
            </md-button>
            <h1 flex style="text-align: center;">
                <span>{{viewName}}</span>
            </h1>
            <md-button ng-click="showHome()" class="md-icon-button" aria-label="More"
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
                        <md-button ng-click="showMessage()">
                            <ng-md-icon icon="person"></ng-md-icon>
                            My Profile
                        </md-button>
                    </md-menu-item>
                    <md-menu-item>
                        <md-button ng-click="logout()">
                            <ng-md-icon icon="logout"></ng-md-icon>
                            Logout
                        </md-button>
                    </md-menu-item>
                    <md-menu-item ng-if="mobileDevice != null && mobileDevice.disablePushNotification == true">
                        <md-button ng-click="disableNotification()">
                            Disable Notification
                        </md-button>
                    </md-menu-item>
                    <md-menu-item ng-if="mobileDevice != null && mobileDevice.disablePushNotification == true">
                        <md-button ng-click="enableNotification()">
                            Enable Notification
                        </md-button>
                    </md-menu-item>
                </md-menu-content>
            </md-menu>
        </div>
    </md-toolbar>

    <md-content flex md-scroll-y>
        <div class="main-view" ui-view style="" ng-if="loggedIn == true"></div>
    </md-content>
</div>
