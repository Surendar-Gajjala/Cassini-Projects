define(['app/desktop/modules/widgets/persons/personWidget.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/personService',
        'app/shared/services/departmentService'

    ],
    function (module) {
        module.controller('PersonsWidgetController', PersonsWidgetController);

        function PersonsWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, CommonService,PersonService,DepartmentService) {

            var vm = this;
            vm.persons = [];
            vm.showPersonDetails = showPersonDetails;
            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.persons = angular.copy(pagedResults);

            function nextPage() {
                if (vm.persons.last != true) {
                    pageable.page++;
                    loadPersons();
                }
            }

            function previousPage() {
                if (vm.persons.first != true) {
                    pageable.page--;
                    loadPersons();
                }
            }




            function showPersonDetails(person) {
                $state.go("app.person.details", {personId: person.id});
            }

            function loadPersons() {
                CommonService.getAllPersonsByPersonType(1).then(
                    function(data){
                        vm.persons = data;
                        angular.forEach(vm.persons,function(person){
                            loadEmergencyContactByPersonId(person);
                            otherInfoByPersonId(person);
                        })
                    }
                )
                vm.loading = false;
            }

            function loadEmergencyContactByPersonId(person) {
                PersonService.getEmergencyContactByPersonId(person.id).then(
                    function(data){
                        if(data != null && data != "") {
                            person.emergencyContact = data;
                        }
                    }
                )
            }

            function otherInfoByPersonId(person) {
                PersonService.getPersonOtherInfoByPersonId(person.id).then(
                    function(data){
                        if(data != null && data != "") {
                            getDepartment(person, data);
                            person.otherInfo = data;
                        }
                    }
                )
            }

            function getDepartment(person,data){
                DepartmentService.getDepartmentById(data.department).then(
                    function(data){
                        person.department = data;
                    }
                )

            }

            (function () {
                loadPersons();
            })();
        }
    }
);

