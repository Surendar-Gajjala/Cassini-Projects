<div class="header-left" style="height: 50px;">
    <div class="topnav" id="app-navigation">
        <ul class="nav nav-horizontal">
            <li class="active hidden-xs hidden-sm">
                <a href="" class="topmenu-item-link topmenu-item-home">
                    <span style="">BZA.<span style="color: #7BB7EB;">RRI</span></span>
                </a>
            </li>

            <li ng-if="hasRole('Administrator') == true || isAdmin() == true">
                <a ui-sref="app.home" class="topmenu-item-link">
                    <i class="fa fa-home" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Home</span>
                </a>
            </li>

            <li ng-if="hasRole('Administrator') == true || isAdmin() == true">
                <a ui-sref="app.project.all" class="topmenu-item-link">
                    <i class="fa flaticon-businessman277 nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Projects</span>
                </a>
            </li>

            <li ng-if="hasRole('Staff') == true || hasRole('Administrator') == true ||
                    hasRole('Supervisor') == true || hasRole('Officer') == true || isAdmin() == true">
                <a ui-sref="app.task.all" class="topmenu-item-link">
                    <i class="fa fa-tasks nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Tasks</span>
                </a>
            </li>

            <%--<li id="nav-shift">
                <a ui-sref="app.shift.all" class="topmenu-item-link">
                    <i class="fa flaticon-deadlines nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Shifts</span>
                </a>
            </li>--%>

            <%-- <li id="nav-report">
                 <a ui-sref="app.report.all" class="topmenu-item-link">
                     <i class="fa flaticon-deadlines nav-icon" style="font-size: 15px;"></i>
                     <span class="hidden-xs hidden-sm">Reports</span>
                 </a>
             </li>--%>

            <li ng-if="hasRole('Staff') == true || hasRole('Administrator') == true || hasRole('Officer') == true || hasRole('Supervisor') == true || isAdmin() == true">
                <a ui-sref="app.person.all" class="topmenu-item-link">
                    <i class="fa flaticon-businessman276 nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Persons</span>
                </a>
            </li>

            <li ng-if="hasRole('Administrator') == true || isAdmin() == true || hasRole('Officer') == true">
                <a ui-sref="app.accommodation.all" class="topmenu-item-link">
                    <i class="fa flaticon-office42 nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Accommodation</span>
                </a>
            </li>

            <li ng-if="hasRole('Administrator') == true || isAdmin() == true">
                <a ui-sref="app.department.all" class="topmenu-item-link">
                    <i class="fa flaticon-businessman278 nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Departments</span>
                </a>
            </li>
            <li ng-if="hasRole('Administrator') == true || isAdmin() == true">
                <a ui-sref="app.layout.all" class="topmenu-item-link">
                    <i class="fa fa-files-o nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Layout</span>
                </a>
            </li>
            <li ng-if="hasRole('Administrator') == true || isAdmin() == true">
                <a ui-sref="app.media" class="topmenu-item-link">
                    <i class="fa fa-image nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Media</span>
                </a>
            </li>

            <li id="nav-admin">
                <a ui-sref="app.admin.usersettings" class="topmenu-item-link">
                    <i class="fa fa-lock nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Admin</span>
                </a>
            </li>
            <!--
            <li id="nav-admin">
                <a ui-sref="app.admin.logins" class="topmenu-item-link">
                    <i class="fa fa-lock nav-icon" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Admin</span>
                </a>
            </li>
            -->
        </ul>
    </div>
</div>

<div class="header-right">
    <ul class="headermenu">
        <%@ include file="assignedTasksNotification.jsp" %>
        <li style="height:50px;">
            <div class="btn-group" style="height:100%;" uib-dropdown>
                <button type="button" class="btn btn-default" uib-dropdown-toggle
                        style="height:100%;">
                    <i class="glyphicon glyphicon-user mr5"></i>
                    <span class="hidden-xs hidden-sm">{{mainVm.user.person.firstName}}</span>
                    <span class="caret"></span>
                </button>
                <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" uib-dropdown-menu>
                    <%--<li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i class="glyphicon glyphicon-user"></i>My Profile</a></li>--%>
                        <li><a href="" ui-sref="app.person.details({personId: mainVm.user.person.id})"><i class="glyphicon glyphicon-user"></i>My Profile</a></li>
                    <li><a href="" ng-click="mainVm.logout()"><i class="glyphicon glyphicon-log-out"></i>Log Out</a></li>
                    <li><a href="" ng-click="mainVm.feedback()"><i class="glyphicon glyphicon-log-out"></i>Feedback</a></li>
                </ul>
            </div>
        </li>
    </ul>
</div>
