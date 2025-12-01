define(['app/desktop/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/personService',
        'app/shared/services/departmentService'
    ],
    function(module) {
        module.controller('PersonOtherInfoController', PersonOtherInfoController);

        function PersonOtherInfoController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,CommonService,
                                           PersonService, DepartmentService ) {
            var vm = this;

            vm.person = [];
            var personId = $stateParams.personId;

            function loadOtherInfo(person) {
                PersonService.getPersonOtherInfoByPersonId(personId).then(
                    function(data){
                        vm.person.otherInfo = data;
                    }
                )
            }

            /*function loadPersons() {
                CommonService.getPerson(personId).then(
                    function (data) {
                        vm.person = data;
                        loadOtherInfoByPersonId(vm.person);
                    }
                )
            }*/

            function updateOtherInfo() {
                PersonService.updatePersonOtherInfo(vm.person.otherInfo).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Person Updated Successfully!");
                    }
                )
            }

            (function() {
                loadOtherInfo();
                $scope.$on('app.otherInfo.update', function() {
                    updateOtherInfo();

                })
            })();
        }
    }
);