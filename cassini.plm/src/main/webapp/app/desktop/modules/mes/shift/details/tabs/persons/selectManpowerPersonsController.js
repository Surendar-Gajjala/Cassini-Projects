/**
 * Created by swapna on 1/11/18.
 */
define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/shiftService',
        'app/shared/services/core/manpowerService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'

    ],
    function (module) {
        module.controller('SelectManpowerPersonsController', SelectManpowerPersonsController);

        function SelectManpowerPersonsController($scope, $rootScope, $timeout, $translate, $state, LoginService, ProjectService, $stateParams, CommonService, ShiftService, ManpowerService) {

            var vm = this;
            vm.select = select;
            vm.checkAll = checkAll;

            vm.loadManpowerPersons = loadManpowerPersons;

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
                        person.selected = vm.selectedAll;
                        vm.selectedPersons.push(person);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.persons.content, function (person) {
                        person.selected = vm.selectedAll;
                        vm.selectedPersons = [];
                    });
                }
            }

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
                shift: $stateParams.shiftId,
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
                loadManpowerPersons();
            }

            function previousPage() {
                vm.pageable.page--;
                loadManpowerPersons();
            }


            function clearFilter() {
                loadManpowerPersons();
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
                loadManpowerPersons();
            }

            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadManpowerPersons();
                }
                else {
                    resetPage();
                }
            }

            function loadManpowerPersons() {
                vm.persons = [];
                vm.clear = false;
                vm.loading = true;
                ManpowerService.getAllManpowerPersons(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.persons = data;
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
                        $scope.$off('app.select.manpower.person.add', onOk);
                    }
                    else {
                        $rootScope.showWarningMessage("Select atleast one person")
                    }
                }, 100)

            }

            (function () {
                loadManpowerPersons();
                $scope.$on('app.select.manpower.person.add', onOk);
                //}
            })();
        }

    }
);