define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('QcrAttributesController', QcrAttributesController);

        function QcrAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.qcrId = $stateParams.qcrId;

            (function () {
                $scope.$on('app.qcr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                });
            })();
        }
    }
)
;