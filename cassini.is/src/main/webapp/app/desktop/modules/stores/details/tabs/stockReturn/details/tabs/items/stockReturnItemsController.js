/**
 * Created by swapna on 05/12/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/stockReturnService'

    ],
    function (module) {
        module.controller('StockReturnItemsController', StockReturnItemsController);

        function StockReturnItemsController($scope, $rootScope, $window, $state, $stateParams, StockReturnService) {
            var vm = this;

            vm.requisition = null;
            vm.back = back;
            $rootScope.stockReturnItemsList = [];
            vm.isNew = false;
            vm.loading = false;

            $rootScope.hasNewItems = false;

            var pageable = {
                page: 0,
                size: 20
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

            $rootScope.stockReturnItemsList = angular.copy(pagedResults);

            function loadStockReturnItems() {
                vm.loading = true;
                StockReturnService.getStockReturnItems($rootScope.storeId, $stateParams.stockReturnId, pageable).then(
                    function (data) {
                        $rootScope.stockReturnItemsList = data;
                        vm.loading = false;
                    }
                )
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'RET'});
            }

            function nextPage() {
                if ($rootScope.stockReturnItemsList.last != true) {
                    pageable.page++;
                    loadStockReturnItems();
                }
            }

            function previousPage() {
                if ($rootScope.stockReturnItemsList.first != true) {
                    pageable.page--;
                    loadStockReturnItems();
                }
            }

            (function () {
                $rootScope.stockReturnItemsList = [];
                $scope.$on('app.stockReturn.items.nextPageDetails', nextPage);
                $scope.$on('app.stockReturn.items.previousPageDetails', previousPage);
                if ($application.homeLoaded == true) {
                    $scope.$on('app.stock.stockReturnItems', function (event, data) {
                        loadStockReturnItems();
                    });
                }
            })();
        }
    }
)
;