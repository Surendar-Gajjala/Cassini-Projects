/**
 * Created by swapna on 29/01/19.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/core/subContractService'
    ],

    function (module) {
        module.factory('ContractorSelector', ContractorSelector);

        function ContractorSelector(SubContractService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling contractor selector dialog");
                var options = {
                    title: 'Select Contractor',
                    template: 'app/desktop/modules/select/contractor/contractorSelectionView.jsp',
                    controller: 'ContractorSelectionController as contractorSelectVm',
                    resolve: 'app/desktop/modules/select/contractor/contractorSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.contractor.selected'}
                    ],
                    callback: function (selectedContractor) {
                        callback(selectedContractor, selectedContractor.name);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                SubContractService.getContractor(id).then(
                    function (data) {
                        attribute.refValueString = data.name;
                        attribute[attributeName] = data.name;
                    }
                );
            }
        }
    }
);