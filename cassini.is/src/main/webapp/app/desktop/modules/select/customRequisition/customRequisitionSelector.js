/**
 * Created by swapna on 28/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/requisitionService'
    ],

    function (module) {
        module.factory('CustomRequisitionSelector', CustomRequisitionSelector);

        function CustomRequisitionSelector(RequisitionService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling Custom Requisition selector dialog");
                var options = {
                    title: 'Select Custom Requisition',
                    template: 'app/desktop/modules/select/customRequisition/customRequisitionSelectionView.jsp',
                    controller: 'CustomRequisitionSelectionController as customRequisitionSelectVm',
                    resolve: 'app/desktop/modules/select/customRequisition/customRequisitionSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.customRequisition.selected'}
                    ],
                    callback: function (selectedCustomRequisition) {
                        callback(selectedCustomRequisition, selectedCustomRequisition.requisitionNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                RequisitionService.getRequisition(null, id).then(
                    function (data) {
                        attribute.refValueString = data.requisitionNumber;
                        attribute[attributeName] = data.requisitionNumber;
                    }
                );
            }
        }
    }
);