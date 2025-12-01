<style>
    .table-fixed {

    }

    .table-fixed thead {
        position: sticky !important;
        position: -webkit-sticky !important;
        top: 0 !important;
        z-index: 999 !important;
    }

    .table-fixed thead th {
        position: sticky !important;
        position: -webkit-sticky !important;
        top: 0 !important;
        z-index: 999 !important;
    }

    .color1 {
        font-size: 16px;
        font-weight: bold;
    }

    .color2 {
        font-size: 16px;
        font-weight: bolder;
        width: 70px !important;
        color: orange !important;
    }

    .color3 {
        font-size: 16px;
        font-weight: bolder;
        width: 60px !important;
        color: blue !important;
    }

    .color4 {
        font-size: 16px;
        font-weight: bolder;
        width: 50px !important;
        color: green !important;
    }

    .styled-panel {
        padding: 0px !important;
    }
</style>
<div id="admin-rightView" class="styled-panel permissions-table" ng-show="userVm.showPermissionGrid"
     style="overflow: auto;">
    <table class="table-fixed">
        <thead style="background-color: #fff">
        <tr>
            <th class="skew" style="background-color: white;">
                <div class="odd" style="width: auto;"><span>Functions</span></div>
            </th>
            <th class="skew">
                <div class="odd" style="width: auto;"><span>Permissions</span></div>
            </th>
            <th class="skew" ng-repeat="group in userVm.excludeExternalGroup">
                <div ng-class="{'odd': $index %2 != 1 || $index %2 == 1,'color1':group.level == 0,'color2':group.level == 1,
                                'color3':group.level == 2,'color4':group.level == 3}">
                    <span ng-if="group.groupId == 5" style="bottom: 5px;">{{group.name}}</span>
                    <span ng-if="group.groupId != 5">{{group.name}}</span>
                </div>
            </th>
        </tr>
        </thead>
        <tbody>
        <%--<tr class="odd" style="border-top: 5px solid #D3D3D3;">
            <td></td>
            <td></td>
            <td ng-repeat="group in userVm.groups">
                <div class="btn-group" uib-dropdown style="margin-bottom: 0px;">
                    <button type="button" class="btn btn-xs btn-default dropdown-toggle" uib-dropdown-toggle
                            ng-disabled="group.name == 'Administrator'">
                        <i class="fa fa-cog fa-fw"></i></span>
                    </button>
                    <ul class="roles-grid-menu dropdown-menu" role="menu">
                        <li><a href="" style="text-align:left" ng-click="userVm.deleteGroup(group)">Remove
                            Group</a></li>
                        <li><a href="" style="text-align:left" ng-click="userVm.clearGroupPermissions()">Clear
                            Selections</a></li>
                    </ul>
                </div>
            </td>
        </tr>--%>

        <tr ng-repeat-start="pg in userVm.permissionGroups" style="border-top: 5px solid #D3D3D3;"
            ng-init="permission = pg.permissions[0]">
            <td nowrap class="group odd"
                rowspan="{{pg.permissions.length}}"
                style="white-space: nowrap; padding: 10px; text-align: left; font-weight: bold;"
                class="first"
                ng-class="{'first': userVm.first, 'last': userVm.last, 'indent': (pg.group.indexOf('.') != -1)}"
                ng-init="userVm.first = true; userVm.last = $last">
                <span ng-class="{'plus': (pg.group.indexOf('.') != -1),
                                 'minus': (pg.group.indexOf('.') != -1) && !permission.collapsed}"
                      ng-click="userVm.toggleNode(permission, pg.permissions)">
                </span>
                {{pg.group.substring(pg.group.indexOf('.')+1)}}
            </td>
            <td nowrap ng-class="{'first': userVm.first}" class="permission-name">{{permission.name}}</td>
            <td align="center"
                ng-class="{'odd': $index %2 != 1,
                                'first': userVm.first,
                                'has-hidden-permissions': (permission.collapsed && userVm.hasHiddenPermissions(group, permission, pg.permissions))}"
                ng-repeat="group in userVm.excludeExternalGroup">
                <input type="checkbox"
                       style="text-align:center"
                       ng-init="permIndex = userVm.getPermissionIndexInGroup(group, permission);"
                       ng-disabled="group.permissions[permIndex].disabled == true || !hasPermission('permission.admin.permissions')"
                       ng-model="group.permissions[permIndex].selected"
                       ng-change="userVm.toggleRolePermission(group, group.permissions[permIndex])"/>
            </td>
        </tr>

        <tr ng-repeat-end ng-repeat="permission in pg.permissions.slice(1)">
            <td nowrap class="permission-name" ng-show="permission.show"
                ng-init="userVm.first = false" ng-class="{'last': (userVm.last && $last)}">
                <span style="">{{permission.name}}</span>
            </td>
            <td align="center"
                ng-class="{'odd': $index %2 != 1,
                                   'first': userVm.first, 'last':  (userVm.last && $parent.$last)}"
                ng-show="permission.show" ng-repeat="group in userVm.excludeExternalGroup">
                <input type="checkbox"
                       style="text-align:center"
                       ng-init="permIndex = userVm.getPermissionIndexInGroup(group, permission);"
                       ng-disabled="group.permissions[permIndex].disabled == true || !hasPermission('permission.admin.permissions')"
                       ng-model="group.permissions[permIndex].selected"
                       ng-change="userVm.toggleRolePermission(group, group.permissions[permIndex])"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>