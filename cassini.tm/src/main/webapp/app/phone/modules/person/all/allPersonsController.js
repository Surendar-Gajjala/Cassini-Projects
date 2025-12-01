define(
    [
        'app/phone/modules/person/person.module',
        'app/shared/services/personService',
        'app/assets/bower_components/cassini-platform/app/phone/directives/mobileDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('AllPersonsController', AllPersonsController);

        function AllPersonsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                      $application, CommonService, PersonService) {

            $rootScope.viewName = "Persons";
            $rootScope.backgroundColor = "#00bcd4";

            var vm = this;

            vm.persons = [];
            vm.showPersonDetails = showPersonDetails;
            vm.search = search;
            vm.loading = true;

            vm.filters = {
                searchQuery: null,
                personType: 1
            };

            var pageable = {
                page: 0,
                size: 1000,
                sort: {
                    field: "modifiedDate"
                }
            };

            function showPersonDetails() {
                $state.go('app.person.details', {personId: 1});
            }

            function loadPersons() {
                vm.loading = true;
                vm.persons = [];
                CommonService.getAllPersonsByPersonType(1).then(
                    function(data){
                        vm.persons = data;
                        sortPersons(vm.persons);
                        vm.loading = false;
                    }
                )
            }

            function search() {
                if(vm.filters.searchQuery != null && vm.filters.searchQuery.trim() != "") {
                    vm.loading = true;
                    vm.persons = [];
                    CommonService.freeTextSearch(vm.filters, pageable).then(
                        function(data) {
                            vm.persons = data.content;
                            sortPersons(vm.persons);
                            vm.loading = false;
                        }
                    )
                }
                else {
                    loadPersons();
                }
            }

            function sortPersons(persons) {
                persons.sort(function(p1, p2) {
                    return p1.firstName.toLowerCase().localeCompare(p2.firstName.toLowerCase())
                });
            }

            (function () {
                loadPersons();
            })();
        }
    }
);