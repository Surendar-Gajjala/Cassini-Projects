define(['app/desktop/modules/person/person.module',
        'app/desktop/modules/person/new/newPersonController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/personService',
        'app/shared/services/departmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'

    ],
    function (module) {
        module.controller('AllPersonsController', AllPersonsController);

        function AllPersonsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModal,
                                      CommonService,PersonService,DialogService,DepartmentService, LoginService) {

            $rootScope.viewInfo.icon = "fa flaticon-businessman276";
            $rootScope.viewInfo.title = "Persons";

            var vm = this;
            vm.showPersonDetails = showPersonDetails;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.deletePerson = deletePerson;
            vm.otherInfos = null;
            vm.emrcontacts = null;
            vm.loading = true;
            vm.previousPage =  previousPage;
            vm.nextPage = nextPage;
            vm.persons = [];
            vm.clearFilter = clearFilter;
            vm.clear = false;
            vm.department = null;
            vm.filters = {
                searchQuery: null,
                personType: 1
            }
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

            function resetPage() {
                pageable.page = 0;
            }

            function showPersonDetails(person) {
                $state.go("app.person.details",{personId : person.id});
            }

            function clearFilter(){
                loadPersons();
                vm.clear = false;

            }

            function deletePerson(person) {
                var options = {
                    title: 'Delete Person',
                    message: 'Are you sure you want to delete this Person?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        CommonService.deletePerson(person.id).then(
                            function (data) {
                                var index = vm.persons.content.indexOf(person);
                                vm.persons.content.splice(index, 1);
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                                loadPersons();
                            }
                        )
                    }
                });
            }

            function loadPersons() {
                vm.loading = false;
                CommonService.getPersonsByPersonType(1, pageable).then(
                    function(data){
                        vm.persons = data;
                        angular.forEach(vm.persons.content,function(person){
                            loadEmergencyContactByPersonId(person);
                            otherInfoByPersonId(person);
                            loadLoginName(person);
                        })
                    }
                )
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

            function loadLoginName(person){
                LoginService.getLoginName(person.id).then(
                    function(data){
                            person.loginName = data;
                    }
                )
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    CommonService.freeTextSearch(vm.filters, pageable).then(
                        function (data) {
                            vm.persons = data;
                            angular.forEach(vm.persons.content,function(person){
                                loadEmergencyContactByPersonId(person);
                                otherInfoByPersonId(person);
                            })
                        }
                    )
                } else {
                    resetPage();
                    loadPersons();
                }
                vm.clear = true;
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
