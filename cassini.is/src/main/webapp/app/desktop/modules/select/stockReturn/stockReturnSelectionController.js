/**
 * Created by swapna on 27/12/18.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/store/stockReturnService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('StockReturnSelectionController', StockReturnSelectionController);

        function StockReturnSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $uibModal,
                                                StockReturnService, ProjectService, TopStoreService) {

            var vm = this;

            vm.loading = true;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.selectedObj = null;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
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
                first: true,
                numberOfElements: 0
            };

            vm.stockReturns = angular.copy(pagedResults);

            function loadStockReturns() {
                vm.clear = false;
                vm.loading = true;
                StockReturnService.getPageableStockReturns(null, pageable).then(
                    function (data) {
                        vm.stockReturns = data;
                        ProjectService.getProjectReferences(vm.stockReturns.content, "project");
                        TopStoreService.getStoreReferences(vm.stockReturns.content, 'store');
                        angular.forEach(data.content, function (stockReturn) {
                            stockReturn.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.stockReturns = [];
                    StockReturnService.freeSearch(null, pageable, freeText).then(
                        function (data) {
                            vm.stockReturns = data;
                            vm.clear = true;
                            ProjectService.getProjectReferences(vm.stockReturns.content, "project");
                            TopStoreService.getStoreReferences(vm.stockReturns.content, 'store');
                            angular.forEach(data.content, function (stockReturn) {
                                stockReturn.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadStockReturns();
                }
            }

            function clearFilter() {
                loadStockReturns();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(stockReturn, $event) {
                radioChange(stockReturn, $event);
                selectRadio();
            }

            function radioChange(stockReturn, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === stockReturn) {
                    stockReturn.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = stockReturn;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.stockReturns.last != true) {
                    pageable.page++;
                    loadStockReturns();
                }
            }

            function previousPage() {
                if (vm.stockReturns.first != true) {
                    pageable.page--;
                    loadStockReturns();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.stockReturn.selected', selectRadio);
                    loadStockReturns();
                }
            })();
        }
    }
)
;
