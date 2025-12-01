/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/supplier/supplierService'
    ],

    function (module) {
        module.factory('SupplierSelector', SupplierSelector);

        function SupplierSelector(SupplierService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling supplier selector dialog");
                var options = {
                    title: 'Select Supplier',
                    template: 'app/desktop/modules/select/supplier/supplierSelectionView.jsp',
                    controller: 'SupplierSelectionController as supplierSelectVm',
                    resolve: 'app/desktop/modules/select/supplier/supplierSelectionController',
                    width: 600,
                    data: {},
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.supplier.selected'}
                    ],
                    callback: function (selectedSupplier) {
                        callback(selectedSupplier, selectedSupplier.name);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                SupplierService.getSupplier(id).then(
                    function (data) {
                        attribute.refValueString = data.name;
                        attribute[attributeName] = data.name;
                    }
                );
            }
        }
    }
);