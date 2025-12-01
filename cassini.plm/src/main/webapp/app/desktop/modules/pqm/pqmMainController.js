/**
 * Created by swapna on 1/4/18.
 */

define(['app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/qualityTypeService'
    ],
    function (module) {
        module.controller('PqmMainController', PqmMainController);

        function PqmMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, QualityTypeService) {
            //$rootScope.viewInfo.icon =
            $rootScope.viewInfo.title = "";

            var vm = this;

            $rootScope.getQualityAttributes = function (objectType, type, attributeIds) {
                QualityTypeService.getAllQualityTypeAttributes(objectType, type).then(
                    function (data) {
                        angular.forEach(data.qualityTypeAttributes, function (at1) {
                            attributeIds.push(at1.id);
                        });
                    });
            };

            (function () {

            })();
        }
    }
);