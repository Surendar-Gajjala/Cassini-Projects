define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/pm/project/bomService',
        'app/desktop/modules/tm/details/tabs/material/materialDialogueController',
        'app/shared/services/store/topStockMovementService'
    ],
    function (module) {
        module.controller('MaterialTaskController', MaterialTaskController);

        function MaterialTaskController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                        TaskService, $window, BomService, ProjectService, DialogService, TopStockMovementService) {
            var vm = this;

            vm.updateMaterials = updateMaterials;
            vm.deleteMaterialResource = deleteMaterialResource;
            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            vm.resourceMaterials = [];
            var referenceIds = [];
            vm.loading = false;

            function loadMaterialResources() {
                vm.loading = true;
                TaskService.getProjectResourcesByType(vm.projectId, vm.taskId, 'MATERIALTYPE').then(
                    function (data) {
                        vm.resourceMaterials = data;
                        BomService.getBoqItemReferences(vm.projectId, vm.resourceMaterials, 'referenceId');
                        $timeout(function () {
                            loadResourcesQuantity();
                            vm.loading = false;
                        }, 1000);
                    }
                );
            }

            function loadResourcesQuantity() {
                angular.forEach(vm.resourceMaterials, function (material) {
                    referenceIds.push(material.referenceId);
                });
                TaskService.getItemAvailableQuantities(vm.projectId, referenceIds).then(
                    function (data) {
                        angular.forEach(vm.resourceMaterials, function (material) {
                            material.assignedQty = data[material.referenceId];
                            material.balanceQty = material.referenceIdObject.quantity - material.assignedQty;
                        })
                    })
            }

            function validateQuantity(data, material) {
                var valid = false;
                if (material.balanceQty >= data) {
                    valid = true;
                }
                return valid;
            }

            function updateMaterials(data, material) {
                if (data > 0) {
                    if (validateQuantity(data, material)) {
                        var resource = {
                            id: material.id,
                            referenceId: material.referenceId,
                            project: $stateParams.projectId,
                            task: vm.taskId,
                            quantity: data,
                            units: material.units,
                            resourceType: "MATERIALTYPE",
                            itemNumber: material.itemNumber
                        };

                        TaskService.updateResource(vm.projectId, resource).then(
                            function (data) {
                                loadMaterialResources();
                                $rootScope.showSuccessMessage("Material qty updated successfully");
                            });
                    } else {
                        return "Material qty cannot be greater than balance qty";
                    }
                } else {
                    return "Please enter +ve number";
                }

            }

            function deleteMaterialResource(material) {
                TopStockMovementService.getStockMovementByBomItemAndItemNum(material.referenceIdObject.itemNumber, material.referenceId).then(
                    function (stockMovements) {
                        var x = 0;
                        angular.forEach(stockMovements, function (stockMovement) {
                            if (stockMovement.movementType == "ISSUED") {
                                x++;
                            }
                        });
                        if (x > 0) {
                            $rootScope.showErrorMessage("This material is already issued! We cannot delete this material")
                        }
                        else {
                            var options = {
                                title: 'Remove Material',
                                message: 'Are you sure you want to remove this Material?',
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ProjectService.deleteResource(vm.projectId, material.id).then(
                                        function (data) {
                                            var index = vm.resourceMaterials.indexOf(material);
                                            vm.resourceMaterials.splice(index, 1);
                                            $rootScope.showSuccessMessage("Material Removed successfully");
                                            loadMaterialResources();
                                            $rootScope.loadDetailsCount();
                                        }
                                    )
                                }
                            });
                        }
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.task.tabactivated', function (event, data) {
                        if (data.tabId == 'details.material') {
                            loadMaterialResources();
                        }
                    });
                    $scope.$on('app.task.addMaterial', function () {
                        var options = {
                            title: 'Select Material(s)',
                            side: 'left',
                            showMask: true,
                            template: 'app/desktop/modules/tm/details/tabs/material/materialDialogueView.jsp',
                            controller: 'MaterialDialogueController as materialDialogueVm',
                            resolve: 'app/desktop/modules/tm/details/tabs/material/materialDialogueController',
                            width: 700,
                            data: {
                                resourceMaterials: vm.resourceMaterials
                            },
                            buttons: [
                                {text: 'Add', broadcast: 'app.material.new'}
                            ],

                            callback: function () {
                                loadMaterialResources();
                                $rootScope.loadDetailsCount();
                            }
                        };
                        $rootScope.showSidePanel(options);
                    });
                }
            })();
        }
    }
)
;
