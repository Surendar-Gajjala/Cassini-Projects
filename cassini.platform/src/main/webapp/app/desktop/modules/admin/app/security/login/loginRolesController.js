define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/roleService'
         ],

    function(module){

        module.controller('LoginRolesController',LoginRolesController);

        function LoginRolesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                  LoginService,RoleService) {
            var vm = this;

            vm.selectedLogin = $scope.$parent.selectedLogin;
            vm.loading = true;
            vm.roles = [];
            vm.login={};

            vm.canModifyOtherAdmin = canModifyOtherAdmin;
            vm.isRoleDisabled = isRoleDisabled;
            vm.isAdminRoleSelected = isAdminRoleSelected;
            var map = new Hashtable();

            function loadRoles() {
                vm.loading = true;
                vm.roles = [];
                map = new Hashtable();

                RoleService.getAllRoles().then(
                    function(data) {
                        vm.roles = data;
                        angular.forEach(vm.roles, function(role) {
                            role.selected = false;
                            map.put(role.id, role);
                        });
                        vm.loading = false;

                        return RoleService.getLoginRoles(vm.selectedLogin);
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

            function isAdminRoleSelected() {
                var selected = false;
                angular.forEach(vm.roles, function(role) {
                    var name = role.name;
                    if(name == 'Administrator') {
                        if(role.selected == true) {
                            selected = true;
                        }
                    }
                });

                return selected;
            };

            function canModifyOtherAdmin() {
                var modify = true;

                if(vm.selectedLogin.loginName != 'admin' && isAdminRoleSelected() == true &&
                    ($application.login.isSuperUser == null ||
                    $application.login.isSuperUser == false)) {
                    modify = false;
                }

                return modify;
            };

            function isRoleDisabled(role) {
                var disabled = false;

             /*   LoginService.getLogin($stateParams.loginId).then(
                    function(data) {
                        vm.loading = false;
                        vm.login=data;
                    }
                );
*/
           /*     if(role.name == 'Administrator' &&
                    (app.session.login.isSuperUser == null ||
                    app.session.login.isSuperUser == false)) {
                    disabled = true;
                }
                else if(vm.isAdminRoleSelected() == true &&
                    (app.session.login.isSuperUser == null ||
                    app.session.login.isSuperUser == false)) {
                    disabled = true;
                }
                else if(vm.selectedLogin.loginName == 'admin' ||
                    (role.name != 'Administrator' && vm.isAdminRoleSelected() == true)) {
                    disabled = true;
                }
*/
                if(role.name == 'Administrator' &&
                    ($application.login.isSuperUser == null ||
                    $application.login.isSuperUser == false)) {
                    disabled = true;
                }
                else if(vm.isAdminRoleSelected() == true &&
                    ($application.login.isSuperUser == null ||
                    $application.login.isSuperUser == false)) {
                    disabled = true;
                }
                else if(vm.selectedLogin.loginName == 'admin' ||
                    (role.name != 'Administrator' && vm.isAdminRoleSelected() == true)) {
                    disabled = true;
                }

                return disabled;
            };


            vm.saveLoginRoles = function() {
                var selectedRoles = [];
                angular.forEach(vm.roles, function(role) {
                    if(role.selected == true) {
                        selectedRoles.push(role);
                    }
                });

                RoleService.saveLoginRoles(vm.selectedLogin, selectedRoles).then(
                    function(data) {
                        $rootScope.showSuccessMessage("Selected roles have been assigned to the login successfully!");
                        $scope.$parent.closeRoles();
                    }
                );
            };

            $scope.$on("loginSelectionChanged", function(event, data) {
                vm.selectedLogin = data;
                loadRoles();
            });

            (function() {
                //if($application.homeLoaded == true) {
                    loadRoles();

                //}
            })();
        }
    }
)