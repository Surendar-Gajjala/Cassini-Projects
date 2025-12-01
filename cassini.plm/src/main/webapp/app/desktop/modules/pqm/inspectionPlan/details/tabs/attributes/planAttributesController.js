define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('PlanAttributesController', PlanAttributesController);

        function PlanAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.planId = $stateParams.planId;

            (function () {
                $scope.$on('app.inspectionPlan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                });
            })();
        }
    }
)
;