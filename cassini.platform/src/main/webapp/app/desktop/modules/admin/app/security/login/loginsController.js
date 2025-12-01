define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/login/loginRolesController'],

    function(module){

        module.controller('SecurityLoginsController',SecurityLoginsController);

        function SecurityLoginsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                          LoginService,CommonService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-key";
            $rootScope.viewInfo.title = "Login  ";

            //vm.setActiveFlag(0);

            vm.spinner = {active: false};
            vm.loginCreated = false;
            vm.showNewLoginForm = false;

            vm.selectedLogin = null;
            $scope.selectedLogin = null;

            vm.loading = true;
            vm.pageable = {
                page: 1,
                size: 10,
                sort: {
                    label: "loginTime",
                    field: "loginTime",
                    order: "desc"
                }
            };

            vm.pagedResults = {
                content: [],
                last: false,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: false,
                numberOfElements: 0
            };

            vm.emptyLogin = {
                loginName: null,
                person: null,
                phone: null,
                email: null
            };
            vm.newLogin = angular.copy(vm.emptyLogin);
            vm.persons = [];


            vm.logins = vm.pagedResults;
            vm.showRoles=showRoles;
            vm.pageChanged=pageChanged;
            vm.createAnother=createAnother;
            vm.createNewLogin=createNewLogin;
            vm.resetPassword=resetPassword;
            vm.cancelNewLogin= cancelNewLogin;
            vm.showNewLogin = showNewLogin;

            function pageChanged() {
                vm.loading = true;
                vm.logins.content = [];
                vm.loadLogins();
            };

            function loadLogins() {
                vm.loading = true;
                LoginService.getLogins(vm.pageable).then(
                    function (data) {
                        vm.logins = data;
                        vm.loading = false;
                        angular.forEach(vm.logins.content, function (item) {
                            item.showRoles=false;
                        });
                        vm.spinner.active = false;
                    }
                )
            };

            vm.rolesRow = {
                showRoles: false
            };

            function showRoles(login) {
                if (vm.rolesRow != null && vm.rolesRow.showRoles == true) {
                    closeRoles();
                }
                vm.selectedLogin = login;
                $scope.selectedLogin = login;
                var index = vm.logins.content.indexOf(vm.rolesRow);
                if (index != -1) {
                    vm.rolesRow.showRoles = false;
                    vm.logins.content.splice(index, 1);
                }

                vm.rolesRow.showRoles = true;
                index = vm.logins.content.indexOf(login);
                vm.logins.content.splice(index + 1, 0, vm.rolesRow);

                $rootScope.$broadcast("loginSelectionChanged", login);
            };

            $scope.closeRoles = function() {
                closeRoles();
            };

            function closeRoles() {
                $scope.selectedLogin = null;
                vm.selectedLogin = null;
                vm.rolesRow.showRoles = false;
                var index = vm.logins.content.indexOf(vm.rolesRow);
                vm.logins.content.splice(index, 1);
            };

            function loadPersons() {
                CommonService.getAllPersons().then(
                    function (data) {
                        vm.persons = data;
                    }
                );
            };

            function showNewLogin() {
                vm.showNewLoginForm = true;
                vm.loginCreated = false;
                vm.newLogin = angular.copy(vm.emptyLogin);
            };

            function createAnother() {
                vm.loginCreated = false;
                vm.newLogin = angular.copy(vm.emptyLogin);
            };

            function createNewLogin() {
                if (validateLogin()) {
                    vm.spinner.active = true;
                    LoginService.createLogin(vm.newLogin, vm.newLogin.phone, vm.newLogin.email).then(
                        function (data) {
                            vm.loginCreated = true;
                            $rootScope.showSuccessMessage("Login created for " +
                                vm.newLogin.person.firstName + " created successfully!");
                            vm.loadLogins();
                            vm.spinner.active = false;
                        },
                        function(error) {
                            $rootScope.showErrorMessage(error.message);
                            vm.spinner.active = false;
                        }
                    );
                }
            };

            function validateLogin() {
                var validated = true;
                if (vm.newLogin.person == null || vm.newLogin.person == "") {
                    $rootScope.showErrorMessage("Employee cannot be empty");
                    validated = false;
                }
                else if (vm.newLogin.loginName == null || vm.newLogin.loginName == "") {
                    $rootScope.showErrorMessage("Login name cannot be empty");
                    validated = false;
                }
                else if ((vm.newLogin.phone == null || vm.newLogin.phone == "") &&
                    (vm.newLogin.email == null || vm.newLogin.email == "")) {
                    $rootScope.showErrorMessage("Either a phone number or an email has to be provided to notify the user");
                    validated = false;
                }
                return validated;
            }

            function resetPassword(login) {
                LoginService.resetPassword(login).then(
                    function (data) {
                        login = data;
                    }
                )
            };

            function cancelNewLogin() {
                vm.showNewLoginForm = false;
            };

            (function() {
                //if($application.homeLoaded == true) {
                    loadLogins();
                    loadPersons();

                //}
            })();
        }
    }
)