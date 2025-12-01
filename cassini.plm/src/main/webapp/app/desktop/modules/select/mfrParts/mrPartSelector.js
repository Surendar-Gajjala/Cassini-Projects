/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/mfrPartsService'
    ],

    function (module) {
        module.factory('MfrPartSelector', MfrPartSelector);

        function MfrPartSelector(MfrPartsService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: $rootScope.selectMfrPartTitle,
                    template: 'app/desktop/modules/select/mfrParts/mfrPartSelectionView.jsp',
                    controller: 'MfrPartSelectionController as mfrPartSelectVm',
                    resolve: 'app/desktop/modules/select/mfrParts/mfrPartSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.mfrPart'}
                    ],
                    callback: function (selectedPart) {
                        callback(selectedPart, selectedPart.partName);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                MfrPartsService.getManufacturepart(id).then(
                    function (data) {
                        attribute.refValueString = data.partNumber;
                    }
                );
            }
        }
    }
);
