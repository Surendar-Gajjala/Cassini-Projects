define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('DCRAttributesController', DCRAttributesController);

        function DCRAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.dcrId = $stateParams.dcrId;

            (function () {
                $scope.$on('app.dcr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
