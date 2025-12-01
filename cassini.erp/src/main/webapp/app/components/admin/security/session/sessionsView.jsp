<div class="row" style="margin-bottom: 20px">
    <div class="col-md-12"  style="text-align: right" ng-show="sessions.numberOfElements > 0">
        <div style="text-align: right;">
            <pagination total-items="sessions.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{sessions.totalElements}} sessions</small>
        </div>
    </div>
</div>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <%--<th style="width:100px; text-align: center">Actions</th>--%>
                <th style="width:150px; text-align: center">Login</th>
                <th style="width:150px; text-align: center">User</th>
                <th style="text-align: center">IP Address</th>
                <th style="text-align: center">Login Time</th>
                <th style="text-align: center">Logout Time</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <%--<td style="width:100px; text-align: center;">
                    <table-filters-actions apply-filters="applyFilters()" reset-filters="resetFilters()"/>
                </td>--%>

                <td>
                    <table-filter align="center" placeholder="Login" filters="filters" property="login" apply-filters="applyFilters()" />
                </td>
                <td>
                    <table-filter align="center" placeholder="User" filters="filters" property="user" apply-filters="applyFilters()" />
                </td>
                <td style="width: 200px;">
                    <table-filter align="center" placeholder="IP Address" filters="filters" property="ipAddress" apply-filters="applyFilters()" />
                </td>
                <td style="width: 200px;"">
                    <input placeholder="Login time"
                           style="width: 100%; text-align: center"
                           options="dateRangeOptions"
                           date-range-picker
                           clearable="true"
                           class="form-control date-picker"
                           ng-enter="applyFilters()"
                           ng-class="{ hasFilter: (filters.loginTime.startDate != null && filters.loginTime.endDate != '') }"
                           type="text" ng-model="filters.loginTime" />
                </td>
                <td style="width: 200px;">
                    <input placeholder="Logout time"
                           style="width: 100%; text-align: center"
                           options="dateRangeOptions"
                           date-range-picker
                           clearable="true"
                           class="form-control date-picker"
                           ng-enter="applyFilters()"
                           ng-class="{ hasFilter: (filters.logoutTime.startDate != null && filters.logoutTime.endDate != '') }"
                           type="text" ng-model="filters.logoutTime" />
                </td>
            </tr>

            <tr ng-if="customers.totalElements == 0 || loading == true">
                <td colspan="6">
                    <span ng-hide="loading">There are no sessions</span>
                    <span ng-show="loading">
                        <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading sessions...
                    </span>
                </td>
            </tr>

            <tr ng-repeat="session in sessions.content">
               <%-- <td class="col-center actions-col">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        &lt;%&ndash;<ul class="dropdown-menu" role="menu">
                            <li><a ui-sref="app.admin.session({sessionId: session.id})">Show Details</a></li>
                        </ul>&ndash;%&gt;
                    </div>
                </td>--%>
                <td style="width:150px; text-align: center">
                    <a ui-sref="app.admin.session({sessionId: session.id})">{{session.login.loginName}}</a>
                </td>
                <td style="text-align: center">{{session.login.person.firstName}}</td>
                <td style="text-align: center">{{session.ipAddress}}</td>
                <td style="text-align: center">{{session.loginTime}}</td>
                <td style="text-align: center">{{session.logoutTime}}</td>
            </tr>
        </tbody>
    </table>
</div>