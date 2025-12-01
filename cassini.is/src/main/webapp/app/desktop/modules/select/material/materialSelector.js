/**
 * Created by swapna on 24/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/core/itemService'
    ],

    function (module) {
        module.factory('MaterialSelector', MaterialSelector);

        function MaterialSelector(ItemService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling material selector dialog");
                var options = {
                    title: 'Select Material',
                    template: 'app/desktop/modules/select/material/materialSelectionView.jsp',
                    controller: 'MaterialSelectionController as materialSelectVm',
                    resolve: 'app/desktop/modules/select/material/materialSelectionController',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Add', broadcast: 'app.material.selected'}
                    ],
                    callback: function (selectedMaterial) {
                        callback(selectedMaterial, selectedMaterial.itemNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                ItemService.getMaterialItem(id).then(
                    function (data) {
                        attribute.refValueString = data.itemNumber;
                        attribute[attributeName] = data.itemNumber;
                    }
                );
            }
        }
    }
);