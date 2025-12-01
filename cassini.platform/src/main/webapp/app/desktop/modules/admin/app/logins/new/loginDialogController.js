define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService', 'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'],

    function (module) {


        module.controller('LoginDialogController', LoginDialogController);

        function LoginDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance,
                                       LoginService, CommonService) {
            var vm = this;

            vm.creating = true;
            vm.valid = true;
            vm.error = "";

            vm.person = {
                firstName: null,
                lastName: null,
                phoneMobile: null,
                email: null,
                personType: 1

            };

            vm.login = {
                loginName: null,
                password: null,
                person: null

            };

            vm.create = create;
            vm.cancel = cancel;

            $rootScope.viewInfo.icon = "fa-key";
            $rootScope.viewInfo.title = "New Login";


            function validate() {
                vm.valid = true;
                if (vm.person.firstName == null) {
                    vm.valid = false;
                    vm.error = "FirstName Cannot Be Empty";
                }
                else if (vm.login.loginName == null) {
                    vm.valid = false;
                    vm.error = "Username is Mandatory";
                }
                else if (vm.login.password == null) {
                    vm.valid = false;
                    vm.error = "Password is Mandatory";
                }
                else if (vm.person.phoneMobile == null) {
                    vm.valid = false;
                    vm.error = "Mobile Number Cannot Be Empty";
                }

                return vm.valid;
            }

            function create() {
                if (validate()) {
                    vm.creating = true;
                    CommonService.createPerson(vm.person).then(
                        function (data) {
                            vm.login.person = data;
                            return LoginService.createLogin(vm.login, vm.person.phoneMobile, vm.person.email);

                        }
                    ).then(
                        function (data) {
                            vm.creating = false;
                            $uibModalInstance.close(data);
                        }
                    );

                }
            }


            /*  function cancel(){
             $state.go('app.admin.logins')
             }*/

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function () {

            })();
        }
    }
);
