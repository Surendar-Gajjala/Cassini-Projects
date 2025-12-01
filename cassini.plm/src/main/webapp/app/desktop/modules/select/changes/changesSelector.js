/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/ecoService'
    ],

    function (module) {
        module.factory('ChangeSelector', ChangeSelector);

        function ChangeSelector(ECOService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var objectName = null;
                if (attributeDef.refSubTypeName != null) {
                    objectName = attributeDef.refSubTypeName;
                } else {
                    objectName = 'Change';
                }
                var options = {
                    title: $rootScope.selectObjectTitle + " " + objectName + " " + $rootScope.objectTitle,
                    template: 'app/desktop/modules/select/changes/changeSelectionView.jsp',
                    controller: 'ChangeSelectionController as changeSelectVm',
                    resolve: 'app/desktop/modules/select/changes/changeSelectionController',
                    width: 600,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.change'}
                    ],
                    callback: function (selectedEco) {
                        if (selectedEco.changeType == 'ECO') {
                            callback(selectedEco, selectedEco.ecoNumber);
                        } else if (selectedEco.changeType == 'DCO') {
                            callback(selectedEco, selectedEco.dcoNumber);
                        } else if (selectedEco.changeType == 'ECR' || selectedEco.changeType == 'DCR') {
                            callback(selectedEco, selectedEco.crNumber);
                        } else if (selectedEco.changeType == 'MCO') {
                            callback(selectedEco, selectedEco.mcoNumber);
                        } else if (selectedEco.changeType == 'DEVIATION' || selectedEco.changeType == 'WAIVER') {
                            callback(selectedEco, selectedEco.varianceNumber);
                        }
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                ECOService.getECO(id).then(
                    function (data) {
                        attribute.refValueString = data.ecoNumber;
                    }
                );
            }
        }
    }
);
