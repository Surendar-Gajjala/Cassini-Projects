/**
 * Created by swapna on 28/12/18.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/store/customPurchaseOrderService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('PurchaseOrderSelectionController', PurchaseOrderSelectionController);

        function PurchaseOrderSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies,
                                                  $uibModal, CustomPurchaseOrderService, TopStoreService) {

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

            vm.purchaseOrders = angular.copy(pagedResults);

            function loadPurchaseOrders() {
                vm.clear = false;
                vm.loading = true;
                CustomPurchaseOrderService.getAllPurchaseOrders(pageable).then(
                    function (data) {
                        vm.purchaseOrders = data;
                        TopStoreService.getStoreReferences(vm.purchaseOrders.content, 'store');
                        angular.forEach(data.content, function (purchaseOrder) {
                            purchaseOrder.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.purchaseOrders = [];
                    CustomPurchaseOrderService.purchaseOrderFreeTextSearch(pageable, freeText).then(
                        function (data) {
                            vm.purchaseOrders = data;
                            vm.clear = true;
                            TopStoreService.getStoreReferences(vm.purchaseOrders.content, 'store');
                            angular.forEach(data.content, function (purchaseOrder) {
                                purchaseOrder.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadPurchaseOrders();
                }
            }

            function clearFilter() {
                loadPurchaseOrders();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(purchaseOrder, $event) {
                radioChange(purchaseOrder, $event);
                selectRadio();
            }

            function radioChange(purchaseOrder, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === purchaseOrder) {
                    purchaseOrder.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = purchaseOrder;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.customIndents.last != true) {
                    pageable.page++;
                    loadPurchaseOrders();
                }
            }

            function previousPage() {
                if (vm.customIndents.first != true) {
                    pageable.page--;
                    loadPurchaseOrders();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.purchaseOrder.selected', selectRadio);
                    loadPurchaseOrders();
                }
            })();
        }
    }
)
;
