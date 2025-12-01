define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'],

    function (module) {


        module.controller('PasswordResetController', PasswordResetController);

        function PasswordResetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                         LoginService, CommonService, $translate) {
            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.userList = [];
            vm.selectedList = [];
            vm.loading = false;
            var login = $scope.data.login;
            vm.create = create;
            var parsed = angular.element("<div></div>");
            var newPasswordValidation = parsed.html($translate.instant("NEW_PASSWORD_VALIDATION")).html();
            var notSamePasswordValidation = parsed.html($translate.instant("NOT_SAME_PASSWORD_VALIDATION")).html();
            var confirmPasswordValidation = parsed.html($translate.instant("CONFIRM_PASSWORD_VALID")).html();
            vm.passwordInformation = parsed.html($translate.instant("PASSWORD_INFORMATION")).html();

            vm.invalidPassword = parsed.html($translate.instant("INVALID_PASSWORD")).html();
            vm.invalidUsername = parsed.html($translate.instant("INVALID_USERNAME")).html();
            var passwordResetMsg = parsed.html($translate.instant("PASSWORD_RESET_MSG")).html();
            var changePasswordMessage = parsed.html($translate.instant("CHANGE_PASSWORD_MESSAGE")).html();

            vm.resetPassword = {
                confirmPassword: null,
                newPassword: null,
                email: null
            };

            vm.loginDto = {
                login: login,
                newPassword: null
            };
            var flag = true;
            function create() {
                if (validatePassword()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.loginDto.newPassword = vm.resetPassword.newPassword;
                    LoginService.passwordReset(vm.loginDto).then(
                        function () {
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            if(flag){
                                if($rootScope.localStorageLogin.login.loginName == login.loginName) {
                                    $rootScope.showSuccessMessage(changePasswordMessage);
                                    performLogout();
                                } else {
                                    $rootScope.showSuccessMessage(passwordResetMsg);
                                }
                                flag = false;
                            }
                        }, function (error) {
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            function validatePassword() {
                var valid = true;
                if (vm.resetPassword.newPassword == null || vm.resetPassword.newPassword == "") {
                    $rootScope.showWarningMessage(newPasswordValidation);
                    valid = false;
                } else if (vm.resetPassword.confirmPassword == null || vm.resetPassword.confirmPassword == "") {
                    $rootScope.showWarningMessage(confirmPasswordValidation);
                    valid = false;
                } else if (vm.resetPassword.newPassword != vm.resetPassword.confirmPassword) {
                    $rootScope.showWarningMessage(notSamePasswordValidation);
                    valid = false;

                }

                return valid;
            }


            vm.showPassword = showPassword;
            function showPassword() {
                var eyeIcon = document.getElementById("showPassword");
                var newPassword = document.getElementById("newPassword");
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

            vm.showConfirmPassword = showConfirmPassword;
            function showConfirmPassword() {
                var eyeIcon = document.getElementById("showConfirmPassword");
                var newPassword = document.getElementById("confirmPassword");
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

            function performLogout() {
                $timeout(function () {
                    LoginService.logout().then(
                        function (success) {
                            $rootScope.$broadcast("app.notifications.logout");
                            $state.go('login', {}, {reload: true});
                        },
                        function (error) {
                            console.error(error);
                        }
                    );
                }, 2000);
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.loadingUsers = false;
                    $rootScope.$on('app.reset.password', create);

                //}
            })();
        }
    }
)
;
