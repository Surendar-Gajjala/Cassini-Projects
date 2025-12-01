/**
 * Created by swapna on 29/01/19.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/core/subContractService'
    ],

    function (module) {
        module.factory('WorkOrderSelector', WorkOrderSelector);

        function WorkOrderSelector(SubContractService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling Work Order selector dialog");
                var options = {
                    title: 'Select Work Order',
                    template: 'app/desktop/modules/select/workOrder/workOrderSelectionView.jsp',
                    controller: 'WorkOrderSelectionController as workOrderSelectVm',
                    resolve: 'app/desktop/modules/select/workOrder/workOrderSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.workOrder.selected'}
                    ],
                    callback: function (selectedWorkOrder) {
                        callback(selectedWorkOrder, selectedWorkOrder.number);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                SubContractService.getWorkOrder(id).then(
                    function (data) {
                        attribute.refValueString = data.number;
                        attribute[attributeName] = data.number;
                    }
                );
            }
        }
    }
);