define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('PersonSelectionController', PersonSelectionController);

        function PersonSelectionController($scope, $rootScope, $stateParams, $translate, $timeout, CommonService, WorkflowService, LoginService) {
            var vm = this;

            vm.persons = [];
            var personType = $scope.data.selectedPersonType;
            var type = $scope.data.selectedType;
            var parsed = angular.element("<div></div>");
            var personSelection = parsed.html($translate.instant("PLEASE_SELECT_ONE_PERSON")).html();
            var personAddedApprovers = parsed.html($translate.instant("ADDED_APPROVERS")).html();
            var personAddedObserver = parsed.html($translate.instant("ADDED_OBSERVERS")).html();
            var personAddedAcknowledger = parsed.html($translate.instant("ADDED_ACKNOWLEDGER")).html();

            vm.onOk = onOk;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };

            vm.filters = {
                typeId: type.id,
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
            vm.persons = angular.copy(pagedResults);

            vm.existedNumbers = [];
            var persons = "";

            function nextPage() {
                vm.pageable.page++;
                loadPersons();
            }

            function previousPage() {
                vm.pageable.page--;
                loadPersons();
            }
            function onOk() {
                vm.existedNumbers = [];
                persons = "";
                if (vm.selectPersons.length == 0) {
                    $rootScope.showWarningMessage(personSelection);
                }
                if (vm.selectPersons.length > 0) {
                    angular.forEach(vm.selectPersons, function (person) {
                        if (vm.existedNumbers.length == 0) {
                            persons = persons + " " + person.firstName;
                        } else {
                            persons = persons + ", " + person.firstName;
                        }
                        vm.existedNumbers.push(person);
                    });
                    $scope.callback(vm.selectPersons);
                    if (personType == 'approvers') {
                        $rootScope.showSuccessMessage(persons + ":" + personAddedApprovers);
                    } else if (personType == 'observers') {
                        $rootScope.showSuccessMessage(persons + ":" + personAddedObserver);
                    } else if (personType == 'acknowledgers') {
                        $rootScope.showSuccessMessage(persons + ":" + personAddedAcknowledger);
                    }
                }
            }

            vm.selectAllCheck = false;
            vm.selectPersons = [];
            function selectCheck(person) {
                var flag = true;
                angular.forEach(vm.selectPersons, function (persons) {
                    if (persons.id == person.id) {
                        flag = false;
                        var index = vm.selectPersons.indexOf(person);
                        vm.selectPersons.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectPersons.push(person);
                }

                if (vm.selectPersons.length == vm.persons.content.length) {
                    vm.selectAllCheck = true;
                } else {
                    vm.selectAllCheck = false;
                }
            }

            function selectAll(check) {
                vm.selectPersons = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.persons.content, function (person) {
                        person.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.persons.content, function (person) {
                        person.selected = true;
                        vm.selectPersons.push(person);
                    })
                }
            }

            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadPersons();

                } else {
                    resetPage();
                }
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.persons = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadPersons();
            }

            function loadPersons() {
                WorkflowService.getStatusAssigmentPersons(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.persons = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.workflow.persons', onOk);
                loadPersons();
                //}
            })();
        }
    }
);