define(['app/desktop/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('ChangePasswordController', ChangePasswordController);

        function ChangePasswordController($scope, $rootScope, $uibModalInstance, $uibModal, $timeout,
                                          $window, $interval, $state, $cookies, person, LoginService) {

            var vm = this;
            vm.personId = person;
            vm.submitChangePassword = submitChangePassword;
            vm.cancelChangePassword = cancelChangePassword;
            vm.password = {
                oldPassword: "",
                newPassword: "",
                confirmPassword: ""
            };
            vm.error = {
                hasError: false,
                errorMessage: ""
            };

            function validatePassword() {
                var valid = true;
                if (vm.password.oldPassword == vm.password.newPassword) {
                    vm.error.errorMessage = "Old and new passwords cannot be the same.";
                    vm.error.hasError = true;
                    valid = false;
                }
                else if (vm.password.newPassword != vm.password.confirmPassword) {
                    vm.error.errorMessage = "New password and confirm password are not the same";
                    vm.error.hasError = true;
                    valid = false;
                }

                return valid;
            }

            function cancelChangePassword() {
                $uibModalInstance.dismiss('cancel');
            }


            function submitChangePassword() {
                vm.error.hasError = false;

                if (validatePassword()) {
                    LoginService.changePasswordByPersonId(vm.personId, vm.password.oldPassword, vm.password.newPassword).then(
                        function (data) {
                            $uibModalInstance.close(data);
                        },
                        function (error) {
                            vm.error.errorMessage = error.message;
                            vm.error.hasError = true;
                        });
                }
                ;
            };
        }
    }
);
