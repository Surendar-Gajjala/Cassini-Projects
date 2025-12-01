define(['app/app.modules',
    'app/components/admin/security/role/roleFactory'],
    function (app) {
        app.controller('RolesController',
            [
                '$scope', '$q', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                'roleFactory',

                function ($scope, $q, $rootScope, $timeout, $interval, $state, $stateParams,
                          roleFactory) {
                    $scope.setActiveFlag(1);

                    $scope.spinner = {active: false};
                    $scope.showGrid = true;
                    $scope.showNewRoleForm = false;
                    $scope.loading = true;
                    $scope.rolesMap = new Hashtable();
                    $scope.allPermissions = [];

                    $scope.roles = [
                    ];

                    $scope.newRole = {
                        name: null,
                        description: null
                    };

                    $scope.permissionGroups = [];

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('roles-view-tb');
                    });

                    $scope.toggleNode = function(permission, permissions) {
                        permission.collapsed = !permission.collapsed;
                        for(var i=1; i<permissions.length; i++) {
                            permissions[i].show = !permissions[i].show;
                        }
                    };

                    $rootScope.addNewRole = function() {
                        $scope.showNewRoleForm = true;
                        $scope.showGrid = false;
                    };

                    $scope.hideNewRoleForm = function() {
                        $scope.showNewRoleForm = false;
                        $scope.showGrid = true;

                        $scope.newRole = {
                            name: null,
                            description: null
                        };
                    };

                    $scope.createNewRole = function() {
                        if($scope.newRole.name != null &&
                                $scope.newRole.name != "") {
                            roleFactory.createRole($scope.newRole).then (
                                function(role) {
                                    role.permissions = angular.copy($scope.allPermissions);
                                    $scope.roles.push(role);
                                }
                            );
                        }

                        $scope.hideNewRoleForm();
                    };

                    $scope.loadRolesGrid = function() {
                        roleFactory.getRolesGrid().then (
                            function(data) {
                                $scope.permissionGroups = data;
                                $scope.loading = false;

                                createRolesMap();
                            }
                        );
                    };

                    $scope.toggleRolePermission = function(role, permission) {
                        if(permission.selected == true) {
                            $scope.toggleSubPermissions(role, permission, true);
                        }
                        else {
                            $scope.toggleSubPermissions(role, permission, false);
                        }
                    };

                    $scope.toggleSubPermissions = function(role, permission, flag) {
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


                    $scope.getPermissionIndexInRole = function(role, permission) {
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
                        $scope.allPermissions = [];
                        $scope.rolesMap.clear();
                        angular.forEach($scope.permissionGroups, function(group) {
                            angular.forEach(group.permissions, function(permission) {
                                $scope.allPermissions.push(permission);
                                $scope.rolesMap.put(permission.id, permission);
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
                        for(var i=0; i<$scope.roles.length; i++) {
                            var permissions = angular.copy($scope.allPermissions);

                            for(var j=0; j<permissions.length; j++) {
                                permissions[j].selected = isPermissionAssignedToRole($scope.roles[i], permissions[j]);
                            }

                            $scope.roles[i].permissions = permissions;

                            for(var k=0; k<$scope.roles[i].permissions.length; k++) {
                                var p = $scope.roles[i].permissions[k];
                                if($scope.roles[i].name == "Administrator") {
                                    p.disabled = true;
                                }

                                if(p.selected == true){
                                    $scope.toggleSubPermissions($scope.roles[i], p, true);
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

                    $scope.hasHiddenPermissions = function(role, permission, permissions) {
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
                        roleFactory.getAllRoles().then(
                            function(data) {
                                $scope.roles = data;
                                $scope.loadRolesGrid();
                            }
                        );
                    }



                    $rootScope.saveRoles = function() {
                        $scope.spinner.active = true;
                        var copyRoles = angular.copy($scope.roles);

                        for(var i=0; i<copyRoles.length; i++) {
                            var role = copyRoles[i];
                            role.permissions.remove(function (permission) {
                                return (permission.selected == false ||
                                    permission.selected == undefined ||
                                    permission.selected == null);
                            });
                        }

                        roleFactory.saveRoles(copyRoles).then (
                            function(data) {
                                $rootScope.showSuccessMessage("Roles and permissions saved successfully!");
                                $scope.spinner.active = false;
                            }
                        );
                    };

                    (function() {
                        loadRoles();
                    })();
                }
            ]
        );
    }
);