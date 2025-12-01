/**
 * Created by swapna on 23/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/shared/services/tm/taskService'
    ],
    function (module) {
        module.controller('NewWorkOrderItemController', NewWorkOrderItemController);

        function NewWorkOrderItemController($scope, $rootScope, $timeout, $state, $cookies, $stateParams,
                                            SubContractService, TaskService) {

            var vm = this;
            vm.workOrder = $scope.data.workOrder;
            var itemsMap = $scope.data.itemsMap;
            vm.tasks = [];
            var workOrderItems = [];
            vm.newWorkOrderItem = {
                task: null,
                workOrder: $stateParams.workOrderId
            };
            vm.select = select;
            vm.checkAll = checkAll;

            function validate() {
                var flag = true;
                if (workOrderItems.length < 1) {
                    flag = false;
                    $rootScope.showErrorMessage("Select atleast one task");
                }
                else {
                    $rootScope.showSuccessMessage("Item added successfully");
                }
                return flag
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator();
                    SubContractService.createWorkOrderItems($stateParams.workOrderId, workOrderItems).then(
                        function (data) {
                            angular.forEach(workOrderItems, function (item) {
                                itemsMap.put(item.task, item);
                            });
                            workOrderItems = [];
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel('right');
                            $scope.callback(workOrderItems);
                        },
                        function (error) {
                            workOrderItems = [];
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel('right');
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function select(task) {
                var flag = true;
                vm.newWorkOrderItem.task = task.id;
                if (task.selected == false) {
                    vm.selectedAll = false;
                    vm.newWorkOrderItem.task = task.id;
                    var index = workOrderItems.indexOf(vm.newWorkOrderItem);
                    workOrderItems.splice(index, 1);
                } else {
                    angular.forEach(workOrderItems, function (item) {
                        if (item.task == task.id) {
                            flag = false;
                            var index = workOrderItems.indexOf(vm.newWorkOrderItem);
                            workOrderItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        workOrderItems.push(vm.newWorkOrderItem);
                    }
                }
                if (workOrderItems.length == vm.tasks.length) {
                    vm.selectedAll = true;
                }
            }

            function checkAll() {
                if (vm.selectedAll) {
                    workOrderItems = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.tasks, function (task) {
                        task.selected = vm.selectedAll;
                        vm.newWorkOrderItem.task = task.id;
                        workOrderItems.push(vm.newWorkOrderItem);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.tasks, function (task) {
                        task.selected = vm.selectedAll;
                        vm.newWorkOrderItem.task = task.id;
                        workOrderItems = [];
                    });
                }
            }

            function loadProjectTasks() {
                TaskService.getContractTasks(vm.workOrder.project).then(
                    function (data) {
                        vm.tasks = data;
                        angular.forEach(vm.tasks, function (task) {
                            if (itemsMap.get(task.id) != null) {
                                var index = vm.tasks.indexOf(task);
                                vm.tasks.splice(index, 1);
                            }
                        })
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.workOrder.newItem', create);
                }
                loadProjectTasks();
            })();
        }
    }
);