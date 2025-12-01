/**
 * Created by swapna on 28/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/customPurchaseOrderService'
    ],

    function (module) {
        module.factory('PurchaseOrderSelector', PurchaseOrderSelector);

        function PurchaseOrderSelector(CustomPurchaseOrderService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling purchaseOrder selector dialog");
                var options = {
                    title: 'Select PurchaseOrder',
                    template: 'app/desktop/modules/select/customPurchaseOrder/purchaseOrderSelectionView.jsp',
                    controller: 'PurchaseOrderSelectionController as purchaseOrderSelectVm',
                    resolve: 'app/desktop/modules/select/customPurchaseOrder/purchaseOrderSelectionController',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.purchaseOrder.selected'}
                    ],
                    callback: function (selectedPurchaseOrder) {
                        callback(selectedPurchaseOrder, selectedPurchaseOrder.poNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                CustomPurchaseOrderService.getPurchaseOrder(id).then(
                    function (data) {
                        attribute.refValueString = data.poNumber;
                        attribute[attributeName] = data.poNumber;
                    }
                );
            }
        }
    }
);