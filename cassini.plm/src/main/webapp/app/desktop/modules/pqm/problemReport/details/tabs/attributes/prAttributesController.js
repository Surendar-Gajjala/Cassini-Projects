define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('PrAttributesController', PrAttributesController);

        function PrAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.problemReportId = $stateParams.problemReportId;

            (function () {
                $scope.$on('app.problemReport.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                });
            })();
        }
    }
)
;