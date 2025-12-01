define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/workOrderService'
    ],
    function (module) {
        module.controller('WorkOrderResourcesController', WorkOrderResourcesController);

        function WorkOrderResourcesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, DialogService, WorkOrderService) {
            var vm = this;
            vm.workOrderId = $stateParams.workOrderId;

            vm.addResources = addResources;
            vm.saveResources = saveResources;
            vm.deleteResource = deleteResource;
            /* vm.showResource = showResource;*/

            vm.selectedResources = [];

            var emptyresource = {
                id: null,
                workOrder: vm.workOrderId,
                resourceId: null,
                resourceType: null
            }

            vm.workOrderResources = [];
            vm.loading = true;

            function loadWorkOrderResources() {
                vm.loading = false;
                vm.loading = true;
                WorkOrderService.getWorkOrderResources(vm.workOrderId).then(
                    function (data) {
                        vm.workOrderResources = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function addResources() {
                var options = {
                    title: "Add Resources",
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/resources/selectResourcesView.jsp',
                    controller: 'SelectResourceController as selectResourcesVm',
                    resolve: 'app/desktop/modules/mro/workOrder/details/tabs/resources/selectResourcesController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedWorkOrderId: vm.workOrderId,
                        mode: "WORKORDER"
                    },
                    buttons: [
                        {text: "Add", broadcast: 'app.workorder.resources.add'}
                    ],
                    callback: function (result) {
                        vm.workOrderResources = [];
                        vm.selectedResources = [];
                        angular.forEach(result, function (resource) {
                            var part = angular.copy(emptyresource);
                            part.resourceId = resource.id;
                            part.resourceType = resource.objectType;
                            vm.workOrderResources.unshift(part);
                            vm.selectedResources.push(part);
                        });
                        saveResources();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function saveResources() {
                $rootScope.showBusyIndicator();
                WorkOrderService.createMultipleWorkOrderResources(vm.workOrderId, vm.selectedResources).then(
                    function (data) {
                        loadWorkOrderResources();
                        $rootScope.loadWorkOrderTabCounts();
                        vm.selectedResources = [];
                        $rootScope.showSuccessMessage("Resources added successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function deleteResource(part) {
                var options = {
                    title: "Remove Resource",
                    message: "Are you sure you want to remove resource?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            WorkOrderService.deleteWorkOrderResource(vm.workOrderId, part.id).then(
                                function (data) {
                                    loadWorkOrderResources();
                                    $rootScope.loadWorkOrderTabCounts();
                                    $rootScope.showSuccessMessage("Resource removed successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }


            /*function showSparePart(part) {
             $state.go('app.mro.sparePart.details', {sparePartId: part.sparePart.id, tab: 'details.basic'});
             }*/

            (function () {
                $scope.$on('app.workOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.resources') {
                        loadWorkOrderResources();
                    }
                })
            })();
        }
    }
);
