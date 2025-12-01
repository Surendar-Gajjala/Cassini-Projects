<div style="margin:10px;" class="view-toolbar">
    <button class="btn btn-sm btn-success" ng-click="roleVm.saveRoles()">Save Roles</button>
    <button class="btn btn-sm btn-primary" ng-click="roleVm.addNewRole()">New Role</button>
</div>

<div class="row" ng-show="roleVm.showNewRoleForm">
    <div class="styled-panel grid-form col-lg-4 col-lg-offset-4 col-md-10 col-md-offset-1 col-sm-12 text-center">
        <h3 class="text-center">New Role</h3>
        <br/><br/>

        <div class="row">
            <div class="col-xs-4 text-primary text-right" style="padding-top:10px;">
                Role Name:
            </div>
            <div class="col-xs-8">
                <input type="text" class="form-control" ng-model="roleVm.newRole.name"/>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-xs-4 text-primary text-right">
                Role Description:
            </div>
            <div class="col-xs-8">
                <textarea class="form-control" rows="5" ng-model="roleVm.newRole.description"></textarea>
            </div>
        </div>
        <br/><br/>

        <div class="text-center">
            <button class="btn btn-default mr10" ng-click="roleVm.hideNewRoleForm()">Cancel</button>
            <button class="btn btn-primary" ng-click="roleVm.createNewRole()">Create</button>
        </div>
    </div>
</div>

<div ng-if="roleVm.loading">
    <span style="font-size: 15px;">
        <img src="app/assets/images/loaders/loader9.gif" class="mr5">Loading roles and permissions...
    </span>
</div>

<div class="styled-panel permissions-table" ng-show="roleVm.showGrid && !roleVm.loading">
    <treasure-overlay-spinner active='roleVm.spinner.active'>
        <table>
            <thead>
            <tr>
                <th class="skew">
                    <div class="odd" style="width: auto;"><span>Functions</span></div>
                </th>
                <th class="skew">
                    <div class="" style="width: auto;"><span>Permissions</span></div>
                </th>
                <th class="skew" ng-repeat="role in roleVm.roles">
                    <div ng-class="{'odd': $index %2 != 1}"><span>{{role.name}}</span></div>
                </th>
            </tr>

            </thead>
            <tbody>
            <tr class="odd" style="border-top: 5px solid #D3D3D3;">
                <td></td>
                <td></td>
                <td ng-repeat="role in roleVm.roles">
                    <div class="btn-group" uib-dropdown style="margin-bottom: 0px;">
                        <button type="button" class="btn btn-xs btn-default dropdown-toggle" uib-dropdown-toggle
                                ng-disabled="role.name == 'Administrator'">
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="roles-grid-menu dropdown-menu" role="menu">
                            <li><a href="" style="text-align:left">Remove Role</a></li>
                            <li><a href="" style="text-align:left">Clear Selections</a></li>
                        </ul>
                    </div>
                </td>
            </tr>
            <tr ng-repeat-start="pg in roleVm.permissionGroups" style="border-right: 1px solid #D3D3D3;"
                ng-init="roleVm.permission = pg.permissions[0]">
                <td nowrap class="group odd"
                    rowspan="{{pg.permissions.length}}"
                    style="white-space: nowrap; padding: 10px; text-align: left; font-weight: bold;"
                    ng-class="{'first': roleVm.first, 'last': roleVm.last, 'indent': (pg.group.indexOf('.') != -1)}"
                    ng-init="roleVm.first = true; roleVm.last = $last">
                        <span ng-class="{'plus': (pg.group.indexOf('.') != -1),
                                         'minus': (pg.group.indexOf('.') != -1) && !roleVm.permission.collapsed}"
                              ng-click="roleVm.toggleNode(roleVm.permission, pg.permissions)">
                        </span>
                    {{pg.group.substring(pg.group.indexOf('.')+1)}}
                </td>
                <td nowrap ng-class="{'first': roleVm.first}" class="permission-name">{{roleVm.permission.name}}---</td>
                <td align="center"
                    ng-class="{'odd': $index %2 != 1,
                                'first': roleVm.first,
                                'has-hidden-permissions': (roleVm.permission.collapsed && roleVm.hasHiddenPermissions(role, roleVm.permission, pg.permissions))}"
                    ng-repeat="role in roleVm.roles">
                    <input type="checkbox"
                           style="text-align:center"
                           ng-disabled="role.permissions[roleVm.permIndex].disabled == true"
                           ng-init="roleVm.permIndex = roleVm.getPermissionIndexInRole(role, roleVm.permission);"
                           ng-model="role.permissions[roleVm.permIndex].selected"
                           ng-change="roleVm.toggleRolePermission(role, role.permissions[roleVm.permIndex])"/>
                </td>
            </tr>

            <tr ng-repeat-end ng-repeat="permission in pg.permissions.slice(1)">
                <td nowrap class="permission-name" ng-show="permission.show"
                    ng-init="roleVm.first = false" ng-class="{'last': (roleVm.last && $last)}">
                    <span style="">{{permission.name}}</span>
                </td>
                <td align="center"
                    ng-class="{'odd': $index %2 != 1,
                                   'first': roleVm.first, 'last':  (roleVm.last && $parent.$last)}"
                    ng-show="permission.show" ng-repeat="role in roleVm.roles">
                    <input type="checkbox"
                           style="text-align:center"
                           ng-disabled="role.permissions[roleVm.permIndex].disabled == true"
                           ng-init="roleVm.permIndex = roleVm.getPermissionIndexInRole(role, permission);"
                           ng-model="role.permissions[roleVm.permIndex].selected"
                           ng-change="roleVm.toggleRolePermission(role, role.permissions[roleVm.permIndex])"/>
                </td>
            </tr>
            </tbody>
        </table>
    </treasure-overlay-spinner>
</div>