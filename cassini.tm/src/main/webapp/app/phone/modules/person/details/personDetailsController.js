define(
    [
        'app/phone/modules/person/person.module',
        'app/shared/services/personService',
        'app/shared/services/departmentService',
        'app/shared/services/accommodationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application'
    ],
    function(module) {
        module.controller('PersonDetailsController', PersonDetailsController);

        function PersonDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                         DepartmentService, PersonService, CommonService, AccommodationService) {


            $rootScope.viewName = "Person Details";
            $rootScope.backgroundColor = "#00bcd4";

            var vm = this;

            vm.person = null;
            vm.emergencyContact = null;
            vm.otherInfo = null;
            vm.assignedAccommodation = {
                accommodation: null,
                suite: null,
                bed: null
            };
            vm.disableFields = true;
            vm.departments = [];


            function loadPersonInfo() {
                var personId = $stateParams.personId;
                CommonService.getPerson(personId).then(
                    function(data) {
                        vm.person = data;
                        vm.person.department = null;
                        return PersonService.getEmergencyContactByPersonId(personId);
                    }
                ).then(
                    function(data) {
                        vm.emergencyContact = data;
                        return PersonService.getPersonOtherInfoByPersonId(personId);
                    }
                ).then (
                    function(data) {
                        vm.otherInfo = data;
                        loadDepartment(vm.person, data);
                        checkPermission();
                        return AccommodationService.getBedByAssignedTo(vm.person.id);
                    }
                ).then (
                    function(data) {
                        if(data != null && data != "") {
                            vm.assignedAccommodation.bed = data;
                            AccommodationService.getSuitById("null", data.suite).then (
                                function(data) {
                                    vm.assignedAccommodation.suite = data;
                                    AccommodationService.getAccommodation(data.accommodation).then (
                                        function(data) {
                                            vm.assignedAccommodation.accommodation = data;
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }

            function loadDepartment(person, data){
                DepartmentService.getDepartmentById(data.department).then(
                    function(data){
                        person.department = data;
                    }
                )

            }

            function checkPermission() {
                if(vm.person.id == $application.login.person.id ||
                        $application.login.loginName == 'admin' ||
                        vm.person.firstName == "Administrator" ||
                        vm.otherInfo.role == "Administrator") {
                    vm.disableFields = false;
                }
            }

            function loadDepartments() {
                DepartmentService.getAllDepartments().then(
                    function(data) {
                        vm.departments = data;
                    }
                )
            }


            (function() {
                loadPersonInfo();
                loadDepartments();
            })();
        }
    }
);