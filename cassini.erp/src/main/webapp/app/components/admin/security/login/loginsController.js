define(['app/app.modules',
        'app/components/login/loginFactory',
        'app/components/hrm/employee/employeeFactory',
        'app/components/admin/security/login/loginRolesController'
    ],
    function (app) {
        app.controller('LoginsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                'loginFactory', 'employeeFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                          loginFactory, employeeFactory) {

                    $scope.setActiveFlag(0);

                    $scope.spinner = {active: false};
                    $scope.loginCreated = false;
                    $scope.showNewLoginForm = false;

                    $scope.selectedLogin = null;

                    $scope.loading = true;
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "loginTime",
                            field: "loginTime",
                            order: "desc"
                        }
                    };

                    $scope.pagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.emptyLogin = {
                        loginName: null,
                        person: null,
                        phone: null,
                        email: null
                    };
                    $scope.newLogin = angular.copy($scope.emptyLogin);
                    $scope.employees = [];


                    $scope.logins = $scope.pagedResults;

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate("logins-view-tb");
                    });

                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        $scope.logins.content = [];
                        $scope.loadLogins();
                    };

                    $scope.loadLogins = function () {
                        $scope.loading = true;
                        loginFactory.getLogins($scope.pageable).then(
                            function (data) {
                                $scope.logins = data;
                                $scope.loading = false;
                                angular.forEach($scope.logins.content, function (item) {

                                });
                                $scope.spinner.active = false;
                            }
                        )
                    };

                    $scope.rolesRow = {
                        showRoles: false
                    };

                    $scope.showRoles = function (login) {
                        if ($scope.rolesRow != null && $scope.rolesRow.showRoles == true) {
                            $scope.closeRoles();
                        }
                        $scope.selectedLogin = login;

                        var index = $scope.logins.content.indexOf($scope.rolesRow);
                        if (index != -1) {
                            $scope.rolesRow.showRoles = false;
                            $scope.logins.content.splice(index, 1);
                        }

                        $scope.rolesRow.showRoles = true;
                        index = $scope.logins.content.indexOf(login);
                        $scope.logins.content.splice(index + 1, 0, $scope.rolesRow);

                        $scope.$broadcast("loginSelectionChanged", login);
                    };

                    $scope.closeRoles = function () {
                        $scope.selectedLogin = null;
                        $scope.rolesRow.showRoles = false;
                        var index = $scope.logins.content.indexOf($scope.rolesRow);
                        $scope.logins.content.splice(index, 1);
                    };

                    $scope.loadEmployees = function () {
                        employeeFactory.getAllEmployees().then(
                            function (data) {
                                $scope.employees = data;
                            }
                        );
                    };

                    $rootScope.showNewLogin = function () {
                        $scope.showNewLoginForm = true;
                        $scope.loginCreated = false;
                        $scope.newLogin = angular.copy($scope.emptyLogin);
                    };

                    $scope.createAnother = function () {
                        $scope.loginCreated = false;
                        $scope.newLogin = angular.copy($scope.emptyLogin);
                    };

                    $scope.createNewLogin = function () {
                        if (validateLogin()) {
                            $scope.spinner.active = true;
                            loginFactory.createLogin($scope.newLogin, $scope.newLogin.phone, $scope.newLogin.email).then(
                                function (data) {
                                    $scope.loginCreated = true;
                                    $rootScope.showSuccessMessage("Login created for " +
                                        $scope.newLogin.person.firstName + " created successfully!");
                                    $scope.loadLogins();
                                    $scope.spinner.active = false;
                                },
                                function(error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $scope.spinner.active = false;
                                }
                            );
                        }
                    };

                    function validateLogin() {
                        var validated = true;
                        if ($scope.newLogin.person == null || $scope.newLogin.person == "") {
                            $rootScope.showErrorMessage("Employee cannot be empty");
                            validated = false;
                        }
                        else if ($scope.newLogin.loginName == null || $scope.newLogin.loginName == "") {
                            $rootScope.showErrorMessage("Login name cannot be empty");
                            validated = false;
                        }
                        else if (($scope.newLogin.phone == null || $scope.newLogin.phone == "") &&
                            ($scope.newLogin.email == null || $scope.newLogin.email == "")) {
                            $rootScope.showErrorMessage("Either a phone number or an email has to be provided to notify the user");
                            validated = false;
                        }
                        return validated;
                    }

                    $scope.resetPassword = function (login) {
                        loginFactory.resetPassword(login).then(
                            function (data) {
                                login = data;
                            }
                        )
                    };

                    $scope.cancelNewLogin = function () {
                        $scope.showNewLoginForm = false;
                    };

                    (function () {
                        $scope.loadLogins();
                        $scope.loadEmployees();
                    })();

                }
            ]
        );
    }
);