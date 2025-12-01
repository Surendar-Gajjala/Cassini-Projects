/**
 * Created by swapna on 24/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/core/itemService'
    ],

    function (module) {
        module.factory('ManpowerSelector', ManpowerSelector);

        function ManpowerSelector(ItemService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling manpower selector dialog");
                var options = {
                    title: 'Select Manpower',
                    template: 'app/desktop/modules/select/manpower/manpowerSelectionView.jsp',
                    controller: 'ManpowerSelectionController as manpowerSelectVm',
                    resolve: 'app/desktop/modules/select/manpower/manpowerSelectionController',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Add', broadcast: 'app.manpower.selected'}
                    ],
                    callback: function (selectedManpower) {
                        callback(selectedManpower, selectedManpower.itemNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                ItemService.getManpowerItem(id).then(
                    function (data) {
                        attribute.refValueString = data.itemNumber;
                        attribute[attributeName] = data.itemNumber;
                    }
                );
            }
        }
    }
);