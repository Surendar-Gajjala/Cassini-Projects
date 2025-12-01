define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService'
    ],
    function (module) {
        module.controller('UpdateLicenceController', UpdateLicenceController);

        function UpdateLicenceController($scope, $rootScope, $state, $stateParams, $window, $timeout, $cookies, $translate,
                                         $application, LoginService, CommonService, LicenseService, PreferenceService) {
            var vm = this;

            vm.hasError = false;
            vm.errorMessage = "";

            vm.submitText = 'Submit';
            $rootScope.preferences = [];
            vm.resetLicence = resetLicence;
            vm.cancel = cancel;
            var parsed = angular.element("<div></div>");
            $rootScope.licenceKeyValidationMessage = parsed.html($translate.instant("LICENCE_KEY_VALIDATION")).html();

            function loadPreferences() {
                $rootScope.preferences = [];
                CommonService.getPreferenceByContext("APPLICATION").then(
                    function (data) {
                        $rootScope.preferences = data;
                        if ($rootScope.licensesFraud) {
                            $window.localStorage.setItem('validateLicense', false);
                            PreferenceService.getPreferenceByKey('SYSTEM.CASSINI.LICENSE').then(
                                function (data) {
                                    if (data != null && data != "") {
                                        vm.licenceKey = data.stringValue;
                                        resetLicence();
                                    }
                                })
                        }
                    }
                )
            }

            function cancel() {
                LoginService.logout().then(
                    function (success) {
                        $rootScope.hasLicenseError = false;
                        $state.go('login', {}, {reload: true});
                    },
                    function (error) {
                    }
                );
            }

            $rootScope.updateUserForLicense = updateUserForLicense;
            function updateUserForLicense(user) {
                if (!user.isSuperUser) {
                    user.checkedLicence = true;
                    LoginService.updateLogin(user).then(
                        function (data) {
                        }, function (error) {
                            $rootScope.hasLicenseError = true;
                            $rootScope.errorLicenseMessage = error.message;
                        }
                    )
                }
            }

            $rootScope.activeUsers = [];
            function getActiveUsers() {
                $rootScope.activeUsers = [];
                LoginService.getInternalActiveLogins().then(
                    function (data) {
                        $rootScope.activeUsers = data;
                    },
                    function (error) {
                    }
                );
            }

            $rootScope.cancelUserModal = cancelUserModal;
            function cancelUserModal() {
                $('#active-user-modal').modal('hide');
                // $rootScope.errorLicenseMessage = "";
            }


            $rootScope.validateActiveUsers = validateActiveUsers;
            function validateActiveUsers() {
                LoginService.getInternalActiveLogins().then(
                    function (data) {
                        if (data.length <= $rootScope.licenses) {
                            $('#active-user-modal').modal('hide');
                            updateLicense();
                        } else {
                            $rootScope.hasLicenseError = true;
                            $rootScope.errorLicenseMessage = "You have only " + " " + $rootScope.licenses + " " + "  user licenses, Please de-activate remaining users and proceed further";
                        }
                    },
                    function (error) {
                    }
                );
            }

            function updateLicense() {
                LicenseService.saveLicense(vm.licenceKey).then(
                    function () {
                        $window.localStorage.setItem('validateLicense', true);
                        continueNextStep();
                    },
                    function (error) {
                        $rootScope.hasLicenseError = true;
                        $rootScope.errorLicenseMessage = error.message;
                    }
                )
            }

            vm.licenceKey = null;
            vm.licenseDto = {licenseKey: null};
            function resetLicence() {
                if (vm.licenceKey != null && vm.licenceKey != "") {
                    vm.licenseDto.licenseKey = vm.licenceKey;
                    checkActiveUserLicenses(vm.licenseDto);
                }
                else {
                    $rootScope.hasLicenseError = true;
                    $rootScope.errorLicenseMessage = $rootScope.licenceKeyValidationMessage;
                }
            }

            $rootScope.licenses = null;
            function checkActiveUserLicenses(licenseDto) {
                $rootScope.licenses = null;
                LicenseService.checkActiveUserLicenses(licenseDto).then(
                    function (data) {
                        $rootScope.licenses = data.licenses;
                        if (!data.valid) {
                            updateLicense();
                        } else {
                            //show popup
                            $rootScope.hasLicenseError = true;
                            if ($rootScope.loginPersonDetails.isAdmin) {
                                getActiveUsers();
                                $('#active-user-modal').modal('show');
                                $rootScope.errorLicenseMessage = "You have only" + " " + $rootScope.licenses + " " + " user licenses, Please de-activate remaining users and proceed further";
                            }
                            else {
                                $rootScope.errorLicenseMessage = "You have only " + " " + $rootScope.licenses + " " + " user licenses, Please contact admin";
                            }

                        }
                    },
                    function (error) {
                        $rootScope.hasLicenseError = true;
                        $rootScope.errorLicenseMessage = error.message;
                    }
                )
            }

            vm.continueNextStep = continueNextStep;
            function continueNextStep() {
                $window.localStorage.setItem('validateLicense', true);
                if (window.$application.session.login.person.email == null ||
                    window.$application.session.login.person.email === undefined ||
                    window.$application.session.login.person.email === "") {
                    $timeout(function () {
                        $rootScope.showUpdateEmail(window.$application.session.login.person, "update");
                    }, 1000)
                }
                else {
                    if (!window.$application.session.login.person.emailVerified) {
                        $timeout(function () {
                            $rootScope.showUpdateEmail(window.$application.session.login.person, "verify");
                        }, 1000)
                    } else if ($application.session != null && $application.session != undefined && $application.session.hasTwoFactorAuthentication) {
                        $rootScope.showTwoFactorAuthentication("sendPasscode");
                    } else if ($rootScope.sharedUrl != null && $rootScope.sharedUrl != "" && $rootScope.sharedUrl != undefined) {
                        if (localStorage.getItem('local_storage_login') != null) {
                            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                        }
                        $window.location.href = "/#" + $rootScope.sharedUrl;
                    } else {
                        $state.go('app.home', {}, {reload: true});
                    }
                }
            }

            (function () {
                loadPreferences();
            })();

        }
    }
);