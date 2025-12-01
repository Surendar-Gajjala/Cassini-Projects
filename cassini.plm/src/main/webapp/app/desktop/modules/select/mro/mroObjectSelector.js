/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.factory('MROObjectSelector', MROObjectSelector);

        function MROObjectSelector(MESObjectTypeService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var objectName = null;
                if (attributeDef.refSubTypeName != null) {
                    objectName = attributeDef.refSubTypeName;
                } else {
                    objectName = 'MRO';
                }
                var options = {
                    title: $rootScope.selectObjectTitle + " " + objectName + " " + $rootScope.objectTitle,
                    template: 'app/desktop/modules/select/mro/mroObjectSelectionView.jsp',
                    controller: 'MROObjectSelectionController as mroObjectSelectVm',
                    resolve: 'app/desktop/modules/select/mro/mroObjectSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.mroObject'}
                    ],
                    callback: function (selectedPart) {
                        callback(selectedPart, selectedPart.number);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                MESObjectTypeService.getMROObject(id).then(
                    function (data) {
                        attribute.refValueString = data.number;
                    }
                );
            }
        }
    }
);
