define(['app/desktop/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/personService',
        'app/desktop/modules/person/changePasswordController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('PersonBasicInfoController', PersonBasicInfoController);

        function PersonBasicInfoController($scope, $rootScope, $timeout, $state, $uibModal, $stateParams, $cookies,
                                           CommonService, PersonService, LoginService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            var personId = $stateParams.personId;
            vm.person = null;
            vm.updatePerson = updatePerson;
            vm.showChangePassword = showChangePassword;

            function loadPersons() {
                vm.loading = true;
                CommonService.getPerson(personId).then(
                    function (data) {
                        vm.person = data;
                        loadLoginName(personId);
                        vm.loading = false;
                        $rootScope.viewInfo.title = "Person Details (" + vm.person.firstName + "  " + vm.person.lastName + ")";
                    }
                )
            }

            function loadLoginName(personId){
                LoginService.getLoginName(personId).then(
                    function(data){
                        vm.person.loginName = data;
                    }
                )
            }

            function showChangePassword(){
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/person/changePasswordView.jsp',
                    controller: 'ChangePasswordController as changePasswordVm',
                    size: 'md',
                    resolve: {
                        person: function () {
                            return personId;
                        }
                    }

                });
                modalInstance.result.then(
                    function (result) {
                        $rootScope.showSuccessMessage("Password Updated Successfully!");
                    }
                )
            }


            function updatePerson() {
                CommonService.updatePerson(vm.person).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Person Updated Successfully!");
                    }
                )
            }

            (function () {
                loadPersons();
                $scope.$on('app.person.update', function() {
                    updatePerson();

                })

            })();
        }
    }
);