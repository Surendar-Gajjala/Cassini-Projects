/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.factory('MESObjectSelector', MESObjectSelector);

        function MESObjectSelector(MESObjectTypeService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var objectName = null;
                if (attributeDef.refSubTypeName != null) {
                    objectName = attributeDef.refSubTypeName;
                } else {
                    objectName = 'MES';
                }
                var options = {
                    title: $rootScope.selectObjectTitle + " " + objectName + " " + $rootScope.objectTitle,
                    template: 'app/desktop/modules/select/mes/mesObjectSelectionView.jsp',
                    controller: 'MESObjectSelectionController as mesObjectSelectVm',
                    resolve: 'app/desktop/modules/select/mes/mesObjectSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.mesObject'}
                    ],
                    callback: function (selectedPart) {
                        callback(selectedPart, selectedPart.number);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                MESObjectTypeService.getMESObject(id).then(
                    function (data) {
                        attribute.refValueString = data.number;
                    }
                );
            }
        }
    }
);
