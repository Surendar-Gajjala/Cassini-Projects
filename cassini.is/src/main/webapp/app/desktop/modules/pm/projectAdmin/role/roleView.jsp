<style>
    .responsive-table .dropdown-content {
        display: none;
        position: absolute;
        background-color: #fff !important;
        min-width: 100px;
        box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
        z-index: 1 !important;
        border-radius: 5px;
        text-align: left;
        color: black !important;
        margin-top: -30px;

    }

    .responsive-table .dropdown-content a {
        text-decoration: none;
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content a:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    .responsive-table .dropdown-content i:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    .responsive-table .dropdown:hover .dropdown-content {
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content {
        margin-left: -35px !important;
    }

    table .ui-select-choices {
        position: absolute !important;
        top: auto !important;
        left: auto !important;
        width: auto !important;
    }

</style>
<div class="responsive-table" style="overflow-y: auto;height: 100%;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th>Role</th>
            <th>Description</th>
            <th style="width: 150px;" class="text-center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="sitesVm.loading == true">
            <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Roles...
                        </span>
            </td>
        </tr>

        <tr ng-if="roleVm.loading == false && roleVm.projectRoles.content.length == 0">
            <td colspan="11">No Roles are available to view</td>
        </tr>

        <tr ng-repeat="ProjectRole in roleVm.projectRoles.content">
            <%--<td>{{ProjectRole.role}}</td>--%>
            <td title="{{ProjectRole.role}}">
                <span ng-if="ProjectRole.editMode == false">{{ProjectRole.role | limitTo: 15}}{{ProjectRole.role.length > 15 ? '...' : ''}}</span>
                <input ng-if="ProjectRole.editMode == true" type=" text" class="form-control input-sm"
                       style="width : 250px"
                       ng-model="ProjectRole.newRole">
            </td>


            <td title="{{ProjectRole.description}}">
                <span ng-if="ProjectRole.editMode == false">{{ProjectRole.description | limitTo: 15}}{{ProjectRole.description.length > 15 ? '...' : ''}}</span>
                <input ng-if="ProjectRole.editMode == true" type=" text" class="form-control input-sm"
                       style="width : 250px"
                       ng-model="ProjectRole.newDescription">
            </td>


            <td class="text-center" style="text-align: center;">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="roleVm.applyChanges(ProjectRole)">
                            <a class="dropdown-item" title="Apply Changes"
                               ng-if="(ProjectRole.editMode == true && ProjectRole.showValues == false)"
                                    >
                                <span style="padding-left: 3px;">Save</span>
                            </a></li>
                        <li ng-click="roleVm.cancelChanges(ProjectRole)">
                            <a class="dropdown-item" type="button"
                               ng-if="(ProjectRole.editMode == true && ProjectRole.showValues == false)"
                               title="Cancel Changes">
                                <span style="padding-left: 3px;">Cancel</span>
                            </a>
                        </li>
                        <li ng-click="roleVm.editProjectRole(ProjectRole)"
                            ng-if="(ProjectRole.showValues == true || ProjectRole.editMode == false)">
                            <a title="Edit this role" type="button"
                               class="dropdown-item"
                               ng-disabled="(!login.person.isProjectOwner || selectedProject.locked == true) || !hasPermission('permission.team.editRole')">
                                <span style="padding-left: 3px;">Edit</span>
                            </a>
                        </li>
                        <li ng-click="roleVm.deleteProjectRole(ProjectRole)"
                            ng-if="(ProjectRole.showValues == true || ProjectRole.editMode == false)">
                            <a title="Delete this role"
                               type="button"
                               class="dropdown-item"
                               ng-disabled="(!login.person.isProjectOwner || selectedProject.locked == true) || !hasPermission('permission.team.deleteRole')">
                                <span style="padding-left: 3px;">Delete</span>
                            </a>
                        </li>
                    </ul>
                                    </span>
            </td>
        </tr>
        </tbody>

    </table>
</div>

