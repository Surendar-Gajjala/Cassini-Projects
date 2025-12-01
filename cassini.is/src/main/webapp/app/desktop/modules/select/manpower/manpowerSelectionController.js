define(['app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ManpowerSelectionController', ManpowerSelectionController);

        function ManpowerSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                             $uibModal, ItemService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
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

            var manpowerCriteria = {
                freeTextSearch: true
            };

            vm.selectedObj = null;
            vm.manpowerList = angular.copy(pagedResults);

            function nextPage() {
                if (vm.manpowerList.last != true) {
                    pageable.page++;
                    loadManpower();
                }
            }

            function previousPage() {
                if (vm.manpowerList.first != true) {
                    pageable.page--;
                    loadManpower();
                }
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    manpowerCriteria.searchQuery = freeText;
                    ItemService.manpowerFreeTextSearch(pageable, manpowerCriteria).then(
                        function (data) {
                            vm.manpowerList = data;
                            vm.clear = true;
                        }
                    )
                } else {
                    resetPage();
                    loadManpower();
                }
            }

            function clearFilter() {
                loadManpower();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function loadManpower() {
                vm.clear = false;
                vm.loading = true;
                ItemService.getManpower(pageable).then(
                    function (data) {
                        vm.manpowerList = data;
                        angular.forEach(data, function (manpower) {
                            manpower.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function selectRadioChange(manpower, $event) {
                radioChange(manpower, $event);
                selectRadio();
            }

            function radioChange(manpower, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === manpower) {
                    manpower.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = manpower;
                }
            }

            function selectRadio() {
                if (vm.selectedObj == null) {
                    $rootScope.showErrorMessage("Select atleast one ManPower");
                }
                else {
                    $scope.callback(vm.selectedObj);
                }
                $rootScope.hideSidePanel('left');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.manpower.selected', selectRadio);
                    loadManpower();
                }
            })();
        }
    }
)
;
