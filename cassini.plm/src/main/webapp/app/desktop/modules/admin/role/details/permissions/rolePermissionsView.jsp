<div>
    <style scoped>
        .view-details .view-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .view-details .view-header .view-title {
            font-size: 16px;
            font-weight: 600;
        }

        .view-details .view-header .view-summary {
            color: #707d91;
            font-weight: 300;
        }

        .added-column i {
            display: none;
            cursor: pointer;
            margin-left: 5px;
        }

        .view-content {
            position: static;
        }

        .view-content .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 40px;
            top: 80px;
            overflow: auto;
            height: 520px;
        }

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }

        .view-content .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .view-content .table-footer > div h5 {
            margin: 0;
        }

        .view-content .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .view-content .table-footer > div > div > i {
            font-size: 16px;
        }

        .added-column:hover i {
            display: inline-block;
        }

        .scroll-style-1::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
            border-radius: 10px;
            background-color: #F5F5F5;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
            /*background-color: #fff;*/
        }

        .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
            background-color: #d6e1e0;
        }

        .selected-role-permissions table th {
            position: -webkit-sticky;
            position: sticky;
            top: -1px !important;
            z-index: 5 !important;
        }

        .role-permission-table-container {
            position: absolute;
            top: 83px !important;
            bottom: 160px !important;
            left: 0;
            right: 0;
        }

        .role-details .role-card.details-panel {
            width: 100% !important;
            padding: 0 !important;;
            min-height: 428px !important;;
        }
    </style>
    <div class="view-details">
        <div class="view-header d-flex">
            <div style="flex: 1">
                <div class="view-title" translate>ROLE_PERMISSIONS</div>
                <div class="view-summary" translate>ADD_OR_DELETE_PERMISSIONS</div>
            </div>
            <div>
                <div ng-disabled="rolePermissionsVm.objectTypes.length == 0" style="width: 150px;" class="col-md-3">
                    <ui-select ng-model="rolePermissionsVm.objectType"
                               on-select="rolePermissionsVm.loadSubTypePermission()">
                        <ui-select-match placeholder="{{rolePermissionsVm.objectTypeTitle}}" title="{{$select.selected}}">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="objectType in rolePermissionsVm.objectTypes | filter: $select.search" >
                            <div title="{{objectType.length > 15 ? objectType : '' }}" ng-bind="objectType"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
                <div ng-disabled="rolePermissionsVm.subTypes.length == 0" style="width: 150px;" class="col-md-3">
                    <ui-select ng-model="rolePermissionsVm.subType"
                               on-select="rolePermissionsVm.loadFilteredPermission()">
                        <ui-select-match placeholder="{{rolePermissionsVm.subTypeTitle}}">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices repeat="subType in rolePermissionsVm.subTypes | filter: $select.search">
                            <div ng-bind="subType"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="pull-right" style="margin-top: 2px;">
                <button class="btn btn-danger btn-sm" ng-disabled="rolePermissionsVm.clear == false"
                        ng-click="rolePermissionsVm.clearFilter()" translate>CLEAR
                </button>
            </div>
            <div class="header-buttons" style="margin-top: 2px;">
                <button class="btn btn-sm btn-new" ng-click="rolePermissionsVm.addPermission()" translate>ADD</button>
            </div>
        </div>
        <div class="role-permission-table-container" style="padding:0px; height: 84%!important;overflow: auto;">
            <div class="selected-role-permissions">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th class="permission-col-width-200" translate>NAME</th>
                        <th style="width: 150px;" translate>OBJECT_TYPE</th>
                        <th style="width: 150px;" translate>SUB_TYPE</th>
                        <th style="width: 200px;" translate>PRIVILEGE</th>
                        <%--<th style="width: 200px;" translate>ATTRIBUTE_GROUP</th>--%>
                        <%--<th class="permission-col-width-200" translate>ATTRIBUTE</th>--%>
                        <%--<th style="width: 200px;" translate>CRITERIA</th>--%>
                        <th style="width: 200px;" translate>PRIVILEGE_TYPE</th>
                        <th style="width: 80px;text-align: center" class="sticky-col sticky-actions-col" translate>ACTIONS</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="rolePermissionsVm.loading == true">
                        <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_PERMISSIONS</span>
                    </span>
                        </td>
                    </tr>
                    <tr ng-if="rolePermissionsVm.loading == false && rolePermissionsVm.securityPermissions.length == 0">
                        <td colspan="12" style="background-color: white  !important;color: unset !important;">
                            <div class="no-data">
                                <img src="app/assets/no_data_images/Permissions.png" alt="" class="image">

                                <div class="message" translate>NO_PERMISSIONS</div>
                            </div>
                        </td>
                    </tr>
                    <tr ng-repeat="permission in rolePermissionsVm.securityPermissions">
                        <td class="permission-col-width-200">
                            <span ng-bind-html="permission.name"></span>
                        </td>
                        <td style="width: 200px;">
                            <span ng-bind-html="permission.objectType"></span>
                        </td>
                        <td style="width: 200px;">
                            <span ng-bind-html="permission.subType"></span>
                        </td>
                        <td style="width: 200px;"><span ng-bind-html="permission.privilege"></span>
                        </td>
                        <%--<td style="width: 200px;"><span ng-bind-html="permission.attributeGroup"></span></td>
                        <td class="permission-col-width-200"><span ng-bind-html="permission.attribute"></span></td>
                        <td style="width: 200px;">
                             <span style="padding: 20px;" ng-if="permission.criteria != null">
                                        <i class="fa fa-pencil"
                                           ng-click="rolePermissionsVm.viewCriteria(permission)"></i>
                             </span>
                        </td>--%>
                        <td>
                            <privilege-type type="permission.privilegeType"></privilege-type>
                        </td>
                        <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="fa fa-trash" title='Delete'
                              ng-click="rolePermissionsVm.deleteSecurityPermission(permission)">
                        </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>
