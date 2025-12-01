define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mbomInstanceService',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MBOMInstanceOperationBasicInfoController', MBOMInstanceOperationBasicInfoController);

        function MBOMInstanceOperationBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                            $translate, MBOMInstanceService) {
            var vm = this;
            vm.loading = true;
            vm.operationId = $stateParams.operationId;
            vm.mbomInstanceId = $stateParams.mbomInstanceId;
            vm.mbomOperation = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.loading = false;

            function loadMbomInstanceBasicDetails() {
                vm.loading = true;
                if (vm.operationId != null && vm.operationId != undefined) {
                    MBOMInstanceService.getMbomInstanceOperation(vm.operationId).then(
                        function (data) {
                            vm.mbomOperation = data;
                            $rootScope.mbomOperation = data;
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
                $scope.$on('app.mbomInstance.operation.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadMbomInstanceBasicDetails();
                    }
                });
            })();

        }
    }
);