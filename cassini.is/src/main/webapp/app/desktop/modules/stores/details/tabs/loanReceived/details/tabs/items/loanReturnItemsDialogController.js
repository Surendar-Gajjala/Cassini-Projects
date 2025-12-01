/**
 * Created by swapna on 21/08/18.
 */
/**
 * Created by swapna on 12/08/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/loanService'

    ],
    function (module) {
        module.controller('LoanReturnItemsDialogController', LoanReturnItemsDialogController);

        function LoanReturnItemsDialogController($scope, $rootScope, $timeout, $window, $state, $cookies, $stateParams,
                                                 LoanService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";

            vm.back = back;
            vm.storeId = $rootScope.storeId;
            vm.items = [];
            var loan = null;
            vm.validate = validate;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function back() {
                $window.history.back();
            }

            function loadLoanReceivedItems() {
                LoanService.getLoanReturnItemsDetails($rootScope.storeId, $stateParams.loanId, pageable).then(
                    function (items) {
                        vm.items = items.content;
                        angular.forEach(vm.items, function (item) {
                            item.project = loan.toProject;
                            item.balancedQuantity = 0;
                            item.balancedQuantity = item.itemIssueQuantity - item.itemReturnQuantity;
                            if (item.itemIssueQuantity == item.itemReturnQuantity) {
                                var index = vm.items.indexOf(item);
                                vm.items.splice(index, 1);
                            }
                        })
                    }
                )
            }

            function returnLoan() {
                if (vm.items.length > 0) {
                    $scope.callback(vm.items);
                }
            }

            function loadLoan() {
                LoanService.getLoanById($rootScope.storeId, $stateParams.loanId).then(
                    function (data) {
                        loan = data;
                    })
            }

            function validate(item) {
                if (item.Qty != undefined && item.Qty != null && item.Qty <= 0) {
                    item.Qty = null;
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadLoan();
                    loadLoanReceivedItems();
                    $rootScope.$on('app.stores.return', returnLoan)
                }
            })();
        }
    }
)
;