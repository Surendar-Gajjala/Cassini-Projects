<div>
    <style scoped>

        .roles-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(350px, max-content));
            grid-gap: 20px;
            justify-content: center;
            padding: 30px 30px 50px 30px;
            margin: 0 100px;
        }

        @media screen and (max-width: 1375px) {
            .roles-container {
                margin: 0 50px;
            }
        }

        .roles-container .role-card {
            position: relative;
            height: 180px;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 30px;
            cursor: pointer;
        }

        .roles-container .role-card:hover {
            box-shadow: 0 7px 14px rgba(147, 148, 150, 0.25),
            0 5px 5px rgba(177, 177, 179, 0.22);
            transition: all .2s ease;
            transform: translate3D(0, -1px, 0) scale(1.01);
        }

        .d-flex {
            display: flex !important;
        }

        .flex-column {
            flex-direction: column;
        }

        .align-items-center {
            -webkit-box-align: center !important;
            -ms-flex-align: center !important;
            align-items: center !important;
        }

        .role-card .card-view .role-name {
            font-size: 16px;
            font-weight: 600 !important;
        }

        .role-card .delete-role-icon {
            position: absolute;
            top: -12px;
            right: -8px;
            font-size: 22px;
            color: red;
            visibility: hidden;
        }

        .role-card:hover .delete-role-icon {
            visibility: inherit;
        }

        .role-card .card-view .role-description {
            color: #707d91 !important;
        }

        .role-card .card-view .role-users {
            padding: 20px 0 0 30px;
            display: flex;
            height: 100px;
            margin-top: 15px;
        }

        .role-card .card-view .role-users .role-avatar {
            position: relative;
            border-radius: 50%;
            margin-left: -25px;
            z-index: 1;
            height: 50px;
            width: 50px;
            background-color: #e1f0ff;
            color: #3699ff;
            display: inline-flex;
            border: 2px solid #fff;
            align-items: center;
            justify-content: center;
        }

        .role-card .card-view .role-users .role-avatar:hover {
            z-index: 100 !important;
        }

        .role-card .card-view .role-users .more-roles {
            background-color: #f7ecec;
            color: #ed55b2;
        }

        .role-card .card-view .role-users .no-roles {
            font-size: 12px;
            line-height: 12px;
            text-align: center;
            border: 1px solid #e1f0ff;
        }

        .role-table .role-users {
            display: flex;
            padding-left: 25px;
        }

        .role-table .role-users .role-avatar {
            position: relative;
            border-radius: 50%;
            margin-left: -25px;
            z-index: 1;
            height: 50px;
            width: 50px;
            background-color: #e1f0ff;
            color: #3699ff;
            display: inline-flex;
            border: 2px solid #fff;
            align-items: center;
            justify-content: center;
        }

        .role-table .role-users .role-avatar:hover {
            z-index: 100 !important;
        }

        .role-table .role-users .more-roles {
            background-color: #f7ecec;
            color: #ed55b2;
        }

        .role-table .role-users .no-roles {
            font-size: 12px;
            line-height: 12px;
            text-align: center;
            border: 1px solid #e1f0ff;
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

    </style>

    <div class="roles-container" ng-show="adminVm.viewType == 'cards'">
        <div class="role-card" ng-repeat="role in allRolesVm.roles.content">
            <i class="la la-times-circle delete-role-icon" title="{{deleteRoleTitle}}"
               ng-click="allRolesVm.deleteRole(role)"
               ng-if="loginPersonDetails.isAdmin"></i>

            <div class="card-view" ng-click="allRolesVm.showRoleDetails(role)">
                <div class="ribbon ribbon-blue" ng-if="role.external"><span>EXTERNAL</span></div>
                <div class="d-flex align-items-center">
                    <div class="d-flex flex-column">
                        <div class="role-name">
                            <span ng-bind-html="role.name | highlightText: allRolesVm.filters.searchQuery"></span>
                        </div>
                        <div class="role-description">
                            <span ng-bind-html="role.description | highlightText: allRolesVm.filters.searchQuery"></span>
                        </div>
                    </div>
                </div>
                <div class="role-users">
                    <div class="role-avatar" title="{{groupMember.person.fullName}}" ng-if="$index < 6"
                         ng-repeat="groupMember in role.groupMembers">
                        <span ng-if="!groupMember.person.hasImage">{{groupMember.imageWord}}</span>
                        <img ng-if="groupMember.person.hasImage" ng-src="{{groupMember.person.personImage}}"
                             style="height: 50px;width: 50px;border-radius: 50%">
                    </div>
                    <div class="role-avatar more-roles" ng-if="role.groupMembers.length > 6">
                        {{role.groupMembers.length - 6}}+
                    </div>
                    <div class="role-avatar no-roles d-flex align-items-center" ng-if="role.groupMembers.length == 0">
                        No Users
                    </div>
                </div>
            </div>
        </div>
        <div ng-if="allRolesVm.noResults == true">
            <div class="no-data">
                <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                <div class="message">{{ 'NO_SEARCH_RESULT_FOUND' | translate}}</div>
                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE
                </div>
            </div>
        </div>
    </div>
    <div class="responsive-table" ng-show="adminVm.viewType == 'table'">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th></th>
                <th translate>ROLE_NAME</th>
                <th translate>DESCRIPTION</th>
                <th translate>USERS</th>
                <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                    ACTIONS
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="allRolesVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ROLES</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="allRolesVm.loading == false && allRolesVm.roles.content.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                        <div class="message">{{ 'NO_SEARCH_RESULT_FOUND' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>

            <tr ng-repeat="role in allRolesVm.roles.content">
                <td style="vertical-align: middle !important;width: 85px;">
                    <span class="label label-primary" ng-if="role.external">EXTERNAL</span>
                </td>
                <td style="vertical-align: middle !important;" class="col-width-250">
                    <a href="" ng-click="allRolesVm.showRoleDetails(role)" title="{{clickToShowDetails}}">
                        <span ng-bind-html="role.name | highlightText: allRolesVm.filters.searchQuery"></span>
                    </a>
                </td>
                <td style="vertical-align: middle !important;">
                    <span ng-bind-html="role.description  | highlightText: allRolesVm.filters.searchQuery"></span>
                </td>
                <td style="vertical-align: middle !important;" class="role-table">
                    <div class="role-users">
                        <div class="role-avatar" title="{{groupMember.person.fullName}}" ng-if="$index < 6"
                             ng-repeat="groupMember in role.groupMembers">
                            <span ng-if="!groupMember.person.hasImage">{{groupMember.imageWord}}</span>
                            <img ng-if="groupMember.person.hasImage" ng-src="{{groupMember.person.personImage}}"
                                 style="height: 50px;width: 50px;border-radius: 50%">
                        </div>
                        <div class="role-avatar more-roles" ng-if="role.groupMembers.length > 6">
                            {{role.groupMembers.length - 6}}+
                        </div>
                        <div class="role-avatar no-roles d-flex align-items-center"
                             ng-if="role.groupMembers.length == 0">
                            No Users
                        </div>
                    </div>
                </td>
                <td class="text-center actions-col sticky-col sticky-actions-col"
                    style="vertical-align: middle !important;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href="" ng-click="allRolesVm.deleteRole(role)" translate>DELETE_ROLE</a>
                                </li>
                            </ul>
                     </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="table-footer" style="position: fixed;bottom: 30px !important;background-color: #f9fbfe">
        <table-footer objects="allRolesVm.roles" pageable="allRolesVm.pageable"
                      previous-page="allRolesVm.previousPage"
                      next-page="allRolesVm.nextPage" page-size="allRolesVm.pageSize"></table-footer>
    </div>
</div>
