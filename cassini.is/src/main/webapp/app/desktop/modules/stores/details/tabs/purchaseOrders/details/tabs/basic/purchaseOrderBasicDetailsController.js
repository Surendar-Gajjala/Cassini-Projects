/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/customPurchaseOrderService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('PurchaseOrderBasicDetailsController', PurchaseOrderBasicDetailsController);

        function PurchaseOrderBasicDetailsController($scope, $rootScope, $window, $state, $stateParams, CustomPurchaseOrderService) {

            var vm = this;
            vm.purchaseOrder = null;
            vm.update = update;

            function update() {
                CustomPurchaseOrderService.updateCustomPurchaseOrder(vm.purchaseOrder).then(
                    function (data) {
                        $rootScope.purchaseOrder = data;
                    });
            }

            function loadPurchaseOrdert() {
                CustomPurchaseOrderService.getPurchaseOrder($rootScope.purchaseOrderId).then(
                    function (data) {
                        $rootScope.purchaseOrder = data
                        vm.purchaseOrder = data;
                    }
                )
            }

            function back() {
                $window.history.back();
            }

            function validatePurchaseOrder() {
                var valid = true;
                if (vm.purchaseOrder.approvedBy == null || vm.purchaseOrder.approvedBy == undefined || vm.purchaseOrder.approvedBy == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Approved By");
                }

                return valid;
            }

            function approvePurchaseOrder() {
                if (validatePurchaseOrder()) {
                    vm.purchaseOrder.status = 'APPROVED';
                    CustomPurchaseOrderService.updateCustomPurchaseOrder(vm.purchaseOrder).then(
                        function (data) {
                            $rootScope.purchaseOrder = data;
                            $rootScope.showSuccessMessage("Purchase Order (" + data.poNumber + ") approved successfully");
                        }, function (error) {
                        });
                }
            }

            (function () {
                loadPurchaseOrdert();
                $scope.$on('app.po.approve', approvePurchaseOrder);
            })();
        }
    }
)
;