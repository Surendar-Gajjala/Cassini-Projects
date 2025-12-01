<div class="view-container">
    <%-- <div class="view-toolbar">
         <button class="btn btn-success btn-sm" ng-click="userVm.newIssue()">New Issue</button>
         <free-text-search on-clear="userVm.resetPage" on-search="userVm.freeTextSearch"></free-text-search>
     </div>
  --%>
    <div class="view-content">
        <div class="pull-right text-center">
                <span ng-if="userVm.loadingRoles == false"><small><span translate>PAGE</span> {{userVm.roles.number+1}}
                    of {{userVm.roles.totalPages}}
                </small></span>
            <br>

            <div class="btn-group" style="margin-bottom: 0">
                <button class="btn btn-xs btn-default"
                        ng-click="userVm.previousRolesPage()"
                        ng-disabled="userVm.roles.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="userVm.nextRolesPage()"
                        ng-disabled="userVm.roles.last">
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>
            <br>
                 <span ng-if="userVm.loadingRoles == false"><small>
                     {{userVm.roles.totalElements}}
                     <span translate>ROLES</span>
                 </small></span>
        </div>
        <div>
            <a href="" class="btn btn-xs btn-danger" ng-if="userVm.clear == true"
               ng-click="userVm.clearFilter()" translate>CLEAR_FILTER</a>
        </div>
        <table class="table table striped">
            <thead>
            <tr>
                <th translate>NAME</th>
                <th translate>DESCRIPTION</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="userVm.loadingRoles == true">
                <td colspan="6">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"><span translate>LOADING_ROLES</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="userVm.loadingRoles == false && userVm.roles.content.length == 0">
                <td colspan="6"><span translate>NO_ROLES</span></td>
            </tr>

            <tr ng-repeat="role in userVm.roles.content">
                <td>{{role.name}}</td>
                <td>{{role.description}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
