define(['app/app.modules',
        'app/components/login/loginFactory',
        'app/components/admin/security/role/roleFactory'
    ],
    function (app) {
        app.controller('LoginRolesController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                'loginFactory', 'roleFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                          loginFactory, roleFactory) {

                    $scope.loading = true;
                    $scope.roles = [];

                    var map = new Hashtable();

                    function loadRoles() {
                        $scope.loading = true;
                        $scope.roles = [];
                        map = new Hashtable();

                        roleFactory.getAllRoles().then(
                            function(data) {
                                $scope.roles = data;
                                angular.forEach($scope.roles, function(role) {
                                    role.selected = false;
                                    map.put(role.id, role);
                                });
                                $scope.loading = false;

                                return roleFactory.getLoginRoles($scope.selectedLogin);
                            }
                        ).then(
                            function(loginRoles) {
                                angular.forEach(loginRoles, function(loginRole) {
                                    var role = loginRole.id.role;
                                    var fromMap = map.get(role.id);
                                    if(fromMap != null && fromMap != undefined) {
                                        fromMap.selected = true;
                                    }
                                });
                            }
                        )
                    }

                    $scope.isAdminRoleSelected = function() {
                        var selected = false;
                        angular.forEach($scope.roles, function(role) {
                            var name = role.name;
                            if(name == 'Administrator') {
                                if(role.selected == true) {
                                    selected = true;
                                }
                            }
                        });

                        return selected;
                    };

                    $scope.canModifyOtherAdmin = function() {
                        var modify = true;
                        if($scope.selectedLogin.loginName != 'admin' && $scope.isAdminRoleSelected() == true &&
                            (app.session.login.isSuperUser == null ||
                            app.session.login.isSuperUser == false)) {
                            modify = false;
                        }

                        return modify;
                    };

                    $scope.isRoleDisabled = function(role) {
                        var disabled = false;

                        if(role.name == 'Administrator' &&
                            (app.session.login.isSuperUser == null ||
                            app.session.login.isSuperUser == false)) {
                            disabled = true;
                        }
                        else if($scope.isAdminRoleSelected() == true &&
                            (app.session.login.isSuperUser == null ||
                                app.session.login.isSuperUser == false)) {
                            disabled = true;
                        }
                        else if($scope.selectedLogin.loginName == 'admin' ||
                                (role.name != 'Administrator' && $scope.isAdminRoleSelected() == true)) {
                            disabled = true;
                        }

                        return disabled;
                    };


                    $scope.saveLoginRoles = function() {
                        var selectedRoles = [];
                        angular.forEach($scope.roles, function(role) {
                            if(role.selected == true) {
                                selectedRoles.push(role);
                            }
                        });

                        roleFactory.saveLoginRoles($scope.selectedLogin, selectedRoles).then(
                            function(data) {
                                $rootScope.showSuccessMessage("Selected roles have been assigned to the login successfully!");
                                $scope.closeRoles()
                            }
                        );
                    };

                    $scope.$on("loginSelectionChanged", function(event, data) {
                        $scope.selectedLogin = data;
                        loadRoles();
                    });


                    (function() {
                        loadRoles();
                    })();

                }
            ]
        );
    }
);