/**
 * Created by Nageshreddy on 12-01-2021.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/desktop/modules/securityPermission/securityPermissionService'
    ],
    function (module) {
        module.controller('RolePermissionsController', RolePermissionsController);

        function RolePermissionsController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                           $state, $stateParams, $i18n, SecurityPermissionService, GroupService, DialogService, LoginService) {

            var vm = this;
            var roleId = $stateParams.roleId;
            vm.securityPermissions = [];
            vm.loading = false;
            vm.clear = false;
            vm.objectTypes = [];
            vm.subTypes = [];
            vm.filteredList = [];
            vm.deleteSecurityPermission = deleteSecurityPermission;
            vm.clearFilter = clearFilter;
            vm.addPermission = addPermission;
            vm.viewCriteria = viewCriteria;
            vm.loadFilteredPermission = loadFilteredPermission;
            vm.loadSubTypePermission = loadSubTypePermission;
            vm.editTitle = $i18n.getValue("EDIT");
            var addButton = $i18n.getValue("ADD");
            vm.objectTypeTitle = $i18n.getValue("OBJECT_TYPE");
            vm.subTypeTitle = $i18n.getValue("SUB_TYPE");
            var removePermission = $i18n.getValue("REMOVE_PERMISSION");
            var permissionWillRemoved = $i18n.getValue("PERMISSION_WILL_REMOVED");
            var permissionAdded = $i18n.getValue("PERMISSION_ADDED");
            var permissionRemoved = $i18n.getValue("PERMISSION_REMOVED");

            function deleteSecurityPermission(permission) {
                var options = {
                    title: removePermission,
                    message: '"' + permission.name + '"' + permissionWillRemoved,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            SecurityPermissionService.deleteGroupSecurityPermission(permission.id, roleId).then(
                                function (data) {
                                    loadRolePermission();
                                    $rootScope.showSuccessMessage(permissionRemoved)
                                }
                                ,
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                            ;
                        }
                    }
                )
                ;
            }

            function addPermission() {
                var options = {
                    title: "Permissions",
                    template: 'app/desktop/modules/admin/role/details/permissions/permissionSelectionView.jsp',
                    controller: 'PermissionSelectionController as permissionSelectionVm',
                    resolve: 'app/desktop/modules/admin/role/details/permissions/permissionSelectionController',
                    width: 1200,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'add.select.permissions'}
                    ],
                    data: {
                        groupId: roleId
                    },
                    callback: function (result) {
                        SecurityPermissionService.createMultiplePermissions(roleId, result).then(
                            function (data) {
                                loadRolePermission();
                                $rootScope.showSuccessMessage(permissionAdded)
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                };
                $rootScope.showSidePanel(options);
            }

            $rootScope.viewCriteria = viewCriteria;
            function viewCriteria(permission) {
                var options = {
                    title: "Permission Criteria",
                    template: 'app/desktop/modules/admin/permission/criteria/newCriteriaView.jsp',
                    controller: 'NewCriteriaController as newCriteriaVm',
                    resolve: 'app/desktop/modules/admin/permission/criteria/newCriteriaController',
                    width: 1000,
                    side: "left",
                    showMask: true,
                    data: {
                        mode: "view",
                        permission: permission,
                        className: $rootScope.typeNames[permission.objectType]
                    },
                    callback: function () {
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadFilteredPermission() {
                vm.clear = true;
                vm.loading = true;
                vm.filteredList = [];
                vm.securityPermissions = vm.resetPermissions;
                angular.forEach(vm.securityPermissions, function (value) {
                    if (vm.objectType != null && vm.subType == null) {
                        if (vm.objectType == value.objectType) vm.filteredList.push(value);
                    }
                    if (vm.objectType == null && vm.subType != null) {
                        if (vm.subType == value.subType) vm.filteredList.push(value);
                    }
                    if (vm.objectType != null && vm.subType != null) {
                        if (vm.objectType == value.objectType && vm.subType == value.subType) {
                            vm.filteredList.push(value);
                        }
                    }
                });
                if (vm.objectTypes.length > 0) {
                    vm.loading = false;
                    vm.securityPermissions = vm.filteredList;
                }

            }



            function loadSubTypePermission() {
                vm.clear = true;
                vm.loading = true;
                vm.filteredList = [];
                vm.subTypes = [];
                vm.subType = null;
                vm.securityPermissions = vm.resetPermissions;
                angular.forEach(vm.securityPermissions, function (value) {
                    if (vm.objectType != null && vm.subType == null) {
                        if (vm.objectType == value.objectType) vm.filteredList.push(value);
                    }
                    if (vm.objectType == null && vm.subType != null) {
                        if (vm.subType == value.subType) vm.filteredList.push(value);
                    }
                    if (vm.objectType != null && vm.subType != null) {
                        if (vm.objectType == value.objectType && vm.subType == value.subType) {
                            vm.filteredList.push(value);
                        }
                    }
                });
                if (vm.objectTypes.length > 0) {
                    vm.loading = false;
                    vm.securityPermissions = vm.filteredList;
                }
                angular.forEach(vm.securityPermissions, function (value) {
                  if(vm.subTypes.indexOf(value.subType) == -1)  vm.subTypes.push(value.subType);
                });

            }



            function clearFilter() {
                vm.objectType = null;
                vm.subType = null;
                vm.filteredList = [];
                vm.subTypes = [];
                vm.clear = false;
                loadRolePermission();
            }

            function loadRolePermission() {
                vm.loading = true;
                $scope.selecteObject = 'All';
                GroupService.getGroupPermissionsByGroupId(roleId).then(
                    function (data) {
                        vm.securityPermissions = data.securityPermissions;
                        vm.objectTypes = data.objectTypes;
                        vm.resetPermissions = data.securityPermissions;
                        angular.forEach(data.subTypes, function (value) {
                            if (value != null && value != "" && value != undefined) vm.subTypes.push(value);
                        });
                        if (vm.objectType != null && vm.subType == null) {
                            loadFilteredPermission();
                        }
                        if (vm.objectType == null && vm.subType != null) {
                            loadFilteredPermission();
                        }
                        if (vm.objectType != null && vm.subType != null) {
                            loadFilteredPermission();
                        }
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            (function () {
                loadRolePermission();
            })();
        }
    }
)
;