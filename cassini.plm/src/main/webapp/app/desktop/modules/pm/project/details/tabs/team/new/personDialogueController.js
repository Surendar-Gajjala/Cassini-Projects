/**
 * Created by swapna on 1/11/18.
 */
define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'

    ],
    function (module) {
        module.controller('PersonDialogueController', PersonDialogueController);

        function PersonDialogueController($scope, $rootScope, $timeout, $translate, $state, LoginService, ProjectService, $stateParams, CommonService, ItemService) {

            var vm = this;
            vm.select = select;
            vm.create = create;
            vm.checkAll = checkAll;

            vm.loadPersons = loadPersons;

            var projectPersons = $scope.data.projecPersons;
            vm.selectedAll = false;
            vm.selectedPersons = [];
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            var personAddedSuccesMessage = parsed.html($translate.instant("PERSON_ADD_SUCCESS")).html();
            var personAddMsg = parsed.html($translate.instant("PERSON_ADD_MSG")).html();

            function select(person) {
                var flag = true;
                if (person.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedPersons.indexOf(person);
                    vm.selectedPersons.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedPersons, function (selectPerson) {
                        if (selectPerson.id == person.id) {
                            flag = false;
                            var index = vm.selectedPersons.indexOf(person);
                            vm.selectedPersons.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedPersons.push(person);
                    }
                    if (vm.selectedPersons.length == vm.persons.content.length) {
                        vm.selectedAll = true;
                    }

                }
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedPersons = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.persons.content, function (person) {
                        person.person.selected = vm.selectedAll;
                        vm.selectedPersons.push(person.person);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.persons.content, function (person) {
                        person.person.selected = vm.selectedAll;
                        vm.selectedPersons = [];
                    });
                }
            }

            function create() {
                if (vm.selectedPersons.length != 0) {
                    ProjectService.createProjecMembers($stateParams.projectId, vm.selectedPersons).then(
                        function (data) {
                            $rootScope.hideSidePanel('right');
                            $scope.callback(data);
                            vm.selectedPersons = [];
                            vm.creating = false;
                            $rootScope.showSuccessMessage(personAddedSuccesMessage);
                            $scope.$off('app.person.new', create);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                            $scope.$off('app.person.new', create);
                        }
                    )

                } else {
                    $rootScope.showWarningMessage(personAddMsg);
                }
            }


            /*function loadPersons() {
             vm.loading = true;
             vm.selectedPersons = [];
             CommonService.getAllActivePersons().then(
             function (data) {
             vm.loading = false;
             vm.persons = data;
             angular.forEach(projectPersons.content, function (projectPerson) {
             angular.forEach(vm.persons, function (person) {
             if (person.id == projectPerson.person) {
             var index = vm.persons.indexOf(person);
             vm.persons.splice(index, 1);
             }
             });

             });
             }, function (error) {
             $rootScope.showErrorMessage(error.message);
             $rootScope.hideBusyIndicator();
             }
             )
             }*/


            vm.freeTextSearch = freeTextSearch;
            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;


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
            vm.persons = angular.copy(pagedResults);

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
                vm.persons = angular.copy(pagedResults);
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
                else {
                    resetPage();
                }
            }

            function loadPersons() {
                vm.persons = [];
                vm.clear = false;
                vm.loading = true;
                LoginService.getAllFilteredActiveLogins(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.persons = data;
                        angular.forEach(projectPersons.content, function (projectPerson) {
                            angular.forEach(vm.persons.content, function (person) {
                                if (person.person.id == projectPerson.person) {
                                    vm.persons.totalElements = vm.persons.totalElements - 1;
                                    vm.persons.numberOfElements = vm.persons.numberOfElements - 1;
                                    var index = vm.persons.content.indexOf(person);
                                    vm.persons.content.splice(index, 1);
                                }
                            });

                        });


                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }


            function onOk() {
                $timeout(function () {
                    if (vm.selectedPersons.length > 0) {
                        $rootScope.hideSidePanel();
                        $scope.callback(vm.selectedPersons);
                        $scope.$off('app.person.new', onOk);
                    }
                    else {
                        $rootScope.showWarningMessage("Select atleast one team member")
                    }
                }, 100)

            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadPersons();
                $scope.$on('app.person.new', onOk);
                //}
            })();
        }

    }
);