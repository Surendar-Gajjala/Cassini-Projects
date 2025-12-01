/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/topStockReceivedService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/supplier/supplierService',
        'app/shared/services/store/customPurchaseOrderService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ReceiveSelectionController', ReceiveSelectionController);

        function ReceiveSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                            TopStockReceivedService, ProjectService, CustomPurchaseOrderService, SupplierService, TopStoreService) {

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
                first: true,
                numberOfElements: 0
            };

            vm.stockReceives = angular.copy(pagedResults);

            function loadStockReceives() {
                vm.clear = false;
                vm.loading = true;
                TopStockReceivedService.getAllStockReceives($stateParams.storeId, pageable).then(
                    function (data) {
                        vm.stockReceives = data;
                        ProjectService.getProjectReferences(vm.stockReceives.content, 'project');
                        TopStoreService.getStoreReferences(vm.stockReceives.content, 'store');
                        //CustomPurchaseOrderService.getPurchaseOrderReferences(vm.stockReceives.content, 'purchaseOrderNumber');
                        //SupplierService.getSupplierReferences(vm.stockReceives.content, 'supplier');
                        angular.forEach(vm.stockReceives.content, function (stockReceive) {
                            stockReceive.checked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.stockReceives = [];
                    TopStockReceivedService.freeSearch(pageable, freeText).then(
                        function (data) {
                            vm.stockReceives = data;
                            vm.clear = true;
                            ProjectService.getProjectReferences(vm.stockReceives.content, 'project');
                            TopStoreService.getStoreReferences(vm.stockReceives.content, 'store');
                            //CustomPurchaseOrderService.getPurchaseOrderReferences(vm.stockReceives.content, 'purchaseOrderNumber');
                            //SupplierService.getSupplierReferences(vm.stockReceives.content, 'supplier');
                            angular.forEach(vm.stockReceives.content, function (stockReceive) {
                                stockReceive.checked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadStockReceives();
                }
            }

            function clearFilter() {
                loadStockReceives();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(stockReceive, $event) {
                radioChange(stockReceive, $event);
                selectRadio();
            }

            function radioChange(stockReceive, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === stockReceive) {
                    stockReceive.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = stockReceive;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select stockReceive");
                }
            }

            function nextPage() {
                if (vm.stockReceives.last != true) {
                    pageable.page++;
                    loadStockReceives();
                }
            }

            function previousPage() {
                if (vm.stockReceives.first != true) {
                    pageable.page--;
                    loadStockReceives();
                }
            }

            (function () {
                $rootScope.$on('app.receive.selected', selectRadio);
                loadStockReceives();
            })();
        }
    }
)
;

