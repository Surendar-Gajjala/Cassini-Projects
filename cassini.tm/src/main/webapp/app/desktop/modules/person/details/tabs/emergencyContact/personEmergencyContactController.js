define(['app/desktop/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/personService'
    ],
    function (module) {
        module.controller('PersonEmergencyContactController', PersonEmergencyContactController);

        function PersonEmergencyContactController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, CommonService,
                                                  PersonService) {

            var vm = this;
            vm.person = [];
            var personId = $stateParams.personId;
            vm.emergencyContact = null;
            vm.updateEmergencyContact = updateEmergencyContact;

            function loadEmergencyContact() {
                PersonService.getEmergencyContactByPersonId(personId).then(
                    function(data){
                        vm.person.emergencyContact = data;
                    }
                )
            }

            /*function loadPersons() {
                CommonService.getPerson(personId).then(
                    function (data) {
                        vm.person = data;
                        loadEmergencyContactByPersonId(vm.person);
                    }
                )
            }*/

            function updateEmergencyContact() {
                PersonService.updateEmergencyContact(vm.person.emergencyContact).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Person Updated Successfully!");
                    }
                )
            }

            (function () {
                loadEmergencyContact();
                $scope.$on('app.emergencyContact.update', function() {
                    updateEmergencyContact();

                })

            })();
        }
    }
);