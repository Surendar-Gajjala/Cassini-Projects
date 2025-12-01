define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('PersonSelectionController', PersonSelectionController);

        function PersonSelectionController($scope, $rootScope, $timeout, LoginService, $state, $stateParams, $window, $cookies, $cookieStore,
                                           CommonService) {

            var vm = this;

            vm.loading = true;
            vm.selectedObj = null;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var personId = $scope.data.existObjectId;

            var criteria = {
                searchQuery: null,
                objectPerson: ''
            };
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.logins = angular.copy(pagedResults);

            function loadPersons() {
                vm.loading = true;
                if (personId != null && personId != "" && personId != undefined) {
                    criteria.objectPerson = personId;
                }
                CommonService.freeTextSearch(criteria, pageable).then(
                    function (data) {
                        vm.logins = data;
                        angular.forEach(vm.logins.content, function (person) {
                            person.checked = false;
                        });
                        vm.loading = false;
                    }
                )
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    criteria.searchQuery = freeText;
                    loadPersons();
                } else {
                    resetPage();
                    loadPersons();
                }
            }

            function clearFilter() {
                loadPersons();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
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
                    $rootScope.showWarningMessage("Please select person");
                }
            }

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

            (function () {
                $rootScope.$on('app.person.selected', selectRadio);
                loadPersons();
            })();
        }
    }
)
;

