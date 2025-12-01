<div class="responsive-table scroll-style-1" style="padding: 10px;">
    <table class="table table-striped highlight-row" style="table-layout:fixed;">
        <thead>
        <tr>
            <th translate>NAME</th>
            <th style="width: 200px;" translate>OBJECT_TYPE</th>
            <th style="width: 200px;" translate>SUB_TYPE</th>
            <th style="width: 150px;" translate>PRIVILEGE</th>
            <%--<th style="width: 200px;" translate>ATTRIBUTE_GROUP</th>--%>
            <%--<th style="width: 300px;" translate>ATTRIBUTE</th>--%>
            <%--<th style="width: 200px;" translate>CRITERIA</th>--%>
            <th style="width: 150px;" translate>PRIVILEGE_TYPE</th>
            <th style="width: 80px;text-align: center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="allPermissionsVm.loading == true">
            <td colspan="12">
                                <span style="font-size: 15px;">
                                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                         class="mr5"><span translate>LOADING_PERMISSIONS</span>
                                </span>
            </td>
        </tr>
        <tr ng-if="allPermissionsVm.loading == false && allPermissionsVm.securityPermissions.content.length == 0">
            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Permissions.png" alt="" class="image">

                    <div class="message" translate>NO_PERMISSIONS</div>
                </div>
            </td>
        </tr>

        <tr ng-repeat="permission in allPermissionsVm.securityPermissions.content">
            <td class="permission-col-width-200">
                <span ng-bind-html="permission.name | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 200px;">
                <span ng-bind-html="permission.objectType | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 200px;">
                <span ng-bind-html="permission.subType | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 150px;">
                <span ng-bind-html="permission.privilege | highlightText: freeTextQuery"></span>
            </td>
            <%--<td style="width: 200px;">
                <span ng-bind-html="permission.attributeGroup | highlightText: freeTextQuery"></span>
            </td>
            <td class="permission-col-width-200">
                <span ng-bind-html="permission.attribute | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 100px;padding-top: 5px !important;">
                <span style="padding: 20px;">
                    <i class="fa fa-pencil" ng-if="permission.criteria != null" ng-click="allPermissionsVm.viewCriteria(permission)"></i>
                </span>
            </td>--%>
            <td style="width: 150px !important;padding-top: 5px !important;">
                <privilege-type type="permission.privilegeType"></privilege-type>
            </td>
            <td style="width: 80px  !important;padding-top: 8px !important;text-align: center">
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-cog dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li title="Edit Permission">
                            <a ng-click="allPermissionsVm.editSecurityPermission(permission)"
                               translate>
                                EDIT
                            </a>
                        </li>
                        <li title="{{permission.usedPermission ? cannotDeleteAddedPermission : ''}}">
                            <a ng-click="allPermissionsVm.deleteSecurityPermission(permission)"
                               ng-class="{'disabled': permission.usedPermission == true}"
                               translate>
                                DELETE
                            </a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="table-footer">
    <table-footer objects="allPermissionsVm.securityPermissions"
                  pageable="allPermissionsVm.pageable"
                  previous-page="allPermissionsVm.previousPage"
                  next-page="allPermissionsVm.nextPage"
                  page-size="allPermissionsVm.pageSize"></table-footer>
</div>