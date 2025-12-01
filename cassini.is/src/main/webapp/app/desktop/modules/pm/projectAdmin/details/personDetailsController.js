define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('PersonDetailsController', PersonDetailsController);

        function PersonDetailsController($scope, $rootScope, $timeout, $state, $stateParams,
                                         ProjectService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "flaticon-prize3";
            $rootScope.viewInfo.title = "Person Details";

            var vm = this;

            $scope.uiRouterState = $state;
            vm.loading = true;
            vm.person = null;
            var personId = $stateParams.personId;
            vm.personRoles = [];
            vm.updatePerson = updatePerson;

            function loadProjectPerson() {
                vm.loading = false;
                CommonService.getPerson(personId).then(
                    function (data) {
                        vm.person = data;
                        vm.person.fullName = null;
                        if (vm.person.lastName == null || vm.person.lastName == "") {
                            vm.person.fullName = angular.copy(vm.person.firstName);

                        }
                        else {
                            vm.person.fullName = angular.copy(vm.person.firstName + " " + vm.person.lastName);
                        }
                        $rootScope.viewInfo.title = "Person Details - " + vm.person.fullName;
                    }
                );
                ProjectService.getRolesByPersonId($stateParams.projectId, personId).then(
                    function (data) {
                        vm.personRoles = data;
                        ProjectService.getRoleReferences($stateParams.projectId, vm.personRoles, 'role');
                    }
                )
            }

            function updatePerson() {
                var arr = [];
                var name1 = vm.person.fullName;
                name1 = name1.split(' ');
                arr.push(name1.shift());
                arr.push(name1.join(' '));
                if (arr[0] != vm.person.firstName) {
                    vm.person.firstName = arr[0];
                }
                if (arr[1] != vm.person.lastName) {
                    vm.person.lastName = arr[1];
                }
                CommonService.updatePerson(vm.person).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Person updated successfully");
                        loadProjectPerson();
                    }
                )
            }

            function validate() {
                var flag = true;
                if (vm.person.fullName == null || vm.person.fullName == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Person Name cannot be empty");
                    loadProjectPerson();
                }
                return flag;
            }

            /*    function updatePerson() {
             if(validate()) {
             CommonService.updatePerson(vm.person).then(
             function (data) {
             loadProjectPerson();
             $rootScope.showSuccessMessage("Person updated successfully");
             },function(error){
             $rootScope.showErrorMessage(error.message);
             }

             )
             loadProjectPerson();
             }
             }
             */

            (function () {
                loadProjectPerson();
            })();
        }
    }
);