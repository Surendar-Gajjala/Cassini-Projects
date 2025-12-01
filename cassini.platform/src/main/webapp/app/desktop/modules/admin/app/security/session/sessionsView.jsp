<div class="row" style="padding-top: 25px">
    <div class="col-md-12"  style="text-align: right;padding: 0 20px 0 20px;" ng-show="sessionVm.sessions.numberOfElements > 0">
        <div style="text-align: right;">
            <pagination total-items="sessionVm.sessions.totalElements"
                        items-per-page="sessionVm.pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="sessionVm.pageable.page"
                        ng-change="sessionVm.pageChanged()">
            </pagination>
        </div>

        <div>
            <small>Total {{sessionVm.sessions.totalElements}} sessions</small>
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
                <table-filter align="center" placeholder="Login" filters="sessionVm.filters" property="login" apply-filters="sessionVm.applyFilters()" />
            </td>
            <td>
                <table-filter align="center" placeholder="User" filters="sessionVm.filters" property="user" apply-filters="sessionVm.applyFilters()" />
            </td>
            <td style="width: 200px;">
                <table-filter align="center" placeholder="IP Address" filters="sessionVm.filters" property="ipAddress" apply-filters="sessionVm.applyFilters()" />
            </td>
            <td style="width: 200px">
            <input placeholder="Login time"
                   style="width: 100%; text-align: center"
                   options="sessionVm.dateRangeOptions"
                   date-range-picker
                   clearable="true"
                   class="form-control date-picker"
                   ng-enter="sessionVm.applyFilters()"
                   ng-class="{ hasFilter: (sessionVm.filters.loginTime.startDate != null && sessionVm.filters.loginTime.endDate != '') }"
                   type="text" ng-model="sessionVm.filters.loginTime" />
            </td>
            <td style="width: 200px;">
                <input placeholder="Logout time"
                       style="width: 100%; text-align: center"
                       options="sessionVm.dateRangeOptions"
                       date-range-picker
                       clearable="true"
                       class="form-control date-picker"
                       ng-enter="sessionVm.applyFilters()"
                       ng-class="{ hasFilter: (sessionVm.filters.logoutTime.startDate != null && sessionVm.filters.logoutTime.endDate != '') }"
                       type="text" ng-model="sessionVm.filters.logoutTime" />
            </td>
        </tr>

        <tr ng-if="sessionVm.customers.totalElements == 0 || sessionVm.loading == true">
            <td colspan="6">
                <span ng-hide="sessionVm.loading">There are no sessions</span>
                    <span ng-show="sessionVm.loading">
                        <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading sessions...
                    </span>
            </td>
        </tr>

        <tr ng-repeat="session in sessionVm.sessions.content">
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