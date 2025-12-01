define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/operationService',
        'app/shared/services/core/bopService',
        'app/desktop/modules/directives/mesObjectTypeDirective'
    ],
    function (module) {
        module.controller('ResourcesSelectionController', ResourcesSelectionController);

        function ResourcesSelectionController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              $translate, OperationService, BOPService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_RESOURCE_VALIDATION")).html();
            var resourceCreatedMsg = parsed.html($translate.instant("BOP_PLAN_RESOURCE_CREATE_MSG")).html();
            vm.bopPlanId = $stateParams.bopPlanId;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.filters = {
                searchQuery: null,
                number: null,
                name: null,
                type: '',
                typeName: null,
                resource: null
            };
            vm.selectedResources = [];
            vm.loading = false;
            vm.resources = angular.copy(pagedResults);
            vm.selectAllCheck = false;
            vm.selectCheck = selectCheck;
            vm.clearFilter = clearFilter;

            var emptyPlan = {
                id: null,
                bopOperation: vm.bopPlanId,
                operation: null,
                type: null,
                resourceType: null,
                resource: null,
                notes: null
            };

            function selectCheck(mbom) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedResources, function (selectedOperation) {
                    if (selectedOperation.id == mbom.id) {
                        flag = false;
                        var index = vm.selectedResources.indexOf(selectedOperation);
                        if (index != -1) {
                            vm.selectedResources.splice(index, 1);
                            selectedOperationsMap.remove(mbom.id);
                        }
                    }
                });
                if (flag) {
                    vm.selectedResources.push(mbom);
                    selectedOperationsMap.put(mbom.id, mbom);
                }
                var count = 0;
                angular.forEach(vm.resources.content, function (mbom) {
                    if (mbom.selected) {
                        count++;
                    }
                })
                if (count != vm.resources.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.onSelectType = onSelectType;
            function onSelectType(operationType) {
                if (operationType != null && operationType != undefined) {
                    vm.selectedType = operationType;
                    vm.filters.type = operationType.id;
                    vm.filters.typeName = operationType.name;
                    vm.pageable.page = 0;
                    loadResources();
                    vm.clear = true;
                }
            }

            vm.clearSelection = clearSelection;
            function clearSelection() {
                selectedOperationsMap = new Hashtable();
                vm.selectedResources = [];
                vm.selectAllCheck = false;
                angular.forEach(vm.resources.content, function (mbom) {
                    mbom.selected = false;
                })
            }

            function clearFilter() {
                vm.filters.name = null;
                vm.filters.number = null;
                vm.filters.type = '';
                vm.filters.typeName = null;

                vm.selectedType = null;
                $scope.check = false;
                vm.selectAllCheck = false;
                vm.clear = false;
                loadResources();
            }

            vm.selectAll = selectAll;
            function selectAll(check) {
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.resources.content, function (mbom) {
                        mbom.selected = false;
                        var mbomExist = selectedOperationsMap.get(mbom.id);
                        if (mbomExist != null) {
                            var index = vm.selectedResources.indexOf(mbomExist);
                            if (index != -1) {
                                vm.selectedResources.splice(index, 1);
                                selectedOperationsMap.remove(mbom.id);
                            }
                        }
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.resources.content, function (mbom) {
                        mbom.selected = true;
                        var mbomExist = selectedOperationsMap.get(mbom.id);
                        if (mbomExist == null) {
                            vm.selectedResources.push(mbom);
                            selectedOperationsMap.put(mbom.id, mbom);
                        }
                    })
                }
            }

            vm.searchFilterOperations = searchFilterOperations;
            function searchFilterOperations() {
                vm.pageable.page = 0;
                loadResources();
                vm.clear = true;
                vm.selectAllCheck = false;
                if ((vm.filters.name == "" || vm.filters.name == null) && (vm.filters.number == "" || vm.filters.number == null)) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            vm.onOk = onOk;
            function onOk() {
                if (vm.selectedResources.length != 0) {
                    var resources = [];
                    angular.forEach(vm.selectedResources, function (resource) {
                        var bopPlanResource = angular.copy(emptyPlan);
                        bopPlanResource.type = vm.selectedResourceType.resource;
                        bopPlanResource.resourceType = vm.selectedResourceType.resourceType;
                        bopPlanResource.operation = vm.selectedResourceType.operation;
                        bopPlanResource.resource = resource.id;
                        resources.push(bopPlanResource);
                    });
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    BOPService.createMultipleBopPlanResources(vm.bopPlanId, resources).then(
                        function (data) {
                            loadOperationResources();
                            vm.selectedResource = null;
                            vm.selectedResourceType = null;
                            vm.resources = angular.copy(pagedResults);
                            vm.selectAllCheck = false;
                            $scope.callback();
                            $rootScope.showSuccessMessage(resourceCreatedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

                if (vm.selectedResources.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            vm.nextPage = nextPage;
            function nextPage() {
                if (vm.resources.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadResources();
                }
            }

            vm.previousPage = previousPage;
            function previousPage() {
                if (vm.resources.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadResources();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadResources();
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.resources = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadResources();
            }

            vm.onSelectResource = onSelectResource;
            function onSelectResource(resource) {
                vm.selectedResourceType = null;
                vm.resources = angular.copy(pagedResults);
                vm.selectedResources = [];
                if (resource.resourceTypes.length == 1) {
                    vm.selectedResourceType = resource.resourceTypes[0];
                    loadResources();
                }
            }

            var selectedOperationsMap = new Hashtable();
            vm.selectedResource = null;
            vm.selectedResourceType = null;
            vm.loadResources = loadResources;
            function loadResources() {
                vm.loading = true;
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                if (vm.selectedResource != null) {
                    vm.filters.resource = vm.selectedResource.resource;
                }
                vm.filters.type = vm.selectedResourceType.resourceType;
                BOPService.searchBopPlanOperationResources(vm.bopPlanId, vm.pageable, vm.filters).then(
                    function (data) {
                        vm.resources = [];
                        if (vm.filters.resource == "MACHINES") {
                            vm.resources = data.machines;
                        } else if (vm.filters.resource == "EQUIPMENTS") {
                            vm.resources = data.equipments;
                        } else if (vm.filters.resource == "INSTRUMENTS") {
                            vm.resources = data.instruments;
                        } else if (vm.filters.resource == "TOOLS") {
                            vm.resources = data.tools;
                        } else if (vm.filters.resource == "JIGS_FIXTURES") {
                            vm.resources = data.jigsFixtures;
                        } else if (vm.filters.resource == "MATERIALS") {
                            vm.resources = data.materials;
                        } else if (vm.filters.resource == "MANPOWER") {
                            vm.resources = data.manpowers;
                        }
                        var count = 0;
                        angular.forEach(vm.resources.content, function (operation) {
                            operation.selected = false;
                            var selectedItemExist = selectedOperationsMap.get(operation.id);
                            if (selectedItemExist != null) {
                                operation.selected = true;
                                count++;
                            }
                        })
                        if (vm.resources.content.length == count) {
                            vm.selectAllCheck = false;
                        } else {
                            vm.selectAllCheck = false;
                        }
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadOperationResources() {
                OperationService.getOperationResourcesByGroup($rootScope.bopPlan.operation, vm.bopPlanId).then(
                    function (data) {
                        vm.operationResources = data;
                        vm.selectedResources = [];
                        if (vm.operationResources.length == 1) {
                            vm.selectedResource = vm.operationResources[0];
                            onSelectResource(vm.selectedResource);
                        }
                    }
                )
            }

            (function () {
                loadOperationResources();
                $rootScope.$on('app.select.bop.plan.resources', onOk);
            })();
        }
    });