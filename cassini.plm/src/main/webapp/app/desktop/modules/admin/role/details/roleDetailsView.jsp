<div>
    <style scoped>
        .role-details {
            margin-top: 100px;
            margin-bottom: 100px;
        }

        .role-details .role-card {
            position: relative;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 20px;
        }

        .role-details .role-card.role-info {
            width: 300px;
            max-height: 600px;
            margin-right: 30px;
        }

        .role-details .role-card.details-panel {
            width: calc(100% - 320px);
            padding: 0;
            min-height: 600px;
        }

        .role-details .role-card.role-info .profile-pic-btn i {
            font-size: 14px;
        }

        .role-card .label-name {
            font-size: 16px;
            font-weight: 600 !important;
        }

        .role-card .account-details {
            height: 155px;
            padding: 20px;
            border-bottom: 1px solid #eee;
            margin-bottom: 20px;
        }

        .role-card .account-details div {
            padding: 5px;
            margin-bottom: 5px;
            color: #707d91;
        }

        .role-card .account-details div i {
            margin-right: 5px;
        }

        .role-card .role-actions .action-btn {
            display: flex;
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;
            padding: 10px;
            cursor: pointer;
            margin-bottom: 10px;
        }

        .role-card .role-actions .action-btn.admin-btn {
            color: #1bc5bd;
            border-color: transparent;
        }

        .role-card .role-actions .action-btn.admin-btn:hover {
            color: #1bc5bd;
            background-color: #c9f7f5;
        }

        .role-card .role-actions .action-btn i {
            margin-right: 5px;
        }

        .role-card .role-actions .action-btn.active {
            background-color: #f3f6f9;
            color: #3699ff;
            transition: all .15s ease;
            border-radius: 3px;
        }

        .role-card .role-actions .action-btn:hover {
            background-color: #f3f6f9;
            color: #3699ff;
            border-radius: 3px;
        }

        .view-details .view-header .header-buttons {
            width: 100px;
            text-align: right;
            margin-top: 5px;
        }

        .view-details .view-header .header-buttons .btn-new {
            background-color: #3699ff;
            border-color: #3699ff;
            min-width: 80px;
            color: #fff;
        }

        .view-details .view-header .header-buttons .btn-new:hover {
            background-color: #187de4;
            border-color: #187de4;
        }

        /*.container {
            width: 1450px !important;
        }*/

    </style>
    <div class="view-toolbar">

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="roleDetailsVm.gotoAdmin()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
    </div>
    <div class="container">
        <div class="role-details d-flex">
            <div class="role-card role-info">
                <div class="ribbon ribbon-blue" ng-if="personGroup.external"><span>EXTERNAL</span></div>
                <div class="d-flex align-items-center" style="border-bottom: 1px solid #eee;margin-bottom: 20px;">
                    <div class="d-flex flex-column" style="margin-left: 20px;padding-bottom: 20px;">
                        <div class="label-name">{{personGroup.name}}</div>
                        <div class="label-role">{{personGroup.description}}</div>
                    </div>
                </div>
                <div class="role-actions">
                    <div class="action-btn" ng-class="{'active': roleDetailsVm.details == 'basic'}"
                         ng-click="roleDetailsVm.showBasicRoleDetails()">
                        <i class="las la-stream"></i>
                        <span translate>BASIC_DETAILS</span>
                    </div>

                    <div class="action-btn" ng-class="{'active': roleDetailsVm.details == 'users'}"
                         ng-click="roleDetailsVm.showRoleUsers()">
                        <i class="las la-user"></i>
                        <span translate>USERS</span>
                    </div>
                    <div class="action-btn" ng-class="{'active': roleDetailsVm.details == 'permissions'}"
                         ng-click="roleDetailsVm.showRolePermissions()">
                        <i class="las la-user"></i>
                        <span translate>PERMISSIONS</span>
                    </div>
                    <div class="action-btn admin-btn" ng-if="loginPersonDetails.isSuperUser"
                         ng-click="roleDetailsVm.gotoAdmin()">
                        <i class="las la-user-shield"></i>
                        <span translate>BACK_TO_ADMIN</span>
                    </div>
                </div>
            </div>
            <div class="role-card details-panel" ui-view></div>
        </div>
    </div>
</div>