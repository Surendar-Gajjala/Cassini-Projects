<div class="header-left" style="height: 50px;" ng-controller="NavController">

    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li class="active hidden-xs hidden-sm">
                <a href="" class="topmenu-item-link topmenu-item-home">
                    <span style="">Cassini.<span style="color: #7BB7EB;">ERP</span></span>
                </a>
            </li>
            <li id="nav-home">
                <a ui-sref="app.home" class="topmenu-item-link">
                    <i class="fa fa-home" style="font-size: 15px;"></i> <span class="hidden-xs hidden-sm">Home</span>
                </a>
            </li>


            <li ng-if="hasRole('Order Entry')">
                <a ui-sref="app.crm.orders.all" class="topmenu-item-link">
                    <i class="fa flaticon-chart44 nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Orders</span>
                </a>
            </li>

            <li ng-if="hasRole('Order Entry')">
                <a ui-sref="app.crm.customers" class="topmenu-item-link">
                    <i class="fa flaticon-office nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Customers</span>
                </a>
            </li>

            <li ng-if="hasRole('Order Entry')">
                <a ui-sref="app.crm.salesregions" class="topmenu-item-link">
                    <i class="fa flaticon-global42 nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Regions</span>
                </a>
            </li>

            <li ng-if="hasRole('Order Entry') && hasPermission('permission.hrm.employee.all')">
                <a ui-sref="app.hrm.employees" class="topmenu-item-link">
                    <i class="fa flaticon-businessman276 nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Employees</span>
                </a>
            </li>

            <li ng-if="hasRole('HR Manager')">
                <a ui-sref="app.hrm.payroll" class="topmenu-item-link">
                    <i class="fa flaticon-finance-and-business4 nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Payroll</span>
                </a>
            </li>

            <li ng-if="hasRole('HR Manager')">
                <a ui-sref="app.hrm.attendance" class="topmenu-item-link">
                    <i class="fa fa-building nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Attendance</span>
                </a>
            </li>

            <li class="nav-parent" dropdown ng-repeat="group in navigation" ng-if="hasRole('Administrator') || hasRole('Sales Administrator')">
                <a class="dropdown-toggle topmenu-item-link" data-toggle="dropdown" href="#" dropdown-toggle>
                    <i class="fa" ng-class="group.icon"></i>
                    <span class="hidden-xs hidden-sm">{{group.text}}</span>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu children">
                    <li ng-repeat="navItem in group.children">
                        <a ui-sref="{{navItem.state}}">
                            <i class="fa nav-icon-font" ng-class="navItem.icon"></i>{{navItem.text}}
                        </a>
                    </li>
                </ul>
            </li>

            <li id="nav-reporting" ng-if="hasRole('Administrator')">
                <a ui-sref="app.reporting.reports" class="topmenu-item-link">
                    <i class="fa fa-bar-chart-o"></i> <span class="hidden-xs hidden-sm">Reporting</span>
                </a>
            </li>
        </ul>
    </div>
</div>

<div class="header-right" ng-controller="NotificationController">
    <ul class="headermenu">
        <li ng-if="hasShoppingCart()">
            <div class="btn-group">
                <button ng-click="showShoppingCart()" class="btn btn-default dropdown-toggle tp-icon">
                    <i class="fa fa-shopping-cart"></i>
                    <span class="badge">{{getShoppingCartSize()}}</span>
                </button>
            </div>
        </li>

        <%@ include file="ordersNotification.jsp" %>
        <%@ include file="returnsNotification.jsp" %>

        <li style="height:50px;">
            <div class="btn-group" style="height:100%;" dropdown>
                <button type="button" class="btn btn-default dropdown-toggle" dropdown-toggle
                        style="height:100%;">
                    <i class="glyphicon glyphicon-user mr5"></i>
                    <span class="hidden-xs hidden-sm">{{userName}}</span>
                    <span class="caret"  style="border-top-color:#f7fbff"></span>
                </button>
                <ul class="dropdown-menu dropdown-menu-usermenu pull-right">
                    <li><a href="" ng-click="showProfile()"><i class="glyphicon glyphicon-user"></i> My Profile</a></li>
                    <!--<li><a href="#"><i class="glyphicon glyphicon-cog"></i> Account Settings</a></li>-->
                    <li><a ui-sref="app.help"><i class="glyphicon glyphicon-question-sign"></i> Help</a></li>
                    <li><a href="" ng-click="showTour()"><i class="glyphicon glyphicon-question-sign"></i> Take a
                        tour</a></li>
                    <li><a ui-sref="app.feedback"><i class="glyphicon glyphicon-log-out"></i>Feedback</a></li>
                    <li><a href="" ng-click="logout()"><i class="glyphicon glyphicon-log-out"></i> Log Out</a></li>
                </ul>
            </div>
        </li>
    </ul>
</div>
