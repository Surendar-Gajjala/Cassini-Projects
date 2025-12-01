/**
 * Created by swapna on 26/12/18.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('StoreSelectionController', StoreSelectionController);

        function StoreSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies,
                                          $uibModal, TopStoreService) {

            var vm = this;

            vm.loading = true;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.selectedObj = null;
            var criteria = {
                searchQuery: null
            }
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
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

            vm.stores = angular.copy(pagedResults);

            function loadStores() {
                vm.clear = false;
                vm.loading = true;
                TopStoreService.getPageableTopStores(pageable).then(
                    function (data) {
                        vm.stores = data;
                        angular.forEach(data.content, function (store) {
                            store.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.stores = [];
                    criteria.searchQuery = freeText;
                    TopStoreService.freeTextSearch(pageable, criteria).then(
                        function (data) {
                            vm.stores = data;
                            vm.clear = true;
                            angular.forEach(data.content, function (store) {
                                store.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadStores();
                }
            }

            function clearFilter() {
                loadStores();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(store, $event) {
                radioChange(store, $event);
                selectRadio();
            }

            function radioChange(store, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === store) {
                    store.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = store;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.stores.last != true) {
                    pageable.page++;
                    loadStores();
                }
            }

            function previousPage() {
                if (vm.stores.first != true) {
                    pageable.page--;
                    loadStores();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.store.selected', selectRadio);
                    loadStores();
                }
            })();
        }
    }
)
;
