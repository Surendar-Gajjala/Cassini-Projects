/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService'
    ],

    function (module) {
        module.factory('CustomObjectSelector', CustomObjectSelector);

        function CustomObjectSelector(CustomObjectService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: 'Select Custom Object',
                    template: 'app/desktop/modules/select/customObject/customObjectSelectionView.jsp',
                    controller: 'CustomObjectSelectionController as customObjectSelectVm',
                    resolve: 'app/desktop/modules/select/customObject/customObjectSelectionController',
                    width: 600,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.selector.customObject'}
                    ],
                    callback: function (selectedPart) {
                        callback(selectedPart, selectedPart.number);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                CustomObjectService.getCustomObject(id).then(
                    function (data) {
                        attribute.refValueString = data.number;
                    }
                );
            }
        }
    }
);
