/**
 * Created by swapna on 01/10/18.
 */

define(['app/desktop/modules/stores/store.module',
        'app/shared/services/core/storeService',
        'app/shared/services/store/loanService',
        'app/shared/services/pm/project/projectService'

    ],
    function (module) {
        module.controller('LoanItemsDialogController', LoanItemsDialogController);

        function LoanItemsDialogController($scope, $rootScope, $timeout, $window, $state, $cookies, $stateParams, TopInventoryService, LoanService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";

            vm.back = back;
            vm.storeId = $rootScope.storeId;
            vm.items = [];
            vm.showItemDetails = showItemDetails;
            vm.loan = null;
            var loanItems = [];

            var pageable = {
                page: 0,
                size: 20
            };

            function back() {
                $window.history.back();
            }

            function loadLoanIssueItems() {
                LoanService.getLoanItems($rootScope.storeId, $stateParams.loanId, pageable).then(
                    function (stockMovements) {
                        loanItems = stockMovements;
                    }
                )
            }

            function loadStoreInventoryforLoan() {
                TopInventoryService.getStoreInventoryforLoan($rootScope.storeId, vm.loan.fromProject).then(
                    function (data) {
                        angular.forEach(data, function (inventory) {
                            if (inventory.storeOnHand > 0) {
                                vm.items.push(inventory);
                            }
                        })
                    })

            }

            function showItemDetails(item) {
                item.itemDTO.loanIssuedOn = new Date();
                item.itemDTO.quantity = item.quantity;
                var options = {
                    title: 'Loan Issued Item Details',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
                    controller: 'ItemBasicInfoController as itemBasicInfoVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
                    width: 600,
                    data: {
                        item: item.itemDTO
                    },

                    callback: function () {
                        //loadProperties();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadLoan() {
                LoanService.getLoanById($rootScope.storeId, $stateParams.loanId).then(
                    function (data) {
                        vm.loan = data;
                        loadStoreInventoryforLoan();
                    });
            }

            function addItems() {
                $scope.callback(vm.items);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadLoanIssueItems();
                    loadLoan();
                    $rootScope.$on('app.stores.addLoanItems', addItems);
                }
            })();
        }
    }
)
;