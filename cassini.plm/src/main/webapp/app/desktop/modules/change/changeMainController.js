define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/qualityTypeService'
    ],
    function (module) {
        module.controller('ChangeMainController', ChangeMainController);

        function ChangeMainController($scope, $rootScope, $timeout, $interval, $state, $cookies, QualityTypeService) {

            $rootScope.viewInfo.icon = "flaticon-contract11";
            $rootScope.viewInfo.title = "Change Management";


            $rootScope.getChangeAttributes = function (objectType, type, attributeIds) {
                QualityTypeService.getAllQualityTypeAttributes(objectType, type).then(
                    function (data) {
                        angular.forEach(data.changeTypeAttributes, function (at1) {
                            attributeIds.push(at1.id);
                        });
                        angular.forEach(data.objectTypeAttributes, function (attri) {
                            if (attri.objectType == 'CHANGE') {
                                attributeIds.push(attri.id);
                            }
                        });
                    });
            };


            (function () {

            })();
        }
    }
);