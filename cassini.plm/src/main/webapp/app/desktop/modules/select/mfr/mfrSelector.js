/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/mfrService'
    ],

    function (module) {
        module.factory('MfrSelector', MfrSelector);

        function MfrSelector(MfrService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: $rootScope.selectMfrTitle,
                    template: 'app/desktop/modules/select/mfr/mfrSelectionView.jsp',
                    controller: 'MfrSelectionController as mfrSelectVm',
                    resolve: 'app/desktop/modules/select/mfr/mfrSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.mfr'}
                    ],
                    callback: function (selectedMfr) {
                        callback(selectedMfr, selectedMfr.name);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                MfrService.getManufacturer(id).then(
                    function (data) {
                        attribute.refValueString = data.name;
                    }
                );
            }
        }
    }
);
