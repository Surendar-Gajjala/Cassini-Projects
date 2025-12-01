<div>
    <style scoped>
        .user-details {
            margin-top: 100px;
            margin-bottom: 100px;
        }

        .user-details .user-card {
            position: relative;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 20px;
        }

        .user-details .user-card.user-info {
            width: 300px;
            max-height: 600px;
            margin-right: 30px;
        }

        .user-details .user-card.details-panel {
            width: calc(100% - 320px);
            padding: 0;
            min-height: 600px;
        }

        .user-details .user-card.user-info .profile-pic-btn {
            height: 24px;
            width: 24px;
            margin-left: -40px;
            margin-top: -60px;
            padding-left: 5px;
            background-color: #fff;
            border-radius: 50%;
            box-shadow: 0 9px 16px 0 rgba(24, 28, 50, .25) !important;
            cursor: pointer;
        }

        .user-details .user-card.user-info .profile-remove-btn {
            height: 24px;
            width: 24px;
            /*margin-left: -40px;*/
            margin-bottom: -60px;
            padding-left: 5px;
            background-color: #fff;
            border-radius: 50%;
            box-shadow: 0 9px 16px 0 rgba(24, 28, 50, .25) !important;
            cursor: pointer;
            position: absolute;
        }

        .user-details .user-card.user-info .profile-pic-btn i {
            font-size: 14px;
        }

        .user-details .user-card.user-info .profile-remove-btn i {
            font-size: 14px;
        }

        .user-details .user-card .user-image {
            width: 75px;
            height: 75px;
            border-radius: 50%;
            background-color: #e1f0ff;
            color: #3699ff;
            margin-right: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }

        .user-card .label-name {
            font-size: 16px;
            font-weight: 600 !important;
        }

        .user-card .label-role {
            color: #707d91 !important;
        }

        .user-card .account-details {
            height: 155px;
            padding: 20px;
            border-bottom: 1px solid #eee;
            margin-bottom: 20px;
        }

        .user-card .account-details div {
            padding: 5px;
            margin-bottom: 5px;
            color: #707d91;
        }

        .user-card .account-details div i {
            margin-right: 5px;
        }

        .user-card .user-actions .action-btn {
            display: flex;
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;
            padding: 10px;
            cursor: pointer;
            margin-bottom: 10px;
        }

        .user-card .user-actions .action-btn.admin-btn {
            color: #1bc5bd;
            border-color: transparent;
        }

        .user-card .user-actions .action-btn.admin-btn:hover {
            color: #1bc5bd;
            background-color: #c9f7f5;
        }

        .user-card .user-actions .action-btn i {
            margin-right: 5px;
        }

        .user-card .user-actions .action-btn.active {
            background-color: #f3f6f9;
            color: #3699ff;
            transition: all .15s ease;
            border-radius: 3px;
        }

        .user-card .user-actions .action-btn:hover {
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

    </style>

    <div class="view-toolbar">

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-if="hasPermission('admin','all') || hasPermission('admin','view')" ng-click="userDetailsVm.gotoAdmin()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
    </div>
    <div class="container">

        <div class="user-details d-flex">
            <div class="user-card user-info">
                <div class="ribbon ribbon-green" ng-if="loginDetails.isSuperUser"><span>ADMIN</span></div>
                <div class="ribbon ribbon-blue" ng-if="loginDetails.external"><span>EXTERNAL</span></div>
                <div class="ribbon ribbon-red" ng-if="!loginDetails.isActive"><span>INACTIVE</span></div>
                <div class="d-flex align-items-center">
                    <div class="user-image">
                        <span ng-if="!loginDetails.person.hasImage">{{loginDetails.imageWord}}</span>
                        <img ng-if="loginDetails.person.hasImage" ng-src="{{userDetailsVm.personImage}}"
                             ng-click="userDetailsVm.showPhotoPreview()" title="View photo"
                             style="height: 75px;width: 75px;border-radius: 50%;cursor: pointer;">
                    </div>
                    <div class="profile-pic-btn"
                         ng-if="loginDetails.id == loginPersonDetails.id">
                        <a href="" class="la la-pen"
                           onclick="$('#userImage').click()"
                           title="{{addImage}}">
                        </a>
                        <input style="display: none"
                               id="userImage"
                               type="file" ng-file-model="userDetailsVm.image" accept="image/*"
                               onchange="angular.element(this).scope().saveUserImage(this.files[0])">
                    </div>
                    <div ng-if="loginDetails.person.hasImage && loginDetails.id == loginPersonDetails.id"
                         class="profile-remove-btn" title="Remove image">
                        <a href="" class="la la-trash"
                           ng-click="userDetailsVm.removeProfilePicture()">
                        </a>
                    </div>
                    <div class="d-flex flex-column" style="margin-left: 20px;">
                        <div class="label-name">{{loginDetails.person.fullName}}</div>
                        <div class="label-role">{{loginDetails.person.defaultGroupObject.name}}</div>
                    </div>
                    <div id="item-thumbnail-basic{{loginDetails.id}}" class="item-thumbnail modal">
                        <div class="item-thumbnail-content">
                            <div class="thumbnail-content" style="display: flex;width: 100%;">
                                <div class="thumbnail-view" id="thumbnail-view-basic{{loginDetails.id}}">
                                    <div id="thumbnail-image-basic{{loginDetails.id}}"
                                         style="display: table-cell;vertical-align: middle;text-align: center;">
                                        <img ng-src="{{userDetailsVm.personImage}}"
                                             style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{loginDetails.id}}"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="account-details">
                    <div>
                        <i class="las la-user"></i>
                        <span>{{loginDetails.loginName}}</span>
                    </div>
                    <div>
                        <i class="las la-envelope"></i>
                        <span style="word-break: break-all">{{loginDetails.person.email}}</span>
                    </div>
                    <div>
                        <i class="las la-phone"></i>
                        <span>{{loginDetails.person.phoneMobile}}</span>
                    </div>
                </div>

                <div class="user-actions">
                    <div class="action-btn" ng-class="{'active': userDetailsVm.details == 'activity'}"
                         ng-click="userDetailsVm.showUserActivity()">
                        <i class="las la-stream"></i>
                        <span translate>USER_ACTIVITY</span>
                    </div>

                    <div class="action-btn" ng-class="{'active': userDetailsVm.details == 'account'}"
                         ng-click="userDetailsVm.showAccountInfo()">
                        <i class="las la-user"></i>
                        <span translate>ACCOUNT_INFORMATION</span>
                    </div>

                    <div class="action-btn" ng-class="{'active': userDetailsVm.details == 'password'}"
                         ng-click="userDetailsVm.changePassword()">
                        <i class="las la-lock"></i>
                        <span translate>CHANGE_PASSWORD</span>
                    </div>

                    <div class="action-btn" ng-class="{'active': userDetailsVm.details == 'roles'}"
                         ng-click="userDetailsVm.showUserRoles()">
                        <i class="las la-users-cog"></i>
                        <span translate>USER_ROLES</span>
                    </div>

                    <div class="action-btn" ng-class="{'active': userDetailsVm.details == 'settings'}"
                         ng-click="userDetailsVm.showUserSettings()">
                        <i class="las la-cogs"></i>
                        <span translate>USER_SETTINGS</span>
                    </div>

                    <div class="action-btn admin-btn" ng-if="loginPersonDetails.isSuperUser && (hasPermission('admin','all') || hasPermission('admin','view'))"
                         ng-click="userDetailsVm.gotoAdmin()">
                        <i class="las la-user-shield"></i>
                        <span translate>BACK_TO_ADMIN</span>
                    </div>
                </div>
            </div>
            <div class="user-card details-panel" ui-view></div>
        </div>
    </div>
</div>