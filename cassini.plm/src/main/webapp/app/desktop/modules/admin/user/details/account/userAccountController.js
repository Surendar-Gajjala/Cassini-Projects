define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService'
    ],
    function (module) {
        module.controller('UserAccountController', UserAccountController);

        function UserAccountController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application, $i18n,
                                       $state, $stateParams, $cookies, CommonService, PersonGroupService, LicenseService, LoginService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var invalidUsername = parsed.html($translate.instant("INVALID_USERNAME")).html();
            var firstNameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var userNameValidation = parsed.html($translate.instant("USER_NAME_VALIDATION")).html();
            var passwordValidation = parsed.html($translate.instant("PASSWORD_VALIDATION_FORMAT")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            var emailCannotEmpty = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var externalValidation = parsed.html($translate.instant("EXTERNAL_CHECK_BOX_VALIDATION")).html();
            var defaultGroupValidation = parsed.html($translate.instant("DEFAULT_GROUP_VALIDATION")).html();
            var userCreatedMessage = parsed.html($translate.instant("USER_CREATED_MESSAGE")).html();
            var usernameExists = parsed.html($translate.instant("USERNAME_EXISTS")).html();
            var userUpdatedMessage = parsed.html($translate.instant("USER_UPDATED_MESSAGE")).html();
            var accountUpdatedMessage = parsed.html($translate.instant("ACCOUNT_UPDATED_MESSAGE")).html();
            var licenceWarningMessage = parsed.html($translate.instant("LICENCE_WARNING_MESSAGE")).html();
            $scope.selectGroup = parsed.html($translate.instant("SELECT_GROUP_TITLE")).html();
            $scope.yes = $i18n.getValue("YES");
            $scope.no = $i18n.getValue("NO");

            var userId = $stateParams.userId;
            vm.copiedPerson = null;
            vm.copiedLoginName = null;
            vm.licenses = 1;
            vm.activeLicenses = 0;
            function loadPerson() {
                vm.copiedPerson = null;
                CommonService.getPerson(userId).then(
                    function (data) {
                        vm.person = data;
                        vm.copiedPerson = angular.copy(data);
                        vm.copiedLoginName = $rootScope.loginDetails.loginName;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function objectsAreEqual(original, copied) {
                var valid = false;
                if (!angular.equals(original.firstName, copied.firstName) || !angular.equals(original.lastName, copied.lastName)
                    || !angular.equals($rootScope.loginDetails.loginName, vm.copiedLoginName) || !angular.equals(original.phoneMobile, copied.phoneMobile)
                    || !angular.equals(original.email, copied.email) || !angular.equals(original.defaultGroup, copied.defaultGroup)
                    || !angular.equals(original.defaultGroup, copied.defaultGroup)) {
                    valid = true
                }
                return valid;
            }

            vm.updateUser = updateUser;
            function updateUser() {
                if (validateUser()) {
                    $rootScope.showBusyIndicator();
                    CommonService.updatePerson(vm.person).then(
                        function (data) {
                            vm.person = data;
                            $rootScope.loginDetails.person.defaultGroup = vm.person.defaultGroup;
                            $rootScope.loginDetails.isAdmin = $rootScope.loginDetails.isSuperUser;
                            LoginService.updateLogin($rootScope.loginDetails).then(
                                function (data) {
                                    $rootScope.loginDetails.loginName = data.loginName;
                                    // $rootScope.showSuccessMessage(accountUpdatedMessage);
                                    $rootScope.hideBusyIndicator();

                                    if (vm.person.id == $rootScope.personInfo.id && objectsAreEqual(vm.person, vm.copiedPerson)) {
                                        performLogout();
                                        $rootScope.showSuccessMessage(accountUpdatedMessage);
                                    }
                                    else {
                                        vm.copiedPerson = angular.copy(vm.person);
                                        $rootScope.showSuccessMessage(userUpdatedMessage);

                                    }

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.loginDetails.loginName = vm.copiedLoginName;
                                    loadPerson();
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.loginDetails.loginName = vm.copiedLoginName;
                            loadPerson();
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function performLogout() {
                $timeout(function () {
                    LoginService.logout().then(
                        function (success) {
                            $rootScope.$broadcast("app.notifications.logout");
                            $state.go('login', {}, {reload: true});
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }, 2000);
            }

            function validateUser() {
                vm.valid = true;

                if (vm.person.firstName == null || vm.person.firstName == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(firstNameValidation);
                }
                else if ($rootScope.loginDetails.loginName == null || $rootScope.loginDetails.loginName == "" || $rootScope.loginDetails.loginName == undefined) {
                    vm.valid = false;
                    $rootScope.showErrorMessage(userNameValidation);
                }
                else if (vm.person.defaultGroup == null || vm.person.defaultGroup == "" || vm.person.defaultGroup == undefined) {
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
                }

                if ($rootScope.loginDetails.loginName != null) {
                    if (safeCharsUserNameCheck() == false) {
                        $rootScope.showErrorMessage(invalidUsername);
                        vm.valid = false;
                    }
                }

                return vm.valid;

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

            function safeCharsUserNameCheck() {
                if ($rootScope.loginDetails.loginName.includes("@")) {
                    return false;
                }
                else {
                    $scope.safeCharsUserName = false;
                    return true;
                }

            }

            vm.onSelectGroup = onSelectGroup;
            function onSelectGroup(group) {
                if (group.external) {
                    $rootScope.loginDetails.external = true;
                } else {
                    $rootScope.loginDetails.external = false;
                }
            }


            function loadGroups() {
                PersonGroupService.getAllPersonGroups().then(
                    function (data) {
                        vm.groups = [];
                        angular.forEach(data, function (group) {
                            if (group.isActive) {
                                vm.groups.push(group);
                            }
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function activeLicenses() {
                LoginService.getIsActiveAndExternalLoginsCount(true, false).then(
                    function (data) {
                        vm.activeLicenses = data;
                    })
            }

            function loadNoOfLicences() {
                LicenseService.getLicense().then(function (data) {
                    vm.licenses = data.licenses
                })
            }

            vm.userActivation = userActivation;
            function userActivation(loginDetails) {
                if (!loginDetails.external) {
                    if (loginDetails.isActive) {
                        if (vm.licenses <= vm.activeLicenses) {
                            loginDetails.isActive = false;
                            $rootScope.showWarningMessage(licenceWarningMessage);
                        } else {
                            vm.activeLicenses++;
                        }
                    } else {
                        vm.activeLicenses--;
                    }
                }
            }

            (function () {
                loadPerson();
                loadGroups();
                activeLicenses();
                loadNoOfLicences();
            })();
        }
    }
);