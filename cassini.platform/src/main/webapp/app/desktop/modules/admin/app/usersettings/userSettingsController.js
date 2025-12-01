define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/roleService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/profileService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/userListDialog/userListDialogController',
        //'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/profileList/profileListDialogController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/groupListDialog/groupListDialogController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/resetPassword/passwordResetController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService'
    ],

    function (module) {

        module.controller('userSettingsController', userSettingsController);

        function userSettingsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, $translate, $http,
                                        LoginService, PersonGroupService, CommonService, GroupService, RoleService, DialogService, ProfileService, LicenseService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.logins = [];
            vm.roles = [];
            vm.groups = [];
            $rootScope.userNameDelete = null;

            vm.userPersonGroups = [];
            vm.userInRoles = [];
            vm.loadingUsers = true;
            vm.loadingRoles = true;
            vm.loadingGroups = true;

            vm.isUserEdit = false;
            vm.isRoleEdit = false;
            vm.isGroupEdit = false;
            vm.showPermissionGrid = false;
            vm.rolesMap = new Hashtable();
            vm.cross = cross;
            //fixed the Licenses, but it will come from licenses file.

            vm.licenses = 1;

            vm.activeLicenses = null;
            vm.permissionMap = new Hashtable();
            $rootScope.updateModeType = false;
            vm.spinner = {
                active: false
            };

            $rootScope.viewInfo.icon = "fa fa-user";
            $rootScope.viewInfo.title = $translate.instant("ADMIN_TITLE");

            var nodeId = 4;
            var adminTree = null;
            var rootNode = null;
            var selectedType = "";
            var selectedGroup = {};
            var selectedUser = {};
            vm.groupPermissions = [];
            vm.permissionGroups = [];
            var selectedGroupMembers = [];
            vm.personDefaultGrp = null;
            $rootScope.allLoginNames = [];
            var defaultGroupMemberObj = {
                rowId: null,
                person: null,
                login: null,
                permissions: null,
                personGroup: null
            };
            var templates = {
                user: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/user.jsp',
                group: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/group.jsp',
                role: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/role.jsp',
                userEdit: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/userEdit.jsp',
                roleEdit: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/roleEdit.jsp',
                groupEdit: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/groupEdit.jsp',
                allUsers: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/allUsers.jsp',
                allGroups: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/allGroups.jsp',
                security: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/securityView.jsp'

            };

            vm.setTabActive = setTabActive;
            vm.toggleRolePermission = toggleRolePermission;
            vm.getPermissionIndexInRole = getPermissionIndexInRole;
            vm.getPermissionIndexInGroup = getPermissionIndexInGroup;
            vm.GroupPermissions = saveGroupPermissions;

            vm.tabs = [
                {
                    heading: 'Logins',
                    active: true,
                    icon: 'fa-key',
                    state: 'app.admin.usersettings.logins',
                    view: 'logins'
                },
                {
                    heading: 'Roles & Permissions',
                    icon: 'fa-lock',
                    active: false,
                    state: 'app.admin.usersettings.roles',
                    view: 'roles'
                },
                {
                    heading: 'User Sessions',
                    icon: 'flaticon-businessman276',
                    active: false,
                    state: 'app.admin.usersettings.sessions',
                    view: 'sessions'
                }
            ];
            vm.currentTab = vm.tabs[0].view;

            function cross() {

                vm.valid = true;
            }

            function setTabActive(tab) {
                if (tab.state != null) {
                    $state.go(tab.state);
                    vm.currentTab = tab.view;
                }
            }

            function setActiveFlag(index) {
                angular.forEach($scope.tabs, function (t) {
                    t.active = false;
                });
                for (var i = 0; i < $scope.tabs.length; i++) {
                    if (i == index) {
                        $scope.tabs[i].active = true;
                    }
                }
            }

            vm.template = "";
            vm.creating = true;
            vm.valid = true;
            vm.passwordProperties = null;
            vm.updateUserStatus = false;

            vm.selectedUserList = [];
            vm.selectedGroupList = [];


            vm.pageable = {
                page: 1,
                size: 20,
                sort: {
                    field: "createdDate",
                    order: "ASC"
                }
            };


            vm.pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.logins = angular.copy(vm.pagedResults);
            vm.logins = angular.copy(vm.pagedResults);
            vm.roles = angular.copy(vm.pagedResults);
            vm.groups = angular.copy(vm.pagedResults);
            vm.person = {
                firstName: null,
                lastName: null,
                phoneMobile: null,
                email: null,
                personType: 1,
                defaultGroup: null
            };
            vm.tempPerson = {
                firstName: null,
                lastName: null,
                phoneMobile: null,
                email: null,
                personType: 1

            };

            vm.login = {
                loginName: null,
                password: null,
                person: null,
                id: null,
                isActive: false,
                external: false,
                oldPassword: null,
                newPassword: null,
                flag: false
            };

            vm.personGroup = {
                name: null,
                description: null,
                groupMember: [],
                groupId: null,
                parent: null,
                isActive: true,
                external: false,
                profiles: []
            };
            vm.allProfiles = [];
            vm.role = {
                name: null,
                description: null,
                personRoles: [],
                personGroupRoles: [],
                id: null
            };

            vm.createGroup = createGroup;
            vm.createUser = createUser;
            vm.onCreateSelect = onCreateSelect;
            vm.onDeleteSelect = onDeleteSelect;
            vm.cancel = cancel;

            vm.deleteGroupMember = deleteGroupMember;

            vm.editUser = editUser;
            vm.updateUser = updateUser;

            vm.editGroup = editGroup;
            vm.resetPassword = resetPassword;
            vm.updateGroup = updateGroup;

            vm.nextUsersPage = nextUsersPage;
            vm.previousUsersPage = previousUsersPage;

            vm.nextGroupsPage = nextGroupsPage;
            vm.previousGroupsPage = previousGroupsPage;

            vm.selectUsers = selectUsers;
            vm.selectGroups = selectGroups;

            vm.onGroupSelectType = onGroupSelectType;
            vm.toggleNode = toggleNode;
            vm.userActivation = userActivation;
            vm.deleteGroup = deleteGroup;

            vm.showUserPassword = showUserPassword;
            $scope.setViewHeight = setViewHeight;
            vm.inActive = false;

            function activeLicenses() {
                LoginService.getIsActiveLogins(true).then(
                    function (data) {
                        vm.activeLicenses = data;
                    })
            }

            var licenceWarningMessage = parsed.html($translate.instant("LICENCE_WARNING_MESSAGE")).html();
            $scope.invalidPassword = parsed.html($translate.instant("INVALID_PASSWORD")).html();
            $scope.invalidUsername = parsed.html($translate.instant("INVALID_USERNAME")).html();
            vm.removeUser = parsed.html($translate.instant("DELETE_THIS_USER")).html();
            function userActivation() {
                if (vm.login.isActive == true) {
                    if (vm.licenses <= vm.activeLicenses) {
                        vm.login.isActive = false;
                        $rootScope.showWarningMessage(licenceWarningMessage);
                    } else {
                        vm.activeLicenses++;
                    }
                } else {
                    vm.activeLicenses--;
                }
            }

            var usersAlreadyAddedMessage = parsed.html($translate.instant("USERS_ALREADY_ADDED")).html();
            var usersAddedMessage = parsed.html($translate.instant("USERS_ADDED_MESSAGE")).html();
            var usersAddedExistingMessage = parsed.html($translate.instant("USERS_ADDED_EXISTING_MESSAGE")).html();
            var selectUsersTitle = parsed.html($translate.instant("SELECT_USERS")).html();
            var selectProfilesTitle = parsed.html($translate.instant("SELECT_PROFILE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            var Submit = parsed.html($translate.instant("SUBMIT")).html();
            var ResetPassword = parsed.html($translate.instant("RESET_PASSWORD")).html();
            var usernameExists = parsed.html($translate.instant("USERNAME_EXISTS")).html();
            var selectedGroupUsers = " ";
            var existedGroupUsers = " ";
            vm.existedGroupUsers = [];
            vm.selectedGroupUsers = [];
            function selectUsers() {
                var options = {
                    title: selectUsersTitle,
                    template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/userListDialog/userListDialogView.jsp',
                    controller: 'UserListDialogController as userDialogVm',
                    data: {
                        userData: {
                            userList: vm.personGroup.groupMember
                        }
                    },
                    width: 650,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'app.user.users'}
                    ],
                    callback: function (result) {
                        vm.selectedUserList = result;
                        existedGroupUsers = " ";
                        selectedGroupUsers = " ";
                        vm.existedGroupUsers = [];
                        vm.selectedGroupUsers = [];
                        if (vm.selectedUserList != null && vm.selectedUserList != undefined && vm.selectedUserList.length > 0) {

                            angular.forEach(vm.selectedUserList, function (user) {
                                //loop for group users

                                /* if (vm.isGroupEdit) {*/
                                var userExists = false;
                                angular.forEach(vm.personGroup.groupMember, function (existingUser) {

                                    if (existingUser != null && existingUser.person.id == user.person.id) {
                                        userExists = true;
                                        if (vm.existedGroupUsers.length == 0) {
                                            existedGroupUsers = existedGroupUsers + " " + existingUser.person.firstName;
                                        } else {
                                            existedGroupUsers = existedGroupUsers + ", " + existingUser.person.firstName;
                                        }
                                        vm.existedGroupUsers.push(existingUser);
                                    }
                                });
                                /* }*/
                                if (!userExists/* && vm.isGroupEdit*/) {
                                    var grpMember = {person: null, personGroup: null};
                                    grpMember.person = user.person;
                                    grpMember.personObject = user;
                                    vm.personGroup.groupMember.push(grpMember);
                                }
                                if (userExists == false /*&& vm.isGroupEdit*/) {
                                    if (vm.selectedGroupUsers.length == 0) {
                                        selectedGroupUsers = selectedGroupUsers + " " + user.person.firstName;
                                    } else {
                                        selectedGroupUsers = selectedGroupUsers + ", " + user.person.firstName;
                                    }
                                    vm.selectedGroupUsers.push(user);
                                }

                                if (userExists == false /* && vm.isRoleEdit */) {
                                    if (vm.selectedGroupUsers.length == 0) {
                                        selectedGroupUsers = selectedGroupUsers + " " + user.person.firstName;
                                    } else {
                                        selectedGroupUsers = selectedGroupUsers + ", " + user.person.firstName;
                                    }
                                    vm.selectedGroupUsers.push(user);
                                }
                            });
                            if (vm.existedGroupUsers.length > 0 && vm.selectedGroupUsers.length == 0) {
                                $rootScope.showWarningMessage(existedGroupUsers + " : " + usersAlreadyAddedMessage)
                            }
                            if (vm.selectedGroupUsers.length > 0 && vm.existedGroupUsers.length == 0) {
                                $rootScope.showInfoMessage(selectedGroupUsers + " : " + usersAddedMessage)
                            }
                            if (vm.selectedGroupUsers.length > 0 && vm.existedGroupUsers.length > 0) {
                                $rootScope.showInfoMessage(selectedGroupUsers + " : " + usersAddedExistingMessage + " " + existedGroupUsers + ") : " + usersAlreadyAddedMessage);
                            }
                        }


                        updateGroup();

                    }
                };
                $rootScope.showSidePanel(options);
            }

            /*function selectProfile() {
             var options = {
             title: selectProfilesTitle,
             template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/profileList/profileListDialogView.jsp',
             controller: 'ProfileListDialogController as profileDialogVm',
             data: {
             profileData: {
             profile: vm.personGroup.profile
             }
             },
             width: 650,
             showMask: true,
             buttons: [
             {text: addButton, broadcast: 'app.profile.profiles'}
             ],
             callback: function (result) {
             var profile3 = result;
             vm.existedGroupProfiles = [];
             vm.selectedGroupProfiles = [];
             if (profile3 != null && profile3 != undefined && profile3 != "") {
             vm.personGroup.profile = profile3;
             }
             updateGroup();
             }
             };
             $rootScope.showSidePanel(options);
             }*/


            function resetPassword() {
                var options = {
                    title: ResetPassword,
                    template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/resetPassword/passwordResetView.jsp',
                    controller: 'PasswordResetController as resetPasswordVm',
                    data: {
                        login: vm.login
                    },
                    width: 500,
                    showMask: true,
                    buttons: [
                        {text: Submit, broadcast: 'app.reset.password'}
                    ],
                    callback: function (result) {
                    }
                };
                $rootScope.showSidePanel(options);
            }


            var selectGroupTitle = parsed.html($translate.instant("SELECT_GROUPS")).html();
            var groupsAlreadyAddedMessage = parsed.html($translate.instant("GROUPS_ALREADY_ADDED")).html();
            var groupsAddedMessage = parsed.html($translate.instant("GROUPS_ADDED_MESSAGE")).html();
            var groupsAddedExistingMessage = parsed.html($translate.instant("GROUPS_ADDED_EXISTING_MESSAGE")).html();
            var selectedGroups = " ";
            var existedGroups = " ";
            vm.existedGroups = [];
            vm.selectedGroups = [];
            function selectGroups() {
                var options = {
                    title: selectGroupTitle,
                    template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/groupListDialog/groupListDialogView.jsp',
                    controller: 'GroupListDialogController as groupDialogVm',
                    data: {
                        login: vm.login
                    },
                    width: 500,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'app.groups.group'}
                    ],
                    callback: function (result) {
                        vm.selectedGroupList = result;
                        selectedGroups = " ";
                        existedGroups = " ";
                        vm.selectedGroups = [];
                        selectedGroupMembers = [];
                        vm.existedGroups = [];
                        if (vm.selectedGroupList != null && vm.selectedGroupList != undefined && vm.selectedGroupList.length > 0) {
                            angular.forEach(vm.selectedGroupList, function (group) {
                                //loop for groups
                                var groupExists = false;
                                var grpMember = {person: vm.person, personGroup: null};
                                angular.forEach(vm.personGroup.groupMember, function (existingGroup) {

                                    if (existingGroup != null && existingGroup.groupId == group.groupId) {
                                        groupExists = true;
                                        if (vm.existedGroups.length == 0) {
                                            existedGroups = existedGroups + " " + existingGroup.personGroup.name;
                                        } else {
                                            existedGroups = existedGroups + ", " + existingGroup.personGroup.name;
                                        }
                                        vm.existedGroups.push(existingGroup);
                                    }
                                });
                                if (!groupExists) {
                                    var grp = {role: null, personGroup: null};
                                    grp.personGroup = group;
                                    grp.groupId = group.groupId;
                                    //vm.role.personGroupRoles.push(grp);
                                }
                                if (groupExists == false) {
                                    if (vm.selectedGroups.length == 0) {
                                        selectedGroups = selectedGroups + " " + group.name;
                                    } else {
                                        selectedGroups = selectedGroups + ", " + group.name;
                                    }
                                    grpMember.personGroup = group;
                                    selectedGroupMembers.push(grpMember);
                                    vm.selectedGroups.push(group);
                                }
                            });
                            if (vm.selectedGroups.length > 0 && vm.existedGroups.length == 0) {
                                $rootScope.showInfoMessage(selectedGroups + " : " + groupsAddedMessage)
                            }
                            if (vm.existedGroups.length > 0 && vm.selectedGroups.length > 0) {
                                $rootScope.showInfoMessage(selectedGroups + " : " + groupsAddedExistingMessage + " " + existedGroups + ") : " + groupsAlreadyAddedMessage)
                            }
                            if (vm.existedGroups.length > 0 && vm.selectedGroups.length == 0) {
                                $rootScope.showWarningMessage(existedGroups + " : " + groupsAlreadyAddedMessage)
                            }
                            updatePersonGroups();
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function cancel() {
                if (vm.template == templates.group) vm.template = templates.allGroups;
                if (vm.template == templates.user) vm.template = templates.allUsers;
                vm.selectedUserList = [];
                vm.valid = true;
            }

            var adminTitle = parsed.html($translate.instant("ADMIN_TITLE")).html();
            var usersTitle = parsed.html($translate.instant("USERS")).html();
            var groupsTitle = parsed.html($translate.instant("GROUPS")).html();

            var permissionsTitle = parsed.html($translate.instant("PERMISSIONS")).html();

            function initAdminTree() {
                adminTree = $('#adminTree').tree({
                    data: [
                        {
                            id: 0,
                            text: adminTitle,
                            iconCls: 'admin-root',
                            attributes: {},
                            children: [{
                                attributes: {},
                                iconCls: "users-node",
                                id: "1",
                                text: usersTitle
                            },
                                {
                                    attributes: {},
                                    iconCls: "groups-node",
                                    id: "2",
                                    text: groupsTitle
                                },
                                {
                                    attributes: {},
                                    iconCls: "permissions-node",
                                    id: "3",
                                    text: permissionsTitle
                                }]
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onDblClick: onDblClick,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectType
                });

                rootNode = adminTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });
                loadUsers();
                loadGroups();
                loadAllGroups();
                loadProfiles();
            }


            var newUser = parsed.html($translate.instant("NEW_USER")).html();
            var newGroup = parsed.html($translate.instant("NEW_GROUP")).html();
            var deleteUser = parsed.html($translate.instant("DELETE_USER")).html();
            var deleteGroupTitle = parsed.html($translate.instant("DELETE_GROUP")).html();
            vm.passwordInformation = parsed.html($translate.instant("PASSWORD_INFORMATION")).html();
            vm.passwordMediumInformation = parsed.html($translate.instant("PASSWORD_MEDIUM_INFORMATION")).html();
            vm.passwordLowInformation = parsed.html($translate.instant("PASSWORD_LOW_INFORMATION")).html();
            vm.passwordMinLength = parsed.html($translate.instant("PASSWORD_MINIMUM_LENGTH")).html();
            vm.passwordNumbersOnly = parsed.html($translate.instant("PASSWORD_NUMBERS_ONLY")).html();
            vm.passwordNumbersAndSpecialChar = parsed.html($translate.instant("PASSWORD_NUMBERS_AND_SPECIAL_CHARACTERS")).html();
            vm.passwordCaseSensitivie = parsed.html($translate.instant("PASSWORD_CASE_SENSITIVIE")).html();
            vm.password = parsed.html($translate.instant("PASSWORD")).html();

            var current = null;
            var parent = null;

            function onContextMenu(e, node) {
                e.preventDefault();

                current = adminTree.tree('getData', node.target);

                var $contextMenu = $("#contextMenu");

                $('#addType,#deleteType').hide();
                if (current.id == 1) {
                    /* $('#addType').text(newUser);
                     $('#addType').show();
                     selectedType = "USER";
                     selectedUser = {parent: node.target, type: selectedType};*/
                    return false;
                }

                else if (current.id == 2) {
                    /* $('#addType').text(newGroup);
                     $('#addType').show();
                     selectedType = "GROUP";
                     selectedGroup = {parent: node.target, type: selectedType};*/
                    return false;
                }
                if (current.id == 3) {
                    return false;
                } else if (current.attributes.item.type === "USER") {
                    /* $('#deleteType').text(deleteUser);
                     if($rootScope.hasPermission("permission.user.delete")){
                     $('#deleteType').show();

                     }else{
                     return false;
                     }
                     selectedType = "CHILD_USER";
                     selectedUser = {parent: node.target, type: selectedType};*/
                    return false;
                }
                else if (current.attributes.item.type === "GROUP") {
                    /*$('#addType').text(newGroup);
                     $('#addType').show();
                     $('#deleteType').text(deleteGroupTitle);
                     if($rootScope.hasPermission("permission.group.delete")){
                     $('#deleteType').show();
                     }
                     selectedType = "CHILD_GROUP";
                     selectedGroup = {parent: node.target, type: selectedType};
                     */
                    return false;
                } else {
                    return false;
                }
                if (node.text == "Administrator" || node.text == "External User") {
                    $('#addType').text(newGroup);
                    $('#addType').show();
                    $('#deleteType').hide();
                }
                if (node.text1 == 'admin' && current.id != 1) {
                    if (node.attributes.item.loginName == "admin") {
                        $('#deleteType').hide();
                        $("#contextMenu").hide();
                    }
                }

                if (node.text1 != 'admin') {
                    $contextMenu.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    $contextMenu.show();
                }

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
            }

            function onDblClick(node) {

            }

            function onAfterEdit(node) {

            }

            function setViewHeight() {
                var height = $(".view-content").outerHeight();
                var viewTitleContainer = $("#viewTitleContainer").outerHeight();
                $timeout(function () {
                    var barHeight = $('#admin-rightView-bar').outerHeight();

                    if (barHeight != null) {
                        if (viewTitleContainer) {
                            $("#admin-rightView").height(height - 120);
                        } else {
                            $("#admin-rightView").height(height - 70);
                        }
                    } else {
                        if (viewTitleContainer) {
                            $("#admin-rightView").height(height - 70);
                        } else {
                            $("#admin-rightView").height(height - 20);
                        }
                    }
                }, 500)
            }

            vm.selectedUserNode = null;
            function onSelectType(node) {
                $rootScope.lastSelectedGroup = null;
                current = adminTree.tree('getData', node.target);
                parent = adminTree.tree('getParent', node.target);
                vm.selectedGroupId = null;
                vm.selectedUserNode = node;
                $rootScope.lastSelectedGroup = node.attributes.item;
                if (node.id == 1) {
                    vm.template = templates.allUsers;
                    LoginService.getLogins(vm.pageable).then(
                        function (data) {
                            vm.logins = data;
                            vm.loadingUsers = false;
                        }
                    );
                    //$scope.$apply();
                }

                else if (node.id == 2) {
                    vm.template = templates.allGroups;
                    PersonGroupService.getPersonGroups(vm.pageable).then(
                        function (data) {
                            vm.groups = data;
                            vm.loadingGroups = false;
                        }
                    );
                    //$scope.$apply();


                } else if (node.id == 3) {
                    loadPermissionsOfGroups();
                    vm.template = templates.security;
                    //$scope.$apply();
                } else if (node.id != 0 && node.id != 1 && node.id != 2) {

                    updateEditView(current, parent);
                }
            }

            vm.selectedGroupId = null;
            function updateEditView(current, parent) {
                $rootScope.userNameDelete = current.text;
                var data = current.attributes;
                if (parent.id == 1) {
                    vm.template = templates.userEdit;
                    vm.person = data.item.person;

                    vm.tempPerson.firstName = vm.person.firstName;
                    vm.tempPerson.lastName = vm.person.lastName;
                    vm.tempPerson.email = vm.person.email;
                    vm.tempPerson.phoneMobile = vm.person.phoneMobile;


                    vm.valid = true;
                    vm.isUserEdit = false;
                    /* vm.login = {'loginName': data.item.loginName,'id':data.item.id};*/
                    vm.login = data.item;
                    vm.tempExternal = data.item.external;
                    vm.login.oldPassword = data.item.password;
                    //vm.login.newPassword = data.item.password;
                    vm.login.flag = false;
                    vm.userPersonGroups = [];

                    PersonGroupService.getPersonGroupsByPersonId(vm.person.id).then(
                        function (data) {
                            vm.userPersonGroups = data;
                        }
                    );

                    if (vm.person.defaultGroup != null) {
                        GroupService.getGroupById(vm.person.defaultGroup).then(
                            function (data) {
                                vm.personDefaultGrp = data;
                                defaultGroupMemberObj.person = vm.person;
                                defaultGroupMemberObj.login = vm.login;
                            }
                        );
                        PersonGroupService.getGroupMember(vm.person.id, vm.person.defaultGroup).then(
                            function (data) {
                                defaultGroupMemberObj.rowId = data.rowId;
                            }
                        )
                    }
                }
                else {
                    vm.template = templates.groupEdit;

                    vm.valid = true;
                    vm.isGroupEdit = false;
                    vm.inActive = false;
                    PersonGroupService.getPersonGrpById(data.item.groupId).then(
                        function (data) {
                            vm.personGroup = data;
                            if (data.profile != null) {
                                CommonService.getPersonReferences([data.profile], 'createdBy');
                            }
                            vm.selectedGroupId = vm.personGroup.groupId;
                            LoginService.getLoginReferences(vm.personGroup.groupMember, 'person');
                            if (data.isActive == true) {
                                vm.inActive = false;
                            }
                            else {
                                vm.inActive = true;
                            }
                        }
                    );

                }

                $scope.$apply();
            }

            var firstNameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var userNameValidation = parsed.html($translate.instant("USER_NAME_VALIDATION")).html();
            var passwordValidation = parsed.html($translate.instant("PASSWORD_VALIDATION_FORMAT")).html();
            var phoneNumberValidation = parsed.html($translate.instant("ENTER_VALID_PHONE_NUMBER")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            var phoneNumberCannotEmpty = parsed.html($translate.instant("PHONE_NUMBER_VALIDATION")).html();
            var emailCannotEmpty = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var externalValidation = parsed.html($translate.instant("EXTERNAL_CHECK_BOX_VALIDATION")).html();
            var passwordLength = parsed.html($translate.instant("PASSWORD_LENGTH")).html();
            var passwordValidFormat = parsed.html($translate.instant("PASSWORD_VALIDATION_FORMAT")).html();
            var passwordMediumLength = parsed.html($translate.instant("PASSWORD_MEDIUM_LENGTH")).html();
            var passwordLowerCase = parsed.html($translate.instant("PASSWORD_LOWERCASE")).html();
            var passwordUpperCase = parsed.html($translate.instant("PASSWORD_UPPERCASE")).html();
            var passwordSpecialChar = parsed.html($translate.instant("PASSWORD_SPECIAL")).html();
            var passwordNumber = parsed.html($translate.instant("PASSWORD_NUMBER")).html();
            var defaultGroupValidation = parsed.html($translate.instant("DEFAULT_GROUP_VALIDATION")).html();
            var PermissionAddMsg = parsed.html($translate.instant("PERMISSION_ADDED_MSG")).html();
            var PermissionRemoveMsg = parsed.html($translate.instant("PERMISSION_REMOVE_MSG")).html();
            var characters = parsed.html($translate.instant("CHARACTERS")).html();


            var passwordStrength = /^[\s\S]{8,32}$/,
                upper = /[A-Z]/,
                lower = /[a-z]/,
                number = /[0-9]/,
                special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;


            /*    $rootScope.passwordStrengthValid = passwordStrengthValid;

             /!*function passwordStrengthValid() {
             var userPassword = document.getElementById("password").value;
             var meter = document.getElementById('password-strength-meter');
             if (vm.passwordStrength == "high") {
             var score = 0;
             if (upper.test(vm.login.password)) {
             score++;
             }
             if (lower.test(vm.login.password)) {
             score++;
             }
             if (number.test(vm.login.password)) {
             score++;
             }
             if (special.test(vm.login.password)) {
             score++;
             }
             }
             if (vm.passwordStrength == "medium") {
             var score = 0;
             if (upper.test(vm.login.password)) {
             score++;
             }
             if (lower.test(vm.login.password)) {
             score++;
             }
             if (number.test(vm.login.password)) {
             score++;
             }
             if (score == 3) {
             score++;
             }
             }
             if (vm.passwordStrength == "low") {
             var score = 0;
             if (lower.test(vm.login.password)) {
             score++;
             }
             if (score == 1) {
             score = 4;
             }
             }
             meter.value = score;
             }*!/


             var passwordStrength = /^[\s\S]{8,32}$/,
             upper = /[A-Z]/,
             lower = /[a-z]/,
             number = /[0-9]/,
             special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;

             $rootScope.passwordStrengthValid = passwordStrengthValid;
             function loadPasswordStrength() {
             var context = 'SYSTEM';
             CommonService.getPreferenceByContext(context).then(
             function (data) {
             angular.forEach(data, function (prop) {
             if (prop.preferenceKey == 'SYSTEM.PASSWORD') {
             vm.passwordProperties = JSON.parse(prop.jsonValue);
             }
             });
             }, function (error) {
             console.log(error);
             }
             )
             }

             vm.loadPasswordMessage = loadPasswordMessage;
             function loadPasswordMessage() {
             vm.passwordInformation = "";
             if (vm.passwordProperties.minLen != null) {
             vm.passwordInformation += "<br>";
             vm.passwordInformation += "\u2022 " + vm.passwordMinLength + vm.passwordProperties.minLen + characters;
             }
             if (vm.passwordProperties.specialChar != null) {
             if (vm.passwordProperties.specialChar == 'Nb') {
             vm.passwordInformation += "<br>";
             vm.passwordInformation += "\u2022 " + vm.passwordNumbersOnly;
             }
             if (vm.passwordProperties.specialChar == 'Nbs') {
             vm.passwordInformation += "<br>";
             vm.passwordInformation += "\u2022 " + vm.passwordNumbersAndSpecialChar;
             }
             }
             if (vm.passwordProperties.cases != null) {
             if (vm.passwordProperties.cases == 'Yes') {
             vm.passwordInformation += "<br>";
             vm.passwordInformation += "\u2022 " + vm.passwordCaseSensitivie;
             }
             }
             }

             function passwordStrengthValid() {
             var userPassword = document.getElementById("password").value;
             var meter = document.getElementById('password-strength-meter');
             var score = null;
             if (vm.passwordProperties != null) {
             loadPasswordMessage();
             var minLength = parseInt(vm.passwordProperties.minLen);
             var specialChar = vm.passwordProperties.specialChar;
             var cases = vm.passwordProperties.cases;

             score = evalPasswordScore(minLength, specialChar, cases);
             /!* if (vm.passwordStrength == "high") {
             var score = 0;
             if (upper.test(vm.login.password)) {
             score++;
             }
             if (lower.test(vm.login.password)) {
             score++;
             }
             if (number.test(vm.login.password)) {
             score++;
             }
             if (special.test(vm.login.password)) {
             score++;
             }
             }
             if (vm.passwordStrength == "medium") {
             var score = 0;
             if (upper.test(vm.login.password)) {
             score++;
             }
             if (lower.test(vm.login.password)) {
             score++;
             }
             if (number.test(vm.login.password)) {
             score++;
             }
             if (score == 3) {
             score++;
             }
             }
             if (vm.passwordStrength == "low") {
             var score = 0;
             if (lower.test(vm.login.password)) {
             score++;
             }
             if (score == 1) {
             score = 4;
             }
             }*!/
             } else {
             score = 4;
             vm.passwordInformation = vm.password;
             }
             meter.value = score;
             }

             vm.passwordScore = null;
             function evalPasswordScore(minLength, specialChar, cases) {
             var len = vm.login.password.length;
             var score = 0;
             if (minLength == null && (specialChar == null || specialChar == 'No') && (cases == null || cases == 'No')) {
             score = 4;
             } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'No' || cases == null)) {
             if (len >= minLength) score = 4;
             } else if (minLength != null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
             if (len >= minLength) {
             score = 2;
             }
             if (number.test(vm.login.password)) {
             score += 2;
             }
             } else if (minLength != null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
             if (len >= minLength) {
             score = 2;
             }
             if (number.test(vm.login.password) && special.test(vm.login.password)) {
             score += 2;
             }
             } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
             if (len >= minLength) {
             score = 2;
             }
             if (upper.test(vm.login.password) && lower.test(vm.login.password)) {
             score += 2;
             }
             } else if (minLength != null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
             if (len >= minLength) {
             score = 2;
             }
             if (number.test(vm.login.password)) {
             score += 1;
             }
             if (upper.test(vm.login.password) && lower.test(vm.login.password)) {
             score += 1;
             }
             } else if (minLength != null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
             if (len >= minLength) {
             score = 2;
             }
             if (number.test(vm.login.password) && special.test(vm.login.password)) {
             score += 1;
             }
             if (upper.test(vm.login.password) && lower.test(vm.login.password)) {
             score += 1;
             }
             } else if (minLength == null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
             if (number.test(vm.login.password)) {
             score = 4;
             }
             } else if (minLength == null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
             if (number.test(vm.login.password) && special.test(vm.login.password)) {
             score = 4;
             }
             } else if (minLength == null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
             if (upper.test(vm.login.password) && lower.test(vm.login.password)) {
             score = 4;
             }
             } else if (minLength == null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
             if (number.test(vm.login.password)) {
             score = 2;
             }
             if (upper.test(vm.login.password) && lower.test(vm.login.password)) {
             score += 2;
             }
             } else if (minLength == null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
             if (number.test(vm.login.password) && special.test(vm.login.password)) {
             score = 2;
             }
             if (upper.test(vm.login.password) && lower.test(vm.login.password)) {
             score += 2;
             }
             }
             vm.passwordScore = score;
             return score;
             }*/


            function scrollSmoothToTop() {
                $('#admin-rightView').animate({
                    scrollTop: $("#content").offset().top
                }, 200);

            }

            $scope.safeCharsPassword = false;
            $scope.safeCharsUserName = false;
            $scope.safeCharsUserNameCheck = safeCharsUserNameCheck;
            $scope.safeCharsPasswordCheck = safeCharsPasswordCheck;


            function safeCharsPasswordCheck() {
                var userPassword = document.getElementById("password").value;
                var regex = /^[a-zA-Z0-9!*:,'_~( )@\*\)\(_\-\s\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC\u00df]+$/g;
                // var match = getValue().match(regex);
                if (userPassword.match(regex) == null || userPassword.includes(" ")) {
                    return false;
                }
                else {
                    $scope.safeCharsPassword = false;
                    return true;
                }

            }

            function safeCharsUserNameCheck() {
                if (vm.login.loginName.includes("@")) {
                    return false;
                }
                else {
                    $scope.safeCharsUserName = false;
                    return true;
                }

            }

            function validateUser(isCreate) {
                vm.valid = true;

                if (vm.person.firstName == null || vm.person.firstName == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(firstNameValidation);
                }
                else if (vm.login.loginName == null || vm.login.loginName == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(userNameValidation);
                }
                else if (vm.login.loginName != null && !validLoginName()) {
                    vm.valid = false;
                    vm.error = "Login Name already exist";
                }
                else if (vm.login.password == null || vm.login.password == "" || $rootScope.validPassword == false && isCreate) {
                    vm.valid = false;
                    $rootScope.showErrorMessage(passwordValidation);
                }

                /*  else if (vm.person.phoneMobile == undefined) {
                 vm.valid = false;
                 vm.error = phoneNumberValidation;
                 }

                 else if (vm.person.phoneMobile == null || vm.person.phoneMobile == "") {
                 vm.valid = false;
                 vm.error = phoneNumberCannotEmpty;
                 }*/
                else if (vm.personDefaultGrp == null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage(defaultGroupValidation);
                }
                else if (vm.person.email == null || vm.person.email == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(emailValidation);
                }
                else if (!validateEmail()) {
                    vm.valid = false;
                    $rootScope.showErrorMessage(emailCannotEmpty);
                } else if (vm.personDefaultGrp.name == "External User" && vm.login.external == false) {
                    vm.valid = false;
                    $rootScope.showErrorMessage(externalValidation);
                }

                /* if (vm.login.password != null && vm.passwordStrength == "high") {
                 if (vm.login.password.length < 8) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordLength);
                 vm.valid = false;
                 } else if (!upper.test(vm.login.password)) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordUpperCase);
                 vm.valid = false;
                 } else if (!lower.test(vm.login.password)) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordLowerCase);
                 vm.valid = false;
                 } else if (!number.test(vm.login.password)) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordNumber);
                 vm.valid = false;
                 } else if (!special.test(vm.login.password)) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordSpecialChar);
                 vm.valid = false;
                 }
                 /!*else if (safeCharsPasswordCheck() == false) {
                 $scope.safeCharsPassword = true;
                 scrollSmoothToTop();
                 vm.valid = false;
                 }*!/
                 }*/

                if (!vm.updateUserStatus) {
                    if (vm.passwordScore != 4 && vm.passwordScore != null) {
                        $scope.safeCharsPassword = false;
                        vm.valid = false;
                        $rootScope.showErrorMessage(passwordValidFormat);
                    }
                }


                /*if (vm.login.password != null && vm.passwordStrength == "medium") {
                 if (vm.login.password.length < 6) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordMediumLength);
                 vm.valid = false;
                 } else if (!upper.test(vm.login.password)) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordUpperCase);
                 vm.valid = false;
                 } else if (!lower.test(vm.login.password)) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordLowerCase);
                 vm.valid = false;
                 } else if (!number.test(vm.login.password)) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordNumber);
                 vm.valid = false;
                 }
                 }


                 if (vm.login.password != null && vm.passwordStrength == "low") {
                 if (vm.login.password.length < 6) {
                 $scope.safeCharsPassword = false;
                 $rootScope.showErrorMessage(passwordMediumLength);
                 vm.valid = false;
                 }
                 }*/


                if (vm.login.loginName != null) {
                    if (safeCharsUserNameCheck() == false) {
                        $rootScope.showErrorMessage($scope.invalidUsername);
                        $scope.safeCharsUserName = true;
                        scrollSmoothToTop();
                        vm.valid = false;
                    }
                }


                if (vm.login.newPassword != null) {
                    if (vm.login.newPassword.length < 8) {
                        $rootScope.showErrorMessage(passwordLength);
                        vm.valid = false;
                    } else if (!upper.test(vm.login.newPassword)) {
                        $rootScope.showErrorMessage(passwordUpperCase);
                        vm.valid = false;
                    } else if (!lower.test(vm.login.newPassword)) {
                        $rootScope.showErrorMessage(passwordLowerCase);
                        vm.valid = false;
                    } else if (!number.test(vm.login.newPassword)) {
                        $rootScope.showErrorMessage(passwordNumber);
                        vm.valid = false;
                    } else if (!special.test(vm.login.newPassword)) {
                        $rootScope.showErrorMessage(passwordSpecialChar);
                        vm.valid = false;
                    }
                }

                if (vm.login.oldPassword != vm.login.newPassword) {
                    vm.login.flag = true
                }

                return vm.valid;

            }

            function validLoginName() {
                var valid = true;

                angular.forEach($rootScope.allLoginNames, function (login) {
                    if (login.id != vm.login.id && vm.login.loginName.toLowerCase() == login.loginName.toLowerCase()) {
                        valid = false;
                        $rootScope.showErrorMessage(usernameExists);
                    }
                });

                return valid;
            }

            function showUserPassword() {
                var eyeIcon = document.getElementById("showPassword");
                var newPassword = document.getElementById("password");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye-slash") {
                    eyeIcon.title = parsed.html($translate.instant("HIDE_PASSWORD")).html();
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                } else {
                    eyeIcon.title = parsed.html($translate.instant("SHOW_PASSWORD")).html();
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                }

                if (newPassword.attributes.type.nodeValue == "password") {
                    newPassword.attributes.type.nodeValue = "text";
                } else {
                    newPassword.attributes.type.nodeValue = "password";
                }
            }

            function validateEmail() {
                var valid = true;
                var atpos = vm.person.email.indexOf("@");
                var dotpos = vm.person.email.lastIndexOf(".");
                if (vm.person.email != null && vm.person.email != undefined && vm.person.email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }


            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var profileValidation = parsed.html($translate.instant("VALIDATE_PROFILE_MESSAGE")).html();

            function validateGroup() {
                vm.valid = true;
                if (vm.personGroup.name == null || vm.personGroup.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(nameValidation);
                }

                if (vm.personGroup.profile == null || vm.personGroup.profile == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(profileValidation);
                }

                return vm.valid;
            }

            var groupCreatedMessage = parsed.html($translate.instant("GROUP_CREATED_MESSAGE")).html();
            var userCreatedMessage = parsed.html($translate.instant("USER_CREATED_MESSAGE")).html();

            function createGroup() {
                if (validateGroup()) {
                    vm.creating = true;

                    /* if(selectedGroup.type === "CHILD_GROUP"){
                     var parent = adminTree.tree('getData', selectedGroup.parent);
                     vm.personGroup.parent = parent.attributes.item.groupId;
                     }*/

                    if (vm.selectedUserList != null && vm.selectedUserList != undefined && vm.selectedUserList.length > 0) {

                        vm.personGroup.groupMember = [];
                        angular.forEach(vm.selectedUserList, function (user) {
                            var grpMember = {person: null, personGroup: null};
                            grpMember.person = user.person;

                            vm.personGroup.groupMember.push(grpMember);
                        });
                    }
                    vm.personGroup.parent = vm.selectedGroupId;
                    PersonGroupService.createPersonGroup(vm.personGroup).then(
                        function (data) {
                            vm.creating = false;
                            data.type = 'GROUP';
                            rootNode = 0;
                            initAdminTree();
                            /*  rootNode = adminTree.tree('find', 2);
                             /!*adminTree.tree('append', {
                             parent: current.target,
                             data: makeGroupNode(data)
                             });*!/
                             adminTree.tree('append', {
                             parent: current.target,
                             data: makeGroupNode(data)
                             });*/
                            PersonGroupService.getPersonGroups(vm.pageable).then(
                                function (data) {
                                    vm.groups = data;
                                    vm.loadingGroups = false;
                                }
                            );
                            vm.template = templates.allGroups;
                            vm.selectedUserList = [];
                            $rootScope.showSuccessMessage(groupCreatedMessage);
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                        }
                    );
                }
            }

            function createUser() {
                if (validateUser(true)) {
                    $rootScope.showBusyIndicator();
                    vm.creating = true;
                    if (vm.personDefaultGrp != null) {
                        vm.person.defaultGroup = vm.personDefaultGrp.groupId;
                        CommonService.createPerson(vm.person).then(
                            function (data) {
                                vm.login.person = data;
                                return LoginService.createLogin(vm.login, vm.person.phoneMobile, vm.person.email);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        ).then(
                            function (data) {
                                /*if(data.isActive == true){
                                 vm.activeLicenses++;
                                 }*/
                                $rootScope.hideBusyIndicator();
                                vm.creating = false;
                                data.type = "USER";
                                activeLicenses();
                                $rootScope.allLoginNames.push(data);
                                rootNode = 0;
                                initAdminTree();
                                /*rootNode = adminTree.tree('find', 1);
                                 adminTree.tree('append', {
                                 parent: rootNode.target,
                                 data: makeUserNode(data)
                                 });*/
                                loadAllUsers();
                                vm.template = templates.allUsers;
                                $rootScope.showSuccessMessage(userCreatedMessage);
                            }, function (error) {
                                CommonService.deletePerson(vm.login.person.id).then(
                                    function (data) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                        );
                    }
                }
            }

            $scope.newUserTitle = parsed.html($translate.instant("ADD_NEW_USER")).html();
            $scope.newGroupTitle = parsed.html($translate.instant("ADD_NEW_GROUP")).html();

            vm.selectType = selectType;
            function selectType(type) {
                resetData();
                if (type == "USER") {
                    vm.personDefaultGrp = null;
                    vm.template = templates.user;
                    var height = $("#contentpanel").outerHeight();
                    GroupService.getAllGroups().then(
                        function (data) {
                            vm.groups = [];
                            vm.excludeExternalGroup = [];
                            vm.allGroups = data;
                            angular.forEach(vm.allGroups, function (group) {
                                vm.groups.push(group);
                                if (group.groupChildren != null && group.groupChildren.length > 0) {
                                    populateGroups(group.groupChildren);
                                }
                                if (group.groupId != 3) vm.excludeExternalGroup.push(group);
                            });
                        }
                    );
                    $timeout(function () {
                        $("#admin-rightView").height(height - 130);
                    }, 500)
                } else if (type == "GROUP") {
                    vm.selectedUserList = [];
                    vm.template = templates.group;
                    var height = $("#contentpanel").outerHeight();
                    $timeout(function () {
                        $("#admin-rightView").height(height - 130);
                    }, 500)
                }
            }

            vm.deleteUsers = deleteUsers;
            function deleteUsers() {
                selectedType = "CHILD_USER";
                selectedUser = {parent: vm.selectedUserNode.target, type: selectedType};
                if ($rootScope.hasPermission("permission.admin.user.delete")) {
                    onDeleteSelect();
                } else {
                    return false;
                }
            }

            vm.deleteGroups = deleteGroups;
            function deleteGroups() {
                selectedType = "CHILD_GROUP";
                selectedGroup = {parent: vm.selectedUserNode.target, type: selectedType};
                if ($rootScope.hasPermission("permission.admin.group.delete")) {
                    onDeleteSelect();
                } else {
                    return false;
                }
            }

            vm.createGroups = createGroups;
            function createGroups() {
                selectedType = "CHILD_GROUP";
                selectedGroup = {parent: vm.selectedUserNode.target, type: selectedType};
                if ($rootScope.hasPermission("permission.admin.group.add")) {
                    onCreateSelect();
                } else {
                    return false;
                }
            }

            function onCreateSelect() {
                resetData();

                if (selectedType == "USER") {
                    vm.template = templates.user;
                    var height = $("#contentpanel").outerHeight();
                    $timeout(function () {
                        $("#admin-rightView").height(height - 130);
                    }, 500)
                } else if (selectedType == "GROUP" || selectedType == "CHILD_GROUP") {
                    vm.selectedUserList = [];
                    vm.template = templates.group;
                    PersonGroupService.getPersonGroups(vm.pageable).then(
                        function (data) {
                            vm.groups = data;
                            vm.loadingGroups = false;
                        }
                    );
                    var height = $("#contentpanel").outerHeight();
                    $timeout(function () {
                        $("#admin-rightView").height(height - 130);
                    }, 500)
                }
            }

            var deleteUserDialogTitle = parsed.html($translate.instant("DELETE_USER")).html();
            var deleteGroupUserDialogTitle = parsed.html($translate.instant("DELETE_GROUP_USER")).html();
            var deleteRoleUserDialogTitle = parsed.html($translate.instant("DELETE_ROLE_USER")).html();
            var deleteRoleGroupDialogTitle = parsed.html($translate.instant("DELETE_ROLE_GROUP")).html();
            var deleteGroupDialogTitle = parsed.html($translate.instant("DELETE_GROUP")).html();
            var deleteRoleDialogTitle = parsed.html($translate.instant("DELETE_ROLE")).html();
            var deleteUserDialogMessage = parsed.html($translate.instant("DELETE_USER_DIALOG_MESSAGE")).html();
            var deleteGroupUserDialogMessage = parsed.html($translate.instant("DELETE_GROUP_USER_DIALOG_MESSAGE")).html();
            var fromGroup = parsed.html($translate.instant("FROM_GROUP")).html();
            var remove = parsed.html($translate.instant("REMOVE")).html();
            var deleteRoleUserDialogMessage = parsed.html($translate.instant("DELETE_ROLE_USER_DIALOG_MESSAGE")).html();
            var deleteRoleGroupDialogMessage = parsed.html($translate.instant("DELETE_ROLE_GROUP_DIALOG_MESSAGE")).html();
            var deleteGroupDialogMessage = parsed.html($translate.instant("DELETE_GROUP_DIALOG_MESSAGE")).html();
            var deleteRoleDialogMessage = parsed.html($translate.instant("DELETE_ROLE_DIALOG_MESSAGE")).html();
            var userDeletedMessage = parsed.html($translate.instant("USER_DELETED_MESSAGE")).html();
            var groupUserDeletedMessage = parsed.html($translate.instant("GROUP_USER_DELETED_MESSAGE")).html();
            var roleUserDeletedMessage = parsed.html($translate.instant("ROLE_USER_DELETED_MESSAGE")).html();
            var roleGroupDeletedMessage = parsed.html($translate.instant("ROLE_GROUP_DELETED_MESSAGE")).html();
            var groupDeletedMessage = parsed.html($translate.instant("GROUP_DELETED_MESSAGE")).html();
            var roleDeletedMessage = parsed.html($translate.instant("ROLE_DELETED_MESSAGE")).html();
            var userAssignedToGroups = parsed.html($translate.instant("USER_ASSIGNED_TO_GROUPS")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var from = parsed.html($translate.instant("FROM")).html();
            var deleteAdmin = parsed.html($translate.instant("DELETE_ADMINISTRATOR_MSG")).html();
            var groupChildDelete = parsed.html($translate.instant("GROUP_CHILD_DELETE_MSG")).html();
            var groupMemberDelete = parsed.html($translate.instant("GROUP_MEMBER_DELETE_MSG")).html();
            var groupDefaultDeleteMessage = parsed.html($translate.instant("GROUP_DEFAULT_DELETE_MSG")).html();


            function onDeleteSelect() {

                if (selectedType == "CHILD_USER" && $rootScope.hasPermission('admin', 'user', 'delete')) {
                    var parent = null;
                    if (vm.selectedUserNode == null)parent = adminTree.tree('getData', selectedUser.parent);
                    else parent = vm.selectedUserNode;
                    var personId = parent.attributes.item.person.id;
                    var grps = "(";
                    PersonGroupService.getPersonGroupsByPersonId(personId).then(
                        function (data) {
                            angular.forEach(data, function (obj, $index) {
                                if ($index == 0)
                                    grps += obj.name;
                                else if ($index == (data.length - 1))
                                    grps += " and " + obj.name;
                                else
                                    grps += ", " + obj.name;
                            });
                            grps += ")";
                            var msg = null;
                            if (data.length > 0) {
                                msg = userAssignedToGroups + " " + grps + ", " + deleteUserDialogMessage;
                            } else {
                                msg = deleteUserDialogMessage;
                            }
                            var options = {
                                title: deleteUserDialogTitle,
                                message: msg,
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    if (personId != 1) {
                                        CommonService.deletePerson(personId).then(
                                            function (data) {
                                                loadAllUsers();
                                                $rootScope.showSuccessMessage(userDeletedMessage);
                                                activeLicenses();
                                                initAdminTree();
                                                vm.template = templates.allUsers;
                                            }
                                        )
                                    } else {
                                        $rootScope.showInfoMessage(deleteAdmin);
                                    }
                                }
                            });
                        }
                    );

                } else if (selectedType == "CHILD_GROUP" && $rootScope.hasPermission('admin', 'group', 'delete')) {

                    var options = {
                        title: deleteGroupDialogTitle,
                        message: deleteGroupDialogMessage,
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            var parent = adminTree.tree('getData', selectedGroup.parent);
                            var groupId = parent.attributes.item.groupId;

                            if (parent.children != undefined && parent.children.length > 0) {
                                $rootScope.showWarningMessage(groupChildDelete);
                            }

                            else {
                                //Checks whether group member(s) are present or not
                                if (parent.attributes.item.groupMember == null || parent.attributes.item.groupMember == '') {
                                    PersonGroupService.deletePersonGroup(groupId).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(groupDeletedMessage);
                                            activeLicenses();
                                            initAdminTree();
                                            PersonGroupService.getPersonGroups(vm.pageable).then(
                                                function (data) {
                                                    vm.groups = data;
                                                    vm.loadingGroups = false;
                                                }
                                            );
                                            vm.template = templates.allGroups;
                                        }
                                    )

                                }
                                else {
                                    $rootScope.showWarningMessage(groupMemberDelete);
                                }
                            }

                        }
                    });

                }
            }


            function makeUserNode(item) {
                var loginName = null;
                var iconClass = '';
                if (item.isActive == false) {
                    loginName = '<span style=""> ' + item.loginName + ' </span>';
                    iconClass = "inActive-user";
                }
                else if (item.external == true && item.external == true) {
                    loginName = '<span style="color: orange !important;"> ' + item.loginName + ' </span>';
                    iconClass = "external-user";
                }
                else {
                    loginName = '<span style="font-weight: bold !important;"> ' + item.loginName + ' </span>';
                    iconClass = "user-node";
                }

                return {
                    id: ++nodeId,
                    text: loginName,
                    text1: item.loginName,
                    iconCls: iconClass,
                    attributes: {
                        item: item
                    }
                };
            }

            function makeGroupNode(item) {
                return {
                    id: ++nodeId,
                    text: item.name,
                    iconCls: 'groups-node',
                    attributes: {
                        item: item
                    }
                };
            }

            function loadAllUsers() {
                LoginService.getLogins(vm.pageable).then(
                    function (data) {
                        vm.logins = data;
                        vm.loadingUsers = false;

                    }
                )
            }

            function loadProfiles() {
                ProfileService.getAllProfiles().then(
                    function (data) {
                        vm.allProfiles = data;
                    }
                );
            }

            function loadAllGroups() {
                GroupService.getAllGroups().then(
                    function (data) {
                        vm.groups = [];
                        vm.excludeExternalGroup = [];
                        vm.allGroups = data;
                        angular.forEach(vm.allGroups, function (group) {
                            vm.groups.push(group);
                            if (group.groupChildren != null && group.groupChildren.length > 0) {
                                populateGroups(group.groupChildren);
                            }
                            if (group.groupId != 3) vm.excludeExternalGroup.push(group);
                        });
                        vm.loadingGroups = false;
                        vm.permissionGroupMap = new Hashtable();
                        var gps = [];
                        angular.forEach(vm.groups, function (group) {
                            gps = [];
                            angular.forEach(vm.groupPermissions, function (gp) {
                                if (group.groupId == gp.id.group.groupId) {
                                    gps.push(gp);
                                }
                            });
                            vm.permissionGroupMap.put(group.groupId, gps);
                        });

                        loadGroupsPermissionsGrid();
                    }
                )
            }

            function populateGroups(groupChildren) {
                angular.forEach(groupChildren, function (child) {
                    vm.groups.push(child);
                    if (child.groupChildren != null && child.groupChildren.length > 0) {
                        populateGroups(child.groupChildren);
                    }
                })

            }

            function loadUsers() {
                LoginService.getAllLogins().then(
                    function (data) {
                        var rootNode = adminTree.tree('find', 1);
                        var nodes = [];

                        angular.forEach(data, function (item) {
                            $rootScope.allLoginNames.push(item);
                            item.type = "USER";
                            var node = makeUserNode(item);
                            /*if (item.external == true && item.isActive == false) {
                             node.text = '<span style="color: orange !important;"> ' + item.loginName + ' </span>';
                             }*/
                            nodes.push(node);
                        });

                        adminTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function loadPermissionsOfGroups() {
                PersonGroupService.getAllGroupPermissions().then(
                    function (data) {
                        vm.groupPermissions = data;
                        loadAllGroups();
                    }
                )
            }

            function getGroupProfiles(group) {
                ProfileService.getGroupProfiles(group.id).then(
                    function (data) {
                        CommonService.getPersonReferences(data, 'createdBy');
                        group.profiles = data;
                    }
                )
            }


            function loadGroups() {
                PersonGroupService.getPersonGroupTree().then(
                    function (data) {
                        var rootNode = adminTree.tree('find', 2);
                        var nodes = [];

                        angular.forEach(data, function (item) {
                            item.type = "GROUP";
                            var node = makeGroupNode(item);
                            if (item.groupChildren != null && item.groupChildren != undefined && item.groupChildren.length > 0) {
                                node.state = "open";
                                visitChildren(node, item.groupChildren);
                            }

                            nodes.push(node);
                        });

                        adminTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    itemType.type = "GROUP";
                    var node = makeGroupNode(itemType);

                    if (itemType.groupChildren != null && itemType.groupChildren != undefined && itemType.groupChildren.length > 0) {
                        node.state = 'open';
                        visitChildren(node, itemType.groupChildren);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function resetData() {
                vm.person = {
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1

                };

                vm.login = {
                    loginName: null,
                    password: null,
                    person: null,
                    newPassword: null

                };

                vm.personGroup = {
                    name: null,
                    description: null

                };

            }


            function deleteGroupMember(login) {
                if ($rootScope.lastSelectedGroup.groupId != login.person.defaultGroup) {
                    var options = {
                        title: deleteGroupUserDialogTitle,
                        message: deleteGroupUserDialogMessage + " (" + login.person.fullName + ") " + fromGroup + " (" + $rootScope.userNameDelete + ")?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            PersonGroupService.deletePersonGroupMember(vm.personGroup.groupId, login.person.id).then(
                                function (data) {

                                    var index = vm.personGroup.groupMember.indexOf(login);
                                    vm.personGroup.groupMember.splice(index, 1);
                                    var index1 = vm.selectedUserNode.attributes.item.groupMember.indexOf(login);
                                    vm.selectedUserNode.attributes.item.groupMember.splice(index1, -1);
                                    $rootScope.showSuccessMessage(groupUserDeletedMessage);
                                }
                            )
                        }
                    });
                } else {
                    $rootScope.showWarningMessage($rootScope.lastSelectedGroup.name + groupDefaultDeleteMessage)
                }
            }

            function nextUsersPage() {
                vm.pageable.page++;
                loadAllUsers();
            }

            function previousUsersPage() {
                vm.pageable.page--;
                loadAllUsers();
            }

            function nextGroupsPage() {
                vm.pageable.page++;
                loadAllGroups();
            }

            function previousGroupsPage() {
                vm.pageable.page--;
                loadAllGroups();

            }

            function resetPage() {
                vm.pageable.page = 0;
            }

            var userUpdatedMessage = parsed.html($translate.instant("USER_UPDATED_MESSAGE")).html();
            var groupUpdatedMessage = parsed.html($translate.instant("GROUP_UPDATED_MESSAGE")).html();

            function updateUser() {
                vm.updateUserStatus = true;
                vm.login.password = vm.login.password;
                if (validateUser(false)) {
                    if (vm.login.flag == true) {
                        vm.login.password = vm.login.password;
                    }
                    if (vm.personDefaultGrp.external == false) {
                        $("#external").prop("checked", false);
                        vm.login.external = false;
                    }
                    vm.creating = true;
                    vm.person.defaultGroup = vm.personDefaultGrp.groupId;
                    CommonService.updatePerson(vm.person).then(
                        function (data) {
                            vm.login.person = data;
                            $rootScope.personInfo = data;
                            if (vm.personDefaultGrp != null) {
                                defaultGroupMemberObj.personGroup = vm.personDefaultGrp;
                                PersonGroupService.updateGroupMember(data.id, defaultGroupMemberObj).then(
                                    function (data) {
                                        PersonGroupService.getPersonGroupsByPersonId(vm.person.id).then(
                                            function (data) {
                                                vm.userPersonGroups = data;
                                            }
                                        );
                                    }
                                );
                            }
                            return LoginService.updateLogin(vm.login);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    ).then(
                        function (data) {
                            vm.creating = false;
                            vm.person = data.person;
                            vm.tempPerson = angular.copy(vm.person);
                            activeLicenses();
                            vm.selectedUserNode.attributes.item = data;
                            if (data.external == false) {
                                vm.selectedUserNode.text = data.loginName;
                            } else {
                                vm.selectedUserNode.text = '<span style="color: orange !important;"> ' + data.loginName + ' </span>';
                            }
                            rootNode = 0;
                            initAdminTree();
                            $rootScope.showSuccessMessage(userUpdatedMessage);
                            vm.isUserEdit = !vm.isUserEdit;
                            vm.valid = true;

                            // to update default group on top nav bar
                            $rootScope.$broadcast("app.main.groups");
                        }
                    );
                }
            }


            function editUser() {
                if (vm.isUserEdit) {
                    vm.person.firstName = vm.tempPerson.firstName;
                    vm.person.lastName = vm.tempPerson.lastName;
                    vm.person.email = vm.tempPerson.email;
                    vm.person.phoneMobile = vm.tempPerson.phoneMobile;
                    vm.login.external = vm.tempExternal;
                }
                vm.isUserEdit = !vm.isUserEdit;

                vm.valid = true;
            }

            function updateGroup() {
                if (validateGroup()) {
                    vm.creating = true;

                    PersonGroupService.updatePersonGroup(vm.personGroup).then(
                        function (data) {
                            vm.creating = false;
                            $rootScope.updateModeType = false;
                            $rootScope.showSuccessMessage(groupUpdatedMessage);
                            loadAllGroups();
                            vm.selectedUserList = [];
                            PersonGroupService.getPersonGrpById(vm.personGroup.groupId).then(
                                function (data) {
                                    CommonService.getPersonReferences([data.profile], 'createdBy');
                                    vm.personGroup = data;
                                    vm.selectedUserNode.attributes.item = data;
                                    vm.selectedUserNode.attributes.item.type = "GROUP";
                                    vm.selectedUserNode.text = vm.personGroup.name;
                                    adminTree.tree('update', {
                                        target: vm.selectedUserNode.target,
                                        attributes: vm.selectedUserNode.attributes
                                    });
                                    LoginService.getLoginReferences(vm.personGroup.groupMember, 'person');
                                    vm.isGroupEdit = false;
                                }
                            );
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            editGroup();
                        }
                    );
                }
            }

            function updatePersonGroups() {
                vm.creating = true;
                PersonGroupService.updatePersonGroups(vm.person.id, selectedGroupMembers).then(
                    function (data) {
                        vm.creating = false;
                        PersonGroupService.getPersonGroupsByPersonId(vm.person.id).then(
                            function (data) {
                                vm.userPersonGroups = data;
                            }
                        );
                        //$rootScope.updateModeType = false;
                        $rootScope.showSuccessMessage(userUpdatedMessage);
                        initAdminTree();
                        /* loadUsers();
                         loadGroups();
                         loadAllGroups();*/
                    }
                );
            }


            function editGroup() {
                if (vm.isGroupEdit) {
                    PersonGroupService.getPersonGrpById(vm.personGroup.groupId).then(
                        function (data) {
                            vm.personGroup = data;
                        }
                    );
                }

                vm.isGroupEdit = !vm.isGroupEdit;
                $rootScope.updateModeType = true;

                vm.valid = true;
            }

            function onGroupSelectType(item) {
                if (item != null && item != undefined) {
                    vm.selectedGroup = item;
                    loadGroupsPermissions();
                }
            }

            function loadGroupsPermissions() {
                loadGroupsPermissionsGrid();
                GroupService.getAllGroups().then(
                    function (data) {
                        vm.groups = data;
                        // loadRolesGrid();
                    }
                );
            }

            function loadGroupsPermissionsGrid() {
                var url = "app/desktop/modules/groupsPermissionsGrid.json"
                GroupService.getGroupsGrid(url).then(
                    function (data) {
                        vm.permissionGroups = data;
                        vm.showPermissionGrid = true;
                        //createRolesMap();
                        createGroupsMap();
                    }
                );
            }

            function createGroupsMap() {
                vm.allGroupPermissions = [];
                vm.permissionMap.clear();
                angular.forEach(vm.permissionGroups, function (pgroup) {
                    angular.forEach(pgroup.permissions, function (permission) {
                        vm.allGroupPermissions.push(permission);
                        vm.permissionMap.put(permission.id, permission);
                    });
                });
                setValuesFromDBToGroup();

            }

            function setValuesFromDBToGroup() {
                for (var i = 0; i < vm.groups.length; i++) {
                    var allPermissions = angular.copy(vm.allGroupPermissions);

                    for (var j = 0; j < allPermissions.length; j++) {
                        allPermissions[j].selected = isPermissionAssignedToGroup(vm.groups[i], allPermissions[j]);
                    }
                    vm.groups[i].permissions = allPermissions;
                    for (var k = 0; k < vm.groups[i].permissions.length; k++) {
                        var p = vm.groups[i].permissions[k];
                        if (vm.groups[i].name == 'Administrator') {
                            p.disabled = true;
                            p.selected = true;
                        }
                    }
                }
            }

            function setValuesFromDB() {
                for (var i = 0; i < vm.roles.length; i++) {
                    var permissions = angular.copy(vm.allPermissions);

                    for (var j = 0; j < permissions.length; j++) {
                        permissions[j].selected = isPermissionAssignedToRole(vm.groups[i], permissions[j]);
                    }
                    vm.groups[i].permissions = permissions;
                    for (var k = 0; k < vm.groups[i].permissions.length; k++) {
                        var p = vm.groups[i].permissions[k];
                        if (vm.groups[i].name == "Administrator") {
                            p.disabled = true;
                        }
                        if (p.selected == true) {
                            toggleSubPermissions(vm.permissionGroups[i], p, true);
                        }
                    }
                }
            }

            function getPermissionIndexInGroup(group, permission) {
                var index = -1;
                if (group.permissions != null && group.permissions != undefined) {
                    for (var i = 0; i < group.permissions.length; i++) {
                        if (group.permissions[i].id == permission.id) {
                            index = i;
                        }
                    }
                }
                return index;
            }

            function getPermissionIndexInRole(role, permission) {
                var index = -1;
                for (var i = 0; i < role.permissions.length; i++) {
                    if (role.permissions[i].id == permission.id) {
                        index = i;
                    }
                }
                return index;
            }

            function toggleRolePermission(role, permission) {
                $rootScope.showBusyIndicator($("#userSettingsView"));
                if (permission.selected == true) {
                    toggleSubPermissions(role, permission, true);
                }
                else {
                    toggleSubPermissions(role, permission, false);
                }
            }

            function saveGroupPermissions() {
                var allPermissions = [];
                var groupPer = {
                    "id": {
                        "group": {
                            "groupId": null
                        },
                        "permission": {
                            "id": null
                        }
                    }
                };
                var copyGroups = angular.copy(vm.groups);
                for (var i = 0; i < copyGroups.length; i++) {
                    var group = copyGroups[i];
                    if (group.permissions != null && group.permissions != undefined) {
                        angular.forEach(group.permissions, function (permission) {
                            if (permission.selected == false || permission.selected == undefined ||
                                permission.selected == undefined) {
                                group.permissions.splice(group.permissions.indexOf(permission), 1);
                            }
                            else {
                                groupPer.id.group.groupId = group.groupId;
                                groupPer.id.permission.id = permission.id;
                                allPermissions.push(groupPer);
                            }
                        });
                    }
                }

                $timeout(function () {
                    PersonGroupService.createGroupPermissions(allPermissions).then(
                        function (data) {
                        }
                    )
                }, 1000)

            }


            function isPermissionAssignedToGroup(group, permission) {
                var assigned = false;
                var gps = vm.permissionGroupMap.get(group.groupId);
                for (var j = 0; j < gps.length; j++) {
                    if (gps[j].id.permission.id == permission.id) {
                        assigned = true;
                        break;
                    }
                }
                return assigned;
            }


            function isPermissionAssignedToRole(role, permission) {
                var assigned = false;
                /*for (var j = 0; j < vm.permissionGroups.length; j++) {
                 if ((vm.permissionGroups.get(i)).id.permission == permission.id) {
                 assigned = true;
                 break;
                 }
                 }*/
                angular.forEach(vm.groupPermissions, function (perm) {
                    if (perm.id.permission.id == permission.id) {
                        assigned = true;
                    }
                });
                return assigned;
            }

            function toggleSubPermissions(group, permission, flag) {
                vm.spinner.active = true;
                var groupPermissions = [];
                var groupPer = {
                    "id": {
                        "group": {
                            "groupId": null
                        },
                        "permission": {
                            "id": null
                        }
                    }
                };
                var index = permission.id.lastIndexOf(".all");
                if (index != -1) {
                    var root = permission.id.substring(0, index + 1);

                    for (var i = 0; i < group.permissions.length; i++) {
                        var p = group.permissions[i];

                        if (p.id.startsWith(root)) {
                            p.selected = flag;
                            groupPer = {
                                "id": {
                                    "group": {
                                        "groupId": group.groupId
                                    },
                                    "permission": {
                                        "id": p.id
                                    }
                                }
                            };
                            groupPermissions.push(groupPer);
                        }
                    }
                } else {
                    if (flag) {
                        addViewPermissionForAnyActionPermission(permission, group, groupPermissions);
                    }

                    groupPer = {
                        "id": {
                            "group": {
                                "groupId": group.groupId
                            },
                            "permission": {
                                "id": permission.id
                            }
                        }
                    };
                    groupPermissions.push(groupPer);
                    groupPer = {
                        "id": {
                            "group": {
                                "groupId": null
                            },
                            "permission": {
                                "id": null
                            }
                        }
                    };
                    var grpPermissions = [];
                    var selectedFlag = false;
                    var unSelectedFlag = false;
                    var subStrArray = [];
                    var subStr = null;
                    var p = null;
                    var count = 0;
                    var iPosition = 0;
                    var jPosition = 0;
                    var isSubString = false;
                    // to check/uncheck permission's "all" field
                    for (var i = 0; i < group.permissions.length; i++) {
                        if (i == 100) {
                            jPosition++;
                        }
                        subStrArray = [];
                        subStr = null;
                        p = group.permissions[i];
                        subStrArray = permission.id.split(".", 2);
                        for (var j = 0; j < subStrArray.length; j++) {
                            if (subStr == null) {
                                subStr = subStrArray[j] + "."
                            }
                            else {
                                subStr += subStrArray[j] + "."
                            }
                        }
                        if (p.id.startsWith(subStr)) {
                            grpPermissions.push(p);
                        }
                        subStr = subStr.concat("all");
                        if (p.id == subStr) {
                            iPosition = i;
                            isSubString = true;
                            groupPer.id.group.groupId = group.groupId;
                            groupPer.id.permission.id = subStr;
                        }
                    }
                    for (var perm = 0; perm < grpPermissions.length; perm++) {
                        if (count > 0) {
                            if (grpPermissions[perm].selected == true) {
                                selectedFlag = true;
                            }
                            else {
                                unSelectedFlag = true;
                            }
                        }
                        count++;
                    }

                    if (isSubString) {
                        if (selectedFlag == true && unSelectedFlag == false) {
                            group.permissions[iPosition].selected = true;
                        }
                        else {
                            group.permissions[iPosition].selected = false;
                        }
                    }
                }

                $timeout(function () {
                    if (flag == true) {
                        PersonGroupService.createGroupPermissions(groupPermissions).then(
                            function (data) {
                                vm.spinner.active = false;
                                $rootScope.hideBusyIndicator();
                                $rootScope.showInfoMessage(PermissionAddMsg);
                            }
                        )
                    } else {
                        PersonGroupService.deleteGroupPermissions(groupPermissions).then(
                            function (data) {
                                vm.spinner.active = false;
                                $rootScope.hideBusyIndicator();
                                $rootScope.showInfoMessage(PermissionRemoveMsg);
                            }
                        )
                    }
                }, 500);
            }

            function addViewPermissionForAnyActionPermission(permission, group, groupPermissions) {
                var index = permission.id.lastIndexOf(".");
                if (index != -1) {
                    var action1 = permission.id.substring(index + 1, permission.id.length);
                    if (action1 != "view" && action1 != "all") {
                        var root = permission.id.substring(0, index);
                        var selected = 0;
                        var count = 0;
                        for (var i = 0; i < group.permissions.length; i++) {
                            var p = group.permissions[i];
                            var index1 = p.id.lastIndexOf(".");
                            var r = p.id.substring(0, index1);
                            if (r == root) {
                                count = count + 1;
                                if (p.selected) {
                                    selected = selected + 1;
                                }
                            }
                        }
                        if (selected <= 1) {
                            root = root + ".view";
                            for (var i = 0; i < group.permissions.length; i++) {
                                var p = group.permissions[i];
                                if (p.id.startsWith(root)) {
                                    p.selected = true;
                                    groupPer = {
                                        "id": {
                                            "group": {
                                                "groupId": group.groupId
                                            },
                                            "permission": {
                                                "id": root
                                            }
                                        }
                                    };
                                    groupPermissions.push(groupPer);
                                }
                            }
                        } else if (count == selected + 1) {
                            root = root + ".all";
                            for (var i = 0; i < group.permissions.length; i++) {
                                var p = group.permissions[i];
                                if (p.id.startsWith(root)) {
                                    p.selected = true;
                                    groupPer = {
                                        "id": {
                                            "group": {
                                                "groupId": group.groupId
                                            },
                                            "permission": {
                                                "id": root
                                            }
                                        }
                                    };
                                    groupPermissions.push(groupPer);
                                }
                            }
                        }


                    }
                }
            }

            function deleteGroup(group) {
                PersonGroupService.deletePersonGroup(group.groupId).then(
                    function (data) {
                        vm.template = "";
                        $rootScope.showSuccessMessage(groupUserDeletedMessage);
                        adminTree.tree('remove', vm.selectedUserNode.target);
                    }
                )
            }

            function toggleNode(permission, permissions) {
                permission.collapsed = !permission.collapsed;
                for (var i = 1; i < permissions.length; i++) {
                    permissions[i].show = !permissions[i].show;
                }
            }

            vm.onSelectGroup = onSelectGroup;
            function onSelectGroup(item) {
                if (item.external) {
                    vm.login.external = true;
                } else {
                    vm.login.external = false;
                }
            }

            vm.onSelectProfile = onSelectProfile;
            function onSelectProfile(item) {
                vm.personGroup.profile = item;
            }

            function loadNoOfLicences(){
                LicenseService.getLicense().then(function(data){
                    vm.licenses = data.licenses
                })
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadNoOfLicences();
                activeLicenses();
                initAdminTree();
                //}
            })();
        }
    }
);