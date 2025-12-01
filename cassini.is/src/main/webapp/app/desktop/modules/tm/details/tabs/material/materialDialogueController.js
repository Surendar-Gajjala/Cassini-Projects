define(['app/desktop/modules/tm/tm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/pm/project/bomService'

    ],
    function (module) {
        module.controller('MaterialDialogueController', MaterialDialogueController);
        function MaterialDialogueController($scope, $rootScope, $timeout, $state, $stateParams, ProjectService, TaskService, BomService) {

            var vm = this;

            vm.selectedMaterials = [];
            vm.materials = [];
            vm.selectedItems = [];
            vm.selectedAll = false;
            vm.showSearchMode = false;
            $scope.freeTextQuery = null;
            vm.projectId = $stateParams.projectId;

            vm.create = create;
            vm.select = select;
            vm.checkAll = checkAll;
            // vm.applyFilters = applyFilters;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilters = clearFilters;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.selectedAll = false;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: 'modifiedBy',
                    order: 'desc'
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.materials = angular.copy(pagedResults);

            function clearFilters() {
                vm.emptyFilters.itemNumber = null;
                vm.emptyFilters.itemName = null;
                vm.emptyFilters.itemType = "MATERIALTYPE";
                vm.selectedMaterials = [];
                vm.clear = false;
                vm.selectedAll = false;
                loadMaterials();
            }

            function nextPage() {
                if (vm.materials.last != true) {
                    vm.pageable.page++;
                    vm.selectedAll = false;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadMaterials();
                    }
                }
            }

            function previousPage() {
                if (vm.materials.first != true) {
                    vm.pageable.page--;
                    vm.selectedAll = false;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadMaterials();
                    }
                }

            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.showSearchMode = false;
            }

            vm.emptyFilters = {
                itemNumber: null,
                itemName: null,
                itemType: "MATERIALTYPE",
                searchQuery: null
            };

            /* function applyFilters() {
             vm.materials = [];
             BomService.getBoqItemsByFilters($stateParams.projectId,vm.pageable,vm.emptyFilters).then(
             function (data) {
             vm.materials = data;
             angular.forEach(resourceMaterials, function (resourceMaterial) {
             angular.forEach(vm.materials.content, function (material) {
             if (material.id == resourceMaterial.referenceId) {
             var index = vm.materials.content.indexOf(material);
             vm.materials.content.splice(index, 1);
             }
             });
             });
             ProjectService.getProjectReferences(vm.materials.content, 'project');
             vm.clear = true;
             });
             }

             */

            function freeTextSearch(freeText) {
                if (!vm.showSearchMode) {
                    vm.pageable.page = 0;
                }
                vm.showSearchMode = true;
                $scope.freeTextQuery = freeText;
                vm.emptyFilters.searchQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.loading = true;
                    vm.selectedAll = false;
                    BomService.getBoqItemsByFilters($stateParams.projectId, $stateParams.taskId, vm.pageable, vm.emptyFilters).then(
                        function (data) {
                            var count = 0;
                            vm.materials = data;
                            vm.loading = false;
                            vm.materials.numberOfElements = (vm.pageable.page + 1) * vm.pageable.size;
                            if (vm.materials.numberOfElements > vm.materials.totalElements) {
                                vm.materials.numberOfElements = vm.materials.totalElements;
                            }
                            angular.forEach(vm.materials.content, function (material) {
                                angular.forEach(vm.selectedMaterials, function (selectedMaterial) {
                                    if (material.itemNumber == selectedMaterial.itemNumber && material.sheet == selectedMaterial.sheet) {
                                        material.selected = true;
                                        count++;
                                    }
                                })
                            });
                            if (vm.materials.content.length > 0 && count == vm.materials.content.length) {
                                vm.selectedAll = true;
                            }
                            vm.clear = true;
                        }, function (error) {
                            vm.loading = false;
                        });
                } else {
                    resetPage();
                    loadMaterials();
                }
            }

            function select(material) {
                var flag = true;
                if (material.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedMaterials.indexOf(material);
                    vm.selectedMaterials.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedMaterials, function (selectMaterial) {
                        if (selectMaterial.id == material.id) {
                            flag = false;
                            var index = vm.selectedMaterials.indexOf(material);
                            vm.selectedMaterials.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedMaterials.push(material);
                    }
                }
                if (vm.selectedMaterials.length == vm.materials.content.length) {
                    vm.selectedAll = true;
                }
            }

            function checkAll() {
                if (vm.selectedAll) {
                    angular.forEach(vm.materials.content, function (material) {
                        if (!material.selected) {
                            material.selected = vm.selectedAll;
                            vm.selectedMaterials.push(material);
                        }
                    });
                } else {
                    angular.forEach(vm.materials.content, function (material) {
                        var index = vm.selectedMaterials.indexOf(material);
                        vm.selectedMaterials.splice(index, 1);
                        material.selected = vm.selectedAll;
                    });
                }

            }

            function create() {
                if (vm.selectedMaterials.length != 0) {
                    vm.materialResources = [];
                    angular.forEach(vm.selectedMaterials, function (material) {
                        var resource = {
                            referenceId: material.id,
                            project: $stateParams.projectId,
                            task: $stateParams.taskId,
                            quantity: 0,
                            units: material.units,
                            resourceType: material.itemType,
                            itemNumber: material.itemNumber
                        };
                        vm.materialResources.push(resource);
                    });
                    TaskService.createProjectResource($stateParams.projectId, vm.materialResources).then(
                        function (data) {
                            $rootScope.hideSidePanel('left');
                            $scope.callback(data);
                            vm.creating = false;
                            vm.selectedMaterials = [];
                            $rootScope.showSuccessMessage("Material(s) added successfully");
                        });
                }

                else {
                    $rootScope.showErrorMessage("Please add  atleast one material");

                }
            }

            function loadMaterials() {
                vm.loading = true;
                BomService.getItemsForTask($stateParams.projectId, $stateParams.taskId, 'MATERIALTYPE', vm.pageable).then(
                    function (data) {
                        var count = 0;
                        vm.materials = data;
                        vm.loading = false;
                        vm.materials.numberOfElements = (vm.pageable.page + 1) * vm.pageable.size;
                        if (vm.materials.numberOfElements > vm.materials.totalElements) {
                            vm.materials.numberOfElements = vm.materials.totalElements;
                        }
                        angular.forEach(vm.materials.content, function (material) {
                            angular.forEach(vm.selectedMaterials, function (selectedMaterial) {
                                if (material.itemNumber == selectedMaterial.itemNumber && material.sheet == selectedMaterial.sheet) {
                                    material.selected = true;
                                    count++;
                                }
                            })
                        });
                        if (count == vm.materials.content.length) {
                            vm.selectedAll = true;
                        }
                    }
                );
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadMaterials();
                    /* $scope.$on('app.stores.movement.freeText', freeTextSearch);*/
                    $rootScope.$on('app.material.new', create);
                }
            })();
        }
    }
);