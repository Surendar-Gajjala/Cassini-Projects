define(['app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/ecoService'
    ],
    function (module) {
        module.controller('PersonSelectionController', PersonSelectionController);

        function PersonSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                           $uibModal, LoginService, CommonService) {

            var vm = this;
            vm.freeTextSearch = freeTextSearch;
            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var objectId = $scope.data.existObjectId;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.filters = {
                type: '',
                searchQuery: null
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.searchTerm = null;
            vm.selectedObj = null;
            vm.ecos = angular.copy(pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadPersons();
            }

            function previousPage() {
                vm.pageable.page--;
                loadPersons();
            }


            function clearFilter() {
                loadPersons();
                $scope.freeTextQuery = null;
                vm.filters.searchQuery = null;
                vm.clear = false;
            }

            function resetPage() {
                vm.changes = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadPersons();
            }

            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadPersons();
                }
            }

            var parsed = angular.element("<div></div>");
            var pleaseSelectAtleastOneEco = parsed.html($translate.instant("PLEASE_SELECT_ATLEAST_ONE_OBJECT")).html();

            function selectRadioChange(person, $event) {
                radioChange(person, $event);
                selectRadio();
            }

            function radioChange(person, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === person) {
                    person.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = person;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select person");
                }
            }


            function loadPersons() {
                vm.clear = false;
                vm.loading = true;
                LoginService.getAllFilteredActiveLogins(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.persons = data;
                        var existObjectId = false;
                        angular.forEach(vm.persons.content, function (login) {
                            if (objectId != null && objectId != "" && objectId != undefined && objectId == login.person.id) {
                                vm.persons.content.splice(vm.persons.content.indexOf(login), 1);
                                existObjectId = true;
                            }
                        });
                        if (existObjectId) {
                            vm.persons.totalElements = vm.persons.totalElements - 1;
                            vm.persons.numberOfElements = vm.persons.numberOfElements - 1;
                        }


                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function selectRadioChange(person, $event) {
                radioChange(person, $event);
                selectRadio();
            }

            function radioChange(person, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === person) {
                    person.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = person;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select atleast one person");
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.select.person', selectRadio);
                loadPersons();
                //}
            })();
        }
    }
)
;

