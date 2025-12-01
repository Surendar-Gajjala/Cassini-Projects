define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/bopService',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('BOPPlanBasicInfoController', BOPPlanBasicInfoController);

        function BOPPlanBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                            $translate, BOPService) {
            var vm = this;
            vm.loading = true;
            vm.bopPlanId = $stateParams.bopPlanId;
            vm.bopId = $stateParams.bopId;
            vm.bopPlan = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.loading = false;

            function loadBopBasicDetails() {
                vm.loading = true;
                if (vm.bopPlanId != null && vm.bopPlanId != undefined) {
                    BOPService.getBopPlan(vm.bopId, vm.bopPlanId).then(
                        function (data) {
                            vm.bopPlan = data;
                            $rootScope.bopPlan = data;
                            /*$timeout(function () {
                             $scope.$broadcast('app.attributes.tabActivated', {});
                             }, 1000);*/
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                $scope.$on('app.bop.plan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadBopBasicDetails();
                    }
                });
            })();

        }
    }
);