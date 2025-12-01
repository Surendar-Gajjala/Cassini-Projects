define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
            'app/assets/bower_components/cassini-platform/app/shared/services/security/roleService'],

    function(module){

        module.controller('RolesController',RolesController);

        function RolesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                  RoleService) {
            var vm = this;

            vm.spinner = {active: false};
            vm.showGrid = true;
            vm.showNewRoleForm = false;
            vm.loading = true;
            vm.rolesMap = new Hashtable();
            vm.allPermissions = [];

            vm.roles = [
            ];

            vm.newRole = {
                name: null,
                description: null
            };

            vm.toggleNode=toggleNode;
            vm.hideNewRoleForm=hideNewRoleForm;
            vm.createNewRole=createNewRole;
            vm.toggleRolePermission=toggleRolePermission;
            vm.getPermissionIndexInRole=getPermissionIndexInRole;
            vm.hasHiddenPermissions=hasHiddenPermissions;
            vm.addNewRole = addNewRole;
            vm.saveRoles = saveRoles;
            vm.permissionGroups = [];


             function toggleNode(permission, permissions) {
                permission.collapsed = !permission.collapsed;
                for(var i=1; i<permissions.length; i++) {
                    permissions[i].show = !permissions[i].show;
                }
            };

            function addNewRole() {
                vm.showNewRoleForm = true;
                vm.showGrid = false;
            };

            function hideNewRoleForm() {
                vm.showNewRoleForm = false;
                vm.showGrid = true;

                vm.newRole = {
                    name: null,
                    description: null
                };
            };

            function createNewRole() {
                if (validateRole()) {
                    RoleService.createRole(vm.newRole).then(
                        function (role) {
                            role.permissions = angular.copy(vm.allPermissions);
                            vm.roles.push(role);
                        }
                    );
                }

                hideNewRoleForm();
            };

            function validateRole() {
                var validated = true;
                if (vm.newRole.name != null && vm.newRole.name != "") {
                    $rootScope.showErrorMessage("Name field cannot be empty");
                    validated = false
                }
                return validated;
            }

           function loadRolesGrid() {
                RoleService.getRolesGrid("").then (
                    function(data) {
                        vm.permissionGroups = data;
                        vm.loading = false;

                        createRolesMap();
                    }
                );
            };

             function toggleRolePermission(role, permission) {
                if(permission.selected == true) {
                    toggleSubPermissions(role, permission, true);
                }
                else {
                    toggleSubPermissions(role, permission, false);
                }
            };

            function toggleSubPermissions(role, permission, flag) {
                var index = permission.id.lastIndexOf(".all");
                if(index != -1) {
                    var root = permission.id.substring(0, index+1);

                    for(var i=0; i<role.permissions.length; i++) {
                        var p = role.permissions[i];

                        if(p.id.startsWith(root)) {
                            p.selected = flag;
                        }
                    }
                }
            };


            function getPermissionIndexInRole(role, permission) {
                var index = -1;
                for(var i=0; i<role.permissions.length; i++) {
                    if(role.permissions[i].id == permission.id) {
                        index = i;
                        //console.log("Role: " + role.name + " Permission: " + permission.id + " Index: " + index);
                    }
                }

                return index;
            };

            function createRolesMap() {
                vm.allPermissions = [];
                vm.rolesMap.clear();
                angular.forEach(vm.permissionGroups, function(group) {
                    angular.forEach(group.permissions, function(permission) {
                        vm.allPermissions.push(permission);
                        vm.rolesMap.put(permission.id, permission);
                    })
                });

                setValuesFromDB();

                /*
                 var str = "";
                 angular.forEach($scope.allPermissions, function(permission) {
                 var line = "{0},{1},{2}".format(permission.id, permission.name, permission.name);
                 str += line;
                 str += "\n";
                 });

                 console.log(str);
                 */

            }

            function setValuesFromDB() {
                for(var i=0; i<vm.roles.length; i++) {
                    var permissions = angular.copy(vm.allPermissions);

                    for(var j=0; j<permissions.length; j++) {
                        permissions[j].selected = isPermissionAssignedToRole(vm.roles[i], permissions[j]);
                    }

                    vm.roles[i].permissions = permissions;

                    for(var k=0; k<vm.roles[i].permissions.length; k++) {
                        var p = vm.roles[i].permissions[k];
                        if(vm.roles[i].name == "Administrator") {
                            p.disabled = true;
                        }

                        if(p.selected == true){
                            toggleSubPermissions(vm.roles[i], p, true);
                        }
                    }
                }
            }

            function isPermissionAssignedToRole(role, permission) {
                var assigned = false;

                for(var j=0; j<role.permissions.length; j++) {
                    if(role.permissions[j].id == permission.id) {
                        assigned = true;
                        break;
                    }
                }
                return assigned;
            }

            function hasHiddenPermissions(role, permission, permissions) {
                var hasHidden = false;

                var index = permission.id.lastIndexOf(".all");
                var map = new Hashtable();

                for (var m = 0; m < permissions.length; m++) {
                    map.put(permissions[m].id, permissions[m]);
                }

                if (index != -1) {
                    var root = permission.id.substring(0, index + 1);

                    for (var i = 0; i < role.permissions.length; i++) {
                        var p = role.permissions[i];

                        if(p.id == permission.id && p.selected == true) {
                            break;
                        }
                        else if (map.get(p.id) != null &&
                            map.get(p.id) != undefined &&
                            p.selected == true &&
                            p.id != permission.id &&
                            p.id.startsWith(root)) {
                            hasHidden = true;
                            break;
                        }
                    }
                }

                return hasHidden;
            };

            function loadRoles() {
                RoleService.getAllRoles().then(
                    function(data) {
                        vm.roles = data;
                        loadRolesGrid();
                    }
                );
            }



            function saveRoles() {
                vm.spinner.active = true;
                var copyRoles = angular.copy(vm.roles);

                for(var i=0; i<copyRoles.length; i++) {
                    var role = copyRoles[i];
                    role.permissions.remove(function (permission) {
                        return (permission.selected == false ||
                        permission.selected == undefined ||
                        permission.selected == null);
                    });
                }

                RoleService.saveRoles(copyRoles).then (
                    function(data) {
                        $rootScope.showSuccessMessage("Roles and permissions saved successfully!");
                        vm.spinner.active = false;
                    }
                );
            };



            (function() {
                //if($application.homeLoaded == true) {
                loadRoles();
                //}
            })();
        }
    }
)