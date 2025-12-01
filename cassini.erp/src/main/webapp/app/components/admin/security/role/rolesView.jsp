<script type="text/ng-template" id="roles-view-tb">
    <div>
        <button class="btn btn-sm btn-success" ng-click="saveRoles()">Save Roles</button>
        <button class="btn btn-sm btn-primary" ng-click="addNewRole()">New Role</button>
    </div>
</script>

<div class="row" ng-show="showNewRoleForm">
    <div class="styled-panel grid-form col-lg-4 col-lg-offset-4 col-md-10 col-md-offset-1 col-sm-12 text-center">
        <h3 class="text-center">New Role</h3>
        <br/><br/>

        <div class="row">
            <div class="col-xs-4 text-primary text-right" style="padding-top:10px;">
                Role Name:
            </div>
            <div class="col-xs-8">
                <input type="text" class="form-control" ng-model="newRole.name"/>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-4 text-primary text-right">
                Role Description:
            </div>
            <div class="col-xs-8">
                <textarea class="form-control" rows="5" ng-model="newRole.description"></textarea>
            </div>
        </div>
        <br/><br/>

        <div class="text-center">
            <button class="btn btn-default mr10" ng-click="hideNewRoleForm()">Cancel</button>
            <button class="btn btn-primary" ng-click="createNewRole()">Create</button>
        </div>
    </div>
</div>

<div ng-if="loading">
    <span style="font-size: 15px;">
        <img src="app/assets/images/loaders/loader9.gif" class="mr5">Loading roles and permissions...
    </span>
</div>

<div class="styled-panel permissions-table" ng-show="showGrid && !loading">
    <treasure-overlay-spinner active='spinner.active'>
        <table>
            <thead>
            <tr>
                <th class="skew">
                    <div class="odd" style="width: auto;"><span>Functions</span></div>
                </th>
                <th class="skew">
                    <div class="" style="width: auto;"><span>Permissions</span></div>
                </th>
                <th class="skew" ng-repeat="role in roles">
                    <div ng-class="{'odd': $index %2 != 1}"><span>{{role.name}}</span></div>
                </th>
            </tr>

            </thead>
            <tbody>
            <tr class="odd" style="border-top: 5px solid #D3D3D3;">
                <td></td>
                <td></td>
                <td ng-repeat="role in roles">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;">
                        <button type="button" class="btn btn-xs btn-default dropdown-toggle" dropdown-toggle
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
            <tr ng-repeat-start="pg in permissionGroups" style="border-right: 1px solid #D3D3D3;"
                ng-init="permission = pg.permissions[0]">
                <td nowrap class="group odd"
                    rowspan="{{pg.permissions.length}}"
                    style="white-space: nowrap; padding: 10px; text-align: left; font-weight: bold;"
                    ng-class="{'first': first, 'last': last, 'indent': (pg.group.indexOf('.') != -1)}"
                    ng-init="first = true; last = $last">
                        <span ng-class="{'plus': (pg.group.indexOf('.') != -1),
                                         'minus': (pg.group.indexOf('.') != -1) && !permission.collapsed}"
                              ng-click="toggleNode(permission, pg.permissions)">
                        </span>
                        {{pg.group.substring(pg.group.indexOf('.')+1)}}
                </td>
                <td nowrap ng-class="{'first': first}" class="permission-name">{{permission.name}}</td>
                <td align="center"
                    ng-class="{'odd': $index %2 != 1,
                                'first': first,
                                'has-hidden-permissions': (permission.collapsed && hasHiddenPermissions(role, permission, pg.permissions))}"
                    ng-repeat="role in roles">
                        <input type="checkbox"
                               style="text-align:center"
                                ng-disabled="role.permissions[permIndex].disabled == true"
                                ng-init="permIndex = getPermissionIndexInRole(role, permission);"
                                ng-model="role.permissions[permIndex].selected"
                                ng-change="toggleRolePermission(role, role.permissions[permIndex])"/>
                </td>
            </tr>

            <tr ng-repeat-end ng-repeat="permission in pg.permissions.slice(1)">
                <td nowrap class="permission-name" ng-show="permission.show"
                    ng-init="first = false" ng-class="{'last': (last && $last)}">
                    <span style="">{{permission.name}}</span>
                </td>
                <td align="center"
                    ng-class="{'odd': $index %2 != 1,
                                   'first': first, 'last':  (last && $parent.$last)}"
                    ng-show="permission.show" ng-repeat="role in roles">
                    <input type="checkbox"
                           style="text-align:center"
                           ng-disabled="role.permissions[permIndex].disabled == true"
                           ng-init="permIndex = getPermissionIndexInRole(role, permission);"
                           ng-model="role.permissions[permIndex].selected"
                           ng-change="toggleRolePermission(role, role.permissions[permIndex])"/>
                </td>
            </tr>
            </tbody>
        </table>
    </treasure-overlay-spinner>
</div>