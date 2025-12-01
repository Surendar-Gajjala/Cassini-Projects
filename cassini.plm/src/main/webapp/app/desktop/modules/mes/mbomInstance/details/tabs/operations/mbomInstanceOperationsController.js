define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mbomInstanceService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService'
    ],
    function (module) {
        module.controller('MBOMInstanceOperationsController', MBOMInstanceOperationsController);

        function MBOMInstanceOperationsController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal,
                                                  httpFactory, MBOMInstanceService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.mbomInstanceId = $stateParams.mbomInstanceId;

            function loadMBOMInstanceOperations() {
                vm.loading = true;
                MBOMInstanceService.getMBOMInstanceOperations(vm.mbomInstanceId).then(
                    function (data) {
                        vm.mbomInstanceOperations = [];
                        angular.forEach(data, function (item) {
                            item.parentBop = null;
                            item.editMode = false;
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.bopChildren = [];
                            item.isNew = false;
                            vm.mbomInstanceOperations.push(item);
                            var index = vm.mbomInstanceOperations.indexOf(item);
                            index = populateBOPPlanChildren(item, index);
                        });
                        vm.loading = false;
                    }
                )
            }

            function populateBOPPlanChildren(bopPlan, lastIndex) {
                angular.forEach(bopPlan.children, function (item) {
                    lastIndex++;
                    item.parentBop = bopPlan;
                    item.expanded = true;
                    item.editMode = false;
                    item.level = bopPlan.level + 1;
                    item.count = 0;
                    item.bopChildren = [];
                    item.isNew = false;
                    vm.mbomInstanceOperations.splice(lastIndex, 0, item);
                    bopPlan.count = bopPlan.count + 1;
                    bopPlan.expanded = true;
                    bopPlan.bopChildren.push(item);
                    lastIndex = populateBOPPlanChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.togglePlanNode = togglePlanNode;
            function togglePlanNode(bopPlan) {
                if (bopPlan.expanded == null || bopPlan.expanded == undefined) {
                    bopPlan.expanded = false;
                }
                bopPlan.expanded = !bopPlan.expanded;
                var index = vm.mbomInstanceOperations.indexOf(bopPlan);
                if (bopPlan.expanded == false) {
                    removePlanChildren(bopPlan);
                }
                else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    BOPService.getBopPlanChildren(vm.mbomInstanceId, bopPlan.id).then(
                        function (data) {
                            angular.forEach(data, function (item) {
                                item.isNew = false;
                                item.expanded = false;
                                item.editMode = false;
                                item.level = bopPlan.level + 1;
                                item.bopChildren = [];
                                bopPlan.bopChildren.push(item);
                            });

                            angular.forEach(bopPlan.bopChildren, function (item) {
                                index = index + 1;
                                vm.mbomInstanceOperations.splice(index, 0, item);
                            });
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.showMBOMInstanceOperationDetails = showMBOMInstanceOperationDetails;
            function showMBOMInstanceOperationDetails(plan) {
                $state.go('app.mes.mbomInstance.operationDetails', {mbomInstanceId: vm.mbomInstanceId, operationId: plan.id})
            }

            function removePlanChildren(bopPlan) {
                if (bopPlan != null && bopPlan.bopChildren != null && bopPlan.bopChildren != undefined) {
                    angular.forEach(bopPlan.bopChildren, function (item) {
                        removePlanChildren(item);
                    });

                    var index = vm.mbomInstanceOperations.indexOf(bopPlan);
                    vm.mbomInstanceOperations.splice(index + 1, bopPlan.bopChildren.length);
                    bopPlan.bopChildren = [];
                    bopPlan.expanded = false;

                }
            }

            (function () {
                $scope.$on('app.mbomInstance.tabActivated', function (event, args) {
                    if (args.tabId == 'details.operations') {
                        loadMBOMInstanceOperations();
                    }
                });
            })();
        }
    }
)
;



